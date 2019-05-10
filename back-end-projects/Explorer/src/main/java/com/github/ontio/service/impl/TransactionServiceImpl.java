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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.ontio.config.ConfigParam;
import com.github.ontio.dao.*;
import com.github.ontio.mapper.OntidTxDetailMapper;
import com.github.ontio.mapper.TxDetailMapper;
import com.github.ontio.model.common.EventTypeEnum;
import com.github.ontio.model.common.PageResponseBean;
import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.model.dto.CurrentDto;
import com.github.ontio.model.dto.OntidTxDetailDto;
import com.github.ontio.model.dto.TxDetailDto;
import com.github.ontio.paramBean.OldResult;
import com.github.ontio.service.ITransactionService;
import com.github.ontio.util.ConstantParam;
import com.github.ontio.util.ErrorInfo;
import com.github.ontio.util.Helper;
import com.github.ontio.util.OntologySDKService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/2/27
 */
@Service("TransactionService")
@MapperScan("com.github.ontio.dao")
@Slf4j
public class TransactionServiceImpl implements ITransactionService {

    private static final String VERSION = "1.0";


    @Autowired
    private TxDetailMapper txDetailMapper;
    @Autowired
    private com.github.ontio.mapper.CurrentMapper currentMapper;
    @Autowired
    private OntidTxDetailMapper ontidTxDetailMapper;
    @Autowired
    private TransactionDetailMapper transactionDetailMapper;
    @Autowired
    private Oep4Mapper oep4Mapper;
    @Autowired
    private Oep5Mapper oep5Mapper;
    @Autowired
    private Oep8Mapper oep8Mapper;
    @Autowired
    private ConfigParam configParam;

    private OntologySDKService sdk;

    private synchronized void initSDK() {
        if (sdk == null) {
            sdk = OntologySDKService.getInstance(configParam);
        }
    }

    @Override
    public ResponseBean queryLatestTxs(int count) {

        List<TxDetailDto> txDetails = txDetailMapper.selectTxsByPage(0, count);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), txDetails);
    }

    @Override
    public ResponseBean queryTxsByPage(int pageNumber, int pageSize) {

        int start = pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1);

        List<TxDetailDto> txDetails = txDetailMapper.selectTxsByPage(start, pageSize);

        CurrentDto currentDto = currentMapper.selectSummaryInfo();

        PageResponseBean pageResponseBean = new PageResponseBean(txDetails, currentDto.getTxCount());

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), pageResponseBean);
    }

    @Override
    public ResponseBean queryLatestNonontidTxs(int count) {

        List<TxDetailDto> txDetails = txDetailMapper.selectNonontidTxsByPage(0, count);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), txDetails);
    }

    @Override
    public ResponseBean queryNonontidTxsByPage(int pageNumber, int pageSize) {

        int start = pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1);

        List<TxDetailDto> txDetails = txDetailMapper.selectNonontidTxsByPage(start, pageSize);

        CurrentDto currentDto = currentMapper.selectSummaryInfo();

        PageResponseBean pageResponseBean = new PageResponseBean(txDetails, currentDto.getNonontidTxCount());

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), pageResponseBean);
    }

    @Override
    public ResponseBean queryTxDetailByHash(String txHash) {

        TxDetailDto txDetailDto = txDetailMapper.selectTxByHash(txHash);
        if (Helper.isEmptyOrNull(txDetailDto)) {
            return new ResponseBean(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), false);
        }

        JSONObject detailObj = new JSONObject();
        int eventType = txDetailDto.getEventType();
        //转账or权限交易，获取转账详情
        if (EventTypeEnum.Transfer.getType() == eventType || EventTypeEnum.Auth.getType() == eventType) {

            List<TxDetailDto> txDetailDtos = txDetailMapper.selectTransferTxDetailByHash(txHash);

            txDetailDtos.forEach(item->{
                //ONG转换好精度给前端
                String assetName = item.getAssetName();
                if (ConstantParam.ONG.equals(assetName)) {
                    item.setAmount(item.getAmount().divide(ConstantParam.ONG_TOTAL));
                }
            });
            detailObj.put("transfers", txDetailDtos);
        } else if (EventTypeEnum.Ontid.getType() == eventType) {
            //ONTID交易获取ONTID动作详情
            OntidTxDetailDto ontidTxDetailDtoTemp = OntidTxDetailDto.builder()
                    .txHash(txHash)
                    .build();
            OntidTxDetailDto ontidTxDetailDto = ontidTxDetailMapper.selectOne(ontidTxDetailDtoTemp);

            String ontIdDes = Helper.templateOntIdOperation(ontidTxDetailDto.getDescription());

            detailObj.put("ontid", ontidTxDetailDto.getOntid());
            detailObj.put("description", ontIdDes);
        }
        txDetailDto.setDetail(detailObj);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), txDetailDto);
    }


    @Override
    public OldResult queryAddressInfo(String address, int pageNumber, int pageSize) {
        if (address.length() != 34) {
            return Helper.result("QueryAddressInfo", ErrorInfo.PARAM_ERROR.code(), ErrorInfo.PARAM_ERROR.desc(), VERSION, false);
        }

        if (pageSize > configParam.QUERYADDRINFO_PAGESIZE) {
            return Helper.result("QueryAddressInfo", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, "pageSize limit " + configParam.QUERYADDRINFO_PAGESIZE);
        }

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("Address", address);
        //int txnAmount = transactionDetailMapper.selectTxnAmountByAddressInfo(paramMap);
        //db查询返回总条数or分页的前几十条
//        int dbReturnNum = (pageNumber * pageSize) > txnAmount ? txnAmount : (pageNumber * pageSize);
        paramMap.put("Start", 0);
        paramMap.put("PageSize", pageNumber * pageSize * 3);

        List<Map> fromAddrTxnList = transactionDetailMapper.selectTxnByFromAddressInfo(paramMap);
        List<Map> toAddrTxnList = transactionDetailMapper.selectTxnByToAddressInfo(paramMap);

        List<Map> dbTxnList = new ArrayList<>();
        dbTxnList.addAll(fromAddrTxnList);
        dbTxnList.addAll(toAddrTxnList);

        if (fromAddrTxnList.size() != 0 && toAddrTxnList.size() != 0) {
            sortTxnList(dbTxnList);
        }

        List<Map> formattedTxnList = formatTransferTxnList2(dbTxnList);

        List<Map> returnTxnList = new LinkedList<>();

        if (formattedTxnList.size() < pageSize * pageNumber && formattedTxnList.size() > 0) {
            int amount = transactionDetailMapper.selectAddressRecordAmount(address);
            //针对一个地址有T笔1对N转账or一笔1对M转账的特殊处理(T*N>pageNumber*pageSize*3 or M>pageNumber*pageSize*3)
            if (amount > pageNumber * pageSize * 3) {
                returnTxnList = queryAddressInfoSpe(address, pageNumber, pageSize, amount, "");
            } else {
                //先查询出txnlist，再根据请求条数进行分页
                //根据分页确认start，end即请求的pageSize条数
                int start = (pageNumber - 1) * pageSize <= 0 ? 0 : (pageNumber - 1) * pageSize;
                int end = (pageSize + start) > formattedTxnList.size() ? formattedTxnList.size() : (pageSize + start);
                for (int k = start; k < end; k++) {
                    returnTxnList.add(formattedTxnList.get(k));
                }
            }
        } else {
            //先查询出txnlist，再根据请求条数进行分页
            //根据分页确认start，end即请求的pageSize条数
            int start = (pageNumber - 1) * pageSize <= 0 ? 0 : (pageNumber - 1) * pageSize;
            int end = (pageSize + start) > formattedTxnList.size() ? formattedTxnList.size() : (pageSize + start);
            for (int k = start; k < end; k++) {
                returnTxnList.add(formattedTxnList.get(k));
            }
        }

        //获取账户余额，可提取的ong，待提取的ong
        List<Object> balanceList = getAddressBalance(address, "");

        Map<String, Object> rs = new HashMap<>();
        rs.put("AssetBalance", balanceList);
        rs.put("TxnList", returnTxnList);

        return Helper.result("QueryAddressInfo", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);
    }

    /**
     * 针对一个地址有T笔1对N转账or一笔1对M转账的特殊处理(T*N>pageNumber*pageSize*3 or M>pageNumber*pageSize*3)
     *
     * @param address
     * @param pageNumber
     * @param pageSize
     * @param amount
     * @param assetName
     * @return
     */
    private List<Map> queryAddressInfoSpe(String address, int pageNumber, int pageSize, int amount, String assetName) {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("Address", address);
        paramMap.put("AssetName", assetName);
        //int txnAmount = transactionDetailMapper.selectTxnAmountByAddressInfo(paramMap);
        //db查询返回总条数or分页的前几十条
//        int dbReturnNum = (pageNumber * pageSize) > txnAmount ? txnAmount : (pageNumber * pageSize);
        paramMap.put("Start", 0);
        paramMap.put("PageSize", amount);

        List<Map> fromAddrTxnList = transactionDetailMapper.selectTxnByFromAddressInfo(paramMap);
        List<Map> toAddrTxnList = transactionDetailMapper.selectTxnByToAddressInfo(paramMap);

        List<Map> dbTxnList = new ArrayList<>();
        dbTxnList.addAll(fromAddrTxnList);
        dbTxnList.addAll(toAddrTxnList);

        if (fromAddrTxnList.size() != 0 && toAddrTxnList.size() != 0) {
            sortTxnList(dbTxnList);
        }

        List<Map> formattedTxnList = formatTransferTxnList2(dbTxnList);

        List<Map> returnTxnList = new LinkedList<>();
        //先查询出txnlist，再根据请求条数进行分页
        //根据分页确认start，end即请求的pageSize条数
        int start = (pageNumber - 1) * pageSize <= 0 ? 0 : (pageNumber - 1) * pageSize;
        int end = (pageSize + start) > formattedTxnList.size() ? formattedTxnList.size() : (pageSize + start);
        for (int k = start; k < end; k++) {
            returnTxnList.add(formattedTxnList.get(k));
        }

        return returnTxnList;
    }

    @Override
    public OldResult queryAddressInfo(String address, int pageNumber, int pageSize, String assetName) {

        if (address.length() != 34) {
            return Helper.result("QueryAddressInfo", ErrorInfo.PARAM_ERROR.code(), ErrorInfo.PARAM_ERROR.desc(), VERSION, false);
        }

        if (pageSize > configParam.QUERYADDRINFO_PAGESIZE) {
            return Helper.result("QueryAddressInfo", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, "pageSize limit " + configParam.QUERYADDRINFO_PAGESIZE);
        }

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("Address", address);
        paramMap.put("AssetName", assetName);
        paramMap.put("Start", 0);
        paramMap.put("PageSize", pageNumber * pageSize * 3);

        List<Map> fromAddrTxnList = transactionDetailMapper.selectTxnByFromAddressInfo(paramMap);
        List<Map> toAddrTxnList = transactionDetailMapper.selectTxnByToAddressInfo(paramMap);

        List<Map> dbTxnList = new ArrayList<>();
        dbTxnList.addAll(fromAddrTxnList);
        dbTxnList.addAll(toAddrTxnList);

        if (fromAddrTxnList.size() != 0 && toAddrTxnList.size() != 0) {
            sortTxnList(dbTxnList);
        }

        List<Map> formattedTxnList = formatTransferTxnList2(dbTxnList);

        List<Map> returnTxnList = new LinkedList<>();

        if (formattedTxnList.size() < pageSize * pageNumber && formattedTxnList.size() > 0) {
            int amount = transactionDetailMapper.selectAddressRecordAmount(address);
            //针对一个地址有T笔1对N转账or一笔1对M转账的特殊处理(T*N>pageNumber*pageSize*3 or M>pageNumber*pageSize*3)
            if (amount > pageNumber * pageSize * 3) {
                returnTxnList = queryAddressInfoSpe(address, pageNumber, pageSize, amount, assetName);
            } else {
                //先查询出txnlist，再根据请求条数进行分页
                //根据分页确认start，end即请求的pageSize条数
                int start = (pageNumber - 1) * pageSize <= 0 ? 0 : (pageNumber - 1) * pageSize;
                int end = (pageSize + start) > formattedTxnList.size() ? formattedTxnList.size() : (pageSize + start);
                for (int k = start; k < end; k++) {
                    returnTxnList.add(formattedTxnList.get(k));
                }
            }
        } else {
            //先查询出txnlist，再根据请求条数进行分页
            //根据分页确认start，end即请求的pageSize条数
            int start = (pageNumber - 1) * pageSize <= 0 ? 0 : (pageNumber - 1) * pageSize;
            int end = (pageSize + start) > formattedTxnList.size() ? formattedTxnList.size() : (pageSize + start);
            for (int k = start; k < end; k++) {
                returnTxnList.add(formattedTxnList.get(k));
            }
        }

        //获取账户余额，可提取的ong，待提取的ong
        List<Object> balanceList = getAddressBalance(address, "");

        Map<String, Object> rs = new HashMap<>();
        rs.put("AssetBalance", balanceList);
        rs.put("TxnList", returnTxnList);

        return Helper.result("QueryAddressInfo", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);

    }

    @Override
    public OldResult queryAddressInfoByTimeAndPage(String address, String assetName, int pageSize, int endTime) {

        if (address.length() != 34) {
            return Helper.result("QueryAddressInfo", ErrorInfo.PARAM_ERROR.code(), ErrorInfo.PARAM_ERROR.desc(), VERSION, false);
        }

        Map<String, Object> parmMap = new HashMap<>();
        parmMap.put("Address", address);
        parmMap.put("EndTime", endTime);
        parmMap.put("PageSize", pageSize);

        List<Map> dbTxnList = new ArrayList<>();

        if ("dragon".equals(assetName)) {
            parmMap.put("AssetName", assetName + "%");
            dbTxnList = transactionDetailMapper.selectTxnByAddressInfoAndTimePageDragon(parmMap);
        } else {
            parmMap.put("AssetName", assetName);
            dbTxnList = transactionDetailMapper.selectTxnByAddressInfoAndTimePage(parmMap);
        }

        //格式化转账交易列表
        List<Map> rsList = formatTransferTxnList(dbTxnList);
        //获取账户余额，可提取的ong，待提取的ong
        List<Object> balanceList = getAddressBalance(address, assetName);

        Map<String, Object> rs = new HashMap<>();
        rs.put("AssetBalance", balanceList);
        rs.put("TxnList", rsList);
        //rs.put("TxnTotal", txnAmount);

        return Helper.result("QueryAddressInfo", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);

    }

    @Override
    public OldResult queryAddressInfoByTime(String address, String assetName, int beginTime, int endTime) {

        if (address.length() != 34) {
            return Helper.result("QueryAddressInfo", ErrorInfo.PARAM_ERROR.code(), ErrorInfo.PARAM_ERROR.desc(), VERSION, false);
        }

        Map<String, Object> parmMap = new HashMap<>();
        parmMap.put("Address", address);
        parmMap.put("AssetName", assetName);
        parmMap.put("BeginTime", beginTime);
        parmMap.put("EndTime", endTime);


        List<Map> dbTxnList = new ArrayList<>();
        if ("dragon".equals(assetName)) {
            parmMap.put("AssetName", assetName + "%");
            dbTxnList = transactionDetailMapper.selectTxnByAddressInfoAndTimeDragon(parmMap);
        } else {
            parmMap.put("AssetName", assetName);
            dbTxnList = transactionDetailMapper.selectTxnByAddressInfoAndTime(parmMap);
        }


        //格式化转账交易列表
        List<Map> rsList = formatTransferTxnList(dbTxnList);
        //获取账户余额，可提取的ong，待提取的ong
        List<Object> balanceList = getAddressBalance(address, assetName);

        Map<String, Object> rs = new HashMap<>();
        rs.put("AssetBalance", balanceList);
        rs.put("TxnList", rsList);

        return Helper.result("QueryAddressInfo", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);

    }

    @Override
    public OldResult queryAddressInfoByTime(String address, String assetName, int beginTime) {

        if (address.length() != 34) {
            return Helper.result("QueryAddressInfo", ErrorInfo.PARAM_ERROR.code(), ErrorInfo.PARAM_ERROR.desc(), VERSION, false);
        }

        Map<String, Object> parmMap = new HashMap<>();
        parmMap.put("Address", address);
        parmMap.put("AssetName", assetName);
        parmMap.put("BeginTime", beginTime);

        List<Map> dbTxnList = transactionDetailMapper.selectTxnByAddressInfoAndTime(parmMap);

        //格式化转账交易列表
        List<Map> rsList = formatTransferTxnList(dbTxnList);
        //获取账户余额，可提取的ong，待提取的ong
        List<Object> balanceList = getAddressBalance(address, assetName);

        Map<String, Object> rs = new HashMap<>();
        rs.put("AssetBalance", balanceList);
        rs.put("TxnList", rsList);

        return Helper.result("QueryAddressInfo", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);

    }

    @Override
    public OldResult queryAddressBalance(String address) {
        if (address.length() != 34) {
            return Helper.result("QueryAddressInfo", ErrorInfo.PARAM_ERROR.code(), ErrorInfo.PARAM_ERROR.desc(), VERSION, false);
        }

        List balanceList = getAddressBalance(address, "");
        return Helper.result("QueryAddressBalance", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, balanceList);
    }


    /**
     * 获取账户余额，可提取的ong，待提取的ong
     *
     * @param address
     * @return
     */
    private List getAddressBalance(String address, String assetName) {

        List<Object> balanceList = new ArrayList<>();
        if (address.length() != 34) {
            return balanceList;
        }

        initSDK();
        Map<String, Object> balanceMap = sdk.getNativeAssetBalance(address);

        if (Helper.isEmptyOrNull(assetName) || ConstantParam.ONG.equals(assetName)) {

            Map<String, Object> ongMap = new HashMap<>();
            ongMap.put("AssetName", "ong");
            ongMap.put("Balance", (new BigDecimal((String) balanceMap.get("ong")).divide(ConstantParam.ONT_TOTAL)).toPlainString());
            balanceList.add(ongMap);

            //计算等待提取的ong
            String waitBoundOng = calculateWaitingBoundOng(address, (String) balanceMap.get("ont"));
            Map<String, Object> waitBoundOngMap = new HashMap<>();
            waitBoundOngMap.put("AssetName", "waitboundong");
            waitBoundOngMap.put("Balance", waitBoundOng);
            balanceList.add(waitBoundOngMap);


            Map<String, Object> unBoundOngMap = new HashMap<>();
            unBoundOngMap.put("AssetName", "unboundong");
            //获取可提取的ong
            String unBoundOng = sdk.getUnBoundOng(address);
            if (Helper.isEmptyOrNull(unBoundOng)) {
                unBoundOng = "0";
            }
            unBoundOngMap.put("Balance", unBoundOng);
            balanceList.add(unBoundOngMap);

            if (Helper.isEmptyOrNull(assetName)) {
                Map<String, Object> ontMap = new HashMap<>();
                ontMap.put("AssetName", "ont");
                ontMap.put("Balance", balanceMap.get("ont"));
                balanceList.add(ontMap);
            }

        } else if (ConstantParam.ONT.equals(assetName)) {

            Map<String, Object> ontMap = new HashMap<>();
            ontMap.put("AssetName", "ont");
            ontMap.put("Balance", balanceMap.get("ont"));
            balanceList.add(ontMap);
        }

        if (Helper.isEmptyOrNull(assetName) || assetName.startsWith("pumpkin")) {
            balanceList = getPumpkinBalance(sdk, balanceList, address, assetName);
        }

        //OEP4余额
        List<Map> oep4s = oep4Mapper.selectAllKeyInfo();
        for (Map map : oep4s) {
            String contract = (String) map.get("Contract");
            BigDecimal balance = new BigDecimal(sdk.getOep4AssetBalance(address, contract)).divide(new BigDecimal(Math.pow(10, ((BigDecimal) map.get("Decimals")).intValue())));
            if (balance.equals(new BigDecimal(0))) {
                continue;
            }

            String symbol = (String) map.get("Symbol");
            Map<String, Object> oep4Map = new HashMap<>();
            oep4Map.put("AssertType", "OEP4");
            oep4Map.put("AssetName", symbol);
            oep4Map.put("Balance", balance.toPlainString());
            balanceList.add(oep4Map);
        }

        //OEP5余额
        List<Map> oep5s = oep5Mapper.selectAllKeyInfo();
        for (Map map : oep5s) {
            String contract = (String) map.get("Contract");
            BigDecimal balance = new BigDecimal(sdk.getOep5AssetBalance(address, contract));
            if (balance.equals(new BigDecimal(0))) {
                continue;
            }

            String symbol = (String) map.get("Symbol");
            Map<String, Object> oep4Map = new HashMap<>();
            oep4Map.put("AssertType", "OEP5");
            oep4Map.put("AssetName", symbol);
            oep4Map.put("Balance", balance.toPlainString());
            balanceList.add(oep4Map);
        }

        //OEP8余额
        List<Map> oep8s = oep8Mapper.selectAllKeyInfo();
        for (Map map : oep8s) {
            String contract = (String) map.get("Contract");
            String symbol = (String) map.get("Symbol");
            if (symbol.startsWith("pumpkin")) {
                continue;
            }

            JSONArray balanceArray = sdk.getOpe8AssetBalance(address, contract);
            String[] symbols = symbol.split(",");
            for (int i = 0; i < symbols.length; i++) {
                if (Integer.parseInt((String) balanceArray.get(i)) == 0) {
                    continue;
                }

                Map<String, Object> oep8Map = new HashMap<>();
                oep8Map.put("AssertType", "OEP8");
                oep8Map.put("AssetName", symbols[i]);
                oep8Map.put("Balance", balanceArray.get(i));
                balanceList.add(oep8Map);
            }
        }

        return balanceList;
    }


    /**
     * 查询南瓜余额
     *
     * @param sdkService
     * @param balanceList
     * @param address
     * @return
     */
    private List getPumpkinBalance(OntologySDKService sdkService, List<Object> balanceList, String address, String assetName) {

        JSONArray oep8Balance = sdkService.getOpe8AssetBalance(address, configParam.OEP8_PUMPKIN_CODEHASH);

        int pumpkinTotal = 0;
        for (Object obj :
                oep8Balance) {
            pumpkinTotal = pumpkinTotal + Integer.valueOf((String) obj);
        }
        Map<String, Object> pumpkinMap = new HashMap<>();
        pumpkinMap.put("AssertType", "OEP8");
        switch (assetName) {
            case "pumpkin08":
                pumpkinMap.put("AssetName", assetName);
                pumpkinMap.put("Balance", oep8Balance.get(7).toString());
                balanceList.add(pumpkinMap);
                break;
            case "pumpkin07":
                pumpkinMap.put("AssetName", assetName);
                pumpkinMap.put("Balance", oep8Balance.get(6).toString());
                balanceList.add(pumpkinMap);
                break;
            case "pumpkin06":
                pumpkinMap.put("AssetName", assetName);
                pumpkinMap.put("Balance", oep8Balance.get(5).toString());
                balanceList.add(pumpkinMap);
                break;
            case "pumpkin05":
                pumpkinMap.put("AssetName", assetName);
                pumpkinMap.put("Balance", oep8Balance.get(4).toString());
                balanceList.add(pumpkinMap);
                break;
            case "pumpkin04":
                pumpkinMap.put("AssetName", assetName);
                pumpkinMap.put("Balance", oep8Balance.get(3).toString());
                balanceList.add(pumpkinMap);
                break;
            case "pumpkin03":
                pumpkinMap.put("AssetName", assetName);
                pumpkinMap.put("Balance", oep8Balance.get(2).toString());
                balanceList.add(pumpkinMap);
                break;
            case "pumpkin02":
                pumpkinMap.put("AssetName", assetName);
                pumpkinMap.put("Balance", oep8Balance.get(1).toString());
                balanceList.add(pumpkinMap);
                break;
            case "pumpkin01":
                pumpkinMap.put("AssetName", assetName);
                pumpkinMap.put("Balance", oep8Balance.get(0).toString());
                balanceList.add(pumpkinMap);
                break;
            case "":
                getAllPumpkinBalance(oep8Balance, balanceList);
                break;
        }

        Map<String, Object> pumpkinMap2 = new HashMap<>();
        pumpkinMap2.put("AssetName", "totalpumpkin");
        pumpkinMap2.put("Balance", String.valueOf(pumpkinTotal));
        balanceList.add(pumpkinMap2);

        return balanceList;
    }

    /**
     * 获取所有南瓜余额
     *
     * @param oep8Balance
     * @param balanceList
     * @return
     */
    private List<Object> getAllPumpkinBalance(JSONArray oep8Balance, List<Object> balanceList) {

        Map<String, Object> pumpkinMap8 = new HashMap<>();
        pumpkinMap8.put("AssertType", "OEP8");
        pumpkinMap8.put("AssetName", "pumpkin08");
        pumpkinMap8.put("Balance", oep8Balance.get(7).toString());
        balanceList.add(pumpkinMap8);

        Map<String, Object> pumpkinMap7 = new HashMap<>();
        pumpkinMap7.put("AssertType", "OEP8");
        pumpkinMap7.put("AssetName", "pumpkin07");
        pumpkinMap7.put("Balance", oep8Balance.get(6).toString());
        balanceList.add(pumpkinMap7);

        Map<String, Object> pumpkinMap6 = new HashMap<>();
        pumpkinMap6.put("AssertType", "OEP8");
        pumpkinMap6.put("AssetName", "pumpkin06");
        pumpkinMap6.put("Balance", oep8Balance.get(5).toString());
        balanceList.add(pumpkinMap6);

        Map<String, Object> pumpkinMap5 = new HashMap<>();
        pumpkinMap5.put("AssertType", "OEP8");
        pumpkinMap5.put("AssetName", "pumpkin05");
        pumpkinMap5.put("Balance", oep8Balance.get(4).toString());
        balanceList.add(pumpkinMap5);

        Map<String, Object> pumpkinMap4 = new HashMap<>();
        pumpkinMap4.put("AssertType", "OEP8");
        pumpkinMap4.put("AssetName", "pumpkin04");
        pumpkinMap4.put("Balance", oep8Balance.get(3).toString());
        balanceList.add(pumpkinMap4);

        Map<String, Object> pumpkinMap3 = new HashMap<>();
        pumpkinMap3.put("AssertType", "OEP8");
        pumpkinMap3.put("AssetName", "pumpkin03");
        pumpkinMap3.put("Balance", oep8Balance.get(2).toString());
        balanceList.add(pumpkinMap3);

        Map<String, Object> pumpkinMap2 = new HashMap<>();
        pumpkinMap2.put("AssertType", "OEP8");
        pumpkinMap2.put("AssetName", "pumpkin02");
        pumpkinMap2.put("Balance", oep8Balance.get(1).toString());
        balanceList.add(pumpkinMap2);

        Map<String, Object> pumpkinMap1 = new HashMap<>();
        pumpkinMap1.put("AssertType", "OEP8");
        pumpkinMap1.put("AssetName", "pumpkin01");
        pumpkinMap1.put("Balance", oep8Balance.get(0).toString());
        balanceList.add(pumpkinMap1);

        return balanceList;
    }

    /**
     * 计算待提取的ong
     *
     * @param address
     * @param ont
     * @return
     */
    private String calculateWaitingBoundOng(String address, String ont) {

        Integer txntime = transactionDetailMapper.selectLastONTTransferTxnTime(address);

        if (Helper.isEmptyOrNull(txntime)) {
            return "0";
        }

        long now = System.currentTimeMillis() / 1000L;
        log.info("txntime:{},now:{}", txntime, now);

        BigDecimal totalOng = new BigDecimal(now).subtract(new BigDecimal(txntime)).multiply(configParam.ONG_SECOND_GENERATE);
        BigDecimal ong = totalOng.multiply(new BigDecimal(ont)).divide(ConstantParam.ONT_TOTAL);

        return ong.toPlainString();
    }

    /**
     * 格式化转账交易列表
     *
     * @param dbTxnList
     * @return
     */
    private List<Map> formatTransferTxnList(List<Map> dbTxnList) {

        //保持原排序不变动
        Map<String, Object> txnInfoMap = new LinkedHashMap<>();

        int previousTxnIndex = 0;
        for (Map map :
                dbTxnList) {

            map.put("Fee", ((BigDecimal) map.get("Fee")).toPlainString());
            String assetName = (String) map.get("AssetName");
            BigDecimal amount = (BigDecimal) map.get("Amount");
            //ONG 精度格式化
            if (ConstantParam.ONG.equals(assetName)) {
                amount = amount.divide(ConstantParam.ONT_TOTAL);
            }

            String txnhash = (String) map.get("TxnHash");
            // log.info("txnhash:{}", txnhash);
            if (txnInfoMap.containsKey(txnhash)) {
                //自己给自己转账，sql会查询出两条记录.
                if (previousTxnIndex != (Integer) map.get("TxnIndex")) {
                    Map tempMap = (Map) txnInfoMap.get(txnhash);
                    List<Map> transferTxnList = (List<Map>) tempMap.get("TransferList");

                    Map<String, Object> transfertxnListMap = new HashMap<>();
                    transfertxnListMap.put("Amount", amount.toPlainString());
                    transfertxnListMap.put("FromAddress", map.get("FromAddress"));
                    transfertxnListMap.put("ToAddress", map.get("ToAddress"));
                    transfertxnListMap.put("AssetName", assetName);

                    transferTxnList.add(transfertxnListMap);
                }
                previousTxnIndex = (Integer) map.get("TxnIndex");
            } else {

                Map<String, Object> transfertxnListMap = new HashMap<>();
                transfertxnListMap.put("Amount", amount.toPlainString());
                transfertxnListMap.put("FromAddress", map.get("FromAddress"));
                transfertxnListMap.put("ToAddress", map.get("ToAddress"));
                transfertxnListMap.put("AssetName", assetName);
                List<Map> transferTxnList = new ArrayList<>();
                transferTxnList.add(transfertxnListMap);

                map.put("TransferList", transferTxnList);

                map.remove("FromAddress");
                map.remove("ToAddress");
                map.remove("Amount");
                map.remove("AssetName");

                previousTxnIndex = (Integer) map.get("TxnIndex");
                map.remove("TxnIndex");

                txnInfoMap.put((String) map.get("TxnHash"), map);
            }
        }

        List<Map> rsList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : txnInfoMap.entrySet()) {
            rsList.add((Map) entry.getValue());
        }

        return rsList;
    }


    /**
     * 根据height倒序,再txnhash,txnindex正序排序
     *
     * @param list
     * @return
     */
    private List<Map<String, Object>> sortTxnList(List list) {

        Collections.sort(list, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Integer height1 = (Integer) o1.get("Height");
                Integer height2 = (Integer) o2.get("Height");

                if (height1.compareTo(height2) == 0) {
                    String txnHash1 = o1.get("TxnHash").toString();
                    String txnHash2 = o2.get("TxnHash").toString();
                    if (txnHash1.compareTo(txnHash2) == 0) {
                        if (o1.containsKey("TxnIndex") && o2.containsKey("TxnIndex")) {
                            Integer txnindex1 = (Integer) o1.get("TxnIndex");
                            Integer txnindex2 = (Integer) o2.get("TxnIndex");
                            return txnindex1.compareTo(txnindex2);
                        }
                        return txnHash1.compareTo(txnHash2);
                    } else {
                        return txnHash1.compareTo(txnHash2);
                    }
                } else {
                    return -height1.compareTo(height2);
                }
            }
        });

        return list;
    }


    /**
     * 格式化转账交易列表
     *
     * @param dbTxnList
     * @return
     */
    private List<Map> formatTransferTxnList2(List<Map> dbTxnList) {

        List<Map> formattedTxnList = new ArrayList<>();

        String previousTxnHash = "";
        int previousTxnIndex = 0;
        for (int i = 0; i < dbTxnList.size(); i++) {
            Map map = dbTxnList.get(i);
            //金额转换string给前端显示
            map.put("Fee", ((BigDecimal) map.get("Fee")).toPlainString());
            //ONG精度格式化
            String assetName = (String) map.get("AssetName");
            BigDecimal amount = (BigDecimal) map.get("Amount");
            if (ConstantParam.ONG.equals(assetName)) {
                amount = amount.divide(ConstantParam.ONT_TOTAL);
            }

            String txnhash = (String) map.get("TxnHash");
            //   log.info("txnhash:{}", txnhash);

            if (txnhash.equals(previousTxnHash)) {
                //自己给自己转账，sql会查询出两条记录.
                if (previousTxnIndex != (Integer) map.get("TxnIndex")) {

                    Map<String, Object> transfertxnListMap = new HashMap<>();
                    transfertxnListMap.put("Amount", amount.toPlainString());
                    transfertxnListMap.put("FromAddress", map.get("FromAddress"));
                    transfertxnListMap.put("ToAddress", map.get("ToAddress"));
                    transfertxnListMap.put("AssetName", assetName);

                    List<Map> transferTxnList = (List<Map>) (formattedTxnList.get(formattedTxnList.size() - 1)).get("TransferList");
                    transferTxnList.add(transfertxnListMap);
                }
                previousTxnIndex = (Integer) map.get("TxnIndex");
            } else {

                Map<String, Object> transfertxnListMap = new HashMap<>();
                transfertxnListMap.put("Amount", amount.toPlainString());
                transfertxnListMap.put("FromAddress", map.get("FromAddress"));
                transfertxnListMap.put("ToAddress", map.get("ToAddress"));
                transfertxnListMap.put("AssetName", assetName);

                List<Map> transferTxnList = new ArrayList<>();
                transferTxnList.add(transfertxnListMap);

                map.put("TransferList", transferTxnList);

                map.remove("FromAddress");
                map.remove("ToAddress");
                map.remove("Amount");
                map.remove("AssetName");
                previousTxnIndex = (Integer) map.get("TxnIndex");
                map.remove("TxnIndex");

                formattedTxnList.add(map);
            }

            previousTxnHash = txnhash;
        }

        return formattedTxnList;
    }


}
