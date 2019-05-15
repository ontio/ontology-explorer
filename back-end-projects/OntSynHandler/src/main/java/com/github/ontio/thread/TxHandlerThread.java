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
import com.github.ontio.common.Address;
import com.github.ontio.common.Helper;
import com.github.ontio.config.ParamsConfig;
import com.github.ontio.core.transaction.Transaction;
import com.github.ontio.mapper.*;
import com.github.ontio.model.common.EventTypeEnum;
import com.github.ontio.model.common.OntIdEventDesEnum;
import com.github.ontio.model.common.SessionMapperDto;
import com.github.ontio.model.common.TransactionTypeEnum;
import com.github.ontio.model.dao.*;
import com.github.ontio.network.exception.RestfulException;
import com.github.ontio.service.CommonService;
import com.github.ontio.smartcontract.neovm.abi.BuildParams;
import com.github.ontio.utils.ConstantParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
public class TxHandlerThread {

    public ThreadLocal<Map<String, Boolean>> IS_OEPTX_FLAG = new ThreadLocal<>();

    private final ParamsConfig paramsConfig;

    private final ContractMapper contractMapper;

    private final SqlSessionTemplate sqlSessionTemplate;

    private final CommonService commonService;

    @Autowired
    public TxHandlerThread(ParamsConfig paramsConfig, ContractMapper contractMapper, SqlSessionTemplate sqlSessionTemplate, CommonService commonService) {
        this.paramsConfig = paramsConfig;
        this.contractMapper = contractMapper;
        this.sqlSessionTemplate = sqlSessionTemplate;
        this.commonService = commonService;
    }

    @Async
    public Future<String> asyncHandleTx(JSONObject txJson, int blockHeight, int blockTime, int indexInBlock) throws Exception {
        Map<String, Boolean> map = new HashMap<>();
        map.put(ConstantParam.IS_OEP4TX, false);
        map.put(ConstantParam.IS_OEP5TX, false);
        map.put(ConstantParam.IS_OEP8TX, false);
        IS_OEPTX_FLAG.set(map);
        //设置一个模式为BATCH，自动提交为false的session，最后统一提交，需防止内存溢出
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        SessionMapperDto sessionMapperDto = constructSessionMapper(session);

        try {
            String threadName = Thread.currentThread().getName();
            log.info("{} run--------blockHeight:{},txHash:{}", threadName, blockHeight, txJson.getString("Hash"));
            handleOneTx(sessionMapperDto, txJson, blockHeight, blockTime, indexInBlock);
            //手动提交
            session.commit();
            //清理缓存，防止溢出
            session.clearCache();
            log.info("{} end-------blockHeight:{},txHash:{}", threadName, blockHeight, txJson.getString("Hash"));
            return new AsyncResult<String>("success");
        } catch (Exception e) {
            log.error("asyncHandleTx error...", e);
            session.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * 构造session的mapper
     *
     * @param session
     * @return
     */
    private SessionMapperDto constructSessionMapper(SqlSession session) {
        TxDetailMapper txDetailMapper = session.getMapper(TxDetailMapper.class);
        TxDetailDailyMapper txDetailDailyMapper = session.getMapper(TxDetailDailyMapper.class);
        Oep4TxDetailMapper oep4TxDetailMapper = session.getMapper(Oep4TxDetailMapper.class);
        Oep5TxDetailMapper oep5TxDetailMapper = session.getMapper(Oep5TxDetailMapper.class);
        Oep8TxDetailMapper oep8TxDetailMapper = session.getMapper(Oep8TxDetailMapper.class);
        OntidTxDetailMapper ontidTxDetailMapper = session.getMapper(OntidTxDetailMapper.class);
        TxEventLogMapper txEventLogMapper = session.getMapper(TxEventLogMapper.class);
        ContractMapper contractMapper = session.getMapper(ContractMapper.class);
        Oep5DragonMapper oep5DragonMapper = session.getMapper(Oep5DragonMapper.class);
        Oep5Mapper oep5Mapper = session.getMapper(Oep5Mapper.class);
        Oep8Mapper oep8Mapper = session.getMapper(Oep8Mapper.class);

        SessionMapperDto sessionMapperDto = SessionMapperDto.builder()
                .txDetailMapper(txDetailMapper)
                .txDetailDailyMapper(txDetailDailyMapper)
                .oep4TxDetailMapper(oep4TxDetailMapper)
                .oep5TxDetailMapper(oep5TxDetailMapper)
                .oep8TxDetailMapper(oep8TxDetailMapper)
                .ontidTxDetailMapper(ontidTxDetailMapper)
                .contractMapper(contractMapper)
                .txEventLogMapper(txEventLogMapper)
                .oep5DragonMapper(oep5DragonMapper)
                .oep5Mapper(oep5Mapper)
                .oep8Mapper(oep8Mapper)
                .build();
        return sessionMapperDto;
    }

    @Transactional(rollbackFor = Exception.class)
    public void handleOneTx(SessionMapperDto sessionMapperDto, JSONObject txJson, int blockHeight, int blockTime, int indexInBlock) throws Exception {

        int txType = txJson.getInteger("TxType");
        String txHash = txJson.getString("Hash");
        String payer = txJson.getString("Payer");
        String calledContractHash = parseCalledContractHash(txJson);
        log.info("####txType:{}, txHash:{}, calledContractHash:{}", txType, txHash, calledContractHash);

        try {
            JSONObject eventLogObj = txJson.getJSONObject("EventLog");
            log.info("eventLog:{}", eventLogObj.toJSONString());
            insertTxEventLog(sessionMapperDto, txHash, blockTime, txType, blockHeight, indexInBlock, calledContractHash, eventLogObj.toJSONString());
            //eventstate 1:success 0:failed
            int confirmFlag = eventLogObj.getInteger("State");

            BigDecimal gasConsumed = new BigDecimal(eventLogObj.getLongValue("GasConsumed")).divide(ConstantParam.ONG_DECIMAL);
            //deploy smart contract transaction
            if (TransactionTypeEnum.DEPLOYCODE.type() == txType) {
                handleDeployContractTx(sessionMapperDto, txJson, blockHeight, blockTime, indexInBlock, confirmFlag, gasConsumed, payer, calledContractHash);
            }

            JSONArray notifyArray = eventLogObj.getJSONArray("Notify");
            //no event transaction
            if (notifyArray.size() == 0) {
                insertTxBasicInfo(sessionMapperDto, txType, txHash, blockHeight, blockTime, indexInBlock, confirmFlag, "",
                        gasConsumed, 1, EventTypeEnum.Others.type(), "", payer, calledContractHash);
                return;
            }

            // 判断是否为OEP合约交易,用于将属于OEP交易的数据存储在各自OEP详情表中
            //judgeIsOepTransaction(notifyList);
            JSONArray stateArray = null;
            for (int i = 0, len = notifyArray.size(); i < len; i++) {
                JSONObject notifyObj = (JSONObject) notifyArray.get(i);
                String contractAddress = notifyObj.getString("ContractAddress");

                Object object = notifyObj.get("States");
                if (object instanceof JSONArray) {
                    stateArray = (JSONArray) object;
                } else {
                    //other transaction
                    insertTxBasicInfo(sessionMapperDto, txType, txHash, blockHeight, blockTime, indexInBlock, confirmFlag, "",
                            gasConsumed, i + 1, EventTypeEnum.Others.type(), contractAddress, payer, calledContractHash);
                    continue;
                }

                if (paramsConfig.ONG_CONTRACTHASH.equals(contractAddress) || paramsConfig.ONT_CONTRACTHASH.equals(contractAddress)) {
                    //transfer transaction
                    handleTransferTxn(sessionMapperDto, stateArray, txType, txHash, blockHeight, blockTime, indexInBlock,
                            contractAddress, gasConsumed, i + 1, notifyArray.size(), confirmFlag, payer, calledContractHash);

                } else if (paramsConfig.ONTID_CONTRACTHASH.equals(contractAddress)) {
                    //ontId operation transaction
                    handleOntIdTxn(sessionMapperDto, stateArray, txType, txHash, blockHeight, blockTime, indexInBlock,
                            gasConsumed, i + 1, contractAddress, payer, calledContractHash);
                    addOneBlockOntIdTxnCount();

                } else if (paramsConfig.CLAIMRECORD_CONTRACTHASH.equals(contractAddress)) {
                    //claimrecord transaction
                    handleClaimRecordTxn(sessionMapperDto, stateArray, txType, txHash, blockHeight, blockTime, indexInBlock,
                            gasConsumed, i + 1, contractAddress, payer, calledContractHash);

                } else if (paramsConfig.AUTH_CONTRACTHASH.equals(contractAddress)) {
                    //auth transaction
                    insertTxBasicInfo(sessionMapperDto, txType, txHash, blockHeight, blockTime, indexInBlock, confirmFlag, EventTypeEnum.Auth.des(),
                            gasConsumed, i + 1, EventTypeEnum.Auth.type(), contractAddress, payer, calledContractHash);

                } else if (ConstantParam.OEP8CONTRACTS.contains(contractAddress)) {
                    //OEP8交易
                    handleOep8TransferTx(sessionMapperDto, stateArray, txType, txHash, blockHeight, blockTime, indexInBlock,
                            gasConsumed, i + 1, confirmFlag, contractAddress, notifyArray.size(), payer, calledContractHash);

                } else if (ConstantParam.OEP5CONTRACTS.contains(contractAddress)) {
                    //OEP5交易
                    handleOep5TransferTxn(sessionMapperDto, stateArray, txType, txHash, blockHeight, blockTime, indexInBlock,
                            gasConsumed, i + 1, confirmFlag, contractAddress, ConstantParam.OEP5MAP.get(contractAddress), payer, calledContractHash);

                } else if (ConstantParam.OEP4CONTRACTS.contains(contractAddress)) {
                    //OEP4交易
                    handleOep4TransferTxn(sessionMapperDto, stateArray, txType, txHash, blockHeight, blockTime, indexInBlock,
                            gasConsumed, i + 1, confirmFlag, (JSONObject) ConstantParam.OEP4MAP.get(contractAddress), contractAddress, payer, calledContractHash);

                } else {
                    //other transaction
                    insertTxBasicInfo(sessionMapperDto, txType, txHash, blockHeight, blockTime, indexInBlock, confirmFlag, "",
                            gasConsumed, i + 1, EventTypeEnum.Others.type(), contractAddress, payer, calledContractHash);
                }
            }
        } catch (RestfulException e) {
            log.error("handleOneTx RestfulException...{}", e);
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            log.error("handleOneTx error...", e);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 记录交易的eventlog
     *
     * @param sessionMapperDto
     * @param txHash
     * @param txTime
     * @param txType
     * @param blockHeight
     * @param blockIndex
     * @param callednContractHash
     * @param eventLog
     */
    private void insertTxEventLog(SessionMapperDto sessionMapperDto, String txHash, int txTime, int txType,
                                  int blockHeight, int blockIndex, String callednContractHash, String eventLog) {
        TxEventLog txEventLog = TxEventLog.builder()
                .txHash(txHash)
                .txTime(txTime)
                .txType(txType)
                .blockHeight(blockHeight)
                .blockIndex(blockIndex)
                .eventLog(eventLog.substring(0, eventLog.length() > 5000 ? 5000 : eventLog.length()))
                .calledContractHash(callednContractHash)
                .build();
        sessionMapperDto.getTxEventLogMapper().insert(txEventLog);
    }

    /**
     * 解析这笔交易真正调用的合约hash
     *
     * @param txJson
     * @return
     */
    private String parseCalledContractHash(JSONObject txJson) {

        String code = txJson.getJSONObject("Payload").getString("Code");
        String calledContractHash = "";

        while (code.contains("67")) {
            int index = code.indexOf("67");
            code = code.substring(index + 2);
            if (code.length() < 40) {
                //native合约都是792e4e61746976652e496e766f6b65
                calledContractHash = code;
                break;
            } else if (code.length() == 40) {
                calledContractHash = Helper.reverse(code);
                break;
            }
        }

        if (ConstantParam.OEP5CONTRACTS.contains(calledContractHash)) {
            IS_OEPTX_FLAG.get().put(ConstantParam.IS_OEP5TX, true);
        } else if (ConstantParam.OEP4CONTRACTS.contains(calledContractHash)) {
            IS_OEPTX_FLAG.get().put(ConstantParam.IS_OEP4TX, true);
        } else if (ConstantParam.OEP8CONTRACTS.contains(calledContractHash)) {
            IS_OEPTX_FLAG.get().put(ConstantParam.IS_OEP8TX, true);
        }
        return calledContractHash;
    }


    /**
     * 处理部署合约交易
     *
     * @param sessionMapperDto
     * @param txJson
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @param confirmFlag
     * @param gasConsumed
     * @param payer
     * @throws Exception
     */
    private void handleDeployContractTx(SessionMapperDto sessionMapperDto, JSONObject txJson, int blockHeight, int blockTime, int indexInBlock,
                                        int confirmFlag, BigDecimal gasConsumed, String payer, String calledContractHash) throws Exception {

        String txHash = txJson.getString("Hash");
        int txType = txJson.getInteger("TxType");
        JSONObject contractObj = commonService.getContractInfoByTxHash(txHash);
        //原生合约hash需要转换
        String contractHash = getNativeContractHash(contractObj.getString("ContractHash"));

        contractObj.remove("ContractHash");

        insertTxBasicInfo(sessionMapperDto, txType, txHash, blockHeight,
                blockTime, indexInBlock, confirmFlag, contractObj.toString(),
                gasConsumed, 0, EventTypeEnum.DeployContract.type(), contractHash, payer, calledContractHash);

        Contract contract = Contract.builder()
                .contractHash(contractHash)
                .build();
        // 部署合约，将合约信息保存到合约列表
        Integer count = contractMapper.selectCount(contract);
        if (count.equals(0)) {
            insertContractInfo(sessionMapperDto, contractHash, blockTime, contractObj, payer);
        }
    }

    /**
     * 遍历notify里event的合约hash判断这笔交易是否是对应的oep交易
     *
     * @param notifyList
     */
/*    private void judgeIsOepTransaction(JSONArray notifyList) {
        for (int i = 0, len = notifyList.size(); i < len; i++) {
            JSONObject notifyObj = (JSONObject) notifyList.get(i);
            String contractAddress = notifyObj.getString("ContractAddress");

            if (ConstantParam.OEP5CONTRACTS.contains(contractAddress)) {
                OEP5TX = true;
                break;
            } else if (ConstantParam.OEP4CONTRACTS.contains(contractAddress)) {
                OEP4TX = true;
                break;
            } else if (ConstantParam.OEP8CONTRACTS.contains(contractAddress)) {
                OEP8TX = true;
                break;
            }
        }
    }*/

    /**
     * 累加一个区块里注册ontid的交易
     */
    public synchronized void addOneBlockOntIdCount() {
        ConstantParam.ONEBLOCK_ONTID_COUNT++;
    }

    /**
     * 累加一个区块里跟ontid相关的交易
     */
    public synchronized void addOneBlockOntIdTxnCount() {
        ConstantParam.ONEBLOCK_ONTIDTX_COUNT++;
    }


    /**
     * 处理oep8交易
     *
     * @param sessionMapperDto
     * @param stateArray
     * @param txType
     * @param txHash
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @param gasConsumed
     * @param indexInTx
     * @param confirmFlag
     * @param contractAddress
     * @param stateSize
     * @throws Exception
     */
    private void handleOep8TransferTx(SessionMapperDto sessionMapperDto, JSONArray stateArray, int txType, String txHash, int blockHeight,
                                      int blockTime, int indexInBlock, BigDecimal gasConsumed, int indexInTx,
                                      int confirmFlag, String contractAddress, int stateSize, String payer, String calledContractHash) throws Exception {
        if (stateArray.size() < 5) {
            TxDetail txDetail = generateTransaction("", "", "", ConstantParam.ZERO, txType, txHash, blockHeight,
                    blockTime, indexInBlock, confirmFlag, "", gasConsumed, indexInTx, EventTypeEnum.Others.type(), contractAddress, payer, calledContractHash);

            sessionMapperDto.getTxDetailMapper().insert(txDetail);
            sessionMapperDto.getTxDetailDailyMapper().insert(TxDetail.toTxDetailDaily(txDetail));
            sessionMapperDto.getOep8TxDetailMapper().insert(TxDetail.toOep8TxDetail(txDetail));
            return;
        }

        String action = new String(Helper.hexToBytes((String) stateArray.get(0)));
        String fromAddress = (String) stateArray.get(1);
        String toAddress = (String) stateArray.get(2);
        String tokenId = (String) stateArray.get(3);
        JSONObject oep8Obj = (JSONObject) ConstantParam.OEP8MAP.get(contractAddress + "-" + tokenId);
        if ("00".equals(fromAddress) && stateSize == 2) {
            // mint方法即增加发行量方法, 区分标志：fromAddress为“00”，同时state数量为2
            //TODO 南瓜合成交易，金南瓜会减少
            Oep8 oep8 = Oep8.builder()
                    .contractHash(contractAddress)
                    .tokenId((String) stateArray.get(3))
                    .totalSupply(Long.valueOf(ConstantParam.ONT_SDKSERVICE.neovm().oep8().queryTotalSupply(Helper.hexToBytes(tokenId))))
                    .build();
            sessionMapperDto.getOep8Mapper().updateByPrimaryKeySelective(oep8);
        }

        if (40 == fromAddress.length()) {
            fromAddress = Address.parse(fromAddress).toBase58();
        }
        if (40 == toAddress.length()) {
            toAddress = Address.parse(toAddress).toBase58();
        }

        BigDecimal eventAmount = new BigDecimal(Helper.BigIntFromNeoBytes(Helper.hexToBytes((String) stateArray.get(4))).longValue());
        log.info("OEP8TransferTx:fromaddress:{}, toaddress:{}, tokenid:{}, amount:{}", fromAddress, toAddress, tokenId, eventAmount);

        TxDetail txDetail = generateTransaction(fromAddress, toAddress, oep8Obj.getString("name"), eventAmount, txType, txHash, blockHeight,
                blockTime, indexInBlock, confirmFlag, action, gasConsumed, indexInTx, EventTypeEnum.Transfer.type(), contractAddress, payer, calledContractHash);

        sessionMapperDto.getTxDetailMapper().insert(txDetail);
        sessionMapperDto.getTxDetailDailyMapper().insert(TxDetail.toTxDetailDaily(txDetail));
        sessionMapperDto.getOep8TxDetailMapper().insert(TxDetail.toOep8TxDetail(txDetail));
    }

    /**
     * 处理oep5交易
     *
     * @param sessionMapperDto
     * @param stateArray
     * @param txType
     * @param txHash
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @param gasConsumed
     * @param indexInTx
     * @param confirmFlag
     * @param contractAddress
     * @param oep5Obj
     * @throws Exception
     */
    private void handleOep5TransferTxn(SessionMapperDto sessionMapperDto, JSONArray stateArray, int txType, String txHash, int blockHeight,
                                       int blockTime, int indexInBlock, BigDecimal gasConsumed, int indexInTx, int confirmFlag,
                                       String contractAddress, JSONObject oep5Obj, String payer, String calledContractHash) throws Exception {

        String action = new String(Helper.hexToBytes((String) stateArray.get(0)));
        //只解析birth和transfer合约方法
        if (!(action.equalsIgnoreCase("transfer") || action.equalsIgnoreCase("birth"))) {
            TxDetail txDetail = generateTransaction("", "", "", ConstantParam.ZERO, txType, txHash, blockHeight,
                    blockTime, indexInBlock, confirmFlag, action, gasConsumed, indexInTx, EventTypeEnum.Others.type(), contractAddress, payer, calledContractHash);
            sessionMapperDto.getTxDetailMapper().insert(txDetail);
            sessionMapperDto.getTxDetailDailyMapper().insert(TxDetail.toTxDetailDaily(txDetail));
            sessionMapperDto.getOep5TxDetailMapper().insert(TxDetail.toOep5TxDetail(txDetail));
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
        BigDecimal amount = ConstantParam.ZERO;
        //云斗龙特殊处理,记录birth出来的云斗龙信息
        if ("HyperDragons".equalsIgnoreCase(oep5Obj.getString("name"))) {
            // 如果是birth方法，tokenid位置在2；如果是transfer方法，tokenid位置在3
            String dragonId = "";
            if ("birth".equalsIgnoreCase(action)) {
                dragonId = Helper.BigIntFromNeoBytes(Helper.hexToBytes((String) stateArray.get(2))).toString();
                fromAddress = "";
                toAddress = "";

                Oep5Dragon oep5Dragon = Oep5Dragon.builder()
                        .contractHash(contractAddress)
                        .assetName(ConstantParam.ASSET_NAME_DRAGON + dragonId)
                        .jsonUrl(getDragonUrl(contractAddress, dragonId))
                        .build();
                sessionMapperDto.getOep5DragonMapper().insert(oep5Dragon);
            } else {
                amount = ConstantParam.ONE;
                dragonId = Helper.BigIntFromNeoBytes(Helper.hexToBytes((String) stateArray.get(3))).toString();
            }
            assetName = ConstantParam.ASSET_NAME_DRAGON + dragonId;
        } else {
            //OEP5初始化交易，更新total_supply。且tokenid位置在2
            if ("birth".equalsIgnoreCase(action)) {
                assetName = oep5Obj.getString("symbol") + stateArray.get(2);
                fromAddress = "";
                toAddress = "";

                Long totalSupply = commonService.getOep5TotalSupply(contractAddress);
                Oep5 oep5 = Oep5.builder()
                        .contractHash(contractAddress)
                        .totalSupply(totalSupply)
                        .build();
                sessionMapperDto.getOep5Mapper().updateByPrimaryKeySelective(oep5);

            } else if ("transfer".equalsIgnoreCase(action)) {
                //transfer方法，tokenid在位置3
                assetName = oep5Obj.getString("symbol") + stateArray.get(3);
                amount = ConstantParam.ONE;
            }
        }

        log.info("OEP5TransferTx:fromaddress:{}, toaddress:{}, assetName:{}", fromAddress, toAddress, assetName);

        TxDetail txDetail = generateTransaction(fromAddress, toAddress, assetName, amount, txType, txHash, blockHeight,
                blockTime, indexInBlock, confirmFlag, action, gasConsumed, indexInTx, EventTypeEnum.Transfer.type(), contractAddress, payer, calledContractHash);

        sessionMapperDto.getTxDetailMapper().insert(txDetail);
        sessionMapperDto.getTxDetailDailyMapper().insert(TxDetail.toTxDetailDaily(txDetail));
        sessionMapperDto.getOep5TxDetailMapper().insert(TxDetail.toOep5TxDetail(txDetail));
    }


    /**
     * 处理原生ont，ong转账
     *
     * @param sessionMapperDto
     * @param stateList
     * @param txType
     * @param txHash
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @param contractAddress
     * @param gasConsumed
     * @param indexInTx
     * @param notifyListSize
     * @param confirmFlag
     * @param payer
     * @throws Exception
     */
    private void handleTransferTxn(SessionMapperDto sessionMapperDto, JSONArray stateList, int txType, String txHash,
                                   int blockHeight, int blockTime, int indexInBlock, String contractAddress,
                                   BigDecimal gasConsumed, int indexInTx, int notifyListSize, int confirmFlag,
                                   String payer, String calledContractHash) throws Exception {
        if (stateList.size() < 3) {
            TxDetail txDetail = generateTransaction("", "", "", ConstantParam.ZERO, txType, txHash, blockHeight,
                    blockTime, indexInBlock, confirmFlag, "", gasConsumed, indexInTx, EventTypeEnum.Transfer.type(), contractAddress, payer, calledContractHash);
            sessionMapperDto.getTxDetailMapper().insert(txDetail);
            sessionMapperDto.getTxDetailDailyMapper().insert(TxDetail.toTxDetailDaily(txDetail));
            return;
        }

        int eventType = EventTypeEnum.Transfer.type();
        String assetName = "";
        if (paramsConfig.ONT_CONTRACTHASH.equals(contractAddress)) {
            assetName = ConstantParam.ASSET_NAME_ONT;
        } else if (paramsConfig.ONG_CONTRACTHASH.equals(contractAddress)) {
            assetName = ConstantParam.ASSET_NAME_ONG;
        }

        String action = (String) stateList.get(0);
        //手续费不为0的情况下，notifylist的最后一个一定是收取手续费event
        if (gasConsumed.compareTo(ConstantParam.ZERO) != 0 && (indexInTx == notifyListSize) && paramsConfig.ONG_CONTRACTHASH.equals(contractAddress)) {
            action = EventTypeEnum.Gasconsume.des();
            eventType = EventTypeEnum.Gasconsume.type();
        }

        String fromAddress = (String) stateList.get(1);
        String toAddress = (String) stateList.get(2);
        BigDecimal amount = new BigDecimal((stateList.get(3)).toString());
        log.info("####fromAddress:{},toAddress:{},amount:{}####", fromAddress, toAddress, amount.toPlainString());

        TxDetail txDetail = generateTransaction(fromAddress, toAddress, assetName, amount, txType, txHash, blockHeight,
                blockTime, indexInBlock, confirmFlag, action, gasConsumed, indexInTx, eventType, contractAddress, payer, calledContractHash);

        sessionMapperDto.getTxDetailMapper().insert(txDetail);
        sessionMapperDto.getTxDetailDailyMapper().insert(TxDetail.toTxDetailDaily(txDetail));
        // OEP交易的手续费入库
        if (paramsConfig.ONG_CONTRACTHASH.equals(contractAddress)) {
            // OEP合约交易插入对应的子表
            if (IS_OEPTX_FLAG.get().get(ConstantParam.IS_OEP8TX)) {
                sessionMapperDto.getOep8TxDetailMapper().insert(TxDetail.toOep8TxDetail(txDetail));
            } else if (IS_OEPTX_FLAG.get().get(ConstantParam.IS_OEP5TX)) {
                sessionMapperDto.getOep5TxDetailMapper().insert(TxDetail.toOep5TxDetail(txDetail));
            } else if (IS_OEPTX_FLAG.get().get(ConstantParam.IS_OEP4TX)) {
                sessionMapperDto.getOep4TxDetailMapper().insert(TxDetail.toOep4TxDetail(txDetail));
            }
        }

    }

    /**
     * 处理ontid交易
     *
     * @param sessionMapperDto
     * @param stateList
     * @param txType
     * @param txHash
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @param gasConsumed
     * @param indexInTx
     * @param contractAddress
     * @throws Exception
     */
    private void handleOntIdTxn(SessionMapperDto sessionMapperDto, JSONArray stateList, int txType, String txHash, int blockHeight,
                                int blockTime, int indexInBlock, BigDecimal gasConsumed, int indexInTx,
                                String contractAddress, String payer, String calledContractHash) throws Exception {

        String action = stateList.getString(0);
        String ontId = "";
        if (OntIdEventDesEnum.REGISTERONTID.des().equals(action)) {
            ontId = stateList.getString(1);
        } else {
            ontId = stateList.getString(2);
        }
        String descriptionStr = formatOntIdOperation(ontId, action, stateList);

        OntidTxDetail ontidTxDetail = OntidTxDetail.builder()
                .blockHeight(blockHeight)
                .txHash(txHash)
                .txType(txType)
                .txTime(blockTime)
                .ontid(ontId)
                .fee(gasConsumed)
                .description(descriptionStr)
                .build();
        sessionMapperDto.getOntidTxDetailMapper().insert(ontidTxDetail);

        insertTxBasicInfo(sessionMapperDto, txType, txHash, blockHeight, blockTime, indexInBlock, 1, EventTypeEnum.Ontid.des() + action,
                gasConsumed, indexInTx, EventTypeEnum.Ontid.type(), contractAddress, payer, calledContractHash);

        //如果是注册ontid交易，ontid数量加1
        if (ConstantParam.REGISTER.equals(action)) {
            addOneBlockOntIdCount();
        }
    }

    /**
     * 处理存证交易
     *
     * @param sessionMapperDto
     * @param stateList
     * @param txType
     * @param txHash
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @param gasConsumed
     * @param indexInTx
     * @param contractAddress
     * @throws Exception
     */
    private void handleClaimRecordTxn(SessionMapperDto sessionMapperDto, JSONArray stateList, int txType, String txHash, int blockHeight,
                                      int blockTime, int indexInBlock, BigDecimal gasConsumed, int indexInTx,
                                      String contractAddress, String payer, String calledContractHash) throws Exception {

        String actionType = new String(Helper.hexToBytes(stateList.getString(0)));
        StringBuilder sb = new StringBuilder(140);
        sb.append(EventTypeEnum.Claimrecord.des());

        if (ConstantParam.CLAIMRECORD_OPE_PREFIX.equals(actionType)) {
            if (stateList.size() >= 4) {
                String issuerOntId = new String(Helper.hexToBytes(stateList.getString(1)));
                String action = new String(Helper.hexToBytes(stateList.getString(2)));
                String claimId = new String(Helper.hexToBytes(stateList.getString(3)));
                log.info("####ClaimRecord:action:{}, issuerOntId:{}, claimId:{}#####", action, issuerOntId, claimId);
                sb.append(issuerOntId);
                sb.append(action);
                sb.append("claimId:");
                sb.append(claimId);
                //创建claim的动作也记录到ontid的表中
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(action);
                stringBuilder.append("claimId:");
                stringBuilder.append(claimId);

                OntidTxDetail ontidTxDetail = OntidTxDetail.builder()
                        .blockHeight(blockHeight)
                        .txHash(txHash)
                        .txType(txType)
                        .txTime(blockTime)
                        .ontid(issuerOntId)
                        .fee(gasConsumed)
                        .description(stringBuilder.toString())
                        .build();
                sessionMapperDto.getOntidTxDetailMapper().insert(ontidTxDetail);
            }
        }

        insertTxBasicInfo(sessionMapperDto, txType, txHash, blockHeight, blockTime, indexInBlock, 1, sb.toString(),
                gasConsumed, indexInTx, EventTypeEnum.Claimrecord.type(), contractAddress, payer, calledContractHash);
    }

    /**
     * 构造基本交易信息dao
     *
     * @param fromAddress
     * @param toAddress
     * @param assetName
     * @param account
     * @param txType
     * @param txHash
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @param confirmFlag
     * @param action
     * @param gasConsumed
     * @param indexInTx
     * @param eventType
     * @param contractAddress
     * @param payer
     * @return
     */
    private TxDetail generateTransaction(String fromAddress, String toAddress, String assetName, BigDecimal account, int txType,
                                         String txHash, int blockHeight, int blockTime, int indexInBlock, int confirmFlag,
                                         String action, BigDecimal gasConsumed, int indexInTx, int eventType, String contractAddress,
                                         String payer, String calledContractHash) {
        TxDetail txDetail = TxDetail.builder()
                .txHash(txHash)
                .txType(txType)
                .txTime(blockTime)
                .blockHeight(blockHeight)
                .blockIndex(indexInBlock)
                .confirmFlag(confirmFlag)
                .description(action)
                .fee(gasConsumed)
                .txIndex(indexInTx)
                .eventType(eventType)
                .payer(payer)
                .calledContractHash(calledContractHash)
                .contractHash(contractAddress)
                .amount(account)
                .assetName(assetName)
                .fromAddress(fromAddress)
                .toAddress(toAddress)
                .build();
        return txDetail;
    }

    /**
     * 处理交易基本信息
     *
     * @param sessionMapperDto
     * @param txType
     * @param txHash
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @param confirmFlag
     * @param action
     * @param gasConsumed
     * @param indexInTx
     * @param eventType
     * @param contractAddress
     * @param payer
     * @throws Exception
     */
    private void insertTxBasicInfo(SessionMapperDto sessionMapperDto, int txType, String txHash, int blockHeight, int blockTime,
                                   int indexInBlock, int confirmFlag, String action, BigDecimal gasConsumed, int indexInTx,
                                   int eventType, String contractAddress, String payer, String calledContractHash) throws Exception {

        TxDetail txDetail = generateTransaction("", "", "", ConstantParam.ZERO, txType, txHash, blockHeight,
                blockTime, indexInBlock, confirmFlag, action, gasConsumed, indexInTx, eventType, contractAddress, payer, calledContractHash);

        sessionMapperDto.getTxDetailMapper().insert(txDetail);
        sessionMapperDto.getTxDetailDailyMapper().insert(TxDetail.toTxDetailDaily(txDetail));

        // OEP合约交易插入对应的子表
        if (IS_OEPTX_FLAG.get().get(ConstantParam.IS_OEP8TX)) {
            sessionMapperDto.getOep8TxDetailMapper().insert(TxDetail.toOep8TxDetail(txDetail));
        } else if (IS_OEPTX_FLAG.get().get(ConstantParam.IS_OEP5TX)) {
            sessionMapperDto.getOep5TxDetailMapper().insert(TxDetail.toOep5TxDetail(txDetail));
        } else if (IS_OEPTX_FLAG.get().get(ConstantParam.IS_OEP4TX)) {
            sessionMapperDto.getOep4TxDetailMapper().insert(TxDetail.toOep4TxDetail(txDetail));
        }
    }


    /**
     * 记录合约基本信息
     *
     * @param contractHash
     * @param blockTime
     * @param contractObj
     * @param player
     */
    private void insertContractInfo(SessionMapperDto sessionMapperDto, String contractHash, int blockTime, JSONObject contractObj, String player) {

        Contract contract = Contract.builder()
                .contractHash(contractHash)
                .name(contractObj.getString("Name"))
                .description(contractObj.getString("Description"))
                .abi("")
                .code("")
                .sourceCode("")
                .createTime(blockTime)
                .updateTime(blockTime)
                .creator(player)
                .txCount(0)
                .addressCount(0)
                .ongSum(ConstantParam.ZERO)
                .ontSum(ConstantParam.ZERO)
                .tokenSum(new JSONObject().toJSONString())
                .dappstoreFlag(false)
                .auditFlag(false)
                .category("")
                .contactInfo("")
                .type("")
                .logo("")
                .dappName("")
                .totalReward(ConstantParam.ZERO)
                .lastweekReward(ConstantParam.ZERO)
                .build();
        sessionMapperDto.getContractMapper().insertSelective(contract);
    }

    /**
     * 原生合约hash需要特殊转换
     *
     * @param contractHash
     * @return
     */
    private String getNativeContractHash(String contractHash) {
        switch (contractHash) {
            case "0239dcf9b4a46f15c5f23f20d52fac916a0bac0d":
                contractHash = paramsConfig.ONT_CONTRACTHASH;
                break;
            case "08b6dcfed2aace9190a44ae34a320e42c04b46ac":
                contractHash = paramsConfig.ONG_CONTRACTHASH;
                break;
            case "6815cbe7b4dbad4d2d09ae035141b5254a002f79":
                contractHash = paramsConfig.ONTID_CONTRACTHASH;
                break;
            case "24a15c6aed092dfaa711c4974caf1e9d307bf4b5":
                contractHash = paramsConfig.AUTH_CONTRACTHASH;
                break;
            default:
                contractHash = contractHash;
        }

        return contractHash;
    }

    /**
     * 处理oep4交易
     *
     * @param sessionMapperDto
     * @param stateArray
     * @param txType
     * @param txHash
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @param gasConsumed
     * @param indexInTx
     * @param confirmFlag
     * @param oep4Obj
     * @param contractHash
     * @throws Exception
     */
    private void handleOep4TransferTxn(SessionMapperDto sessionMapperDto, JSONArray stateArray, int txType, String txHash, int blockHeight,
                                       int blockTime, int indexInBlock, BigDecimal gasConsumed, int indexInTx, int confirmFlag,
                                       JSONObject oep4Obj, String contractHash, String payer, String calledContractHash) throws Exception {
        if (stateArray.size() < 3) {
            TxDetail txDetail = generateTransaction("", "", "", ConstantParam.ZERO, txType, txHash, blockHeight,
                    blockTime, indexInBlock, confirmFlag, "", gasConsumed, indexInTx, EventTypeEnum.Others.type(), contractHash, payer, calledContractHash);
            sessionMapperDto.getTxDetailMapper().insert(txDetail);
            sessionMapperDto.getTxDetailDailyMapper().insert(TxDetail.toTxDetailDaily(txDetail));
            sessionMapperDto.getOep4TxDetailMapper().insert(TxDetail.toOep4TxDetail(txDetail));
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
            String action = new String(Helper.hexToBytes((String) stateArray.get(0)));
            if (!action.equalsIgnoreCase("transfer")) {
                return;
            }
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
        log.info("OEP4TransferTx:fromaddress:{}, toaddress:{}, amount:{}", fromAddress, toAddress, eventAmount);

        String assetName = oep4Obj.getString("symbol");
        BigDecimal amount = eventAmount.divide(new BigDecimal(Math.pow(10, oep4Obj.getInteger("decimals"))));

        TxDetail txDetail = generateTransaction(fromAddress, toAddress, assetName, amount, txType, txHash, blockHeight,
                blockTime, indexInBlock, confirmFlag, EventTypeEnum.Transfer.des(), gasConsumed, indexInTx, EventTypeEnum.Transfer.type(), contractHash, payer, calledContractHash);

        sessionMapperDto.getTxDetailMapper().insert(txDetail);
        sessionMapperDto.getTxDetailDailyMapper().insert(TxDetail.toTxDetailDaily(txDetail));
        sessionMapperDto.getOep4TxDetailMapper().insert(TxDetail.toOep4TxDetail(txDetail));
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

        if (OntIdEventDesEnum.REGISTERONTID.des().equals(action)) {

            descriptionSb.append(ontId);
            str = descriptionSb.toString();
            log.info("####Register OntId:{}", ontId);

        } else if (OntIdEventDesEnum.PUBLICKEYOPE.des().equals(action)) {

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

        } else if (OntIdEventDesEnum.ATTRIBUTEOPE.des().equals(action)) {

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

        } else if (OntIdEventDesEnum.RECOVERYOPE.des().equals(action)) {

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
     * 获取dragon的信息
     *
     * @param contractHash
     * @param tokenId
     * @return
     */
    private String getDragonUrl(String contractHash, String tokenId) {
        try {
            List paramList = new ArrayList<>();
            paramList.add("tokenMetadata".getBytes());

            List args = new ArrayList();
            args.add(tokenId);
            paramList.add(args);
            byte[] params = BuildParams.createCodeParamsScript(paramList);

            Transaction tx = ConstantParam.ONT_SDKSERVICE.vm().makeInvokeCodeTransaction(Helper.reverse(contractHash), null, params, null, 20000, 500);
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
