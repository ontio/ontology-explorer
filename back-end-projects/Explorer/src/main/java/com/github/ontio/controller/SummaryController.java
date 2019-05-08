package com.github.ontio.controller;

import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.service.impl.SummaryServiceImpl;
import com.github.ontio.util.Helper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        log.info("###{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        return summaryService.getBlockChainLatestInfo();
    }

    @ApiOperation(value = "Get blockchain tps information")
    @GetMapping(value = "/blockchain/tps")
    public ResponseBean getBlockChainTps() {

        log.info("###{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        return summaryService.getBlockChainTps();
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
}
