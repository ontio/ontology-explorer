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

import com.github.ontio.paramBean.Result;
import com.github.ontio.service.impl.TransactionServiceImpl;
import com.github.ontio.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/3/15
 */
@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/api/v1/explorer/")
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    private final String CLASS_NAME = this.getClass().getSimpleName();

    @Autowired
    private TransactionServiceImpl transactionService;

    /**
     * query the last few transactions
     *
     * @return
     */
    @RequestMapping(value = "/transactionlist/{amount}", method = RequestMethod.GET)
    @ResponseBody
    public Result queryTransactionList(@PathVariable("amount") int amount) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        logger.info("amount:{}", amount);

        Result rs = transactionService.queryTxnList(amount);
        return rs;
    }

    /**
     * query transactions by page
     *
     * @return
     */
    @RequestMapping(value = "/transactionlist/{pagesize}/{pagenumber}", method = RequestMethod.GET)
    @ResponseBody
    public Result queryTxn(@PathVariable("pagesize") Integer pageSize,
                           @PathVariable("pagenumber") Integer pageNumber) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        logger.info("pageSize:{}, pageNumber:{}", pageSize, pageNumber);

        Result rs = transactionService.queryTxnList(pageSize, pageNumber);
        return rs;
    }

    /**
     * query transaction by txnhash
     *
     * @return
     */
    @RequestMapping(value = "/transaction/{txnhash}", method = RequestMethod.GET)
    @ResponseBody
    public Result queryTxnByHash(@PathVariable("txnhash") String txnHash) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        logger.info("txnHash:{}", txnHash);

        Result rs = transactionService.queryTxnDetailByHash(txnHash);
        return rs;
    }

    /**
     * query asset balance and transactions by address
     *
     * @return
     */
    @RequestMapping(value = "/address/{address}/{pagesize}/{pagenumber}", method = RequestMethod.GET)
    @ResponseBody
    public Result queryAddressInfo(@PathVariable("address") String address,
                                   @PathVariable("pagesize") int pageSize,
                                   @PathVariable("pagenumber") int pageNumber) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        logger.info("address:{},pagesize:{},pagenumber:{}", address, pageSize, pageNumber);

        Result rs = transactionService.queryAddressInfo(address, pageNumber, pageSize);
        return rs;
    }

    /**
     * query the specially asset balance and transactions
     *
     * @return
     */
    @RequestMapping(value = "/address/{address}/{assetname}/{pagesize}/{pagenumber}", method = RequestMethod.GET)
    @ResponseBody
    public Result queryAddressInfo(@PathVariable("address") String address,
                                   @PathVariable("pagesize") int pageSize,
                                   @PathVariable("pagenumber") int pageNumber,
                                   @PathVariable("assetname") String assetName) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        logger.info("address:{},pagesize:{},pagenumber:{}, assetname:{}", address, pageSize, pageNumber, assetName);

        Result rs = transactionService.queryAddressInfo(address, pageNumber, pageSize, assetName);
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
    public Result queryAddressInfoByTimeAndPage(@PathVariable("address") String address,
                                         @PathVariable("pagesize") int pageSize,
                                         @PathVariable("assetname") String assetName,
                                         @PathVariable("endtime") int endTime) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        logger.info("address:{},assetname:{},pageSize:{},endTime:{}", address, assetName, pageSize, endTime);

        Result rs = transactionService.queryAddressInfoByTimeAndPage(address, assetName, pageSize, endTime);
        return rs;
    }

    /**
     * query the specially asset balance and transactions
     * ONTO use
     * @return
     */
    @RequestMapping(value = "/address/time/{address}/{assetname}/{begintime}/{endtime}", method = RequestMethod.GET)
    @ResponseBody
    public Result queryAddressInfoByTime(@PathVariable("address") String address,
                                         @PathVariable("begintime") int beginTime,
                                         @PathVariable("assetname") String assetName,
                                         @PathVariable("endtime") int endTime) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        logger.info("address:{},assetname:{},beginTime:{},endTime:{}", address, assetName, beginTime, endTime);

        Result rs = transactionService.queryAddressInfoByTime(address, assetName, beginTime, endTime);
        return rs;
    }

    /**
     * query the specially asset balance and transactions
     *
     * @return
     */
    @RequestMapping(value = "/address/time/{address}/{assetname}/{begintime}", method = RequestMethod.GET)
    @ResponseBody
    public Result queryAddressInfoByTime2(@PathVariable("address") String address,
                                         @PathVariable("begintime") int beginTime,
                                         @PathVariable("assetname") String assetName) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        logger.info("address:{},assetname:{},beginTime:{}", address, assetName, beginTime);

        Result rs = transactionService.queryAddressInfoByTime(address, assetName, beginTime);
        return rs;
    }

    /**
     * query the balance
     *
     * @return
     */
    @RequestMapping(value = "/address/balance/{address}", method = RequestMethod.GET)
    @ResponseBody
    public Result queryAddressBalance(@PathVariable("address") String address) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        logger.info("address:{}", address);

        Result rs = transactionService.queryAddressBalance(address);
        return rs;
    }

    /**
     * query all address information
     *
     * @return
     */
/*    @RequestMapping(value = "/address/list", method = RequestMethod.GET)
    @ResponseBody
    public Result queryAddressList() {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        Result rs = transactionService.queryAddressList();
        return rs;
    }*/
}
