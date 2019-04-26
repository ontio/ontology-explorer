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

import com.github.ontio.paramBean.OldResult;
import com.github.ontio.paramBean.ResponseBean;
import com.github.ontio.service.ITransactionService;
import com.github.ontio.util.Helper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/3/15
 */
@Validated
@Slf4j
@RestController
@RequestMapping(value = "/v2/transactions")
public class TransactionController {

    private final String CLASS_NAME = this.getClass().getSimpleName();

    @Autowired
    private ITransactionService transactionService;


    @ApiOperation(value = "Get latest transaction list")
    @GetMapping()
    public ResponseBean queryLatestTxs(@RequestParam("count") @Max(50) int count) {

        log.info("###{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        ResponseBean rs = transactionService.queryLatestTxs(count);
        return rs;
    }

    /**
     * query transactions by page
     *
     * @return
     */
    @RequestMapping(value = "/transactionlist/{pagesize}/{pagenumber}", method = RequestMethod.GET)
    @ResponseBody
    public OldResult queryTxn(@PathVariable("pagesize") Integer pageSize,
                              @PathVariable("pagenumber") Integer pageNumber) {

        log.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        log.info("pageSize:{}, pageNumber:{}", pageSize, pageNumber);

        OldResult rs = transactionService.queryLatestTxs(pageSize, pageNumber);
        return rs;
    }

    /**
     * query transaction by txnhash
     *
     * @return
     */
    @RequestMapping(value = "/transaction/{txnhash}", method = RequestMethod.GET)
    @ResponseBody
    public OldResult queryTxnByHash(@PathVariable("txnhash") String txnHash) {

        log.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        log.info("txnHash:{}", txnHash);

        OldResult rs = transactionService.queryTxnDetailByHash(txnHash);
        return rs;
    }

    /**
     * query asset balance and transactions by address
     *
     * @return
     */
    @RequestMapping(value = "/address/{address}/{pagesize}/{pagenumber}", method = RequestMethod.GET)
    @ResponseBody
    public OldResult queryAddressInfo(@PathVariable("address") String address,
                                      @PathVariable("pagesize") int pageSize,
                                      @PathVariable("pagenumber") int pageNumber) {

        log.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        log.info("address:{},pagesize:{},pagenumber:{}", address, pageSize, pageNumber);

        OldResult rs = transactionService.queryAddressInfo(address, pageNumber, pageSize);
        return rs;
    }

    /**
     * query the specially asset balance and transactions
     *
     * @return
     */
    @RequestMapping(value = "/address/{address}/{assetname}/{pagesize}/{pagenumber}", method = RequestMethod.GET)
    @ResponseBody
    public OldResult queryAddressInfo(@PathVariable("address") String address,
                                      @PathVariable("pagesize") int pageSize,
                                      @PathVariable("pagenumber") int pageNumber,
                                      @PathVariable("assetname") String assetName) {

        log.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        log.info("address:{},pagesize:{},pagenumber:{}, assetname:{}", address, pageSize, pageNumber, assetName);

        OldResult rs = transactionService.queryAddressInfo(address, pageNumber, pageSize, assetName);
        return rs;
    }

    /**
     * query the specially asset balance and transactions
     * ONTO use
     *
     * @return
     */
    @RequestMapping(value = "/address/timeandpage/{address}/{assetname}/{pagesize}/{endtime}", method = RequestMethod.GET)
    @ResponseBody
    public OldResult queryAddressInfoByTimeAndPage(@PathVariable("address") String address,
                                                   @PathVariable("pagesize") int pageSize,
                                                   @PathVariable("assetname") String assetName,
                                                   @PathVariable("endtime") int endTime) {

        log.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        log.info("address:{},assetname:{},pageSize:{},endTime:{}", address, assetName, pageSize, endTime);

        OldResult rs = transactionService.queryAddressInfoByTimeAndPage(address, assetName, pageSize, endTime);
        return rs;
    }

    /**
     * query the specially asset balance and transactions
     * ONTO use
     * @return
     */
    @RequestMapping(value = "/address/time/{address}/{assetname}/{begintime}/{endtime}", method = RequestMethod.GET)
    @ResponseBody
    public OldResult queryAddressInfoByTime(@PathVariable("address") String address,
                                            @PathVariable("begintime") int beginTime,
                                            @PathVariable("assetname") String assetName,
                                            @PathVariable("endtime") int endTime) {

        log.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        log.info("address:{},assetname:{},beginTime:{},endTime:{}", address, assetName, beginTime, endTime);

        OldResult rs = transactionService.queryAddressInfoByTime(address, assetName, beginTime, endTime);
        return rs;
    }

    /**
     * query the specially asset balance and transactions
     *
     * @return
     */
    @RequestMapping(value = "/address/time/{address}/{assetname}/{begintime}", method = RequestMethod.GET)
    @ResponseBody
    public OldResult queryAddressInfoByTime2(@PathVariable("address") String address,
                                             @PathVariable("begintime") int beginTime,
                                             @PathVariable("assetname") String assetName) {

        log.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        log.info("address:{},assetname:{},beginTime:{}", address, assetName, beginTime);

        OldResult rs = transactionService.queryAddressInfoByTime(address, assetName, beginTime);
        return rs;
    }

    /**
     * query the balance
     *
     * @return
     */
    @RequestMapping(value = "/address/balance/{address}", method = RequestMethod.GET)
    @ResponseBody
    public OldResult queryAddressBalance(@PathVariable("address") String address) {

        log.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        log.info("address:{}", address);

        OldResult rs = transactionService.queryAddressBalance(address);
        return rs;
    }

    /**
     * query all address information
     *
     * @return
     */
/*    @RequestMapping(value = "/address/list", method = RequestMethod.GET)
    @ResponseBody
    public OldResult queryAddressList() {

        log.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        OldResult rs = transactionService.queryAddressList();
        return rs;
    }*/

    /**
     * query all address information
     *
     * @return
     */
    @RequestMapping(value = "/address/queryaddressinfo/{address}", method = RequestMethod.GET)
    @ResponseBody
    public OldResult queryAddressInfo(@PathVariable("address") String address) {

        log.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        OldResult rs = transactionService.queryAddressInfoForExcel(address);
        return rs;
    }
}
