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

import com.github.ontio.dao.OntIdMapper;
import com.github.ontio.dao.TransactionDetailMapper;
import com.github.ontio.model.OntId;
import com.github.ontio.paramBean.Result;
import com.github.ontio.service.ITransactionService;
import com.github.ontio.utils.*;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/2/27
 */
@Service("TransactionService")
@MapperScan("com.github.ontio.dao")
public class TransactionServiceImpl implements ITransactionService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String VERSION = "1.0";

    @Autowired
    private TransactionDetailMapper transactionDetailMapper;
    @Autowired
    private OntIdMapper ontIdMapper;
    @Autowired
    private ConfigParam configParam;

    private OntologySDKService sdk;

    private synchronized void initSDK() {
        if (sdk == null) {
            sdk = OntologySDKService.getInstance(configParam);
        }
    }

    @Override
    public Result queryTxnList(int amount) {

        List<Map> txnList = transactionDetailMapper.selectTxnWithoutOntId(0, amount);
        for (Map map :
                txnList) {
            map.put("Fee", ((BigDecimal) map.get("Fee")).toPlainString());
        }

        Map<String, Object> rs = new HashMap();
        rs.put("TxnList", txnList);

        return Helper.result("QueryTransaction", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);
    }

    @Override
    public Result queryTxnList(int pageSize, int pageNumber) {

        int start = pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1);

        List<Map> txnList = transactionDetailMapper.selectTxnWithoutOntId(start, pageSize);
        for (Map map :
                txnList) {
            map.put("Fee", ((BigDecimal) map.get("Fee")).toPlainString());
        }

        int amount = transactionDetailMapper.selectTxnWithoutOntIdAmount();

        Map<String, Object> rs = new HashMap();
        rs.put("TxnList", txnList);
        rs.put("Total", amount);

        return Helper.result("QueryTransaction", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);
    }


    @Override
    public Result queryTxnDetailByHash(String txnHash) {

        Map<String, Object> txnInfo = transactionDetailMapper.selectTxnByHash(txnHash);
        if (Helper.isEmptyOrNull(txnInfo)) {
            return Helper.result("QueryTransaction", ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), VERSION, false);
        }

        BigDecimal fee = (BigDecimal) txnInfo.get("Fee");
        txnInfo.put("Fee", fee.toPlainString());

        String desc = (String) txnInfo.get("Description");
        logger.info("txn desc:{}", desc);
        if (ConstantParam.TRANSFER_OPE.equals(desc) || ConstantParam.AUTH_OPE.equals(desc)) {

            List<Map> txnDetailList = transactionDetailMapper.selectTransferTxnDetailByHash(txnHash);
            for (Map map :
                    txnDetailList) {
                BigDecimal amount = (BigDecimal) map.get("Amount");
                String assetName = (String) map.get("AssetName");
                //转换string给前端显示
                if (ConstantParam.ONG.equals(assetName)) {
                    //ONG 精度格式化
                    map.put("Amount", amount.divide(ConstantParam.ONT_TOTAL).toPlainString());
                } else if (ConstantParam.ONT.equals(assetName)) {
                    map.put("Amount", amount.toPlainString());
                }
            }

            Map<String, Object> detailMap = new HashMap<>();
            detailMap.put("TransferList", txnDetailList);
            txnInfo.put("Detail", detailMap);
        } else if (desc.startsWith(ConstantParam.ONTID_OPE_PREFIX)) {

            OntId ontIdInfo = ontIdMapper.selectByPrimaryKey(txnHash);
            String ontId = ontIdInfo.getOntid();
            String ontIdDes = ontIdInfo.getDescription();
            logger.info("ontId:{}, description:{}", ontId, ontIdDes);
            ontIdDes = Helper.templateOntIdOperation(ontIdDes);
            Map temp = new HashMap();
            temp.put("OntId", ontId);
            temp.put("Description", ontIdDes);
            txnInfo.put("Detail", temp);
        }

        return Helper.result("QueryTransaction", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, txnInfo);
    }


    @Override
    public Result queryAddressInfo(String address, int pageNumber, int pageSize) {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("Address", address);

        int txnAmount = transactionDetailMapper.selectTxnAmountByAddressInfo(paramMap);

        int start = (pageNumber - 1) * pageSize < 0 ? 0 : (pageNumber - 1) * pageSize;
        paramMap.put("Start", start);
        paramMap.put("PageSize", pageSize);
        List<Map> dbTxnList = transactionDetailMapper.selectTxnByAddressInfo(paramMap);

        //格式化转账交易列表
        List<Map> rsList = formatTransferTxnList(dbTxnList);
        //获取账户余额，可提取的ong，待提取的ong
        List<Object> balanceList = getAddressBalance(address, "");

        Map<String, Object> rs = new HashMap<>();
        rs.put("AssetBalance", balanceList);
        rs.put("TxnList", rsList);
        rs.put("TxnTotal", txnAmount);

        return Helper.result("QueryAddressInfo", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);
    }

    @Override
    public Result queryAddressInfo(String address, int pageNumber, int pageSize, String assetName) {

        Map<String, Object> parmMap = new HashMap<>();
        parmMap.put("Address", address);
        parmMap.put("AssetName", assetName);

        int txnAmount = transactionDetailMapper.selectTxnAmountByAddressInfo(parmMap);

        int start = (pageNumber - 1) * pageSize < 0 ? 0 : (pageNumber - 1) * pageSize;
        parmMap.put("Start", start);
        parmMap.put("PageSize", pageSize);
        List<Map> dbTxnList = transactionDetailMapper.selectTxnByAddressInfo(parmMap);

        //格式化转账交易列表
        List<Map> rsList = formatTransferTxnList(dbTxnList);
        //获取账户余额，可提取的ong，待提取的ong
        List<Object> balanceList = getAddressBalance(address, assetName);

        Map<String, Object> rs = new HashMap<>();
        rs.put("AssetBalance", balanceList);
        rs.put("TxnList", rsList);
        rs.put("TxnTotal", txnAmount);

        return Helper.result("QueryAddressInfo", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);

    }


    @Override
    public Result queryAddressInfoByTimeAndPage(String address, String assetName, int pageSize, int time) {

        Map<String, Object> rs = new HashMap<>();

        Map<String, Object> parmMap = new HashMap<>();
        parmMap.put("Address", address);
        parmMap.put("AssetName", assetName);
        parmMap.put("Time", time);
        parmMap.put("PageSize", pageSize);

        int txnAmount = transactionDetailMapper.selectTxnAmountByAddressInfo(parmMap);

        List<Map> dbTxnList = transactionDetailMapper.selectTxnByAddressInfoAndTimePage(parmMap);

        //格式化转账交易列表
        List<Map> rsList = formatTransferTxnList(dbTxnList);
        //获取账户余额，可提取的ong，待提取的ong
        List<Object> balanceList = getAddressBalance(address, assetName);

        rs.put("AssetBalance", balanceList);
        rs.put("TxnList", rsList);
        rs.put("TxnTotal", txnAmount);

        return Helper.result("QueryAddressInfo", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);

    }


    @Override
    public Result queryAddressInfoByTime(String address, String assetName, int beginTime, int endTime) {

        Map<String, Object> rs = new HashMap<>();

        Map<String, Object> parmMap = new HashMap<>();
        parmMap.put("Address", address);
        parmMap.put("AssetName", assetName);
        parmMap.put("BeginTime", beginTime);
        parmMap.put("EndTime", endTime);

        int txnAmount = transactionDetailMapper.selectTxnAmountByAddressInfo(parmMap);

        List<Map> dbTxnList = transactionDetailMapper.selectTxnByAddressInfoAndTime(parmMap);

        //格式化转账交易列表
        List<Map> rsList = formatTransferTxnList(dbTxnList);
        //获取账户余额，可提取的ong，待提取的ong
        List<Object> balanceList = getAddressBalance(address, assetName);

        rs.put("AssetBalance", balanceList);
        rs.put("TxnList", rsList);
        rs.put("TxnTotal", txnAmount);

        return Helper.result("QueryAddressInfo", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);


    }


    @Override
    public Result queryAddressInfoByTime(String address, String assetName, int beginTime) {


        Map<String, Object> rs = new HashMap<>();

        Map<String, Object> parmMap = new HashMap<>();
        parmMap.put("Address", address);
        parmMap.put("AssetName", assetName);
        parmMap.put("BeginTime", beginTime);

        int txnAmount = transactionDetailMapper.selectTxnAmountByAddressInfo(parmMap);

        List<Map> dbTxnList = transactionDetailMapper.selectTxnByAddressInfoAndTime(parmMap);

        //格式化转账交易列表
        List<Map> rsList = formatTransferTxnList(dbTxnList);
        //获取账户余额，可提取的ong，待提取的ong
        List<Object> balanceList = getAddressBalance(address, assetName);

        rs.put("AssetBalance", balanceList);
        rs.put("TxnList", rsList);
        rs.put("TxnTotal", txnAmount);

        return Helper.result("QueryAddressInfo", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);

    }


    /**
     * 获取账户余额，可提取的ong，待提取的ong
     *
     * @param address
     * @return
     */
    private List getAddressBalance(String address, String assetName) {

        List<Object> balanceList = new ArrayList<>();

        initSDK();

        Map<String, Object> balanceMap = sdk.getAddressBalance(address);
        if (Helper.isEmptyOrNull(balanceMap)) {
            return balanceList;
        }

        if(Helper.isEmptyOrNull(assetName) || ConstantParam.ONG.equals(assetName)) {

            //计算等待提取的ong
            String waitBoundOng = calculateWaitingBoundOng(address, (String) balanceMap.get("ont"));

            Map<String, Object> waitBoundOngMap = new HashMap<>();
            waitBoundOngMap.put("AssetName", "waitboundong");
            waitBoundOngMap.put("Balance", waitBoundOng);

            balanceList.add(waitBoundOngMap);

            String unBoundOng = "0";
            Map<String, Object> unBoundOngMap = new HashMap<>();
            unBoundOngMap.put("AssetName", "unboundong");
            //获取可提取的ong
            String  unBoundOng2 = sdk.getUnBoundOng(address);
            if(!Helper.isEmptyOrNull(unBoundOng)) {
                unBoundOng = unBoundOng2;
            }
            unBoundOngMap.put("Balance", unBoundOng);

            balanceList.add(unBoundOngMap);

            Map<String,Object> ongMap = new HashMap<>();
            ongMap.put("AssetName", "ong");
            ongMap.put("Balance", (new BigDecimal((String) balanceMap.get("ong")).divide(ConstantParam.ONT_TOTAL)).toPlainString());
            balanceList.add(ongMap);

            if(Helper.isEmptyOrNull(assetName)) {
                Map<String,Object> ontMap = new HashMap<>();
                ontMap.put("AssetName", "ont");
                ontMap.put("Balance", balanceMap.get("ont"));
                balanceList.add(ontMap);
            }

        }else if(ConstantParam.ONT.equals(assetName)) {

            Map<String,Object> ontMap = new HashMap<>();
            ontMap.put("AssetName", "ont");
            ontMap.put("Balance", balanceMap.get("ont"));
            balanceList.add(ontMap);
        }

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

        int txntime = transactionDetailMapper.selectLastONTTransferTxnTime(address);
        if (txntime == 0) {
            txntime = configParam.GENESISBLOCKTIME;
        }
        long now = System.currentTimeMillis() / 1000L;
        logger.info("txntime:{},now:{}", txntime, now);

        BigDecimal totalOng = new BigDecimal(now).subtract(new BigDecimal(txntime)).multiply(ConstantParam.ONG_SECONDMAKE);
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

        Map<String, Object> txnInfoMap = new HashMap<>();

        for (Map map :
                dbTxnList) {

            String txnhash = (String) map.get("TxnHash");
            logger.info("txnhash:{}", txnhash);
            map.put("Fee",((BigDecimal)map.get("Fee")).toPlainString());

            if (txnInfoMap.containsKey(txnhash)) {
                Map tempMap = (Map) txnInfoMap.get(txnhash);
                List<Map> transferTxnList = (List<Map>) tempMap.get("TransferList");

                String assetName = (String) map.get("AssetName");
                BigDecimal amount = (BigDecimal) map.get("Amount");
                //转换string给前端显示
                //ONG 精度格式化
                if (ConstantParam.ONG.equals(assetName)) {
                    amount = amount.divide(ConstantParam.ONT_TOTAL);
                }

                Map<String, Object> transfertxnListMap = new HashMap<>();
                transfertxnListMap.put("Amount", amount.toPlainString());
                transfertxnListMap.put("FromAddress", map.get("FromAddress"));
                transfertxnListMap.put("ToAddress", map.get("ToAddress"));
                // transfertxnListMap.put("AssetName", assetName);

                transferTxnList.add(transfertxnListMap);

            } else {
                String assetName = (String) map.get("AssetName");
                BigDecimal amount = (BigDecimal) map.get("Amount");
                //金额转换string给前端显示
                //ONG 精度格式化
                if (ConstantParam.ONG.equals(assetName)) {
                    amount = amount.divide(ConstantParam.ONT_TOTAL);
                }

                Map<String, Object> transfertxnListMap = new HashMap<>();
                transfertxnListMap.put("Amount", amount.toPlainString());
                transfertxnListMap.put("FromAddress", map.get("FromAddress"));
                transfertxnListMap.put("ToAddress", map.get("ToAddress"));

                //transfertxnListMap.put("AssetName", assetName);

                List<Map> transferTxnList = new ArrayList<>();
                transferTxnList.add(transfertxnListMap);
                map.put("TransferList", transferTxnList);

                map.remove("FromAddress");
                map.remove("ToAddress");
                map.remove("Amount");
                //map.remove("AssetName");

                txnInfoMap.put((String) map.get("TxnHash"), map);
            }

        }

        List<Map> rsList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : txnInfoMap.entrySet()) {
            rsList.add((Map) entry.getValue());
        }

        return rsList;
    }


}
