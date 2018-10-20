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
import java.util.*;

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

        if(pageSize > configParam.QUERYADDRINFO_PAGESIZE) {
            return Helper.result("QueryTransaction", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, "pageSize limit "+configParam.QUERYADDRINFO_PAGESIZE);
        }

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

        if(pageSize > configParam.QUERYADDRINFO_PAGESIZE) {
            return Helper.result("QueryAddressInfo", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, "pageSize limit "+configParam.QUERYADDRINFO_PAGESIZE);
        }

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("Address", address);
        int txnAmount = transactionDetailMapper.selectTxnAmountByAddressInfo(paramMap);
        //db查询返回总条数or分页的前几十条
        int dbReturnNum = (pageNumber * pageSize) > txnAmount ? txnAmount : (pageNumber * pageSize);

        List<Map> rsList = new LinkedList<>();
        //条数差额
        int difAmount = dbReturnNum - rsList.size();

        int limitStart = 0;
        //查询出的所有交易信息map
        Map<String, Object> allTxnInfoMap = new LinkedHashMap<>();

        while (difAmount > 0) {

            paramMap.put("Start", limitStart);
            paramMap.put("PageSize", dbReturnNum * 2);
            //保证转账event在前，手续费event在后
            List<Map> dbTxnList = transactionDetailMapper.selectTxnByAddressInfo(paramMap);
            //格式化转账交易列表，txnlist数量会减少
            List<Map> formattedTxnList = formatTransferTxnList2(dbTxnList, allTxnInfoMap);
            //处理自己给自己转账的交易因为分页导致在两次查询中出现
            delRepetitionTxn(formattedTxnList, rsList);

            int num = rsList.size() + formattedTxnList.size();
            //若此次查询出来的数量+已查询出来的数量 > returnNum，则只选择差额的交易数量。若 <= returnNum,则直接全部添加
            if (num > dbReturnNum) {
                for (int j = 0; j < difAmount; j++) {
                    rsList.add(formattedTxnList.get(j));
                }
            } else {
                rsList.addAll(formattedTxnList);
            }
            difAmount = dbReturnNum - rsList.size();
            limitStart = limitStart + dbReturnNum;
        }

        //先查询出txnlist，再根据请求条数进行分页
        //分页开始index
        int start = (pageNumber - 1) * pageSize < 0 ? 0 : (pageNumber - 1) * pageSize;
        List<Map> returnTxnList = new LinkedList<>();
        if (start < txnAmount) {
            //api返回条数： 交易总数量or根据请求的pageSize对应的条数
            int apiReturnNum = txnAmount > pageSize ? pageSize : txnAmount;
            int end = (apiReturnNum + start) > rsList.size() ? rsList.size() : (apiReturnNum + start);
            for (int k = start; k < end; k++) {
                returnTxnList.add(rsList.get(k));
            }
        }

        //获取账户余额，可提取的ong，待提取的ong
        List<Object> balanceList = getAddressBalance(address, "");

        Map<String, Object> rs = new HashMap<>();
        rs.put("AssetBalance", balanceList);
        rs.put("TxnList", returnTxnList);
        rs.put("TxnTotal", txnAmount);

        return Helper.result("QueryAddressInfo", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);
    }


    @Override
    public Result queryAddressInfo(String address, int pageNumber, int pageSize, String assetName) {

        if(pageSize > configParam.QUERYADDRINFO_PAGESIZE) {
            return Helper.result("QueryAddressInfo", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, "pageSize limit "+configParam.QUERYADDRINFO_PAGESIZE);
        }

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("Address", address);
        paramMap.put("AssetName", assetName);
        int txnAmount = transactionDetailMapper.selectTxnAmountByAddressInfo(paramMap);
        //db查询应返回的条数
        int dbReturnNum = (pageNumber * pageSize) > txnAmount ? txnAmount : (pageNumber * pageSize);

        List<Map> rsList = new LinkedList<>();
        //条数差额
        int difAmount = dbReturnNum - rsList.size();
        //查询出的所有交易信息map
        Map<String, Object> allTxnInfoMap = new LinkedHashMap<>();

        int limitStart = 0;
        while (difAmount > 0) {

            paramMap.put("Start", limitStart);
            paramMap.put("PageSize", dbReturnNum);
            List<Map> dbTxnList = transactionDetailMapper.selectTxnByAddressInfo(paramMap);
            //格式化转账交易列表
            List<Map> formattedTxnList = formatTransferTxnList2(dbTxnList, allTxnInfoMap);
            //处理自己给自己转账的交易因为导致在两次查询中出现
            delRepetitionTxn(formattedTxnList, rsList);

            int num = rsList.size() + formattedTxnList.size();
            //若两次查询出来的数量 > returnNum，则只选择差额的交易数量。若 <= returnNum,则直接全部添加
            if (num > dbReturnNum) {
                for (int j = 0; j < difAmount; j++) {
                    rsList.add(formattedTxnList.get(j));
                }
            } else {
                rsList.addAll(formattedTxnList);
            }
            //差额数量
            difAmount = dbReturnNum - rsList.size();
            limitStart = limitStart + dbReturnNum;
        }
        //先查询出txnlist，再根据请求条数进行分页
        //分页开始index
        int start = (pageNumber - 1) * pageSize < 0 ? 0 : (pageNumber - 1) * pageSize;

        List<Map> returnTxnList = new LinkedList<>();
        if (start < txnAmount) {
            //api返回条数： 交易总数量or根据请求的pageSize对应的条数
            int apiReturnNum = txnAmount > pageSize ? pageSize : txnAmount;
            int end = (apiReturnNum + start) > rsList.size() ? rsList.size() : (apiReturnNum + start);
            for (int k = start; k < end; k++) {
                returnTxnList.add(rsList.get(k));
            }
        }

        //获取账户余额，可提取的ong，待提取的ong
        List<Object> balanceList = getAddressBalance(address, "");

        Map<String, Object> rs = new HashMap<>();
        rs.put("AssetBalance", balanceList);
        rs.put("TxnList", returnTxnList);
        rs.put("TxnTotal", txnAmount);

        return Helper.result("QueryAddressInfo", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);

    }

    /**
     * 处理前一个list的最后一条数据与最新的list的第一条数据重复问题
     * 自己给自己的转账，db查询出来是两条记录，在分页下可能出现在两次查询中都出现
     *
     * @param tempTxnList
     * @param txnList
     * @return
     */
    private static List<Map> delRepetitionTxn(List<Map> tempTxnList, List<Map> txnList) {

        if (txnList.size() > 0) {
            Map tempMap = tempTxnList.get(0);
            Map map = txnList.get(txnList.size() - 1);

            String tempTxnHash = (String) tempMap.get("TxnHash");
            String txnHash = (String) map.get("TxnHash");

            String tempToAddress = (String) ((Map) ((List) tempMap.get("TransferList")).get(0)).get("ToAddress");
            String toAddress = (String) ((Map) ((List) map.get("TransferList")).get(0)).get("ToAddress");

            String tempFromAddress = (String) ((Map) ((List) tempMap.get("TransferList")).get(0)).get("FromAddress");
            String fromAddress = (String) ((Map) ((List) map.get("TransferList")).get(0)).get("FromAddress");

            String tempAssetName = (String) ((Map) ((List) tempMap.get("TransferList")).get(0)).get("AssetName");
            String assetName = (String) ((Map) ((List) map.get("TransferList")).get(0)).get("AssetName");

            String tempAmount = (String) ((Map) ((List) tempMap.get("TransferList")).get(0)).get("Amount");
            String amount = (String) ((Map) ((List) map.get("TransferList")).get(0)).get("Amount");


            if (tempFromAddress.equals(fromAddress) &&
                    tempToAddress.equals(toAddress) &&
                    tempTxnHash.equals(txnHash) &&
                    tempAssetName.equals(assetName) &&
                    tempAmount.equals(amount)) {
                tempTxnList.remove(0);
            }
        }

        return tempTxnList;

    }

    @Override
    public Result queryAddressInfoByTimeAndPage(String address, String assetName, int pageSize, int endTime) {

        Map<String, Object> rs = new HashMap<>();

        Map<String, Object> parmMap = new HashMap<>();
        parmMap.put("Address", address);
        parmMap.put("AssetName", assetName);

        int txnAmount = transactionDetailMapper.selectTxnAmountByAddressInfo(parmMap);

        parmMap.put("EndTime", endTime);
        parmMap.put("PageSize", pageSize);

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


    @Override
    public Result queryAddressBalance(String address) {

        List balanceList = getAddressBalance(address, "");

        return Helper.result("QueryAddressBalance", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, balanceList);
    }


    @Override
    public Result queryAddressList() {

        List<String> addrList = transactionDetailMapper.selectAllAddress();

        Map<String, Object> rsMap = new HashMap<>();
        rsMap.put("Total", addrList.size());
        rsMap.put("AddrList", addrList);

        return Helper.result("QueryAllAddress", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rsMap);
    }

    @Override
    public Result queryLatestTransferTxnAddrInfo(int amount) {

        List<Map> addrTxnInfoList = new ArrayList<>();
        List<Object> addrList = new ArrayList<>();
        int j = 1;

        while (addrTxnInfoList.size() < amount) {
            List<Map> txnList = transactionDetailMapper.selectLatestTransferTxnAddr(amount * (j - 1), amount);
            for (int i = 0; i < txnList.size(); i++) {
                Map map = txnList.get(i);
                if (!addrList.contains(map.get("FromAddress")) && ConstantParam.ONT.equals(map.get("AssetName")) && addrTxnInfoList.size() < amount) {
                    addrList.add(map.get("FromAddress"));
                    addrTxnInfoList.add(map);
                }
            }
            j++;
        }

        initSDK();

        for (Map map :
                addrTxnInfoList) {
            List<Map> balanceList = new ArrayList<>();
            Map<String, Object> balanceMap = sdk.getAddressBalance((String) map.get("FromAddress"));
            balanceList.add(balanceMap);
            map.put("BalanceList", balanceList);
        }

        return Helper.result("QueryLatestTransferTxnAddress", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, addrTxnInfoList);
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

            String unBoundOng = "0";
            Map<String, Object> unBoundOngMap = new HashMap<>();
            unBoundOngMap.put("AssetName", "unboundong");
            //获取可提取的ong
            String unBoundOng2 = sdk.getUnBoundOng(address);
            if (!Helper.isEmptyOrNull(unBoundOng)) {
                unBoundOng = unBoundOng2;
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

            Integer txntime2 = transactionDetailMapper.selectSwapTransferTxnTime(address);
            if (Helper.isEmptyOrNull(txntime2)) {
                return "0";
            } else {
                txntime = txntime2;
            }
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

        //保持原排序不变动
        Map<String, Object> txnInfoMap = new LinkedHashMap<>();

        for (Map map :
                dbTxnList) {

            map.put("Fee", ((BigDecimal) map.get("Fee")).toPlainString());

            String txnhash = (String) map.get("TxnHash");
            logger.info("txnhash:{}", txnhash);

            if (txnInfoMap.containsKey(txnhash)) {

                String fromaddress = (String) map.get("FromAddress");
                String toaddress = (String) map.get("ToAddress");
                //自己给自己转账，sql会查询出两条记录.
                if (!fromaddress.equals(toaddress)) {
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
                    transfertxnListMap.put("AssetName", assetName);

                    transferTxnList.add(transfertxnListMap);
                }

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
                transfertxnListMap.put("AssetName", assetName);
                List<Map> transferTxnList = new ArrayList<>();
                transferTxnList.add(transfertxnListMap);

                map.put("TransferList", transferTxnList);

                map.remove("FromAddress");
                map.remove("ToAddress");
                map.remove("Amount");
                map.remove("AssetName");

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
     * 格式化转账交易列表
     *
     * @param dbTxnList
     * @param allTxnInfoMap 查询出的所有交易信息map
     * @return
     */
    private List<Map> formatTransferTxnList2(List<Map> dbTxnList, Map<String, Object> allTxnInfoMap) {

        //本次查询出的交易map，保持原排序不变动
        Map<String, Object> txnInfoMap = new LinkedHashMap<>();

        for (Map map :
                dbTxnList) {
            //金额转换string给前端显示
            map.put("Fee", ((BigDecimal) map.get("Fee")).toPlainString());

            String txnhash = (String) map.get("TxnHash");
            logger.info("txnhash:{}", txnhash);

            if (allTxnInfoMap.containsKey(txnhash)) {

                String fromaddress = (String) map.get("FromAddress");
                String toaddress = (String) map.get("ToAddress");
                //自己给自己转账，sql会查询出两条记录.
                if (!fromaddress.equals(toaddress)) {

                    String assetName = (String) map.get("AssetName");
                    BigDecimal amount = (BigDecimal) map.get("Amount");
                    //ONG精度格式化
                    if (ConstantParam.ONG.equals(assetName)) {
                        amount = amount.divide(ConstantParam.ONT_TOTAL);
                    }

                    Map<String, Object> transfertxnListMap = new HashMap<>();
                    transfertxnListMap.put("Amount", amount.toPlainString());
                    transfertxnListMap.put("FromAddress", map.get("FromAddress"));
                    transfertxnListMap.put("ToAddress", map.get("ToAddress"));
                    transfertxnListMap.put("AssetName", assetName);

                    List<Map> transferTxnList = (List<Map>) ((Map) allTxnInfoMap.get(txnhash)).get("TransferList");
                    transferTxnList.add(transfertxnListMap);
                }

            } else {
                String assetName = (String) map.get("AssetName");
                BigDecimal amount = (BigDecimal) map.get("Amount");
                //ONG精度格式化
                if (ConstantParam.ONG.equals(assetName)) {
                    amount = amount.divide(ConstantParam.ONT_TOTAL);
                }

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

                txnInfoMap.put((String) map.get("TxnHash"), map);
                allTxnInfoMap.put((String) map.get("TxnHash"), map);
            }

        }
        // allTxnInfoMap.putAll(txnInfoMap);
        List<Map> rsList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : txnInfoMap.entrySet()) {
            rsList.add((Map) entry.getValue());
        }

        return rsList;
    }


}
