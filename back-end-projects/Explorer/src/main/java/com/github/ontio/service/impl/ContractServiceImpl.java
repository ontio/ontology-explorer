package com.github.ontio.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.ontio.common.Address;
import com.github.ontio.dao.*;
import com.github.ontio.model.Contracts;
import com.github.ontio.model.Oep4;
import com.github.ontio.model.Oep5;
import com.github.ontio.model.Oep8;
import com.github.ontio.paramBean.Result;
import com.github.ontio.service.IContractService;
import com.github.ontio.utils.ErrorInfo;
import com.github.ontio.utils.Helper;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author
 * @date
 */
@Service("ContractService")
@MapperScan("com.github.ontio.dao")
public class ContractServiceImpl implements IContractService {
    private static final Logger logger = LoggerFactory.getLogger(CurrentServiceImpl.class);

    private static final String CLASS_NAME = ContractServiceImpl.class.getSimpleName();

    private static final String VERSION = "1.0";

    @Autowired
    private ContractsMapper contractsMapper;

    @Autowired
    private Oep4Mapper oep4Mapper;

    @Autowired
    private Oep5Mapper oep5Mapper;

    @Autowired
    private Oep8Mapper oep8Mapper;

    @Autowired
    private Oep4TxnDetailMapper oep4TxnDetailMapper;

    @Autowired
    private Oep5TxnDetailMapper oep5TxnDetailMapper;

    @Autowired
    private Oep8TxnDetailMapper oep8TxnDetailMapper;

    @Autowired
    private TransactionDetailMapper transactionDetailMapper;

    @Autowired
    private ContractSummaryMapper contractSummaryMapper;

    @Override
    public Result queryContract(Integer pageSize, Integer pageNumber) {
        logger.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("Start", pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1));
        paramMap.put("PageSize", pageSize);
        List<Map> list = contractsMapper.selectApprovedContractByPage(paramMap);
        if (!list.isEmpty()) {
            for (Map map : list) {
                map.put("OntCount", ((BigDecimal) map.get("OntCount")).toPlainString());
                map.put("OngCount", ((BigDecimal) map.get("OngCount")).toPlainString());
            }
        }

        Map<String, Object> rs = new HashMap();
        rs.put("Total", contractsMapper.selectContractCount());
        rs.put("ContractList", list);

        return Helper.result("QueryContract", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);
    }

    /**
     * query txn by page
     *
     * @param contractHash contractHash
     * @param pageSize     the amount of each page
     * @param pageNumber   the start page
     * @return
     */
    @Override
    public Result queryContractByHash(String contractHash, int pageSize, int pageNumber) {
        logger.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        Map<String, Object> rs = getResultMap(contractHash, "", "", pageSize, pageNumber);

        return Helper.result("QueryContractByHash", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);
    }

    private Map<String, Object> getResultMap(String contractHash, String type, String tokenName, int pageSize, int pageNumber) {
        Contracts contract = contractsMapper.selectContractByContractHash(contractHash);
        if (contract == null) {
            return null;
        }

        if (type.isEmpty()) {
            type = contract.getType();
        }

        String contractAddress = Address.parse(com.github.ontio.common.Helper.reverse(contractHash)).toBase58();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("contractHash", contractHash);
        paramMap.put("contractAddress", contractAddress);
        paramMap.put("Start", pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1));
        paramMap.put("PageSize", pageSize);

        List<Map> txnList = null;
        Integer txnCount = 0;
        switch (type.toLowerCase()) {
            case "oep4":
                //TODO 考虑复兴积分，暂时从txn_detail表查询。等重新同步到oep4_txn_detail表，再更新回来
//                txnList = oep4TxnDetailMapper.selectContractByHash(paramMap);
//                txnCount = oep4TxnDetailMapper.selectContractByHashAmount(paramMap);
                txnList = transactionDetailMapper.selectContractByHash(paramMap);
                txnCount = transactionDetailMapper.selectContractCountByHash(paramMap);
                break;
            case "oep5":
                //TODO 考虑复兴积分，暂时从txn_detail表查询。等重新同步到oep4_txn_detail表，再更新回来
//                txnList = oep5TxnDetailMapper.selectContractByHash(paramMap);
//                txnCount = oep5TxnDetailMapper.selectContractByHashAmount(paramMap);
                txnList = transactionDetailMapper.selectOep5ByHash(paramMap);
                txnCount = transactionDetailMapper.selectOep5ContractCountByHash(paramMap);
                break;
            case "oep8":
                //TODO 考虑南瓜oep8，暂时从txn_detail表查询。等重新同步到oep8_txn_detail表，再更新回来
                if (!tokenName.isEmpty()) {
                    paramMap.put("AssetName", tokenName);
                }
//                txnList = oep8TxnDetailMapper.selectContractByHash(paramMap);
//                txnCount = oep5TxnDetailMapper.selectContractByHashAmount(paramMap);
                txnList = transactionDetailMapper.selectContractByHash(paramMap);
                txnCount = transactionDetailMapper.selectContractCountByHash(paramMap);
                break;
            default:
                txnList = transactionDetailMapper.selectContractByHash(paramMap);
                txnCount = transactionDetailMapper.selectContractCountByHash(paramMap);
                break;
        }

        if (!txnList.isEmpty()) {
            for (Map map : txnList) {
                map.put("Fee", ((BigDecimal) map.get("Fee")).toPlainString());
            }
        }

        Map<String, Object> rs = new HashMap();
        rs.put("TxnList", txnList);
        rs.put("Total", txnCount);
        rs.put("Creator", contract.getCreator());
        rs.put("Name", contract.getName());
        rs.put("ABI", contract.getAbi());
        rs.put("Code", contract.getCode());
        rs.put("CreateTime", contract.getCreatetime());
        rs.put("UpdateTime", contract.getUpdatetime());
        rs.put("ContactInfo", contract.getContactinfo());
        rs.put("Description", contract.getDescription());
        rs.put("Logo", contract.getLogo());
        rs.put("AddressCount", contract.getAddresscount());
        rs.put("TxSum", contract.getTxcount());
        rs.put("OntCount", (contract.getOntcount()).toPlainString());
        rs.put("OngCount", (contract.getOngcount()).toPlainString());
        rs.put("TokenCount", contract.getTokencount());

        return rs;
    }

    /**
     * 依据类型查询token列表
     *
     * @param type       type
     * @param pageSize   the amount of each page
     * @param pageNumber the start page
     * @return
     */
    @Override
    public Result queryOEPContract(String type, int pageSize, int pageNumber) {
        logger.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("Start", pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1));
        paramMap.put("PageSize", pageSize);
        List<Map> contractList = null;
        Integer totalNum = 0;
        switch (type.toLowerCase()) {
            case "oep4":
                contractList = oep4Mapper.queryOEPContracts(paramMap);
                if (!contractList.isEmpty()) {
                    for (Map map : contractList) {
                        map.put("OngCount", ((BigDecimal) map.get("OngCount")).toPlainString());
                        map.put("OntCount", ((BigDecimal) map.get("OntCount")).toPlainString());
                    }
                }
                totalNum = oep4Mapper.queryOEPContractCount();
                break;
            case "oep5":
                contractList = oep5Mapper.queryOEPContracts(paramMap);
                if (!contractList.isEmpty()) {
                    for (Map map : contractList) {
                        map.put("OngCount", ((BigDecimal) map.get("OngCount")).toPlainString());
                        map.put("OntCount", ((BigDecimal) map.get("OntCount")).toPlainString());
                    }
                }
                totalNum = oep5Mapper.queryOEPContractCount();
                break;
            case "oep8":
                contractList = oep8Mapper.queryOEPContracts(paramMap);
                if (!contractList.isEmpty()) {
                    for (Map map : contractList) {
                        getKeyAndValue(map);

                        map.put("OngCount", ((BigDecimal) map.get("OngCount")).toPlainString());
                        map.put("OntCount", ((BigDecimal) map.get("OntCount")).toPlainString());
                    }
                }
                totalNum = oep8Mapper.queryOEPContractCount();
                break;
            default:
                break;
        }

        Map<String, Object> rs = new HashMap();
        rs.put("ContractList", contractList);
        rs.put("Total", totalNum == null ? 0 : totalNum);

        return Helper.result("QueryOEPContracts", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);
    }

    /**
     * 依据合约hash(tokenName)查询Token合约详情
     *
     * @param contractHash contractHash
     * @param type         type
     * @param tokenName    tokenName
     * @param pageSize     the amount of each page
     * @param pageNumber   the start page
     * @return
     */
    @Override
    public Result queryOEPContractByHashAndTokenName(String contractHash, String type, String tokenName, int pageSize, int pageNumber) {
        logger.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        Map<String, Object> rs = getResultMap(contractHash, type, tokenName, pageSize, pageNumber);
        if (rs == null) {
            return Helper.result("QueryOEPContractByHash", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);
        }
        switch (type.toLowerCase()) {
            case "oep4":
                Oep4 oep4 = oep4Mapper.queryOEPContract(contractHash);
                rs.put("TotalSupply", oep4 == null ? 0 : oep4.getTotalsupply());
                rs.put("Decimals", oep4 == null ? 0 : oep4.getDecimals());
                rs.put("Symbol", oep4 == null ? "" : oep4.getSymbol());
                break;
            case "oep5":
                Oep5 oep5 = oep5Mapper.queryOEPContract(contractHash);
                rs.put("TotalSupply", oep5 == null ? 0 : oep5.getTotalsupply());
                rs.put("Symbol", oep5 == null ? "" : oep5.getSymbol());
                break;
            case "oep8":
                Oep8 oep8 = null;
                if (tokenName.isEmpty()) {
                    oep8 = oep8Mapper.queryOEPContract(contractHash);
                    rs.put("TotalSupply", oep8 == null ? 0 : oep8.getDescription());
                    rs.put("Symbol", oep8 == null ? "" : oep8.getSymbol());
                    rs.put("TokenName", oep8 == null ? "" : oep8.getName());
                    rs.put("TokenId", oep8 == null ? "" : oep8.getTokenid());

                    getKeyAndValue(rs);
                } else {
                    oep8 = oep8Mapper.queryOEPContractByHashAndTokenName(contractHash, tokenName);
                    rs.put("TotalSupply", oep8 == null ? "0" : oep8.getTotalsupply().toString());
                    rs.put("Symbol", oep8 == null ? "" : oep8.getSymbol());
                    rs.put("TokenName", oep8 == null ? "" : oep8.getName());
                    rs.put("TokenId", oep8 == null ? "" : oep8.getTokenid());
                }
                break;
            default:
                break;
        }

        return Helper.result("QueryOEPContractByHash", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);
    }

    private void getKeyAndValue(Map map) {
        String[] tokenIds = ((String) map.get("TokenId")).split(",");
        String[] totalSupplys = ((String) map.get("TotalSupply")).split(",");
        String[] symbols = ((String) map.get("Symbol")).split(",");
        String[] tokenNames = ((String) map.get("TokenName")).split(",");
        Map tokenIdMap = new HashMap();
        Map totalSupplyMap = new HashMap();
        Map symbolMap = new HashMap();
        Map tokenNameMap = new HashMap();
        for (int i = 0; i < tokenIds.length; i++) {
            tokenIdMap.put(tokenIds[i], tokenIds[i]);
            totalSupplyMap.put(tokenIds[i], totalSupplys[i]);
            symbolMap.put(tokenIds[i], symbols[i]);
            tokenNameMap.put(tokenIds[i], tokenNames[i]);
        }

        map.put("TokenId", JSON.toJSON(tokenIdMap));
        map.put("TotalSupply", JSON.toJSON(totalSupplyMap));
        map.put("Symbol", JSON.toJSON(symbolMap));
        map.put("TokenName", JSON.toJSON(tokenNameMap));
    }


    @Override
    public Result queryDappstoreContractInfo(Integer pageSize, Integer pageNumber) {

        Map<String, Object> rsMap = new HashMap<>();

        //查询Dappstore的合约基本信息
        List<Map> allContractList = contractsMapper.selectDappstoreContract();
        if (allContractList.size() == 0) {
            rsMap.put("ContractList", allContractList);
            rsMap.put("Total", allContractList.size());
        } else {
            List<String> allContractHashList = new ArrayList<>();
            allContractList.forEach(item -> allContractHashList.add((String) item.get("ContractHash")));

            long nowTime = System.currentTimeMillis();
            //一天前的UTC 0点
            Calendar yesterdayCalendar = Calendar.getInstance();
            yesterdayCalendar.setTimeInMillis(nowTime);
            yesterdayCalendar.add(Calendar.DAY_OF_MONTH, -1);
            yesterdayCalendar.set(Calendar.HOUR_OF_DAY, -8);
            yesterdayCalendar.set(Calendar.MINUTE, 0);
            yesterdayCalendar.set(Calendar.SECOND, 0);
            long yesterday0HourTime = yesterdayCalendar.getTimeInMillis() / 1000L;
            //7天前的UTC 0点
            Calendar last7dayCalendar = Calendar.getInstance();
            last7dayCalendar.setTimeInMillis(nowTime);
            last7dayCalendar.add(Calendar.DAY_OF_MONTH, -7);
            last7dayCalendar.set(Calendar.HOUR_OF_DAY, -8);
            last7dayCalendar.set(Calendar.MINUTE, 0);
            last7dayCalendar.set(Calendar.SECOND, 0);
            long last7Day0HourTime = last7dayCalendar.getTimeInMillis() / 1000L;

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("contractHashList", allContractHashList);
            paramMap.put("time", yesterday0HourTime);
            paramMap.put("start", pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1));
            paramMap.put("pageSize", pageSize);
            //先根据所有合约hash列表排序查询后返回分页结果
            List<Map> contractList = contractSummaryMapper.selectDappstoreContractYesterdayInfo(paramMap);

            List<String> contractHashList = new ArrayList<>();
            contractList.forEach(item -> contractHashList.add((String) item.get("ContractHash")));

            paramMap.put("contractHashList", contractHashList);
            paramMap.put("beginTime", last7Day0HourTime);
            paramMap.put("endTime", yesterday0HourTime);
            //根据昨日排序查询后的分页结果的合约hash列表，查这些合约hash的周统计数据
            List<Map> contractOneWeekInfoList = contractSummaryMapper.selectDappstoreContractOneWeekInfo(paramMap);

            for (Map map :
                    contractList) {
                String contractHash = (String) map.get("ContractHash");
                for (Map map1 :
                        allContractList) {
                    if (contractHash.equals(map1.get("ContractHash"))) {
                        map.putAll(map1);
                    }
                }
                for (Map map2 :
                        contractOneWeekInfoList) {
                    if (contractHash.equals(map2.get("ContractHash"))) {
                        map.putAll(map2);
                    }
                }
            }

            rsMap.put("ContractList", contractList);
            rsMap.put("Total", allContractList.size());
        }

        return Helper.result("QueryDappStoreContract", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rsMap);
    }


    @Override
    public Result queryDappstore24hSummary() {

        //查询Dappstore的合约基本信息
        List<Map> allContractList = contractsMapper.selectDappstoreContract();
        if (allContractList.size() == 0) {
            Map rsMap = new HashMap();
            rsMap.put("DayOnt", 0);
            rsMap.put("DayOng", 0);
            rsMap.put("DayActiveAddressCount", 0);
            rsMap.put("DayTxCount", 0);
            rsMap.put("Total", 0);
            return Helper.result("QueryDappStoreContract", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rsMap);
        } else {
            List<String> allContractHashList = new ArrayList<>();
            allContractList.forEach(item -> allContractHashList.add((String) item.get("ContractHash")));

            long nowTime = System.currentTimeMillis();
            //一天前的UTC 0点
            Calendar yesterdayCalendar = Calendar.getInstance();
            yesterdayCalendar.setTimeInMillis(nowTime);
            yesterdayCalendar.add(Calendar.DAY_OF_MONTH, -1);
            yesterdayCalendar.set(Calendar.HOUR_OF_DAY, -8);
            yesterdayCalendar.set(Calendar.MINUTE, 0);
            yesterdayCalendar.set(Calendar.SECOND, 0);
            long yesterday0HourTime = yesterdayCalendar.getTimeInMillis() / 1000L;
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("contractHashList", allContractHashList);
            paramMap.put("time", yesterday0HourTime);

            Map<String, Object> rsMap = new HashMap<>();
            rsMap.put("Total", allContractList.size());

            Map contractInfo = contractSummaryMapper.selectAllDappstoreContractYesterdayInfo(paramMap);
            if (Helper.isEmptyOrNull(contractInfo)) {
                rsMap.put("DayOnt", 0);
                rsMap.put("DayOng", 0);
                rsMap.put("DayActiveAddressCount", 0);
                rsMap.put("DayTxCount", 0);
            } else {
                rsMap.putAll(contractInfo);
            }
            return Helper.result("QueryDappStoreContract", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rsMap);
        }
    }
}
