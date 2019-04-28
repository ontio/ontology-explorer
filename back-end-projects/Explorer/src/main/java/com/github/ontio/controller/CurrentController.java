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

import com.alibaba.fastjson.JSONObject;
import com.github.ontio.paramBean.Result;
import com.github.ontio.service.impl.CurrentServiceImpl;
import com.github.ontio.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/2/27
 */
@Slf4j
@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/api/v1/explorer")
public class CurrentController {

    private final String CLASS_NAME = this.getClass().getSimpleName();

    @Autowired
    private CurrentServiceImpl currentService;

    /**
     * query current summary information
     *
     * @return
     */
    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    @ResponseBody
    public Result querySummaryInfo() {

        log.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        Result rs = currentService.querySummaryInfo();
        return rs;
    }

    /**
     * 负载均衡存活探测api
     *
     * @return
     */
    @RequestMapping(value = "/detection", method = RequestMethod.GET)
    @ResponseBody
    public String detection() {

        log.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        return "success";
    }

    @RequestMapping(value = "/contract/registerContractInfo", method = RequestMethod.POST)
    public Result registerContractInfo(@RequestBody JSONObject reqObj) {

        log.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        log.info("####reqObj:{}", reqObj.toJSONString());

        Result rs = currentService.registerContractInfo(reqObj);
        return rs;
    }

    /**
     * query current summary information
     *
     * @return
     */
    @RequestMapping(value = "/marketing/info", method = RequestMethod.GET)
    public Result queryMarketingInfo() {

        log.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        Result rs = currentService.queryMarketingInfo();
        return rs;
    }
}
