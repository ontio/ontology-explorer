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
import com.github.ontio.io.BinaryReader;
import com.github.ontio.mapper.ContractMapper;
import com.github.ontio.mapper.Oep5Mapper;
import com.github.ontio.mapper.Oep8Mapper;
import com.github.ontio.model.common.EventTypeEnum;
import com.github.ontio.model.common.OntIdEventDesEnum;
import com.github.ontio.model.common.TransactionTypeEnum;
import com.github.ontio.model.dao.*;
import com.github.ontio.network.exception.RestfulException;
import com.github.ontio.service.CommonService;
import com.github.ontio.smartcontract.neovm.abi.BuildParams;
import com.github.ontio.txPush.TransferTransactionPush;
import com.github.ontio.utils.ConstantParam;
import com.github.ontio.utils.Web3jSdkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.Utils;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionDecoder;
import org.web3j.utils.Numeric;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.Future;


/**
 * @author zhouq
 * @version 1.0
 * @date 2018/3/13
 */
@Slf4j
@Component
public class TxHandlerThread {

    private static ThreadLocal<Map<String, Boolean>> IS_OEPTX_FLAG = new ThreadLocal<>();

    private final ParamsConfig paramsConfig;

    private final CommonService commonService;

    private final ContractMapper contractMapper;

    private final Oep8Mapper oep8Mapper;

    private final Oep5Mapper oep5Mapper;

    private final TransferTransactionPush transferTransactionPush;

    private final Web3jSdkUtil web3jSdkUtil;

    @Autowired
    public TxHandlerThread(ParamsConfig paramsConfig, ContractMapper contractMapper, CommonService commonService, Oep8Mapper oep8Mapper, Oep5Mapper oep5Mapper, TransferTransactionPush transferTransactionPush, Web3jSdkUtil web3jSdkUtil) {
        this.paramsConfig = paramsConfig;
        this.contractMapper = contractMapper;
        this.commonService = commonService;
        this.oep8Mapper = oep8Mapper;
        this.oep5Mapper = oep5Mapper;
        this.transferTransactionPush = transferTransactionPush;
        this.web3jSdkUtil = web3jSdkUtil;
    }

    @Async
    public Future<String> asyncHandleTx(JSONObject txJson, int blockHeight, int blockTime, int indexInBlock) throws Exception {
        Map<String, Boolean> map = new HashMap<>();
        map.put(ConstantParam.IS_OEP4TX, false);
        map.put(ConstantParam.IS_OEP5TX, false);
        map.put(ConstantParam.IS_OEP8TX, false);
        map.put(ConstantParam.IS_ORC20TX, false);
        map.put(ConstantParam.IS_ORC721TX, false);
        map.put(ConstantParam.IS_ORC1155TX, false);
        IS_OEPTX_FLAG.set(map);

        try {
            String threadName = Thread.currentThread().getName();
            log.info("{} run--------blockHeight:{},txHash:{}", threadName, blockHeight, txJson.getString("Hash"));
            handleOneTx(txJson, blockHeight, blockTime, indexInBlock);
            log.info("{} end-------blockHeight:{},txHash:{}", threadName, blockHeight, txJson.getString("Hash"));
            return new AsyncResult<String>("success");
        } catch (Exception e) {
            log.error("asyncHandleTx error...", e);
            throw e;
        }

    }


    /**
     * 处理单笔交易
     *
     * @param txJson
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @throws Exception
     */
    public void handleOneTx(JSONObject txJson, int blockHeight, int blockTime, int indexInBlock) throws Exception {

        Boolean isOntidTx = false;
        Boolean insertInvokeDeploy = false;
        int txType = txJson.getInteger("TxType");
        String txHash = txJson.getString("Hash");
        String payer = txJson.getString("Payer");

        String code;
        if (TransactionTypeEnum.EVM_INVOKECODE.type() == txType) {
            code = txJson.getString("Payload");
            // wasm合约可以调用evm合约，所以对应的txHash需要判断
            txHash = ConstantParam.EVM_ADDRESS_PREFIX + Helper.reverse(txHash);
            payer = com.github.ontio.utils.Helper.ontAddrToEthAddr(payer);
        } else {
            code = txJson.getJSONObject("Payload").getString("Code");
        }

        // 交易类型的判断,解析交易类型,拿到交易中payload解析的calledContractHash 0x为部署合约
        String calledContractHash = parseCalledContractHash(code, txType, txHash);

        log.info("####txType:{}, txHash:{}, calledContractHash:{}", txType, txHash, calledContractHash);

        try {
            JSONObject eventLogObj = txJson.getJSONObject("EventLog");
            log.info("eventLog:{}", eventLogObj.toJSONString());
            //eventstate 1:success 0:failed
            int confirmFlag = eventLogObj.getInteger("State");
            // gasConsumed精度还是9
            BigDecimal gasConsumed = new BigDecimal(eventLogObj.getLongValue("GasConsumed")).divide(ConstantParam.ONG_DECIMAL);


            //deploy smart contract transaction  txType = 208  部署合约的逻辑处理
            if (TransactionTypeEnum.DEPLOYCODE.type() == txType) {
                handleDeployContractTx(txJson, blockHeight, blockTime, indexInBlock, confirmFlag, gasConsumed, payer, calledContractHash);
            } else if (TransactionTypeEnum.EVM_INVOKECODE.type() == txType && ConstantParam.EVM_ADDRESS_PREFIX.equals(calledContractHash)) {
                handleEVMDeployContractTx(txJson, txHash, txType, blockHeight, blockTime, indexInBlock, confirmFlag, gasConsumed, payer, calledContractHash);
            }

            //invoke smart contract transaction
            JSONArray notifyArray = eventLogObj.getJSONArray("Notify");
            //no event transaction or deploy smart contract transaction
            if (notifyArray.size() == 0) {
                insertTxBasicInfo(txType, txHash, blockHeight, blockTime, indexInBlock, confirmFlag, "",
                        gasConsumed, 1, EventTypeEnum.Others.type(), "", payer, calledContractHash);
            } else {
                for (int i = 0, len = notifyArray.size(); i < len; i++) {
                    JSONArray stateArray = null;
                    String evmStates = "";
                    JSONObject notifyObj = (JSONObject) notifyArray.get(i);
                    String contractAddress = notifyObj.getString("ContractAddress");

                    Object object = notifyObj.get("States");
                    if (object instanceof JSONArray) {
                        stateArray = (JSONArray) object;
                    } else if (object instanceof String && object.toString().startsWith(ConstantParam.EVM_ADDRESS_PREFIX)) {
                        evmStates = object.toString();
                    } else {
                        insertTxBasicInfo(txType, txHash, blockHeight, blockTime, indexInBlock, confirmFlag, "",
                                gasConsumed, i + 1, EventTypeEnum.Others.type(), contractAddress, payer, calledContractHash);
                        continue;
                    }

                    if (TransactionTypeEnum.EVM_INVOKECODE.type() == txType && paramsConfig.ONG_CONTRACTHASH.equals(contractAddress)) {
                        handleEVMOngTransferTx(evmStates, txType, txHash, blockHeight, blockTime, indexInBlock, contractAddress, gasConsumed, i + 1, notifyArray.size(), confirmFlag, payer, calledContractHash);

                    } else if (paramsConfig.ONG_CONTRACTHASH.equals(contractAddress) || paramsConfig.ONT_CONTRACTHASH.equals(contractAddress)) {
                        //transfer transaction
                        handleNativeTransferTx(stateArray, txType, txHash, blockHeight, blockTime, indexInBlock,
                                contractAddress, gasConsumed, i + 1, notifyArray.size(), confirmFlag, payer, calledContractHash);

                    } else if (paramsConfig.ONTID_CONTRACTHASH.equals(contractAddress)) {
                        isOntidTx = true;
                        //ontId operation transaction
                        handleOntIdTx(stateArray, txType, txHash, blockHeight, blockTime, indexInBlock,
                                gasConsumed, i + 1, contractAddress, payer, calledContractHash);

                    } else if (paramsConfig.CLAIMRECORD_CONTRACTHASH.equals(contractAddress)) {

                        if (stateArray.size() >= 4 && ConstantParam.CLAIMRECORD_OPE_PREFIX.equals(new String(Helper.hexToBytes(stateArray.getString(0))))) {
                            isOntidTx = true;
                        }
                        //claimrecord transaction
                        handleClaimRecordTxn(stateArray, txType, txHash, blockHeight, blockTime, indexInBlock,
                                gasConsumed, i + 1, contractAddress, payer, calledContractHash);

                    } else if (paramsConfig.AUTH_CONTRACTHASH.equals(contractAddress)) {
                        //auth transaction
                        insertTxBasicInfo(txType, txHash, blockHeight, blockTime, indexInBlock, confirmFlag, EventTypeEnum.Auth.des(),
                                gasConsumed, i + 1, EventTypeEnum.Auth.type(), contractAddress, payer, calledContractHash);

                    } else if (ConstantParam.OEP8CONTRACTS.contains(contractAddress)) {
                        //OEP8交易
                        handleOep8TransferTx(stateArray, txType, txHash, blockHeight, blockTime, indexInBlock,
                                gasConsumed, i + 1, confirmFlag, contractAddress, notifyArray.size(), payer, calledContractHash);

                    } else if (ConstantParam.OEP5CONTRACTS.contains(contractAddress)) {
                        //OEP5交易
                        handleOep5TransferTxn(stateArray, txType, txHash, blockHeight, blockTime, indexInBlock,
                                gasConsumed, i + 1, confirmFlag, contractAddress, ConstantParam.OEP5MAP.get(contractAddress), payer, calledContractHash);

                    } else if (ConstantParam.OEP4CONTRACTS.contains(contractAddress) && !paramsConfig.CANCEL_APPROVE_CONTRACT.equals(calledContractHash)) {
                        // wing 取消approve交易不解析
                        //OEP4交易
                        handleOep4TransferTxn(stateArray, txType, txHash, blockHeight, blockTime, indexInBlock,
                                gasConsumed, i + 1, confirmFlag, (JSONObject) ConstantParam.OEP4MAP.get(contractAddress), contractAddress, payer, calledContractHash);

                    } else if (ConstantParam.ORC20CONTRACTS.contains(contractAddress)) {
                        // orc20 交易
                        handleOrc20TransferTxn(evmStates, txType, txHash, blockHeight, blockTime, indexInBlock, gasConsumed, i + 1, confirmFlag, ConstantParam.ORC20MAP.get(contractAddress), contractAddress, payer, calledContractHash);

                    } else if (ConstantParam.ORC721CONTRACTS.contains(contractAddress)) {
                        // orc721 交易
                        handleOrc721TransferTxn(evmStates, txType, txHash, blockHeight, blockTime, indexInBlock, gasConsumed, i + 1, confirmFlag, ConstantParam.ORC721MAP.get(contractAddress), contractAddress, payer, calledContractHash);

                    } else if (ConstantParam.ORC1155CONTRACTS.contains(contractAddress)) {
                        // orc1155 交易
                        handleOrc1155TransferTxn(evmStates, txType, txHash, blockHeight, blockTime, indexInBlock, gasConsumed, i + 1, confirmFlag, contractAddress, payer, calledContractHash);

                    } else if (paramsConfig.UNISWAP_FACTORY_CONTRACTHASH.contains(contractAddress)) {
                        if (!insertInvokeDeploy) {
                            for (int j = 0; j < notifyArray.size(); j++) {
                                JSONObject subNotifyObj = (JSONObject) notifyArray.get(j);
                                String subContractAddress = subNotifyObj.getString("ContractAddress");
                                Object subObject = subNotifyObj.get("States");
                                JSONArray subStateArray = (JSONArray) subObject;
                                if (ConstantParam.INVOKE_DEPLOY_CONTRACT_ACTION.equals(subStateArray.getString(0))) {
                                    handleDeployContractTxByInvoke(blockTime, confirmFlag, payer, subContractAddress);
                                    break;
                                }
                            }
                            insertInvokeDeploy = true;
                        }
                        insertTxBasicInfo(txType, txHash, blockHeight, blockTime, indexInBlock, confirmFlag, "",
                                gasConsumed, i + 1, EventTypeEnum.Others.type(), contractAddress, payer, calledContractHash);
                    } else {
                        //other transaction
                        handleUnauditedOepOrcTransferTx(stateArray, evmStates, txType, txHash, blockHeight, blockTime, indexInBlock, gasConsumed, i + 1, confirmFlag, contractAddress, payer, calledContractHash);
//                        insertTxBasicInfo(txType, txHash, blockHeight, blockTime, indexInBlock, confirmFlag, "",
//                                gasConsumed, i + 1, EventTypeEnum.Others.type(), contractAddress, payer, calledContractHash);
                    }

                }
            }

            //记录交易基本信息和eventlog详情 对应一笔交易
            // 新增 nonce 值的字段，存在Eventlog 表中
            insertTxEventLog(txHash, blockTime, txType, blockHeight, indexInBlock, gasConsumed, confirmFlag, calledContractHash, eventLogObj.toJSONString(), isOntidTx);


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

    private void handleDeployContractTxByInvoke(int blockTime, int confirmFlag, String payer, String contractAddress) throws Exception {
        JSONObject contractObj = commonService.getContractInfoByContractAddress(contractAddress);
        //部署成功的合约才记录
        if (confirmFlag == 1) {
            Contract contract = Contract.builder()
                    .contractHash(contractAddress)
                    .build();
            Integer count = contractMapper.selectCount(contract);
            if (count.equals(0)) {
                insertContractInfo(contractAddress, blockTime, contractObj, payer);
            }
        }
    }

    /**
     * 解析这笔交易真正调用的合约hash
     *
     * @param code
     * @param txType
     * @return
     */
    private String parseCalledContractHash(String code, Integer txType, String txHash) throws IOException {
        // 从payload 中解析 被调用的 合约hash
        String calledContractHash = "";
        // neoVm 交易
        if (TransactionTypeEnum.NEOVM_INVOKECODE.type() == txType) {
            while (code.contains(ConstantParam.TXPAYLOAD_CODE_FLAG)) {
                int index = code.indexOf(ConstantParam.TXPAYLOAD_CODE_FLAG);
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
        } else if (TransactionTypeEnum.WASMVM_INVOKECODE.type() == txType) {
            // Wasm 交易的payload 前40位是逆序
            calledContractHash = Helper.reverse(code.substring(0, 40));
        } else if (TransactionTypeEnum.EVM_INVOKECODE.type() == txType) {
            RawTransaction rawTransaction = TransactionDecoder.decode(code);
            calledContractHash = rawTransaction.getTo();
            if (!ConstantParam.EVM_ADDRESS_PREFIX.equals(calledContractHash)) {
                // 解决evm类型的ong转账,calledContractHash为转出地址的问题
                String contractCode = web3jSdkUtil.getCode(calledContractHash);
                if (null == contractCode || ConstantParam.EVM_ADDRESS_PREFIX.equalsIgnoreCase(contractCode)) {
                    // 用户地址code为0x
                    calledContractHash = ConstantParam.ONG_CONTRACT_ADDRESS;
                }
            }
        }

        //判断属于什么OEP类型交易 payload 解析完就是的，在常量池存放的是正确的
        if (ConstantParam.OEP5CONTRACTS.contains(calledContractHash)) {
            IS_OEPTX_FLAG.get().put(ConstantParam.IS_OEP5TX, true);
        } else if (ConstantParam.OEP4CONTRACTS.contains(calledContractHash)) {
            IS_OEPTX_FLAG.get().put(ConstantParam.IS_OEP4TX, true);
        } else if (ConstantParam.OEP8CONTRACTS.contains(calledContractHash)) {
            IS_OEPTX_FLAG.get().put(ConstantParam.IS_OEP8TX, true);
        } else if (ConstantParam.ORC20CONTRACTS.contains(calledContractHash)) {
            IS_OEPTX_FLAG.get().put(ConstantParam.IS_ORC20TX, true);
        } else if (ConstantParam.ORC721CONTRACTS.contains(calledContractHash)) {
            IS_OEPTX_FLAG.get().put(ConstantParam.IS_ORC721TX, true);
        } else if (ConstantParam.ORC1155CONTRACTS.contains(calledContractHash)) {
            IS_OEPTX_FLAG.get().put(ConstantParam.IS_ORC1155TX, true);
        }
        return calledContractHash;
    }


    /**
     * 记录交易的eventlog
     *
     * @param txHash
     * @param txTime
     * @param txType
     * @param blockHeight
     * @param blockIndex
     * @param gasConsumed
     * @param confirmFlag
     * @param callednContractHash
     * @param eventLog
     */
    private void insertTxEventLog(String txHash, int txTime, int txType, int blockHeight, int blockIndex,
                                  BigDecimal gasConsumed, int confirmFlag, String callednContractHash,
                                  String eventLog, Boolean ontidTxFlag) {
        TxEventLog txEventLog = TxEventLog.builder()
                .txHash(txHash)
                .txTime(txTime)
                .txType(txType)
                .blockHeight(blockHeight)
                .blockIndex(blockIndex)
                .fee(gasConsumed)
                .confirmFlag(confirmFlag)
                .calledContractHash(callednContractHash)
                //表字段长度为5000
                .eventLog(eventLog.substring(0, eventLog.length() > 5000 ? 5000 : eventLog.length()))
                .ontidTxFlag(ontidTxFlag)
                .build();
        ConstantParam.BATCHBLOCKDTO.getTxEventLogs().add(txEventLog);
    }


    /**
     * 处理部署合约交易
     *
     * @param txJson
     * @param blockHeight
     * @param blockTime
     * @param indexInBlock
     * @param confirmFlag
     * @param gasConsumed
     * @param payer
     * @throws Exception
     */
    private void handleDeployContractTx(JSONObject txJson, int blockHeight, int blockTime, int indexInBlock,
                                        int confirmFlag, BigDecimal gasConsumed, String payer, String calledContractHash) throws Exception {

        String txHash = txJson.getString("Hash");
        int txType = txJson.getInteger("TxType");

        //  根据部署合约交易hash获取链上合约信息 拿到合约中的具体信息
        JSONObject contractObj = commonService.getContractInfoByTxHash(txHash);
        //原生合约hash需要转换  bf133b069101995c32eb560cfc984e751869cf39
        String contractHash = convertNativeContractHash(contractObj.getString("ContractHash"));

        contractObj.remove("ContractHash");

        insertTxBasicInfo(txType, txHash, blockHeight, blockTime, indexInBlock, confirmFlag, contractObj.toString(),
                gasConsumed, 0, EventTypeEnum.DeployContract.type(), contractHash, payer, calledContractHash);

        //部署成功的合约才记录
        if (confirmFlag == 1) {
            Contract contract = Contract.builder()
                    .contractHash(contractHash)
                    .build();
            Integer count = contractMapper.selectCount(contract);
            if (count.equals(0)) {
                insertContractInfo(contractHash, blockTime, contractObj, payer);
            }
        }
    }

    private void handleEVMDeployContractTx(JSONObject txJson, String txHash, int txType, int blockHeight, int blockTime, int indexInBlock, int confirmFlag, BigDecimal gasConsumed, String payer, String calledContractHash) throws Exception {
        JSONObject eventLogObj = txJson.getJSONObject("EventLog");
        String deployContractAddress = eventLogObj.getString("CreatedContract");
        deployContractAddress = ConstantParam.EVM_ADDRESS_PREFIX + Helper.reverse(deployContractAddress);

        JSONObject contractObj = new JSONObject();
        contractObj.put("Name", "");
        contractObj.put("Description", "deployed contract");
        contractObj.put("ContractHash", deployContractAddress);
        insertTxBasicInfo(txType, txHash, blockHeight, blockTime, indexInBlock, confirmFlag, contractObj.toString(), gasConsumed, 0, EventTypeEnum.DeployContract.type(), deployContractAddress, payer, calledContractHash);


        if (confirmFlag == 1) {
            Contract contract = Contract.builder()
                    .contractHash(deployContractAddress)
                    .build();
            Integer count = contractMapper.selectCount(contract);
            if (count.equals(0)) {
                insertContractInfo(deployContractAddress, blockTime, contractObj, payer);
            }
        }
    }


    /**
     * 原生合约hash需要特殊转换
     *
     * @param contractHash
     * @return
     */
    private String convertNativeContractHash(String contractHash) {
        switch (contractHash) {
            case ConstantParam.ONT_CONTRACTHASH:
                contractHash = paramsConfig.ONT_CONTRACTHASH;
                break;
            case ConstantParam.ONG_CONTRACTHASH:
                contractHash = paramsConfig.ONG_CONTRACTHASH;
                break;
            case ConstantParam.ONTID_CONTRACTHASH:
                contractHash = paramsConfig.ONTID_CONTRACTHASH;
                break;
            case ConstantParam.AUTH_CONTRACTHASH:
                contractHash = paramsConfig.AUTH_CONTRACTHASH;
                break;
            default:
                contractHash = contractHash;
        }

        return contractHash;
    }


    /**
     * 记录合约基本信息
     *
     * @param contractHash
     * @param blockTime
     * @param contractObj
     */
    private void insertContractInfo(String contractHash, int blockTime, JSONObject contractObj, String payer) {
        //在该批区块中可能出现多个部署合约交易， 但部署的是同一个合约
        if (!ConstantParam.BATCHBLOCK_CONTRACTHASH_LIST.contains(contractHash)) {
            Contract contract = Contract.builder()
                    .contractHash(contractHash)
                    .name(contractObj.getString("Name"))
                    .description(contractObj.getString("Description"))
                    .abi("")
                    .code("")
                    .sourceCode("")
                    .createTime(blockTime)
                    .updateTime(blockTime)
                    .creator(payer)
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

            ConstantParam.BATCHBLOCKDTO.getContracts().add(contract);
            ConstantParam.BATCHBLOCK_CONTRACTHASH_LIST.add(contractHash);
        }
    }


    /**
     * 处理原生ont，ong转账
     *
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
    private void handleNativeTransferTx(JSONArray stateList, int txType, String txHash,
                                        int blockHeight, int blockTime, int indexInBlock, String contractAddress,
                                        BigDecimal gasConsumed, int indexInTx, int notifyListSize, int confirmFlag,
                                        String payer, String calledContractHash) throws Exception {
        int stateSize = stateList.size();
        if (stateSize < 3) {
            TxDetail txDetail = generateTransaction("", "", "", ConstantParam.ZERO, txType, txHash, blockHeight,
                    blockTime, indexInBlock, confirmFlag, "", gasConsumed, indexInTx, EventTypeEnum.Transfer.type(), contractAddress, payer, calledContractHash);

            ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
            ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));
            return;
        }

        int eventType = EventTypeEnum.Transfer.type();
        String assetName = "";

        String action = (String) stateList.get(0);
        //手续费不为0的情况下，notifylist的最后一个一定是收取手续费event
        if (gasConsumed.compareTo(ConstantParam.ZERO) != 0 && (indexInTx == notifyListSize) && paramsConfig.ONG_CONTRACTHASH.equals(contractAddress)) {
            action = EventTypeEnum.Gasconsume.des();
            eventType = EventTypeEnum.Gasconsume.type();
        }

        String fromAddress = (String) stateList.get(1);
        String toAddress = (String) stateList.get(2);
        BigDecimal amount = new BigDecimal((stateList.get(3)).toString());
        if (stateSize < 5) {
            // 旧精度event
            if (paramsConfig.ONG_CONTRACTHASH.equals(contractAddress)) {
                assetName = ConstantParam.ASSET_NAME_ONG;
                amount = amount.divide(ConstantParam.ONG_DECIMAL);
            } else if (paramsConfig.ONT_CONTRACTHASH.equals(contractAddress)) {
                assetName = ConstantParam.ASSET_NAME_ONT;
            }
        } else {
            // 新精度event
            BigDecimal extra = new BigDecimal((stateList.get(4)).toString());
            if (paramsConfig.ONG_CONTRACTHASH.equals(contractAddress)) {
                assetName = ConstantParam.ASSET_NAME_ONG;
                amount = amount.divide(ConstantParam.ONG_DECIMAL);
                BigDecimal extraDecimal = extra.divide(ConstantParam.NEW_ONG_DECIMAL);
                amount = amount.add(extraDecimal);
            } else if (paramsConfig.ONT_CONTRACTHASH.equals(contractAddress)) {
                assetName = ConstantParam.ASSET_NAME_ONT;
                BigDecimal extraDecimal = extra.divide(ConstantParam.NEW_ONT_DECIMAL);
                amount = amount.add(extraDecimal);
            }
        }
        log.info("####fromAddress:{},toAddress:{},amount:{}####", fromAddress, toAddress, amount.toPlainString());

        TxDetail txDetail = generateTransaction(fromAddress, toAddress, assetName, amount, txType, txHash, blockHeight,
                blockTime, indexInBlock, confirmFlag, action, gasConsumed, indexInTx, eventType, contractAddress, payer, calledContractHash);

        ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
        ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));
        // OEP交易的手续费入对应的子表
        if (paramsConfig.ONG_CONTRACTHASH.equals(contractAddress)) {
            if (IS_OEPTX_FLAG.get().get(ConstantParam.IS_OEP8TX)) {
                ConstantParam.BATCHBLOCKDTO.getOep8TxDetails().add(TxDetail.toOep8TxDetail(txDetail));
            } else if (IS_OEPTX_FLAG.get().get(ConstantParam.IS_OEP5TX)) {
                ConstantParam.BATCHBLOCKDTO.getOep5TxDetails().add(TxDetail.toOep5TxDetail(txDetail));
            } else if (IS_OEPTX_FLAG.get().get(ConstantParam.IS_OEP4TX)) {
                ConstantParam.BATCHBLOCKDTO.getOep4TxDetails().add(TxDetail.toOep4TxDetail(txDetail));
            }
        }
        if (EventTypeEnum.Transfer.type() == eventType) {
            transferTransactionPush.publish(txDetail);
        }
    }


    // EVM 类型的 支付手续费
    private void handleEVMOngTransferTx(String evmStates, int txType, String txHash,
                                        int blockHeight, int blockTime, int indexInBlock, String contractAddress,
                                        BigDecimal gasConsumed, int indexInTx, int notifyListSize, int confirmFlag,
                                        String payer, String calledContractHash) throws Exception {
        if (evmStates.startsWith(ConstantParam.EVM_ADDRESS_PREFIX)) {
            evmStates = evmStates.substring(2);
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(Helper.hexToBytes(evmStates));
        BinaryReader reader = new BinaryReader(bais);

        // 前20位截到,合约地址没用
        byte[] addressBytes = reader.readBytes(20);

        int length = reader.readInt();
        List<String> topicList = new ArrayList<String>();
        for (int i = 0; i < length; i++) {
            byte[] TopicBytes = reader.readBytes(32);
            String topic = Helper.toHexString(TopicBytes);
            topicList.add(topic);
        }

        byte[] dataBytes = reader.readVarBytes();
        String data = Helper.toHexString(dataBytes);

        String decodeData = topicList.get(1) + topicList.get(2) + data;

        List<Type> result = handleEvmOrc20Orc721TransferResults(decodeData);

        String fromAddress = result.get(0).getValue().toString();
        String toAddress = result.get(1).getValue().toString();
        BigInteger amountValue = (BigInteger) result.get(2).getValue();
        String action = EventTypeEnum.Transfer.des();
        int eventType = EventTypeEnum.Transfer.type();
        // 处理好精度
        BigDecimal amount = new BigDecimal(amountValue).divide(ConstantParam.NEW_ONG_DECIMAL);

        log.info("####fromAddress:{},toAddress:{},amount:{}####", fromAddress, toAddress, amount.toPlainString());

        if (gasConsumed.compareTo(ConstantParam.ZERO) != 0 && (indexInTx == notifyListSize)) {
            action = EventTypeEnum.Gasconsume.des();
            eventType = EventTypeEnum.Gasconsume.type();
        }
        TxDetail txDetail = generateTransaction(fromAddress, toAddress, ConstantParam.ASSET_NAME_ONG, amount, txType, txHash, blockHeight,
                blockTime, indexInBlock, confirmFlag, action, gasConsumed, indexInTx, eventType, ConstantParam.ONG_CONTRACT_ADDRESS, payer, calledContractHash);

        ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
        ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));

        // ORC交易的手续费入对应的子表
        if (IS_OEPTX_FLAG.get().get(ConstantParam.IS_ORC20TX)) {
            ConstantParam.BATCHBLOCKDTO.getOrc20TxDetails().add(TxDetail.toOrc20TxDetail(txDetail));
        } else if (IS_OEPTX_FLAG.get().get(ConstantParam.IS_ORC721TX)) {
            ConstantParam.BATCHBLOCKDTO.getOrc721TxDetails().add(TxDetail.toOrc721TxDetail(txDetail, ConstantParam.EMPTY));
        } else if (IS_OEPTX_FLAG.get().get(ConstantParam.IS_ORC1155TX)) {
            ConstantParam.BATCHBLOCKDTO.getOrc1155TxDetails().add(TxDetail.toOrc1155TxDetail(txDetail, ConstantParam.EMPTY));
        }
    }


    /**
     * 处理ontid交易
     *
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
    private void handleOntIdTx(JSONArray stateList, int txType, String txHash, int blockHeight,
                               int blockTime, int indexInBlock, BigDecimal gasConsumed, int indexInTx,
                               String contractAddress, String payer, String calledContractHash) throws Exception {

        String action = stateList.getString(0);
        String ontId = "";
        if (OntIdEventDesEnum.REGISTERONTID.des().equals(action)
                || OntIdEventDesEnum.REVOKE.des().equals(action)
                || OntIdEventDesEnum.REMOVECONTROLLER.des().equals(action)) {
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
        ConstantParam.BATCHBLOCKDTO.getOntidTxDetails().add(ontidTxDetail);

        insertTxBasicInfo(txType, txHash, blockHeight, blockTime, indexInBlock, 1, EventTypeEnum.Ontid.des() + action,
                gasConsumed, indexInTx, EventTypeEnum.Ontid.type(), contractAddress, payer, calledContractHash);

        //如果是注册ontid交易，ontid数量加1
        if (ConstantParam.REGISTER.equals(action)) {
            addOneBlockOntIdCount();
        }
    }

    /**
     * 累加一个区块里注册ontid的交易
     */
    public synchronized void addOneBlockOntIdCount() {
        ConstantParam.BATCHBLOCK_ONTID_COUNT++;
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
                    attrName = new String(Helper.hexToBytes(attrName));
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

        } else if (OntIdEventDesEnum.RECOVERYOPE.des().equals(action) || OntIdEventDesEnum.RECOVERYOPE.des().toLowerCase().equals(action)) {
            log.info("compare action: {} & {}", OntIdEventDesEnum.RECOVERYOPE.des(), action);
            String op = stateList.getString(1);
            String address = "";
            try {
                address = Address.parse(stateList.getString(3)).toBase58();
            } catch (Exception e) {
                op = op + " " + OntIdEventDesEnum.RECOVERYOPE.des();
            }
            log.info("####Recovery op:{}, ontid:{}, address:{}####", op, ontId, address);

            descriptionSb.append(op);
            descriptionSb.append(ConstantParam.ONTID_SEPARATOR);
            descriptionSb.append(ontId);
            if (!StringUtils.isEmpty(address)) {
                descriptionSb.append(ConstantParam.ONTID_SEPARATOR);
                descriptionSb.append(address);
            }

            str = descriptionSb.toString();
        }
        return str;
    }

    /**
     * 处理存证交易
     *
     * @param stateArray
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
    private void handleClaimRecordTxn(JSONArray stateArray, int txType, String txHash, int blockHeight,
                                      int blockTime, int indexInBlock, BigDecimal gasConsumed, int indexInTx,
                                      String contractAddress, String payer, String calledContractHash) throws Exception {

        String actionType = new String(Helper.hexToBytes(stateArray.getString(0)));
        StringBuilder sb = new StringBuilder(140);
        sb.append(EventTypeEnum.Claimrecord.des());

        if (ConstantParam.CLAIMRECORD_OPE_PREFIX.equals(actionType)) {
            if (stateArray.size() >= 4) {
                String issuerOntId = new String(Helper.hexToBytes(stateArray.getString(1)));
                String action = new String(Helper.hexToBytes(stateArray.getString(2)));
                String claimId = new String(Helper.hexToBytes(stateArray.getString(3)));
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
                ConstantParam.BATCHBLOCKDTO.getOntidTxDetails().add(ontidTxDetail);
            }
        }

        insertTxBasicInfo(txType, txHash, blockHeight, blockTime, indexInBlock, 1, sb.toString(),
                gasConsumed, indexInTx, EventTypeEnum.Claimrecord.type(), contractAddress, payer, calledContractHash);
    }


    /**
     * 处理oep8交易
     *
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
    private void handleOep8TransferTx(JSONArray stateArray, int txType, String txHash, int blockHeight,
                                      int blockTime, int indexInBlock, BigDecimal gasConsumed, int indexInTx,
                                      int confirmFlag, String contractAddress, int stateSize, String payer, String calledContractHash) throws Exception {
        boolean wasmOep8;
        String action = (String) stateArray.get(0);
        try {
            action = new String(Helper.hexToBytes(action));
            wasmOep8 = false;
        } catch (Exception e) {
            log.warn("parse oep8 action error:{}", action);
            wasmOep8 = true;
        }
        boolean needParse = EventTypeEnum.Transfer.des().equalsIgnoreCase(action) || EventTypeEnum.Approval.des().equalsIgnoreCase(action);

        if (stateArray.size() < 5 || !needParse) {
            TxDetail txDetail = generateTransaction("", "", "", ConstantParam.ZERO, txType, txHash, blockHeight,
                    blockTime, indexInBlock, confirmFlag, "", gasConsumed, indexInTx, EventTypeEnum.Others.type(), contractAddress, payer, calledContractHash);

            ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
            ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));
            ConstantParam.BATCHBLOCKDTO.getOep8TxDetails().add(TxDetail.toOep8TxDetail(txDetail));
            return;
        }

        String fromAddress = (String) stateArray.get(1);
        String toAddress = (String) stateArray.get(2);
        String tokenId = (String) stateArray.get(3);
        String amount = (String) stateArray.get(4);
        JSONObject oep8Obj = (JSONObject) ConstantParam.OEP8MAP.get(contractAddress + "-" + tokenId);
        if ("00".equals(fromAddress) && stateSize == 2) {
            // mint方法即增加发行量方法, 区分标志：fromAddress为“00”，同时stateSize为2
            Oep8 oep8 = Oep8.builder()
                    .contractHash(contractAddress)
                    .tokenId((String) stateArray.get(3))
                    .totalSupply(commonService.getOep8TotalSupply(tokenId))
                    .build();
            //在子线程直接更新，不批量更新
            oep8Mapper.updateByPrimaryKeySelective(oep8);
        }

        if (40 == fromAddress.length()) {
            fromAddress = Address.parse(fromAddress).toBase58();
        }
        if (40 == toAddress.length()) {
            toAddress = Address.parse(toAddress).toBase58();
        }

        BigDecimal eventAmount;
        if (wasmOep8) {
            eventAmount = new BigDecimal(amount);
        } else {
            eventAmount = new BigDecimal(Helper.BigIntFromNeoBytes(Helper.hexToBytes(amount)));
        }
        if (ConstantParam.MAX_APPROVAL_AMOUNT.compareTo(eventAmount) <= 0) {
            eventAmount = ConstantParam.MAX_APPROVAL_AMOUNT;
        }
        log.info("OEP8TransferTx:fromaddress:{}, toaddress:{}, tokenid:{}, amount:{}", fromAddress, toAddress, tokenId, eventAmount);

        TxDetail txDetail = generateTransaction(fromAddress, toAddress, oep8Obj.getString("name"), eventAmount, txType, txHash, blockHeight,
                blockTime, indexInBlock, confirmFlag, action, gasConsumed, indexInTx, EventTypeEnum.Transfer.type(), contractAddress, payer, calledContractHash);

        ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
        ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));
        ConstantParam.BATCHBLOCKDTO.getOep8TxDetails().add(TxDetail.toOep8TxDetail(txDetail));

        transferTransactionPush.publish(txDetail);
    }

    /**
     * 处理oep5交易
     *
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
    private void handleOep5TransferTxn(JSONArray stateArray, int txType, String txHash, int blockHeight,
                                       int blockTime, int indexInBlock, BigDecimal gasConsumed, int indexInTx, int confirmFlag,
                                       String contractAddress, JSONObject oep5Obj, String payer, String calledContractHash) throws Exception {

        Boolean isTransfer = Boolean.FALSE;
        String action;
        try {
            action = new String(Helper.hexToBytes((String) stateArray.get(0)));
        } catch (Exception e) {
            action = (String) stateArray.get(0);
        }
        //只解析birth和transfer合约方法
        if (!(action.equalsIgnoreCase("transfer") || action.equalsIgnoreCase("birth"))) {
            TxDetail txDetail = generateTransaction("", "", "", ConstantParam.ZERO, txType, txHash, blockHeight,
                    blockTime, indexInBlock, confirmFlag, action, gasConsumed, indexInTx, EventTypeEnum.Others.type(), contractAddress, payer, calledContractHash);

            ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
            ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));
            ConstantParam.BATCHBLOCKDTO.getOep5TxDetails().add(TxDetail.toOep5TxDetail(txDetail));
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
                ConstantParam.BATCHBLOCKDTO.getOep5Dragons().add(oep5Dragon);
            } else {
                amount = ConstantParam.ONE;
                dragonId = Helper.BigIntFromNeoBytes(Helper.hexToBytes((String) stateArray.get(3))).toString();
                isTransfer = Boolean.TRUE;
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
                //在子线程直接更新，不批量更新
                oep5Mapper.updateByPrimaryKeySelective(oep5);

            } else if ("transfer".equalsIgnoreCase(action)) {
                //transfer方法，tokenid在位置3
                assetName = oep5Obj.getString("symbol") + stateArray.get(3);
                amount = ConstantParam.ONE;
                isTransfer = Boolean.TRUE;
            }
        }

        log.info("OEP5TransferTx:fromaddress:{}, toaddress:{}, assetName:{}", fromAddress, toAddress, assetName);

        TxDetail txDetail = generateTransaction(fromAddress, toAddress, assetName, amount, txType, txHash, blockHeight,
                blockTime, indexInBlock, confirmFlag, action, gasConsumed, indexInTx, EventTypeEnum.Transfer.type(), contractAddress, payer, calledContractHash);

        ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
        ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));
        ConstantParam.BATCHBLOCKDTO.getOep5TxDetails().add(TxDetail.toOep5TxDetail(txDetail));

        if (isTransfer) {
            transferTransactionPush.publish(txDetail);
        }
    }


    private void handleOep4TransferTxn(JSONArray stateArray, int txType, String txHash, int blockHeight,
                                       int blockTime, int indexInBlock, BigDecimal gasConsumed, int indexInTx, int confirmFlag,
                                       JSONObject oep4Obj, String contractHash, String payer, String calledContractHash) throws Exception {
        String fromAddress = "";
        String toAddress = "";
        BigDecimal eventAmount = BigDecimal.ZERO;
        Boolean isTransfer = Boolean.FALSE;
        String txAction = EventTypeEnum.Transfer.des();
        Integer eventType = EventTypeEnum.Transfer.type();

        if (stateArray.size() != 4) {
            log.warn("Invalid OEP-4 event in transaction {}", txHash);
            TxDetail txDetail = generateTransaction(fromAddress, toAddress, "", eventAmount, txType, txHash, blockHeight,
                    blockTime, indexInBlock, confirmFlag, "", gasConsumed, indexInTx, EventTypeEnum.Others.type(), contractHash, payer, calledContractHash);

            ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
            ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));
            ConstantParam.BATCHBLOCKDTO.getOep4TxDetails().add(TxDetail.toOep4TxDetail(txDetail));
            return;
        }

        String action;
        try {
            action = new String(Helper.hexToBytes((String) stateArray.get(0)));
        } catch (Exception e) {
            action = (String) stateArray.get(0);
        }

        if (action.equalsIgnoreCase("transfer")) {
            try {
                fromAddress = Address.parse((String) stateArray.get(1)).toBase58();
            } catch (Exception e) {
                fromAddress = (String) stateArray.get(1);
            }

            try {
                toAddress = Address.parse((String) stateArray.get(2)).toBase58();
            } catch (Exception e) {
                toAddress = (String) stateArray.get(2);
            }
            eventAmount = BigDecimalFromNeoVmData((String) stateArray.get(3));
            log.info("Parsing OEP4 transfer event: from {}, to {}, amount {}", fromAddress, toAddress, eventAmount);
            isTransfer = Boolean.TRUE;
        }

        if (action.equalsIgnoreCase("approval")) {
            txAction = EventTypeEnum.Approval.des();
            eventType = EventTypeEnum.Approval.type();
            try {
                fromAddress = Address.parse((String) stateArray.get(1)).toBase58();
            } catch (Exception e) {
                fromAddress = (String) stateArray.get(1);
            }

            try {
                toAddress = Address.parse((String) stateArray.get(2)).toBase58();
            } catch (Exception e) {
                toAddress = (String) stateArray.get(2);
            }
            eventAmount = BigDecimalFromNeoVmData((String) stateArray.get(3));
            log.info("Parsing OEP4 approval event: from {}, to {}, amount {}", fromAddress, toAddress, eventAmount);
//            isTransfer = Boolean.TRUE;
        }

        if (paramsConfig.PAX_CONTRACTHASH.equals(contractHash)) {
            if (action.equalsIgnoreCase("IncreasePAX")) {
                try {
                    fromAddress = paramsConfig.PAX_CONTRACTHASH;
                    toAddress = Address.parse((String) stateArray.get(1)).toBase58();
                    eventAmount = BigDecimalFromNeoVmData((String) stateArray.get(2));
                } catch (Exception e) {
                    log.info("Parsing increase PAX event failed in transaction {}", txHash);
                }
                log.info("Parsing increase PAX event: from {} to {} amount {}", fromAddress, toAddress, eventAmount);
            }

            if (action.equalsIgnoreCase("DecreasePAX")) {
                try {
                    fromAddress = Address.parse((String) stateArray.get(1)).toBase58();
                    toAddress = paramsConfig.PAX_CONTRACTHASH;
                    eventAmount = BigDecimalFromNeoVmData((String) stateArray.get(3));
                } catch (Exception e) {
                    log.info("Parsing increase PAX event failed in transaction {}", txHash);
                }
                log.info("Parsing decrease PAX event: from {} to {} amount {}", fromAddress, toAddress, eventAmount);
            }
        }

        String assetName = oep4Obj.getString("symbol");
        Integer decimals = oep4Obj.getInteger("decimals");
        BigDecimal amount = eventAmount.divide(BigDecimal.TEN.pow(decimals), decimals, RoundingMode.DOWN);
        if (ConstantParam.MAX_APPROVAL_AMOUNT.compareTo(amount) <= 0 || BigDecimal.ZERO.compareTo(amount) > 0) {
            amount = ConstantParam.MAX_APPROVAL_AMOUNT;
        }
        TxDetail txDetail = generateTransaction(fromAddress, toAddress, assetName, amount, txType, txHash, blockHeight,
                blockTime, indexInBlock, confirmFlag, txAction, gasConsumed, indexInTx, eventType, contractHash, payer, calledContractHash);

        ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
        ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));
        ConstantParam.BATCHBLOCKDTO.getOep4TxDetails().add(TxDetail.toOep4TxDetail(txDetail));

        if (isTransfer) {
            transferTransactionPush.publish(txDetail);
        }
    }

    private void handleOrc20TransferTxn(String evmStates, int txType, String txHash, int blockHeight, int blockTime, int indexInBlock, BigDecimal gasConsumed, int indexInTx, int confirmFlag,
                                        JSONObject orc20Obj, String contractHash, String payer, String calledContractHash) {
        if (evmStates.startsWith(ConstantParam.EVM_ADDRESS_PREFIX)) {
            evmStates = evmStates.substring(2);
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(Helper.hexToBytes(evmStates));
        BinaryReader reader = new BinaryReader(bais);

        try {
            byte[] addressBytes = reader.readBytes(20);
            String contractAddress = Helper.toHexString(addressBytes);

            int length = reader.readInt();
            List<String> topicList = new ArrayList<String>();
            for (int i = 0; i < length; i++) {
                byte[] TopicBytes = reader.readBytes(32);
                String topic = Helper.toHexString(TopicBytes);
                topicList.add(topic);
            }

            byte[] dataBytes = reader.readVarBytes();
            String data = Helper.toHexString(dataBytes);

            String fromAddress = ConstantParam.EMPTY;
            String toAddress = ConstantParam.EMPTY;
            String txAction = ConstantParam.EMPTY;
            BigDecimal amountValue = BigDecimal.ZERO;
            int eventType = 0;

            if (ConstantParam.TRANSFER_TX.equals(topicList.get(0))) {
                // Transfer TX
                String parseData = topicList.get(1) + topicList.get(2) + data;
                List<Type> result = handleEvmOrc20Orc721TransferResults(parseData);
                fromAddress = result.get(0).getValue().toString();
                toAddress = result.get(1).getValue().toString();
                amountValue = new BigDecimal((BigInteger) result.get(2).getValue());
                txAction = EventTypeEnum.Transfer.des();
                eventType = EventTypeEnum.Transfer.type();

            } else if (ConstantParam.Approval_TX.equals(topicList.get(0))) {
                // Approval TX
                String parseData = topicList.get(1) + topicList.get(2) + data;
                List<Type> result = handleEvmOrc20Orc721TransferResults(parseData);
                fromAddress = result.get(0).getValue().toString();
                toAddress = result.get(1).getValue().toString();
                amountValue = new BigDecimal((BigInteger) result.get(2).getValue());
                txAction = EventTypeEnum.Approval.des();
                eventType = EventTypeEnum.Approval.type();
            }
            String assetName = orc20Obj.getString("name");
            Integer decimals = orc20Obj.getInteger("decimals");
            BigDecimal amount = amountValue.divide(BigDecimal.TEN.pow(decimals), decimals, RoundingMode.DOWN);
            if (ConstantParam.MAX_APPROVAL_AMOUNT.compareTo(amount) <= 0 || BigDecimal.ZERO.compareTo(amount) > 0) {
                amount = ConstantParam.MAX_APPROVAL_AMOUNT;
            }

            contractHash = ConstantParam.EVM_ADDRESS_PREFIX + Helper.reverse(contractHash);

            TxDetail txDetail = generateTransaction(fromAddress, toAddress, assetName, amount, txType, txHash, blockHeight,
                    blockTime, indexInBlock, confirmFlag, txAction, gasConsumed, indexInTx, eventType, contractHash, payer, calledContractHash);

            ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
            ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));
            ConstantParam.BATCHBLOCKDTO.getOrc20TxDetails().add(TxDetail.toOrc20TxDetail(txDetail));
        } catch (IOException e) {
            log.error("handle orc20 tx error");
            e.printStackTrace();
        }
    }


    public List<Type> handleEvmOrc20Orc721TransferResults(String parseData) {
        List<TypeReference<?>> outputParameters = Arrays.asList(ConstantParam.TYPE_REFERENCE_ADDRESS, ConstantParam.TYPE_REFERENCE_ADDRESS, ConstantParam.TYPE_REFERENCE_UINT256);
        List<TypeReference<Type>> convert = Utils.convert(outputParameters);

        return FunctionReturnDecoder.decode(parseData, convert);
    }


    private void handleOrc721TransferTxn(String evmStates, int txType, String txHash, int blockHeight, int blockTime, int indexInBlock, BigDecimal gasConsumed, int indexInTx, int confirmFlag, JSONObject orc721Obj, String contractHash, String payer, String calledContractHash) {
        if (evmStates.startsWith(ConstantParam.EVM_ADDRESS_PREFIX)) {
            evmStates = evmStates.substring(2);
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(Helper.hexToBytes(evmStates));
        BinaryReader reader = new BinaryReader(bais);

        try {
            byte[] addressBytes = reader.readBytes(20);
            String contractAddress = Helper.toHexString(addressBytes);

            int length = reader.readInt();
            List<String> topicList = new ArrayList<String>();
            for (int i = 0; i < length; i++) {
                byte[] TopicBytes = reader.readBytes(32);
                String topic = Helper.toHexString(TopicBytes);
                topicList.add(topic);
            }

            String fromAddress = ConstantParam.EMPTY;
            String toAddress = ConstantParam.EMPTY;
            String txAction = ConstantParam.EMPTY;
            BigDecimal amount = BigDecimal.ZERO;
            String tokenId = ConstantParam.EMPTY;
            int eventType = 0;

            if (ConstantParam.TRANSFER_TX.equals(topicList.get(0))) {
                // Transfer TX
                String parseData = topicList.get(1) + topicList.get(2) + topicList.get(3);
                List<Type> result = handleEvmOrc20Orc721TransferResults(parseData);
                fromAddress = result.get(0).getValue().toString();
                toAddress = result.get(1).getValue().toString();
                amount = BigDecimal.ONE;
                tokenId = result.get(2).getValue().toString();
                txAction = EventTypeEnum.Transfer.des();
                eventType = EventTypeEnum.Transfer.type();
            } else if (ConstantParam.Approval_TX.equals(topicList.get(0))) {
                // Approval TX
                String parseData = topicList.get(1) + topicList.get(2) + topicList.get(3);
                List<Type> result = handleEvmOrc20Orc721TransferResults(parseData);
                fromAddress = result.get(0).getValue().toString();
                toAddress = result.get(1).getValue().toString();
                amount = BigDecimal.ONE;
                tokenId = result.get(2).getValue().toString();
                txAction = EventTypeEnum.Approval.des();
                eventType = EventTypeEnum.Approval.type();
            }

            String assetName = orc721Obj.getString("name");

            contractHash = ConstantParam.EVM_ADDRESS_PREFIX + Helper.reverse(contractHash);

            TxDetail txDetail = generateTransaction(fromAddress, toAddress, assetName, amount, txType, txHash, blockHeight, blockTime, indexInBlock, confirmFlag, txAction, gasConsumed, indexInTx, eventType, contractHash, payer, calledContractHash);

            ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
            ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));
            ConstantParam.BATCHBLOCKDTO.getOrc721TxDetails().add(TxDetail.toOrc721TxDetail(txDetail, tokenId));
        } catch (IOException e) {
            log.error("handle orc721 tx error");
            e.printStackTrace();
        }
    }

    private void handleOrc1155TransferTxn(String evmStates, int txType, String txHash, int blockHeight, int blockTime, int indexInBlock, BigDecimal gasConsumed, int indexInTx, int confirmFlag, String contractHash, String payer, String calledContractHash) {
        if (evmStates.startsWith(ConstantParam.EVM_ADDRESS_PREFIX)) {
            evmStates = evmStates.substring(2);
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(Helper.hexToBytes(evmStates));
        BinaryReader reader = new BinaryReader(bais);

        try {
            byte[] addressBytes = reader.readBytes(20);

            int length = reader.readInt();
            List<String> topicList = new ArrayList<String>();
            for (int i = 0; i < length; i++) {
                byte[] TopicBytes = reader.readBytes(32);
                String topic = Helper.toHexString(TopicBytes);
                topicList.add(topic);
            }

            byte[] dataBytes = reader.readVarBytes();
            String data = Helper.toHexString(dataBytes);

            String fromAddress = ConstantParam.EMPTY;
            String toAddress = ConstantParam.EMPTY;
            String txAction = ConstantParam.EMPTY;
            BigDecimal amount = BigDecimal.ZERO;
            String tokenId = ConstantParam.EMPTY;
            String assetName = ConstantParam.EMPTY;
            int eventType = 0;

            String hexContractHash = ConstantParam.EVM_ADDRESS_PREFIX + Helper.reverse(contractHash);

            if (ConstantParam.TRANSFER_SINGLE_TX.equals(topicList.get(0))) {
                // Transfer Single TX
                String parseData = topicList.get(1) + topicList.get(2) + topicList.get(3) + data;
                List<Type> result = handleOrc1155TransferResults(parseData);
                fromAddress = result.get(1).getValue().toString();
                toAddress = result.get(2).getValue().toString();
                tokenId = result.get(3).getValue().toString();
                amount = new BigDecimal((BigInteger) result.get(4).getValue());
                if (ConstantParam.MAX_APPROVAL_AMOUNT.compareTo(amount) <= 0) {
                    amount = ConstantParam.MAX_APPROVAL_AMOUNT;
                }
                txAction = EventTypeEnum.Transfer.des();
                eventType = EventTypeEnum.Transfer.type();
                JSONObject orc1155Obj = ConstantParam.ORC1155MAP.get(contractHash + "-" + tokenId);
                if (orc1155Obj != null) {
                    assetName = orc1155Obj.getString("name");
                }
                TxDetail txDetail = generateTransaction(fromAddress, toAddress, assetName, amount, txType, txHash, blockHeight, blockTime, indexInBlock, confirmFlag, txAction, gasConsumed, indexInTx, eventType, hexContractHash, payer, calledContractHash);
                ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
                ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));
                ConstantParam.BATCHBLOCKDTO.getOrc1155TxDetails().add(TxDetail.toOrc1155TxDetail(txDetail, tokenId));
            } else if (ConstantParam.TRANSFER_BATCH_TX.equals(topicList.get(0))) {
                fromAddress = ConstantParam.EVM_ADDRESS_PREFIX + topicList.get(2).substring(24);
                toAddress = ConstantParam.EVM_ADDRESS_PREFIX + topicList.get(3).substring(24);
                List<Type> result = handleOrc1155BatchTransferResults(data);
                List<Uint256> tokenIds = (List<Uint256>) result.get(0).getValue();
                List<Uint256> values = (List<Uint256>) result.get(1).getValue();
                txAction = EventTypeEnum.Transfer.des();
                eventType = EventTypeEnum.Transfer.type();
                int replaceIndexInTx = indexInTx;
                for (int i = 0; i < tokenIds.size(); i++) {
                    tokenId = tokenIds.get(i).getValue().toString();
                    amount = new BigDecimal(values.get(i).getValue());
                    if (ConstantParam.MAX_APPROVAL_AMOUNT.compareTo(amount) <= 0) {
                        amount = ConstantParam.MAX_APPROVAL_AMOUNT;
                    }
                    JSONObject orc1155Obj = ConstantParam.ORC1155MAP.get(contractHash + "-" + tokenId);
                    if (orc1155Obj != null) {
                        assetName = orc1155Obj.getString("name");
                    }
                    if (i > 0) {
                        replaceIndexInTx = indexInTx + 1000 + i;
                    }
                    TxDetail txDetail = generateTransaction(fromAddress, toAddress, assetName, amount, txType, txHash, blockHeight, blockTime, indexInBlock, confirmFlag, txAction, gasConsumed, replaceIndexInTx, eventType, hexContractHash, payer, calledContractHash);
                    ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
                    ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));
                    ConstantParam.BATCHBLOCKDTO.getOrc1155TxDetails().add(TxDetail.toOrc1155TxDetail(txDetail, tokenId));
                }
            } else {
                TxDetail txDetail = generateTransaction(fromAddress, toAddress, assetName, amount, txType, txHash, blockHeight, blockTime, indexInBlock, confirmFlag, txAction, gasConsumed, indexInTx, eventType, hexContractHash, payer, calledContractHash);
                ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
                ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));
                ConstantParam.BATCHBLOCKDTO.getOrc1155TxDetails().add(TxDetail.toOrc1155TxDetail(txDetail, tokenId));
            }
        } catch (IOException e) {
            log.error("handle orc1155 tx error");
            e.printStackTrace();
        }
    }

    public List<Type> handleOrc1155TransferResults(String parseData) {
        List<TypeReference<?>> outputParameters = Arrays.asList(ConstantParam.TYPE_REFERENCE_ADDRESS, ConstantParam.TYPE_REFERENCE_ADDRESS, ConstantParam.TYPE_REFERENCE_ADDRESS, ConstantParam.TYPE_REFERENCE_UINT256, ConstantParam.TYPE_REFERENCE_UINT256);
        List<TypeReference<Type>> convert = Utils.convert(outputParameters);

        return FunctionReturnDecoder.decode(parseData, convert);
    }

    public List<Type> handleOrc1155BatchTransferResults(String parseData) {
        List<TypeReference<?>> outputParameters = Arrays.asList(ConstantParam.TYPE_REFERENCE_UINT256_ARRAY, ConstantParam.TYPE_REFERENCE_UINT256_ARRAY);
        List<TypeReference<Type>> convert = Utils.convert(outputParameters);

        return FunctionReturnDecoder.decode(parseData, convert);
    }

    private void handleUnauditedOepOrcTransferTx(JSONArray stateArray, String evmStates, int txType, String txHash, int blockHeight, int blockTime, int indexInBlock, BigDecimal gasConsumed, int indexInTx, int confirmFlag, String contractAddress, String payer, String calledContractHash) {
        if (stateArray == null) {
            // evm交易
            handleUnauditedOrcTransferTx(evmStates, txType, txHash, blockHeight, blockTime, indexInBlock, gasConsumed, indexInTx, confirmFlag, contractAddress, payer, calledContractHash);
        } else {
            // ovm交易
            handleUnauditedOepTransferTx(stateArray, txType, txHash, blockHeight, blockTime, indexInBlock, gasConsumed, indexInTx, confirmFlag, contractAddress, payer, calledContractHash);
        }
    }

    private void handleUnauditedOrcTransferTx(String evmStates, int txType, String txHash, int blockHeight, int blockTime, int indexInBlock, BigDecimal gasConsumed, int indexInTx, int confirmFlag, String contractAddress, String payer, String calledContractHash) {
        try {
            evmStates = Numeric.cleanHexPrefix(evmStates);
            ByteArrayInputStream bais = new ByteArrayInputStream(Helper.hexToBytes(evmStates));
            BinaryReader reader = new BinaryReader(bais);
            reader.readBytes(20);

            int length = reader.readInt();
            byte[] eventBytes = reader.readBytes(32);
            String eventHash = Helper.toHexString(eventBytes);
            if (ConstantParam.TRANSFER_TX.equalsIgnoreCase(eventHash)) {
                String txAction = EventTypeEnum.Transfer.des();
                Integer eventType = EventTypeEnum.Transfer.type();
                if (length == 3) {
                    // orc20
                    Map<String, Object> result = parseUnAuditedOrc20Event(length, reader, contractAddress);
                    String fromAddress = (String) result.get("fromAddress");
                    String toAddress = (String) result.get("toAddress");
                    BigDecimal amount = (BigDecimal) result.get("amount");
                    String contractHash = (String) result.get("contractHash");
                    String assetName = (String) result.get("assetName");

                    TxDetail txDetail = generateTransaction(fromAddress, toAddress, assetName, amount, txType, txHash, blockHeight, blockTime, indexInBlock, confirmFlag, txAction, gasConsumed, indexInTx, eventType, contractHash, payer, calledContractHash);
                    ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
                    ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));
                    ConstantParam.BATCHBLOCKDTO.getOrc20TxDetails().add(TxDetail.toOrc20TxDetail(txDetail));
                } else if (length == 4) {
                    // orc721
                    Map<String, Object> result = parseUnAuditedOrc721Event(length, reader, contractAddress);
                    String fromAddress = (String) result.get("fromAddress");
                    String toAddress = (String) result.get("toAddress");
                    String tokenId = (String) result.get("tokenId");
                    String contractHash = (String) result.get("contractHash");
                    String assetName = (String) result.get("assetName");

                    TxDetail txDetail = generateTransaction(fromAddress, toAddress, assetName, BigDecimal.ONE, txType, txHash, blockHeight, blockTime, indexInBlock, confirmFlag, txAction, gasConsumed, indexInTx, eventType, contractHash, payer, calledContractHash);
                    ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
                    ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));
                    ConstantParam.BATCHBLOCKDTO.getOrc721TxDetails().add(TxDetail.toOrc721TxDetail(txDetail, tokenId));
                }

            } else if (ConstantParam.Approval_TX.equalsIgnoreCase(eventHash)) {
                String txAction = EventTypeEnum.Approval.des();
                Integer eventType = EventTypeEnum.Approval.type();
                if (length == 3) {
                    // orc20
                    Map<String, Object> result = parseUnAuditedOrc20Event(length, reader, contractAddress);
                    String fromAddress = (String) result.get("fromAddress");
                    String toAddress = (String) result.get("toAddress");
                    BigDecimal amount = (BigDecimal) result.get("amount");
                    String contractHash = (String) result.get("contractHash");
                    String assetName = (String) result.get("assetName");

                    TxDetail txDetail = generateTransaction(fromAddress, toAddress, assetName, amount, txType, txHash, blockHeight, blockTime, indexInBlock, confirmFlag, txAction, gasConsumed, indexInTx, eventType, contractHash, payer, calledContractHash);
                    ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
                    ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));
                    ConstantParam.BATCHBLOCKDTO.getOrc20TxDetails().add(TxDetail.toOrc20TxDetail(txDetail));
                } else if (length == 4) {
                    // orc721
                    Map<String, Object> result = parseUnAuditedOrc721Event(length, reader, contractAddress);
                    String fromAddress = (String) result.get("fromAddress");
                    String toAddress = (String) result.get("toAddress");
                    String tokenId = (String) result.get("tokenId");
                    String contractHash = (String) result.get("contractHash");
                    String assetName = (String) result.get("assetName");

                    TxDetail txDetail = generateTransaction(fromAddress, toAddress, assetName, BigDecimal.ONE, txType, txHash, blockHeight, blockTime, indexInBlock, confirmFlag, txAction, gasConsumed, indexInTx, eventType, contractHash, payer, calledContractHash);
                    ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
                    ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));
                    ConstantParam.BATCHBLOCKDTO.getOrc721TxDetails().add(TxDetail.toOrc721TxDetail(txDetail, tokenId));
                }
            } else if (ConstantParam.TRANSFER_SINGLE_TX.equalsIgnoreCase(eventHash) || ConstantParam.TRANSFER_BATCH_TX.equalsIgnoreCase(eventHash)) {
                String txAction = EventTypeEnum.Transfer.des();
                Integer eventType = EventTypeEnum.Transfer.type();
                Map<String, Object> result = parseUnAuditedOrc1155Event(length, reader, contractAddress);
                String fromAddress = (String) result.get("fromAddress");
                String toAddress = (String) result.get("toAddress");
                Object tokenIds = result.get("tokenIds");
                Object amounts = result.get("amounts");
                String contractHash = (String) result.get("contractHash");
                String assetName = (String) result.get("assetName");

                if (tokenIds instanceof String) {
                    String tokenId = (String) tokenIds;
                    BigDecimal amount = (BigDecimal) amounts;
                    TxDetail txDetail = generateTransaction(fromAddress, toAddress, assetName, amount, txType, txHash, blockHeight, blockTime, indexInBlock, confirmFlag, txAction, gasConsumed, indexInTx, eventType, contractHash, payer, calledContractHash);
                    ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
                    ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));
                    ConstantParam.BATCHBLOCKDTO.getOrc1155TxDetails().add(TxDetail.toOrc1155TxDetail(txDetail, tokenId));
                } else if (tokenIds instanceof List) {
                    int replaceIndexInTx = indexInTx;
                    for (int i = 0; i < ((List<?>) tokenIds).size(); i++) {
                        String tokenId = (String) ((List<?>) tokenIds).get(i);
                        BigDecimal amount = (BigDecimal) ((List<?>) amounts).get(i);
                        if (i > 0) {
                            replaceIndexInTx = indexInTx + 1000 + i;
                        }
                        TxDetail txDetail = generateTransaction(fromAddress, toAddress, assetName, amount, txType, txHash, blockHeight, blockTime, indexInBlock, confirmFlag, txAction, gasConsumed, replaceIndexInTx, eventType, contractHash, payer, calledContractHash);
                        ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
                        ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));
                        ConstantParam.BATCHBLOCKDTO.getOrc1155TxDetails().add(TxDetail.toOrc1155TxDetail(txDetail, tokenId));
                    }
                }
            } else {
                // other
                TxDetail txDetail = generateTransaction(ConstantParam.EMPTY, ConstantParam.EMPTY, ConstantParam.EMPTY, ConstantParam.ZERO, txType, txHash, blockHeight,
                        blockTime, indexInBlock, confirmFlag, ConstantParam.EMPTY, gasConsumed, indexInTx, EventTypeEnum.Others.type(), contractAddress, payer, calledContractHash);
                ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
                ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));
            }
        } catch (Exception e) {
            log.error("handleUnAuditedOrcTransferTx error", e);
            TxDetail txDetail = generateTransaction(ConstantParam.EMPTY, ConstantParam.EMPTY, ConstantParam.EMPTY, ConstantParam.ZERO, txType, txHash, blockHeight,
                    blockTime, indexInBlock, confirmFlag, ConstantParam.EMPTY, gasConsumed, indexInTx, EventTypeEnum.Others.type(), contractAddress, payer, calledContractHash);
            ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
            ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));
        }
    }

    private Map<String, Object> parseUnAuditedOrc20Event(int length, BinaryReader reader, String contractAddress) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length - 1; i++) {
            byte[] TopicBytes = reader.readBytes(32);
            String topic = Helper.toHexString(TopicBytes);
            stringBuilder.append(topic);
        }
        byte[] dataBytes = reader.readVarBytes();
        String data = Helper.toHexString(dataBytes);
        String parseData = stringBuilder.append(data).toString();
        List<Type> types = handleEvmOrc20Orc721TransferResults(parseData);
        String fromAddress = types.get(0).getValue().toString();
        String toAddress = types.get(1).getValue().toString();
        BigDecimal amountValue = new BigDecimal((BigInteger) types.get(2).getValue());

        String contractHash = ConstantParam.EVM_ADDRESS_PREFIX + Helper.reverse(contractAddress);
        String assetName = web3jSdkUtil.getOrcTokenSymbol(contractHash);
        Integer decimals = web3jSdkUtil.getOrc20Decimals(contractHash);
        BigDecimal amount = amountValue.divide(BigDecimal.TEN.pow(decimals), decimals, RoundingMode.DOWN);
        if (ConstantParam.MAX_APPROVAL_AMOUNT.compareTo(amount) <= 0 || BigDecimal.ZERO.compareTo(amount) > 0) {
            amount = ConstantParam.MAX_APPROVAL_AMOUNT;
        }
        Map<String, Object> result = new HashMap<>();
        result.put("fromAddress", fromAddress);
        result.put("toAddress", toAddress);
        result.put("amount", amount);
        result.put("contractHash", contractHash);
        result.put("assetName", assetName);
        return result;
    }

    private Map<String, Object> parseUnAuditedOrc721Event(int length, BinaryReader reader, String contractAddress) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length - 1; i++) {
            byte[] TopicBytes = reader.readBytes(32);
            String topic = Helper.toHexString(TopicBytes);
            stringBuilder.append(topic);
        }
        String parseData = stringBuilder.toString();
        List<Type> types = handleEvmOrc20Orc721TransferResults(parseData);
        String fromAddress = types.get(0).getValue().toString();
        String toAddress = types.get(1).getValue().toString();
        String tokenId = types.get(2).getValue().toString();

        String contractHash = ConstantParam.EVM_ADDRESS_PREFIX + Helper.reverse(contractAddress);
        String assetName = web3jSdkUtil.getOrcTokenSymbol(contractHash);
        Map<String, Object> result = new HashMap<>();
        result.put("fromAddress", fromAddress);
        result.put("toAddress", toAddress);
        result.put("tokenId", tokenId);
        result.put("contractHash", contractHash);
        result.put("assetName", assetName);
        return result;
    }

    private Map<String, Object> parseUnAuditedOrc1155Event(int length, BinaryReader reader, String contractAddress) throws IOException {
        Map<String, Object> result = new HashMap<>();
        byte[] operatorBytes = reader.readBytes(32);
        String operatorAddress = Helper.toHexString(operatorBytes);
        byte[] fromBytes = reader.readBytes(32);
        String fromAddress = Helper.toHexString(fromBytes);
        byte[] toBytes = reader.readBytes(32);
        String toAddress = Helper.toHexString(toBytes);
        List<String> tokenIds = new ArrayList<>();
        List<BigDecimal> amounts = new ArrayList<>();
        byte[] dataBytes = reader.readVarBytes();
        String data = Helper.toHexString(dataBytes);
        if (data.length() > 128) {
            // transferBatch
            fromAddress = ConstantParam.EVM_ADDRESS_PREFIX + fromAddress.substring(24);
            toAddress = ConstantParam.EVM_ADDRESS_PREFIX + toAddress.substring(24);
            List<Type> types = handleOrc1155BatchTransferResults(data);
            List<Uint256> ids = (List<Uint256>) types.get(0).getValue();
            List<Uint256> values = (List<Uint256>) types.get(1).getValue();
            for (int i = 0; i < ids.size(); i++) {
                String tokenId = ids.get(i).getValue().toString();
                BigDecimal amount = new BigDecimal(values.get(i).getValue());
                if (ConstantParam.MAX_APPROVAL_AMOUNT.compareTo(amount) <= 0) {
                    amount = ConstantParam.MAX_APPROVAL_AMOUNT;
                }
                tokenIds.add(tokenId);
                amounts.add(amount);
            }
            result.put("tokenIds", tokenIds);
            result.put("amounts", amounts);
        } else {
            // transferSingle
            String parseData = operatorAddress + fromAddress + toAddress + data;
            List<Type> types = handleOrc1155TransferResults(parseData);
            fromAddress = types.get(1).getValue().toString();
            toAddress = types.get(2).getValue().toString();
            String tokenId = types.get(3).getValue().toString();
            BigDecimal amount = new BigDecimal((BigInteger) types.get(4).getValue());
            if (ConstantParam.MAX_APPROVAL_AMOUNT.compareTo(amount) <= 0) {
                amount = ConstantParam.MAX_APPROVAL_AMOUNT;
            }
            result.put("tokenIds", tokenId);
            result.put("amounts", amount);
        }

        String contractHash = ConstantParam.EVM_ADDRESS_PREFIX + Helper.reverse(contractAddress);
        String assetName = web3jSdkUtil.getOrcTokenSymbol(contractHash);
        result.put("fromAddress", fromAddress);
        result.put("toAddress", toAddress);
        result.put("contractHash", contractHash);
        result.put("assetName", assetName);
        return result;
    }

    private void handleUnauditedOepTransferTx(JSONArray stateArray, int txType, String txHash, int blockHeight, int blockTime, int indexInBlock, BigDecimal gasConsumed, int indexInTx, int confirmFlag, String contractHash, String payer, String calledContractHash) {
        try {
            Boolean needParse = Boolean.FALSE;
            Boolean isWasm = Boolean.FALSE;
            String txAction = ConstantParam.EMPTY;
            int eventType = EventTypeEnum.Others.type();
            int size = stateArray.size();
            String action = stateArray.getString(0);
            if (ConstantParam.NEOVM_TRANSFER_ACTION.equals(action)) {
                needParse = Boolean.TRUE;
                txAction = EventTypeEnum.Transfer.des();
                eventType = EventTypeEnum.Transfer.type();
            } else if (EventTypeEnum.Transfer.des().equalsIgnoreCase(action)) {
                isWasm = Boolean.TRUE;
                needParse = Boolean.TRUE;
                txAction = EventTypeEnum.Transfer.des();
                eventType = EventTypeEnum.Transfer.type();
            } else if (ConstantParam.NEOVM_APPROVAL_ACTION.equals(action)) {
                needParse = Boolean.TRUE;
                txAction = EventTypeEnum.Approval.des();
                eventType = EventTypeEnum.Approval.type();
            } else if (EventTypeEnum.Approval.des().equalsIgnoreCase(action)) {
                isWasm = Boolean.TRUE;
                needParse = Boolean.TRUE;
                txAction = EventTypeEnum.Approval.des();
                eventType = EventTypeEnum.Approval.type();
            }
            if (needParse) {
                String assetName;
                String fromAddress = stateArray.getString(1);
                String toAddress = stateArray.getString(2);
                if (!isWasm) {
                    try {
                        fromAddress = Address.parse(fromAddress).toBase58();
                    } catch (Exception e) {
                        log.warn("Address.parse(fromAddress).toBase58() error:{}", fromAddress);
                    }
                    try {
                        toAddress = Address.parse(toAddress).toBase58();
                    } catch (Exception e) {
                        log.warn("Address.parse(toAddress).toBase58() error:{}", fromAddress);
                    }
                }
                if (size == 4) {
                    assetName = commonService.getOepSymbol(isWasm, contractHash, null);
                    Integer decimals = commonService.getOep4Decimals(isWasm, contractHash);
                    if (decimals == null) {
                        // oep5
                        String tokenId = stateArray.getString(3);
                        BigDecimal amount = BigDecimal.ONE;
                        assetName = assetName + tokenId;
                        TxDetail txDetail = generateTransaction(fromAddress, toAddress, assetName, amount, txType, txHash, blockHeight,
                                blockTime, indexInBlock, confirmFlag, txAction, gasConsumed, indexInTx, eventType, contractHash, payer, calledContractHash);
                        ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
                        ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));
                        ConstantParam.BATCHBLOCKDTO.getOep5TxDetails().add(TxDetail.toOep5TxDetail(txDetail));
                    } else {
                        // oep4
                        BigDecimal eventAmount = BigDecimalFromNeoVmData(stateArray.getString(3));
                        BigDecimal amount = eventAmount.divide(BigDecimal.TEN.pow(decimals), decimals, RoundingMode.DOWN);
                        if (ConstantParam.MAX_APPROVAL_AMOUNT.compareTo(amount) <= 0 || BigDecimal.ZERO.compareTo(amount) > 0) {
                            amount = ConstantParam.MAX_APPROVAL_AMOUNT;
                        }
                        TxDetail txDetail = generateTransaction(fromAddress, toAddress, assetName, amount, txType, txHash, blockHeight,
                                blockTime, indexInBlock, confirmFlag, txAction, gasConsumed, indexInTx, eventType, contractHash, payer, calledContractHash);
                        ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
                        ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));
                        ConstantParam.BATCHBLOCKDTO.getOep4TxDetails().add(TxDetail.toOep4TxDetail(txDetail));
                    }
                } else if (size == 5) {
                    // oep8
                    String tokenId = stateArray.getString(3);
                    assetName = commonService.getOepSymbol(isWasm, contractHash, tokenId);
                    BigDecimal amount = BigDecimalFromNeoVmData(stateArray.getString(4));
                    if (ConstantParam.MAX_APPROVAL_AMOUNT.compareTo(amount) <= 0) {
                        amount = ConstantParam.MAX_APPROVAL_AMOUNT;
                    }
                    TxDetail txDetail = generateTransaction(fromAddress, toAddress, assetName, amount, txType, txHash, blockHeight,
                            blockTime, indexInBlock, confirmFlag, txAction, gasConsumed, indexInTx, eventType, contractHash, payer, calledContractHash);
                    ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
                    ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));
                    ConstantParam.BATCHBLOCKDTO.getOep8TxDetails().add(TxDetail.toOep8TxDetail(txDetail));
                } else {
                    // other
                    TxDetail txDetail = generateTransaction(ConstantParam.EMPTY, ConstantParam.EMPTY, ConstantParam.EMPTY, ConstantParam.ZERO, txType, txHash, blockHeight,
                            blockTime, indexInBlock, confirmFlag, ConstantParam.EMPTY, gasConsumed, indexInTx, EventTypeEnum.Others.type(), contractHash, payer, calledContractHash);
                    ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
                    ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));
                }
            } else {
                // other
                TxDetail txDetail = generateTransaction(ConstantParam.EMPTY, ConstantParam.EMPTY, ConstantParam.EMPTY, ConstantParam.ZERO, txType, txHash, blockHeight,
                        blockTime, indexInBlock, confirmFlag, ConstantParam.EMPTY, gasConsumed, indexInTx, EventTypeEnum.Others.type(), contractHash, payer, calledContractHash);
                ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
                ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));
            }
        } catch (Exception e) {
            log.error("handleUnauditedOepTransferTx error", e);
            TxDetail txDetail = generateTransaction(ConstantParam.EMPTY, ConstantParam.EMPTY, ConstantParam.EMPTY, ConstantParam.ZERO, txType, txHash, blockHeight,
                    blockTime, indexInBlock, confirmFlag, ConstantParam.EMPTY, gasConsumed, indexInTx, EventTypeEnum.Others.type(), contractHash, payer, calledContractHash);
            ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
            ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));
        }
    }

    private BigDecimal BigDecimalFromNeoVmData(String value) {
        try {
            byte[] bytes = Helper.hexToBytes(value);
            return new BigDecimal(Helper.BigIntFromNeoBytes(bytes).toString());
        } catch (Exception e) {
            return new BigDecimal(value);
        }

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
        return TxDetail.builder()
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
    }

    /**
     * 处理交易基本信息
     *
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
    // 首先插入 tx_detail 大表中
    private void insertTxBasicInfo(int txType, String txHash, int blockHeight, int blockTime,
                                   int indexInBlock, int confirmFlag, String action, BigDecimal gasConsumed, int indexInTx,
                                   int eventType, String contractAddress, String payer, String calledContractHash) throws Exception {

        TxDetail txDetail = generateTransaction("", "", "", ConstantParam.ZERO, txType, txHash, blockHeight,
                blockTime, indexInBlock, confirmFlag, action, gasConsumed, indexInTx, eventType, contractAddress, payer, calledContractHash);

        ConstantParam.BATCHBLOCKDTO.getTxDetails().add(txDetail);
        ConstantParam.BATCHBLOCKDTO.getTxDetailDailys().add(TxDetail.toTxDetailDaily(txDetail));

        // OEP合约交易插入对应的子表
        if (IS_OEPTX_FLAG.get().get(ConstantParam.IS_OEP8TX)) {
            ConstantParam.BATCHBLOCKDTO.getOep8TxDetails().add(TxDetail.toOep8TxDetail(txDetail));
        } else if (IS_OEPTX_FLAG.get().get(ConstantParam.IS_OEP5TX)) {
            ConstantParam.BATCHBLOCKDTO.getOep5TxDetails().add(TxDetail.toOep5TxDetail(txDetail));
        } else if (IS_OEPTX_FLAG.get().get(ConstantParam.IS_OEP4TX)) {
            ConstantParam.BATCHBLOCKDTO.getOep4TxDetails().add(TxDetail.toOep4TxDetail(txDetail));
        } else if (IS_OEPTX_FLAG.get().get(ConstantParam.IS_ORC20TX)) {
            ConstantParam.BATCHBLOCKDTO.getOrc20TxDetails().add(TxDetail.toOrc20TxDetail(txDetail));
        } else if (IS_OEPTX_FLAG.get().get(ConstantParam.IS_ORC721TX)) {
            ConstantParam.BATCHBLOCKDTO.getOrc721TxDetails().add(TxDetail.toOrc721TxDetail(txDetail, ConstantParam.EMPTY));
        } else if (IS_OEPTX_FLAG.get().get(ConstantParam.IS_ORC1155TX)) {
            ConstantParam.BATCHBLOCKDTO.getOrc1155TxDetails().add(TxDetail.toOrc1155TxDetail(txDetail, ConstantParam.EMPTY));
        }

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
