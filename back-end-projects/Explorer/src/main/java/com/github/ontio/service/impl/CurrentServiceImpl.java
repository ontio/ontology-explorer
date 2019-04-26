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

import com.alibaba.fastjson.JSONObject;
import com.github.ontio.dao.*;
import com.github.ontio.model.*;
import com.github.ontio.paramBean.OldResult;
import com.github.ontio.service.ICurrentService;
import com.github.ontio.config.ConfigParam;
import com.github.ontio.util.ErrorInfo;
import com.github.ontio.util.Helper;
import com.github.ontio.util.OntologySDKService;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/2/27
 */
@Service("CurrentService")
@MapperScan("com.github.ontio.dao")
public class CurrentServiceImpl implements ICurrentService {
    private static final Logger logger = LoggerFactory.getLogger(CurrentServiceImpl.class);

    private static final String VERSION = "1.0";

    @Autowired
    private CurrentMapper currentMapper;

    @Autowired
    TransactionDetailMapper transactionDetailMapper;

    @Autowired
    private Oep4Mapper oep4Mapper;

    @Autowired
    private Oep5Mapper oep5Mapper;

    @Autowired
    private Oep8Mapper oep8Mapper;

    @Autowired
    private AddressSummaryMapper addressSummaryMapper;

    @Autowired
    private ContractsMapper contractsMapper;

    @Autowired
    private ConfigParam configParam;

    private OntologySDKService sdk;

    private synchronized void initSDK() {
        if (sdk == null) {
            sdk = OntologySDKService.getInstance(configParam);
        }
    }

    @Override
    public OldResult querySummaryInfo() {
        Map summary = currentMapper.selectSummaryInfo();
       // List<String> addrList = transactionDetailMapper.selectAllAddress();
        initSDK();
//        int nodeCount = sdk.getNodeCount();
        Map<String, Object> rs = new HashMap();

        rs.put("NodeCount", Integer.parseInt(configParam.SDK_NODE_COUNT));
        rs.put("CurrentHeight", summary.get("Height"));
        rs.put("TxnCount", summary.get("TxnCount"));
        rs.put("OntIdCount", summary.get("OntIdCount"));
        rs.put("AddressCount", addressSummaryMapper.selectAllAddressCount());

        return Helper.result("QueryCurrentInfo", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);
    }

    /**
     * 注册合约信息
     * @param reqObj
     * @return
     */
    @Override
    public OldResult registerContractInfo(JSONObject reqObj) {
        initSDK();

        //TODO 首先更新合约总表，再更新token子表（oep4和oep8）, 需要联系信息
        String contractHash = reqObj.getString("contractHash");
        Contracts contract = contractsMapper.selectContractByContractHash(contractHash);
        if(Helper.isEmptyOrNull(contract)) {
            return Helper.result("RegisterContractInfo", ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), "1.0", false);
        }

        contract.setProject(reqObj.getString("project"));
        contract.setCode(reqObj.getString("code"));
        contract.setAbi(reqObj.getString("abi"));
        contract.setName(reqObj.getString("name"));
        contract.setType(reqObj.getString("type"));
        contract.setContactinfo(reqObj.getString("contactinfo"));
        contract.setDescription(reqObj.getString("description"));
        contract.setLogo(reqObj.getString("logo"));
        contract.setUpdatetime(Integer.valueOf(String.valueOf(System.currentTimeMillis() / 1000)));
        contract.setOntcount(null);
        contract.setOngcount(null);
        contractsMapper.updateByPrimaryKeySelective(contract);


        String type = reqObj.getString("type");
        if (type == null){
            type = "";
        }
        switch (type.toLowerCase()){
            case "oep4":
                Oep4 oep4 = new Oep4();
                oep4.setContract(contractHash);
                Oep4 oep4DAO = oep4Mapper.selectByPrimaryKey(oep4);
                if(!Helper.isEmptyOrNull(oep4DAO)) {
                    oep4Mapper.deletContractByHash(contractHash);
                }

                JSONObject oep4Info = sdk.queryOep4Info(contractHash);
                oep4DAO = new Oep4();
                oep4DAO.setSymbol(oep4Info.getString("Symbol"));
                oep4DAO.setName(oep4Info.getString("Name"));
                oep4DAO.setTotalsupply(new BigDecimal(oep4Info.getString("TotalSupply")));
                oep4DAO.setDecimals(new BigDecimal(oep4Info.getString("Decimal")));
                oep4DAO.setDescription(reqObj.getString("description"));
                oep4DAO.setContactinfo(reqObj.getString("contactinfo"));
                oep4DAO.setLogo(reqObj.getString("logo"));
                oep4DAO.setContract(contractHash);
                oep4DAO.setAuditflag(1);
                oep4DAO.setCreatetime(new Date());
                oep4DAO.setUpdatetime(new Date());
                oep4Mapper.insertSelective(oep4DAO);

                break;
            case "oep5":
                if(!Helper.isEmptyOrNull(oep5Mapper.selectByPrimaryKey(contractHash))) {
                    oep5Mapper.deletContractByHash(contractHash);
                }

                JSONObject oep5Info = sdk.queryOep5Info(contractHash);
                Oep5 oep5DAO = new Oep5();
                oep5DAO.setSymbol(oep5Info.getString("Symbol"));
                oep5DAO.setName(oep5Info.getString("Name"));
                oep5DAO.setTotalsupply(new BigDecimal(oep5Info.getString("TotalSupply")));
                oep5DAO.setDescription(reqObj.getString("description"));
                oep5DAO.setContactinfo(reqObj.getString("contactinfo"));
                oep5DAO.setLogo(reqObj.getString("logo"));
                oep5DAO.setContract(contractHash);
                oep5DAO.setAuditflag(1);
                oep5DAO.setCreatetime(new Date());
                oep5DAO.setUpdatetime(new Date());
                oep5Mapper.insertSelective(oep5DAO);

                break;

            case "oep8":
                Oep8 oep8Contract = oep8Mapper.queryOEPContract(contractHash);
                if(!Helper.isEmptyOrNull(oep8Contract)) {
                    // 因为南瓜合约的name是pumpkin，不是合约内的tokenName，所以不重新录入
                    return Helper.result("RegisterContractInfo", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), "1.0", true);
                }

                // 要求tokenId内容为：01，02，03，04，05
                String[] tokenIds = reqObj.getString("tokenId").split(",");
                JSONObject oep8Info = sdk.queryOep8Info(contractHash, tokenIds);

                String[] names = (String[])oep8Info.get("Name");
                String[] symbols = (String[])oep8Info.get("Symbol");
                String[] totalSupplys = (String[])oep8Info.get("TotalSupply");
                List list = new ArrayList();
                for (int i = 0; i < tokenIds.length; i++){
                    Oep8 oep8 = new Oep8();
                    oep8.setTokenid(tokenIds[i]);
                    oep8.setName(names[i]);
                    oep8.setSymbol(symbols[i]);
                    oep8.setTotalsupply(new BigDecimal(totalSupplys[i]));
                    oep8.setDescription(reqObj.getString("description"));
                    oep8.setContactinfo(reqObj.getString("contactinfo"));
                    oep8.setLogo(reqObj.getString("logo"));
                    oep8.setAuditflag(1);
                    oep8.setCreatetime(new Date());
                    oep8.setUpdatetime(new Date());
                    oep8.setContract(contractHash);

                    list.add(oep8);
                }
                oep8Mapper.banchInsertSelective(list);
                break;

            default:
                break;
        }

        return Helper.result("RegisterContractInfo", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), "1.0", true);
    }

    /**
     * Marketing Info
     * @return
     */
    @Override
    public OldResult queryMarketingInfo() {
        Map summary = currentMapper.selectSummaryInfo();
        int height = (Integer) summary.get("Height");

        Map<String, Object> rsMap = new HashMap<>();
        rsMap.put("CurrentHeight", height);
        rsMap.put("CurrentSupply", "59.75%");

        return Helper.result("QueryMarketingInfo", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rsMap);
    }
}
