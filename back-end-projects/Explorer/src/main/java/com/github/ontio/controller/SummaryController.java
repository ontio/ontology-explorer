package com.github.ontio.controller;

import com.github.ontio.paramBean.Result;
import com.github.ontio.service.impl.SummaryServiceImpl;
import com.github.ontio.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

/**
 * @author king
 * @version 1.0
 * @date 2018/12/17
 */
@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/api/v1/explorer")
public class SummaryController {

    private static final Logger logger = LoggerFactory.getLogger(SummaryController.class);

    private final String CLASS_NAME = this.getClass().getSimpleName();

    @Autowired
    private SummaryServiceImpl summaryService;

    /**
     * query all address information
     *
     * @return
     */
    @RequestMapping(value = "/summaryAllInfo", method = RequestMethod.POST)
    @ResponseBody
    public Result summaryAllInfo() {
        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        Result rs = summaryService.summaryAllInfo();
        return rs;
    }

    /**
     * query current summary information
     *
     * @return
     */
    @RequestMapping(value = "/summary/{amount}", method = RequestMethod.GET)
    @ResponseBody
    public Result querySummary(@PathVariable("amount") int amount) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        Result rs = summaryService.querySummary(amount);
        return rs;
    }

    /**
     * 统计 TPS
     * @return
     */
    @RequestMapping(value = "/summary/tps", method = RequestMethod.GET)
    public Result queryTps() {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        Result rs = summaryService.queryTps();
        return rs;
    }

    /**
     * 项目统计
     * @return
     */
    @RequestMapping(value = "/summary/project/{project}/{type}/{starttime}/{endtime}", method = RequestMethod.GET)
    public Result queryProjectInfo(@PathVariable("project") String project,
                                   @PathVariable("type") String type,
                                   @PathVariable("starttime") int startTime,
                                   @PathVariable("endtime") int endTime) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        Result rs = summaryService.queryProjectInfo(project, type, startTime, endTime);
        return rs;
    }

    /**
     * 日常统计
     * @param type
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "/summary/{type}/{starttime}/{endtime}", method = RequestMethod.GET)
    public Result querySummary(@PathVariable("type") String type,
                               @PathVariable("starttime") int startTime,
                               @PathVariable("endtime") int endTime) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        logger.info("####startTime:{}, endTime:{}", startTime, endTime);

        Result rs = summaryService.querySummary(type, startTime, endTime);
        return rs;
    }

    /**
     * 合约的日常统计
     *
     * @param contractHash
     * @param type
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "/summary/contract/{contracthash}/{type}/{starttime}/{endtime}", method = RequestMethod.GET)
    public Result queryContract(@PathVariable("contracthash") String contractHash,
                                @PathVariable("type") String type,
                                @PathVariable("starttime") int startTime,
                                @PathVariable("endtime") int endTime) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        logger.info("####startTime:{}, endTime:{}", startTime, endTime);

        Result rs = summaryService.queryContract(contractHash, type, startTime, endTime);
        return rs;
    }

    /**
     * 查询流通量
     *
     * @return
     */
    @GetMapping(value = "/summary/totalsupply")
    public Result queryTotalSupply() {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        Result rs = summaryService.queryTotalSupply();
        return rs;
    }
}
