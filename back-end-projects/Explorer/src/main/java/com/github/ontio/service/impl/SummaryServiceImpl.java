package com.github.ontio.service.impl;

import com.github.ontio.common.Address;
import com.github.ontio.dao.*;
import com.github.ontio.model.*;
import com.github.ontio.paramBean.Result;
import com.github.ontio.schedule.DailyInfoSchedule;
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

    private static final String CLASS_NAME = DailyInfoSchedule.class.getSimpleName();

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
        Map<String, Object> resultMap = new HashMap();

        // 节点数
        initSDK();
        int nodeCount = sdk.getNodeCount();
        resultMap.put("NodeCount", nodeCount);

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
     * Marketing Info
     * @return
     */
    @Override
    public Result summaryAllInfo() {
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

        Map paramMap = new HashMap<>();
        while (System.currentTimeMillis() / 1000 > (startTime + perTime)){
            paramMap.put("startTime", startTime);
            paramMap.put("endTime", startTime + perTime);

            // 区块数
            int blockCount = blockMapper.selectBlockCountInOneDay(startTime, startTime + perTime);

            // 活跃ontid数 + 新增ontid数
            int newOntidCount = ontIdMapper.selectOntIdCountInOneDay(startTime, startTime + perTime, "Register%");
            int activeOntidCount = ontIdMapper.selectActiveOntIdCountInOneDay(startTime, startTime + perTime);

            // 将每天的数据先转移到临时表，再统计其他信息
            transactionDetailTmpMapper.deleteAll();
            transactionDetailTmpMapper.InsertSelective(paramMap);
            setDailySummary(startTime, blockCount, newOntidCount, activeOntidCount);

            startTime = startTime + perTime;
        }

        return Helper.result("QueryMarketingInfo", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, null);
    }

    private void setDailySummary(Integer startTime, Integer blockCount, Integer newOntidCount, Integer activeOntidCount){
        // 交易数
        int txnCount = transactionDetailTmpMapper.selectTxnCountInOneDay();

        // 交易量
        BigDecimal ontCount = transactionDetailTmpMapper.selectOntCountInOneDay();
        BigDecimal ongCount = transactionDetailTmpMapper.selectOngCountInOneDay();

        // 活跃地址数 + 新增地址数
        List<String> txs = transactionDetailTmpMapper.selectAddressInOneDay();
        int activeAddressCount = txs.isEmpty() ? 0 : txs.size();// 活跃地址数

        List<String> addressSummaryList = addressSummaryMapper.selectDistinctAddress();
        txs.removeAll(addressSummaryList);

        // 更新合约的信息
        List<AddressSummary> contractAddressSummarys = updateContractInfo(startTime);

        // 新增地址数
        Integer newAddressCount = setAddressSummary(txs, startTime, contractAddressSummarys);


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

    private Integer setAddressSummary(List<String> txs, Integer startTime, List<AddressSummary> contractAddressSummarys){
        if (txs.isEmpty()){
            return 0;
        }

        List<AddressSummary> addressSummarys = new ArrayList<>();
        int newAddressCount = 0;
        for (String address: txs) {
            if (address.length() != 34){
                continue;
            }

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

    private List<AddressSummary> updateContractInfo(Integer startTime){
        List<Contracts> contractList = contractsMapper.selectAllContract();
        if(contractList.isEmpty()){
            return new ArrayList<>();
        }

        List<ContractSummary> contractSummaryList = new ArrayList<>();
        List<AddressSummary> contractAddressSummarys = new ArrayList<>();
        for (Contracts contracts : contractList) {
            String contractHash = contracts.getContract();
            String contractAddress = Address.parse(com.github.ontio.common.Helper.reverse(contractHash)).toBase58();

            Map<String, Object> paramMap1 = new HashMap<>();
            paramMap1.put("address", contractAddress);
            paramMap1.put("assetname", "ont");
            BigDecimal ontCountByAddress = transactionDetailTmpMapper.selectContractAssetSumByAddress(paramMap1);
            ontCountByAddress = ontCountByAddress == null ? new BigDecimal(0) : ontCountByAddress;

            paramMap1.put("assetname", "ong");
            BigDecimal ongCountByAddress = transactionDetailTmpMapper.selectContractAssetSumByAddress(paramMap1);
            ongCountByAddress = ongCountByAddress == null ? new BigDecimal(0) : ongCountByAddress;

            paramMap1.put("address", contractHash);
            paramMap1.put("assetname", "ont");
            BigDecimal ontCountByContract = transactionDetailTmpMapper.selectContractAssetSumByContract(paramMap1);
            ontCountByContract = ontCountByContract == null ? new BigDecimal(0) : ontCountByContract;

            paramMap1.put("assetname", "ong");
            BigDecimal ongCountByContract = transactionDetailTmpMapper.selectContractAssetSumByContract(paramMap1);
            ongCountByContract = ongCountByContract == null ? new BigDecimal(0) : ongCountByContract;

            BigDecimal ontCount = ontCountByContract.add(ontCountByAddress);
            BigDecimal ongCount = ongCountByContract.add(ongCountByAddress);

            // 依据合约hash和合约地址分别查询交易数
            int txnCountByContractHash = transactionDetailTmpMapper.selectTxnAmountByContractHash(contractHash);
            int txnCountByAddress = transactionDetailTmpMapper.selectTxnAmountByAddress(contractAddress);

            // 依据合约hash和合约地址分别查询地址数（去重）
            List<String> addressByContractList = transactionDetailTmpMapper.selectAllAddressByContract(contractHash);
            List<String> addressByAddressList = transactionDetailTmpMapper.selectAllAddressByAddress(contractAddress);
            addressByContractList.addAll(addressByAddressList);
            Integer activeAddress = addressByContractList.size();

            // 计算新增地址数
            List<String> contractAddressList = addressSummaryMapper.selectDistinctAddressByContract(contractHash);
            addressByContractList.removeAll(contractAddressList);

            contracts.setTxcount(contracts.getTxcount() + txnCountByContractHash + txnCountByAddress);
            contracts.setOntcount(contracts.getOntcount().add(ontCount));
            contracts.setOngcount(contracts.getOngcount().add(ongCount.divide(new BigDecimal("1000000000"))));
            contracts.setAddresscount(contracts.getAddresscount() + addressByContractList.size());

            ContractSummary contractSummary = new ContractSummary();
            contractSummary.setTime(startTime);
            contractSummary.setContracthash(contractHash);
            contractSummary.setTxncount(txnCountByContractHash + txnCountByAddress);
            contractSummary.setOntcount(ontCount);
            contractSummary.setOngcount(ongCount.divide(new BigDecimal("1000000000")));
            contractSummary.setActiveaddress(activeAddress);
            contractSummary.setNewaddress(addressByContractList.size());
            contractSummaryList.add(contractSummary);

            for (String address: addressByContractList) {
                if (address.length() != 34){
                    continue;
                }

                AddressSummary addressSummary = new AddressSummary();
                addressSummary.setTime(startTime);
                addressSummary.setType(contractHash);
                addressSummary.setAddress(address);
                contractAddressSummarys.add(addressSummary);
            }
        }

        contractsMapper.banchUpdateByPrimaryKeySelective(contractList);
        contractSummaryMapper.banchInsertSelective(contractSummaryList);

        return contractAddressSummarys;
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
        for (Contracts contract: contractsList) {
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

        List<Map> projectSummaryList = new ArrayList<>();
        for (Map.Entry<Integer, Map<String, Object>> entry: projectMap.entrySet()) {
            projectSummaryList.add(entry.getValue());
        }

        switch (type){
            case "weekly":
                projectSummaryList = queryContract(projectSummaryList, NUM_7);
                break;
            case "monthly":
                projectSummaryList = queryContract(projectSummaryList, NUM_30);
                break;
            default:
                projectSummaryList = querySummaryOrContract(projectSummaryList);
                break;
        }

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
        resultMap.put("SummaryList", projectSummaryList);
        resultMap.put("Total", projectSummaryList.size());


        return Helper.result("QueryProject", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, resultMap);
    }

    /**
     * Tps Info
     * @return
     */
    @Override
    public Result queryTps() {
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
        return df.format((double)(tpsPerMin + 600) / 60);
    }

    /**
     * Daily Info
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Result querySummary(String type, int startTime, int endTime) {
        Map<String, Object> paramMap = new HashMap();
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        List<Map> dailyList = dailySummaryMapper.selectDailyInfo(paramMap);

        switch (type){
            case "weekly":
                dailyList = querySummaryInfo(dailyList, NUM_7);
                break;
            case "monthly":
                dailyList = querySummaryInfo(dailyList, NUM_30);
                break;
            default:
                dailyList = querySummaryOrContract(dailyList);
                break;
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("SummaryList", dailyList);
        resultMap.put("Total", dailyList.size());

        return Helper.result("QuerySummary", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, resultMap);
    }

    private List<Map> querySummaryInfo(List<Map> dailyList, Integer count) {
        List<Map> resultMapList = new ArrayList<>();
        if(dailyList.size() < count){
            return resultMapList;
        }

        int weekCount = dailyList.size() / count;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
                result.put("OntCount", (((BigDecimal)result.get("OntCount")).add((BigDecimal) perMap.get("OntCount"))).toPlainString());
                result.put("OngCount", (((BigDecimal)result.get("OngCount")).add((BigDecimal) perMap.get("OngCount"))).toPlainString());
            }

            resultMapList.add(result);
        }

        return resultMapList;
    }

    /**
     * Contract Info
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Result queryContract(String contractHash, String type, int startTime, int endTime) {
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

        switch (type){
            case "weekly":
                dailyList = queryContract(dailyList, NUM_7);
                break;
            case "monthly":
                dailyList = queryContract(dailyList, NUM_30);
                break;
            default:
                dailyList = querySummaryOrContract(dailyList);
                break;
        }

        resultMap.put("TxCountSum", contracts.getTxcount());
        resultMap.put("AddressSum", contracts.getAddresscount());
        resultMap.put("OntCountSum", contracts.getOntcount().toPlainString());
        resultMap.put("OngCountSum", contracts.getOngcount().toPlainString());
        resultMap.put("SummaryList", dailyList);
        resultMap.put("Total", dailyList.size());

        return Helper.result("QueryContract", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, resultMap);
    }

    private List<Map> querySummaryOrContract(List<Map> dailyList) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (Map map: dailyList) {
            int time = (Integer) map.get("Time");
            map.put("Time", simpleDateFormat.format((long)time * 1000));
            map.put("OntCount", ((BigDecimal)map.get("OntCount")).toPlainString());
            map.put("OngCount", ((BigDecimal)map.get("OngCount")).toPlainString());
        }

        return dailyList;
    }

    private List<Map> queryContract(List<Map> dailyList, int count) {
        List<Map> resultMapList = new ArrayList<>();
        if(dailyList.size() < count){
            return resultMapList;
        }

        int weekCount = dailyList.size() / count;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < weekCount; i++){
            Map<String, Object> result = dailyList.get(i * count);
            int time = (Integer) result.get("Time");
            result.put("Time", simpleDateFormat.format((long)time * 1000));
            for(int j = 1; j < count; j++){
                Map<String, Object> perMap = dailyList.get(i * count + j);
                result.put("TxnCount", (Integer)result.get("TxnCount") + (Integer)perMap.get("TxnCount"));
                result.put("ActiveAddress", (Integer)result.get("ActiveAddress") + (Integer)perMap.get("ActiveAddress"));
                result.put("NewAddress", (Integer)result.get("NewAddress") + (Integer)perMap.get("NewAddress"));
                result.put("OntCount", (((BigDecimal)result.get("OntCount")).add((BigDecimal) perMap.get("OntCount"))).toPlainString());
                result.put("OngCount", (((BigDecimal)result.get("OngCount")).add((BigDecimal) perMap.get("OngCount"))).toPlainString());
            }

            resultMapList.add(result);
        }

        return resultMapList;
    }
}
