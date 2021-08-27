package com.github.ontio;

import com.github.ontio.config.ParamsConfig;
import com.github.ontio.util.OntologySDKService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/4/2
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class updateContractInfoTest {

    @Autowired
    private ParamsConfig paramsConfig;


    private OntologySDKService sdk;

    private synchronized void initSDK() {
        if (sdk == null) {
            sdk = OntologySDKService.getInstance(paramsConfig);
        }
    }

    @Test
    public void updateContract() {


        // summaryService.summaryAllInfo();


//        summaryService.updateDailyContractInfoAndContractNewAddress(1554048003);

    }

    @Test
    public void queryNodeWalletTest(){
        initSDK();

        String contractHash = "788e5d5523f5092e351918d8f9d570f788e53beb";

        Map bindedNodeInfo = sdk.getDappBindedNodeInfo("939053a288f44eb560cad17c36df5ad34dafca2c", contractHash);
        System.out.println("node:"+bindedNodeInfo);
    }


}
