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
import com.github.ontio.core.transaction.Transaction;
import com.github.ontio.dao.OntIdMapper;
import com.github.ontio.dao.TransactionDetailMapper;
import com.github.ontio.model.OntId;
import com.github.ontio.model.TransactionDetail;
import com.github.ontio.utils.ConfigParam;
import com.github.ontio.utils.ConstantParam;
import com.github.ontio.utils.OntIdEventDesType;
import com.github.ontio.utils.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.Future;

import static com.github.ontio.utils.ConstantParam.ONTID_OPE_PREFIX;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/3/13
 */
@Component
public class TxnHandlerThread {

    private static final Logger logger = LoggerFactory.getLogger(TxnHandlerThread.class);

    private static final String SEPARATOR = "||";

    private static final String SEPARATOR2 = "&";

    @Autowired
    private TransactionDetailMapper transactionDetailMapper;
    @Autowired
    private OntIdMapper ontIdMapper;
    @Autowired
    private ConfigParam configParam;

    @Async
    public Future<String> asyncHandleTxn(OntSdk sdkService, Transaction txn, int blockHeight, int blockTime, int indexInBlock) throws Exception {

        logger.info("{} run--------blockHeight:{}", Thread.currentThread().getName(), blockHeight);

        handleOneTxn(sdkService, txn, blockHeight, blockTime, indexInBlock);

        logger.info("{} end-------blockHeight:{}", Thread.currentThread().getName(), blockHeight);
        return new AsyncResult<String>("success");
    }

    @Transactional(rollbackFor = Exception.class)
    public void handleOneTxn(OntSdk sdkService, Transaction txn, int blockHeight, int blockTime, int indexInBlock) throws Exception {

        int txnType = (txn.txType.value() & 0xFF);
        String txnHash = txn.hash().toString();
        logger.info("####txnType:{}, txnHash:{}####", txnType, txnHash);

        try {
            if (txnType == TransactionType.DEPLOYCODE.type()) {
                //deploy smart contract transaction
                insertTxnBasicInfo(txn, blockHeight, blockTime, indexInBlock, 1, "", 0);

            } else if (txnType == TransactionType.INVOKECODE.type()) {

                JSONObject eventObj = (JSONObject) sdkService.getConnectMgr().getSmartCodeEvent(txnHash);
                logger.info("eventObj:{}", eventObj.toJSONString());
                int state = eventObj.getInteger("State");

                //success transaction
                if (state == 1) {

                    long gasConsumed = eventObj.getLongValue("GasConsumed");
                    logger.info("gasConsumed:{}", gasConsumed);
                    JSONArray notifyList = eventObj.getJSONArray("Notify");

                    if(notifyList.size() > 0) {
                        String contractAddress = ((JSONObject) notifyList.get(0)).getString("ContractAddress");
                        logger.info("####ContractAddress:{}####", contractAddress);

                        JSONArray stateList = ((JSONObject) notifyList.get(0)).getJSONArray("States");

                        //transfer transaction
                        if (configParam.ASSET_ONG_CODEHASH.equals(contractAddress) || configParam.ASSET_ONT_CODEHASH.equals(contractAddress)) {
                            handleTransferTxn(stateList, txn, blockHeight, blockTime, indexInBlock, contractAddress, gasConsumed);
                        } else if (configParam.ONTID_CODEHASH.equals(contractAddress)) {
                            //ontId operation transaction
                            handleOntIdTxn(stateList, txn, blockHeight, blockTime, indexInBlock, gasConsumed);
                        } else if (configParam.RECORD_CODEHASH.equals(contractAddress)) {
                            //record transaction
                            insertTxnBasicInfo(txn, blockHeight, blockTime, indexInBlock, 1, ConstantParam.RECORD_OPE, gasConsumed);
                        } else {
                            insertTxnBasicInfo(txn, blockHeight, blockTime, indexInBlock, 1, "", gasConsumed);
                        }
                    }else {
                        insertTxnBasicInfo(txn, blockHeight, blockTime, indexInBlock, 1, "", gasConsumed);
                    }


                } else if (state == 0) {
                    //fail transaction
                    insertTxnBasicInfo(txn, blockHeight, blockTime, indexInBlock, 2, "", 0);
                }
            }

        } catch (Exception e) {
            logger.error("handleOneTxn error...", e);
            e.printStackTrace();
            throw e;
        }

        addcount();
    }

    public synchronized void addcount() {
        ConstantParam.INIT_AMOUNT++;
    }

    /**
     * format byte[]
     *
     * @param byteList
     * @return
     */
/*    private byte[] format(List byteList) {
        byte[] bys = new byte[byteList.toArray().length];
        for (int i = 0; i < bys.length; i++) {
            bys[i] = (byte) ((int) byteList.get(i) & 0xff);
        }
        return bys;
    }*/

    /**
     * insert transfer transaction
     *
     * @param txn
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @param codeHash
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void handleTransferTxn(JSONArray stateList, Transaction txn, int blockHeight, int blockTime, int indexInBlock, String codeHash, long gasConsumed) throws Exception {

        String assetName = "";
        if (configParam.ASSET_ONT_CODEHASH.equals(codeHash)) {
            assetName = "ont";
        } else if (configParam.ASSET_ONG_CODEHASH.equals(codeHash)) {
            assetName = "ong";
        }

        String action = (String) stateList.get(0);
        logger.info("####action:{}####", action);

        String fromAddress = (String) stateList.get(1);
        logger.info("####fromAddress:{}####", fromAddress);

        String toAddress = (String) stateList.get(2);
        logger.info("####toAddress:{}####", toAddress);

        String amountString = ((Object) stateList.get(3)).toString();
        BigDecimal amount = new BigDecimal(amountString);
        logger.info("####amount:{}####", amountString);

        TransactionDetail transactionDetailDO = new TransactionDetail();
        transactionDetailDO.setFromaddress(fromAddress);
        transactionDetailDO.setToaddress(toAddress);
        transactionDetailDO.setAssetname(assetName);
        transactionDetailDO.setDescription(action);
        transactionDetailDO.setFee(gasConsumed);
        transactionDetailDO.setHeight(blockHeight);
        transactionDetailDO.setBlockindex(indexInBlock);
        transactionDetailDO.setTxnhash(txn.hash().toString());
        transactionDetailDO.setTxntype(txn.txType.value() & 0xFF);
        transactionDetailDO.setTxntime(blockTime);
        transactionDetailDO.setAmount(amount);
        transactionDetailDO.setTxnindex(1);
        transactionDetailDO.setConfirmflag(1);
        transactionDetailMapper.insertSelective(transactionDetailDO);

    }

    /**
     * insert ontId operation transaction ans basic information of the transaction
     *
     * @param txn
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void handleOntIdTxn(JSONArray stateList, Transaction txn, int blockHeight, int blockTime, int indexInBlock, long gasConsumed) throws Exception {

        String action = stateList.getString(0);
        logger.info("####action:{}####", action);
        String ontId = "";
        if (OntIdEventDesType.REGISTERONTID.value().equals(action)) {
            ontId = stateList.getString(1);
        } else {
            ontId = stateList.getString(2);
        }
        logger.info("####ontId:{}####", ontId);

        String descriptionStr = formatOntIdOperation(action, stateList);

        OntId ontIdDO = new OntId();
        ontIdDO.setHeight(blockHeight);
        ontIdDO.setTxnhash(txn.hash().toString());
        ontIdDO.setTxntype(txn.txType.value() & 0xFF);
        ontIdDO.setDescription(descriptionStr);
        ontIdDO.setTxntime(blockTime);
        ontIdDO.setOntid(ontId);
        ontIdDO.setFee(gasConsumed);
        ontIdMapper.insertSelective(ontIdDO);

        insertTxnBasicInfo(txn, blockHeight, blockTime, indexInBlock, 1, ONTID_OPE_PREFIX + action, gasConsumed);
    }

    /**
     * insert the basic information of one transaction
     *
     * @param txn
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @param confirmFlag  1:success 2:fail
     * @param action
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertTxnBasicInfo(Transaction txn, int blockHeight, int blockTime, int indexInBlock, int confirmFlag, String action, long gasConsumed) throws Exception {

        logger.info("{} run....", com.github.ontio.utils.Helper.currentMethod());

        TransactionDetail transactionDetailDO = new TransactionDetail();
        transactionDetailDO.setFromaddress("");
        transactionDetailDO.setToaddress("");
        transactionDetailDO.setAssetname("");
        transactionDetailDO.setDescription(action);
        transactionDetailDO.setFee(gasConsumed);
        transactionDetailDO.setHeight(blockHeight);
        transactionDetailDO.setBlockindex(indexInBlock);
        transactionDetailDO.setTxnhash(txn.hash().toString());
        transactionDetailDO.setTxntype(txn.txType.value() & 0xFF);
        transactionDetailDO.setTxntime(blockTime);
        transactionDetailDO.setAmount(new BigDecimal("0"));
        transactionDetailDO.setTxnindex(1);
        transactionDetailDO.setConfirmflag(confirmFlag);

        transactionDetailMapper.insertSelective(transactionDetailDO);
    }

    /**
     * format ontId operation description
     *
     * @param action
     * @param stateList
     * @return
     */
    private String formatOntIdOperation(String action, JSONArray stateList) throws Exception {

        String str = "";
        StringBuilder descriptionSb = new StringBuilder();
        descriptionSb.append(action);
        descriptionSb.append(SEPARATOR);

        if (OntIdEventDesType.REGISTERONTID.value().equals(action)) {

            String ontId = stateList.getString(1);
            logger.info("####ontId:{}####", ontId);
            descriptionSb.append(ontId);
            str = descriptionSb.toString();

        } else if (OntIdEventDesType.PUBLICKEYOPE.value().equals(action)) {

            String op = stateList.getString(1);
            logger.info("####op:{}####", op);
            String ontId = stateList.getString(2);
            logger.info("####ontId:{}####", ontId);
            int publickeyNumber = stateList.getInteger(3);
            logger.info("publickeyNumber:{}", publickeyNumber);
            String publicKey = stateList.getString(4);
            logger.info("####publicKey:{}####", publicKey);

            descriptionSb.append(op);
            descriptionSb.append(SEPARATOR);
            descriptionSb.append(ontId);
            descriptionSb.append(SEPARATOR);
            descriptionSb.append(publicKey);
            descriptionSb.append(SEPARATOR);
            descriptionSb.append(publickeyNumber);
            str = descriptionSb.toString();

        } else if (OntIdEventDesType.ATTRIBUTEOPE.value().equals(action)) {

            String op = stateList.getString(1);
            logger.info("####op:{}####", op);
            String ontId = stateList.getString(2);
            logger.info("####ontId:{}####", ontId);

            descriptionSb.append(op);
            descriptionSb.append(SEPARATOR);
            descriptionSb.append(ontId);
            descriptionSb.append(SEPARATOR);

            if (ConstantParam.ADD.equals(op)) {
                JSONArray attrNameArray = stateList.getJSONArray(3);
                logger.info("####attrName:{}####", attrNameArray.toArray());
                for (Object obj :
                        attrNameArray) {
                    String attrName = (String) obj;
                    attrName = new String(com.github.ontio.common.Helper.hexToBytes(attrName));
                    descriptionSb.append(attrName);
                    descriptionSb.append(SEPARATOR2);
                }
            } else {
                String attrName = stateList.getString(3);
                logger.info("####attrName:{}####", attrName);
                descriptionSb.append(attrName);
                descriptionSb.append(SEPARATOR2);
            }
            str = descriptionSb.substring(0, descriptionSb.length() - 1);

        } else if (OntIdEventDesType.RECOVERYOPE.value().equals(action)) {

            String op = stateList.getString(1);
            logger.info("####op:{}####", op);
            String ontId = stateList.getString(2);
            logger.info("####ontId:{}####", ontId);
            String address = Address.parse(stateList.getString(3)).toBase58();
            logger.info("####address:{}####", address);

            descriptionSb.append(op);
            descriptionSb.append(SEPARATOR);
            descriptionSb.append(ontId);
            descriptionSb.append(SEPARATOR);
            descriptionSb.append(address);

            str = descriptionSb.toString();
        }
        return str;
    }


}
