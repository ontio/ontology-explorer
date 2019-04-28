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


package com.github.ontio.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.ontio.OntSdk;
import com.github.ontio.common.Address;
import com.github.ontio.common.Helper;
import com.github.ontio.core.payload.DeployCode;
import com.github.ontio.core.transaction.Transaction;
import com.github.ontio.dao.ContractsMapper;
import com.github.ontio.dao.Oep5Mapper;
import com.github.ontio.dao.Oep8Mapper;
import com.github.ontio.model.*;
import com.github.ontio.network.exception.ConnectorException;
import com.github.ontio.network.exception.RestfulException;
import com.github.ontio.smartcontract.neovm.abi.BuildParams;
import com.github.ontio.utils.ConfigParam;
import com.github.ontio.utils.ConstantParam;
import com.github.ontio.utils.OntIdEventDesType;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;


/**
 * @author zhouq
 * @version 1.0
 * @date 2018/3/13
 */
@Slf4j
@Component
public class TxnHandlerThread {

    private static Boolean OEP4TXN = false;

    private static Boolean OEP5TXN = false;

    private static Boolean OEP8TXN = false;

    @Autowired
    private ConfigParam configParam;

    @Autowired
    private ContractsMapper contractsMapper;

    @Autowired
    private Oep5Mapper oep5Mapper;

    @Autowired
    private Oep8Mapper oep8Mapper;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Async
    public Future<String> asyncHandleTxn(JSONObject txnJson, int blockHeight, int blockTime, int indexInBlock) throws Exception {

        //设置一个模式为BATCH，自动提交为false的session，最后统一提交，需防止内存溢出
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        try {
            String threadName = Thread.currentThread().getName();
            log.info("{} run--------blockHeight:{},txnHash:{}", threadName, blockHeight, txnJson.getString("Hash"));
            handleOneTxn(session, txnJson, blockHeight, blockTime, indexInBlock);
            // 手动提交
            session.commit();
            // 清理缓存，防止溢出
            session.clearCache();
            log.info("{} end-------blockHeight:{},txnHash:{}", threadName, blockHeight, txnJson.getString("Hash"));

            return new AsyncResult<String>("success");
        } catch (Exception e) {
            log.error("asyncHandleTxn error...", e);
            session.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void handleOneTxn(SqlSession session, JSONObject txnJson, int blockHeight, int blockTime, int indexInBlock) throws Exception {

        int txnType = txnJson.getInteger("TxType");
        String txnHash = txnJson.getString("Hash");
        String payer = txnJson.getString("Payer");
        String calledContractHash = parseCalledContractHash(txnJson);
        log.info("####txnType:{}, txnHash:{}, payer:{}, calledContractHash:{}####", txnType, txnHash, payer, calledContractHash);

        OEP4TXN = false;
        OEP5TXN = false;
        OEP8TXN = false;
        try {
            JSONObject eventObj = getEventObjByTxnHash(txnHash);
            log.info("eventObj:{}", eventObj.toJSONString());
            //eventstate 1:success 0:failed
            //confimflag 1:success 2:failed
            int confirmFlag = 1;
            if (eventObj.getInteger("State") == 0) {
                confirmFlag = 2;
            }
            BigDecimal gasConsumed = new BigDecimal(eventObj.getLongValue("GasConsumed")).divide(ConstantParam.ONG_DECIMAL);

            if (208 == txnType) {
                //deploy smart contract transaction
                handleDeployContractTx(session, txnJson, blockHeight, blockTime, indexInBlock, confirmFlag, gasConsumed, payer, calledContractHash);
            }

            JSONArray notifyList = eventObj.getJSONArray("Notify");
            if (notifyList.size() == 0) {
                //no event transaction
                insertTxnBasicInfo(session, txnType, txnHash, blockHeight, blockTime, indexInBlock, confirmFlag, "",
                        gasConsumed, 1, 0, "", payer, calledContractHash);
                return;
            }

            // 判断是否为OEP合约交易,用于将属于OEP交易的数据存储在各自OEP详情表中
            judgeIsOepTransaction(notifyList);

            JSONArray stateArray = null;
            for (int i = 0, len = notifyList.size(); i < len; i++) {
                JSONObject notifyObj = (JSONObject) notifyList.get(i);
                String contractAddress = notifyObj.getString("ContractAddress");

                Object object = notifyObj.get("States");
                if (object instanceof JSONArray) {
                    stateArray = (JSONArray) object;
                } else {
                    continue;
                }

                if (configParam.ASSET_ONG_CODEHASH.equals(contractAddress) || configParam.ASSET_ONT_CODEHASH.equals(contractAddress)) {
                    //transfer transaction
                    handleTransferTxn(session, stateArray, txnType, txnHash, blockHeight, blockTime, indexInBlock,
                            contractAddress, gasConsumed, i + 1, notifyList.size(), confirmFlag, payer, calledContractHash);

                } else if (configParam.ONTID_CODEHASH.equals(contractAddress)) {
                    //ontId operation transaction
                    handleOntIdTxn(session, stateArray, txnType, txnHash, blockHeight, blockTime, indexInBlock,
                            gasConsumed, i + 1, contractAddress, payer, calledContractHash);
                    addOneBlockOntIdTxnCount();

                } else if (configParam.CLAIMRECORD_CODEHASH.equals(contractAddress)) {
                    //claimrecord transaction
                    handleClaimRecordTxn(session, stateArray, txnType, txnHash, blockHeight, blockTime, indexInBlock,
                            gasConsumed, i + 1, contractAddress, payer, calledContractHash);

                } else if (configParam.AUTH_CODEHASH.equals(contractAddress)) {
                    //auth transaction
                    insertTxnBasicInfo(session, txnType, txnHash, blockHeight, blockTime, indexInBlock, confirmFlag, ConstantParam.AUTH_OPE_PREFIX,
                            gasConsumed, i + 1, 6, contractAddress, payer, calledContractHash);

                } else if (configParam.OEP8_PUMPKIN_CODEHASH.equals(contractAddress) || ConstantParam.OEP8CONTRACTS.contains(contractAddress)) {
                    //南瓜资产 或者 OEP8交易
                    handleOep8TransferTxn(session, stateArray, txnType, txnHash, blockHeight, blockTime, indexInBlock,
                            gasConsumed, i + 1, confirmFlag, contractAddress, notifyList.size(), payer, calledContractHash);

                } else if (configParam.DRAGON_CODEHASH.equals(contractAddress) || ConstantParam.OEP5CONTRACTS.contains(contractAddress)) {
                    //云斗龙 或者 OEP5交易
                    handleOep5TransferTxn(session, stateArray, txnType, txnHash, blockHeight, blockTime, indexInBlock,
                            gasConsumed, i + 1, confirmFlag, contractAddress, ConstantParam.OEP5MAP.get(contractAddress), payer, calledContractHash);

                } else if (ConstantParam.OEP4CONTRACTS.contains(contractAddress)) {
                    //OEP4交易
                    handleOep4TransferTxn(session, stateArray, txnType, txnHash, blockHeight, blockTime, indexInBlock,
                            gasConsumed, i + 1, confirmFlag, (JSONObject) ConstantParam.OEP4MAP.get(contractAddress), contractAddress, payer, calledContractHash);

                } else {
                    //other transaction
                    insertTxnBasicInfo(session, txnType, txnHash, blockHeight, blockTime, indexInBlock, confirmFlag, "",
                            gasConsumed, i + 1, 0, contractAddress, payer, calledContractHash);
                }
            }
        } catch (RestfulException e) {
            log.error("handleOneTxn RestfulException...{}", e);
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            log.error("handleOneTxn error...", e);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 解析这笔交易真正调用的合约hash
     *
     * @param txnJson
     * @return
     */
    private String parseCalledContractHash(JSONObject txnJson) {

        String code = txnJson.getJSONObject("Payload").getString("Code");
        String calledContractHash = "";

        while (code.contains("67")) {
            int index = code.indexOf("67");
            code = code.substring(index + 2);
            if (code.length() < 40) {
                break;
            } else if (code.length() == 40) {
                calledContractHash = Helper.reverse(code);
                break;
            }
        }
        return calledContractHash;

    }


    /**
     * 处理部署合约交易
     *
     * @param session
     * @param txnJson
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @param confirmFlag
     * @param gasConsumed
     * @param payer
     * @throws Exception
     */
    private void handleDeployContractTx(SqlSession session, JSONObject txnJson, int blockHeight, int blockTime, int indexInBlock,
                                        int confirmFlag, BigDecimal gasConsumed, String payer, String calledContractHash) throws Exception {

        String txnHash = txnJson.getString("Hash");
        int txnType = txnJson.getInteger("TxType");
        JSONObject contractObj = getSmartContractInfo(txnHash);
        String contractAddress = getNativeContractHash(contractObj.getString("contractAddress"));

        contractObj.remove("contractAddress");
        insertTxnBasicInfo(session, txnType, txnHash, blockHeight,
                blockTime, indexInBlock, confirmFlag, contractObj.toString(),
                gasConsumed, 0, 2, contractAddress, payer, calledContractHash);

        // 部署合约，将合约信息保存到合约列表
        Integer count = contractsMapper.selectCountByContractHash(contractAddress);
        if (count.equals(0)) {
            insertContratInfo(contractAddress, blockTime, contractObj, payer);
        }
    }

    /**
     * 遍历notify里event的合约hash判断这笔交易是否是对应的oep交易
     *
     * @param notifyList
     */
    private void judgeIsOepTransaction(JSONArray notifyList) {
        for (int i = 0, len = notifyList.size(); i < len; i++) {
            JSONObject notifyObj = (JSONObject) notifyList.get(i);
            String contractAddress = notifyObj.getString("ContractAddress");

            if (configParam.DRAGON_CODEHASH.equals(contractAddress) || ConstantParam.OEP5CONTRACTS.contains(contractAddress)) {
                OEP5TXN = true;
                break;
            } else if (ConstantParam.OEP4CONTRACTS.contains(contractAddress)) {
                OEP4TXN = true;
                break;
            } else if (ConstantParam.OEP8CONTRACTS.contains(contractAddress)) {
                OEP8TXN = true;
                break;
            }
        }
    }

    /**
     * 累加一个区块里注册ontid的交易
     */
    public synchronized void addOneBlockOntIdCount() {
        ConstantParam.ONEBLOCK_ONTID_AMOUNT++;
    }

    /**
     * 累加一个区块里跟ontid相关的交易
     */
    public synchronized void addOneBlockOntIdTxnCount() {
        ConstantParam.ONEBLOCK_ONTIDTXN_AMOUNT++;
    }


    /**
     * 处理oep8交易
     *
     * @param session
     * @param stateArray
     * @param txnType
     * @param txnHash
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @param gasConsumed
     * @param indexInTxn
     * @param confirmFlag
     * @param contractAddress
     * @param stateSize
     * @throws Exception
     */
    private void handleOep8TransferTxn(SqlSession session, JSONArray stateArray, int txnType, String txnHash, int blockHeight,
                                       int blockTime, int indexInBlock, BigDecimal gasConsumed, int indexInTxn,
                                       int confirmFlag, String contractAddress, int stateSize, String payer, String calledContractHash) throws Exception {
        if (stateArray.size() < 5) {
            Oep8TxnDetail transactionDetailDO = generateTransaction("", "", "", new BigDecimal("0"), txnType, txnHash, blockHeight,
                    blockTime, indexInBlock, confirmFlag, "", gasConsumed, indexInTxn, 1, contractAddress, payer, calledContractHash);
            session.insert("com.github.ontio.dao.TransactionDetailMapper.insertSelective", transactionDetailDO);
            session.insert("com.github.ontio.dao.TransactionDetailDailyMapper.insertSelective", transactionDetailDO);
            session.insert("com.github.ontio.dao.Oep8TxnDetailMapper.insertSelective", transactionDetailDO);
            return;
        }

        String action = new String(Helper.hexToBytes((String) stateArray.get(0)));
        String fromAddress = (String) stateArray.get(1);
        String toAddress = (String) stateArray.get(2);
        String tokenId = (String) stateArray.get(3);
        JSONObject oep8Obj = (JSONObject) ConstantParam.OEP8MAP.get(contractAddress + "-" + tokenId);
        if ("00".equals(fromAddress) && stateSize == 2) {
            // mint方法即增加发行量方法, 区分标志：fromAddress为“00”，同时state数量为2
            Oep8 oep8 = new Oep8();
            oep8.setContract(contractAddress);
            oep8.setTokenid((String) stateArray.get(3));
            ConstantParam.ONT_SDKSERVICE.neovm().oep8().setContractAddress(contractAddress);
            oep8.setTotalsupply(new BigDecimal(ConstantParam.ONT_SDKSERVICE.neovm().oep8().queryTotalSupply(Helper.hexToBytes(tokenId))));
            //TODO 南瓜合成交易，金南瓜会减少

            oep8Mapper.updateTotalSupply(oep8);
        }

        if (40 == fromAddress.length()) {
            fromAddress = Address.parse(fromAddress).toBase58();
        }
        if (40 == toAddress.length()) {
            toAddress = Address.parse(toAddress).toBase58();
        }

        BigDecimal eventAmount = new BigDecimal(Helper.BigIntFromNeoBytes(Helper.hexToBytes((String) stateArray.get(4))).longValue());
        log.info("OEP8TransferTxn:fromaddress:{}, toaddress:{}, tokenid:{}, amount:{}", fromAddress, toAddress, tokenId, eventAmount);

        Oep8TxnDetail oep8TxnDetailDAO = generateTransaction(fromAddress, toAddress, oep8Obj.getString("Name"), eventAmount, txnType, txnHash, blockHeight,
                blockTime, indexInBlock, confirmFlag, action, gasConsumed, indexInTxn, 3, contractAddress, payer, calledContractHash);
        oep8TxnDetailDAO.setTokenname(oep8Obj.getString("Name"));

        session.insert("com.github.ontio.dao.Oep8TxnDetailMapper.insertSelective", oep8TxnDetailDAO);
        session.insert("com.github.ontio.dao.TransactionDetailMapper.insertSelective", oep8TxnDetailDAO);
        session.insert("com.github.ontio.dao.TransactionDetailDailyMapper.insertSelective", oep8TxnDetailDAO);
    }

    /**
     * 处理oep5交易
     *
     * @param session
     * @param stateArray
     * @param txnType
     * @param txnHash
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @param gasConsumed
     * @param indexInTxn
     * @param confirmFlag
     * @param contractAddress
     * @param oep5Obj
     * @throws Exception
     */
    private void handleOep5TransferTxn(SqlSession session, JSONArray stateArray, int txnType, String txnHash, int blockHeight,
                                       int blockTime, int indexInBlock, BigDecimal gasConsumed, int indexInTxn, int confirmFlag,
                                       String contractAddress, Object oep5Obj, String payer, String calledContractHash) throws Exception {

        String action = new String(Helper.hexToBytes((String) stateArray.get(0)));
        if (!(action.equalsIgnoreCase("transfer") || action.equalsIgnoreCase("birth"))) {
            Oep8TxnDetail transactionDetailDO = generateTransaction("", "", "", new BigDecimal("0"), txnType, txnHash, blockHeight,
                    blockTime, indexInBlock, confirmFlag, action, gasConsumed, indexInTxn, 1, contractAddress, payer, calledContractHash);
            session.insert("com.github.ontio.dao.TransactionDetailMapper.insertSelective", transactionDetailDO);
            session.insert("com.github.ontio.dao.TransactionDetailDailyMapper.insertSelective", transactionDetailDO);
            session.insert("com.github.ontio.dao.Oep5TxnDetailMapper.insertSelective", transactionDetailDO);
            return;
        }

        String fromAddress = (String) stateArray.get(1);
        if (40 == fromAddress.length()) {
            fromAddress = Address.parse(fromAddress).toBase58();
        }
        String toAddress = (String) stateArray.get(2);
        if (40 == toAddress.length()) {
            toAddress = Address.parse(toAddress).toBase58();
        }

        String assetName = "";
        if (oep5Obj == null || "HyperDragons".equalsIgnoreCase(((JSONObject) oep5Obj).getString("Name"))) {
            // 如果是birth方法，id位置在2；如果是transfer方法，id位置在3
            String dragonId = "";
            if ("birth".equalsIgnoreCase(action)) {
                dragonId = Helper.BigIntFromNeoBytes(Helper.hexToBytes((String) stateArray.get(2))).toString();
                fromAddress = "";
                toAddress = "";

                Oep5Dragon oep5Dragon = new Oep5Dragon();
                oep5Dragon.setContract(contractAddress);
                oep5Dragon.setAssertname("HyperDragons: " + dragonId);
                oep5Dragon.setJsonurl(getDragonUrl(contractAddress, dragonId));
                session.insert("com.github.ontio.dao.Oep5DragonMapper.insertSelective", oep5Dragon);
            } else {
                dragonId = Helper.BigIntFromNeoBytes(Helper.hexToBytes((String) stateArray.get(3))).toString();
            }
            assetName = "HyperDragons: " + dragonId;
        } else {
            assetName = ((JSONObject) oep5Obj).getString("Name") + ": " + stateArray.get(3);
        }

        log.info("fromaddress:{}, toaddress:{}, dragonId:{}", fromAddress, toAddress, assetName);
        Oep8TxnDetail transactionDetailDO = generateTransaction(fromAddress, toAddress, assetName, new BigDecimal("1"), txnType, txnHash, blockHeight,
                blockTime, indexInBlock, confirmFlag, action, gasConsumed, indexInTxn, 3, contractAddress, payer, calledContractHash);
        if (!"transfer".equalsIgnoreCase(action)) {
            transactionDetailDO.setAmount(ConstantParam.ZERO);
        }

        session.insert("com.github.ontio.dao.TransactionDetailMapper.insertSelective", transactionDetailDO);
        session.insert("com.github.ontio.dao.TransactionDetailDailyMapper.insertSelective", transactionDetailDO);
        session.insert("com.github.ontio.dao.Oep5TxnDetailMapper.insertSelective", transactionDetailDO);
        if ("birth".equalsIgnoreCase(action) && oep5Obj != null) {
            Oep5 oep5 = new Oep5();
            oep5.setContract(contractAddress);
            ConstantParam.ONT_SDKSERVICE.neovm().oep5().setContractAddress(contractAddress);
            oep5.setTotalsupply(new BigDecimal(ConstantParam.ONT_SDKSERVICE.neovm().oep5().queryTotalSupply()));

            oep5Mapper.updateByPrimaryKeySelective(oep5);
        }
    }

    /**
     * 处理原生ont，ong转账
     *
     * @param session
     * @param stateList
     * @param txnType
     * @param txnHash
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @param contractAddress
     * @param gasConsumed
     * @param indexInTxn
     * @param notifyListSize
     * @param confirmFlag
     * @param payer
     * @throws Exception
     */
    private void handleTransferTxn(SqlSession session, JSONArray stateList, int txnType, String txnHash,
                                   int blockHeight, int blockTime, int indexInBlock, String contractAddress,
                                   BigDecimal gasConsumed, int indexInTxn, int notifyListSize, int confirmFlag,
                                   String payer, String calledContractHash) throws Exception {
        if (stateList.size() < 3) {
            Oep8TxnDetail transactionDetailDO = generateTransaction("", "", "", new BigDecimal("0"), txnType, txnHash, blockHeight,
                    blockTime, indexInBlock, confirmFlag, "", gasConsumed, indexInTxn, 1, contractAddress, payer, calledContractHash);
            session.insert("com.github.ontio.dao.TransactionDetailMapper.insertSelective", transactionDetailDO);
            session.insert("com.github.ontio.dao.TransactionDetailDailyMapper.insertSelective", transactionDetailDO);
            return;
        }

        int eventType = 3;
        String assetName = "";
        if (configParam.ASSET_ONT_CODEHASH.equals(contractAddress)) {
            assetName = "ont";
        } else if (configParam.ASSET_ONG_CODEHASH.equals(contractAddress)) {
            assetName = "ong";
        }

        String action = (String) stateList.get(0);
        //手续费不为0的情况下，notifylist的最后一个一定是收取手续费event
        if (gasConsumed.compareTo(ConstantParam.ZERO) != 0 && (indexInTxn == notifyListSize) && configParam.ASSET_ONG_CODEHASH.equals(contractAddress)) {
            action = "gasconsume";
            eventType = 1;
        }

        String fromAddress = (String) stateList.get(1);
        String toAddress = (String) stateList.get(2);
        BigDecimal amount = new BigDecimal((stateList.get(3)).toString());
        log.info("####fromAddress:{},toAddress:{},amount:{}####", fromAddress, toAddress, amount.toPlainString());

        Oep8TxnDetail transactionDetailDO = generateTransaction(fromAddress, toAddress, assetName, amount, txnType, txnHash, blockHeight,
                blockTime, indexInBlock, confirmFlag, action, gasConsumed, indexInTxn, eventType, contractAddress, payer, calledContractHash);

        session.insert("com.github.ontio.dao.TransactionDetailMapper.insertSelective", transactionDetailDO);
        session.insert("com.github.ontio.dao.TransactionDetailDailyMapper.insertSelective", transactionDetailDO);
        // OEP交易的手续费入库
        if (configParam.ASSET_ONG_CODEHASH.equals(contractAddress) && (OEP4TXN || OEP5TXN || OEP8TXN)) {
            insertSelectiveChoise(session, transactionDetailDO);
        }
    }

    /**
     * 处理ontid交易
     *
     * @param session
     * @param stateList
     * @param txnType
     * @param txnHash
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @param gasConsumed
     * @param indexInTxn
     * @param contractAddress
     * @throws Exception
     */
    private void handleOntIdTxn(SqlSession session, JSONArray stateList, int txnType, String txnHash, int blockHeight,
                                int blockTime, int indexInBlock, BigDecimal gasConsumed, int indexInTxn,
                                String contractAddress, String payer, String calledContractHash) throws Exception {

        String action = stateList.getString(0);
        String ontId = "";
        if (OntIdEventDesType.REGISTERONTID.value().equals(action)) {
            ontId = stateList.getString(1);
        } else {
            ontId = stateList.getString(2);
        }
        String descriptionStr = formatOntIdOperation(ontId, action, stateList);

        OntId ontIdDO = new OntId();
        ontIdDO.setHeight(blockHeight);
        ontIdDO.setTxnhash(txnHash);
        ontIdDO.setTxntype(txnType);
        ontIdDO.setDescription(descriptionStr);
        ontIdDO.setTxntime(blockTime);
        ontIdDO.setOntid(ontId);
        ontIdDO.setFee(gasConsumed);
        session.insert("com.github.ontio.dao.OntIdMapper.insertSelective", ontIdDO);

        insertTxnBasicInfo(session, txnType, txnHash, blockHeight, blockTime, indexInBlock, 1, ConstantParam.ONTID_OPE_PREFIX + action,
                gasConsumed, indexInTxn, 4, contractAddress, payer, calledContractHash);

        //如果是注册ontid交易，ontid数量加1
        if (ConstantParam.REGISTER.equals(action)) {
            addOneBlockOntIdCount();
        }
    }

    /**
     * 处理存证交易
     *
     * @param session
     * @param stateList
     * @param txnType
     * @param txnHash
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @param gasConsumed
     * @param indexInTxn
     * @param contractAddress
     * @throws Exception
     */
    private void handleClaimRecordTxn(SqlSession session, JSONArray stateList, int txnType, String txnHash, int blockHeight,
                                      int blockTime, int indexInBlock, BigDecimal gasConsumed, int indexInTxn,
                                      String contractAddress, String payer, String calledContractHash) throws Exception {

        String actionType = new String(Helper.hexToBytes(stateList.getString(0)));
        StringBuilder sb = new StringBuilder(140);
        sb.append(ConstantParam.CLAIMRECORD_OPE);

        if (ConstantParam.CLAIMRECORD_OPE_PREFIX.equals(actionType)) {
            if (stateList.size() >= 4) {
                String issuerOntId = new String(Helper.hexToBytes(stateList.getString(1)));
                String action = new String(Helper.hexToBytes(stateList.getString(2)));
                String claimId = new String(Helper.hexToBytes(stateList.getString(3)));
                log.info("####ClaimRecord:action:{}, issuerOntId:{}, claimid:{}#####", action, issuerOntId, claimId);
                sb.append(issuerOntId);
                sb.append(action);
                sb.append(claimId);
            }
        }

        insertTxnBasicInfo(session, txnType, txnHash, blockHeight, blockTime, indexInBlock, 1, sb.toString(),
                gasConsumed, indexInTxn, 5, contractAddress, payer, calledContractHash);
    }

    /**
     * 构造基本交易信息dao
     *
     * @param fromAddress
     * @param toAddress
     * @param assetName
     * @param account
     * @param txnType
     * @param txnHash
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @param confirmFlag
     * @param action
     * @param gasConsumed
     * @param indexInTxn
     * @param eventType
     * @param contractAddress
     * @param payer
     * @return
     */
    private Oep8TxnDetail generateTransaction(String fromAddress, String toAddress, String assetName, BigDecimal account, int txnType,
                                              String txnHash, int blockHeight, int blockTime, int indexInBlock, int confirmFlag,
                                              String action, BigDecimal gasConsumed, int indexInTxn, int eventType, String contractAddress,
                                              String payer, String calledContractHash) {

        Oep8TxnDetail txDetail = new Oep8TxnDetail();
        txDetail.setFromaddress(fromAddress);
        txDetail.setToaddress(toAddress);
        txDetail.setAssetname(assetName);
        txDetail.setDescription(action);
        txDetail.setFee(gasConsumed);
        txDetail.setHeight(blockHeight);
        txDetail.setBlockindex(indexInBlock);
        txDetail.setTxnhash(txnHash);
        txDetail.setTxntype(txnType);
        txDetail.setTxntime(blockTime);
        txDetail.setAmount(account);
        txDetail.setTxnindex(indexInTxn);
        txDetail.setConfirmflag(confirmFlag);
        txDetail.setEventtype(eventType);
        txDetail.setContracthash(contractAddress);
        txDetail.setPayer(payer);
        txDetail.setCalledcontracthash(calledContractHash);

        return txDetail;
    }

    /**
     * 处理交易基本信息
     *
     * @param session
     * @param txnType
     * @param txnHash
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @param confirmFlag
     * @param action
     * @param gasConsumed
     * @param indexInTxn
     * @param eventType
     * @param contractAddress
     * @param payer
     * @throws Exception
     */
    private void insertTxnBasicInfo(SqlSession session, int txnType, String txnHash, int blockHeight, int blockTime,
                                    int indexInBlock, int confirmFlag, String action, BigDecimal gasConsumed, int indexInTxn,
                                    int eventType, String contractAddress, String payer, String calledContractHash) throws Exception {

        Oep8TxnDetail transactionDetailDO = generateTransaction("", "", "", ConstantParam.ZERO, txnType, txnHash, blockHeight,
                blockTime, indexInBlock, confirmFlag, action, gasConsumed, indexInTxn, eventType, contractAddress, payer, calledContractHash);

        session.insert("com.github.ontio.dao.TransactionDetailMapper.insertSelective", transactionDetailDO);
        session.insert("com.github.ontio.dao.TransactionDetailDailyMapper.insertSelective", transactionDetailDO);

        // OEP合约交易理应插入分表
        if (OEP4TXN || OEP5TXN || OEP8TXN) {
            insertSelectiveChoise(session, transactionDetailDO);
        }
    }

    /**
     * 插入交易记录到对应的oep交易详情表
     *
     * @param session
     * @param transactionDetailDO
     */
    private void insertSelectiveChoise(SqlSession session, Oep8TxnDetail transactionDetailDO) {
        // OEP交易应该入库对应的分表
        if (OEP8TXN) {
            transactionDetailDO.setTokenname("");
            session.insert("com.github.ontio.dao.Oep8TxnDetailMapper.insertSelective", transactionDetailDO);
        } else if (OEP5TXN) {
            session.insert("com.github.ontio.dao.Oep5TxnDetailMapper.insertSelective", transactionDetailDO);
        } else {
            session.insert("com.github.ontio.dao.Oep4TxnDetailMapper.insertSelective", transactionDetailDO);
        }
    }

    /**
     * 记录合约基本信息
     *
     * @param contractAddress
     * @param blockTime
     * @param contractObj
     * @param player
     */
    private void insertContratInfo(String contractAddress, int blockTime, JSONObject contractObj, String player) {
        Contracts contracts = new Contracts();
        contracts.setProject("");
        contracts.setContract(contractAddress);
        contracts.setName(contractObj.getString("Name"));
        contracts.setDescription(contractObj.getString("Description"));
        contracts.setAbi("");
        contracts.setCode("");
        contracts.setTxcount(0);
        contracts.setCreatetime(blockTime);
        contracts.setUpdatetime(blockTime);
        contracts.setAuditflag(0);
        contracts.setCreator(player);
        contracts.setAddresscount(0);
        contracts.setOntcount(new BigDecimal(0));
        contracts.setOngcount(new BigDecimal(0));
        contracts.setTokencount(JSON.toJSONString(new ArrayList<>()));
        contracts.setDappstoreflag(ConstantParam.DAPPSTOREFLAG_NO);
        contracts.setCategory("");
        contractsMapper.insertSelective(contracts);
    }

    /**
     * 原生合约hash需要特殊转换
     *
     * @param contractAddress
     * @return
     */
    private String getNativeContractHash(String contractAddress) {
        String contractHash = "";
        switch (contractAddress) {
            case "0239dcf9b4a46f15c5f23f20d52fac916a0bac0d":
                contractHash = configParam.ASSET_ONT_CODEHASH;
                break;
            case "08b6dcfed2aace9190a44ae34a320e42c04b46ac":
                contractHash = configParam.ASSET_ONG_CODEHASH;
                break;
            case "6815cbe7b4dbad4d2d09ae035141b5254a002f79":
                contractHash = configParam.ONTID_CODEHASH;
                break;
            case "24a15c6aed092dfaa711c4974caf1e9d307bf4b5":
                contractHash = configParam.AUTH_CODEHASH;
                break;
            default:
                contractHash = contractAddress;
        }

        return contractHash;
    }

    /**
     * 处理oep4交易
     *
     * @param session
     * @param stateArray
     * @param txnType
     * @param txnHash
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @param gasConsumed
     * @param indexInTxn
     * @param confirmFlag
     * @param oep4Obj
     * @param contractHash
     * @throws Exception
     */
    private void handleOep4TransferTxn(SqlSession session, JSONArray stateArray, int txnType, String txnHash, int blockHeight,
                                       int blockTime, int indexInBlock, BigDecimal gasConsumed, int indexInTxn, int confirmFlag,
                                       JSONObject oep4Obj, String contractHash, String payer, String calledContractHash) throws Exception {
        if (stateArray.size() < 3) {
            Oep8TxnDetail transactionDetailDO = generateTransaction("", "", "", new BigDecimal("0"), txnType, txnHash, blockHeight,
                    blockTime, indexInBlock, confirmFlag, "", gasConsumed, indexInTxn, 1, contractHash, payer, calledContractHash);
            session.insert("com.github.ontio.dao.TransactionDetailMapper.insertSelective", transactionDetailDO);
            session.insert("com.github.ontio.dao.TransactionDetailDailyMapper.insertSelective", transactionDetailDO);
            session.insert("com.github.ontio.dao.Oep4TxnDetailMapper.insertSelective", transactionDetailDO);
            return;
        }

        String fromAddress = "";
        String toAddress = "";
        BigDecimal eventAmount = new BigDecimal("0");
        //初始化交易列表长度为3
        if (stateArray.size() == 3) {
            fromAddress = (String) stateArray.get(0);
            if (40 == fromAddress.length()) {
                fromAddress = Address.parse(fromAddress).toBase58();
            }
            toAddress = (String) stateArray.get(1);
            if (40 == toAddress.length()) {
                toAddress = Address.parse(toAddress).toBase58();
            }
            eventAmount = new BigDecimal(Helper.BigIntFromNeoBytes(Helper.hexToBytes((String) stateArray.get(2))).longValue());

        } else {
            fromAddress = (String) stateArray.get(1);
            if (40 == fromAddress.length()) {
                fromAddress = Address.parse(fromAddress).toBase58();
            }
            toAddress = (String) stateArray.get(2);
            if (40 == toAddress.length()) {
                toAddress = Address.parse(toAddress).toBase58();
            }

            eventAmount = new BigDecimal(Helper.BigIntFromNeoBytes(Helper.hexToBytes((String) stateArray.get(3))).longValue());
        }
        log.info("OEP4TransferTxn:fromaddress:{}, toaddress:{}, amount:{}", fromAddress, toAddress, eventAmount);

        String assetName = oep4Obj.getString("Symbol");
        BigDecimal amount = eventAmount.divide(new BigDecimal(Math.pow(10, oep4Obj.getInteger("Decimals"))));
        Oep8TxnDetail transactionDetailDO = generateTransaction(fromAddress, toAddress, assetName, amount, txnType, txnHash, blockHeight,
                blockTime, indexInBlock, confirmFlag, "transfer", gasConsumed, indexInTxn, 3, contractHash, payer, calledContractHash);

        session.insert("com.github.ontio.dao.TransactionDetailMapper.insertSelective", transactionDetailDO);
        session.insert("com.github.ontio.dao.TransactionDetailDailyMapper.insertSelective", transactionDetailDO);
        session.insert("com.github.ontio.dao.Oep4TxnDetailMapper.insertSelective", transactionDetailDO);
    }

    /**
     * format ontId operation description
     *
     * @param action
     * @param stateList
     * @return
     */
    private String formatOntIdOperation(String ontId, String action, JSONArray stateList) throws Exception {

        String str = "";
        StringBuilder descriptionSb = new StringBuilder(140);
        descriptionSb.append(action);
        descriptionSb.append(ConstantParam.ONTID_SEPARATOR);

        if (OntIdEventDesType.REGISTERONTID.value().equals(action)) {

            descriptionSb.append(ontId);
            str = descriptionSb.toString();
            log.info("####Register OntId:{}", ontId);

        } else if (OntIdEventDesType.PUBLICKEYOPE.value().equals(action)) {

            String op = stateList.getString(1);
            int publickeyNumber = stateList.getInteger(3);
            String publicKey = stateList.getString(4);
            log.info("####PublicKey op:{},keyNumber:{},publicKey:{}####", op, publickeyNumber, publicKey);

            descriptionSb.append(op);
            descriptionSb.append(ConstantParam.ONTID_SEPARATOR);
            descriptionSb.append(ontId);
            descriptionSb.append(ConstantParam.ONTID_SEPARATOR);
            descriptionSb.append(publicKey);
            descriptionSb.append(ConstantParam.ONTID_SEPARATOR);
            descriptionSb.append(publickeyNumber);
            str = descriptionSb.toString();

        } else if (OntIdEventDesType.ATTRIBUTEOPE.value().equals(action)) {

            String op = stateList.getString(1);
            log.info("####Attribute op:{}####", op);

            descriptionSb.append(op);
            descriptionSb.append(ConstantParam.ONTID_SEPARATOR);
            descriptionSb.append(ontId);
            descriptionSb.append(ConstantParam.ONTID_SEPARATOR);

            if (ConstantParam.ADD.equals(op)) {
                JSONArray attrNameArray = stateList.getJSONArray(3);
                log.info("####attrName:{}####", attrNameArray.toArray());
                for (Object obj :
                        attrNameArray) {
                    String attrName = (String) obj;
                    attrName = new String(com.github.ontio.common.Helper.hexToBytes(attrName));
                    descriptionSb.append(attrName);
                    descriptionSb.append(ConstantParam.ONTID_SEPARATOR2);
                }
            } else {
                String attrName = stateList.getString(3);
                log.info("####attrName:{}####", attrName);
                descriptionSb.append(attrName);
                descriptionSb.append(ConstantParam.ONTID_SEPARATOR2);
            }
            str = descriptionSb.substring(0, descriptionSb.length() - 1);

        } else if (OntIdEventDesType.RECOVERYOPE.value().equals(action)) {

            String op = stateList.getString(1);
            String address = Address.parse(stateList.getString(3)).toBase58();
            log.info("####Recovery op:{}, ontid:{}, address:{}####", op, ontId, address);

            descriptionSb.append(op);
            descriptionSb.append(ConstantParam.ONTID_SEPARATOR);
            descriptionSb.append(ontId);
            descriptionSb.append(ConstantParam.ONTID_SEPARATOR);
            descriptionSb.append(address);

            str = descriptionSb.toString();
        }
        return str;
    }

    /**
     * get the bookkeeper of the block by height
     *
     * @return
     * @throws Exception
     */
    private JSONObject getEventObjByTxnHash(String txnHash) throws Exception {

        JSONObject eventObj = new JSONObject();
        int tryTime = 1;
        while (true) {
            try {
                eventObj = (JSONObject) ConstantParam.ONT_SDKSERVICE.getConnect().getSmartCodeEvent(txnHash);
                break;
            } catch (ConnectorException ex) {
                log.error("getEventObjByTxnHash error, try again...restful: {}, error:", ConstantParam.MASTERNODE_RESTFULURL, ex);
                if (tryTime % configParam.NODE_INTERRUPTTIME_MAX == 0) {
                    switchNode();
                    tryTime++;
                    continue;
                } else {
                    tryTime++;
                    Thread.sleep(1000);
                    continue;
                }
            } catch (IOException ex) {
                log.error("getEventObjByTxnHash thread can't work,error {} ", ex);
                throw new Exception(ex);
            }
        }

        return eventObj;
    }


    /**
     * switch to another node and initialize ONT_SDKSERVICE object
     * when the master node have an exception
     */
    private void switchNode() {

        ConstantParam.MASTERNODE_INDEX++;
        if (ConstantParam.MASTERNODE_INDEX >= configParam.NODE_AMOUNT) {
            ConstantParam.MASTERNODE_INDEX = 0;
        }
        ConstantParam.MASTERNODE_RESTFULURL = ConstantParam.NODE_RESTFULURLLIST.get(ConstantParam.MASTERNODE_INDEX);
        log.warn("####switch node restfulurl to {}####", ConstantParam.MASTERNODE_RESTFULURL);

        OntSdk wm = OntSdk.getInstance();
        wm.setRestful(ConstantParam.MASTERNODE_RESTFULURL);
        ConstantParam.ONT_SDKSERVICE = wm;
    }

    private JSONObject getSmartContractInfo(String txnHash) throws Exception {

        DeployCode deployCodeObj = (DeployCode) ConstantParam.ONT_SDKSERVICE.getConnect().getTransaction(txnHash);
        String code = Helper.toHexString(deployCodeObj.code);
        String contractAddress = Address.AddressFromVmCode(code).toHexString();
        log.info("smartcontract codehash:{}", contractAddress);
        JSONObject contractObj = new JSONObject();
        contractObj.put("contractAddress", contractAddress);
        contractObj.put("Name", "");
        contractObj.put("Description", "");
        //catch底层报错，根据合约hash查不到合约信息
        try {
            contractObj = (JSONObject) ConstantParam.ONT_SDKSERVICE.getConnect().getContractJson(contractAddress);
            contractObj.put("contractAddress", contractAddress);
            log.info("smartcontract obj:{}", contractObj.toJSONString());
            contractObj.remove("Code");
        } catch (Exception e) {
            log.error("error...", e);
        }
        return contractObj;
    }

    private String getDragonUrl(String contractAddress, String tokenId) {
        try {
            List paramList = new ArrayList<>();
            paramList.add("tokenMetadata".getBytes());

            List args = new ArrayList();
            args.add(tokenId);
            paramList.add(args);
            byte[] params = BuildParams.createCodeParamsScript(paramList);

            Transaction tx = ConstantParam.ONT_SDKSERVICE.vm().makeInvokeCodeTransaction(Helper.reverse(contractAddress), null, params, null, 20000, 500);
            Object obj = ConstantParam.ONT_SDKSERVICE.getConnect().sendRawTransactionPreExec(tx.toHexString());
            String jsonUrl = BuildParams.deserializeItem(Helper.hexToBytes(((JSONObject) obj).getString("Result"))).toString();

            Map<String, Object> jsonUrlMap = new HashMap<>();
            if (jsonUrl.contains(",") && jsonUrl.contains("=")) {
                jsonUrlMap.put("image", new String(Helper.hexToBytes(jsonUrl.split(",")[0].split("=")[1])));
                jsonUrlMap.put("name", new String(Helper.hexToBytes(jsonUrl.split(",")[1].split("=")[1].replaceAll("\\}", ""))));
            }

            return JSON.toJSONString(jsonUrlMap);
        } catch (Exception e) {
            log.error("getAddressOep4Balance error...", e);
            return "";
        }
    }
}
