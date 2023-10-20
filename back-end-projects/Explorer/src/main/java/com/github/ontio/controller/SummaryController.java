package com.github.ontio.controller;

import com.github.ontio.aop.RequestLimit;
import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.service.impl.SummaryServiceImpl;
import com.github.ontio.util.ConstantParam;
import com.github.ontio.util.ErrorInfo;
import com.github.ontio.util.Helper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Slf4j
@RestController
public class SummaryController {

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private final SummaryServiceImpl summaryService;

    @Autowired
    public SummaryController(SummaryServiceImpl summaryService) {
        this.summaryService = summaryService;
    }


    @ApiOperation(value = "Get blockchain latest summary information")
    @GetMapping(value = "/v2/summary/blockchain/latest-info")
    public ResponseBean getBlockChainLatestInfo() {

        log.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        return summaryService.getBlockChainLatestInfo();
    }

    @ApiOperation(value = "Get blockchain tps information")
    @GetMapping(value = "/v2/summary/blockchain/tps")
    public ResponseBean getBlockChainTps() {

        log.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        return summaryService.getBlockChainTps();
    }

    @RequestLimit(count = 120)
    @ApiOperation(value = "Get blockchain daily summary information")
    @GetMapping(value = "/v2/summary/blockchain/daily")
    public ResponseBean getBlockChainSummary(@RequestParam("start_time") Long startTime,
                                             @RequestParam("end_time") Long endTime) {

        log.info("####{}.{} begin...start_time:{},end_time:{}", CLASS_NAME, Helper.currentMethod(), startTime, endTime);

        if (Helper.isTimeRangeExceedLimit(startTime, endTime)) {
            return new ResponseBean(ErrorInfo.TIME_RANGE_EXCEED.code(), ErrorInfo.TIME_RANGE_EXCEED.desc(), false);
        }
        return summaryService.getBlockChainDailySummary(startTime, endTime);
    }


    @RequestLimit(count = 120)
    @ApiOperation(value = "Get contract daily summary information")
    @GetMapping(value = "/v2/summary/contracts/{contract_hash}/daily")
    public ResponseBean getContractSummary(@PathVariable("contract_hash") @Length(min = 40, max = 40, message = "Incorrect contract hash") String contractHash,
                                           @RequestParam("start_time") Long startTime,
                                           @RequestParam("end_time") Long endTime) {

        log.info("####{}.{} begin...start_time:{},end_time:{}", CLASS_NAME, Helper.currentMethod(), startTime, endTime);

        if (Helper.isTimeRangeExceedLimit(startTime, endTime)) {
            return new ResponseBean(ErrorInfo.TIME_RANGE_EXCEED.code(), ErrorInfo.TIME_RANGE_EXCEED.desc(), false);
        }
        return summaryService.getContractDailySummary(contractHash, startTime, endTime);
    }


    @ApiOperation(value = "Get ONT,ONG total supply")
    @GetMapping(value = "/v2/summary/native/totalsupply")
    public ResponseBean queryNativeTotalSupply() {
        log.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        ResponseBean rs = summaryService.getNativeTotalSupply();
        return rs;
    }


    @ApiOperation(value = "Get ONT,ONG total supply")
    @GetMapping(value = "/api/v1/explorer/summary/native/totalsupply")
    public ResponseBean queryNativeTotalSupplyV1() {
        log.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        ResponseBean rs = summaryService.getNativeTotalSupply();
        return rs;
    }

    @ApiOperation(value = "Get ONT,ONG total supply")
    @GetMapping(value = "/v2/summary/native/circulating-supply/{token}")
    public BigDecimal queryNativeTotalCirculatingSupply(@PathVariable String token) {
        log.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        BigDecimal rs = summaryService.queryNativeTotalCirculatingSupply(token);
        return rs;
    }

    @ApiOperation(value = "Get ONT,ONG total supply")
    @GetMapping(value = "/v2/summary/native/total-supply/{token}")
    public BigDecimal queryNativeTotalSupply(@PathVariable String token) {
        if (ConstantParam.ONT.equalsIgnoreCase(token) || ConstantParam.ONG.equalsIgnoreCase(token)) {
            return new BigDecimal("1000000000");
        } else {
            return null;
        }
    }
}
