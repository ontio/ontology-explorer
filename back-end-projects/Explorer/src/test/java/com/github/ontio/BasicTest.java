package com.github.ontio;

import com.alibaba.fastjson.JSONObject;
import com.github.ontio.common.Helper;
import com.github.ontio.core.transaction.Transaction;
import com.github.ontio.mapper.BlockMapper;
import com.github.ontio.smartcontract.neovm.abi.BuildParams;
import com.github.ontio.util.ConstantParam;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
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
    public void test01() {

        String redisKey = "362484804:1750070316:com.github.ontio.mapper.BlockMapper.selectOneByHeight:0:2147483647:select";

        String packageName = BlockMapper.class.getPackage().getName();
        int index = redisKey.indexOf(packageName);
        if (index > 0) {
            String str = redisKey.substring(index + packageName.length() + 1, redisKey.length());
            int dex = str.indexOf(":");
            String s = str.substring(0, dex);
            if (ConstantParam.REDIS_LONGEXPIRETIME_KEYLIST.contains(s)) {
                System.out.println("ssssss");
            }
        }


        String[] arr = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar2 = Calendar.getInstance();
        //calendar2.setTimeInMillis(1553875800);
        System.out.println("今天是：" + arr[calendar2.get(Calendar.DAY_OF_WEEK) - 1]); //1.数组下标从0开始；2.老外的第一天是从星期日开始的


        double e = -new BigDecimal("1").divide(new BigDecimal("2")).doubleValue();
        System.out.println("e:" + e);
        double aa = Math.exp(e);
        System.out.println("aa:" + aa);


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

}
