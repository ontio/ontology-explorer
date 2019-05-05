package com.github.ontio.controller;

import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.service.impl.SummaryServiceImpl;
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
}
