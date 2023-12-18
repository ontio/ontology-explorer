package com.github.ontio.controller;

import com.github.ontio.aop.RequestLimit;
import com.github.ontio.model.dto.Anniversary6thDataDto;
import com.github.ontio.service.IActivityDataService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@Slf4j
@RestController
@RequestMapping(value = "/v2/data")
public class ActivityDataController {

    @Autowired
    private IActivityDataService activityDataService;

    @RequestLimit(count = 120)
    @ApiOperation(value = "Get address 6th anniversary data")
    @GetMapping(value = "/6th_anniversary/{address}")
    public Anniversary6thDataDto queryAddress6thAnniversaryData(@PathVariable("address") @Length(min = 34, max = 42, message = "Incorrect address format") String address) {
        return activityDataService.queryAddress6thAnniversaryData(address);
    }

    @RequestLimit(count = 120)
    @ApiOperation(value = "Get address tx count in a period")
    @GetMapping(value = "/tx_count_in_period")
    public Integer queryAddressTxCountInPeriod(@RequestParam @Length(min = 34, max = 42, message = "Incorrect address format") String address, @RequestParam(required = false) Integer startTime, @RequestParam(required = false) Integer endTime) {
        return activityDataService.queryAddressTxCountInPeriod(address, startTime, endTime);
    }

    @RequestLimit(count = 120)
    @ApiOperation(value = "Get address balance at a certain time")
    @GetMapping(value = "/certain_time_balance")
    public String queryAddressCertainTimeBalance(@RequestParam @Length(min = 34, max = 42, message = "Incorrect address format") String address, @RequestParam Integer timestamp) {
        return activityDataService.queryAddressCertainTimeBalance(address, timestamp);
    }
}
