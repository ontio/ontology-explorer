package com.github.ontio;

import com.github.ontio.service.impl.SummaryServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/4/2
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class updateContractInfoTest {

    @Resource
    private SummaryServiceImpl summaryService;


    @Test
    public void updateContract(){


        summaryService.summaryAllInfo();


//        summaryService.updateDailyContractInfoAndContractNewAddress(1554048003);

    }


}
