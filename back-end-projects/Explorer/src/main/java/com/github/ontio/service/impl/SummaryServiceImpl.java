package com.github.ontio.service.impl;

import com.github.ontio.common.Address;
import com.github.ontio.dao.*;
import com.github.ontio.model.*;
import com.github.ontio.paramBean.Result;
import com.github.ontio.service.ISummaryService;
import com.github.ontio.utils.ConfigParam;
import com.github.ontio.utils.ErrorInfo;
import com.github.ontio.utils.Helper;
import com.github.ontio.utils.OntologySDKService;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author king
 * @version 1.0
 * @date 2018/12/14
 */
@Service("SummaryService")
@MapperScan("com.github.ontio.dao")
public class SummaryServiceImpl implements ISummaryService {
    private static final Logger logger = LoggerFactory.getLogger(CurrentServiceImpl.class);

    private static final String VERSION = "1.0";

    private static final Integer NUM_7 = 7;

    private static final Integer NUM_30 = 30;

    private static final String CLASS_NAME = SummaryServiceImpl.class.getSimpleName();

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
        logger.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());
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
     * @return
     */
    @Override
    public Result summaryAllInfo() {
        logger.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        if (!configParam.EXPLORER_DAILY_SCHEDULE.equalsIgnoreCase("true")){
            return Helper.result("SummaryAllInfo", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, null);
        }

        // blockCount、txCount、ontCount、ongCount、activeAddressCount、newAddressCount、ontidactivecount、ontidnewacount
        Integer perTime = 24 * 60 * 60;
        Integer startTime = dailySummaryMapper.selectMaxTime();
        if (startTime == null){
            // 创始区块的时间： 2018-06-30 00：00：00    时间戳：1530288000
            startTime = 1530288000;
        }
        else {
            startTime += perTime;
        }

        Integer maxTime = blockMapper.selectBlockMaxTime();
        Map paramMap = new HashMap<>();
        //正常每天只会跑一次，while用于跑历史统计数据
        while (maxTime > (startTime + perTime)){
            paramMap.put("startTime", startTime);
            paramMap.put("endTime", startTime + perTime);

            // 每日新增区块数
            int newBlockCount = blockMapper.selectBlockCountInOneDay(startTime, startTime + perTime);
            // 每日新增ontid数
            int newOntIdCount = ontIdMapper.selectOntIdCountInOneDay(startTime, startTime + perTime, "Register%");
            // 每日活跃ontid数
            int activeOntIdCount = ontIdMapper.selectActiveOntIdCountInOneDay(startTime, startTime + perTime);

            // 先删除temp表所有数据，再将前一天的数据迁移到temp表，再做其他各种信息统计
            transactionDetailTmpMapper.deleteAll();
            transactionDetailTmpMapper.InsertSelective(paramMap);
            //在temp表进行每日其他数据统计
            setDailySummaryInfoFromTempTable(startTime, newBlockCount, newOntIdCount, activeOntIdCount);

            startTime = startTime + perTime;
        }

        return Helper.result("SummaryAllInfo", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, null);
    }

    /**
     * 从temp表进行每日各种数据统计
     *
     * @param startTime
     * @param blockCount
     * @param newOntidCount
     * @param activeOntidCount
     */
    private void setDailySummaryInfoFromTempTable(Integer startTime, Integer blockCount, Integer newOntidCount, Integer activeOntidCount){

        // 每日交易数
        int txnCount = transactionDetailTmpMapper.selectTxnCountInOneDay();

        // 每日交易ont，ong量
        BigDecimal ontCount = transactionDetailTmpMapper.selectOntCountInOneDay();
        BigDecimal ongCount = transactionDetailTmpMapper.selectOngCountInOneDay();

        // 每日活跃地址（没有剔除不符合地址格式的数据）
        List<String> addressList = transactionDetailTmpMapper.selectAddressInOneDay();
/*        List<String> addressList = new ArrayList<>();
        if (!txs.isEmpty()){
            for (String address: txs) {
                if (address.length() != 34){
                    continue;
                }

                addressList.add(address);
            }
        }*/
        // 每日新增地址数=每日活跃地址-address_summary表所有地址
        int activeAddressCount = addressList.size();
        List<String> addressSummaryList = addressSummaryMapper.selectDistinctAddress();
        addressList.removeAll(addressSummaryList);

        // 更新合约的信息
        List<AddressSummary> contractAddressSummarys = updateContractInfo(startTime);

        // 新增地址插入address_summary表
        Integer newAddressCount = setAddressSummary(addressList, startTime, contractAddressSummarys);

        DailySummary dailySummary = new DailySummary();
        dailySummary.setBlockcount(blockCount);
        dailySummary.setTime(startTime);
        dailySummary.setTxncount(txnCount);
        dailySummary.setOntidactivecount(activeOntidCount);
        dailySummary.setOntidnewcount(newOntidCount);
        dailySummary.setOntcount(ontCount == null ? new BigDecimal(0) : ontCount);
        dailySummary.setOngcount(ongCount == null ? new BigDecimal(0) : ongCount.divide(new BigDecimal(1000000000)));
        dailySummary.setActiveaddress(activeAddressCount);
        dailySummary.setNewaddress(newAddressCount);

        dailySummaryMapper.insert(dailySummary);
    }

    /**
     * 新增地址插入address_summary表
     *
     * @param addressList
     * @param startTime
     * @param contractAddressSummarys
     * @return
     */
    private Integer setAddressSummary(List<String> addressList, Integer startTime, List<AddressSummary> contractAddressSummarys){
        if (addressList.isEmpty()){
            return 0;
        }

        List<AddressSummary> addressSummarys = new ArrayList<>();
        int newAddressCount = 0;
        for (String address: addressList) {
/*            if (address.length() != 34){
                continue;
            }*/

            AddressSummary addressSummary = new AddressSummary();
            addressSummary.setTime((int)startTime);
            addressSummary.setType("common");
            addressSummary.setAddress(address);
            addressSummarys.add(addressSummary);
        }

        newAddressCount = addressSummarys.size();
        addressSummarys.addAll(contractAddressSummarys);
        if (!addressSummarys.isEmpty()){
            addressSummaryMapper.banchInsertSelective(addressSummarys);
        }

        return newAddressCount;
    }

    /**
     * 更新合约每日统计数据,即更新contract_summary表数据
     * @param startTime
     * @return
     */
    private List<AddressSummary> updateContractInfo(Integer startTime){
        //查询所有合约，不管是否审核过
        List<Contracts> contractList = contractsMapper.selectAllContract();
        if(contractList.isEmpty()){
            return new ArrayList<>();
        }

        List<ContractSummary> contractSummaryList = new ArrayList<>();
        List<AddressSummary> contractAddressSummarys = new ArrayList<>();
        for (Contracts contracts : contractList) {
            String contractHash = contracts.getContract();
            String contractAddress = Address.parse(com.github.ontio.common.Helper.reverse(contractHash)).toBase58();

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("contractHash", contractHash);
            paramMap.put("contractAddress", contractAddress);
            paramMap.put("assetname", "ont");
/*            BigDecimal ontCount = transactionDetailTmpMapper.selectContractAssetSum(paramMap);
            ontCount = ontCount == null ? new BigDecimal(0) : ontCount;*/
            BigDecimal ontCount = transactionDetailTmpMapper.selectContractAssetSumNew(paramMap);
            ontCount = ontCount == null ? new BigDecimal(0) : ontCount;

            paramMap.put("assetname", "ong");
/*            BigDecimal ongCount = transactionDetailTmpMapper.selectContractAssetSum(paramMap);
            ongCount = ongCount == null ? new BigDecimal(0) : ongCount;*/
            BigDecimal ongCount = transactionDetailTmpMapper.selectContractAssetSumNew(paramMap);
            ongCount = ongCount == null ? new BigDecimal(0) : ongCount;

            // 依据合约hash查询交易数
            int txnCount = transactionDetailTmpMapper.selectTxnAmount(paramMap);
            //每日合约活跃地址列表
            List<String> toAddrList = transactionDetailTmpMapper.selectToAddressCountByContractNew(contractHash);
            List<String> fromAddrList = transactionDetailTmpMapper.selectFromAddressCountByContractNew(contractHash);
            toAddrList.addAll(fromAddrList);

            List<String> addressList = toAddrList;
            //每日合约活跃地址数
            Integer activeAddressCount = addressList.size();

            // 依据合约hash和合约地址分别查询地址数（去重）
/*            List<String> addressByContractList = transactionDetailTmpMapper.selectAllAddressByContract(contractHash);
            List<String> addressByAddressList = transactionDetailTmpMapper.selectAllAddressByAddress(contractAddress);
            addressByContractList.addAll(addressByAddressList);
            
            List<String> addressList = new ArrayList<>();
            for (String address: addressByContractList) {
                if (address.length() != 34){
                    continue;
                }

                addressList.add(address);
            }
            Integer activeAddress = addressList.size();*/


            // 新增地址数=每日合约活跃地址数-address_summary所有地址
            List<String> contractAddressList = addressSummaryMapper.selectDistinctAddressByContract(contractHash);
            addressList.removeAll(contractAddressList);
            //更新contract_summary表当天的合约统计信息
            ContractSummary contractSummary = new ContractSummary();
            contractSummary.setTime(startTime);
            contractSummary.setContracthash(contractHash);
            contractSummary.setTxncount(txnCount);
            contractSummary.setOntcount(ontCount);
            contractSummary.setOngcount(ongCount.divide(new BigDecimal("1000000000")));
            contractSummary.setActiveaddress(activeAddressCount);
            contractSummary.setNewaddress(addressList.size());
            contractSummaryList.add(contractSummary);
            //新增地址放到列表中，再外面再插入address_summary表
            for (String address: addressList) {
/*                if (address.length() != 34){
                    continue;
                }*/
                AddressSummary addressSummary = new AddressSummary();
                addressSummary.setTime(startTime);
                addressSummary.setType(contractHash);
                addressSummary.setAddress(address);
                contractAddressSummarys.add(addressSummary);
            }
        }

        contractSummaryMapper.banchInsertSelective(contractSummaryList);

        return contractAddressSummarys;
    }

    /**
     * Tps Info
     * @return
     */
    @Override
    public Result queryTps() {
        logger.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("CurrentTps", queryCurrentTps());
        resultMap.put("MaxTps", 10000);

        return Helper.result("QueryTps", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, resultMap);
    }

    private String queryCurrentTps(){
        int nowTime = (int)(System.currentTimeMillis() / 1000);

        Map<String, Object> paramMap = new HashMap();
        paramMap.put("startTime", nowTime - 60);
        paramMap.put("endTime", nowTime);
        Integer tpsPerMin = transactionDetailMapper.queryTransactionCount(paramMap);

        DecimalFormat df = new DecimalFormat("0.00");
        return df.format((double)(tpsPerMin) / 60);
    }

    /**
     * Daily Info
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Result querySummary(String type, int startTime, int endTime) {
        logger.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        Map<String, Object> paramMap = new HashMap();
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        List<Map> dailyList = dailySummaryMapper.selectDailyInfo(paramMap);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        Map<String, Object> addressAndOntIdCount = dailySummaryMapper.selectAddressAndOntIdCount(startTime);
        switch (type){
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
        if(dailyList.size() < count){
            return resultMapList;
        }

        int weekCount = dailyList.size() / count;
        for (int i = 0; i < weekCount; i++){
            Map<String, Object> result = dailyList.get(i * count);
            int time = (Integer) result.get("Time");
            result.put("Time", simpleDateFormat.format((long)time * 1000));
            for(int j = 1; j < count; j++){
                Map<String, Object> perMap = dailyList.get(i * count + j);
                result.put("TxnCount", (Integer)result.get("TxnCount") + (Integer)perMap.get("TxnCount"));
                result.put("ActiveAddress", (Integer)result.get("ActiveAddress") + (Integer)perMap.get("ActiveAddress"));
                result.put("BlockCount", (Integer)result.get("BlockCount") + (Integer)perMap.get("BlockCount"));
                result.put("OntIdNewCount", (Integer)result.get("OntIdNewCount") + (Integer)perMap.get("OntIdNewCount"));
                result.put("OntIdActiveCount", (Integer)result.get("OntIdActiveCount") + (Integer)perMap.get("OntIdActiveCount"));
                result.put("NewAddress", (Integer)result.get("NewAddress") + (Integer)perMap.get("NewAddress"));
                result.put("OntCount", ((BigDecimal)result.get("OntCount")).add((BigDecimal) perMap.get("OntCount")));
                result.put("OngCount", ((BigDecimal)result.get("OngCount")).add((BigDecimal) perMap.get("OngCount")));
            }

            result.put("OntCount", ((BigDecimal)result.get("OntCount")).toPlainString());
            result.put("OngCount", ((BigDecimal)result.get("OngCount")).toPlainString());
            resultMapList.add(result);
        }

        return resultMapList;
    }

    private List<Map> querySummaryInfo(List<Map> dailyList, SimpleDateFormat simpleDateFormat, Map<String, Object> addressAndOntIdCount) {
        int addressCount = 0;
        int ontIdCount = 0;
        if(addressAndOntIdCount != null && addressAndOntIdCount.get("addressSum") != null){
            addressCount = Integer.parseInt( addressAndOntIdCount.get("addressSum").toString());
        }
        if(addressAndOntIdCount != null && addressAndOntIdCount.get("ontIdSum") != null){
            ontIdCount = Integer.parseInt( addressAndOntIdCount.get("ontIdSum").toString());
        }

        for (Map map: dailyList) {
            int time = (Integer) map.get("Time");
            map.put("Time", simpleDateFormat.format((long)time * 1000));
            map.put("OntCount", ((BigDecimal)map.get("OntCount")).toPlainString());
            map.put("OngCount", ((BigDecimal)map.get("OngCount")).toPlainString());
            addressCount += (Integer) map.get("NewAddress");
            ontIdCount += (Integer) map.get("OntIdNewCount");
            map.put("AddressSum", addressCount);
            map.put("OntIdSum", ontIdCount);

        }

        return dailyList;
    }

    /**
     * Contract Info
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Result queryContract(String contractHash, String type, int startTime, int endTime) {
        logger.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        Map<String, Object> resultMap = new HashMap();
        resultMap.put("SummaryList", new ArrayList<>());
        resultMap.put("TxCountSum", 0);
        resultMap.put("AddressSum", 0);
        resultMap.put("OntCountSum", new BigDecimal(0).toPlainString());
        resultMap.put("OngCountSum", new BigDecimal(0).toPlainString());
        resultMap.put("Total", 0);

        Contracts contracts = contractsMapper.selectContractByContractHash(contractHash);
        if(contracts == null){
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
        for (Map map: dailyList) {
            int time = (Integer) map.get("Time");
            map.put("Time", simpleDateFormat.format((long)time * 1000));
            map.put("OntCount", ((BigDecimal)map.get("OntCount")).toPlainString());
            map.put("OngCount", ((BigDecimal)map.get("OngCount")).toPlainString());
        }

        return dailyList;
    }

    private List<Map> queryContract(List<Map> dailyList, SimpleDateFormat simpleDateFormat, int count) {
        List<Map> resultMapList = new ArrayList<>();
        if(dailyList.size() < count){
            return resultMapList;
        }

        int weekCount = dailyList.size() / count;
        for (int i = 0; i < weekCount; i++){
            Map<String, Object> result = dailyList.get(i * count);
            int time = (Integer) result.get("Time");
            result.put("Time", simpleDateFormat.format((long)time * 1000));
            for(int j = 1; j < count; j++){
                Map<String, Object> perMap = dailyList.get(i * count + j);
                result.put("TxnCount", (Integer)result.get("TxnCount") + (Integer)perMap.get("TxnCount"));
                result.put("ActiveAddress", (Integer)result.get("ActiveAddress") + (Integer)perMap.get("ActiveAddress"));
                result.put("NewAddress", (Integer)result.get("NewAddress") + (Integer)perMap.get("NewAddress"));
                result.put("OntCount", ((BigDecimal)result.get("OntCount")).add((BigDecimal) perMap.get("OntCount")));
                result.put("OngCount", ((BigDecimal)result.get("OngCount")).add((BigDecimal) perMap.get("OngCount")));
            }
            result.put("OntCount", ((BigDecimal)result.get("OntCount")).toPlainString());
            result.put("OngCount", ((BigDecimal)result.get("OngCount")).toPlainString());

            resultMapList.add(result);
        }

        return resultMapList;
    }

    private List<Map> getContractListMap(List<Map> dailyList, String type){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        switch (type){
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
     * @param project
     * @param type
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Result queryProjectInfo(String project, String type, int startTime, int endTime) {
        logger.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        List<Contracts> contractsList = contractsMapper.selectAllContractByProject(project);
        Map<String, Object> resultMap = new HashMap();
        resultMap.put("SummaryList", new ArrayList<>());
        resultMap.put("TxCountSum", 0);
        resultMap.put("AddressSum", 0);
        resultMap.put("OntCountSum", new BigDecimal(0).toPlainString());
        resultMap.put("OngCountSum", new BigDecimal(0).toPlainString());
        resultMap.put("Total", 0);

        if(contractsList.isEmpty()){
            return Helper.result("QueryProject", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, resultMap);
        }

        // 每天的统计
        Map<Integer, Map<String, Object>> projectMap = new HashMap<>();
        List<Map> projectSummaryList0 = new ArrayList<>();
        List<String> contracts = new ArrayList<>();
        for (Contracts contract: contractsList) {
            contracts.add(contract.getContract());
            Map<String, Object> paramMap = new HashMap();
            paramMap.put("contractHash", contract.getContract());
            paramMap.put("startTime", startTime);
            paramMap.put("endTime", endTime);
            List<Map> dailyList = contractSummaryMapper.selectDailySummaryByContractHash(paramMap);

            int time = 0;
            for (Map map: dailyList) {
                time = (int)map.get("Time");

                Map<String, Object> perMap = projectMap.get(time);
                if (perMap == null){
                    projectMap.put(time, map);
                    projectSummaryList0.add(map);
                }
                else{
                    perMap.put("TxnCount", (Integer)perMap.get("TxnCount") + (Integer)map.get("TxnCount"));
                    perMap.put("ActiveAddress", (Integer)perMap.get("ActiveAddress") + (Integer)map.get("ActiveAddress"));
                    perMap.put("NewAddress", (Integer)perMap.get("NewAddress") + (Integer)map.get("NewAddress"));
                    perMap.put("OntCount", ((BigDecimal)perMap.get("OntCount")).add((BigDecimal) map.get("OntCount")));
                    perMap.put("OngCount", ((BigDecimal)perMap.get("OngCount")).add((BigDecimal) map.get("OngCount")));
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
        for (Contracts contract: contractsList) {
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
}
