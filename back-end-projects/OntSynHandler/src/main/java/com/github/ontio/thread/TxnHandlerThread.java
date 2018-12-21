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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.ontio.OntSdk;
import com.github.ontio.common.Address;
import com.github.ontio.common.Helper;
import com.github.ontio.core.payload.DeployCode;
import com.github.ontio.dao.ContractsMapper;
import com.github.ontio.dao.Oep4Mapper;
import com.github.ontio.dao.Oep5Mapper;
import com.github.ontio.dao.Oep8Mapper;
import com.github.ontio.model.*;
import com.github.ontio.network.exception.ConnectorException;
import com.github.ontio.network.exception.RestfulException;
import com.github.ontio.utils.ConfigParam;
import com.github.ontio.utils.ConstantParam;
import com.github.ontio.utils.OntIdEventDesType;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.Future;


/**
 * @author zhouq
 * @version 1.0
 * @date 2018/3/13
 */
@Component
public class TxnHandlerThread {

    private static final Logger logger = LoggerFactory.getLogger(TxnHandlerThread.class);

    private static Boolean OEP4TXN = false;

    private static Boolean OEP5TXN = false;

    private static Boolean OEP8TXN = false;

    @Autowired
    private ConfigParam configParam;

    @Autowired
    private ContractsMapper contractsMapper;

    @Autowired
    private Oep4Mapper oep4Mapper;

    @Autowired
    private Oep5Mapper oep5Mapper;

    @Autowired
    private Oep8Mapper oep8Mapper;

    @Async
    public Future<String> asyncHandleTxn(SqlSession session, JSONObject txnJson, int blockHeight, int blockTime, int indexInBlock) throws Exception {

        String threadName = Thread.currentThread().getName();
        logger.info("{} run--------blockHeight:{}", threadName, blockHeight);

        handleOneTxn(session, txnJson, blockHeight, blockTime, indexInBlock);

        logger.info("{} end-------blockHeight:{}", threadName, blockHeight);
        return new AsyncResult<String>("success");
    }

    @Transactional
    public void handleOneTxn(SqlSession session, JSONObject txnJson, int blockHeight, int blockTime, int indexInBlock) throws Exception {

        int txnType = txnJson.getInteger("TxType");
        String txnHash = txnJson.getString("Hash");
        logger.info("####txnType:{}, txnHash:{}####", txnType, txnHash);

        OEP4TXN = false;
        OEP5TXN = false;
        OEP8TXN = false;
        try {
            JSONObject eventObj = getEventObjByTxnHash(txnHash);
            logger.info("eventObj:{}", eventObj.toJSONString());
            //eventstate 1:success 0:failed
            //confimflag 1:success 2:failed
            int confirmFlag = 1;
            if (eventObj.getInteger("State") == 0) {
                confirmFlag = 2;
            }
            BigDecimal gasConsumed = new BigDecimal(eventObj.getLongValue("GasConsumed")).divide(ConstantParam.ONG_DECIMAL);

            if (208 == txnType) {
                //deploy smart contract transaction
                deployContractHandle(session, txnJson, blockHeight, blockTime, indexInBlock, confirmFlag, gasConsumed);
            }

            JSONArray notifyList = eventObj.getJSONArray("Notify");
            if(notifyList.size() == 0){
                //no event transaction
                insertTxnBasicInfo(session, txnType, txnHash, blockHeight, blockTime, indexInBlock, confirmFlag, "",
                        gasConsumed, 1, 0, "");
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
                }
                else {
                    continue;
                }

                // 存在只有一个元素的notify,比如打日志这种，应该忽略
                if (stateArray.size() < 3) {
                    continue;
                }

                if (configParam.ASSET_ONG_CODEHASH.equals(contractAddress) || configParam.ASSET_ONT_CODEHASH.equals(contractAddress)) {
                    //transfer transaction
                    handleTransferTxn(session, stateArray, txnType, txnHash, blockHeight, blockTime, indexInBlock,
                            contractAddress, gasConsumed, i + 1, notifyList.size(), confirmFlag);

                } else if (configParam.ONTID_CODEHASH.equals(contractAddress)) {
                    //ontId operation transaction
                    handleOntIdTxn(session, stateArray, txnType, txnHash, blockHeight, blockTime, indexInBlock,
                            gasConsumed, i + 1, contractAddress);
                    addOneBlockOntIdTxnCount();

                } else if (configParam.CLAIMRECORD_CODEHASH.equals(contractAddress)) {
                    //claimrecord transaction
                    handleClaimRecordTxn(session, stateArray, txnType, txnHash, blockHeight, blockTime, indexInBlock,
                            gasConsumed, i + 1, contractAddress);

                } else if (configParam.AUTH_CODEHASH.equals(contractAddress)) {
                    //auth transaction
                    insertTxnBasicInfo(session, txnType, txnHash, blockHeight, blockTime, indexInBlock, confirmFlag, ConstantParam.AUTH_OPE_PREFIX,
                            gasConsumed, i + 1, 6, contractAddress);

                } else if (configParam.OEP8_PUMPKIN_CODEHASH.equals(contractAddress)) {
                    handlePumpkinTransferTxn(session, stateArray, txnType, txnHash, blockHeight, blockTime, indexInBlock,
                            gasConsumed, i + 1, confirmFlag, contractAddress);

                } else if (configParam.DRAGON_CODEHASH.equals(contractAddress) || ConstantParam.OEP5CONTRACTS.contains(contractAddress)) {
                    //云斗龙 或者 OEP5交易
                    handleDragonTransferTxn(session, stateArray, txnType, txnHash, blockHeight, blockTime, indexInBlock,
                            gasConsumed, i + 1, confirmFlag, contractAddress, ConstantParam.OEP5MAP.get(contractAddress));

                } else if (ConstantParam.OEP4CONTRACTS.contains(contractAddress)) {
                    //OEP4交易
                    JSONObject oep4Obj = (JSONObject) ConstantParam.OEP4MAP.get(contractAddress);
                    handleOep4TransferTxn(session, stateArray, txnType, txnHash, blockHeight, blockTime, indexInBlock,
                            gasConsumed, i + 1, confirmFlag, oep4Obj, contractAddress);

                } else if (ConstantParam.OEP8CONTRACTS.contains(contractAddress)) {
                    //OEP8交易
                    handleOep8TransferTxn(session, stateArray, txnType, txnHash, blockHeight, blockTime, indexInBlock,
                            gasConsumed, i + 1, confirmFlag, contractAddress, notifyList.size());

                } else {
                    //other transaction
                    insertTxnBasicInfo(session, txnType, txnHash, blockHeight, blockTime, indexInBlock, confirmFlag, "",
                            gasConsumed, i + 1, 0, contractAddress);
                }
            }
        } catch (RestfulException e) {
            logger.error("handleOneTxn RestfulException...{}", e);
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            logger.error("handleOneTxn error...", e);
            e.printStackTrace();
            throw e;
        }
    }

    private void deployContractHandle(SqlSession session, JSONObject txnJson, int blockHeight, int blockTime, int indexInBlock, int confirmFlag, BigDecimal gasConsumed) throws Exception{

        String txnHash = txnJson.getString("Hash");
        int txnType = txnJson.getInteger("TxType");
        JSONObject contractObj = getSmartContractInfo(txnHash);
        String contractAddress = getNativeContractHash(contractObj.getString("contractAddress"));

        contractObj.remove("contractAddress");
        insertTxnBasicInfo(session, txnType, txnHash, blockHeight,
                blockTime, indexInBlock, confirmFlag, contractObj.toString(),
                gasConsumed, 0, 2, contractAddress);

        // 部署合约，将合约信息保存到合约列表
        Contracts contracts = contractsMapper.selectContractByContractHash(contractAddress);
        if (contracts == null) {
            insertContratInfo(session, contractAddress, blockTime, contractObj, txnJson.getString("Payer"));
        }
    }

    private void judgeIsOepTransaction(JSONArray notifyList){
        for (int i = 0, len = notifyList.size(); i < len; i++) {
            JSONObject notifyObj = (JSONObject) notifyList.get(i);
            String contractAddress = notifyObj.getString("ContractAddress");

            if(configParam.DRAGON_CODEHASH.equals(contractAddress) || ConstantParam.OEP5CONTRACTS.contains(contractAddress)){
                OEP5TXN = true;
            }else if (ConstantParam.OEP4CONTRACTS.contains(contractAddress)){
                OEP4TXN = true;
            }else if (ConstantParam.OEP8CONTRACTS.contains(contractAddress)){
                OEP8TXN = true;
            }
        }
    }

    public synchronized void addOneBlockOntIdCount() {
        ConstantParam.ONEBLOCK_ONTID_AMOUNT++;
    }

    public synchronized void addOneBlockOntIdTxnCount() {
        ConstantParam.ONEBLOCK_ONTIDTXN_AMOUNT++;
    }

    private void handlePumpkinTransferTxn(SqlSession session, JSONArray stateArray, int txnType, String txnHash,
                                         int blockHeight, int blockTime, int indexInBlock,
                                         BigDecimal gasConsumed, int indexInTxn, int confirmFlag, String contractAddress) throws Exception {

        String action = new String(Helper.hexToBytes((String) stateArray.get(0)));
        String fromAddress = (String) stateArray.get(1);
        if (40 == fromAddress.length()) {
            fromAddress = Address.parse(fromAddress).toBase58();
        }
        String toAddress = (String) stateArray.get(2);
        if (40 == toAddress.length()) {
            toAddress = Address.parse(toAddress).toBase58();
        }

        String assetName = "";
        String tokenId = (String) stateArray.get(3);
        switch (tokenId) {
            case "01":
                assetName = "pumpkin01";
                break;
            case "02":
                assetName = "pumpkin02";
                break;
            case "03":
                assetName = "pumpkin03";
                break;
            case "04":
                assetName = "pumpkin04";
                break;
            case "05":
                assetName = "pumpkin05";
                break;
            case "06":
                assetName = "pumpkin06";
                break;
            case "07":
                assetName = "pumpkin07";
                break;
            case "08":
                assetName = "pumpkin08";
                break;
        }

        BigDecimal amount = new BigDecimal(Helper.BigIntFromNeoBytes(Helper.hexToBytes((String) stateArray.get(4))).longValue());
        logger.info("fromaddress:{}, toaddress:{}, tokenid:{}, amount:{}", fromAddress, toAddress, tokenId, amount);

        Oep8TxnDetail transactionDetailDO = generateTransaction(fromAddress, toAddress, assetName, amount, txnType, txnHash, blockHeight,
                blockTime, indexInBlock, confirmFlag, action, gasConsumed, indexInTxn, 3, contractAddress);
        session.insert("com.github.ontio.dao.TransactionDetailMapper.insertSelective", transactionDetailDO);
    }

    private void handleDragonTransferTxn(SqlSession session, JSONArray stateArray, int txnType, String txnHash,int blockHeight, int blockTime,
                                         int indexInBlock, BigDecimal gasConsumed, int indexInTxn, int confirmFlag, String contractAddress, Object oep5Obj) throws Exception{

        String action = new String(Helper.hexToBytes((String) stateArray.get(0)));
        String fromAddress = (String) stateArray.get(1);
        if (40 == fromAddress.length()) {
            fromAddress = Address.parse(fromAddress).toBase58();
        }
        String toAddress = (String) stateArray.get(2);
        if (40 == toAddress.length()) {
            toAddress = Address.parse(toAddress).toBase58();
        }

        String assetName = "";
        if(oep5Obj == null || "HyperDragons".equalsIgnoreCase(((JSONObject) oep5Obj).getString("Name"))){
            // 如果是birth方法，id位置在2；如果是transfer方法，id位置在3
            int dragonIdIndex = 3;
            if("birth".equalsIgnoreCase(action)){
                dragonIdIndex = 2;
            }
            assetName = "HyperDragons: " + Helper.BigIntFromNeoBytes(Helper.hexToBytes((String) stateArray.get(dragonIdIndex))).toString();
        }
        else {
            assetName = ((JSONObject) oep5Obj).getString("Name") + ": " + stateArray.get(3);
        }

        logger.info("fromaddress:{}, toaddress:{}, dragonId:{}", fromAddress, toAddress, assetName);
        Oep8TxnDetail transactionDetailDO = generateTransaction(fromAddress, toAddress, assetName, new BigDecimal("1"), txnType, txnHash, blockHeight,
                blockTime, indexInBlock, confirmFlag, action, gasConsumed, indexInTxn, 3, contractAddress);
        if(!"transfer".equalsIgnoreCase(action)){
            transactionDetailDO.setAmount(ConstantParam.ZERO);
        }

        session.insert("com.github.ontio.dao.TransactionDetailMapper.insertSelective", transactionDetailDO);
        session.insert("com.github.ontio.dao.Oep5TxnDetailMapper.insertSelective", transactionDetailDO);
        if("birth".equalsIgnoreCase(action) && oep5Obj != null){
            Oep5 oep5 = oep5Mapper.selectByPrimaryKey(contractAddress);
            oep5.setTotalsupply(oep5.getTotalsupply().add(new BigDecimal(1)));
            oep5Mapper.updateByPrimaryKeySelective(oep5);
        }

        OEP5TXN = true;
    }

    private void handleTransferTxn(SqlSession session, JSONArray stateList, int txnType, String txnHash,
                                  int blockHeight, int blockTime, int indexInBlock, String contractAddress,
                                  BigDecimal gasConsumed, int indexInTxn, int notifyListSize, int confirmFlag) throws Exception {

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
        BigDecimal amount = new BigDecimal(((Object) stateList.get(3)).toString());
        logger.info("####fromAddress:{},toAddress:{},amount:{}####", fromAddress, toAddress, amount.toPlainString());

        Oep8TxnDetail transactionDetailDO = generateTransaction(fromAddress, toAddress, assetName, amount, txnType, txnHash, blockHeight,
                blockTime, indexInBlock, confirmFlag, action, gasConsumed, indexInTxn, eventType, contractAddress);

        session.insert("com.github.ontio.dao.TransactionDetailMapper.insertSelective", (TransactionDetail)transactionDetailDO);

        // OEP交易的手续费入库
        if (configParam.ASSET_ONG_CODEHASH.equals(contractAddress) && (OEP4TXN || OEP5TXN || OEP8TXN)){
            if (OEP8TXN){
                transactionDetailDO.setTokenname("");
                session.insert("com.github.ontio.dao.Oep8TxnDetailMapper.insertSelective", transactionDetailDO);
            }
            else {
                session.insert("com.github.ontio.dao.Oep4TxnDetailMapper.insertSelective", (TransactionDetail)transactionDetailDO);
            }
        }
    }

    private void handleOntIdTxn(SqlSession session, JSONArray stateList, int txnType, String txnHash, int blockHeight, int blockTime, int indexInBlock,
                               BigDecimal gasConsumed, int indexInTxn, String contractAddress) throws Exception {

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
                gasConsumed, indexInTxn, 4, contractAddress);

        if (ConstantParam.REGISTER.equals(action)) {
            //ontid数量加1
            addOneBlockOntIdCount();
        }
    }

    private void handleClaimRecordTxn(SqlSession session, JSONArray stateList, int txnType, String txnHash, int blockHeight, int blockTime, int indexInBlock,
                                     BigDecimal gasConsumed, int indexInTxn, String contractAddress) throws Exception {

        String actionType = new String(Helper.hexToBytes(stateList.getString(0)));
        StringBuilder sb = new StringBuilder(140);
        sb.append(ConstantParam.CLAIMRECORD_OPE);

        if (ConstantParam.CLAIMRECORD_OPE_PREFIX.equals(actionType)) {
            if (stateList.size() >= 4) {
                String issuerOntId = new String(Helper.hexToBytes(stateList.getString(1)));
                String action = new String(Helper.hexToBytes(stateList.getString(2)));
                String claimId = new String(Helper.hexToBytes(stateList.getString(3)));
                logger.info("####ClaimRecord:action:{}, issuerOntId:{}, claimid:{}#####", action, issuerOntId, claimId);
                sb.append(issuerOntId);
                sb.append(action);
                sb.append(claimId);
            }
        }

        insertTxnBasicInfo(session, txnType, txnHash, blockHeight, blockTime, indexInBlock, 1, sb.toString(), gasConsumed, indexInTxn, 5, contractAddress);
    }

    private Oep8TxnDetail generateTransaction(String fromAddress, String toAddress, String assetName, BigDecimal account, int txnType,
                                                  String txnHash, int blockHeight, int blockTime, int indexInBlock, int confirmFlag,
                                                  String action, BigDecimal gasConsumed, int indexInTxn, int eventType, String contractAddress){

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

        return txDetail;
    }

    private void insertTxnBasicInfo(SqlSession session, int txnType, String txnHash, int blockHeight, int blockTime, int indexInBlock, int confirmFlag,
                                   String action, BigDecimal gasConsumed, int indexInTxn, int eventType, String contractAddress) throws Exception{

        Oep8TxnDetail transactionDetailDO = generateTransaction("", "", "", ConstantParam.ZERO, txnType, txnHash, blockHeight,
                blockTime, indexInBlock, confirmFlag, action, gasConsumed, indexInTxn, eventType, contractAddress);

        session.insert("com.github.ontio.dao.TransactionDetailMapper.insertSelective", transactionDetailDO);
    }

    private void insertContratInfo(SqlSession session, String contractAddress, int blockTime, JSONObject contractObj, String playAddress) throws Exception{
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
        contracts.setCreator(playAddress);
        contracts.setAddresscount(0);
        contracts.setOntcount(new BigDecimal(0));
        contracts.setOngcount(new BigDecimal(0));
        contractsMapper.insertSelective(contracts);
    }

    private String getNativeContractHash(String contractAddress){
        String contractHash = "";
        switch (contractAddress){
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

    private void handleOep4TransferTxn(SqlSession session, JSONArray stateArray, int txnType, String txnHash,
                                      int blockHeight, int blockTime, int indexInBlock, BigDecimal gasConsumed,
                                      int indexInTxn, int confirmFlag, JSONObject oep4Obj, String contractHash) throws Exception {

        String fromAddress = (String)stateArray.get(1);
        if (40 == fromAddress.length()) {
            fromAddress = Address.parse(fromAddress).toBase58();
        }
        String toAddress = (String) stateArray.get(2);
        if (40 == toAddress.length()) {
            toAddress = Address.parse(toAddress).toBase58();
        }

        BigDecimal eventAmount = new BigDecimal(Helper.BigIntFromNeoBytes(Helper.hexToBytes((String) stateArray.get(3))).longValue());
        logger.info("OEP4TransferTxn:fromaddress:{}, toaddress:{}, amount:{}", fromAddress, toAddress, eventAmount);

        String assetName = oep4Obj.getString("Symbol");
        BigDecimal amount = eventAmount.divide(new BigDecimal(Math.pow(10, oep4Obj.getInteger("Decimals"))));
        Oep8TxnDetail transactionDetailDO = generateTransaction(fromAddress, toAddress, assetName, amount, txnType, txnHash, blockHeight,
                blockTime, indexInBlock, confirmFlag, "transfer", gasConsumed, indexInTxn, 3, contractHash);

        session.insert("com.github.ontio.dao.TransactionDetailMapper.insertSelective", transactionDetailDO);
        session.insert("com.github.ontio.dao.Oep4TxnDetailMapper.insertSelective", transactionDetailDO);

        OEP4TXN = true;
    }

    private void handleOep8TransferTxn(SqlSession session, JSONArray stateArray, int txnType, String txnHash,
                                      int blockHeight, int blockTime, int indexInBlock, BigDecimal gasConsumed,
                                      int indexInTxn, int confirmFlag, String contractHash, int stateSize) throws Exception {
        String fromAddress = (String)stateArray.get(1);
        String toAddress = (String) stateArray.get(2);
        String tokenId = (String) stateArray.get(3);
        JSONObject oep8Obj = (JSONObject) ConstantParam.OEP8MAP.get(contractHash + "-" + tokenId);
        if("00".equals(fromAddress) && stateSize == 2){
            // mint方法即增加发行量方法, 区分标志：fromAddress为“00”，同时state数量为2
            Oep8 oep8 = new Oep8();
            oep8.setContract(contractHash);
            oep8.setTokenid((String) stateArray.get(3));
            ConstantParam.ONT_SDKSERVICE.neovm().oep8().setContractAddress(contractHash);
            oep8.setTotalsupply(new BigDecimal(ConstantParam.ONT_SDKSERVICE.neovm().oep8().queryTotalSupply(Helper.hexToBytes(tokenId))));

            oep8Mapper.updateTotalSupply(oep8);
        }

        if (40 == fromAddress.length()) {
            fromAddress = Address.parse(fromAddress).toBase58();
        }
        if (40 == toAddress.length()) {
            toAddress = Address.parse(toAddress).toBase58();
        }

        BigDecimal eventAmount = new BigDecimal(Helper.BigIntFromNeoBytes(Helper.hexToBytes((String) stateArray.get(4))).longValue());
        logger.info("OEP8TransferTxn:fromaddress:{}, toaddress:{}, tokenid:{}, amount:{}", fromAddress, toAddress, tokenId, eventAmount);

        Oep8TxnDetail oep8TxnDetailDAO = generateTransaction(fromAddress, toAddress, oep8Obj.getString("Symbol"), eventAmount, txnType, txnHash, blockHeight,
                blockTime, indexInBlock, confirmFlag, "transfer", gasConsumed, indexInTxn, 3, contractHash);
        oep8TxnDetailDAO.setTokenname(oep8Obj.getString("Name"));

        session.insert("com.github.ontio.dao.Oep8TxnDetailMapper.insertSelective", oep8TxnDetailDAO);
        session.insert("com.github.ontio.dao.TransactionDetailMapper.insertSelective", oep8TxnDetailDAO);

        OEP8TXN = true;
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
            logger.info("####Register OntId:{}", ontId);

        } else if (OntIdEventDesType.PUBLICKEYOPE.value().equals(action)) {

            String op = stateList.getString(1);
            int publickeyNumber = stateList.getInteger(3);
            String publicKey = stateList.getString(4);
            logger.info("####PublicKey op:{},keyNumber:{},publicKey:{}####", op, publickeyNumber, publicKey);

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
            logger.info("####Attribute op:{}####", op);

            descriptionSb.append(op);
            descriptionSb.append(ConstantParam.ONTID_SEPARATOR);
            descriptionSb.append(ontId);
            descriptionSb.append(ConstantParam.ONTID_SEPARATOR);

            if (ConstantParam.ADD.equals(op)) {
                JSONArray attrNameArray = stateList.getJSONArray(3);
                logger.info("####attrName:{}####", attrNameArray.toArray());
                for (Object obj :
                        attrNameArray) {
                    String attrName = (String) obj;
                    attrName = new String(com.github.ontio.common.Helper.hexToBytes(attrName));
                    descriptionSb.append(attrName);
                    descriptionSb.append(ConstantParam.ONTID_SEPARATOR2);
                }
            } else {
                String attrName = stateList.getString(3);
                logger.info("####attrName:{}####", attrName);
                descriptionSb.append(attrName);
                descriptionSb.append(ConstantParam.ONTID_SEPARATOR2);
            }
            str = descriptionSb.substring(0, descriptionSb.length() - 1);

        } else if (OntIdEventDesType.RECOVERYOPE.value().equals(action)) {

            String op = stateList.getString(1);
            String address = Address.parse(stateList.getString(3)).toBase58();
            logger.info("####Recovery op:{}, ontid:{}, address:{}####", op, ontId, address);

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
                logger.error("getEventObjByTxnHash error, try again...restsful:{},error:", ConstantParam.MASTERNODE_RESTFULURL, ex);
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
                logger.error("getEventObjByTxnHash thread can't work,error {} ", ex);
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
        logger.warn("####switch node restfulurl to {}####", ConstantParam.MASTERNODE_RESTFULURL);

        OntSdk wm = OntSdk.getInstance();
        wm.setRestful(ConstantParam.MASTERNODE_RESTFULURL);
        ConstantParam.ONT_SDKSERVICE = wm;
    }

    /**
     * query smartcontract information
     *
     * @param txnHash
     * @return
     * @throws Exception
     */
    private JSONObject getSmartContractInfo(String txnHash) throws Exception {

        DeployCode deployCodeObj = (DeployCode) ConstantParam.ONT_SDKSERVICE.getConnect().getTransaction(txnHash);
        String code = Helper.toHexString(deployCodeObj.code);
        String contractAddress = Address.AddressFromVmCode(code).toHexString();
        logger.info("smartcontract codehash:{}",contractAddress);
        JSONObject contractObj = new JSONObject();
        contractObj.put("contractAddress", contractAddress);
        contractObj.put("Name","");
        contractObj.put("Description","");
        //catch底层报错，根据合约hash查不到合约信息
        try {
            contractObj = (JSONObject) ConstantParam.ONT_SDKSERVICE.getConnect().getContractJson(contractAddress);
            contractObj.put("contractAddress", contractAddress);
            logger.info("smartcontract obj:{}", contractObj.toJSONString());
            contractObj.remove("Code");
        } catch (Exception e) {
            logger.error("error...", e);
        }
        return contractObj;
    }
}
