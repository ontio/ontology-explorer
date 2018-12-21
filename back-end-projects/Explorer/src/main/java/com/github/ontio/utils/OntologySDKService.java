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


package com.github.ontio.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.ontio.OntSdk;
import com.github.ontio.common.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhouq
 * @date 2018/2/27
 */

@Component
public class OntologySDKService {
    private static final Logger logger = LoggerFactory.getLogger(OntologySDKService.class);

    private static OntologySDKService instance = null;

    @Autowired
    private ConfigParam configParam;

    public static synchronized OntologySDKService getInstance(ConfigParam param) {
        if (instance == null) {
            instance = new OntologySDKService(param);
        }
        return instance;
    }

    public OntologySDKService(ConfigParam param) {
        this.configParam = param;
    }

    public String getDDO(String ontId) {
        OntSdk ontSdk = getOntSdk();
        String ddoStr = "";
        try {
            ddoStr = ontSdk.nativevm().ontId().sendGetDDO(ontId);
        } catch (Exception e) {
            logger.error("get ddo error...", e);
            e.printStackTrace();
        }

        return ddoStr;
    }

    public int getNodeCount() {
        OntSdk ontSdk = getOntSdk();
        int nodeCount = 0;
        try {
            nodeCount = ontSdk.getConnect().getNodeCount();
        } catch (Exception e) {
            logger.error("get nodecount error...", e);
            e.printStackTrace();
        }

        return nodeCount;
    }

    /**
     * 获取账户余额，包括unboundong
     *
     * @param address
     * @return
     */
    public Map getAddressBalance(String address) {
        Map<String, Object> balanceMap = new HashMap<>();
        OntSdk ontSdk = getOntSdk();
        try {
            balanceMap = (Map) ontSdk.getConnect().getBalance(address);
        } catch (Exception e) {
            logger.error("getAddressBalance error...", e);
            e.printStackTrace();
            return null;
        }

        return balanceMap;
    }

    /**
     * 获取OEP4账户余额
     *
     * @param address
     * @return
     */
    public String getAddressOep4Balance(String address, String contractAddr) {
        OntSdk ontSdk = getOep4OntSdk(contractAddr);
        try {
            String balance = ontSdk.neovm().oep4().queryBalanceOf(address);
            return balance;
        } catch (Exception e) {
            logger.error("getAddressOep4Balance error...", e);
            return "";
        }
    }

    /**
     * 获取OEP5账户余额
     *
     * @param address
     * @return
     */
    public String getAddressOep5Balance(String address, String contractAddr) {
        OntSdk ontSdk = getOep5OntSdk(contractAddr);
        try {
            String balance = ontSdk.neovm().oep5().queryBalanceOf(address);
            return balance;
        } catch (Exception e) {
            logger.error("getAddressOep4Balance error...", e);
            return "";
        }
    }

    /**
     * 获取账户南瓜余额，包括unboundong
     *
     * @param address
     * @return
     */
    public JSONArray getAddressOpe8Balance(String address, String codeHash) {
        JSONArray balanceArray = new JSONArray();
        try {
            OntSdk ontSdk = getOep8OntSdk(codeHash);
            String balance = ontSdk.neovm().oep8().balancesOf(address);
            balanceArray = JSON.parseArray(balance);
        } catch (Exception e) {
            logger.error("getAddressOpe8Balance error...", e);
            e.printStackTrace();
            return null;
        }

        return balanceArray;
    }

    /**
     * 获取unboundong
     *
     * @param address
     * @return
     */
    public String getUnBoundOng(String address) {
        OntSdk ontSdk = getOntSdk();
        try {
            String unboundOng = ontSdk.nativevm().ong().unboundOng(address);
            return new BigDecimal(unboundOng).divide(ConstantParam.ONT_TOTAL).toPlainString();
        } catch (Exception e) {
            logger.error("getAddressBalance error...", e);
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject queryOep4Info(String contractHash) {
        try {
            OntSdk ontSdk = getOep4OntSdk(contractHash);
            String name = ontSdk.neovm().oep4().queryName();
            String symbol = ontSdk.neovm().oep4().querySymbol();
            String decimal = ontSdk.neovm().oep4().queryDecimals();
            String total = ontSdk.neovm().oep4().queryTotalSupply();

            JSONObject oep4Obj = new JSONObject();
            oep4Obj.put("Name", name);
            oep4Obj.put("Symbol", symbol);
            oep4Obj.put("TotalSupply", total);
            oep4Obj.put("Decimal",decimal);
            return oep4Obj;
        } catch (Exception e) {
            logger.error("getAddressBalance error...", e);
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject queryOep5Info(String contractHash) {
        try {
            OntSdk ontSdk = getOep5OntSdk(contractHash);
            String name = ontSdk.neovm().oep5().queryName();
            String symbol = ontSdk.neovm().oep5().querySymbol();
            String total = ontSdk.neovm().oep5().queryTotalSupply();

            JSONObject oep5Obj = new JSONObject();
            oep5Obj.put("Name", name);
            oep5Obj.put("Symbol", symbol);
            oep5Obj.put("TotalSupply", total);

            return oep5Obj;
        } catch (Exception e) {
            logger.error("getAddressBalance error...", e);
            e.printStackTrace();
            return null;
        }
    }

    private OntSdk getOntSdk() {
        OntSdk wm = OntSdk.getInstance();
        wm.setRestful(configParam.MASTERNODE_RESTFUL_URL);
        return wm;
    }

    private OntSdk getOep8OntSdk(String codeHash) throws Exception{
        OntSdk wm = OntSdk.getInstance();
        wm.setRestful(configParam.MASTERNODE_RESTFUL_URL);
        wm.neovm().oep8().setContractAddress(codeHash);
        return wm;
    }

    private OntSdk getOep4OntSdk(String codeHash) {
        OntSdk wm = OntSdk.getInstance();
        wm.setRestful(configParam.MASTERNODE_RESTFUL_URL);
        wm.neovm().oep4().setContractAddress(codeHash);
        return wm;
    }

    private OntSdk getOep5OntSdk(String codeHash) {
        OntSdk wm = OntSdk.getInstance();
        wm.setRestful(configParam.MASTERNODE_RESTFUL_URL);
        wm.neovm().oep5().setContractAddress(codeHash);
        return wm;
    }

    public JSONObject queryOep8Info(String contractHash, String[] tokenIds) {
        try {
            OntSdk ontSdk = getOep8OntSdk(contractHash);

            String[] names = new String[tokenIds.length];
            String[] symbols = new String[tokenIds.length];
            String[] totalSupplys = new String[tokenIds.length];
            for(int i = 0; i < tokenIds.length; i++){
                names[i] = ontSdk.neovm().oep8().queryName(Helper.hexToBytes(tokenIds[i]));
                symbols[i] = ontSdk.neovm().oep8().querySymbol(Helper.hexToBytes(tokenIds[i]));
                totalSupplys[i] = ontSdk.neovm().oep8().queryTotalSupply(Helper.hexToBytes(tokenIds[i]));
            }

            JSONObject oep4Obj = new JSONObject();
            oep4Obj.put("Name", names);
            oep4Obj.put("Symbol", symbols);
            oep4Obj.put("TotalSupply", totalSupplys);
            return oep4Obj;
        } catch (Exception e) {
            logger.error("getAddressBalance error...", e);
            e.printStackTrace();
            return null;
        }
    }
}
