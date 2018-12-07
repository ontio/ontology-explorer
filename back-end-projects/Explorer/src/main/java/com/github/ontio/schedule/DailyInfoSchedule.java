package com.github.ontio.schedule;

import com.github.ontio.dao.BlockMapper;
import com.github.ontio.dao.DailyMapper;
import com.github.ontio.dao.OntIdMapper;
import com.github.ontio.dao.TransactionDetailMapper;
import com.github.ontio.model.Daily;
import com.github.ontio.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/12/6
 */
@Component
@EnableScheduling
public class DailyInfoSchedule {


    private static final Logger logger = LoggerFactory.getLogger(DailyInfoSchedule.class);

    private static final String CLASS_NAME = DailyInfoSchedule.class.getSimpleName();

    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private TransactionDetailMapper transactionDetailMapper;
    @Autowired
    private OntIdMapper ontIdMapper;
    @Autowired
    private DailyMapper dailyMapper;


    @Scheduled(cron = "0 0 0 * * *")
    public void UpdateDailyInfo() {

        logger.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        long endTime = System.currentTimeMillis() / 1000L;
        long startTime = endTime - 24 * 60 * 60;

        int blockCount = blockMapper.selectBlockCountInOneDay(startTime, endTime);
        int txnCount = transactionDetailMapper.selectTxnCountInOneDay(startTime, endTime);

        String ontIdDes = "Register%";
        int ontidCount = ontIdMapper.selectOntIdCountInOneDay(startTime, endTime, ontIdDes);

        Daily dailyDAO = new Daily();
        dailyDAO.setBlockcount(blockCount);
        dailyDAO.setTxncount(txnCount);
        dailyDAO.setOntidcount(ontidCount);
        dailyDAO.setTime(new Date(startTime * 1000L));

        dailyMapper.insert(dailyDAO);
    }


}
