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
import com.github.ontio.model.Oep4TxnDetail;
import com.github.ontio.model.OntId;
import com.github.ontio.model.TransactionDetail;
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

    @Autowired
    private ConfigParam configParam;

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

            //deploy smart contract transaction
            if (208 == txnType) {
                JSONObject contractObj = getSmartContractInfo(txnHash);
                String contractAddress = contractObj.getString("contractAddress");
                //description.remove("codeHash");
                insertTxnBasicInfo(session, txnType, txnHash, blockHeight,
                        blockTime, indexInBlock, confirmFlag, contractObj.toString(),
                        gasConsumed, 0, 2, contractAddress);
            }

            JSONArray notifyList = eventObj.getJSONArray("Notify");
            if (notifyList.size() > 0) {

                JSONArray stateArray = null;
                for (int i = 0, len = notifyList.size(); i < len; i++) {

                    JSONObject notifyObj = (JSONObject) notifyList.get(i);
                    String contractAddress = notifyObj.getString("ContractAddress");

                    Object object = notifyObj.get("States");
                    if (object instanceof JSONArray) {
                        stateArray = (JSONArray) object;
                    } else {
                        stateArray = new JSONArray();
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

                    } else if (configParam.DRAGON_CODEHASH.equals(contractAddress)) {
                        handleDragonTransferTxn(session, stateArray, txnType, txnHash, blockHeight, blockTime, indexInBlock,
                                gasConsumed, i + 1, confirmFlag, contractAddress);

                    } else {
                        //OEP4交易
                        if (ConstantParam.OEP4CONTRACTS.contains(contractAddress) && !stateArray.isEmpty()) {
                            JSONObject oep4Obj = (JSONObject) ConstantParam.OEP4MAP.get(contractAddress);
                            handleOep4TransferTxn(session, stateArray, txnType, txnHash, blockHeight, blockTime, indexInBlock,
                                    gasConsumed, i + 1, confirmFlag, oep4Obj, contractAddress);

                        } else {
                            //other transaction
                            insertTxnBasicInfo(session, txnType, txnHash, blockHeight, blockTime, indexInBlock, confirmFlag, "",
                                    gasConsumed, i + 1, 0, contractAddress);
                        }
                    }
                }
            } else {
                //no event transaction
                insertTxnBasicInfo(session, txnType, txnHash, blockHeight, blockTime, indexInBlock, confirmFlag, "",
                        gasConsumed, 1, 0, "");
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


    public synchronized void addOneBlockOntIdCount() {
        ConstantParam.ONEBLOCK_ONTID_AMOUNT++;
    }

    public synchronized void addOneBlockOntIdTxnCount() {
        ConstantParam.ONEBLOCK_ONTIDTXN_AMOUNT++;
    }


    /**
     * insert pumpkin transfer transaction
     *
     * @param txnType
     * @param txnHash
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @param gasConsumed
     * @param indexInTxn
     * @param confirmFlag
     * @param contractAddress
     * @throws Exception
     */
    public void handlePumpkinTransferTxn(SqlSession session, JSONArray stateArray, int txnType, String txnHash,
                                         int blockHeight, int blockTime, int indexInBlock,
                                         BigDecimal gasConsumed, int indexInTxn, int confirmFlag, String contractAddress) throws Exception {

        String action = new String(Helper.hexToBytes((String) stateArray.get(0)));

        String fromAddress = (String) stateArray.get(1);
        if (!"00".equals(fromAddress)) {
            fromAddress = Address.parse(fromAddress).toBase58();
        }
        String toAddress = (String) stateArray.get(2);
        if (!"00".equals(toAddress)) {
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

        TransactionDetail transactionDetailDO = new TransactionDetail();
        transactionDetailDO.setFromaddress(fromAddress);
        transactionDetailDO.setToaddress(toAddress);
        transactionDetailDO.setAssetname(assetName);
        transactionDetailDO.setDescription(action);
        transactionDetailDO.setFee(gasConsumed);
        transactionDetailDO.setHeight(blockHeight);
        transactionDetailDO.setBlockindex(indexInBlock);
        transactionDetailDO.setTxnhash(txnHash);
        transactionDetailDO.setTxntype(txnType);
        transactionDetailDO.setTxntime(blockTime);
        transactionDetailDO.setAmount(amount);
        transactionDetailDO.setTxnindex(indexInTxn);
        transactionDetailDO.setConfirmflag(confirmFlag);
        transactionDetailDO.setEventtype(3);
        transactionDetailDO.setContracthash(contractAddress);
        session.insert("com.github.ontio.dao.TransactionDetailMapper.insertSelective", transactionDetailDO);

    }
    /**
     * insert pumpkin transfer transaction
     *
     * @param txnType
     * @param txnHash
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @param contractAddress
     * @throws Exception
     */
    public void handleDragonTransferTxn(SqlSession session, JSONArray stateArray, int txnType, String txnHash,
                                        int blockHeight, int blockTime, int indexInBlock,
                                        BigDecimal gasConsumed, int indexInTxn, int confirmFlag, String contractAddress) throws Exception {

        String action = new String(Helper.hexToBytes((String) stateArray.get(0)));
        if("Transfer".equals(action)) {
            String fromAddress = (String) stateArray.get(1);
            if (!"00".equals(fromAddress)) {
                fromAddress = Address.parse(fromAddress).toBase58();
            }
            String toAddress = (String) stateArray.get(2);
            if (!"00".equals(toAddress)) {
                toAddress = Address.parse(toAddress).toBase58();
            }

            String dragonId = new BigDecimal(Helper.BigIntFromNeoBytes(Helper.hexToBytes((String) stateArray.get(3))).longValue()).toPlainString();
            logger.info("fromaddress:{}, toaddress:{}, dragonId:{}", fromAddress, toAddress, dragonId);

            TransactionDetail transactionDetailDO = new TransactionDetail();
            transactionDetailDO.setFromaddress(fromAddress);
            transactionDetailDO.setToaddress(toAddress);
            transactionDetailDO.setAssetname("dragon"+dragonId);
            transactionDetailDO.setDescription("transfer");
            transactionDetailDO.setFee(gasConsumed);
            transactionDetailDO.setHeight(blockHeight);
            transactionDetailDO.setBlockindex(indexInBlock);
            transactionDetailDO.setTxnhash(txnHash);
            transactionDetailDO.setTxntype(txnType);
            transactionDetailDO.setTxntime(blockTime);
            transactionDetailDO.setAmount(new BigDecimal("1"));
            transactionDetailDO.setTxnindex(indexInTxn);
            transactionDetailDO.setConfirmflag(confirmFlag);
            transactionDetailDO.setEventtype(3);
            transactionDetailDO.setContracthash(contractAddress);
            session.insert("com.github.ontio.dao.TransactionDetailMapper.insertSelective", transactionDetailDO);
        }

    }


    /**
     * insert transfer transaction
     *
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
     * @throws Exception
     */
    public void handleTransferTxn(SqlSession session, JSONArray stateList, int txnType, String txnHash,
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

        TransactionDetail transactionDetailDO = new TransactionDetail();
        transactionDetailDO.setFromaddress(fromAddress);
        transactionDetailDO.setToaddress(toAddress);
        transactionDetailDO.setAssetname(assetName);
        transactionDetailDO.setDescription(action);
        transactionDetailDO.setFee(gasConsumed);
        transactionDetailDO.setHeight(blockHeight);
        transactionDetailDO.setBlockindex(indexInBlock);
        transactionDetailDO.setTxnhash(txnHash);
        transactionDetailDO.setTxntype(txnType);
        transactionDetailDO.setTxntime(blockTime);
        transactionDetailDO.setAmount(amount);
        transactionDetailDO.setTxnindex(indexInTxn);
        transactionDetailDO.setConfirmflag(confirmFlag);
        transactionDetailDO.setEventtype(eventType);
        transactionDetailDO.setContracthash(contractAddress);
        session.insert("com.github.ontio.dao.TransactionDetailMapper.insertSelective", transactionDetailDO);

        //OEP4交易的手续费入库
        if (configParam.ASSET_ONG_CODEHASH.equals(contractAddress) && OEP4TXN) {
            Oep4TxnDetail oep4TxnDetailDAO = new Oep4TxnDetail();
            oep4TxnDetailDAO.setTxnhash(txnHash);
            oep4TxnDetailDAO.setTxntype(txnType);
            oep4TxnDetailDAO.setFromaddress(fromAddress);
            oep4TxnDetailDAO.setToaddress(toAddress);
            oep4TxnDetailDAO.setTxntime(blockTime);
            oep4TxnDetailDAO.setHeight(blockHeight);
            oep4TxnDetailDAO.setAmount(amount);
            oep4TxnDetailDAO.setBlockindex(indexInBlock);
            oep4TxnDetailDAO.setAssetname("ong");
            oep4TxnDetailDAO.setConfirmflag(confirmFlag);
            oep4TxnDetailDAO.setEventtype(1);
            oep4TxnDetailDAO.setDescription("gasconsume");
            oep4TxnDetailDAO.setTxnindex(indexInTxn);
            oep4TxnDetailDAO.setFee(gasConsumed);
            oep4TxnDetailDAO.setContracthash(contractAddress);
            session.insert("com.github.ontio.dao.Oep4TxnDetailMapper.insertSelective", oep4TxnDetailDAO);
        }

    }

    /**
     * insert ontId operation transaction ans basic information of the transaction
     *
     * @param txnType
     * @param txnHash
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @throws Exception
     */
    public void handleOntIdTxn(SqlSession session, JSONArray stateList, int txnType, String txnHash, int blockHeight, int blockTime, int indexInBlock,
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


    /**
     * insert claimrecord operation transaction insert basic information of the transaction
     *
     * @param txnType
     * @param txnHash
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @throws Exception
     */
    public void handleClaimRecordTxn(SqlSession session, JSONArray stateList, int txnType, String txnHash, int blockHeight, int blockTime, int indexInBlock,
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

        insertTxnBasicInfo(session, txnType, txnHash, blockHeight, blockTime, indexInBlock, 1, sb.toString(),
                gasConsumed, indexInTxn, 5, contractAddress);
    }

    /**
     * insert the basic information of one transaction
     *
     * @param txnType
     * @param txnHash
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @param confirmFlag  1:success 2:fail
     * @param action
     * @param gasConsumed
     * @param indexInTxn
     * @param eventType
     * @param contractAddress
     * @throws Exception
     */
    public void insertTxnBasicInfo(SqlSession session, int txnType, String txnHash, int blockHeight,
                                   int blockTime, int indexInBlock, int confirmFlag, String action,
                                   BigDecimal gasConsumed, int indexInTxn, int eventType, String contractAddress) throws Exception {

        TransactionDetail transactionDetailDO = new TransactionDetail();
        transactionDetailDO.setFromaddress("");
        transactionDetailDO.setToaddress("");
        transactionDetailDO.setAssetname("");
        transactionDetailDO.setDescription(action);
        transactionDetailDO.setFee(gasConsumed);
        transactionDetailDO.setHeight(blockHeight);
        transactionDetailDO.setBlockindex(indexInBlock);
        transactionDetailDO.setTxnhash(txnHash);
        transactionDetailDO.setTxntype(txnType);
        transactionDetailDO.setTxntime(blockTime);
        transactionDetailDO.setAmount(ConstantParam.ZERO);
        transactionDetailDO.setTxnindex(indexInTxn);
        transactionDetailDO.setConfirmflag(confirmFlag);
        transactionDetailDO.setEventtype(eventType);
        transactionDetailDO.setContracthash(contractAddress);
        session.insert("com.github.ontio.dao.TransactionDetailMapper.insertSelective", transactionDetailDO);

        // transactionDetailMapper.insertSelective(transactionDetailDO);
    }


    /**
     * handle oep4 transfer transaction
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
    public void handleOep4TransferTxn(SqlSession session, JSONArray stateArray, int txnType, String txnHash,
                                      int blockHeight, int blockTime, int indexInBlock, BigDecimal gasConsumed,
                                      int indexInTxn, int confirmFlag, JSONObject oep4Obj, String contractHash) throws Exception {

        String fromAddress = (String)stateArray.get(1);
        if(!"00".equals(fromAddress)) {
            fromAddress = Address.parse((String) stateArray.get(1)).toBase58();
        }
        String toAddress = (String) stateArray.get(2);
        if(!"00".equals(toAddress)) {
            toAddress = Address.parse((String) stateArray.get(2)).toBase58();
        }
        BigDecimal eventAmount = new BigDecimal(Helper.BigIntFromNeoBytes(Helper.hexToBytes((String) stateArray.get(3))).longValue());
        logger.info("OEP4TransferTxn:fromaddress:{}, toaddress:{}, amount:{}", fromAddress, toAddress, eventAmount);

        String assetName = oep4Obj.getString("Symbol");
        int decimals = oep4Obj.getInteger("Decimals");
        BigDecimal amount = eventAmount.divide(new BigDecimal(Math.pow(10, decimals)));

        Oep4TxnDetail oep4TxnDetailDAO = new Oep4TxnDetail();
        oep4TxnDetailDAO.setHeight(blockHeight);
        oep4TxnDetailDAO.setTxntime(blockTime);
        oep4TxnDetailDAO.setTxntype(txnType);
        oep4TxnDetailDAO.setTxnhash(txnHash);
        oep4TxnDetailDAO.setFromaddress(fromAddress);
        oep4TxnDetailDAO.setToaddress(toAddress);
        oep4TxnDetailDAO.setAssetname(assetName);
        oep4TxnDetailDAO.setAmount(amount);
        oep4TxnDetailDAO.setDescription("transfer");
        oep4TxnDetailDAO.setFee(gasConsumed);
        oep4TxnDetailDAO.setBlockindex(indexInBlock);
        oep4TxnDetailDAO.setTxnindex(indexInTxn);
        oep4TxnDetailDAO.setConfirmflag(confirmFlag);
        oep4TxnDetailDAO.setEventtype(3);
        oep4TxnDetailDAO.setContracthash(contractHash);
        session.insert("com.github.ontio.dao.Oep4TxnDetailMapper.insertSelective", oep4TxnDetailDAO);

        TransactionDetail transactionDetailDO = new TransactionDetail();
        transactionDetailDO.setFromaddress(fromAddress);
        transactionDetailDO.setToaddress(toAddress);
        transactionDetailDO.setAssetname(assetName);
        transactionDetailDO.setDescription("transfer");
        transactionDetailDO.setFee(gasConsumed);
        transactionDetailDO.setHeight(blockHeight);
        transactionDetailDO.setBlockindex(indexInBlock);
        transactionDetailDO.setTxnhash(txnHash);
        transactionDetailDO.setTxntype(txnType);
        transactionDetailDO.setTxntime(blockTime);
        transactionDetailDO.setAmount(amount);
        transactionDetailDO.setTxnindex(indexInTxn);
        transactionDetailDO.setConfirmflag(confirmFlag);
        transactionDetailDO.setEventtype(3);
        transactionDetailDO.setContracthash(contractHash);
        session.insert("com.github.ontio.dao.TransactionDetailMapper.insertSelective", transactionDetailDO);

        OEP4TXN = true;
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
        // logger.info("smartcontract codehash:{}",codeHash);
        JSONObject contractObj = (JSONObject) ConstantParam.ONT_SDKSERVICE.getConnect().getContractJson(contractAddress);
        logger.info("smartcontract obj:{}", contractObj.toJSONString());
        contractObj.remove("Code");
        contractObj.put("contractAddress", contractAddress);

        return contractObj;
    }
}
