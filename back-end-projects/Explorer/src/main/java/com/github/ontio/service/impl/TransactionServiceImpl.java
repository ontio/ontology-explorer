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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.ontio.OntSdk;
import com.github.ontio.config.ParamsConfig;
import com.github.ontio.mapper.*;
import com.github.ontio.model.common.*;
import com.github.ontio.model.dao.TxDetail;
import com.github.ontio.model.dao.TxEventLog;
import com.github.ontio.model.dto.CurrentDto;
import com.github.ontio.model.dto.OntidTxDetailDto;
import com.github.ontio.model.dto.TxDetailDto;
import com.github.ontio.model.dto.TxEventLogDto;
import com.github.ontio.model.dto.TxEventLogTxTypeDto;
import com.github.ontio.network.exception.ConnectorException;
import com.github.ontio.service.IContractService;
import com.github.ontio.service.ITransactionService;
import com.github.ontio.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Bool;

import java.io.IOException;
import java.time.Duration;
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
        String code = determineTxType(txEventLog).getCode();
        TxDetailDto txDetailDtoTemp = queryTXDetail(txEventLog, code);
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
            txDetailDtos.forEach(item -> {
                //ONG转换好精度给前端
                String assetName = item.getAssetName();
                if (ConstantParam.ONG.equals(assetName)) {
                    item.setAmount(item.getAmount().divide(ConstantParam.ONG_TOTAL));
                }
            });
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
            Object txJson = sdk.getTxJson(txHash);
            JSONObject payload = JSONObject.parseObject(txJson.toString()).getJSONObject("Payload");
            txPayload = payload.getString("Code");
        }

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), txPayload);
    }


    private TxDetailDto queryTxDetailByTxHash(String txHash) {
        TxDetailDto txDetailDto = txDetailMapper.selectTxByHash(txHash);
        JSONObject detailObj = new JSONObject();
        List<TxDetailDto> txDetailDtos = txDetailMapper.selectTransferTxDetailByHash(txHash);

        txDetailDtos.forEach(item -> {
            //ONG转换好精度给前端
            String assetName = item.getAssetName();
            if (ConstantParam.ONG.equals(assetName) && !item.getAssetName().startsWith(ConstantParam.EVM_ADDRESS_PREFIX)) {
                item.setAmount(item.getAmount().divide(ConstantParam.ONG_TOTAL));
            }
        });
        detailObj.put("transfers", txDetailDtos);
        txDetailDto.setDetail(detailObj);
        return txDetailDto;
    }

    private List<TxDetailDto> queryTxDetailByTxHash2(String txHash) {
        // 没有approval 类型的
        List<TxDetailDto> txDetailDtos = txDetailMapper.selectTransferTxDetailByHash(txHash);
        txDetailDtos.forEach(item -> {
            //ONG转换好精度给前端
            String assetName = item.getAssetName();
            if (ConstantParam.ONG.equals(assetName) && !item.getAssetName().startsWith(ConstantParam.EVM_ADDRESS_PREFIX)) {
                item.setAmount(item.getAmount().divide(ConstantParam.ONG_TOTAL));
            }
        });
        return txDetailDtos;
    }


    private List<TxDetailDto> queryTxDetailByTxHash3(String txHash) {
        // 没有approval 类型的
        List<TxDetailDto> txDetailDtos = txDetailMapper.selectTransferTxDetailByHash2(txHash);
        txDetailDtos.forEach(item -> {
            //ONG转换好精度给前端
            String assetName = item.getAssetName();
            if (ConstantParam.ONG.equals(assetName) && !item.getAssetName().startsWith(ConstantParam.EVM_ADDRESS_PREFIX)) {
                item.setAmount(item.getAmount().divide(ConstantParam.ONG_TOTAL));
            }
        });
        return txDetailDtos;
    }


    private List<TxDetailDto> queryAllTxDetailByTxHash(String txHash) {
        List<TxDetailDto> txDetailDtos = txDetailMapper.selectTransferTxDetailByHash(txHash);

        txDetailDtos.forEach(item -> {
            //ONG转换好精度给前端
            String assetName = item.getAssetName();
            if (ConstantParam.ONG.equals(assetName) && !item.getAssetName().startsWith(ConstantParam.EVM_ADDRESS_PREFIX)) {
                item.setAmount(item.getAmount().divide(ConstantParam.ONG_TOTAL));
            }
        });
        return txDetailDtos;
    }

    private TxDetailDto queryTXDetail(TxEventLog eventLog, String code) {
        String calledContractHash = eventLog.getCalledContractHash();
        TxDetailDto txDetailDto = TxDetailDto.builder().build();
        //208 ont deploy  0x evm deploy contract
        if ("".equals(calledContractHash) && eventLog.getTxType() == 208 || ("0x".equals(calledContractHash) && eventLog.getTxType() == 211)) {
            // native ontology transfer
        } else if (ConstantParam.NATIVE_CONTRACT_HASH.equals(calledContractHash)) {
            // ontid的逻辑
            if (eventLog.getOntidTxFlag() && eventLog.getConfirmFlag() == 1) {
                List<TxDetailDto> txDetailDtos = queryTxDetailByTxHash3(eventLog.getTxHash());
                if (txDetailDtos.size() != 0) {
                    String fromAddress = txDetailDtos.get(0).getFromAddress();
                    String toAddress = txDetailDtos.get(0).getToAddress();
                    txDetailDto = TxDetailDto.builder().fromAddress(fromAddress).toAddress(toAddress).isContractHash(false).build();
                }
            } else if (code.equals("07") || code.equals("08")) {
                List<TxDetailDto> txDetailDtos = queryTxDetailByTxHash3(eventLog.getTxHash());
                String fromAddress = "";
                String toAddress = "";
                if (txDetailDtos.size() == 2) {
                    fromAddress = txDetailDtos.get(1).getFromAddress();
                    toAddress = txDetailDtos.get(1).getToAddress();
                } else if (txDetailDtos.size() == 1) {
                    fromAddress = txDetailDtos.get(0).getFromAddress();
                    toAddress = txDetailDtos.get(0).getToAddress();
                }
                txDetailDto = TxDetailDto.builder().fromAddress(fromAddress).toAddress(toAddress).isContractHash(false).build();
            } else {
                txDetailDto = TxDetailDto.builder().fromAddress("").toAddress("").isContractHash(false).build();
            }
        } else {
            List<TxDetailDto> txDetailDtos = queryAllTxDetailByTxHash(eventLog.getTxHash());
            int length = txDetailDtos.size();
            if (length == 1) {
                // 只有一笔ong手续费消耗/ 调用合约的
                String fromAddress = txDetailDtos.get(0).getFromAddress();
                String toAddress = eventLog.getCalledContractHash();
                TxDetailDto txDetailDto1 = TxDetailDto.builder().fromAddress(fromAddress).toAddress(toAddress).build();
                // only ong gasConsume
                if (eventLog.getCalledContractHash().equals(ConstantParam.CONTRACTHASH_ONG)) {
                    txDetailDto1.setIsContractHash(false);
                    txDetailDto1.setTagName(" Only ong consume ");
                } else {
                    txDetailDto1.setIsContractHash(true);
                    String name = "";
                    if (ConstantParam.CONTRACT_TAG.containsKey(toAddress)) {
                        name = ConstantParam.CONTRACT_TAG.get(toAddress);
                    }
                    txDetailDto1.setTagName(name);
                }
                txDetailDto = TxDetailDto.builder().fromAddress(txDetailDto1.getFromAddress())
                        .toAddress(txDetailDto1.getToAddress())
                        .isContractHash(txDetailDto1.getIsContractHash())
                        .tagName(txDetailDto1.getTagName())
                        .build();
                // 转账操作
            } else if (length == 2) {
                String fromAddress = txDetailDtos.get(0).getFromAddress();
                String toAddress = txDetailDtos.get(0).getToAddress();
                txDetailDto = TxDetailDto.builder().fromAddress(fromAddress).toAddress(toAddress).isContractHash(false).build();
            } else if (length == 0) {
                txDetailDto = TxDetailDto.builder().fromAddress("").toAddress("").isContractHash(false).build();
            } else {
                // token bridge
                TxDetailDto txDetailDto1 = TxDetailDto.builder().build();
                if (paramsConfig.TOKEN_BRIDGE_CONTRACT.equals(eventLog.getCalledContractHash())) {
                    List<TxDetailDto> txManyDetailDtos = queryTxDetailByTxHash2(eventLog.getTxHash());
                    txDetailDto1.setFromAddress(txManyDetailDtos.get(0).getFromAddress());
                    if (txManyDetailDtos.size() == 3) {
                        txDetailDto1.setToAddress(txManyDetailDtos.get(1).getToAddress());
                    } else if (txManyDetailDtos.size() == 4) {
                        txDetailDto1.setToAddress(txManyDetailDtos.get(2).getToAddress());
                    }
                } else {
                    String fromAddress = txDetailDtos.get(length - 1).getFromAddress();
                    String toAddress = eventLog.getCalledContractHash();
                    txDetailDto1.setFromAddress(fromAddress);
                    txDetailDto1.setToAddress(toAddress);
                    txDetailDto1.setIsContractHash(true);
                    String name = "";
                    if (ConstantParam.CONTRACT_TAG.containsKey(toAddress)) {
                        name = ConstantParam.CONTRACT_TAG.get(toAddress);
                    }
                    txDetailDto1.setTagName(name);
                }
                txDetailDto = TxDetailDto.builder().fromAddress(txDetailDto1.getFromAddress())
                        .toAddress(txDetailDto1.getToAddress())
                        .isContractHash(txDetailDto1.getIsContractHash())
                        .tagName(txDetailDto1.getTagName())
                        .build();
            }
        }
        return txDetailDto;
    }

    // 查询到所有的tx_datail 表
    private TxEventLogTxTypeDto convertTxEventLog(TxEventLog eventLog) {
        TxEventLogTxTypeDto dto = new TxEventLogTxTypeDto();
        dto.setTxHash(eventLog.getTxHash());
        dto.setTxTime(eventLog.getTxTime());
        dto.setBlockHeight(eventLog.getBlockHeight());
        dto.setBlockIndex(eventLog.getBlockIndex());
        dto.setConfirmFlag(eventLog.getConfirmFlag());
        dto.setFee(eventLog.getFee());
        TxTypeEnum txTypeEnum = determineTxType(eventLog);
        dto.setTxType(txTypeEnum);
        String code = txTypeEnum.getCode();
        dto.setVmType(determineVMType(eventLog));
        // 查询到,和code一起进行逻辑判断
        TxDetailDto txDetailDto = queryTXDetail(eventLog, code);
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


    private TxTypeEnum determineTxType(TxEventLog eventLog) {
        TxTypeEnum txType = null;
        // EVM depolyment
        if (eventLog.getTxType() == 208 || ConstantParam.EVM_ADDRESS_PREFIX.equals(eventLog.getCalledContractHash())) {
            txType = TxTypeEnum.CONTRACT_DEPLOYMENT;
        } else {
            String calledContractHash = eventLog.getCalledContractHash();
            if (NATIVE_CALLED_CONTRACT_HASH.equals(calledContractHash)) {
                if (eventLog.getOntidTxFlag()) {
                    txType = TxTypeEnum.ONT_ID;
                } else if (eventLog.getEventLog().contains("0100000000000000000000000000000000000000")) {
                    txType = TxTypeEnum.ONT_TRANSFER;
                } else if (eventLog.getEventLog().contains("0200000000000000000000000000000000000000")) {
                    txType = TxTypeEnum.ONG_TRANSFER;
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
                } else if (eventLog.getCalledContractHash().equals(ConstantParam.CONTRACTHASH_ONG)) {
                    txType = TxTypeEnum.ONG_TRANSFER;
                } else {
                    txType = TxTypeEnum.CONTRACT_CALL;
                }
            }
        }
        return txType;
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
