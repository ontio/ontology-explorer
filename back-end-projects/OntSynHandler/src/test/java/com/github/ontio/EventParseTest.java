package com.github.ontio;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.ontio.common.Address;
import com.github.ontio.common.Helper;
import com.github.ontio.core.payload.DeployCode;
import com.github.ontio.model.common.EventTypeEnum;
import com.github.ontio.utils.ConstantParam;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/3/28
 */
public class EventParseTest {

    @Test
    public void parse(){

        List<String> a = new ArrayList<>();
        a.add("1");
        a.add("2");
        List<String> b = a.subList(2,2);
        System.out.println(""+b.size());

       BigDecimal eventAmount = new BigDecimal(Helper.BigIntFromNeoBytes(Helper.hexToBytes((String) "01")).longValue());
       System.out.println("amount:"+eventAmount);
        BigDecimal amount = eventAmount.divide(new BigDecimal(Math.pow(10,6)));
        System.out.println("amount:"+amount);



    }

    @Test
    public void parseEvent() {
        try {

            System.out.println("code:"+Helper.reverse("792e4e61746976652e496e766f6b65"));

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

    @Test
    public void parseCLaimRecordTx() throws Exception{

        String nodeRestfulUrl = "http://dappnode1.ont.io:20334";
        OntSdk ontSdk = OntSdk.getInstance();
        ontSdk.setRestful(nodeRestfulUrl);

        String txHash = "00002e607b2d5e353c31a0db43afa4212500e35ff4807d9c7da0537454e438c5";
        JSONObject eventObj = (JSONObject) ontSdk.getConnect().getSmartCodeEvent(txHash);
        System.out.println("eventObj:" + eventObj.toJSONString());

        JSONArray notifyArray = eventObj.getJSONArray("Notify");

        JSONArray stateList = ((JSONObject)notifyArray.get(0)).getJSONArray("States");

        String actionType = new String(Helper.hexToBytes(stateList.getString(0)));
        StringBuilder sb = new StringBuilder(140);
        sb.append(EventTypeEnum.Claimrecord.des());

        if (ConstantParam.CLAIMRECORD_OPE_PREFIX.equals(actionType)) {
            if (stateList.size() >= 4) {
                String issuerOntId = new String(Helper.hexToBytes(stateList.getString(1)));
                String action = new String(Helper.hexToBytes(stateList.getString(2)));
                String claimId = new String(Helper.hexToBytes(stateList.getString(3)));
                System.out.println("action:"+action+",issureOntId:"+issuerOntId+",claimId:"+claimId);
                sb.append(issuerOntId);
                sb.append(action);
                sb.append("claimId:");
                sb.append(claimId);
            }
        }
        System.out.println("content:"+sb.toString());

    }

    @Test
    public void ParseOep4Tx() throws Exception{

        String nodeRestfulUrl = "http://dappnode1.ont.io:20334";
        OntSdk ontSdk = OntSdk.getInstance();
        ontSdk.setRestful(nodeRestfulUrl);


        String txHash = "03cef6720e3935bc684ba98d32129b1206f5d076ebf0eaa22e485cc5922ffa54";
        JSONObject eventObj = (JSONObject) ontSdk.getConnect().getSmartCodeEvent(txHash);
        System.out.println("eventObj:" + eventObj.toJSONString());

        JSONArray notifyArray = eventObj.getJSONArray("Notify");

        JSONArray stateArray = ((JSONObject)notifyArray.get(0)).getJSONArray("States");

        String fromAddress = (String) stateArray.get(1);
        if (40 == fromAddress.length()) {
            fromAddress = Address.parse(fromAddress).toBase58();
        }
        String toAddress = (String) stateArray.get(2);
        if (40 == toAddress.length()) {
            toAddress = Address.parse(toAddress).toBase58();
        }

        BigDecimal eventAmount = new BigDecimal(Helper.BigIntFromNeoBytes(Helper.hexToBytes((String) stateArray.get(3))).longValue());
        System.out.println("fromaddress:"+fromAddress+",toaddress:"+toAddress+",amount:"+eventAmount);

    }

    @Test
    public void parseContract() throws Exception{

        String nodeRestfulUrl = "http://dappnode1.ont.io:20334";
        OntSdk ontSdk = OntSdk.getInstance();
        ontSdk.setRestful(nodeRestfulUrl);

       // String txHash = "bdbd56ca56bb5e5d6b1cebd4379f2eb8a3fb39d061075edf1cc0b756cbcc323e";
        String txHash = "152532973f3d35c1b420440487fe1098d5f04ad9076613ec72434931bddd4fd4";
        DeployCode deployCodeObj = (DeployCode) ontSdk.getConnect().getTransaction(txHash);
        //根据code转成合约hash
        String code = Helper.toHexString(deployCodeObj.code);
        String contractHash = Address.AddressFromVmCode(code).toHexString();
        System.out.println("contractHash:"+contractHash);

        //1514857e55b0f711af93c9b5a3a3eb32b53efaab
        //4e4a9b860fb7ffba41f91ea112712191bd7eca53
        JSONObject contractObj = (JSONObject) ontSdk.getConnect().getContractJson(contractHash);
        System.out.println("contract:"+contractObj.toJSONString());

    }

}
