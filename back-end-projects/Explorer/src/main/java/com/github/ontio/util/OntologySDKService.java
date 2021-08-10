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
import com.alibaba.fastjson.JSONObject;
import com.github.ontio.OntSdk;
import com.github.ontio.account.Account;
import com.github.ontio.common.Address;
import com.github.ontio.common.Common;
import com.github.ontio.common.ErrorCode;
import com.github.ontio.common.Helper;
import com.github.ontio.config.ParamsConfig;
import com.github.ontio.core.DataSignature;
import com.github.ontio.core.governance.Configuration;
import com.github.ontio.core.payload.InvokeWasmCode;
import com.github.ontio.core.transaction.Transaction;
import com.github.ontio.crypto.Curve;
import com.github.ontio.crypto.KeyType;
import com.github.ontio.io.BinaryReader;
import com.github.ontio.network.exception.ConnectorException;
import com.github.ontio.sdk.exception.SDKException;
import com.github.ontio.smartcontract.nativevm.abi.NativeBuildParams;
import com.github.ontio.smartcontract.neovm.abi.BuildParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * @author zhouq
 * @date 2018/2/27
 */
@Slf4j
@Component
public class OntologySDKService {

    private String ontIdContractAddress = "0000000000000000000000000000000000000003";
    private static OntologySDKService instance = null;
    private final String contractAddress = "0000000000000000000000000000000000000007";

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

    public String getDocument(String ontId) throws Exception {
        OntSdk ontSdk = getOntSdk();
        String documentStr = "";
        documentStr = ontSdk.nativevm().ontId().sendGetDocument(ontId);
        if (!StringUtils.isEmpty(documentStr)) {
            Map<String, Object> ddo = new HashMap<>();
            JSONObject documentObj = JSONObject.parseObject(documentStr);

            ontId = documentObj.getString("id");
            ddo.put("OntId", ontId);

            JSONArray attributes = documentObj.getJSONArray("attribute");
            if (null == attributes) {
                attributes = new JSONArray();
            } else {
                for (int i = 0; i < attributes.size(); i++) {
                    JSONObject attribute = attributes.getJSONObject(i);
                    String key = attribute.getString("key");
                    String value = attribute.getString("value");
                    String newKey = key.substring(key.indexOf("#") + 1);
                    attribute.put("Key", newKey);
                    attribute.remove("key");
                    attribute.put("Value", value);
                    attribute.remove("value");
                }
            }
            ddo.put("Attributes", attributes);

            JSONArray publicKeys = documentObj.getJSONArray("publicKey");
            List<Map<String, Object>> owners = new ArrayList<>();
            for (int i = 0; i < publicKeys.size(); i++) {
                Map<String, Object> owner = new HashMap<>();
                JSONObject publicKey = publicKeys.getJSONObject(i);
                String pubKeyId = publicKey.getString("id");
                String value = publicKey.getString("publicKeyHex");
                String type = publicKey.getString("type");
                owner.put("Curve", "");
                owner.put("PubKeyId", pubKeyId);
                owner.put("Type", type);
                owner.put("Value", value);
                owners.add(owner);
            }
            ddo.put("Owners", owners);

            JSONArray members = new JSONArray();
            JSONObject controllerObj = documentObj.getJSONObject("controller");
            if (!CollectionUtils.isEmpty(controllerObj)) {
                members = controllerObj.getJSONArray("members");
            }
            ddo.put("Controller", members);

            documentStr = JSON.toJSONString(ddo);
        }
        return documentStr;
    }

    public String getDDO(String ontId) {
        OntSdk ontSdk = getOntSdk();
        String ddoStr = "";
        try {
            ddoStr = sendGetDDO(ontId);
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
     * EVM 类型 查询以太坊地址的账户内的ong余额
     */

    public Map getEVMONGAssetBalance(String address){
        Map<String, Object> balanceMap = new HashMap<>();
        String ontAddr = com.github.ontio.util.Helper.EthAddrToOntAddr(address);
        OntSdk ontSdk = getOntSdk();
        try {
            balanceMap = (Map) ontSdk.getConnect().getBalance(ontAddr);
        } catch (Exception e) {
            log.error("getEVMONGAssetBalance error...", e);
            balanceMap.put("ong", "0");
        }
        return balanceMap;
    }


    /**
     * get neovm OEP4 balance
     *
     * @param address
     * @return
     */
    public String getNeovmOep4AssetBalance(String address, String contractHash) {
        OntSdk ontSdk = getOep4OntSdk(contractHash);
        try {
            String balance = ontSdk.neovm().oep4().queryBalanceOf(address);
            return balance;
        } catch (Exception e) {
            log.error("getNeovmOep4AssetBalance error...", e);
            return "0";
        }
    }


    /**
     * get wasmvm OEP4 balance
     *
     * @param address
     * @return
     */
    public String getWasmvmOep4AssetBalance(String address, String contractHash) {
        try {
            OntSdk ontSdk = getOntSdk();

            List<Object> params = new ArrayList<>(Collections.singletonList(Address.decodeBase58(address)));
            InvokeWasmCode tx = ontSdk.wasmvm().makeInvokeCodeTransaction(contractHash, "balanceOf", params, Address.decodeBase58(address), 500, 25000000);
            JSONObject result = (JSONObject) ontSdk.getRestful().sendRawTransactionPreExec(tx.toHexString());
            log.info("getWasmvmOep4AssetBalance result:{}", result);
            BigInteger bigInteger = Helper.BigIntFromNeoBytes(Helper.hexToBytes(result.getString("Result")));
            return bigInteger.toString();
//            long longValue = bigInteger.longValue();
//            BigDecimal decimal;
//            if (longValue < 0) {
//                double v = Math.pow(2, 64) + longValue;
//                decimal = new BigDecimal(v);
//            } else {
//                decimal = new BigDecimal(longValue);
//            }
//            return decimal.stripTrailingZeros().toPlainString();
        } catch (Exception e) {
            log.error("getNeovmOep4AssetBalance error...", e);
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

    /**
     * 根据dapp合约hash获取绑定的节点信息
     *
     * @param contractHash
     * @param dappContractHash
     * @return
     */
    public Map getDappBindedNodeInfo(String contractHash, String dappContractHash) {
        Map<String, Object> rsMap = new HashMap<>();
        rsMap.put("node_name", "");
        rsMap.put("node_pubkey", "");
        try {
            OntSdk ontSdk = getOntSdk();

            List paramList = new ArrayList();
            paramList.add("get_binded_node".getBytes());

            List args = new ArrayList();
            args.add(Helper.hexToBytes(dappContractHash));
            paramList.add(args);
            byte[] params = BuildParams.createCodeParamsScript(paramList);

            Transaction tx = ontSdk.vm().makeInvokeCodeTransaction(Helper.reverse(contractHash), null, params, null, 20000, 500);
            Object obj = ontSdk.getConnect().sendRawTransactionPreExec(tx.toHexString());

            String result = ((JSONObject) obj).getString("Result");
            log.info("contracthash:{},bindedNodeinfo:{}", dappContractHash, result);
            if (com.github.ontio.util.Helper.isNotEmptyAndNull(result)) {
                Map map = (Map) BuildParams.deserializeItem(Helper.hexToBytes(result));
                rsMap.put("node_name", new String(Helper.hexToBytes(((String) map.get("node_name")))));
                rsMap.put("node_pubkey", map.get("node_pubkey"));
            }
            return rsMap;
        } catch (Exception e) {
            log.error("dappContractHash:{} getDappBindedNodeInfo error...", dappContractHash, e);
        }
        return rsMap;
    }


    /**
     * 根据dapp合约hash获取绑定的onid和钱包账户
     *
     * @param contractHash
     * @param dappContractHash
     * @return
     */
    public Map getDappBindedOntidAndAccount(String contractHash, String dappContractHash) {
        Map<String, Object> rsMap = new HashMap<>();
        rsMap.put("receive_account", "");
        rsMap.put("ontid", "");
        try {
            OntSdk ontSdk = getOntSdk();

            List paramList = new ArrayList();
            paramList.add("get_binded_dapp".getBytes());

            List args = new ArrayList();
            args.add(Helper.hexToBytes(dappContractHash));
            paramList.add(args);
            byte[] params = BuildParams.createCodeParamsScript(paramList);

            Transaction tx = ontSdk.vm().makeInvokeCodeTransaction(Helper.reverse(contractHash), null, params, null, 20000, 500);
            Object obj = ontSdk.getConnect().sendRawTransactionPreExec(tx.toHexString());

            String result = ((JSONObject) obj).getString("Result");
            log.info("contracthash:{},bindedAccountinfo:{}", dappContractHash, result);
            if (com.github.ontio.util.Helper.isNotEmptyAndNull(result)) {
                Map map = (Map) BuildParams.deserializeItem(Helper.hexToBytes(result));
                rsMap.put("receive_account", Address.parse((String) map.get("receive_account")).toBase58());
                rsMap.put("ontid", new String(Helper.hexToBytes((String) map.get("ontid"))));
            }
            return rsMap;
        } catch (Exception e) {
            log.error("getDappBindedOntidAndAccount error...", e);
        }
        return rsMap;
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


    /**
     * verify signature
     *
     * @param ontId
     * @param origDataStr
     * @param signatureStr
     * @return
     */
    public boolean verifySignature(String ontId, String origDataStr, String signatureStr) {
        try {
            Boolean verify = Boolean.FALSE;
            OntSdk ontSdk = OntSdk.getInstance();
            ontSdk.setRestful(paramsConfig.MASTERNODE_RESTFUL_URL);
            String issuerDdo;
            try {
                issuerDdo = getDocument(ontId);
            } catch (Exception e) {
                issuerDdo = ontSdk.nativevm().ontId().sendGetDDO(ontId);
            }
            JSONArray owners = JSONObject.parseObject(issuerDdo).getJSONArray("Owners");
            for (int i = 0; i < owners.size(); ++i) {
                String pubkeyStr = owners.getJSONObject(i).getString("Value");
                Account account = new Account(false, Helper.hexToBytes(pubkeyStr));
                verify = account.verifySignature(Helper.hexToBytes(origDataStr), Helper.hexToBytes(signatureStr));
                if (verify) {
                    break;
                }
            }
            return verify;
        } catch (Exception e) {
            log.error("{} error...", com.github.ontio.util.Helper.currentMethod(), e);
        }
        return false;
    }

    /**
     * sign
     *
     * @param origData
     * @return
     */
    public String signData2HexStr(String origData) {
        try {
            OntSdk ontSdk = OntSdk.getInstance();
            ontSdk.openWalletFile("account.json");
            Account account = ontSdk.getWalletMgr().getAccount(paramsConfig.IDENTITY_ONTID, paramsConfig.IDENTITY_PASSWORD, Base64.getDecoder().decode(paramsConfig.IDENTITY_SALT));
            DataSignature sign = new DataSignature(ontSdk.defaultSignScheme, account, origData.getBytes());
            String sigdata = Helper.toHexString(sign.signature());
            return sigdata;
        } catch (Exception e) {
            log.error("{} error...", com.github.ontio.util.Helper.currentMethod(), e);
        }
        return "";
    }

    /**
     * verify signature by publicKey
     *
     * @param publicKey
     * @param origData
     * @param signatureStr
     * @return
     */
    public boolean verifySignatureByPublicKey(String publicKey, byte[] origData, String signatureStr) throws Exception {
        Account account = new Account(false, Helper.hexToBytes(publicKey));
        Boolean verify = account.verifySignature(origData, Helper.hexToBytes(signatureStr));
        return verify;
    }

    /**
     * @param ontid
     * @return
     * @throws Exception
     * @deprecated 已废弃使用
     */
    //TODO should remove this method
    public String sendGetDDO(String ontid) throws Exception {
        OntSdk ontSdk = getOntSdk();
        if (ontid == null) {
            throw new SDKException(ErrorCode.ParamErr("ontid should not be null"));
        }

        List list = new ArrayList();
        list.add(ontid.getBytes());
        byte[] arg = NativeBuildParams.createCodeParamsScript(list);

        Transaction tx = ontSdk.vm().buildNativeParams(new Address(Helper.hexToBytes(ontIdContractAddress)), "getDDO", arg, null, 0, 0);
        Object obj = ontSdk.getConnect().sendRawTransactionPreExec(tx.toHexString());
        String res = ((JSONObject) obj).getString("Result");
        if (res.equals("")) {
            return res;
        }
        Map map = parseDdoData(ontid, res);
        if (map.size() == 0) {
            return "";
        }
        return JSON.toJSONString(map);
    }

    private Map parseDdoData(String ontid, String obj) throws Exception {
        byte[] bys = Helper.hexToBytes(obj);

        ByteArrayInputStream bais = new ByteArrayInputStream(bys);
        BinaryReader br = new BinaryReader(bais);
        byte[] publickeyBytes;
        byte[] attributeBytes;
        byte[] recoveryBytes;
        byte[] controllerBytes;
        try {
            publickeyBytes = br.readVarBytes();
        } catch (Exception e) {
            publickeyBytes = new byte[]{};
        }
        try {
            attributeBytes = br.readVarBytes();
        } catch (Exception e) {
            e.printStackTrace();
            attributeBytes = new byte[]{};
        }
        try {
            recoveryBytes = br.readVarBytes();
        } catch (Exception e) {
            recoveryBytes = new byte[]{};
        }
        try {
            controllerBytes = br.readVarBytes();
        } catch (Exception e) {
            controllerBytes = new byte[]{};
        }

        List pubKeyList = new ArrayList();
        if (publickeyBytes.length != 0) {
            ByteArrayInputStream bais1 = new ByteArrayInputStream(publickeyBytes);
            BinaryReader br1 = new BinaryReader(bais1);
            while (true) {
                try {
                    Map publicKeyMap = new HashMap();
                    publicKeyMap.put("PubKeyId", ontid + "#keys-" + String.valueOf(br1.readInt()));
                    byte[] pubKey = br1.readVarBytes();
                    if (pubKey.length == 33) {
                        publicKeyMap.put("Type", KeyType.ECDSA.name());
                        publicKeyMap.put("Curve", Curve.P256);
                        publicKeyMap.put("Value", Helper.toHexString(pubKey));
                    } else {
                        publicKeyMap.put("Type", KeyType.fromLabel(pubKey[0]));
                        publicKeyMap.put("Curve", Curve.fromLabel(pubKey[1]));
                        publicKeyMap.put("Value", Helper.toHexString(pubKey));
                    }

                    pubKeyList.add(publicKeyMap);
                } catch (Exception e) {
                    break;
                }
            }
        }
        List attrsList = new ArrayList();
        if (attributeBytes.length != 0) {
            ByteArrayInputStream bais2 = new ByteArrayInputStream(attributeBytes);
            BinaryReader br2 = new BinaryReader(bais2);
            while (true) {
                try {
                    Map<String, Object> attributeMap = new HashMap();
                    attributeMap.put("Key", new String(br2.readVarBytes()));
                    attributeMap.put("Type", new String(br2.readVarBytes()));
                    attributeMap.put("Value", new String(br2.readVarBytes()));
                    attrsList.add(attributeMap);
                } catch (Exception e) {
                    break;
                }
            }
        }

        List<String> controllers = new ArrayList<>();
        if (controllerBytes.length != 0) {
            String controllerStr = new String(controllerBytes);
            if (controllerStr.startsWith(Common.didont)) {
                controllers.add(controllerStr);
                log.info(controllerStr);
            } else {
                JSONObject jsonObject = JSON.parseObject(controllerStr);
                List<String> members = jsonObject.getObject("members", List.class);
                for (String controller : members) {
                    controllers.add(controller);
                }
            }
        }

        Map map = new HashMap();
        map.put("Owners", pubKeyList);
        map.put("Attributes", attrsList);
        if (recoveryBytes.length != 0) {
            map.put("Recovery", Address.parse(Helper.toHexString(recoveryBytes)).toBase58());
        }
        map.put("OntId", ontid);
        map.put("Controller", controllers);
        return map;
    }

    public String getAuthorizeInfo(String publicKey, String address) throws SDKException {
        OntSdk ontSdk = getOntSdk();
        Address addr = Address.decodeBase58(address);
        return ontSdk.nativevm().governance().getAuthorizeInfo(publicKey, addr);
    }

    public int getPreConsensusCount() {
        try {
            Configuration preConfiguration = getPreConfiguration();
            return preConfiguration.K;
        } catch (Exception e) {
            log.warn("Getting authorize info failed: {}", e.getMessage());
        }
        return 0;
    }

    public Configuration getPreConfiguration() throws Exception {
        OntSdk ontSdk = getOntSdk();
        String res = ontSdk.getConnect().getStorage(Helper.reverse(contractAddress), Helper.toHexString("preConfig".getBytes()));
        if (res == null) {
            return null;
        }
        Configuration configuration = new Configuration();
        ByteArrayInputStream in = new ByteArrayInputStream(Helper.hexToBytes(res));
        BinaryReader reader = new BinaryReader(in);
        configuration.deserialize(reader);
        return configuration;
    }
}
