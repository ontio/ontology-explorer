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
import com.github.ontio.common.Address;
import com.github.ontio.config.ParamsConfig;
import com.github.ontio.mapper.*;
import com.github.ontio.model.common.*;
import com.github.ontio.model.dao.DecodeInputAbi;
import com.github.ontio.model.dao.TxEventLog;
import com.github.ontio.model.dto.*;
import com.github.ontio.service.ITransactionService;
import com.github.ontio.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.web3j.protocol.core.methods.response.Transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
    private LoadingCache<String, Map<String, List<AbiParameters>>> decodeInputAbi;
    private final DecodeInputAbiMapper decodeInputAbiMapper;

    @Autowired
    public TransactionServiceImpl(TxDetailMapper txDetailMapper, CurrentMapper currentMapper, OntidTxDetailMapper ontidTxDetailMapper,
                                  TxEventLogMapper txEventLogMapper, ContractMapper contractMapper, Web3jSdkUtil web3jSdkUtil,
                                  ParamsConfig paramsConfig, ContractTagMapper contractTagMapper, DecodeInputAbiMapper decodeInputAbiMapper) {
        this.txDetailMapper = txDetailMapper;
        this.currentMapper = currentMapper;
        this.ontidTxDetailMapper = ontidTxDetailMapper;
        this.txEventLogMapper = txEventLogMapper;
        this.contractMapper = contractMapper;
        this.web3jSdkUtil = web3jSdkUtil;
        this.paramsConfig = paramsConfig;
        this.contractTagMapper = contractTagMapper;
        this.decodeInputAbiMapper = decodeInputAbiMapper;
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
            initSDK();
            boolean inMemPool = sdk.checkTxInMemPool(txHash);
            if (inMemPool) {
                Map<String, Object> tempResult = new HashMap<>();
                tempResult.put("confirm_flag", ConfirmFlagEnum.PENDING.ordinal());
                return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), tempResult);
            } else {
                return new ResponseBean(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), false);
            }
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
            txPayload = web3jSdkUtil.queryPayloadByTxHash(txHash).getInput();
        } else {
            initSDK();
            txPayload = sdk.getTxPayload(txHash);
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), txPayload);
    }

    @Override
    public ResponseBean queryInputDataAndDecode(String txHash) {
        InputDataView inputDataView = null;
        String inputData = "";
        int txType = 0;
        try {
            if (txHash.startsWith(ConstantParam.EVM_ADDRESS_PREFIX)) {
                Transaction transaction = web3jSdkUtil.queryPayloadByTxHash(txHash);
                inputData = transaction.getInput();
                String to = transaction.getTo();
                String abi = null;
                ContractDto contractDto = contractMapper.selectByPrimaryKey(to);
                if (contractDto != null) {
                    abi = contractDto.getAbi();
                }
                inputDataView = new InputDataView();
                inputDataView.setOriginalView(inputData);
                inputDataView.setAbi(abi);
            } else {
                String resp = HttpClientUtil.getRequest(String.format(ConstantParam.GET_TRANSACTION_URL, paramsConfig.MASTERNODE_RESTFUL_URL, txHash), Collections.emptyMap(), Collections.emptyMap());
                JSONObject jsonObject = JSONObject.parseObject(resp);
                Integer error = jsonObject.getInteger("Error");
                if (error == 0) {
                    JSONObject result = jsonObject.getJSONObject("Result");
                    JSONObject payload = result.getJSONObject("Payload");
                    txType = result.getInteger("TxType");
                    inputData = payload.getString("Code");
                }
                inputDataView = decodeInputData(txType, inputData);
                if (inputDataView == null) {
                    inputDataView = new InputDataView();
                }
                inputDataView.setOriginalView(inputData);
            }
        } catch (Exception e) {
            log.error("getTxPayload error...", e);
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), inputDataView);
    }

    @Override
    public ResponseBean decodeInputData(String inputData) {
        InputDataView inputDataView = decodeInputData(209, inputData);
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), inputDataView);
    }

    public InputDataView decodeInputData(int txType, String inputData) {
        InputDataView inputDataView = null;
        if (txType == 209) {
            int nativeInvokeIndex = inputData.lastIndexOf(ConstantParam.NATIVE_INPUT_DATA_END);
            if (nativeInvokeIndex != -1) {
                // native
                inputDataView = decodeNativeInputData(inputData, nativeInvokeIndex);
            } else {
                // neoVm
                inputDataView = decodeNeoVmInputData(inputData);
            }
        } else if (txType == 210) {
            // wasmVm
        }
        return inputDataView;
    }

    private InputDataView decodeNeoVmInputData(String inputData) {
        StringBuilder function = new StringBuilder();
        function.append(ConstantParam.FUNCTION_PARAM_START);
        try {
            int contractIndex = inputData.length() - 40;
//            String contract = inputData.substring(contractIndex);
            String searchMethod = inputData.substring(0, contractIndex - 2);
            int opPackIndex = searchMethod.lastIndexOf(ConstantParam.OP_PACK);
            String args = searchMethod.substring(0, opPackIndex);
            String methodHex = searchMethod.substring(opPackIndex + 2 + 2);
            String method = new String(com.github.ontio.common.Helper.hexToBytes(methodHex));
            Map<String, List<AbiParameters>> oep4Abi = decodeInputAbi.get(ConstantParam.VM_CATEGORY_NEOVM);
            if (oep4Abi == null) {
                return null;
            }
            List<AbiParameters> abiParameters = oep4Abi.get(method);
            if (abiParameters == null) {
                return null;
            }
            List<String> params = new ArrayList<>();
            List<InputDataDecode> decodes = new ArrayList<>();
            for (int i = abiParameters.size() - 1; i >= 0; i--) {
                InputDataDecode inputDataDecode = new InputDataDecode();
                AbiParameters parameters = abiParameters.get(i);
                String type = parameters.getType();
                String name = parameters.getName();
                function.append(type).append(ConstantParam.BLANK).append(name).append(ConstantParam.FUNCTION_PARAM_SPLIT);
                Map<String, Object> map = decodeDataByType(type, args, false);
                String rawData = (String) map.get("rawData");
                String removeVarBytes = args.substring(2);
                int index = removeVarBytes.indexOf(rawData);
                args = removeVarBytes.substring(index + rawData.length());
                Object data = map.get("data");
                params.add(rawData);
                inputDataDecode.setName(name);
                inputDataDecode.setType(type);
                inputDataDecode.setData(Collections.singletonList(data));
                decodes.add(inputDataDecode);
            }
            Collections.reverse(decodes);
            int length = function.length();
            if (length > 1) {
                function.delete(length - 1, length);
            }
            function.append(ConstantParam.FUNCTION_PARAM_END);
            function.insert(0, method);
            DefaultView defaultView = new DefaultView();
            defaultView.setFunction(function.toString());
            defaultView.setMethodId(methodHex);
            defaultView.setParams(params);
            InputDataView inputDataView = new InputDataView();
            inputDataView.setDefaultView(defaultView);
            inputDataView.setDecode(decodes);
            return inputDataView;
        } catch (Exception e) {
            log.error("decodeNeoVmInputData error", e);
        }
        return null;
    }

    private InputDataView decodeNativeInputData(String inputData, int nativeInvokeIndex) {
        try {
            long size = 0;
            String methodHex;
            String method;
            String contractHash;
            String[] args = null;
            if (inputData.startsWith(ConstantParam.NATIVE_STRUCT_START)) {
                // 有参数
                String argsMethodContract = inputData.substring(6, nativeInvokeIndex);
                int length = argsMethodContract.length();
                String contract = argsMethodContract.substring(length - 40);
                contractHash = com.github.ontio.common.Helper.reverse(contract);
                String argsMethod = argsMethodContract.substring(0, length - 42);
                args = argsMethod.split(ConstantParam.NATIVE_ARGS_OP_CODE);
                String opSizeMethod = args[args.length - 1];

                String opMethod = opSizeMethod.substring(2);
                int opPackIndex = opMethod.lastIndexOf(ConstantParam.OP_PACK);
                if (opPackIndex != -1) {
                    // 参数为list
                    methodHex = opMethod.substring(opPackIndex + 2 + 2);
                    method = new String(com.github.ontio.common.Helper.hexToBytes(methodHex));
                    size = com.github.ontio.util.Helper.parseInputDataNumber(opMethod.substring(0, opPackIndex), true).longValue();
                } else {
                    // 考虑到方法名不会大于255个字节
                    methodHex = opMethod.substring(2);
                    method = new String(com.github.ontio.common.Helper.hexToBytes(methodHex));
                }
            } else {
                // native方法无参数
                String methodContract = inputData.substring(0, nativeInvokeIndex);
                int length = methodContract.length();
                String contract = methodContract.substring(length - 40);
                contractHash = com.github.ontio.common.Helper.reverse(contract);
                methodHex = methodContract.substring(2, length - 42);
                method = new String(com.github.ontio.common.Helper.hexToBytes(methodHex));
            }
            Map<String, List<AbiParameters>> methodParameters = decodeInputAbi.get(contractHash);
            if (methodParameters == null) {
                // 没有该合约abi
                return null;
            }
            List<AbiParameters> abiParameters = methodParameters.get(method);
            if (abiParameters == null) {
                // 没有该方法
                return null;
            }
            // 遍历abiParameters,在args中取参数
            return decodeInputDataParameter(methodHex, method, abiParameters, args, size);
        } catch (Exception e) {
            log.error("decodeNativeInputData error", e);
        }
        return null;
    }

    private InputDataView decodeInputDataParameter(String methodId, String method, List<AbiParameters> abiParameters, String[] args, long size) throws IOException {
        List<String> params = new ArrayList<>();
        List<InputDataDecode> decodes = Collections.emptyList();
        StringBuilder function = new StringBuilder();
        function.append(ConstantParam.FUNCTION_PARAM_START);
        int argIndex = 0;
        int lastLength = 0;
        if (!CollectionUtils.isEmpty(abiParameters)) {
            decodes = new ArrayList<>();
            for (AbiParameters parameter : abiParameters) {
                String type = parameter.getType();
                String name = parameter.getName();
                if (ConstantParam.NATIVE_TYPE_STRUCT.equals(type)) {
                    List<AbiParameters> subType = parameter.getSubType();
                    for (AbiParameters subParameter : subType) {
                        // for make function
                        String sType = subParameter.getType();
                        String sName = subParameter.getName();
                        function.append(sType).append(ConstantParam.BLANK).append(sName).append(ConstantParam.FUNCTION_PARAM_SPLIT);
                    }
                    for (int j = 0; j < size; j++) {
                        for (AbiParameters subParameter : subType) {
                            InputDataDecode inputDataDecode = new InputDataDecode();
                            String sType = subParameter.getType();
                            String arg = args[argIndex];
                            arg = arg.startsWith(ConstantParam.NATIVE_SECOND_STRUCT_START) ? arg.substring(8) : arg;
                            Map<String, Object> map = decodeDataByType(sType, arg, true);
                            String rawData = (String) map.get("rawData");
                            Object data = map.get("data");
                            params.add(rawData);
                            inputDataDecode.setName(subParameter.getName());
                            inputDataDecode.setType(sType);
                            inputDataDecode.setData(Collections.singletonList(data));
                            decodes.add(inputDataDecode);
                            argIndex++;
                        }
                    }
                } else if (type.endsWith(ConstantParam.TYPE_ARRAY_SUFFIX)) {
                    InputDataDecode inputDataDecode = new InputDataDecode();
                    List<Object> dataList = new ArrayList<>();
                    List<AbiParameters> subType = parameter.getSubType();
                    for (int j = 0; j < lastLength; j++) {
                        for (AbiParameters subParameter : subType) {
                            String sType = subParameter.getType();
                            String arg = args[argIndex];
                            Map<String, Object> map = decodeDataByType(sType, arg, true);
                            String rawData = (String) map.get("rawData");
                            Object data = map.get("data");
                            params.add(rawData);
                            dataList.add(data);
                            argIndex++;
                        }
                    }
                    function.append(type).append(ConstantParam.BLANK).append(name).append(ConstantParam.FUNCTION_PARAM_SPLIT);
                    inputDataDecode.setName(name);
                    inputDataDecode.setType(type);
                    inputDataDecode.setData(dataList);
                    decodes.add(inputDataDecode);
                } else {
                    InputDataDecode inputDataDecode = new InputDataDecode();
                    String arg = args[argIndex];
                    Map<String, Object> map = decodeDataByType(type, arg, true);
                    String rawData = (String) map.get("rawData");
                    Object data = map.get("data");
                    params.add(rawData);
                    if (data instanceof BigInteger) {
                        lastLength = ((BigInteger) data).intValue();
                    }
                    function.append(type).append(ConstantParam.BLANK).append(name).append(ConstantParam.FUNCTION_PARAM_SPLIT);
                    inputDataDecode.setName(name);
                    inputDataDecode.setType(type);
                    inputDataDecode.setData(Collections.singletonList(data));
                    decodes.add(inputDataDecode);
                    argIndex++;
                }
            }

        }
        int length = function.length();
        if (length > 1) {
            function.delete(length - 1, length);
        }
        function.append(ConstantParam.FUNCTION_PARAM_END);
        function.insert(0, method);
        DefaultView defaultView = new DefaultView();
        defaultView.setFunction(function.toString());
        defaultView.setMethodId(methodId);
        defaultView.setParams(params);
        InputDataView inputDataView = new InputDataView();
        inputDataView.setDefaultView(defaultView);
        inputDataView.setDecode(decodes);
        return inputDataView;
    }

    private Map<String, Object> decodeDataByType(String type, String arg, boolean isNative) throws IOException {
        Map<String, Object> map = new HashMap<>();
        Object data = null;
        String rawData = null;
        switch (type) {
            case "Address":
                Address address = Helper.parseInputDataAddress(arg);
                data = address.toBase58();
                rawData = com.github.ontio.common.Helper.toHexString(address.toArray());
                break;
            case "Long":
            case "BigInt":
            case "Int":
                data = Helper.parseInputDataNumber(arg, isNative);
                BigInteger number = (BigInteger) data;
                if (isNative) {
                    BigInteger rawNumber;
                    int value = number.intValue();
                    if (value > 0 && value <= 16) {
                        rawNumber = number.add(BigInteger.valueOf(80));
                    } else if (value == 0) {
                        rawNumber = BigInteger.ZERO;
                    } else if (value == -1) {
                        rawNumber = BigInteger.valueOf(79);
                    } else {
                        rawNumber = number;
                    }
                    if (BigInteger.ZERO.compareTo(rawNumber) == 0) {
                        rawData = "00";
                    } else {
                        byte[] bytes = com.github.ontio.common.Helper.BigIntToNeoBytes(rawNumber);
                        rawData = com.github.ontio.common.Helper.toHexString(bytes);
                    }
                } else {
                    byte[] bytes = com.github.ontio.common.Helper.BigIntToNeoBytes(number);
                    rawData = com.github.ontio.common.Helper.toHexString(bytes);
                }
                break;
            case "ByteArray":
                data = Helper.parseInputDataBytes(arg);
                rawData = (String) data;
                break;
            case "String":
                data = Helper.parseInputDataString(arg);
                rawData = com.github.ontio.common.Helper.toHexString(((String) data).getBytes());
                break;
            default:
                break;
        }
        map.put("rawData", rawData);
        map.put("data", data);
        return map;
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
            name = null;
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
                    calledContractHash = ConstantParam.CONTRACTHASH_ONG;
                    txType = TxTypeEnum.CONTRACT_CALL;
                }
            } else {
                ContractType contractType = contractTypes.get(calledContractHash);
                if (contractType == null) {
                    txType = TxTypeEnum.CONTRACT_CALL;
                } else {
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
                    } else if (contractType.isOrc1155()) {
                        txType = TxTypeEnum.ORC1155;
                    } else if (calledContractHash.equals(ConstantParam.CONTRACTHASH_ONG)) {
                        txType = TxTypeEnum.ONG_TRANSFER;
                    } else {
                        txType = TxTypeEnum.CONTRACT_CALL;
                    }
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
                .expireAfterWrite(Duration.ofDays(1))
                .build(contractHash -> {
                    ContractType contractType = contractMapper.findContractType(contractHash);
                    return contractType == null ? ContractType.NULL : contractType;
                });
    }

    @Autowired
    public void setDecodeInputAbi() {
        this.decodeInputAbi = Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.DAYS).build(contractHash -> {
            DecodeInputAbi decodeInputAbi = decodeInputAbiMapper.selectByPrimaryKey(contractHash);
            if (decodeInputAbi != null) {
                Map<String, List<AbiParameters>> map = new HashMap<>();
                try {
                    String abi = decodeInputAbi.getAbi();
                    JSONObject jsonObject = JSONObject.parseObject(abi);
                    JSONArray functions = jsonObject.getJSONArray("functions");
                    if (!CollectionUtils.isEmpty(functions)) {
                        for (Object obj : functions) {
                            JSONObject function = (JSONObject) obj;
                            String name = function.getString("name");
                            JSONArray parameters = function.getJSONArray("parameters");
                            if (CollectionUtils.isEmpty(parameters)) {
                                map.put(name, Collections.emptyList());
                            } else {
                                List<AbiParameters> abiParameters = JSONArray.parseArray(parameters.toString(), AbiParameters.class);
                                map.put(name, abiParameters);
                            }
                        }
                    }
                    return map;
                } catch (Exception e) {
                    log.error("parse contract abi error:{},{}", contractHash, e.getMessage());
                }
            }
            return null;
        });
    }
}
