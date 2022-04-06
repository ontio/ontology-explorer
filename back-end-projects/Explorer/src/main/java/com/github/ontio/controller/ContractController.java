/*
 * Copyright (C) 2018 The ontology Authors
 * This file is part of The ontology library.
 *
 * The ontology is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The ontology is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with The ontology.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.ontio.controller;

import com.github.ontio.aop.RequestLimit;
import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.service.impl.ContractServiceImpl;
import com.github.ontio.util.Helper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Slf4j
@Validated
@RestController
@RequestMapping(value = "/v2/contracts")
public class ContractController {

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private final ContractServiceImpl contractService;

    @Autowired
    public ContractController(ContractServiceImpl contractService) {
        this.contractService = contractService;
    }


    @ApiOperation(value = "Get contract list by page")
    @GetMapping
    public ResponseBean queryContractsByPage(@RequestParam("page_size") @Max(20) @Min(1) Integer pageSize,
                                             @RequestParam("page_number") @Min(1) Integer pageNumber) {

        log.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        return contractService.queryContractsByPage(pageSize, pageNumber);
    }


    @ApiOperation("Get contract detail by contract hash")
    @GetMapping(value = "/{contract_hash}")
    public ResponseBean queryContractDetail(@PathVariable("contract_hash") @Length(min = 40, max = 42, message = "Incorrect contract hash") String contractHash) {

        log.info("####{}.{} begin...contract_hash:{}", CLASS_NAME, Helper.currentMethod(), contractHash);

        return contractService.queryContractDetail(contractHash);
    }


    @RequestLimit(count = 120)
    @ApiOperation(value = "Get contract transaction list by contracthash")
    @GetMapping(value = "/{contract_type}/{contract_hash}/transactions")
    public ResponseBean queryContractTxsByPage(@PathVariable("contract_type") @Pattern(regexp = "oep4|OEP4|oep5|OEP5|oep8|OEP8|orc20|ORC20|orc721|ORC721|orc1155|ORC1155|other|OTHER", message = "Incorrect contract type") String contractType,
                                               @PathVariable("contract_hash") @Length(min = 40, max = 42, message = "Incorrect contract hash") String contractHash,
                                               @RequestParam("page_number") @Min(1) Integer pageNumber,
                                               @RequestParam("page_size") @Max(20) @Min(1) Integer pageSize) {

        log.info("####{}.{} begin...contract_type:{},contract_hash:{}", CLASS_NAME, Helper.currentMethod(), contractType, contractHash);

        return contractService.queryTxsByContractHash(contractType, contractHash, pageNumber, pageSize);
    }

    @RequestLimit(count = 120)
    @ApiOperation(value = "Get contract transaction list by contracthash(no contract_type param)")
    @GetMapping(value = "/{contract_hash}/transactions")
    public ResponseBean queryContractTxsByPage(@PathVariable("contract_hash") @Length(min = 40, max = 42, message = "Incorrect contract hash") String contractHash,
                                               @RequestParam("page_number") @Min(1) Integer pageNumber,
                                               @RequestParam("page_size") @Max(20) @Min(1) Integer pageSize) {

        log.info("####{}.{} begin...contract_hash:{}", CLASS_NAME, Helper.currentMethod(), contractHash);

        return contractService.queryTxsByContractHash(contractHash, pageNumber, pageSize);
    }

    @RequestLimit(count = 60)
    @ApiOperation(value = "Get dapp binded information")
    @GetMapping(value = "/bindedinfo")
    public ResponseBean queryDappBindedInfo(@RequestParam("dapp_name") String dappNameArrayStr,
                                            @RequestParam("start_time") long startTime,
                                            @RequestParam("end_time") long endTime) {

        log.info("####{}.{} begin...dappNameArrayStr:{}", CLASS_NAME, Helper.currentMethod(), dappNameArrayStr);

        return contractService.queryDappBindedInfo(dappNameArrayStr, startTime, endTime);
    }


    @RequestLimit(count = 60)
    @ApiOperation(value = "Get binded wallet dapp information")
    @GetMapping(value = "/binded-wallet-dapps")
    public ResponseBean queryBindedWalletDappInfo(@RequestParam("start_time") long startTime,
                                                  @RequestParam("end_time") long endTime) {

        log.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        return contractService.queryBindedWalletDappInfo(startTime, endTime);
    }


    @RequestLimit(count = 60)
    @ApiOperation(value = "Get binded node dapp information")
    @GetMapping(value = "/binded-node-dapps")
    public ResponseBean queryBindedNodeDappInfo(@RequestParam("start_time") long startTime,
                                                @RequestParam("end_time") long endTime) {

        log.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        return contractService.queryBindedNodeDappInfo(startTime, endTime);
    }


    @RequestLimit(count = 60)
    @ApiOperation(value = "Get Dapp store dapp information")
    @GetMapping(value = "/dappstore-dapps")
    public ResponseBean queryDappstoreDappsInfo(@RequestParam("page_size") @Max(20) @Min(1) Integer pageSize,
                                                @RequestParam("page_number") @Min(1) Integer pageNumber) {

        log.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        return contractService.queryDappstoreDappsInfo(pageSize, pageNumber);
    }


    @RequestLimit(count = 60)
    @ApiOperation(value = "Get all Dapp store dapp summary information")
    @GetMapping(value = "/dappstore-dapps/summary")
    public ResponseBean queryDappstoreDappsSummary() {

        log.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        return contractService.queryDappstoreDappsSummary();
    }

    @RequestLimit(count = 120)
    @ApiOperation(value = "Get contract daily aggregations by ont/ong token")
    @GetMapping(value = "/{contract_hash}/daily")
    public ResponseBean queryDailyAggregation(
            @PathVariable("contract_hash") @Length(min = 40, max = 42, message = "Incorrect contract hash") String contractHash,
            @RequestParam(value = "token") @Pattern(regexp = "ont|ONT|ong|ONG", message = "Incorrect token") String token,
            @RequestParam(name = "from", required = false) @DateTimeFormat(pattern = "yyyyMMdd") Date from,
            @RequestParam(name = "to", required = false) @DateTimeFormat(pattern = "yyyyMMdd") Date to
    ) {
        log.info("####{}.{} begin...contract_hash:{}", CLASS_NAME, Helper.currentMethod(), contractHash);

        return contractService.queryDailyAggregation(contractHash, token, from, to);
    }

    @RequestLimit(count = 120)
    @ApiOperation(value = "Get address daily aggregations by specific token type")
    @GetMapping(value = "/{contract_hash}/{token_type}/daily")
    public ResponseBean queryDailyAggregationOfTokenType(
            @PathVariable("contract_hash") @Length(min = 40, max = 42, message = "Incorrect contract hash") String contractHash,
            @PathVariable(value = "token_type") @Pattern(regexp = "oep4|OEP4|native|NATIVE|orc20|ORC20", message = "Incorrect token type") String tokenType,
            @RequestParam(name = "from", required = false) @DateTimeFormat(pattern = "yyyyMMdd") Date from,
            @RequestParam(name = "to", required = false) @DateTimeFormat(pattern = "yyyyMMdd") Date to
    ) {
        log.info("####{}.{} begin...contract_hash:{}", CLASS_NAME, Helper.currentMethod(), contractHash);

        return contractService.queryDailyAggregationOfTokenType(contractHash, tokenType, from, to);
    }

    @ApiOperation(value = "check if contract exist")
    @GetMapping(value = "/checkIfExist/{contract_hash}")
    public ResponseBean checkIfExistContract(@PathVariable("contract_hash") @Length(min = 40, max = 42, message = "Incorrect contract hash") String contractHash) {
        log.info("####{}.{} begin...contract_hash:{}", CLASS_NAME, Helper.currentMethod(), contractHash);
        return contractService.checkIfExistsHash(contractHash);
    }

    @RequestLimit(count = 30)
    @ApiOperation(value = "check if contract exist")
    @GetMapping(value = "/check_type/{content}")
    public ResponseBean checkTypeOfSearch(@PathVariable("content") String content) {
        log.info("####{}.{} begin...content:{}", CLASS_NAME, Helper.currentMethod(), content);
        return contractService.checkTypeOfSearch(content);
    }
}
