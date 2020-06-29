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
@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/api/v1/explorer")
public class CurrentController {

    private static final Logger logger = LoggerFactory.getLogger(CurrentController.class);

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

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());

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

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        return "success";
    }




    @RequestMapping(value = "/oep4/info", method = RequestMethod.POST)
    public Result registerOep4Info(@RequestBody JSONObject reqObj) {

        logger.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        logger.info("####reqObj:{}", reqObj.toJSONString());

        Result rs = currentService.registerOep4Info(reqObj);
        return rs;
    }

}
