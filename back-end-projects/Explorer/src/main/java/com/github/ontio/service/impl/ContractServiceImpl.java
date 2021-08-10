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
import com.github.ontio.model.dao.Erc20TxDetail;
import com.github.ontio.model.dao.NodeInfoOffChain;
import com.github.ontio.model.dao.NodeInfoOnChain;
import com.github.ontio.model.dto.ContractDto;
import com.github.ontio.model.dto.NodeInfoOffChainDto;
import com.github.ontio.model.dto.Oep5TxDetailDto;
import com.github.ontio.model.dto.TxDetailDto;
import com.github.ontio.model.dto.TxEventLogDto;
import com.github.ontio.model.dto.aggregation.ContractAggregationDto;
import com.github.ontio.service.IContractService;
import com.github.ontio.util.ConstantParam;
import com.github.ontio.util.ErrorInfo;
import com.github.ontio.util.Helper;
import com.github.ontio.util.OntologySDKService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service("ContractService")
public class ContractServiceImpl implements IContractService {

    private final ContractDailySummaryMapper contractDailySummaryMapper;
    private final ContractMapper contractMapper;
    private final Oep4TxDetailMapper oep4TxDetailMapper;
    private final Oep5TxDetailMapper oep5TxDetailMapper;
    private final Oep8TxDetailMapper oep8TxDetailMapper;

    private final Erc20TxDetailMapper erc20TxDetailMapper;

    private final TxEventLogMapper txEventLogMapper;
    private final ParamsConfig paramsConfig;
    private final NodeInfoOffChainMapper nodeInfoOffChainMapper;
    private final NodeInfoOnChainMapper nodeInfoOnChainMapper;
    private final ContractDailyAggregationMapper contractDailyAggregationMapper;

    @Autowired
    public ContractServiceImpl(ContractMapper contractMapper, Oep4TxDetailMapper oep4TxDetailMapper,
                               Oep5TxDetailMapper oep5TxDetailMapper, Oep8TxDetailMapper oep8TxDetailMapper, Erc20TxDetailMapper erc20TxDetailMapper, TxEventLogMapper txEventLogMapper,
                               ParamsConfig paramsConfig, NodeInfoOffChainMapper nodeInfoOffChainMapper,
                               ContractDailySummaryMapper contractDailySummaryMapper, NodeInfoOnChainMapper nodeInfoOnChainMapper,
                               ContractDailyAggregationMapper contractDailyAggregationMapper) {
        this.contractMapper = contractMapper;
        this.oep4TxDetailMapper = oep4TxDetailMapper;
        this.oep5TxDetailMapper = oep5TxDetailMapper;
        this.oep8TxDetailMapper = oep8TxDetailMapper;
        this.erc20TxDetailMapper = erc20TxDetailMapper;

        this.paramsConfig = paramsConfig;
        this.txEventLogMapper = txEventLogMapper;
        this.nodeInfoOffChainMapper = nodeInfoOffChainMapper;
        this.contractDailySummaryMapper = contractDailySummaryMapper;
        this.nodeInfoOnChainMapper = nodeInfoOnChainMapper;
        this.contractDailyAggregationMapper = contractDailyAggregationMapper;
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
                    oep5TxDetailDtos = filterDragonTxDetails(oep5TxDetailDtos);
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

            //todo erc20类型合约tx 的查询
            case ConstantParam.ASSET_TYPE_ERC20:
                List<Erc20TxDetail> erc20TxDetails = erc20TxDetailMapper.selectTxsByCalledContractHash(contractHash, start, pageSize);
                count = erc20TxDetailMapper.selectCountByCalledContracthash(contractHash);
                pageResponseBean = new PageResponseBean(erc20TxDetails, count);
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
        if (Helper.isEmptyOrNull(contractType) || ConstantParam.CONTRACT_TYPE_OTHER.equals(contractType)) {
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
                    oep5TxDetailDtos = filterDragonTxDetails(oep5TxDetailDtos);
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
                if (Helper.isNotEmptyAndNull(bindedOntidAndAccount.get("receive_account"))) {
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
                if (Helper.isNotEmptyAndNull(bindedNodeInfo.get("node_pubkey")) && Helper.isNotEmptyAndNull(bindedNodeInfo.get("node_name"))) {
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
                //query node wallet address
                String nodeWallet = getNodeWallet((String) map.get("node_name"), (String) map.get("node_pubkey"));
                if (Helper.isNotEmptyAndNull(nodeWallet)) {
                    obj.put("node_wallet", nodeWallet);

                    JSONArray dappNameArray = new JSONArray();
                    dappNameArray.add(map.get("dapp_name"));
                    obj.put("binded_dapp_names", dappNameArray);

                    rsMap.put(nodeName, obj);
                }
            }
        }

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), rsMap);
    }


    /**
     * query node wallet address
     *
     * @param nodeName
     * @param nodePubkey
     * @return
     */
    private String getNodeWallet(String nodeName, String nodePubkey) {

        NodeInfoOnChain nodeInfoOnChain = new NodeInfoOnChain();
        NodeInfoOffChain nodeInfoOffChain = new NodeInfoOffChain();

        Example example1 = new Example(NodeInfoOnChain.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("publicKey", nodePubkey);
        nodeInfoOnChain = nodeInfoOnChainMapper.selectOneByExample(example1);
        if (Helper.isNotEmptyAndNull(nodeInfoOnChain)) {
            return nodeInfoOnChain.getAddress();
        }

        Example example2 = new Example(NodeInfoOnChain.class);
        Example.Criteria criteria2 = example2.createCriteria();
        criteria2.andEqualTo("name", nodeName);
        nodeInfoOnChain = nodeInfoOnChainMapper.selectOneByExample(example2);
        if (Helper.isNotEmptyAndNull(nodeInfoOnChain)) {
            return nodeInfoOnChain.getAddress();
        }

        NodeInfoOffChainDto nodeInfoOffChainDto1 = NodeInfoOffChainDto.builder()
                .publicKey(nodePubkey)
                .build();
        nodeInfoOffChain = nodeInfoOffChainMapper.selectOne(nodeInfoOffChainDto1);
        if (Helper.isNotEmptyAndNull(nodeInfoOffChain)) {
            return nodeInfoOffChain.getAddress();
        }

        NodeInfoOffChainDto nodeInfoOffChainDto2 = NodeInfoOffChainDto.builder()
                .name(nodeName)
                .build();
        nodeInfoOffChain = nodeInfoOffChainMapper.selectOne(nodeInfoOffChainDto2);
        if (Helper.isNotEmptyAndNull(nodeInfoOffChain)) {
            return nodeInfoOffChain.getAddress();
        }
        return "";
    }


    @Override
    public ResponseBean queryDappstoreDappsInfo(Integer pageSize, Integer pageNumber) {

        Map<String, Object> rsMap = new HashMap<>();

        List<Map> allDappstoreDapp = contractMapper.selectDappstoreDapp();
        if (allDappstoreDapp.isEmpty()) {
            rsMap.put("contracts", new ArrayList<>());
            rsMap.put("total", 0);
        } else {
            List<String> allContractHashList = new ArrayList<>();
            //one dapp may contains many contracts
            Set<Object> allDappNameSet = new HashSet<>();
            allDappstoreDapp.forEach(item -> {
                allDappNameSet.add(item.get("dapp_name"));
                allContractHashList.add((String) item.get("contract_hash"));
            });

            long yesterday0HourTimestamp = getDaysAgo0HourTimestamp(1);
            long last7Day0HourTimestamp = getDaysAgo0HourTimestamp(7);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("beginTime", last7Day0HourTimestamp);
            paramMap.put("endTime", yesterday0HourTimestamp);
            paramMap.put("contractHashList", allContractHashList);
            paramMap.put("start", pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1));
            paramMap.put("pageSize", pageSize);
            //query week summary info by dapp_name，order by week_activeaddress_count,week_tx_count desc
            List<Map> dappOneWeekInfoList = contractDailySummaryMapper.selectDappstoreDappOneWeekInfo(paramMap);
            if (dappOneWeekInfoList.size() > 0) {
                //get dapp_name list by page
                List<String> dappNameList = new ArrayList<>();
                dappOneWeekInfoList.forEach(item -> dappNameList.add((String) item.get("dapp_name")));

                Map<String, Object> paramMap2 = new HashMap<>();
                paramMap2.put("dappNameList", dappNameList);
                List<String> contractHashList = contractMapper.selectHashByDappName(paramMap2);

                paramMap.put("contractHashList", contractHashList);
                paramMap.put("time", yesterday0HourTimestamp);
                //query daily summary info by dapp_name
                List<Map> dappDayInfoList = contractDailySummaryMapper.selectDappstoreContractYesterdayInfo(paramMap);

                for (Map map :
                        dappOneWeekInfoList) {
                    map.put("week_ont_sum", ((BigDecimal) map.get("week_ont_sum")).stripTrailingZeros().toPlainString());
                    map.put("week_ong_sum", ((BigDecimal) map.get("week_ong_sum")).stripTrailingZeros().toPlainString());
                    String dappName = (String) map.get("dapp_name");
                    for (Map contractMap :
                            allDappstoreDapp) {
                        if (dappName.equals(contractMap.get("dapp_name"))) {
                            contractMap.put("total_reward", ((BigDecimal) contractMap.get("total_reward")).stripTrailingZeros().toPlainString());
                            contractMap.put("lastweek_reward", ((BigDecimal) contractMap.get("lastweek_reward")).stripTrailingZeros().toPlainString());
                            map.putAll(contractMap);
                        }
                    }
                    for (Map map2 :
                            dappDayInfoList) {
                        if (dappName.equals(map2.get("dapp_name"))) {
                            map2.put("day_ont_sum", ((BigDecimal) map2.get("day_ont_sum")).stripTrailingZeros().toPlainString());
                            map2.put("day_ong_sum", ((BigDecimal) map2.get("day_ong_sum")).stripTrailingZeros().toPlainString());
                            map.putAll(map2);
                        }
                    }
                }
            }

            rsMap.put("contracts", dappOneWeekInfoList);
            rsMap.put("total", allDappNameSet.size());
        }

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), rsMap);
    }


    @Override
    public ResponseBean queryDappstoreDappsSummary() {

        Map rsMap = new HashMap();
        //查询Dappstore的合约基本信息
        List<Map> allDappstoreDapp = contractMapper.selectDappstoreDapp();
        if (allDappstoreDapp.isEmpty()) {
            rsMap.put("day_ont_sum", "0");
            rsMap.put("day_ong_sum", "0");
            rsMap.put("day_activeaddress_count", 0);
            rsMap.put("day_tx_count", 0);
            rsMap.put("total", 0);
        } else {
            List<String> allContractHashList = new ArrayList<>();
            //一个dapp可能会有多个合约
            Set<Object> allDappNameSet = new HashSet<>();
            allDappstoreDapp.forEach(item -> {
                allDappNameSet.add(item.get("dapp_name"));
                allContractHashList.add((String) item.get("contract_hash"));
            });

            rsMap.put("total", allDappNameSet.size());

            long yesterday0HourTime = getDaysAgo0HourTimestamp(1);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("contractHashList", allContractHashList);
            paramMap.put("time", yesterday0HourTime);
            Map contractInfo = contractDailySummaryMapper.selectAllDappstoreDappYesterdayInfo(paramMap);
            if (Helper.isEmptyOrNull(contractInfo)) {
                rsMap.put("day_ont_sum", "0");
                rsMap.put("day_ong_sum", "0");
                rsMap.put("day_activeaddress_count", 0);
                rsMap.put("day_tx_count", 0);
            } else {
                contractInfo.put("day_ont_sum",
                        ((BigDecimal) contractInfo.get("day_ont_sum")).stripTrailingZeros().toPlainString());
                contractInfo.put("day_ong_sum",
                        ((BigDecimal) contractInfo.get("day_ong_sum")).stripTrailingZeros().toPlainString());
                rsMap.putAll(contractInfo);
            }
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), rsMap);
    }

    @Override
    public ResponseBean queryDailyAggregation(String contractHash, String token, Date from, Date to) {
        String tokenContractHash = paramsConfig.getContractHash(token);
        List<ContractAggregationDto> aggregations = contractDailyAggregationMapper.findAggregations(contractHash, tokenContractHash,
                from, to);
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), aggregations);
    }

    @Override
    public ResponseBean queryDailyAggregationOfTokenType(String contractHash, String tokenType, Date from, Date to) {
        String tokenContractHash = paramsConfig.getContractHash(tokenType);
        List<ContractAggregationDto> aggregations = contractDailyAggregationMapper.findAggregationsForToken(contractHash,
                tokenContractHash, from, to);
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), aggregations);
    }

    @Override
    public ResponseBean checkIfExistsHash(String contractHash) {

        Integer integer = contractMapper.selectIfHashExists(contractHash);
        if (0 == integer) {
            return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), false);
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), true);
    }


    private long getDaysAgo0HourTimestamp(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DAY_OF_MONTH, -days);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long zeroHourTimestamp = calendar.getTimeInMillis() / 1000L;
        return zeroHourTimestamp;
    }

    private List<Oep5TxDetailDto> filterDragonTxDetails(List<Oep5TxDetailDto> details) {
        if (details == null || details.isEmpty()) {
            return details;
        }
        Map<String, Oep5TxDetailDto> detailMap = new LinkedHashMap<>();
        details.forEach(detail -> detailMap.compute(detail.getTxHash(), (k, v) -> {
            if (v == null) {
                return detail;
            } else if (detail.getAssetName() != null && detail.getAssetName().startsWith("HyperDragons")) {
                return detail;
            } else {
                return v;
            }
        }));
        return new ArrayList<>(detailMap.values());
    }

}
