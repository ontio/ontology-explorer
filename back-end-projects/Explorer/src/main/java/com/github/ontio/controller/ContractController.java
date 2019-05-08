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

import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.service.impl.ContractServiceImpl;
import com.github.ontio.util.Helper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

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
    public ResponseBean queryContractDetail(@PathVariable("contract_hash") @Length(min = 40, max = 40, message = "Incorrect contract hash") String contractHash) {

        log.info("####{}.{} begin...contract_hash:{}", CLASS_NAME, Helper.currentMethod(), contractHash);

        return contractService.queryContractDetail(contractHash);
    }


    @ApiOperation(value = "Get contract transaction list by contracthash")
    @GetMapping(value = "/{contract_type}/{contract_hash}/transactions")
    public ResponseBean queryContractTxsByPage(@PathVariable("contract_type") @Pattern(regexp = "oep4|OEP4|oep5|OEP5|oep8|OEP8|other|OTHER", message = "Incorrect contract type") String contractType,
                                               @PathVariable("contract_hash") @Length(min = 40, max = 40, message = "Incorrect contract address") String contractHash,
                                               @RequestParam("page_number") @Min(1) Integer pageNumber,
                                               @RequestParam("page_size") @Max(20) @Min(1) Integer pageSize) {

        log.info("####{}.{} begin...contract_type:{},contract_hash:{}", CLASS_NAME, Helper.currentMethod(), contractType, contractHash);

        return contractService.queryTxsByContractHash(contractType, contractHash, pageNumber,pageSize);
    }

    /**
     * query OEP by type(oep4\oep8)
     *
     * @param type       type
     * @param pageNumber the start page
     * @param pageSize   the amount of each page
     * @return
     */
    @RequestMapping(value = "/oepcontract/{type}/{pagesize}/{pagenumber}", method = RequestMethod.GET)
    public ResponseBean queryOEPContract(@PathVariable("type") String type,
                                         @RequestParam("page_number") Integer pageNumber,
                                         @RequestParam("page_size") Integer pageSize) {
        if (type.isEmpty()) {
            return null;
        }

        ResponseBean rs = contractService.queryOEPContract(type, pageSize, pageNumber);
        return rs;
    }

    /**
     * query OEP by type(oep4\oep8)
     *
     * @param contracthash contracthash
     * @param type         type
     * @param pageNumber   the start page
     * @param pageSize     the amount of each page
     * @return
     */
    @RequestMapping(value = "/oepcontract/{type}/{contracthash}/{pagesize}/{pagenumber}", method = RequestMethod.GET)
    public ResponseBean queryOEPContractByHash(@PathVariable("contracthash") String contracthash,
                                               @PathVariable("type") String type,
                                               @PathVariable("pagenumber") Integer pageNumber,
                                               @PathVariable("pagesize") Integer pageSize) {
        if (type.isEmpty() || contracthash.isEmpty()) {
            return null;
        }

        ResponseBean rs = contractService.queryOEPContractByHashAndTokenName(contracthash, type, "", pageSize, pageNumber);
        return rs;
    }

    /**
     * query OEP by type(oep4\oep8)
     *
     * @param contracthash contracthash
     * @param type         type
     * @param tokenname    tokenname
     * @param pageNumber   the start page
     * @param pageSize     the amount of each page
     * @return
     */
    @RequestMapping(value = "/oepcontract/{type}/{contracthash}/{tokenname}/{pagesize}/{pagenumber}", method = RequestMethod.GET)
    public ResponseBean queryOEPContractByHashAndSymbol(@PathVariable("contracthash") String contracthash,
                                                        @PathVariable("type") String type,
                                                        @PathVariable("tokenname") String tokenname,
                                                        @PathVariable("pagenumber") Integer pageNumber,
                                                        @PathVariable("pagesize") Integer pageSize) {

        if (type.isEmpty() || contracthash.isEmpty()) {
            return null;
        }

        ResponseBean rs = contractService.queryOEPContractByHashAndTokenName(contracthash, type, tokenname, pageSize, pageNumber);
        return rs;
    }


    /**
     * 查询dappstore里的合约列表信息
     *
     * @param pageSize
     * @param pageNumber
     * @return
     */
    @GetMapping(value = "/contract/dappstore/{pagesize}/{pagenumber}")
    public ResponseBean queryDappstoreContractInfo(@PathVariable("pagesize") Integer pageSize, @PathVariable("pagenumber") Integer pageNumber) {
        ResponseBean rs = contractService.queryDappstoreContractInfo(pageSize, pageNumber);
        return rs;
    }


    /**
     * 查询dappstore里合约汇总信息
     *
     * @return
     */
    @GetMapping(value = "/contract/dappstore/24h/summary")
    public ResponseBean queryDappstoreContract24hSummary() {
        ResponseBean rs = contractService.queryDappstore24hSummary();
        return rs;
    }


}
