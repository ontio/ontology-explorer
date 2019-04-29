package com.github.ontio.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.ontio.blocksync.config.ConfigParam;
import com.github.ontio.dao.*;
import com.github.ontio.model.AddressSummary;
import com.github.ontio.model.ContractSummary;
import com.github.ontio.model.Contracts;
import com.github.ontio.model.DailySummary;
import com.github.ontio.paramBean.Result;
import com.github.ontio.service.ISummaryService;
import com.github.ontio.util.*;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

/**
 * @author king
 * @version 1.0
 * @date 2018/12/14
 */
@Slf4j
@Service("SummaryService")
@MapperScan("com.github.ontio.dao")
public class SummaryServiceImpl implements ISummaryService {

    private static final String VERSION = "1.0";

    private static final Integer NUM_7 = 7;

    private static final Integer NUM_30 = 30;

    private static final String CLASS_NAME = SummaryServiceImpl.class.getSimpleName();

    private static final String ONTCONTRACT = "0100000000000000000000000000000000000000";

    private static final String ONGCONTRACT = "0200000000000000000000000000000000000000";

    @Autowired
    private BlockMapper blockMapper;

    @Autowired
    private AddressSummaryMapper addressSummaryMapper;

    @Autowired
    private TransactionDetailMapper transactionDetailMapper;

    @Autowired
    private TransactionDetailTmpMapper transactionDetailTmpMapper;

    @Autowired
    private DailySummaryMapper dailySummaryMapper;

    @Autowired
    private OntIdMapper ontIdMapper;

    @Autowired
    private ContractsMapper contractsMapper;

    @Autowired
    private ContractSummaryMapper contractSummaryMapper;

    @Autowired
    private CurrentMapper currentMapper;

    @Autowired
    private ConfigParam configParam;

    private OntologySDKService sdk;

    private synchronized void initSDK() {
        if (sdk == null) {
            sdk = OntologySDKService.getInstance(configParam);
        }
    }

    /**
     * 查询所有
     *
     * @param amount
     * @return
     */
    @Override
    public Result querySummary(int amount) {
        log.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        Map<String, Object> resultMap = new HashMap();

        // 节点数
        initSDK();
        resultMap.put("NodeCount", Integer.parseInt(configParam.SDK_NODE_COUNT));

        // 区块高度、交易总数、OntId总数
        Map summary = currentMapper.selectSummaryInfo();
        resultMap.put("CurrentHeight", summary.get("Height"));
        resultMap.put("TxnCount", summary.get("TxnCount"));
        resultMap.put("OntIdCount", summary.get("OntIdCount"));

        // 地址总数
        Integer addressCount = addressSummaryMapper.selectAllAddressCount();
        resultMap.put("AddressCount", addressCount);

        // 最新的5个区块
        List<Map> blockList = blockMapper.selectBlockByPage(0, amount);
        resultMap.put("BlockList", blockList);

        // 最新的5个OntId
        List<Map> ontIdList = ontIdMapper.selectOntIdByPage(0, amount);
        for (Map map : ontIdList) {
            map.put("Description", Helper.templateOntIdOperation((String) map.get("Description")));
            map.put("Fee", ((BigDecimal) map.get("Fee")).toPlainString());
        }
        resultMap.put("OntIdList", ontIdList);

        // 最新的5笔交易
        List<Map> txnList = transactionDetailMapper.selectTxnWithoutOntId(0, amount);
        for (Map map : txnList) {
            map.put("Fee", ((BigDecimal) map.get("Fee")).toPlainString());
        }
        resultMap.put("TxnList", txnList);

        // TPS
        resultMap.put("CurrentTps", queryCurrentTps());
        resultMap.put("MaxTps", 10000);

        return Helper.result("QuerySummary", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, resultMap);
    }

    /**
     * 更新所有每日统计信息，更新daily_summary，contract_summary，address_summary
     *
     * @return
     */
    @Override
    public Result summaryAllInfo() {

        log.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        if (!configParam.EXPLORER_DAILY_SCHEDULE.equalsIgnoreCase("true")) {
            return Helper.result("SummaryAllInfo", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, null);
        }

        Integer oneDayTime = 24 * 60 * 60;
        //两天前的0点。若现在是3.28号0:15分，要统计3.27号的数据，twoDayAgo0HourTime是3.26号0点
        Integer twoDayAgo0HourTime = dailySummaryMapper.selectMaxTime();
        Integer oneDayAgo0HourTime = 0;
        if (twoDayAgo0HourTime == null) {
            // 创世区块的时间： UTC:2018-06-30 00：00：00    时间戳：1530316800
            oneDayAgo0HourTime = 1530316800;
        } else {
            //一天前的0点，即3.27号的0点
            oneDayAgo0HourTime = twoDayAgo0HourTime + oneDayTime;
        }
        //最新区块时间，正常是大于3.28号的0：15的
        Integer maxTime = blockMapper.selectBlockMaxTime();
        Map paramMap = new HashMap<>();
        //正常每天只会跑一次，while用于跑历史统计数据
        while (maxTime > (oneDayAgo0HourTime + oneDayTime)) {
            paramMap.put("startTime", oneDayAgo0HourTime);
            paramMap.put("endTime", oneDayAgo0HourTime + oneDayTime);

            // 每日新增区块数
            int dailyBlockCount = blockMapper.selectBlockCountInOneDay(oneDayAgo0HourTime, oneDayAgo0HourTime + oneDayTime);
            // 每日新增ontid数
            int dailyOntIdCount = ontIdMapper.selectOntIdCountInOneDay(oneDayAgo0HourTime, oneDayAgo0HourTime + oneDayTime, "Register%");
            // 每日活跃ontid数
            int dailyActiveOntIdCount = ontIdMapper.selectActiveOntIdCountInOneDay(oneDayAgo0HourTime, oneDayAgo0HourTime + oneDayTime);

            // 先删除temp表所有数据，再将前一天的数据迁移到temp表，基于temp表做各种信息统计
            transactionDetailTmpMapper.deleteAll();
            transactionDetailTmpMapper.InsertSelectiveFromDetailTable(paramMap);
            //在temp表进行每日其他数据统计
            setDailySummaryInfoFromTempTable(oneDayAgo0HourTime, dailyBlockCount, dailyOntIdCount, dailyActiveOntIdCount);

            oneDayAgo0HourTime = oneDayAgo0HourTime + oneDayTime;
        }

        return Helper.result("SummaryAllInfo", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, null);
    }

    /**
     * 从temp表进行每日各种数据统计
     *
     * @param oneDayAgo0HourTime
     * @param dailyBlockCount
     * @param dailyNewOntidCount
     * @param dailyActiveOntidCount
     */
    private void setDailySummaryInfoFromTempTable(Integer oneDayAgo0HourTime, Integer dailyBlockCount, Integer dailyNewOntidCount, Integer dailyActiveOntidCount) {

        // 每日交易数
        int dailyTxnCount = transactionDetailTmpMapper.selectTxnCountInOneDay();
        // 每日交易ont，ong量
        BigDecimal dailyOntAmount = transactionDetailTmpMapper.selectOntAmountInOneDay();
        BigDecimal dailyOngAmount = transactionDetailTmpMapper.selectOngAmountInOneDay();

        //每日活跃地址
        List<String> dailyActiveAddrList = transactionDetailTmpMapper.selectAddressInOneDay();
        int dailyActiveAddrCount = dailyActiveAddrList.size();

        // 每日新增地址数=每日活跃地址-address_summary表所有地址
        List<String> allAddrList = addressSummaryMapper.selectDistinctAddress();
        dailyActiveAddrList.removeAll(allAddrList);
        int dailyNewAddrCount = dailyActiveAddrList.size();

        // 更新合约的信息
        List<AddressSummary> contractAddressSummarys = updateDailyContractInfoAndContractNewAddress(oneDayAgo0HourTime);

        // 新增地址插入address_summary表
        batchInsertNewAddress(dailyActiveAddrList, oneDayAgo0HourTime, contractAddressSummarys);

        DailySummary dailySummary = new DailySummary();
        dailySummary.setTime(oneDayAgo0HourTime);
        dailySummary.setBlockcount(dailyBlockCount);
        dailySummary.setTxncount(dailyTxnCount);
        dailySummary.setOntidactivecount(dailyActiveOntidCount);
        dailySummary.setOntidnewcount(dailyNewOntidCount);
        dailySummary.setOntcount(dailyOntAmount == null ? new BigDecimal(0) : dailyOntAmount);
        dailySummary.setOngcount(dailyOngAmount == null ? new BigDecimal(0) : dailyOngAmount.divide(ConstantParam.ONG_TOTAL));
        dailySummary.setActiveaddress(dailyActiveAddrCount);
        dailySummary.setNewaddress(dailyNewAddrCount);

        dailySummaryMapper.insert(dailySummary);
    }

    /**
     * 每天所有新增地址和每天每个合约新地址插入address_summary表
     *
     * @param addressList
     * @param oneDayAgo0HourTime
     * @param contractAddressSummarys
     * @return
     */
    private void batchInsertNewAddress(List<String> addressList, Integer oneDayAgo0HourTime, List<AddressSummary> contractAddressSummarys) {

        List<AddressSummary> addressSummarys = new ArrayList<>();
        for (String address :
                addressList) {
            AddressSummary addressSummary = new AddressSummary();
            addressSummary.setTime(oneDayAgo0HourTime);
            addressSummary.setType("common");
            addressSummary.setAddress(address);
            addressSummarys.add(addressSummary);
        }

        addressSummarys.addAll(contractAddressSummarys);
        if (!addressSummarys.isEmpty()) {
            addressSummaryMapper.banchInsertSelective(addressSummarys);
        }
    }

    /**
     * 更新contracts_summary表里的每个合约的统计数据，并记录每个合约的新地址
     *
     * @param oneDayAgo0HourTime 前一天的UTC0点的时间戳
     * @return
     */
    public List<AddressSummary> updateDailyContractInfoAndContractNewAddress(Integer oneDayAgo0HourTime) {
        //查询所有合约，不管是否审核过
        List<Contracts> contractList = contractsMapper.selectAllContract();
        if (contractList.isEmpty()) {
            return new ArrayList<>();
        }
        //所有dappstore里的dapp的交易总量
        int allDappTxnCount = 0;
        //所有dappstore里的dapp的活跃地址总数
        int allDappActiveAddrCount = 0;

        List<ContractSummary> dappStoreContractSummaryList = new ArrayList<>();
        List<ContractSummary> notDappStorecontractSummaryList = new ArrayList<>();
        List<AddressSummary> contractAddressSummarys = new ArrayList<>();
        //记录每个合约当天的统计数据，并将每个合约的新地址插入地址汇总表
        for (Contracts contracts :
                contractList) {
            String contractHash = contracts.getContract();
            //String contractAddress = Address.parse(com.github.ontio.common.Helper.reverse(contractHash)).toBase58();

            BigDecimal dailyOntAmount = transactionDetailTmpMapper.selectContractAssetAmount(contractHash, ConstantParam.ONT);
            dailyOntAmount = dailyOntAmount == null ? new BigDecimal(0) : dailyOntAmount;

            BigDecimal dailyOngAmount = transactionDetailTmpMapper.selectContractAssetAmount(contractHash, ConstantParam.ONG);
            dailyOngAmount = dailyOngAmount == null ? new BigDecimal(0) : dailyOngAmount;

            // 依据合约hash查询交易数
            int dailyTxnCount = transactionDetailTmpMapper.selectContractTxnCount(contractHash);
            // 合约活跃地址列表(payer+fromaddress去重)
            List<String> dailyActiveAddrList = transactionDetailTmpMapper.selectContractAddr(contractHash);
            int dailyActiveAddrCount = dailyActiveAddrList.size();

            // 新增地址数=每日合约活跃地址数-address_summary所有地址
            List<String> allAddrList = addressSummaryMapper.selectDistinctAddressByContract(contractHash);
            dailyActiveAddrList.removeAll(allAddrList);
            int dailyNewAddrCount = dailyActiveAddrList.size();

            //更新contract_summary表当天的合约统计信息
            if (contracts.getDappstoreflag() == ConstantParam.DAPPSTOREFLAG_YES) {
                allDappTxnCount += dailyTxnCount;
                allDappActiveAddrCount += dailyActiveAddrCount;

                ContractSummary contractSummary = new ContractSummary();
                contractSummary.setTime(oneDayAgo0HourTime);
                contractSummary.setProject(contracts.getProject());
                contractSummary.setContracthash(contractHash);
                contractSummary.setTxncount(dailyTxnCount);
                contractSummary.setOntcount(dailyOntAmount);
                contractSummary.setOngcount(dailyOngAmount.divide(ConstantParam.ONG_TOTAL));
                contractSummary.setActiveaddress(dailyActiveAddrCount);
                contractSummary.setNewaddress(dailyNewAddrCount);
                contractSummary.setScore(0);
                contractSummary.setOngreward(ConstantParam.ZERO);
                contractSummary.setOntreward(ConstantParam.ZERO);
                dappStoreContractSummaryList.add(contractSummary);
            } else {
                ContractSummary contractSummary = new ContractSummary();
                contractSummary.setTime(oneDayAgo0HourTime);
                contractSummary.setProject(contracts.getProject());
                contractSummary.setContracthash(contractHash);
                contractSummary.setTxncount(dailyTxnCount);
                contractSummary.setOntcount(dailyOntAmount);
                contractSummary.setOngcount(dailyOngAmount.divide(ConstantParam.ONG_TOTAL));
                contractSummary.setActiveaddress(dailyActiveAddrCount);
                contractSummary.setNewaddress(dailyNewAddrCount);
                contractSummary.setScore(0);
                contractSummary.setOngreward(ConstantParam.ZERO);
                contractSummary.setOntreward(ConstantParam.ZERO);
                notDappStorecontractSummaryList.add(contractSummary);
            }
            //新增地址放到列表中，在外面再插入address_summary表
            for (String address :
                    dailyActiveAddrList) {
                AddressSummary addressSummary = new AddressSummary();
                addressSummary.setTime(oneDayAgo0HourTime);
                addressSummary.setType(contractHash);
                addressSummary.setAddress(address);
                contractAddressSummarys.add(addressSummary);
            }
        }

        if (dappStoreContractSummaryList.size() > 0) {
            handleDappStoreContractSummaryInfo(dappStoreContractSummaryList, allDappTxnCount, allDappActiveAddrCount);
        }

        List<ContractSummary> allContractSummaryList = new ArrayList<>();
        allContractSummaryList.addAll(dappStoreContractSummaryList);
        allContractSummaryList.addAll(notDappStorecontractSummaryList);
        contractSummaryMapper.banchInsertSelective(allContractSummaryList);

        return contractAddressSummarys;
    }

    /**
     * 处理DappStore里的合约的日得分
     *
     * @param dappStoreContractSummaryList
     * @param allDappTxnCount
     * @param allDappActiveAddrCount
     */
    private void handleDappStoreContractSummaryInfo(List<ContractSummary> dappStoreContractSummaryList, int allDappTxnCount, int allDappActiveAddrCount) {
        try {
            //计算每日得分
            for (ContractSummary contractSummary :
                    dappStoreContractSummaryList) {
                int txnCount = contractSummary.getTxncount();
                int activeAddrCount = contractSummary.getActiveaddress();
                int score = calculateDailyScore(txnCount, activeAddrCount, allDappTxnCount, allDappActiveAddrCount);
                contractSummary.setScore(score);
            }
        } catch (Exception e) {
            log.error("calculate contract daily score error...", e);
        }

    }


    /**
     * 计算合约的每日或者每周得分
     *
     * @param txnCount
     * @param activeAddrCount
     * @param allTxnCount
     * @param allActiveAddrCount
     * @return
     */
    private int calculateDailyScore(int txnCount, int activeAddrCount, int allTxnCount, int allActiveAddrCount) {
        if (txnCount == 0 || activeAddrCount == 0 || allTxnCount == 0 || activeAddrCount == 0) {
            return 0;
        }

        BigDecimal a = new BigDecimal(txnCount).divide(new BigDecimal(activeAddrCount), 2, ROUND_HALF_DOWN);
        BigDecimal b = new BigDecimal(allTxnCount).divide(new BigDecimal(allActiveAddrCount), 2, ROUND_HALF_DOWN);
        BigDecimal x = new BigDecimal("1.5").multiply(new BigDecimal("2")).multiply(a.divide(b, 2, ROUND_HALF_DOWN));
        double exp = -x.divide(new BigDecimal("2")).doubleValue();

        BigDecimal sapp = x.multiply(new BigDecimal(Math.exp(exp)));
        BigDecimal appScore = sapp.multiply(new BigDecimal(txnCount));
        return appScore.setScale(0, BigDecimal.ROUND_HALF_DOWN).intValue();
    }

    /**
     * 计算合约的每日ong激励
     *
     * @param dappScore
     * @param allDappScore
     * @return
     */
    private BigDecimal calculateDailyOngReward(int dappScore, int allDappScore) {
        if (dappScore == 0 || allDappScore == 0) {
            return ConstantParam.ZERO;
        }

        BigDecimal a = new BigDecimal(dappScore).divide(new BigDecimal(allDappScore), 2, ROUND_HALF_DOWN);
        BigDecimal ongreward = configParam.ONGREWARD_DAILY.divide(new BigDecimal("5"), 2, ROUND_HALF_DOWN).multiply(a);
        return ongreward;
    }

    /**
     * 计算合约的每周ont激励
     *
     * @param weekScore
     * @param allWeekScore
     * @return
     */
    private BigDecimal calculateWeekOntReward(int weekScore, int allWeekScore) {

        BigDecimal ontWeekReward = configParam.ONTREWARD_WEEK.divide(new BigDecimal("5"), 2, ROUND_HALF_DOWN).multiply(new BigDecimal(weekScore).divide(new BigDecimal(allWeekScore), 2, ROUND_HALF_DOWN));
        return ontWeekReward;
    }


    /**
     * Tps Info
     *
     * @return
     */
    @Override
    public Result queryTps() {
        log.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("CurrentTps", queryCurrentTps());
        resultMap.put("MaxTps", 10000);

        return Helper.result("QueryTps", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, resultMap);
    }

    private String queryCurrentTps() {
        int nowTime = (int) (System.currentTimeMillis() / 1000);

        Map<String, Object> paramMap = new HashMap();
        paramMap.put("startTime", nowTime - 60);
        paramMap.put("endTime", nowTime);
        Integer tpsPerMin = transactionDetailMapper.queryTransactionCount(paramMap);

        DecimalFormat df = new DecimalFormat("0.00");
        return df.format((double) (tpsPerMin) / 60);
    }

    /**
     * Daily Info
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Result querySummary(String type, int startTime, int endTime) {
        log.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        Map<String, Object> paramMap = new HashMap();
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        List<Map> dailyList = dailySummaryMapper.selectDailyInfo(paramMap);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        Map<String, Object> addressAndOntIdCount = dailySummaryMapper.selectAddressAndOntIdCount(startTime);
        switch (type) {
            case "weekly":
                dailyList = querySummaryInfo(dailyList, simpleDateFormat, NUM_7);
                Collections.reverse(dailyList);
                break;
            case "monthly":
                dailyList = querySummaryInfo(dailyList, simpleDateFormat, NUM_30);
                Collections.reverse(dailyList);
                break;
            default:
                Collections.reverse(dailyList);
                dailyList = querySummaryInfo(dailyList, simpleDateFormat, addressAndOntIdCount);
                break;
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("SummaryList", dailyList);
        resultMap.put("Total", dailyList.size());

        return Helper.result("QuerySummary", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, resultMap);
    }

    private List<Map> querySummaryInfo(List<Map> dailyList, SimpleDateFormat simpleDateFormat, Integer count) {
        List<Map> resultMapList = new ArrayList<>();
        if (dailyList.size() < count) {
            return resultMapList;
        }

        int weekCount = dailyList.size() / count;
        for (int i = 0; i < weekCount; i++) {
            Map<String, Object> result = dailyList.get(i * count);
            int time = (Integer) result.get("Time");
            result.put("Time", simpleDateFormat.format((long) time * 1000));
            for (int j = 1; j < count; j++) {
                Map<String, Object> perMap = dailyList.get(i * count + j);
                result.put("TxnCount", (Integer) result.get("TxnCount") + (Integer) perMap.get("TxnCount"));
                result.put("ActiveAddress", (Integer) result.get("ActiveAddress") + (Integer) perMap.get("ActiveAddress"));
                result.put("BlockCount", (Integer) result.get("BlockCount") + (Integer) perMap.get("BlockCount"));
                result.put("OntIdNewCount", (Integer) result.get("OntIdNewCount") + (Integer) perMap.get("OntIdNewCount"));
                result.put("OntIdActiveCount", (Integer) result.get("OntIdActiveCount") + (Integer) perMap.get("OntIdActiveCount"));
                result.put("NewAddress", (Integer) result.get("NewAddress") + (Integer) perMap.get("NewAddress"));
                result.put("OntCount", ((BigDecimal) result.get("OntCount")).add((BigDecimal) perMap.get("OntCount")));
                result.put("OngCount", ((BigDecimal) result.get("OngCount")).add((BigDecimal) perMap.get("OngCount")));
            }

            result.put("OntCount", ((BigDecimal) result.get("OntCount")).toPlainString());
            result.put("OngCount", ((BigDecimal) result.get("OngCount")).toPlainString());
            resultMapList.add(result);
        }

        return resultMapList;
    }

    private List<Map> querySummaryInfo(List<Map> dailyList, SimpleDateFormat simpleDateFormat, Map<String, Object> addressAndOntIdCount) {
        int addressCount = 0;
        int ontIdCount = 0;
        if (addressAndOntIdCount != null && addressAndOntIdCount.get("addressSum") != null) {
            addressCount = Integer.parseInt(addressAndOntIdCount.get("addressSum").toString());
        }
        if (addressAndOntIdCount != null && addressAndOntIdCount.get("ontIdSum") != null) {
            ontIdCount = Integer.parseInt(addressAndOntIdCount.get("ontIdSum").toString());
        }

        for (Map map : dailyList) {
            int time = (Integer) map.get("Time");
            map.put("Time", simpleDateFormat.format((long) time * 1000));
            map.put("OntCount", ((BigDecimal) map.get("OntCount")).toPlainString());
            map.put("OngCount", ((BigDecimal) map.get("OngCount")).toPlainString());
            addressCount += (Integer) map.get("NewAddress");
            ontIdCount += (Integer) map.get("OntIdNewCount");
            map.put("AddressSum", addressCount);
            map.put("OntIdSum", ontIdCount);

        }

        return dailyList;
    }

    /**
     * Contract Info
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Result queryContract(String contractHash, String type, int startTime, int endTime) {
        log.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        Map<String, Object> resultMap = new HashMap();
        resultMap.put("SummaryList", new ArrayList<>());
        resultMap.put("TxCountSum", 0);
        resultMap.put("AddressSum", 0);
        resultMap.put("OntCountSum", new BigDecimal(0).toPlainString());
        resultMap.put("OngCountSum", new BigDecimal(0).toPlainString());
        resultMap.put("Total", 0);

        Contracts contracts = contractsMapper.selectContractByContractHash(contractHash);
        if (contracts == null) {
            return Helper.result("QueryContract", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, resultMap);
        }

        Map<String, Object> paramMap = new HashMap();
        paramMap.put("contractHash", contractHash);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        List<Map> dailyList = contractSummaryMapper.selectDailySummaryByContractHash(paramMap);

        // 按照type进行统计
        dailyList = getContractListMap(dailyList, type);

        resultMap.put("TxCountSum", contracts.getTxcount());
        resultMap.put("AddressSum", contracts.getAddresscount());
        resultMap.put("OntCountSum", contracts.getOntcount().toPlainString());
        resultMap.put("OngCountSum", contracts.getOngcount().toPlainString());
        resultMap.put("SummaryList", dailyList);
        resultMap.put("Total", dailyList.size());

        return Helper.result("QueryContract", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, resultMap);
    }

    private List<Map> queryContract(List<Map> dailyList, SimpleDateFormat simpleDateFormat) {
        for (Map map : dailyList) {
            int time = (Integer) map.get("Time");
            map.put("Time", simpleDateFormat.format((long) time * 1000));
            map.put("OntCount", ((BigDecimal) map.get("OntCount")).toPlainString());
            map.put("OngCount", ((BigDecimal) map.get("OngCount")).toPlainString());
        }

        return dailyList;
    }

    private List<Map> queryContract(List<Map> dailyList, SimpleDateFormat simpleDateFormat, int count) {
        List<Map> resultMapList = new ArrayList<>();
        if (dailyList.size() < count) {
            return resultMapList;
        }

        int weekCount = dailyList.size() / count;
        for (int i = 0; i < weekCount; i++) {
            Map<String, Object> result = dailyList.get(i * count);
            int time = (Integer) result.get("Time");
            result.put("Time", simpleDateFormat.format((long) time * 1000));
            for (int j = 1; j < count; j++) {
                Map<String, Object> perMap = dailyList.get(i * count + j);
                result.put("TxnCount", (Integer) result.get("TxnCount") + (Integer) perMap.get("TxnCount"));
                result.put("ActiveAddress", (Integer) result.get("ActiveAddress") + (Integer) perMap.get("ActiveAddress"));
                result.put("NewAddress", (Integer) result.get("NewAddress") + (Integer) perMap.get("NewAddress"));
                result.put("OntCount", ((BigDecimal) result.get("OntCount")).add((BigDecimal) perMap.get("OntCount")));
                result.put("OngCount", ((BigDecimal) result.get("OngCount")).add((BigDecimal) perMap.get("OngCount")));
            }
            result.put("OntCount", ((BigDecimal) result.get("OntCount")).toPlainString());
            result.put("OngCount", ((BigDecimal) result.get("OngCount")).toPlainString());

            resultMapList.add(result);
        }

        return resultMapList;
    }

    private List<Map> getContractListMap(List<Map> dailyList, String type) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        switch (type) {
            case "weekly":
                dailyList = queryContract(dailyList, simpleDateFormat, NUM_7);
                break;
            case "monthly":
                dailyList = queryContract(dailyList, simpleDateFormat, NUM_30);
                break;
            default:
                dailyList = queryContract(dailyList, simpleDateFormat);
                break;
        }
        Collections.reverse(dailyList);

        return dailyList;
    }

    /**
     * project Info
     *
     * @param project
     * @param type
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Result queryProjectInfo(String project, String type, int startTime, int endTime) {
        log.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        List<Contracts> contractsList = contractsMapper.selectAllContractByProject(project);
        Map<String, Object> resultMap = new HashMap();
        resultMap.put("SummaryList", new ArrayList<>());
        resultMap.put("TxCountSum", 0);
        resultMap.put("AddressSum", 0);
        resultMap.put("OntCountSum", new BigDecimal(0).toPlainString());
        resultMap.put("OngCountSum", new BigDecimal(0).toPlainString());
        resultMap.put("Total", 0);

        if (contractsList.isEmpty()) {
            return Helper.result("QueryProject", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, resultMap);
        }

        // 每天的统计
        Map<Integer, Map<String, Object>> projectMap = new HashMap<>();
        List<Map> projectSummaryList0 = new ArrayList<>();
        List<String> contracts = new ArrayList<>();
        for (Contracts contract : contractsList) {
            contracts.add(contract.getContract());
            Map<String, Object> paramMap = new HashMap();
            paramMap.put("contractHash", contract.getContract());
            paramMap.put("startTime", startTime);
            paramMap.put("endTime", endTime);
            List<Map> dailyList = contractSummaryMapper.selectDailySummaryByContractHash(paramMap);

            int time = 0;
            for (Map map : dailyList) {
                time = (int) map.get("Time");

                Map<String, Object> perMap = projectMap.get(time);
                if (perMap == null) {
                    projectMap.put(time, map);
                    projectSummaryList0.add(map);
                } else {
                    perMap.put("TxnCount", (Integer) perMap.get("TxnCount") + (Integer) map.get("TxnCount"));
                    perMap.put("ActiveAddress", (Integer) perMap.get("ActiveAddress") + (Integer) map.get("ActiveAddress"));
                    perMap.put("NewAddress", (Integer) perMap.get("NewAddress") + (Integer) map.get("NewAddress"));
                    perMap.put("OntCount", ((BigDecimal) perMap.get("OntCount")).add((BigDecimal) map.get("OntCount")));
                    perMap.put("OngCount", ((BigDecimal) perMap.get("OngCount")).add((BigDecimal) map.get("OngCount")));
                }
            }
        }

        // 按照type进行统计
        projectSummaryList0 = getContractListMap(projectSummaryList0, type);

        // 总量的统计
        int txCountSum = 0;
        int addressSum = 0;
        BigDecimal ontCountSum = new BigDecimal(0);
        BigDecimal ongCountSum = new BigDecimal(0);
        for (Contracts contract : contractsList) {
            txCountSum += contract.getTxcount();
            addressSum += contract.getAddresscount();
            ontCountSum = ontCountSum.add(contract.getOntcount());
            ongCountSum = ongCountSum.add(contract.getOngcount());
        }
        resultMap.put("TxCountSum", txCountSum);
        resultMap.put("AddressSum", addressSum);
        resultMap.put("OntCountSum", ontCountSum.toPlainString());
        resultMap.put("OngCountSum", ongCountSum.toPlainString());
        resultMap.put("SummaryList", projectSummaryList0);
        resultMap.put("ContractsList", contracts);
        resultMap.put("Total", projectSummaryList0.size());

        return Helper.result("QueryProject", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, resultMap);
    }


    @Override
    public Result queryTotalSupply() {

        BigDecimal specialAmount = new BigDecimal("0");
        //?qid=1&contract=0100000000000000000000000000000000000000&from=0&count=100
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("qid", 1);
        paramMap.put("contract", ONTCONTRACT);
        paramMap.put("from", 0);
        paramMap.put("count", 20);
        try {
            String response = Helper.sendGet(configParam.GOSERVER_DOMAIN + ConstantParam.GO_TOTALSUPPLY_URL, paramMap);
            log.info("totalsupply go response:{}", response);
            JSONObject responseObj = JSONObject.parseObject(response);
            JSONArray addrArray = responseObj.getJSONArray("result");
            for (Object temp :
                    addrArray) {
                JSONObject obj = (JSONObject) temp;
                if (ConstantParam.SPECIALADDRLIST.contains(obj.getString("address"))) {
                    //其他地址，即流通量
                    specialAmount = specialAmount.add(obj.getBigDecimal("percent"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("error...", e);
        }

        return Helper.result("QueryTotalSupply", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, new BigDecimal("1").subtract(specialAmount));
    }

    @Override
    public Result queryNativeTotalSupply() {

        BigDecimal specialAmount = new BigDecimal("0");
        //?qid=1&contract=0100000000000000000000000000000000000000&from=0&count=100
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("qid", 1);
        paramMap.put("contract", ONTCONTRACT);
        paramMap.put("from", 0);
        paramMap.put("count", 20);
        try {
            String response = Helper.sendGet(configParam.GOSERVER_DOMAIN + ConstantParam.GO_TOTALSUPPLY_URL, paramMap);
            log.info("totalsupply go response:{}", response);
            JSONObject responseObj = JSONObject.parseObject(response);
            JSONArray addrArray = responseObj.getJSONArray("result");
            for (Object temp :
                    addrArray) {
                JSONObject obj = (JSONObject) temp;
                if (ConstantParam.SPECIALADDRLIST.contains(obj.getString("address"))) {
                    //特定地址总量
                    specialAmount = specialAmount.add(obj.getBigDecimal("balance"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("error...", e);
        }

        //其他地址，即流通量
        BigDecimal ontTotalSupply = ConstantParam.ONT_TOTAL.subtract(specialAmount);
        //((当前时间-创世区块时间)*每秒生成5个ONG) * ONT持仓量/ONT总量
        BigDecimal ongAmount = new BigDecimal(System.currentTimeMillis() / 1000L).subtract(new BigDecimal(configParam.GENESISBLOCK_TIME)).multiply(configParam.ONG_SECOND_GENERATE);
        BigDecimal ongTotalSupply = ongAmount.multiply(ontTotalSupply).divide(ConstantParam.ONT_TOTAL);

        Map<String, BigDecimal> rsMap = new HashMap<>();
        rsMap.put("ong", ongTotalSupply);
        rsMap.put("ont", ontTotalSupply);

        return Helper.result("QueryNativeTotalSupply", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rsMap);
    }
}
