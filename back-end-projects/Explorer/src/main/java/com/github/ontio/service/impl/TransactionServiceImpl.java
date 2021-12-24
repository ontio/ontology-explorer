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
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.ontio.config.ParamsConfig;
import com.github.ontio.mapper.*;
import com.github.ontio.model.common.*;
import com.github.ontio.model.dao.TxEventLog;
import com.github.ontio.model.dto.*;
import com.github.ontio.service.ITransactionService;
import com.github.ontio.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("TransactionService")
public class TransactionServiceImpl implements ITransactionService {

    private static final String NATIVE_CALLED_CONTRACT_HASH = "792e4e61746976652e496e766f6b65";

    private final TxDetailMapper txDetailMapper;
    private final CurrentMapper currentMapper;
    private final OntidTxDetailMapper ontidTxDetailMapper;
    private final TxEventLogMapper txEventLogMapper;
    private LoadingCache<String, ContractType> contractTypes;
    private final ContractMapper contractMapper;
    private final Web3jSdkUtil web3jSdkUtil;
    private final ContractTagMapper contractTagMapper;
    private final ParamsConfig paramsConfig;

    @Autowired
    public TransactionServiceImpl(TxDetailMapper txDetailMapper, CurrentMapper currentMapper,
                                  OntidTxDetailMapper ontidTxDetailMapper, TxEventLogMapper txEventLogMapper, ContractMapper contractMapper, Web3jSdkUtil web3jSdkUtil, ParamsConfig paramsConfig, ContractTagMapper contractTagMapper) {
        this.txDetailMapper = txDetailMapper;
        this.currentMapper = currentMapper;
        this.ontidTxDetailMapper = ontidTxDetailMapper;
        this.txEventLogMapper = txEventLogMapper;
        this.contractMapper = contractMapper;
        this.web3jSdkUtil = web3jSdkUtil;
        this.paramsConfig = paramsConfig;
        this.contractTagMapper = contractTagMapper;
    }

    private OntologySDKService sdk;

    private synchronized void initSDK() {
        if (sdk == null) {
            sdk = OntologySDKService.getInstance(paramsConfig);
        }
    }

    @Override
    public ResponseBean queryLatestTxs(int count) {

        List<TxEventLogDto> txEventLogDtos = txEventLogMapper.selectTxsByPage(0, count);
        List<TxEventLogTxTypeDto> result = null;
        if (txEventLogDtos != null) {
            result = txEventLogDtos.stream().map(this::convertTxEventLog).collect(Collectors.toList());
        }

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), result);
    }

    @Override
    public ResponseBean queryTxsByPage(int pageNumber, int pageSize) {

        int start = pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1);

        List<TxEventLogDto> txEventLogDtos = txEventLogMapper.selectTxsByPage(start, pageSize);

        List<TxEventLogTxTypeDto> result = null;
        if (txEventLogDtos != null) {
            result = txEventLogDtos.stream().map(this::convertTxEventLog).collect(Collectors.toList());
        }

        CurrentDto currentDto = currentMapper.selectSummaryInfo();

        PageResponseBean pageResponseBean = new PageResponseBean(result, currentDto.getTxCount());

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), pageResponseBean);
    }

    @Override
    public ResponseBean queryLatestNonontidTxs(int count) {

        List<TxEventLogDto> txEventLogDtos = txEventLogMapper.selectNonontidTxsByPage(0, count);
        List<TxEventLogTxTypeDto> result = null;
        if (txEventLogDtos != null) {
            result = txEventLogDtos.stream().map(this::convertTxEventLog).collect(Collectors.toList());
        }

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), result);
    }

    @Override
    public ResponseBean queryNonontidTxsByPage(int pageNumber, int pageSize) {

        int start = pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1);

        List<TxEventLogDto> txEventLogDtos = txEventLogMapper.selectNonontidTxsByPage(start, pageSize);
        List<TxEventLogTxTypeDto> result = null;
        if (txEventLogDtos != null) {
            result = txEventLogDtos.stream().map(this::convertTxEventLog).collect(Collectors.toList());
        }

        CurrentDto currentDto = currentMapper.selectSummaryInfo();

        PageResponseBean pageResponseBean = new PageResponseBean(result, currentDto.getTxCount() - currentDto.getOntidTxCount());

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), pageResponseBean);
    }


    // 传入tx_hash query tx_detail
    @Override
    public ResponseBean queryTxDetailByHash(String txHash) {
        TxEventLog txEventLog = txEventLogMapper.selectTxLogByHash(txHash);
        if (Helper.isEmptyOrNull(txEventLog)) {
            return new ResponseBean(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), false);
        }
        Map<String, Object> txTypeMap = determineTxType(txEventLog);
        String calledContractHash = (String) txTypeMap.get("calledContractHash");
        TxDetailDto txDetailDtoTemp = queryTXDetail(txEventLog, calledContractHash);
        TxDetailDto txDetailDto = TxDetailDto.builder().build();

        List<TxDetailDto> txDetailDtosMany = txDetailMapper.selectTxsByHash(txHash);
        TxDetailDto txDetailDto4Event = TxDetailDto.builder().build();

        for (int i = 0; i < txDetailDtosMany.size(); i++) {
            if (0 != txDetailDtosMany.get(i).getEventType()) {
                txDetailDto4Event.setEventType(txDetailDtosMany.get(i).getEventType());
                txDetailDto = txDetailDtosMany.get(i);
                break;
            }
            txDetailDto4Event.setEventType(txDetailDtosMany.get(i).getEventType());
            txDetailDto = txDetailDtosMany.get(i);
        }

        JSONObject detailObj = new JSONObject();
        int eventType = txDetailDto4Event.getEventType();

        //transfer or authentication tx，get transfer detail
        if (EventTypeEnum.Transfer.getType() == eventType || EventTypeEnum.Auth.getType() == eventType || EventTypeEnum.Approval.getType() == eventType) {

            List<TxDetailDto> txDetailDtos = txDetailMapper.selectTransferTxDetailByHash(txHash);
            detailObj.put("transfers", txDetailDtos);
        } else if (EventTypeEnum.Ontid.getType() == eventType) {
            //ONTID交易获取ONTID动作详情
            OntidTxDetailDto ontidTxDetail = OntidTxDetailDto.builder()
                    .txHash(txHash)
                    .build();
            List<OntidTxDetailDto> ontidTxDetailDtos = ontidTxDetailMapper.select(ontidTxDetail);
            //一笔交易注册多个ONTID
            StringBuilder stringBuilder = new StringBuilder();
            for (OntidTxDetailDto ontidTxDetailDto : ontidTxDetailDtos) {
                stringBuilder.append(ontidTxDetailDto.getOntid());
                stringBuilder.append("||");
            }
            String ontIdDes = Helper.templateOntIdOperation(ontidTxDetailDtos.get(0).getDescription());

            detailObj.put("ontid", stringBuilder.substring(0, stringBuilder.length() - 2).toString());
            detailObj.put("description", ontIdDes);
        }

        txDetailDto.setDetail(detailObj);
        txDetailDto.setFromAddress(txDetailDtoTemp.getFromAddress());
        txDetailDto.setToAddress(txDetailDtoTemp.getToAddress());
        txDetailDto.setIsContractHash(txDetailDtoTemp.getIsContractHash());
        txDetailDto.setTagName(txDetailDtoTemp.getTagName());
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), txDetailDto);
    }

    @Override
    public ResponseBean queryInputDataByHash(String txHash) throws IOException {
        String txPayload;
        if (txHash.startsWith(ConstantParam.EVM_ADDRESS_PREFIX)) {
            txPayload = web3jSdkUtil.queryPayloadByTxHash(txHash);
        } else {
            initSDK();
            txPayload = sdk.getTxPayload(txHash);
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), txPayload);
    }


    private TxDetailDto queryTxDetailByTxHash(String txHash) {
        TxDetailDto txDetailDto = txDetailMapper.selectTxByHash(txHash);
        JSONObject detailObj = new JSONObject();
        List<TxDetailDto> txDetailDtos = txDetailMapper.selectTransferTxDetailByHash(txHash);

        detailObj.put("transfers", txDetailDtos);
        txDetailDto.setDetail(detailObj);
        return txDetailDto;
    }

    private List<TxDetailDto> queryTxDetailByTxHash2(String txHash) {
        // 没有approval 类型的
        List<TxDetailDto> txDetailDtos = txDetailMapper.selectTransferTxDetailByHash(txHash);
        return txDetailDtos;
    }


    private List<TxDetailDto> queryTxDetailByTxHash3(String txHash) {
        // 没有approval 类型的
        List<TxDetailDto> txDetailDtos = txDetailMapper.selectTransferTxDetailByHash2(txHash);
        return txDetailDtos;
    }


    private List<TxDetailDto> queryAllTxDetailByTxHash(String txHash) {
        List<TxDetailDto> txDetailDtos = txDetailMapper.selectTransferTxDetailByHash(txHash);
        return txDetailDtos;
    }

    private TxDetailDto queryTXDetail(TxEventLog eventLog, String contractHash) {
        String name = ConstantParam.CONTRACT_TAG.get(contractHash.toLowerCase());
        String calledContractHash = eventLog.getCalledContractHash();
        String payer = getTransactionPayerByEventLog(eventLog);
        if (StringUtils.isEmpty(payer)) {
            payer = txDetailMapper.selectPayerByHash(eventLog.getTxHash());
        }
        String fromAddress = "";
        String toAddress = "";
        boolean isContractHash;
        TxDetailDto txDetailDto = TxDetailDto.builder().build();
        //208 ont deploy  0x evm deploy contract
        if ("".equals(calledContractHash) && eventLog.getTxType() == 208 || (ConstantParam.EVM_ADDRESS_PREFIX.equals(calledContractHash) && eventLog.getTxType() == 211)) {
            fromAddress = payer;
            isContractHash = false;
        } else if (ConstantParam.CONTRACTHASH_ONG.equals(calledContractHash) && eventLog.getTxType() == 211) {
            // evm ONG转账
            List<TxDetailDto> txDetailDtos = queryTxDetailByTxHash3(eventLog.getTxHash());
            if (txDetailDtos.size() != 0) {
                TxDetailDto txDetail = txDetailDtos.get(0);
                fromAddress = txDetail.getFromAddress();
                toAddress = txDetail.getToAddress();
            }
            isContractHash = false;
        } else if (ConstantParam.NATIVE_CONTRACT_HASH.equals(calledContractHash)) {
            fromAddress = payer;
            toAddress = contractHash;
            isContractHash = true;
        } else {
            fromAddress = payer;
            toAddress = contractHash;
            isContractHash = true;
        }

        txDetailDto = TxDetailDto.builder().fromAddress(fromAddress).toAddress(toAddress).isContractHash(isContractHash).tagName(name).build();
        return txDetailDto;
    }

    private String getTransactionPayerByEventLog(TxEventLog eventLog) {
        String payer = null;
        try {
            String eventStr = eventLog.getEventLog();
            JSONObject event = JSONObject.parseObject(eventStr);
            JSONArray notify = event.getJSONArray("Notify");
            if (!CollectionUtils.isEmpty(notify)) {
                int size = notify.size();
                JSONObject lastNotify = notify.getJSONObject(size - 1);
                String contractAddress = lastNotify.getString("ContractAddress");
                if (ConstantParam.CONTRACTHASH_ONG.equals(contractAddress)) {
                    Object states = lastNotify.get("States");
                    if (states instanceof String && ((String) states).startsWith(ConstantParam.EVM_ADDRESS_PREFIX)) {
                        payer = ConstantParam.EVM_ADDRESS_PREFIX + ((String) states).substring(138, 178);
                    } else if (states instanceof JSONArray) {
                        JSONArray statesArray = (JSONArray) states;
                        payer = statesArray.getString(1);
                    }
                }
            }
        } catch (Exception e) {
            log.error("getTransactionPayerByEventLog error", e);
        }
        return payer;
    }

    // 查询到所有的tx_datail 表
    private TxEventLogTxTypeDto convertTxEventLog(TxEventLog eventLog) {
        Map<String, Object> txTypeMap = determineTxType(eventLog);
        TxTypeEnum txTypeEnum = (TxTypeEnum) txTypeMap.get("txType");
        String calledContractHash = (String) txTypeMap.get("calledContractHash");

        TxEventLogTxTypeDto dto = new TxEventLogTxTypeDto();
        dto.setTxHash(eventLog.getTxHash());
        dto.setTxTime(eventLog.getTxTime());
        dto.setBlockHeight(eventLog.getBlockHeight());
        dto.setBlockIndex(eventLog.getBlockIndex());
        dto.setConfirmFlag(eventLog.getConfirmFlag());
        dto.setFee(eventLog.getFee());
        dto.setTxType(txTypeEnum);
        dto.setVmType(determineVMType(eventLog));
        // 查询到,和code一起进行逻辑判断
        TxDetailDto txDetailDto = queryTXDetail(eventLog, calledContractHash);
        JSONObject detail = new JSONObject();
        detail.put("transfers", txDetailDto);
        dto.setDetail(detail);
        return dto;
    }


    private VmTypeEnum determineVMType(TxEventLog eventLog) {
        VmTypeEnum vmType = null;
        if (eventLog.getTxType() == 208 || ConstantParam.EVM_ADDRESS_PREFIX.equals(eventLog.getCalledContractHash())) {
            vmType = VmTypeEnum.DEPLOYMENT_CODE;
        } else if (eventLog.getTxType().equals(209)) {
            vmType = VmTypeEnum.INVOKE_NEOVM;
        } else if (eventLog.getTxType().equals(210)) {
            vmType = VmTypeEnum.INVOKE_WASMVM;
        } else if (eventLog.getTxType().equals(211)) {
            vmType = VmTypeEnum.INVOKE_EVM;
        }
        return vmType;
    }


    private Map<String, Object> determineTxType(TxEventLog eventLog) {
        Map<String, Object> map = new HashMap<>();
        TxTypeEnum txType = null;
        String calledContractHash = eventLog.getCalledContractHash();
        // EVM depolyment
        if (eventLog.getTxType() == 208 || ConstantParam.EVM_ADDRESS_PREFIX.equals(calledContractHash)) {
            txType = TxTypeEnum.CONTRACT_DEPLOYMENT;
            calledContractHash = "";
        } else {
            if (NATIVE_CALLED_CONTRACT_HASH.equals(calledContractHash)) {
                if (eventLog.getOntidTxFlag()) {
                    txType = TxTypeEnum.ONT_ID;
                    calledContractHash = ConstantParam.CONTRACTHASH_ONTID;
                } else if (eventLog.getEventLog().contains(ConstantParam.CONTRACTHASH_ONT)) {
                    txType = TxTypeEnum.ONT_TRANSFER;
                    calledContractHash = ConstantParam.CONTRACTHASH_ONT;
                } else if (eventLog.getEventLog().contains(ConstantParam.CONTRACTHASH_ONG)) {
                    txType = TxTypeEnum.ONG_TRANSFER;
                    calledContractHash = ConstantParam.CONTRACTHASH_ONG;
                } else {
                    txType = TxTypeEnum.CONTRACT_CALL;
                }
            } else {
                ContractType contractType = contractTypes.get(calledContractHash);
                if (contractType.isOep4()) {
                    txType = TxTypeEnum.OEP4;
                } else if (contractType.isOep5()) {
                    txType = TxTypeEnum.OEP5;
                } else if (contractType.isOep8()) {
                    txType = TxTypeEnum.OEP8;
                } else if (contractType.isOrc20()) {
                    txType = TxTypeEnum.ORC20;
                } else if (contractType.isOrc721()) {
                    txType = TxTypeEnum.ORC721;
                } else if (calledContractHash.equals(ConstantParam.CONTRACTHASH_ONG)) {
                    txType = TxTypeEnum.ONG_TRANSFER;
                } else {
                    txType = TxTypeEnum.CONTRACT_CALL;
                }
            }
        }
        map.put("txType", txType);
        map.put("calledContractHash", calledContractHash);
        return map;
    }

    @Autowired
    public void setContractMapper(ContractMapper contractMapper) {
        this.contractTypes = Caffeine.newBuilder()
                .maximumSize(4096)
                .expireAfterWrite(Duration.ofHours(1))
                .build(contractHash -> {
                    ContractType contractType = contractMapper.findContractType(contractHash);
                    return contractType == null ? ContractType.NULL : contractType;
                });
    }

}
