/*
 * Copyright (C) 2018 The ontology Authors
 * This file is part of The ontology library.
 *
 * The ontology is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The ontology is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with The ontology.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.ontio.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.ontio.dao.*;
import com.github.ontio.dao.Oep4Mapper;
import com.github.ontio.dao.Oep5Mapper;
import com.github.ontio.dao.Oep8Mapper;
import com.github.ontio.mapper.*;
import com.github.ontio.model.Contracts;
import com.github.ontio.model.Oep4;
import com.github.ontio.model.Oep5;
import com.github.ontio.model.Oep8;
import com.github.ontio.model.common.PageResponseBean;
import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.model.dto.*;
import com.github.ontio.service.IContractService;
import com.github.ontio.util.ConstantParam;
import com.github.ontio.util.ErrorInfo;
import com.github.ontio.util.Helper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service("ContractService")
@MapperScan("com.github.ontio.dao")
public class ContractServiceImpl implements IContractService {

    @Autowired
    private ContractMapper contractMapper;
    @Autowired
    private Oep4TxDetailMapper oep4TxDetailMapper;
    @Autowired
    private Oep5TxDetailMapper oep5TxDetailMapper;
    @Autowired
    private Oep8TxDetailMapper oep8TxDetailMapper;
    @Autowired
    private TxDetailMapper txDetailMapper;
    @Autowired
    private ContractsMapper contractsMapper;

    @Autowired
    private Oep4Mapper oep4Mapper;

    @Autowired
    private Oep5Mapper oep5Mapper;

    @Autowired
    private Oep8Mapper oep8Mapper;

    @Autowired
    private TransactionDetailMapper transactionDetailMapper;

    @Autowired
    private ContractSummaryMapper contractSummaryMapper;

    private Map<String, Object> newPageInfoMap(Integer pageSize, Integer pageNumber) {
        Map<String, Object> pageInfo = new HashMap<>();
        pageInfo.put("start", pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1));
        pageInfo.put("page_size", pageSize);
        return pageInfo;
    }

    @Override
    public ResponseBean queryContractsByPage(Integer pageSize, Integer pageNumber) {

        int start = pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1);
        List<ContractDto> contractDtos = contractMapper.selectApprovedContract(start, pageSize);

        ContractDto contractDto = ContractDto.builder()
                .auditFlag(1)
                .build();
        int count = contractMapper.selectCount(contractDto);

        PageResponseBean pageResponseBean = new PageResponseBean(contractDtos, count);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), pageResponseBean);
    }


    @Override
    public ResponseBean queryContractDetail(String contractHash) {

        ContractDto contractDtoTemp = ContractDto.builder()
                .contractHash(contractHash)
                .build();
        ContractDto contractDto = contractMapper.selectOne(contractDtoTemp);
        contractDto.setAuditFlag(null);
        contractDto.setDappstoreFlag(null);
        contractDto.setTotalReward(null);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), contractDto);
    }


    @Override
    public ResponseBean queryTxsByContractHash(String contractType, String contractHash, Integer pageNumber, Integer pageSize) {

        List<TxDetailDto> txDetailDtos = new ArrayList<>();
        Integer count = 0;
        int start = pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1);

        switch (contractType.toLowerCase()){
            case ConstantParam.CONTRACT_TYPE_OEP4:
                txDetailDtos = oep4TxDetailMapper.selectTxsByCalledContractHash(contractHash, start, pageSize);
                count = oep4TxDetailMapper.selectCountByCalledContracthash(contractHash);
                break;
            case ConstantParam.CONTRACT_TYPE_OEP5:
                txDetailDtos = oep5TxDetailMapper.selectTxsByCalledContractHash(contractHash, start, pageSize);
                count = oep5TxDetailMapper.selectCountByCalledContracthash(contractHash);
                break;
            case ConstantParam.CONTRACT_TYPE_OEP8:
                txDetailDtos = oep8TxDetailMapper.selectTxsByCalledContractHash(contractHash, start, pageSize);
                count = oep8TxDetailMapper.selectCountByCalledContracthash(contractHash);
                break;
            case ConstantParam.CONTRACT_TYPE_OTHER:
                txDetailDtos = txDetailMapper.selectTxsByCalledContractHash(contractHash, start, pageSize);
                count = txDetailMapper.selectCountByCalledContracthash(contractHash);
                break;
        }

        PageResponseBean pageResponseBean = new PageResponseBean(txDetailDtos, count);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), pageResponseBean);
    }

    @Override
    public ResponseBean queryContractByHash(String contractHash, int pageSize, int pageNumber) {




        Map<String, Object> result = getResultMap(contractHash, "", "", pageSize, pageNumber);
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), result);
    }

    private Map<String, Object> getResultMap(String contractHash, String type, String tokenName, int pageSize, int pageNumber) {
        Contracts contract = contractsMapper.selectContractByContractHash(contractHash);
        if (contract == null) {
            return null;
        }
        if (type.isEmpty()) {
            type = contract.getType();
        }
        Map<String, Object> paramMap = newPageInfoMap(pageSize, pageNumber);
        Integer txnCount;
        switch (type.toLowerCase()) {
            case "oep4":
                //TODO 考虑复兴积分，暂时从txn_detail表查询。等重新同步到oep4_txn_detail表，再更新回来
                txnCount = transactionDetailMapper.selectContractCountByHash(paramMap);
                break;
            case "oep5":
                //TODO 考虑复兴积分，暂时从txn_detail表查询。等重新同步到oep4_txn_detail表，再更新回来
                txnCount = transactionDetailMapper.selectOep5ContractCountByHash(paramMap);
                break;
            case "oep8":
                //TODO 考虑南瓜oep8，暂时从txn_detail表查询。等重新同步到oep8_txn_detail表，再更新回来
                if (!tokenName.isEmpty()) {
                    paramMap.put("AssetName", tokenName);
                }
                txnCount = transactionDetailMapper.selectContractCountByHash(paramMap);
                break;
            default:
                txnCount = transactionDetailMapper.selectContractCountByHash(paramMap);
                break;
        }
        Map<String, Object> result = new HashMap<>();
        result.put("tx_count", txnCount);
        result.put("creator", contract.getCreator());
        result.put("name", contract.getName());
        result.put("create_time", contract.getCreatetime());
        result.put("update_time", contract.getUpdatetime());
        result.put("contact_info", contract.getContactinfo());
        result.put("description", contract.getDescription());
        result.put("logo", contract.getLogo());
        result.put("address_count", contract.getAddresscount());
        result.put("ont_sum", (contract.getOntcount()).toPlainString());
        result.put("ong_sum", (contract.getOngcount()).toPlainString());
        result.put("token_count", contract.getTokencount());
        return result;
    }

    @Override
    public ResponseBean queryOEPContract(String type, int pageSize, int pageNumber) {
        Map<String, Object> paramMap = newPageInfoMap(pageSize, pageNumber);
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

        Map<String, Object> result = new HashMap<>();
        result.put("ContractList", contractList);
        result.put("Total", totalNum == null ? 0 : totalNum);
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), result);
    }

    @Override
    public ResponseBean queryOEPContractByHashAndTokenName(String contractHash, String type, String tokenName, int pageSize, int pageNumber) {
        Map<String, Object> result = getResultMap(contractHash, type, tokenName, pageSize, pageNumber);
        if (result == null) {
            return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), result);
        }
        switch (type.toLowerCase()) {
            case "oep4":
                Oep4 oep4 = oep4Mapper.queryOEPContract(contractHash);
                result.put("total_supply", oep4 == null ? 0 : oep4.getTotalsupply());
                result.put("decimals", oep4 == null ? 0 : oep4.getDecimals());
                result.put("symbol", oep4 == null ? "" : oep4.getSymbol());
                break;
            case "oep5":
                Oep5 oep5 = oep5Mapper.queryOEPContract(contractHash);
                result.put("total_supply", oep5 == null ? 0 : oep5.getTotalsupply());
                result.put("symbol", oep5 == null ? "" : oep5.getSymbol());
                break;
            case "oep8":
                Oep8 oep8;
                if (tokenName.isEmpty()) {
                    oep8 = oep8Mapper.queryOEPContract(contractHash);
                    result.put("total_supply", oep8 == null ? 0 : oep8.getDescription());
                    result.put("symbol", oep8 == null ? "" : oep8.getSymbol());
                    result.put("token_name", oep8 == null ? "" : oep8.getName());
                    result.put("token_id", oep8 == null ? "" : oep8.getTokenid());

                    getKeyAndValue(result);
                } else {
                    oep8 = oep8Mapper.queryOEPContractByHashAndTokenName(contractHash, tokenName);
                    result.put("total_supply", oep8 == null ? "0" : oep8.getTotalsupply().toString());
                    result.put("symbol", oep8 == null ? "" : oep8.getSymbol());
                    result.put("token_name", oep8 == null ? "" : oep8.getName());
                    result.put("token_id", oep8 == null ? "" : oep8.getTokenid());
                }
                break;
            default:
                break;
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), result);
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
    public ResponseBean queryDappstoreContractInfo(Integer pageSize, Integer pageNumber) {

        Map<String, Object> rsMap = new HashMap<>();

        //查询Dappstore的合约基本信息
        List<Map> allContractInfoList = contractsMapper.selectDappstoreContract();
        if (allContractInfoList.size() == 0) {
            rsMap.put("ContractList", new ArrayList<>());
            rsMap.put("Total", 0);
        } else {
            //一个project可能会有多个合约
            Set<Object> allProjectSet = new HashSet<>();
            allContractInfoList.forEach(item -> allProjectSet.add(item.get("Project")));

            List<String> allContractHashList = new ArrayList<>();
            allContractInfoList.forEach(item -> allContractHashList.add((String) item.get("ContractHash")));

            long nowTime = System.currentTimeMillis();
            //一天前的UTC 0点
            Calendar yesterdayCalendar = Calendar.getInstance();
            yesterdayCalendar.setTimeInMillis(nowTime);
            yesterdayCalendar.add(Calendar.DAY_OF_MONTH, -1);
            yesterdayCalendar.set(Calendar.HOUR_OF_DAY, 0);
            yesterdayCalendar.set(Calendar.MINUTE, 0);
            yesterdayCalendar.set(Calendar.SECOND, 0);
            long yesterday0HourTime = yesterdayCalendar.getTimeInMillis() / 1000L;
            //7天前的UTC 0点
            Calendar last7dayCalendar = Calendar.getInstance();
            last7dayCalendar.setTimeInMillis(nowTime);
            last7dayCalendar.add(Calendar.DAY_OF_MONTH, -7);
            last7dayCalendar.set(Calendar.HOUR_OF_DAY, 0);
            last7dayCalendar.set(Calendar.MINUTE, 0);
            last7dayCalendar.set(Calendar.SECOND, 0);
            long last7Day0HourTime = last7dayCalendar.getTimeInMillis() / 1000L;

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("beginTime", last7Day0HourTime);
            paramMap.put("endTime", yesterday0HourTime);
            paramMap.put("contractHashList", allContractHashList);
            paramMap.put("start", pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1));
            paramMap.put("pageSize", pageSize);

            //根据project分组查询周统计数据，并以WeekActiveAddressCount和WeekTxCount倒序
            List<Map> contractOneWeekInfoList = contractSummaryMapper.selectDappstoreContractOneWeekInfo(paramMap);
            //获取分页后的project列表
            List<String> projectList = new ArrayList<>();
            contractOneWeekInfoList.forEach(item -> projectList.add((String) item.get("Project")));


            Map<String, Object> paramMap2 = new HashMap<>();
            paramMap2.put("ProjectList", projectList);
            List<String> contractHashList = contractsMapper.selectContractHashByProject(paramMap2);

            paramMap.put("contractHashList", contractHashList);
            paramMap.put("time", yesterday0HourTime);
            //查询project日统计数据
            List<Map> contractDayInfoList = contractSummaryMapper.selectDappstoreContractYesterdayInfo(paramMap);

            for (Map map :
                    contractOneWeekInfoList) {
                String project = (String) map.get("Project");
                for (Map map1 :
                        allContractInfoList) {
                    if (project.equals(map1.get("Project"))) {
                        map.putAll(map1);
                    }
                }
                for (Map map2 :
                        contractDayInfoList) {
                    if (project.equals(map2.get("Project"))) {
                        map.putAll(map2);
                    }
                }
            }

            rsMap.put("ContractList", contractOneWeekInfoList);
            rsMap.put("Total", allProjectSet.size());
        }

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), rsMap);
    }


    @Override
    public ResponseBean queryDappstore24hSummary() {

        //查询Dappstore的合约基本信息
        List<Map> allContractInfoList = contractsMapper.selectDappstoreContract();
        Map<String, Object> result = new HashMap<>();
        if (allContractInfoList.size() == 0) {
            result.put("DayOnt", 0);
            result.put("DayOng", 0);
            result.put("DayActiveAddressCount", 0);
            result.put("DayTxCount", 0);
            result.put("Total", 0);
            return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), result);
        }
        //一个project可能会有多个合约
        Set<Object> allProjectSet = new HashSet<>();
        allContractInfoList.forEach(item -> allProjectSet.add(item.get("Project")));

        List<String> allContractHashList = new ArrayList<>();
        allContractInfoList.forEach(item -> allContractHashList.add((String) item.get("ContractHash")));

        result.put("Total", allProjectSet.size());

        long nowTime = System.currentTimeMillis();
        //一天前的UTC 0点
        Calendar yesterdayCalendar = Calendar.getInstance();
        yesterdayCalendar.setTimeInMillis(nowTime);
        yesterdayCalendar.add(Calendar.DAY_OF_MONTH, -1);
        yesterdayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        yesterdayCalendar.set(Calendar.MINUTE, 0);
        yesterdayCalendar.set(Calendar.SECOND, 0);
        long yesterday0HourTime = yesterdayCalendar.getTimeInMillis() / 1000L;

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("contractHashList", allContractHashList);
        paramMap.put("time", yesterday0HourTime);
        Map contractInfo = contractSummaryMapper.selectAllDappstoreContractYesterdayInfo(paramMap);
        if (Helper.isEmptyOrNull(contractInfo)) {
            result.put("DayOnt", 0);
            result.put("DayOng", 0);
            result.put("DayActiveAddressCount", 0);
            result.put("DayTxCount", 0);
        } else {
            result.putAll(contractInfo);
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), result);
    }
}
