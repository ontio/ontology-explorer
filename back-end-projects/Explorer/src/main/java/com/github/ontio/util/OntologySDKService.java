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


package com.github.ontio.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.ontio.OntSdk;
import com.github.ontio.config.ParamsConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhouq
 * @date 2018/2/27
 */
@Slf4j
@Component
public class OntologySDKService {
    
    private static OntologySDKService instance = null;

    @Autowired
    private ParamsConfig paramsConfig;

    public static synchronized OntologySDKService getInstance(ParamsConfig param) {
        if (instance == null) {
            instance = new OntologySDKService(param);
        }
        return instance;
    }

    public OntologySDKService(ParamsConfig param) {
        this.paramsConfig = param;
    }

    public String getDDO(String ontId) {
        OntSdk ontSdk = getOntSdk();
        String ddoStr = "";
        try {
            ddoStr = ontSdk.nativevm().ontId().sendGetDDO(ontId);
        } catch (Exception e) {
            log.error("getDDO error...", e);
            e.printStackTrace();
        }

        return ddoStr;
    }


    /**
     * 获取账户余额，包括unboundong
     *
     * @param address
     * @return
     */
    public Map getNativeAssetBalance(String address) {
        Map<String, Object> balanceMap = new HashMap<>();
        try {
            OntSdk ontSdk = getOntSdk();
            balanceMap = (Map) ontSdk.getConnect().getBalance(address);
        } catch (Exception e) {
            log.error("getNativeAssetBalance error...", e);
            balanceMap.put("ong", "0");
            balanceMap.put("ont", "0");
        }
        return balanceMap;
    }

    /**
     * 获取OEP4账户余额
     *
     * @param address
     * @return
     */
    public String getOep4AssetBalance(String address, String contractHash) {
        OntSdk ontSdk = getOep4OntSdk(contractHash);
        try {
            String balance = ontSdk.neovm().oep4().queryBalanceOf(address);
            return balance;
        } catch (Exception e) {
            log.error("getOep4AssetBalance error...", e);
            return "0";
        }
    }

    /**
     * 获取OEP5账户余额
     *
     * @param address
     * @return
     */
    public String getOep5AssetBalance(String address, String contractAddr) {
        OntSdk ontSdk = getOep5OntSdk(contractAddr);
        try {
            String balance = ontSdk.neovm().oep5().queryBalanceOf(address);
            return balance;
        } catch (Exception e) {
            log.error("getOep5AssetBalance error...", e);
            return "0";
        }
    }

    /**
     * 获取账户南瓜余额，包括unboundong
     *
     * @param address
     * @return
     */
    public JSONArray getOpe8AssetBalance(String address, String codeHash) {
        JSONArray balanceArray = new JSONArray();
        try {
            OntSdk ontSdk = getOep8OntSdk(codeHash);
            String balance = ontSdk.neovm().oep8().balancesOf(address);
            balanceArray = JSON.parseArray(balance);
        } catch (Exception e) {
            log.error("getOpe8AssetBalance error...", e);
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
            log.error("getUnBoundOng error...", e);
            e.printStackTrace();
            return null;
        }
    }
    


    private OntSdk getOntSdk() {
        OntSdk wm = OntSdk.getInstance();
        wm.setRestful(paramsConfig.MASTERNODE_RESTFUL_URL);
        return wm;
    }

    private OntSdk getOep8OntSdk(String codeHash) {
        OntSdk wm = OntSdk.getInstance();
        wm.setRestful(paramsConfig.MASTERNODE_RESTFUL_URL);
        wm.neovm().oep8().setContractAddress(codeHash);
        return wm;
    }

    private OntSdk getOep4OntSdk(String codeHash) {
        OntSdk wm = OntSdk.getInstance();
        wm.setRestful(paramsConfig.MASTERNODE_RESTFUL_URL);
        wm.neovm().oep4().setContractAddress(codeHash);
        return wm;
    }

    private OntSdk getOep5OntSdk(String codeHash) {
        OntSdk wm = OntSdk.getInstance();
        wm.setRestful(paramsConfig.MASTERNODE_RESTFUL_URL);
        wm.neovm().oep5().setContractAddress(codeHash);
        return wm;
    }

}
