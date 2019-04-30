package com.github.ontio.controller;

import com.github.ontio.paramBean.OldResult;
import com.github.ontio.service.impl.SummaryServiceImpl;
import com.github.ontio.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author king
 * @version 1.0
 * @date 2018/12/17
 */
@RestController
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
    public OldResult summaryAllInfo() {
        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        OldResult rs = summaryService.summaryAllInfo();
        return rs;
    }

    @RequestMapping(value = "/blockCountInTwoWeeks/{time}", method = RequestMethod.GET)
    @ResponseBody
    public OldResult blockCountInTwoWeeks(@PathVariable("time") long time) {
        OldResult rs = blockService.blockCountInTwoWeeks(time);
        return rs;
    }

    /**
     * query current summary information
     *
     * @return
     */
    @RequestMapping(value = "/summary/{amount}", method = RequestMethod.GET)
    @ResponseBody
    public OldResult querySummary(@PathVariable("amount") int amount) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        OldResult rs = summaryService.querySummary(amount);
        return rs;
    }

    /**
     * 统计 TPS
     * @return
     */
    @RequestMapping(value = "/summary/tps", method = RequestMethod.GET)
    public OldResult queryTps() {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        OldResult rs = summaryService.queryTps();
        return rs;
    }

    /**
     * 项目统计
     * @return
     */
    @RequestMapping(value = "/summary/project/{project}/{type}/{starttime}/{endtime}", method = RequestMethod.GET)
    public OldResult queryProjectInfo(@PathVariable("project") String project,
                                      @PathVariable("type") String type,
                                      @PathVariable("starttime") int startTime,
                                      @PathVariable("endtime") int endTime) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        OldResult rs = summaryService.queryProjectInfo(project, type, startTime, endTime);
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
    public OldResult querySummary(@PathVariable("type") String type,
                                  @PathVariable("starttime") int startTime,
                                  @PathVariable("endtime") int endTime) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        logger.info("####startTime:{}, endTime:{}", startTime, endTime);

        OldResult rs = summaryService.querySummary(type, startTime, endTime);
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
    public OldResult queryContract(@PathVariable("contracthash") String contractHash,
                                   @PathVariable("type") String type,
                                   @PathVariable("starttime") int startTime,
                                   @PathVariable("endtime") int endTime) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        logger.info("####startTime:{}, endTime:{}", startTime, endTime);

        OldResult rs = summaryService.queryContract(contractHash, type, startTime, endTime);
        return rs;
    }

    /**
     * 查询ONT流通量
     *
     * @return
     */
    @GetMapping(value = "/summary/totalsupply")
    public OldResult queryTotalSupply() {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        OldResult rs = summaryService.queryTotalSupply();
        return rs;
    }

    /**
     * 查询ONT,ONG流通量
     *
     * @return
     */
    @GetMapping(value = "/summary/native/totalsupply")
    public OldResult queryNativeTotalSupply() {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        OldResult rs = summaryService.queryNativeTotalSupply();
        return rs;
    }

}
