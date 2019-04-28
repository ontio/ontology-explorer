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
import com.github.ontio.service.impl.OntIdServiceImpl;
import com.github.ontio.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/3/15
 */
@Slf4j
@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/api/v1/explorer/")
public class OntIdController {

    private final String CLASS_NAME = this.getClass().getSimpleName();

    @Autowired
    private OntIdServiceImpl ontIdService;


    /**
     * 查询ontid操作记录列表
     *
     * @return
     */
    @RequestMapping(value = "/ontidlist/{amount}", method = RequestMethod.GET)
    @ResponseBody
    public Result queryOntIdList(@PathVariable("amount") int amount) {

        log.info("########{}.{} begin...",CLASS_NAME, Helper.currentMethod());
        log.info("amount:{}",amount);

        Result rs = ontIdService.queryOntIdList(amount);
        return rs;
    }

    /**
     * 分页查询ontid操作记录列表
     *
     * @return
     */
    @RequestMapping(value = "/ontidlist/{pagesize}/{pagenumber}", method = RequestMethod.GET)
    @ResponseBody
    public Result queryOntIdListByPage(@PathVariable("pagesize") int pageSize,
                                       @PathVariable("pagenumber") int pageNumber) {

        log.info("########{}.{} begin...",CLASS_NAME, Helper.currentMethod());
        log.info("pageSize:{}, pagenumber:{}", pageSize, pageNumber);

        Result rs = ontIdService.queryOntIdList(pageSize, pageNumber);
        return rs;

    }


    /**
     * 查询某个ontid详情
     *
     * @param ontId
     * @return
     */
    @RequestMapping(value = "/ontid/{ontid}/{pagesize}/{pagenumber}", method = RequestMethod.GET)
    @ResponseBody
    public Result queryOntIdDetail(@PathVariable("ontid") String ontId,
                                   @PathVariable("pagesize") int pageSize,
                                   @PathVariable("pagenumber") int pageNumber) {

        log.info("########{}.{} begin...",CLASS_NAME, Helper.currentMethod());
        log.info("ontId:{},pageSize:{},pageNumber", ontId, pageSize, pageNumber);

        Result rs = ontIdService.queryOntIdDetail(ontId, pageSize, pageNumber);
        return rs;

    }



}
