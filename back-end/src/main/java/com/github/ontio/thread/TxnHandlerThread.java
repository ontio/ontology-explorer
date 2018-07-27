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
import com.github.ontio.dao.OntIdMapper;
import com.github.ontio.dao.TransactionDetailMapper;
import com.github.ontio.model.OntId;
import com.github.ontio.model.TransactionDetail;
import com.github.ontio.network.exception.RestfulException;
import com.github.ontio.utils.ConfigParam;
import com.github.ontio.utils.ConstantParam;
import com.github.ontio.utils.OntIdEventDesType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.Future;

import static com.github.ontio.utils.ConstantParam.CLAIMRECORD_OPE;
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

    private static final BigDecimal ZERO = new BigDecimal("0");

    private static final BigDecimal ONG_DECIMAL = new BigDecimal("1000000000");

    @Autowired
    private TransactionDetailMapper transactionDetailMapper;
    @Autowired
    private OntIdMapper ontIdMapper;
    @Autowired
    private ConfigParam configParam;

    @Async
    public Future<String> asyncHandleTxn(JSONObject txnJson, int blockHeight, int blockTime, int indexInBlock) throws Exception {

        logger.info("{} run--------blockHeight:{}", Thread.currentThread().getName(), blockHeight);

        handleOneTxn(txnJson, blockHeight, blockTime, indexInBlock);

        logger.info("{} end-------blockHeight:{}", Thread.currentThread().getName(), blockHeight);
        return new AsyncResult<String>("success");
    }

    @Transactional(rollbackFor = Exception.class)
    public void handleOneTxn(JSONObject txnJson, int blockHeight, int blockTime, int indexInBlock) throws Exception {

        int txnType = txnJson.getInteger("TxType");
        String txnHash = txnJson.getString("Hash");
        logger.info("####txnType:{}, txnHash:{}####", txnType, txnHash);

        try {
            JSONObject eventObj = getEventObjByTxnHash(txnHash);
            logger.info("eventObj:{}", eventObj.toJSONString());

            int state = eventObj.getInteger("State");
            BigDecimal gasConsumed = new BigDecimal(eventObj.getLongValue("GasConsumed"));
            gasConsumed = gasConsumed.divide(ONG_DECIMAL);
            logger.info("gasConsumed:{}", gasConsumed);

            //success transaction
            if (state == 1) {

                if (208 == txnJson.getInteger("TxType")) {
                    //deploy smart contract transaction
                    insertTxnBasicInfo(txnJson, blockHeight, blockTime, indexInBlock, 1, "", gasConsumed, 0, 2);
                }

                JSONArray notifyList = eventObj.getJSONArray("Notify");
                if (notifyList.size() > 0) {

                    for (int i = 0; i < notifyList.size(); i++) {

                        String contractAddress = Helper.reverse(((JSONObject) notifyList.get(i)).getString("ContractAddress"));
                        logger.info("####ContractAddress:{}####", contractAddress);

                        JSONArray stateList = ((JSONObject) notifyList.get(i)).getJSONArray("States");
                        //transfer transaction
                        if (configParam.ASSET_ONG_CODEHASH.equals(contractAddress) || configParam.ASSET_ONT_CODEHASH.equals(contractAddress)) {

                            handleTransferTxn(stateList, txnJson, blockHeight, blockTime, indexInBlock, contractAddress, gasConsumed, i + 1, notifyList.size());
                        } else if (configParam.ONTID_CODEHASH.equals(contractAddress)) {
                            //ontId operation transaction
                            handleOntIdTxn(stateList, txnJson, blockHeight, blockTime, indexInBlock, gasConsumed, i + 1);
                        } else if (configParam.CLAIMRECORD_CODEHASH.equals(contractAddress)) {
                            //claimrecord transaction
                            handleClaimRecordTxn(stateList, txnJson, blockHeight, blockTime, indexInBlock, gasConsumed, i + 1);
                        } else if (configParam.AUTH_CODEHASH.equals(contractAddress)) {
                            //权限相关交易
                            insertTxnBasicInfo(txnJson, blockHeight, blockTime, indexInBlock, 1, ConstantParam.AUTH_OPE_PREFIX, gasConsumed, i + 1, 6);
                        } else {
                            insertTxnBasicInfo(txnJson, blockHeight, blockTime, indexInBlock, 1, "", gasConsumed, i + 1, 0);
                        }
                    }
                } else {
                    insertTxnBasicInfo(txnJson, blockHeight, blockTime, indexInBlock, 1, "", gasConsumed, 1, 0);
                }

            } else if (state == 0) {
                //fail transaction
                insertTxnBasicInfo(txnJson, blockHeight, blockTime, indexInBlock, 2, "", gasConsumed, 1, 0);
            }


        } catch (RestfulException e) {
            logger.error("RestfulException...{}", e);

            if (com.github.ontio.utils.Helper.isJSONStr(e.getMessage())) {
                JSONObject eObj = JSON.parseObject(e.getMessage());
                if (43001 == eObj.getLong("Error") || 42002 == eObj.getLong("Error")) {
                    logger.info("######this transaction no event ...");
                    insertTxnBasicInfo(txnJson, blockHeight, blockTime, indexInBlock, 2, "", ZERO, 1, 0);
                }
            }

        } catch (Exception e) {
            logger.error("handleOneTxn error...", e);
            e.printStackTrace();
            throw e;
        }

        addTxnCount();
    }

    public synchronized void addTxnCount() {
        ConstantParam.TXN_INIT_AMOUNT++;
    }


    public synchronized void addOntIdTxncount() {
        ConstantParam.ONTIDTXN_INIT_AMOUNT++;
    }


    /**
     * insert transfer transaction
     *
     * @param txnJson
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @param codeHash
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void handleTransferTxn(JSONArray stateList, JSONObject txnJson, int blockHeight, int blockTime, int indexInBlock, String codeHash, BigDecimal gasConsumed, int indexInTxn, int notifyListSize) throws Exception {

        int eventType = 3;
        String assetName = "";
        if (configParam.ASSET_ONT_CODEHASH.equals(codeHash)) {
            assetName = "ont";
        } else if (configParam.ASSET_ONG_CODEHASH.equals(codeHash)) {
            assetName = "ong";
        }

        String action = (String) stateList.get(0);
        //notifylist的最后一个一定是收取手续费event
        if ((indexInTxn == notifyListSize) && configParam.ASSET_ONG_CODEHASH.equals(codeHash)) {
            action = "gasconsume";
            eventType = 1;
        }
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
        transactionDetailDO.setTxnhash(txnJson.getString("Hash"));
        transactionDetailDO.setTxntype(txnJson.getInteger("TxType"));
        transactionDetailDO.setTxntime(blockTime);
        transactionDetailDO.setAmount(amount);
        transactionDetailDO.setTxnindex(indexInTxn);
        transactionDetailDO.setConfirmflag(1);
        transactionDetailDO.setEventtype(eventType);
        transactionDetailMapper.insertSelective(transactionDetailDO);

    }

    /**
     * insert ontId operation transaction ans basic information of the transaction
     *
     * @param txnJson
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void handleOntIdTxn(JSONArray stateList, JSONObject txnJson, int blockHeight, int blockTime, int indexInBlock, BigDecimal gasConsumed, int indexInTxn) throws Exception {

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
        ontIdDO.setTxnhash(txnJson.getString("Hash"));
        ontIdDO.setTxntype(txnJson.getInteger("TxType"));
        ontIdDO.setDescription(descriptionStr);
        ontIdDO.setTxntime(blockTime);
        ontIdDO.setOntid(ontId);
        ontIdDO.setFee(gasConsumed);
        ontIdMapper.insertSelective(ontIdDO);

        insertTxnBasicInfo(txnJson, blockHeight, blockTime, indexInBlock, 1, ONTID_OPE_PREFIX + action, gasConsumed, indexInTxn, 4);

        if (ConstantParam.REGISTER.equals(action)) {
            //ontid交易数加1
            addOntIdTxncount();
        }

    }


    /**
     * insert claimrecord operation transaction insert basic information of the transaction
     *
     * @param txnJson
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void handleClaimRecordTxn(JSONArray stateList, JSONObject txnJson, int blockHeight, int blockTime, int indexInBlock, BigDecimal gasConsumed, int indexInTxn) throws Exception {

        String actionType = new String(Helper.hexToBytes(stateList.getString(0)));
        logger.info("####actionType:{}####", actionType);
        StringBuilder sb = new StringBuilder();
        sb.append(CLAIMRECORD_OPE);

        if (ConstantParam.CLAIMRECORD_OPE_PREFIX.equals(actionType)) {
            if (stateList.size() >= 4) {
                String issuerOntId = new String(Helper.hexToBytes(stateList.getString(1)));
                String action = new String(Helper.hexToBytes(stateList.getString(2)));
                String claimId = new String(Helper.hexToBytes(stateList.getString(3)));
                logger.info("####action:{}, issuerOntId:{}, claimid:{}#####", action, issuerOntId, claimId);
                sb.append(issuerOntId);
                sb.append(action);
                sb.append(claimId);
            }
        }

        insertTxnBasicInfo(txnJson, blockHeight, blockTime, indexInBlock, 1, sb.toString(), gasConsumed, indexInTxn, 5);
    }

    /**
     * insert the basic information of one transaction
     *
     * @param txnJson
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @param confirmFlag  1:success 2:fail
     * @param action
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertTxnBasicInfo(JSONObject txnJson, int blockHeight, int blockTime, int indexInBlock,
                                   int confirmFlag, String action, BigDecimal gasConsumed, int indexInTxn, int eventType) throws Exception {

        logger.info("{} run....", com.github.ontio.utils.Helper.currentMethod());

        TransactionDetail transactionDetailDO = new TransactionDetail();
        transactionDetailDO.setFromaddress("");
        transactionDetailDO.setToaddress("");
        transactionDetailDO.setAssetname("");
        transactionDetailDO.setDescription(action);
        transactionDetailDO.setFee(gasConsumed);
        transactionDetailDO.setHeight(blockHeight);
        transactionDetailDO.setBlockindex(indexInBlock);
        transactionDetailDO.setTxnhash(txnJson.getString("Hash"));
        transactionDetailDO.setTxntype(txnJson.getInteger("TxType"));
        transactionDetailDO.setTxntime(blockTime);
        transactionDetailDO.setAmount(new BigDecimal("0"));
        transactionDetailDO.setTxnindex(indexInTxn);
        transactionDetailDO.setConfirmflag(confirmFlag);
        transactionDetailDO.setEventtype(eventType);

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
            } catch (Exception ex) {

                if (com.github.ontio.utils.Helper.isJSONStr(ex.getMessage())) {
                    JSONObject eObj = JSON.parseObject(ex.getMessage());
                    if (43001 == eObj.getLong("Error") || 42002 == eObj.getLong("Error")) {
                        logger.info("######this transaction no event...");
                        throw new RestfulException(ex.getMessage());
                    }
                }

                logger.error("getEventObjByTxnHash error, try again...restsful:{},error:", ConstantParam.MASTERNODE_RESTFULURL, ex);

                if (tryTime < ConstantParam.NODE_RETRYMAXTIME && tryTime % configParam.NODE_INTERRUPTTIME_MAX == 0) {
                    switchNode();
                    tryTime++;
                    continue;
                } else if (tryTime < ConstantParam.NODE_RETRYMAXTIME) {
                    tryTime++;
                    Thread.sleep(1000);
                    continue;
                }
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


}
