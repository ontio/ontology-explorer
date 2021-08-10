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
import com.github.ontio.mapper.ContractMapper;
import com.github.ontio.mapper.CurrentMapper;
import com.github.ontio.mapper.OntidTxDetailMapper;
import com.github.ontio.mapper.TxDetailMapper;
import com.github.ontio.mapper.TxEventLogMapper;
import com.github.ontio.model.common.*;
import com.github.ontio.model.dao.TxEventLog;
import com.github.ontio.model.dto.CurrentDto;
import com.github.ontio.model.dto.OntidTxDetailDto;
import com.github.ontio.model.dto.TxDetailDto;
import com.github.ontio.model.dto.TxEventLogDto;
import com.github.ontio.model.dto.TxEventLogTxTypeDto;
import com.github.ontio.network.exception.ConnectorException;
import com.github.ontio.service.IContractService;
import com.github.ontio.service.ITransactionService;
import com.github.ontio.util.ConstantParam;
import com.github.ontio.util.ErrorInfo;
import com.github.ontio.util.Helper;
import com.github.ontio.util.Web3jSdkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
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

    @Autowired
    public TransactionServiceImpl(TxDetailMapper txDetailMapper, CurrentMapper currentMapper,
                                  OntidTxDetailMapper ontidTxDetailMapper, TxEventLogMapper txEventLogMapper, ContractMapper contractMapper, Web3jSdkUtil web3jSdkUtil) {
        this.txDetailMapper = txDetailMapper;
        this.currentMapper = currentMapper;
        this.ontidTxDetailMapper = ontidTxDetailMapper;
        this.txEventLogMapper = txEventLogMapper;
        this.contractMapper = contractMapper;
        this.web3jSdkUtil = web3jSdkUtil;
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


    // 传入tx_hash  query tx_detail
    @Override
    public ResponseBean queryTxDetailByHash(String txHash) {

        TxDetailDto txDetailDto = txDetailMapper.selectTxByHash(txHash);
        if (Helper.isEmptyOrNull(txDetailDto)) {
            return new ResponseBean(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), false);
        }

        JSONObject detailObj = new JSONObject();
        int eventType = txDetailDto.getEventType();
        //transfer or authentication tx，get transfer detail
        if (EventTypeEnum.Transfer.getType() == eventType || EventTypeEnum.Auth.getType() == eventType || EventTypeEnum.Approval.getType() == eventType) {
            String txHashReverse = "";
            if (txHash.startsWith("0x")) {
                txHashReverse = com.github.ontio.common.Helper.reverse(txHash.substring(2));
            } else {
                txHashReverse = "0x" + com.github.ontio.common.Helper.reverse(txHash);
            }
            List<TxDetailDto> txDetailDtos = txDetailMapper.selectTransferTxDetailByHash(txHash, txHashReverse);
            txDetailDtos.forEach(item -> {
                //ONG转换好精度给前端
                String assetName = item.getAssetName();
                if (ConstantParam.ONG.equals(assetName) && !item.getFromAddress().startsWith("0x")) {
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

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), txDetailDto);
    }

    @Override
    public ResponseBean queryInputDataByHash(String txHash) throws IOException, ConnectorException {
        String txPayload = "";
        if (txHash.startsWith("0x")) {
            txPayload = web3jSdkUtil.queryPayloadByTxHash(txHash);
        } else {
            OntSdk ontSdk = OntSdk.getInstance();
            String ont_rpcUrl = "http://172.168.3.73:20336";
            ontSdk.setRpc(ont_rpcUrl);
            Object txJson = ontSdk.getConnect().getTransactionJson(txHash);
            JSONObject object = JSONObject.parseObject(JSON.toJSONString(txJson));
            txPayload = object.getJSONObject("Payload").getString("Code");
        }

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), txPayload);
    }


    private TxDetailDto queryTxDetailByTxHash(String txHash) {
        TxDetailDto txDetailDto = txDetailMapper.selectTxByHash(txHash);
        JSONObject detailObj = new JSONObject();
        String txHashReverse = "";
        if (txHash.startsWith("0x")) {
            txHashReverse = com.github.ontio.common.Helper.reverse(txHash.substring(2));
        } else {
            txHashReverse = "0x" + com.github.ontio.common.Helper.reverse(txHash);
        }

        List<TxDetailDto> txDetailDtos = txDetailMapper.selectTransferTxDetailByHash(txHash, txHashReverse);

        txDetailDtos.forEach(
                item -> {
                    String toAddress = item.getToAddress();
                    int toAddrLen = toAddress.length();
                    item.setIs_contract_hash(false);
                    if (toAddrLen == 40 || toAddrLen == 42) {
                        Integer integer = contractMapper.selectIfHashExists(toAddress);
                        if (1 == integer) {
                            item.setIs_contract_hash(true);
                        }
                    }
                }
        );
        txDetailDtos.forEach(item -> {
            //ONG转换好精度给前端
            String assetName = item.getAssetName();
            if (ConstantParam.ONG.equals(assetName) && !item.getFromAddress().startsWith("0x")) {
                item.setAmount(item.getAmount().divide(ConstantParam.ONG_TOTAL));
            }
        });
        detailObj.put("transfers", txDetailDtos);
        txDetailDto.setDetail(detailObj);
        return txDetailDto;
    }


    private TxEventLogTxTypeDto convertTxEventLog(TxEventLog eventLog) {
        TxEventLogTxTypeDto dto = new TxEventLogTxTypeDto();
        dto.setTxHash(eventLog.getTxHash());
        dto.setTxTime(eventLog.getTxTime());
        dto.setBlockHeight(eventLog.getBlockHeight());
        dto.setBlockIndex(eventLog.getBlockIndex());
        dto.setConfirmFlag(eventLog.getConfirmFlag());
        dto.setFee(eventLog.getFee());
        // 查询合约进行合约类型的转化
        dto.setTxType(determineTxType(eventLog));
        dto.setVmType(determineVMType(eventLog));
        JSONObject detail = queryTxDetailByTxHash(eventLog.getTxHash()).getDetail();
        dto.setDetail(detail);
        return dto;
    }


    private VmTypeEnum determineVMType(TxEventLog eventLog) {
        VmTypeEnum vmType = null;
        if (eventLog.getTxType() == 208 || "0x".equals(eventLog.getCalledContractHash())) {
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
        if (eventLog.getTxType() == 208 || "0x".equals(eventLog.getCalledContractHash())) {
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
                }
            } else {
                ContractType contractType = contractTypes.get(calledContractHash);
                if (contractType.isOep4()) {
                    txType = TxTypeEnum.OEP4;
                } else if (contractType.isOep5()) {
                    txType = TxTypeEnum.OEP5;
                } else if (contractType.isOep8()) {
                    txType = TxTypeEnum.OEP8;
                } else if (contractType.isErc20()) {
                    txType = TxTypeEnum.ERC20;
                } else {
                    txType = TxTypeEnum.CONTRACT_CALL;
                }
            }
            // todo 进行新的合约类型的判断 else  211类型
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
