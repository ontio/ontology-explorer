package com.github.ontio;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.ontio.common.Address;
import com.github.ontio.common.Helper;
import com.github.ontio.core.payload.InvokeWasmCode;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/12/3
 */
public class QueryBalanceTest {

    @Test
    public void queryWasmvmBalance() throws Exception {

        String nodeUrl = "http://polaris1.ont.io:20334";
        String address = "ASWTacJZSwozPfjQAe4Bq2saMEZ3aEQK8j";
        String oep4ContractHash = "efbe0cd7006d573eb1629ba7071d0aaba8bc5ce9";

        OntSdk ontSdk = OntSdk.getInstance();
        ontSdk.setRestful(nodeUrl);

        List<Object> params = new ArrayList<>(Collections.singletonList(Address.decodeBase58(address)));
        InvokeWasmCode tx = ontSdk.wasmvm().makeInvokeCodeTransaction(oep4ContractHash, "balanceOf", params, Address.decodeBase58(address), 500, 20000);
        JSONObject result = (JSONObject) ontSdk.getRestful().sendRawTransactionPreExec(tx.toHexString());
        System.out.println(JSON.toJSONString(result));
        String resultStr = result.getString("Result");

        System.out.println(Helper.BigIntFromNeoBytes(Helper.hexToBytes(resultStr)).toString());

        BigDecimal decimal = new BigDecimal(Helper.BigIntFromNeoBytes(Helper.hexToBytes(resultStr)).longValue());
        System.out.println(decimal);

    }
}
