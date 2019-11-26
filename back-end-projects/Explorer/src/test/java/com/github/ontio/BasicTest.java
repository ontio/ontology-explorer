<<<<<<< Updated upstream
package com.github.ontio;

import com.alibaba.fastjson.JSONObject;
import com.github.ontio.common.Address;
import com.github.ontio.common.Helper;
import com.github.ontio.core.transaction.Transaction;
import com.github.ontio.smartcontract.neovm.abi.BuildParams;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/3/30
 */
public class BasicTest {


    @Test
    public void calculateScore() {


/*        String contractAddress = Address.parse(com.github.ontio.common.Helper.reverse("8c15299cc6843e808b42f1ffb9cff7ec36f81ea1")).toBase58();
        System.out.println("addr；"+contractAddress);*/

        int txnCount = 4003;
        int activeAddrCount = 2880;
        int allActiveAddrCount = 2929;
        int allTxnCount = 5835;

        BigDecimal a = new BigDecimal(txnCount).divide(new BigDecimal(activeAddrCount), 2, ROUND_HALF_DOWN);
        BigDecimal b = new BigDecimal(allTxnCount).divide(new BigDecimal(allActiveAddrCount), 2, ROUND_HALF_DOWN);
        BigDecimal x = new BigDecimal("1.5").multiply(new BigDecimal("2")).multiply(a.divide(b, 2, ROUND_HALF_DOWN));
        double exp = -x.divide(new BigDecimal("2")).doubleValue();

        BigDecimal sapp = x.multiply(new BigDecimal(Math.exp(exp)));
        BigDecimal appScore = sapp.multiply(new BigDecimal(txnCount));
        int score = appScore.setScale(4, BigDecimal.ROUND_HALF_DOWN).intValue();
        System.out.println("score:" + score);


        BigDecimal temp = new BigDecimal(score).divide(new BigDecimal(score), 2, ROUND_HALF_DOWN);
        BigDecimal ongreward = new BigDecimal("19841").divide(new BigDecimal("5"), 2, ROUND_HALF_DOWN).multiply(temp);
        System.out.println("ongreward:" + ongreward);

    }

    @Test
    public void dappBindedNodeTest() throws Exception {


        String dappContractHash = "75706715336de4d64fbe40f348993097b1863d3b";
        String contractHash = "939053a288f44eb560cad17c36df5ad34dafca2c";

        OntSdk ontSdk = OntSdk.getInstance();
        ontSdk.setRestful("http://dappnode2.ont.io:20334");


        List paramList = new ArrayList();
        paramList.add("get_binded_node".getBytes());

        List args = new ArrayList();
        args.add(Helper.hexToBytes(dappContractHash));
        paramList.add(args);
        byte[] params = BuildParams.createCodeParamsScript(paramList);

        Transaction tx = ontSdk.vm().makeInvokeCodeTransaction(Helper.reverse(contractHash), null, params, null, 20000, 500);
        Object obj = ontSdk.getConnect().sendRawTransactionPreExec(tx.toHexString());

        String result = ((JSONObject) obj).getString("Result");
        if (com.github.ontio.util.Helper.isNotEmptyAndNull(result)) {

            Map map = (Map) BuildParams.deserializeItem(Helper.hexToBytes(result));
           System.out.println("node_name:"+new String(Helper.hexToBytes(((String) map.get("node_name")))));
           System.out.println("node_pubkey:"+map.get("node_pubkey"));
        }
    }


    @Test
    public void dappBindedWalletTest() throws Exception {


        String dappContractHash = "5389914e9ad96cc8a15a3e961440334719270cf8";
        String contractHash = "939053a288f44eb560cad17c36df5ad34dafca2c";

        OntSdk ontSdk = OntSdk.getInstance();
        ontSdk.setRestful("http://dappnode1.ont.io:20334");

        List paramList = new ArrayList();
        paramList.add("get_binded_dapp".getBytes());

        List args = new ArrayList();
        args.add(Helper.hexToBytes(dappContractHash));
        paramList.add(args);
        byte[] params = BuildParams.createCodeParamsScript(paramList);

        Transaction tx = ontSdk.vm().makeInvokeCodeTransaction(Helper.reverse(contractHash), null, params, null, 20000, 500);
        Object obj = ontSdk.getConnect().sendRawTransactionPreExec(tx.toHexString());

        String result = ((JSONObject) obj).getString("Result");
        System.out.println("result:"+result);
/*        if (com.github.ontio.util.Helper.isNotEmptyAndNull(result)) {

            Map<String,Object> map = (Map<String,Object>) BuildParams.deserializeItem(Helper.hexToBytes(result));
            for(String key:map.keySet()){
                System.out.println("address:"+key);
            }

        }*/
        if (com.github.ontio.util.Helper.isNotEmptyAndNull(result)) {

            Map map = (Map) BuildParams.deserializeItem(Helper.hexToBytes(result));
            System.out.println("receive_account:"+ Address.parse((String) map.get("receive_account")).toBase58());
            System.out.println("ont_id:"+new String(Helper.hexToBytes((String) map.get("ontid"))));
        }
    }

}
=======
package com.github.ontio;

import com.alibaba.fastjson.JSONObject;
import com.github.ontio.common.Address;
import com.github.ontio.common.Helper;
import com.github.ontio.core.transaction.Transaction;
import com.github.ontio.smartcontract.neovm.abi.BuildParams;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/3/30
 */
public class BasicTest {


    @Test
    public void calculateScore() {


/*        String contractAddress = Address.parse(com.github.ontio.common.Helper.reverse("8c15299cc6843e808b42f1ffb9cff7ec36f81ea1")).toBase58();
        System.out.println("addr；"+contractAddress);*/

        int txnCount = 4003;
        int activeAddrCount = 2880;
        int allActiveAddrCount = 2929;
        int allTxnCount = 5835;

        BigDecimal a = new BigDecimal(txnCount).divide(new BigDecimal(activeAddrCount), 2, ROUND_HALF_DOWN);
        BigDecimal b = new BigDecimal(allTxnCount).divide(new BigDecimal(allActiveAddrCount), 2, ROUND_HALF_DOWN);
        BigDecimal x = new BigDecimal("1.5").multiply(new BigDecimal("2")).multiply(a.divide(b, 2, ROUND_HALF_DOWN));
        double exp = -x.divide(new BigDecimal("2")).doubleValue();

        BigDecimal sapp = x.multiply(new BigDecimal(Math.exp(exp)));
        BigDecimal appScore = sapp.multiply(new BigDecimal(txnCount));
        int score = appScore.setScale(4, BigDecimal.ROUND_HALF_DOWN).intValue();
        System.out.println("score:" + score);


        BigDecimal temp = new BigDecimal(score).divide(new BigDecimal(score), 2, ROUND_HALF_DOWN);
        BigDecimal ongreward = new BigDecimal("19841").divide(new BigDecimal("5"), 2, ROUND_HALF_DOWN).multiply(temp);
        System.out.println("ongreward:" + ongreward);

    }

    @Test
    public void dappBindedNodeTest() throws Exception {


        String dappContractHash = "75706715336de4d64fbe40f348993097b1863d3b";
        String contractHash = "939053a288f44eb560cad17c36df5ad34dafca2c";

        OntSdk ontSdk = OntSdk.getInstance();
        ontSdk.setRestful("http://dappnode2.ont.io:20334");


        List paramList = new ArrayList();
        paramList.add("get_binded_node".getBytes());

        List args = new ArrayList();
        args.add(Helper.hexToBytes(dappContractHash));
        paramList.add(args);
        byte[] params = BuildParams.createCodeParamsScript(paramList);

        Transaction tx = ontSdk.vm().makeInvokeCodeTransaction(Helper.reverse(contractHash), null, params, null, 20000, 500);
        Object obj = ontSdk.getConnect().sendRawTransactionPreExec(tx.toHexString());

        String result = ((JSONObject) obj).getString("Result");
        if (com.github.ontio.util.Helper.isNotEmptyOrNull(result)) {

            Map map = (Map) BuildParams.deserializeItem(Helper.hexToBytes(result));
           System.out.println("node_name:"+new String(Helper.hexToBytes(((String) map.get("node_name")))));
           System.out.println("node_pubkey:"+map.get("node_pubkey"));
        }
    }


    @Test
    public void dappBindedWalletTest() throws Exception {


        String dappContractHash = "5389914e9ad96cc8a15a3e961440334719270cf8";
        String contractHash = "939053a288f44eb560cad17c36df5ad34dafca2c";

        OntSdk ontSdk = OntSdk.getInstance();
        ontSdk.setRestful("http://dappnode1.ont.io:20334");

        List paramList = new ArrayList();
        paramList.add("get_binded_dapp".getBytes());

        List args = new ArrayList();
        args.add(Helper.hexToBytes(dappContractHash));
        paramList.add(args);
        byte[] params = BuildParams.createCodeParamsScript(paramList);

        Transaction tx = ontSdk.vm().makeInvokeCodeTransaction(Helper.reverse(contractHash), null, params, null, 20000, 500);
        Object obj = ontSdk.getConnect().sendRawTransactionPreExec(tx.toHexString());

        String result = ((JSONObject) obj).getString("Result");
        System.out.println("result:"+result);
/*        if (com.github.ontio.util.Helper.isNotEmptyOrNull(result)) {

            Map<String,Object> map = (Map<String,Object>) BuildParams.deserializeItem(Helper.hexToBytes(result));
            for(String key:map.keySet()){
                System.out.println("address:"+key);
            }

        }*/
        if (com.github.ontio.util.Helper.isNotEmptyOrNull(result)) {

            Map map = (Map) BuildParams.deserializeItem(Helper.hexToBytes(result));
            System.out.println("receive_account:"+ Address.parse((String) map.get("receive_account")).toBase58());
            System.out.println("ont_id:"+new String(Helper.hexToBytes((String) map.get("ontid"))));
        }
    }

}
>>>>>>> Stashed changes
