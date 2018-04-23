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



package com.github.ontio.task;

import com.alibaba.fastjson.JSONObject;
import com.github.ontio.OntSdk;
import com.github.ontio.common.Address;
import com.github.ontio.common.Helper;
import com.github.ontio.core.transaction.Transaction;
import com.github.ontio.utils.OntIdEventDesType;
import com.github.ontio.dao.OntIdMapper;
import com.github.ontio.dao.TransactionDetailMapper;
import com.github.ontio.model.OntId;
import com.github.ontio.model.TransactionDetail;
import com.github.ontio.utils.ConfigParam;
import com.github.ontio.utils.ConstantParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
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

    @Autowired
    private TransactionDetailMapper transactionDetailMapper;
    @Autowired
    private OntIdMapper ontIdMapper;
    @Autowired
    private ConfigParam configParam;

    @Async
    public Future<String> asyncHandleTxn(OntSdk sdk, Transaction txn, int blockHeight, int blockTime, int indexInBlock) throws Exception {

        logger.info("{} run--------blockHeight:{}", Thread.currentThread().getName(), blockHeight);

        handleOneTxn(sdk, txn, blockHeight, blockTime, indexInBlock);

        logger.info("{} end-------blockHeight:{}", Thread.currentThread().getName(), blockHeight);
        return new AsyncResult<String>("success");
    }

    @Transactional(rollbackFor = Exception.class)
    public void handleOneTxn(OntSdk sdk, Transaction txn, int blockHeight, int blockTime, int indexInBlock) throws Exception {

        int txnType = (txn.txType.value() & 0xFF);
        String txnHash = txn.hash().toString();
        logger.info("####txnType:{}, txnHash:{}####", txnType, txnHash);

        //get rid of BookKeeperTransaction(txnType=0)
        if (txnType != 0) {
            try {
                int indexInTxn = 1;
                List eventObjList = (List) sdk.getConnectMgr().getSmartCodeEvent(txnHash);

                if (eventObjList != null) {

                    for (Object temp :
                            eventObjList) {
                        JSONObject eventObj = (JSONObject) temp;

                        String codeHash = Helper.toHexString(format((List) eventObj.get("CodeHash")));
                        logger.info("####codeHash:{}####", codeHash);
                        //transfer transaction
                        if (configParam.ASSET_ONG_CODEHASH.equals(codeHash) || configParam.ASSET_ONT_CODEHASH.equals(codeHash)) {
                            handleTransferTxn(eventObj, txn, blockHeight, blockTime, indexInBlock, indexInTxn, codeHash);
                        } else if (configParam.ONTID_CODEHASH.equals(codeHash) || configParam.ONTID_CODEHASH2.equals(codeHash)) {
                            //ontId operation transaction
                            handleOntIdTxn(eventObj, txn, blockHeight, blockTime, indexInBlock, indexInTxn);
                        } else {
                            insertTxnBasicInfo(txn, blockHeight, blockTime, indexInTxn, indexInBlock, 1, "");
                        }

                        indexInTxn++;
                    }
                } else if (txnType == 208) {
                    insertTxnBasicInfo(txn, blockHeight, blockTime, indexInTxn, indexInBlock, 1, "");
                } else {
                    insertTxnBasicInfo(txn, blockHeight, blockTime, indexInTxn, indexInBlock, 2, "");
                }
            } catch (Exception e) {
                logger.error("handleOneTxn error...", e);
                e.printStackTrace();
                throw e;
            }

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
    private byte[] format(List byteList) {
        byte[] bys = new byte[byteList.toArray().length];
        for (int i = 0; i < bys.length; i++) {
            bys[i] = (byte) ((int) byteList.get(i) & 0xff);
        }
        return bys;
    }

    /**
     * insert transfer transaction
     *
     * @param eventObj
     * @param txn
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @param indexInTxn
     * @param codeHash
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void handleTransferTxn(JSONObject eventObj, Transaction txn, int blockHeight, int blockTime, int indexInBlock, int indexInTxn, String codeHash) throws Exception {

        String assetName = "";
        if (configParam.ASSET_ONT_CODEHASH.equals(codeHash)) {
            assetName = "ont";
        } else if (configParam.ASSET_ONG_CODEHASH.equals(codeHash)) {
            assetName = "ong";
        }

        List statesList = (List) eventObj.getJSONArray("States");
        String fromAddress = Address.parse(Helper.toHexString(format((List) statesList.get(1)))).toBase58();
        logger.info("####fromAddress:{}####", fromAddress);

        String toAddress = Address.parse(Helper.toHexString(format((List) statesList.get(2)))).toBase58();
        logger.info("####toAddress:{}####", toAddress);

        Long amountLong = ((Integer) statesList.get(3)).longValue();
        BigDecimal amount = new BigDecimal(amountLong);
        logger.info("####amount:{}####", String.valueOf(amountLong));

        TransactionDetail transactionDetailDO = new TransactionDetail();
        transactionDetailDO.setFromaddress(fromAddress);
        transactionDetailDO.setToaddress(toAddress);
        transactionDetailDO.setAssetname(assetName);
        transactionDetailDO.setDescription((String) statesList.get(0));
        transactionDetailDO.setFee(0L);
        transactionDetailDO.setHeight(blockHeight);
        transactionDetailDO.setBlockindex(indexInBlock);
        transactionDetailDO.setTxnhash(txn.hash().toString());
        transactionDetailDO.setTxntype(txn.txType.value() & 0xFF);
        transactionDetailDO.setTxntime(blockTime);
        transactionDetailDO.setAmount(amount);
        transactionDetailDO.setTxnindex(indexInTxn);
        transactionDetailDO.setConfirmflag(1);
        transactionDetailMapper.insertSelective(transactionDetailDO);

    }

    /**
     * insert ontId operation transaction ans basic information of the transaction
     *
     * @param eventObj
     * @param txn
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @param indexInTxn
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void handleOntIdTxn(JSONObject eventObj, Transaction txn, int blockHeight, int blockTime, int indexInBlock, int indexInTxn) throws Exception {

        StringBuilder descriptionSb = new StringBuilder();

        List statesList = (List) eventObj.getJSONArray("States");
        List list = (List) statesList.get(0);
        String action = new String(Helper.hexToBytes((String) list.get(0)));
        logger.info("####action:{}####", action);
        String ontId = new String(Helper.hexToBytes((String) list.get(2)));
        logger.info("####ontId:{}####", ontId);

        descriptionSb = formatOntIdOperation(action, descriptionSb, list);

        OntId ontIdDO = new OntId();
        ontIdDO.setHeight(blockHeight);
        ontIdDO.setTxnhash(txn.hash().toString());
        ontIdDO.setTxntype(txn.txType.value() & 0xFF);
        ontIdDO.setDescription(descriptionSb.toString());
        ontIdDO.setTxntime(blockTime);
        ontIdDO.setOntid(ontId);
        ontIdDO.setFee(0L);
        ontIdMapper.insertSelective(ontIdDO);

        insertTxnBasicInfo(txn, blockHeight, blockTime, indexInTxn, indexInBlock, 1, ONTID_OPE_PREFIX + action);
    }

    /**
     * insert the basic information of one transaction
     *
     * @param txn
     * @param blockHeight
     * @param blockTime
     * @param txnIndex
     * @param indexInBlock
     * @param confirmFlag  1:success 2:failed
     * @param action
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertTxnBasicInfo(Transaction txn, int blockHeight, int blockTime, int txnIndex, int indexInBlock, int confirmFlag, String action) throws Exception {

        logger.info("insertTxnBasicInfo....");

        TransactionDetail transactionDetailDO = new TransactionDetail();
        transactionDetailDO.setFromaddress("");
        transactionDetailDO.setToaddress("");
        transactionDetailDO.setAssetname("");
        transactionDetailDO.setDescription(action);
        transactionDetailDO.setFee(0L);
        transactionDetailDO.setHeight(blockHeight);
        transactionDetailDO.setBlockindex(indexInBlock);
        transactionDetailDO.setTxnhash(txn.hash().toString());
        transactionDetailDO.setTxntype(txn.txType.value() & 0xFF);
        transactionDetailDO.setTxntime(blockTime);
        transactionDetailDO.setAmount(new BigDecimal("0"));
        transactionDetailDO.setTxnindex(txnIndex);
        transactionDetailDO.setConfirmflag(confirmFlag);
        transactionDetailMapper.insertSelective(transactionDetailDO);
    }

    /**
     * format ontId operation description
     *
     * @param action
     * @param descriptionSb
     * @param list
     * @return
     */
    private StringBuilder formatOntIdOperation(String action, StringBuilder descriptionSb, List list) throws Exception {

        descriptionSb.append(action);
        descriptionSb.append(SEPARATOR);

        if (OntIdEventDesType.REGISTERONTID.value().equals(action)) {

            String op = new String(Helper.hexToBytes((String) list.get(1)));
            logger.info("####op:{}####", op);
            String ontId = new String(Helper.hexToBytes((String) list.get(2)));
            logger.info("####ontId:{}####", ontId);

            descriptionSb.append(op);
            descriptionSb.append(SEPARATOR);
            descriptionSb.append(ontId);

        } else if (OntIdEventDesType.PUBLICKEYOPE.value().equals(action)) {

            String op = new String(Helper.hexToBytes((String) list.get(1)));
            logger.info("####op:{}####", op);
            String ontId = new String(Helper.hexToBytes((String) list.get(2)));
            logger.info("####ontId:{}####", ontId);
            String publicKey = Helper.toHexString(((String) list.get(3)).getBytes());
            logger.info("####publicKey:{}####", publicKey);

            descriptionSb.append(op);
            descriptionSb.append(SEPARATOR);
            descriptionSb.append(ontId);
            descriptionSb.append(SEPARATOR);
            descriptionSb.append(publicKey);

        } else if (OntIdEventDesType.ATTRIBUTEOPE.value().equals(action)) {

            String op = new String(Helper.hexToBytes((String) list.get(1)));
            logger.info("####op:{}####", op);
            String ontId = new String(Helper.hexToBytes((String) list.get(2)));
            logger.info("####ontId:{}####", ontId);
            String attrName = new String(Helper.hexToBytes((String) list.get(3)));
            logger.info("####attrName:{}####", attrName);

            descriptionSb.append(op);
            descriptionSb.append(SEPARATOR);
            descriptionSb.append(ontId);
            descriptionSb.append(SEPARATOR);
            descriptionSb.append(attrName);
        }

        return descriptionSb;
    }


}
