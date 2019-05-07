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
import com.github.ontio.service.impl.OntIdServiceImpl;
import com.github.ontio.util.Helper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/3/15
 */
@Validated
@RestController
@RequestMapping(value = "/v2")
@Slf4j
public class OntIdController {

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private final OntIdServiceImpl ontIdService;

    @Autowired
    public OntIdController(OntIdServiceImpl ontIdService) {
        this.ontIdService = ontIdService;
    }


    @ApiOperation(value = "Get latest ONT ID transaction list")
    @GetMapping(value = "/latest-ontids")
    public ResponseBean queryLatestOntIdTxs(@RequestParam("count") int count) {

        log.info("###{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        ResponseBean rs = ontIdService.queryLatestOntIdTxs(count);
        return rs;
    }


    @ApiOperation(value = "Get ONT ID transaction list by page")
    @GetMapping(value = "/ontids")
    public ResponseBean queryOntIdTxsByPage(@RequestParam("page_size") @Max(20) @Min(1) int pageSize,
                                            @RequestParam("page_number") @Min(1) int pageNumber) {

        log.info("###{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        ResponseBean rs = ontIdService.queryOntidTxsByPage(pageSize, pageNumber);
        return rs;

    }


    @ApiOperation(value = "Get ONT ID transaction list by page")
    @GetMapping(value = "/ontids/{ontid}/txs")
    public ResponseBean queryOntIdDetail(@PathVariable("ontid") @Pattern(regexp = "did:ont:A[A-Za-z0-9]{63}") String ontid,
                                         @RequestParam("page_size") @Max(20) @Min(1) int pageSize,
                                         @RequestParam("page_number") @Min(1) int pageNumber) {

        log.info("########{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        ResponseBean rs = ontIdService.queryOntIdDetail(ontid, pageSize, pageNumber);
        return rs;

    }


}
