package com.github.ontio.controller;

import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.service.impl.SummaryServiceImpl;
import com.github.ontio.util.ErrorInfo;
import com.github.ontio.util.Helper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/v2/summary")
public class SummaryController {

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private final SummaryServiceImpl summaryService;

    @Autowired
    public SummaryController(SummaryServiceImpl summaryService) {
        this.summaryService = summaryService;
    }


    @ApiOperation(value = "Get blockchain latest summary information")
    @GetMapping(value = "/blockchain/latest-info")
    public ResponseBean getBlockChainLatestInfo() {

        log.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        return summaryService.getBlockChainLatestInfo();
    }

    @ApiOperation(value = "Get blockchain tps information")
    @GetMapping(value = "/blockchain/tps")
    public ResponseBean getBlockChainTps() {

        log.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        return summaryService.getBlockChainTps();
    }

    @ApiOperation(value = "Get blockchain daily summary information")
    @GetMapping(value = "/blockchain/daily")
    public ResponseBean getBlockChainSummary(@RequestParam("start_time") Long startTime,
                                        @RequestParam("end_time") Long endTime) {

        log.info("####{}.{} begin...start_time:{},end_time:{}", CLASS_NAME, Helper.currentMethod(), startTime, endTime);

        if (Helper.isTimeRangeExceedLimit(startTime, endTime)) {
            return new ResponseBean(ErrorInfo.REQ_TIME_EXCEED.code(), ErrorInfo.REQ_TIME_EXCEED.desc(), false);
        }
        return summaryService.getBlockChainDailySummary(startTime, endTime);
    }


    @ApiOperation(value = "Get contract daily summary information")
    @GetMapping(value = "/contracts/{contract_hash}/daily")
    public ResponseBean getContractSummary(@PathVariable("contract_hash") @Length(min = 40, max = 40, message = "Incorrect contract hash") String contractHash,
                                           @RequestParam("start_time") Long startTime,
                                           @RequestParam("end_time") Long endTime) {

        log.info("####{}.{} begin...start_time:{},end_time:{}", CLASS_NAME, Helper.currentMethod(), startTime, endTime);

        if (Helper.isTimeRangeExceedLimit(startTime, endTime)) {
            return new ResponseBean(ErrorInfo.REQ_TIME_EXCEED.code(), ErrorInfo.REQ_TIME_EXCEED.desc(), false);
        }
        return summaryService.getContractDailySummary(contractHash, startTime, endTime);
    }
}
