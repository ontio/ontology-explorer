package com.github.ontio;

import com.alibaba.fastjson.JSONObject;
import com.github.ontio.common.Address;
import com.github.ontio.common.Helper;
import com.github.ontio.core.asset.State;
import com.github.ontio.core.payload.InvokeCode;
import com.github.ontio.core.transaction.Transaction;
import com.github.ontio.smartcontract.nativevm.abi.NativeBuildParams;
import com.github.ontio.smartcontract.nativevm.abi.Struct;
import com.github.ontio.smartcontract.neovm.abi.BuildParams;
import com.github.ontio.util.ConstantParam;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
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
            System.out.println("node_name:" + new String(Helper.hexToBytes(((String) map.get("node_name")))));
            System.out.println("node_pubkey:" + map.get("node_pubkey"));
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
        System.out.println("result:" + result);
/*        if (com.github.ontio.util.Helper.isNotEmptyAndNull(result)) {

            Map<String,Object> map = (Map<String,Object>) BuildParams.deserializeItem(Helper.hexToBytes(result));
            for(String key:map.keySet()){
                System.out.println("address:"+key);
            }

        }*/
        if (com.github.ontio.util.Helper.isNotEmptyAndNull(result)) {

            Map map = (Map) BuildParams.deserializeItem(Helper.hexToBytes(result));
            System.out.println("receive_account:" + Address.parse((String) map.get("receive_account")).toBase58());
            System.out.println("ont_id:" + new String(Helper.hexToBytes((String) map.get("ontid"))));
        }
    }

    @Test
    public void testDecodeInputData() throws Exception {
        String inputData = "00c66b423032363732326665643233653439343136323635383962326335386132396261663334306166356536316634306132653463383932643231333862386634393766636a7cc814a6133b354f4060d038deacae97bbb0084defd28e6a7cc8081c270000000000006a7cc8296469643a6f6e744157757a725a3961434b32686e684256514a367a32655a39364362737743675165776a7cc8516a7cc86c11726567697374657243616e6469646174651400000000000000000000000000000000000000070068164f6e746f6c6f67792e4e61746976652e496e766f6b65";
        int nativeInvokeIndex = inputData.lastIndexOf(ConstantParam.NATIVE_INPUT_DATA_END);
        if (inputData.startsWith(ConstantParam.EVM_ADDRESS_PREFIX)) {
            // evm
        } else if (nativeInvokeIndex != -1) {
            // native
            String argsMethodContract = inputData.substring(6, nativeInvokeIndex);
            int length = argsMethodContract.length();
            String contract = argsMethodContract.substring(length - 40);
            String argsMethod = argsMethodContract.substring(0, length - 42);
            String[] args = argsMethod.split(ConstantParam.NATIVE_ARGS_OP_CODE);
            String opSizeMethod = args[args.length - 1];
            long size = 0;
            String method;
            String opMethod = opSizeMethod.substring(2);
            int opPackIndex = opMethod.lastIndexOf(ConstantParam.OP_PACK);
            if (opPackIndex != -1) {
                // 参数为list
                String methodHex = opMethod.substring(opPackIndex + 2 + 2);
                method = new String(com.github.ontio.common.Helper.hexToBytes(methodHex));
                size = com.github.ontio.util.Helper.parseInputDataNumber(opMethod.substring(0, opPackIndex), true).longValue();
            } else {
                // 考虑到方法名不会大于255个字节
                String methodHex = opMethod.substring(2);
                method = new String(com.github.ontio.common.Helper.hexToBytes(methodHex));
            }
            System.out.println("contract:" + contract);
            System.out.println("method:" + method);
            System.out.println("args:" + Arrays.toString(args));
            System.out.println("size:" + size);
        }
    }

    @Test
    public void testMakeTransfers() throws Exception {
        OntSdk ontSdk = OntSdk.getInstance();
        ontSdk.setRestful("http://dappnode1.ont.io:20334");
        Address from = Address.decodeBase58("AWuzrZ9aCK2hnhBVQJ6z2eZ96CbswCgQew");
        Address to = Address.decodeBase58("AXTCgkrLSxYWbYGK98CbsD6Ur8WVqEh8Fb");
        long amount = -1;
        State[] states = new State[]{new State(from, to, amount), new State(to, from, amount)};
        InvokeCode transaction = (InvokeCode) ontSdk.nativevm().ongV2().makeTransfer(states, "AXTCgkrLSxYWbYGK98CbsD6Ur8WVqEh8Fb", 400000, 2500);
        System.out.println(Helper.toHexString(transaction.code));
    }

    @Test
    public void testMakeApprove() throws Exception {
        OntSdk ontSdk = OntSdk.getInstance();
        BigInteger amount = new BigInteger("1000000000000000000000000000");
        InvokeCode transaction = (InvokeCode) ontSdk.nativevm().ongV2().makeApprove("AWuzrZ9aCK2hnhBVQJ6z2eZ96CbswCgQew", "AXTCgkrLSxYWbYGK98CbsD6Ur8WVqEh8Fb", amount, "AWuzrZ9aCK2hnhBVQJ6z2eZ96CbswCgQew", 400000, 2500);
        System.out.println(Helper.toHexString(transaction.code));
    }

    @Test
    public void testMakeRegisterOntId() throws Exception {
        OntSdk ontSdk = OntSdk.getInstance();
//        byte[] pubKey = Helper.hexToBytes("");
        byte[] pubKey = "020202020202f7af8eacdc1723f9c08a31f68e0e22001738e8e1f58c903bc305ecb883553e2202f7af8eacdc1723f9c08a31f68e0e22001738e8e1f58c903bc305ecb883553e2202f7af8eacdc1723f9c08a31f68e0e22001738e8e1f58c903bc305ecb883553e2202f7af8eacdc1723f9c08a31f68e0e22001738e8e1f58c903bc305eHA".getBytes();
        String ontId = "did:ont:AWuzrZ9aCK2hnhBVQJ6z2eZ96CbswCgQew";
        InvokeCode transaction = (InvokeCode) ontSdk.nativevm().ontId().makeRegister(ontId, pubKey, "AWuzrZ9aCK2hnhBVQJ6z2eZ96CbswCgQew", 400000, 2500);
        System.out.println(Helper.toHexString(transaction.code));
    }

    @Test
    public void testMakeNoParamsTx() throws Exception {
        OntSdk ontSdk = OntSdk.getInstance();
        InvokeCode transaction = (InvokeCode) ontSdk.vm().buildNativeParams(new Address(Helper.hexToBytes("0000000000000000000000000000000000000007")), "callSplit", new byte[0], "AWuzrZ9aCK2hnhBVQJ6z2eZ96CbswCgQew", 400000, 2500);
        System.out.println(Helper.toHexString(transaction.code));
    }

    @Test
    public void testMakeArrayParamsTx() throws Exception {
        String[] peerPubkey = new String[]{"123", "456", "abcdefg"};
        long[] posList = {100000000L, 200000000L, 3000000000000000000L};
        OntSdk ontSdk = OntSdk.getInstance();
        List list = new ArrayList();
        Struct struct = new Struct();
        struct.add(new Object[]{Address.decodeBase58("AWuzrZ9aCK2hnhBVQJ6z2eZ96CbswCgQew")});
        struct.add(new Object[]{peerPubkey.length});

        int i;
        for (i = 0; i < peerPubkey.length; ++i) {
            struct.add(new Object[]{peerPubkey[i]});
        }

        struct.add(new Object[]{posList.length});

        for (i = 0; i < peerPubkey.length; ++i) {
            struct.add(new Object[]{posList[i]});
        }

        list.add(struct);
        byte[] args = NativeBuildParams.createCodeParamsScript(list);
        InvokeCode transaction = (InvokeCode) ontSdk.vm().buildNativeParams(new Address(Helper.hexToBytes("0000000000000000000000000000000000000007")), "authorizeForPeer", args, "AWuzrZ9aCK2hnhBVQJ6z2eZ96CbswCgQew", 400000, 2500);
        System.out.println(Helper.toHexString(transaction.code));
    }

    @Test
    public void testDeleteStringBuild() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("123(String 1,String 2,");
        int length = stringBuilder.length();
        if (length > 1) {
            stringBuilder.delete(length - 1, length);
        }
        System.out.println(stringBuilder);
    }


}
