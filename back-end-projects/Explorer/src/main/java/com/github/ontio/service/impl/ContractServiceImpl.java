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
import com.github.ontio.model.dto.ContractDto;
import com.github.ontio.model.dto.Oep5TxDetailDto;
import com.github.ontio.model.dto.TxDetailDto;
import com.github.ontio.model.dto.TxEventLogDto;
import com.github.ontio.service.IContractService;
import com.github.ontio.util.ConstantParam;
import com.github.ontio.util.ErrorInfo;
import com.github.ontio.util.Helper;
import com.github.ontio.util.OntologySDKService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service("ContractService")
public class ContractServiceImpl implements IContractService {

    private final ContractMapper contractMapper;
    private final Oep4TxDetailMapper oep4TxDetailMapper;
    private final Oep5TxDetailMapper oep5TxDetailMapper;
    private final Oep8TxDetailMapper oep8TxDetailMapper;
    private final TxDetailMapper txDetailMapper;
    private final TxEventLogMapper txEventLogMapper;
    private final ParamsConfig paramsConfig;

    @Autowired
    public ContractServiceImpl(ContractMapper contractMapper, Oep4TxDetailMapper oep4TxDetailMapper, Oep5TxDetailMapper oep5TxDetailMapper, Oep8TxDetailMapper oep8TxDetailMapper, TxDetailMapper txDetailMapper, TxEventLogMapper txEventLogMapper, ParamsConfig paramsConfig) {
        this.contractMapper = contractMapper;
        this.oep4TxDetailMapper = oep4TxDetailMapper;
        this.oep5TxDetailMapper = oep5TxDetailMapper;
        this.oep8TxDetailMapper = oep8TxDetailMapper;
        this.txDetailMapper = txDetailMapper;
        this.paramsConfig = paramsConfig;
        this.txEventLogMapper = txEventLogMapper;
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
}
