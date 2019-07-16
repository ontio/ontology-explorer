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
import com.github.ontio.config.ParamsConfig;
import com.github.ontio.mapper.*;
import com.github.ontio.model.common.PageResponseBean;
import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.model.dto.*;
import com.github.ontio.service.IContractService;
import com.github.ontio.util.ConstantParam;
import com.github.ontio.util.ErrorInfo;
import com.github.ontio.util.Helper;
import com.github.ontio.util.OntologySDKService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service("ContractService")
public class ContractServiceImpl implements IContractService {

    private final ContractMapper contractMapper;
    private final Oep4TxDetailMapper oep4TxDetailMapper;
    private final Oep5TxDetailMapper oep5TxDetailMapper;
    private final Oep8TxDetailMapper oep8TxDetailMapper;
    private final TxDetailMapper txDetailMapper;
    private final TxEventLogMapper txEventLogMapper;
    private final ParamsConfig paramsConfig;
    private final NodeInfoOffChainMapper nodeInfoOffChainMapper;

    @Autowired
    public ContractServiceImpl(ContractMapper contractMapper, Oep4TxDetailMapper oep4TxDetailMapper, Oep5TxDetailMapper oep5TxDetailMapper, Oep8TxDetailMapper oep8TxDetailMapper, TxDetailMapper txDetailMapper, TxEventLogMapper txEventLogMapper, ParamsConfig paramsConfig, NodeInfoOffChainMapper nodeInfoOffChainMapper) {
        this.contractMapper = contractMapper;
        this.oep4TxDetailMapper = oep4TxDetailMapper;
        this.oep5TxDetailMapper = oep5TxDetailMapper;
        this.oep8TxDetailMapper = oep8TxDetailMapper;
        this.txDetailMapper = txDetailMapper;
        this.paramsConfig = paramsConfig;
        this.txEventLogMapper = txEventLogMapper;
        this.nodeInfoOffChainMapper = nodeInfoOffChainMapper;
    }

    private OntologySDKService sdk;

    private synchronized void initSDK() {
        if (sdk == null) {
            sdk = OntologySDKService.getInstance(paramsConfig);
        }
    }

    @Override
    public ResponseBean queryContractsByPage(Integer pageSize, Integer pageNumber) {

        int start = pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1);

        List<ContractDto> contractDtos = contractMapper.selectApprovedContract(start, pageSize);
        int count = contractMapper.selectApprovedContractCount();

        PageResponseBean pageResponseBean = new PageResponseBean(contractDtos, count);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), pageResponseBean);
    }


    @Override
    public ResponseBean queryContractDetail(String contractHash) {

        ContractDto contractDto = contractMapper.selectContractDetail(contractHash);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), contractDto);
    }


    @Override
    public ResponseBean queryTxsByContractHash(String contractType, String contractHash, Integer pageNumber, Integer pageSize) {

        List<TxDetailDto> txDetailDtos = new ArrayList<>();
        Integer count = 0;
        PageResponseBean pageResponseBean = new PageResponseBean(txDetailDtos, count);

        int start = pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1);

        switch (contractType.toLowerCase()) {
            case ConstantParam.CONTRACT_TYPE_OEP4:
                txDetailDtos = oep4TxDetailMapper.selectTxsByCalledContractHash(contractHash, start, pageSize);
                count = oep4TxDetailMapper.selectCountByCalledContracthash(contractHash);
                pageResponseBean = new PageResponseBean(txDetailDtos, count);
                break;
            case ConstantParam.CONTRACT_TYPE_OEP5:
                //TODO 云斗龙特殊查询，多asset_name,json_url字段。后续oep5需要统一规范
                if (paramsConfig.OEP5_DRAGON_CONTRACTHASH.equals(contractHash)) {
                    List<Oep5TxDetailDto> oep5TxDetailDtos = oep5TxDetailMapper.selectTxs4Dragon(contractHash, start, pageSize);
                    count = oep5TxDetailMapper.selectCountByCalledContracthash(contractHash);
                    pageResponseBean = new PageResponseBean(oep5TxDetailDtos, count);
                } else {
                    txDetailDtos = oep5TxDetailMapper.selectTxsByCalledContractHash(contractHash, start, pageSize);
                    count = oep5TxDetailMapper.selectCountByCalledContracthash(contractHash);
                    pageResponseBean = new PageResponseBean(txDetailDtos, count);
                }
                break;
            case ConstantParam.CONTRACT_TYPE_OEP8:
                txDetailDtos = oep8TxDetailMapper.selectTxsByCalledContractHashAndTokenName(contractHash, "", start, pageSize);
                count = oep8TxDetailMapper.selectCountByCalledContracthashAndTokenName(contractHash, "");
                pageResponseBean = new PageResponseBean(txDetailDtos, count);
                break;
            case ConstantParam.CONTRACT_TYPE_OTHER:
                List<TxEventLogDto> txEventLogDtos = txEventLogMapper.selectTxsByCalledContractHash(contractHash, start, pageSize);
                count = txEventLogMapper.selectCountByCalledContracthash(contractHash);
                pageResponseBean = new PageResponseBean(txEventLogDtos, count);
                break;
        }

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), pageResponseBean);
    }

    @Override
    public ResponseBean queryTxsByContractHash(String contractHash, Integer pageNumber, Integer pageSize) {

        ContractDto contractDto = contractMapper.selectContractDetail(contractHash);
        String contractType = contractDto.getType();
        if (Helper.isEmptyOrNull(contractType)) {
            contractType = ConstantParam.CONTRACT_TYPE_OTHER;
        }
        List<TxDetailDto> txDetailDtos = new ArrayList<>();
        Integer count = 0;
        PageResponseBean pageResponseBean = new PageResponseBean(txDetailDtos, count);

        int start = pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1);

        switch (contractType.toLowerCase()) {
            case ConstantParam.CONTRACT_TYPE_OEP4:
                txDetailDtos = oep4TxDetailMapper.selectTxsByCalledContractHash(contractHash, start, pageSize);
                count = oep4TxDetailMapper.selectCountByCalledContracthash(contractHash);
                pageResponseBean = new PageResponseBean(txDetailDtos, count);
                break;
            case ConstantParam.CONTRACT_TYPE_OEP5:
                //TODO 云斗龙特殊查询，多asset_name,json_url字段。后续oep5需要统一规范
                if (paramsConfig.OEP5_DRAGON_CONTRACTHASH.equals(contractHash)) {
                    List<Oep5TxDetailDto> oep5TxDetailDtos = oep5TxDetailMapper.selectTxs4Dragon(contractHash, start, pageSize);
                    count = oep5TxDetailMapper.selectCountByCalledContracthash(contractHash);
                    pageResponseBean = new PageResponseBean(oep5TxDetailDtos, count);
                } else {
                    txDetailDtos = oep5TxDetailMapper.selectTxsByCalledContractHash(contractHash, start, pageSize);
                    count = oep5TxDetailMapper.selectCountByCalledContracthash(contractHash);
                    pageResponseBean = new PageResponseBean(txDetailDtos, count);
                }
                break;
            case ConstantParam.CONTRACT_TYPE_OEP8:
                txDetailDtos = oep8TxDetailMapper.selectTxsByCalledContractHashAndTokenName(contractHash, "", start, pageSize);
                count = oep8TxDetailMapper.selectCountByCalledContracthashAndTokenName(contractHash, "");
                pageResponseBean = new PageResponseBean(txDetailDtos, count);
                break;
            case ConstantParam.CONTRACT_TYPE_OTHER:
                List<TxEventLogDto> txEventLogDtos = txEventLogMapper.selectTxsByCalledContractHash(contractHash, start, pageSize);
                count = txEventLogMapper.selectCountByCalledContracthash(contractHash);
                pageResponseBean = new PageResponseBean(txEventLogDtos, count);
                break;
        }

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), pageResponseBean);
    }

    @Override
    public ResponseBean queryDappBindedInfo(String dappNameArrayStr, long startTime, long endTime) {

        initSDK();

        Map<String, JSONObject> dappInfoMap = new HashMap<>();

        String[] dappNameArray = dappNameArrayStr.split(",");
        if (dappNameArray.length > 5) {
            return new ResponseBean(ErrorInfo.REQ_NUMBER_RANGE_EXCEED.code(), ErrorInfo.REQ_NUMBER_RANGE_EXCEED.desc(), false);
        }
        List<Map<String, String>> dappList = contractMapper.selectContractHashByDappName(Arrays.asList(dappNameArray));
        dappList.forEach(item -> {
            String name = item.get("dappName");
            String contractHash = item.get("contractHash");
            JSONArray contractHashArray = new JSONArray();
            if (dappInfoMap.containsKey(name)) {
                JSONObject object = dappInfoMap.get(name);
                contractHashArray = object.getJSONArray("contract_hashs");
                contractHashArray.add(contractHash);

            } else {
                contractHashArray.add(contractHash);
                JSONObject object = new JSONObject();
                object.put("contract_hashs", contractHashArray);
                dappInfoMap.put(name, object);
            }
        });

        for (String key :
                dappInfoMap.keySet()) {
            JSONObject valueObject = dappInfoMap.get(key);
            JSONArray contractHashArray = valueObject.getJSONArray("contract_hashs");
            BigDecimal fee = txEventLogMapper.queryTxFeeByParam(contractHashArray, startTime, endTime);
            Integer txCount = txEventLogMapper.queryTxCountByParam(contractHashArray, startTime, endTime);
            valueObject.put("tx_count", txCount);
            valueObject.put("total_fee", fee);
            for (Object obj :
                    contractHashArray) {
                String contractHash = (String) obj;
                valueObject.put(contractHash, new HashMap<>());

                Map bindedNodeInfo = sdk.getDappBindedNodeInfo(paramsConfig.DAPPBIND_CONTRACTHASH, contractHash);
                Map bindedOntidAndAccount = sdk.getDappBindedOntidAndAccount(paramsConfig.DAPPBIND_CONTRACTHASH, contractHash);

                Map map = (Map) valueObject.get(contractHash);
                map.putAll(bindedNodeInfo);
                map.putAll(bindedOntidAndAccount);
            }
        }

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), dappInfoMap);
    }


    @Override
    public ResponseBean queryBindedWalletDappInfo(long startTime, long endTime) {

        initSDK();

        Map<String, JSONObject> dappInfoMap = new HashMap<>();
        //查询所有dapp_store的合约
        List<Map<String, String>> dappList = contractMapper.selectContractHashByDappName(new ArrayList<>());
        dappList.forEach(item -> {
            String name = item.get("dappName");
            String contractHash = item.get("contractHash");
            JSONArray contractHashArray = new JSONArray();
            if (dappInfoMap.containsKey(name)) {
                JSONObject object = dappInfoMap.get(name);
                contractHashArray = object.getJSONArray("contract_hashs");
                contractHashArray.add(contractHash);

            } else {
                contractHashArray.add(contractHash);
                JSONObject object = new JSONObject();
                object.put("contract_hashs", contractHashArray);
                dappInfoMap.put(name, object);
            }
        });

        for (String dappName :
                dappInfoMap.keySet()) {
            JSONObject valueObject = dappInfoMap.get(dappName);
            JSONArray contractHashArray = valueObject.getJSONArray("contract_hashs");
            BigDecimal totalFee = txEventLogMapper.queryTxFeeByParam(contractHashArray, startTime, endTime);
            //Integer txCount = txEventLogMapper.queryTxCountByParam(contractHashArray, startTime, endTime);
            //总手续费
            String totalFeeStr = (totalFee == null ? ConstantParam.ZERO.stripTrailingZeros().toPlainString() :
                    totalFee.multiply(ConstantParam.ONG_TOTAL).stripTrailingZeros().toPlainString());
            //激励手续费
            String rewardFeeStr = (totalFee == null ? ConstantParam.ZERO.stripTrailingZeros().toPlainString() :
                    totalFee.multiply(ConstantParam.ONG_TOTAL).multiply(new BigDecimal(paramsConfig.DAPP_REWARD_PERCENTAGE).divide(new BigDecimal("100"))).stripTrailingZeros().toPlainString());

            //valueObject.put("tx_count", txCount);
            valueObject.put("total_fee", totalFeeStr);
            valueObject.put("reward_fee", rewardFeeStr);
            JSONArray walletArray = new JSONArray();
            //根据每个合约hash查询绑定信息
            contractHashArray.forEach(item -> {
                String contractHash = (String) item;
                // 从合约查询绑定钱包信息
                Map bindedOntidAndAccount = sdk.getDappBindedOntidAndAccount(paramsConfig.DAPPBIND_CONTRACTHASH, contractHash);
                if (Helper.isNotEmptyOrNull(bindedOntidAndAccount.get("receive_account"))) {
                    walletArray.add(bindedOntidAndAccount.get("receive_account"));
                }
            });
            valueObject.put("wallets", walletArray);
        }

        List<Map> rslist = new ArrayList<>();
        for (String dappName :
                dappInfoMap.keySet()) {
            //只返回绑定了钱包的dapp信息
            JSONObject valueObject = dappInfoMap.get(dappName);
            JSONArray walletArray = valueObject.getJSONArray("wallets");
            if (walletArray.size() > 0) {
                Map map = new HashMap();
                map.put("dapp_name", dappName);
                map.put("total_fee", valueObject.getString("total_fee"));
                map.put("reward_fee", valueObject.getString("reward_fee"));
                map.put("wallets", walletArray);
                //map.put("tx_count", valueObject.getInteger("tx_count"));
                map.put("contract_hashs", valueObject.getJSONArray("contract_hashs"));
                rslist.add(map);
            }
        }

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), rslist);
    }


    @Override
    public ResponseBean queryBindedNodeDappInfo(long startTime, long endTime) {

        initSDK();

        Map<String, JSONArray> dappContractHashMap = new HashMap<>();
        //查询所有dapp_store的合约
        List<Map<String, String>> dappList = contractMapper.selectContractHashByDappName(new ArrayList<>());
        dappList.forEach(item -> {
            String name = item.get("dappName");
            String contractHash = item.get("contractHash");
            if (dappContractHashMap.containsKey(name)) {
                JSONArray contractHashArray = dappContractHashMap.get(name);
                contractHashArray.add(contractHash);
            } else {
                JSONArray contractHashArray = new JSONArray();
                contractHashArray.add(contractHash);
                dappContractHashMap.put(name, contractHashArray);
            }
        });

        List<Map> list = new ArrayList<>();
        for (String dappName :
                dappContractHashMap.keySet()) {
            JSONArray contractHashArray = dappContractHashMap.get(dappName);
            BigDecimal totalFee = txEventLogMapper.queryTxFeeByParam(contractHashArray, startTime, endTime);

            //总手续费
            String totalFeeStr = (totalFee == null ? ConstantParam.ZERO.stripTrailingZeros().toPlainString() :
                    totalFee.multiply(ConstantParam.ONG_TOTAL).stripTrailingZeros().toPlainString());
            //激励手续费
            String rewardFeeStr = (totalFee == null ? ConstantParam.ZERO.stripTrailingZeros().toPlainString() :
                    totalFee.multiply(ConstantParam.ONG_TOTAL).multiply(new BigDecimal(paramsConfig.NODE_REWARD_PERCENTAGE).divide(new BigDecimal("100"))).stripTrailingZeros().toPlainString());

            Map<String, Object> map = new HashMap<>();
            map.put("dapp_name", dappName);
            map.put("total_fee", totalFeeStr);
            map.put("reward_fee", rewardFeeStr);

            contractHashArray.forEach(item -> {
                String contractHash = (String) item;
                // 从合约查询绑定节点信息
                Map bindedNodeInfo = sdk.getDappBindedNodeInfo(paramsConfig.DAPPBIND_CONTRACTHASH, contractHash);
                if (Helper.isNotEmptyOrNull(bindedNodeInfo.get("node_pubkey")) && Helper.isNotEmptyOrNull(bindedNodeInfo.get("node_name"))) {
                    map.put("node_pubkey", bindedNodeInfo.get("node_pubkey"));
                    map.put("node_name", bindedNodeInfo.get("node_name"));
                }
            });
            //只返回绑定了节点的dapp信息
            if (map.containsKey("node_pubkey") && map.containsKey("node_name")) {
                list.add(map);
            }
        }

        Map<String, Object> rsMap = new HashMap<>();
        for (Map map :
                list) {
            String nodeName = (String) map.get("node_name");
            if (rsMap.containsKey(nodeName)) {

                JSONObject obj = (JSONObject) rsMap.get(nodeName);
                JSONArray dappNameArray = obj.getJSONArray("binded_dapp_names");
                dappNameArray.add(map.get("dapp_name"));

                //总手续费和激励手续费累加
                BigDecimal totalFee = new BigDecimal(obj.getString("total_fee"));
                totalFee = totalFee.add(new BigDecimal((String) map.get("total_fee")));
                obj.put("total_fee", totalFee.stripTrailingZeros().toPlainString());

                BigDecimal rewardFee = new BigDecimal(obj.getString("reward_fee"));
                rewardFee = rewardFee.add(new BigDecimal((String) map.get("reward_fee")));
                obj.put("reward_fee", rewardFee.stripTrailingZeros().toPlainString());

            } else {
                JSONObject obj = new JSONObject();
                obj.put("total_fee", map.get("total_fee"));
                obj.put("reward_fee", map.get("reward_fee"));
                obj.put("node_pubkey", map.get("node_pubkey"));
                //根据公钥查节点钱包地址
                NodeInfoOffChainDto nodeInfoOffChainDto = nodeInfoOffChainMapper.selectByPublicKey((String) map.get("node_pubkey"));
                if(Helper.isNotEmptyOrNull(nodeInfoOffChainDto)){
                    obj.put("node_wallet", nodeInfoOffChainDto.getAddress());
                }else {
                    obj.put("node_wallet", "");
                }

                JSONArray dappNameArray = new JSONArray();
                dappNameArray.add(map.get("dapp_name"));
                obj.put("binded_dapp_names", dappNameArray);

                rsMap.put(nodeName, obj);
            }
        }

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), rsMap);
    }
}
