package com.github.ontio.controller;

import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.service.impl.SummaryServiceImpl;
import com.github.ontio.util.Helper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v2/summaries")
public class SummaryController {

    @Autowired
    private SummaryServiceImpl summaryService;

    @ApiOperation(value = "Get blockchain latest summary information")
    @GetMapping(value = "/blockchain/latest-info")
    public ResponseBean getLatestInfo() {
        return summaryService.getLatestInfo();
    }

    @ApiOperation(value = "Get blockchain tps information")
    @GetMapping(value = "/blockchain/tps")
    public ResponseBean getTps() {
        return summaryService.getTps();
    }

    @ApiOperation(value = "Get blockchain daily or weekly or monthly summary information")
    @GetMapping(value = "/blockchain/{type}/{start_time}/{end_time}")
    public ResponseBean getChainSummary(@PathVariable("type") String type, @PathVariable("start_time") int startTime,
                                             @PathVariable("end_time") int endTime) {
        return summaryService.getChainSummary(type, startTime, endTime);
    }

    @ApiOperation(value = "Get contract daily or weekly or monthly summary information")
    @GetMapping(value = "/contract/{contract_hash}/{type}/{start_time}/{end_time}")
    public ResponseBean getContractSummary(@PathVariable("contract_hash") String contractHash, @PathVariable("type") String type,
                                           @PathVariable("start_time") int startTime, @PathVariable("end_time") int endTime) {
        return summaryService.getContractSummary(contractHash, type, startTime, endTime);
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
     * 项目统计
     *
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
