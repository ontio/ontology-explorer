package com.github.ontio;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.ontio.common.Helper;
import org.junit.Test;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/3/28
 */
public class EventParseTest {

    @Test
    public void parseEvent() {
        try {

            String contractAddress2 = Helper.reverse("ecd1fcc4ed4d2f14508afa9418ded28eba138c60");

            System.out.println("contractAddress:" + contractAddress2);

            String nodeRestfulUrl = "http://dappnode1.ont.io:20334";
            OntSdk ontSdk = OntSdk.getInstance();
            ontSdk.setRestful(nodeRestfulUrl);

            String txHash = "b2a73f5b42aefd8ac03baec2c97ba95285f0ff681f1bc679668b80fa48c4cf39";
            JSONObject eventObj = (JSONObject) ontSdk.getConnect().getSmartCodeEvent(txHash);
            System.out.println("eventObj:" + eventObj.toJSONString());

            JSONArray notifyList = eventObj.getJSONArray("Notify");
            JSONObject notifyObj = (JSONObject) notifyList.get(0);

            String contractAddress = notifyObj.getString("ContractAddress");
            System.out.println("contractAddress:" + contractAddress);

            JSONArray stateArray = (JSONArray) notifyObj.get("States");


            String action = new String(Helper.hexToBytes((String) stateArray.get(0)));
            System.out.println("action:" + action);


/*            String fromAddress = Address.parse(Helper.reverse((String) stateArray.get(1))).toBase58();
            System.out.println("address:" + fromAddress);

            String toAddress = Address.parse(Helper.reverse("27fa4812f657a9f320003d651a6c3bae7f908226")).toBase58();
            System.out.println("toAddress:" + toAddress);

*//*            BigDecimal amount = new BigDecimal(Helper.BigIntFromNeoBytes(Helper.hexToBytes("01")).longValue());
            System.out.println("amount:"+amount);*//*

            BigDecimal oepAmount = new BigDecimal(Helper.BigIntFromNeoBytes(Helper.hexToBytes("300731e700")).longValue());
            System.out.println("oepAmount:" + oepAmount);*/
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Test
    public void test02() {

        String code = "";
        String calledContractHash = "";
        try {
            while (code.contains("67")) {
                int index = code.indexOf("67");
                code = code.substring(index + 2);
                if (code.length() < 40) {
                    break;
                }else if(code.length() == 40){
                    calledContractHash =Helper.reverse(code);
                    break;
                }
            }
            System.out.println("hash:" + calledContractHash);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
