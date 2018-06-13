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
import com.github.ontio.service.impl.BlockServiceImpl;
import com.github.ontio.utils.ErrorInfo;
import com.github.ontio.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/2/27
 */
@EnableAutoConfiguration
@RestController
@RequestMapping(value = "/api/v1/explorer")
public class BlockController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private static final String VERSION = "1.0";

    @Autowired
    private BlockServiceImpl blockService;


    /**
     * query the last few blocks
     *
     * @param amount the amount of queries
     * @return
     */
    @RequestMapping(value = "/blocklist/{amount}", method = RequestMethod.GET)
    @ResponseBody
    public Result queryBlockList(@PathVariable("amount") int amount) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        logger.info("amount:{}", amount);

        Result rs = blockService.queryBlockList(amount);
        return rs;
    }

    /**
     * query blocks by page
     *
     * @param pageNumber the start page
     * @param pageSize   the amount of each page
     * @return
     */
    @RequestMapping(value = "/blocklist/{pagesize}/{pagenumber}", method = RequestMethod.GET)
    @ResponseBody
    public Result queryBlockByPage(@PathVariable("pagenumber") Integer pageNumber,
                                   @PathVariable("pagesize") Integer pageSize) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        logger.info("pageSize:{}, pageNumber；{}", pageSize, pageNumber);

        Result rs = blockService.queryBlockList(pageSize, pageNumber);
        return rs;
    }

    /**
     * query block information and transaction of this block
     *
     * @return
     */
    @RequestMapping(value = "/block/{param}", method = RequestMethod.GET)
    @ResponseBody
    public Result queryBlock(@PathVariable("param") String param) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        logger.info("param:{}", param);

        Result rs = new Result();
        if (param.length() == 64) {
            rs = blockService.queryBlockByHash(param);
        } else {
            int blockHeight = 1;
            try {
                blockHeight = Integer.valueOf(param);
            } catch (NumberFormatException e) {
                return Helper.result("QueryBlock", ErrorInfo.PARAM_ERROR.code(), ErrorInfo.PARAM_ERROR.desc(), VERSION, false);
            }
            rs = blockService.queryBlockByHeight(blockHeight);
        }

        return rs;
    }


    /**
     * query block information and transaction of this block
     *
     * @return
     */
    @RequestMapping(value = "/block/generatetime/{amount}", method = RequestMethod.GET)
    @ResponseBody
    public Result queryBlockGenerateTime(@PathVariable("amount") int amount) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        logger.info("amount:{}", amount);

        Result rs = blockService.queryBlockGenerateTime(amount);
        return rs;
    }


}
