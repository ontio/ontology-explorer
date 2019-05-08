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
import com.github.ontio.service.IBlockService;
import com.github.ontio.util.ErrorInfo;
import com.github.ontio.util.Helper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Validated
@RestController
@RequestMapping(value = "/v2/")
@Slf4j
public class BlockController {

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private final IBlockService blockService;

    @Autowired
    public BlockController(IBlockService blockService) {
        this.blockService = blockService;
    }



    @ApiOperation(value = "Get latest block list")
    @GetMapping(value = "/latest-blocks")
    public ResponseBean getLatestBlocks(@RequestParam("count") @Min(1) @Max(50) Integer count) {

        log.info("####{}.{} begin...",CLASS_NAME, Helper.currentMethod());
        return blockService.queryLatestBlocks(count);
    }

    @ApiOperation(value = "Get block list by page")
    @GetMapping(value = "/blocks")
    public ResponseBean getBlocksByPage(@RequestParam("page_size") @Min(1) @Max(20) Integer pageSize,
                                        @RequestParam("page_number") @Min(1) Integer pageNumber) {

        log.info("####{}.{} begin...",CLASS_NAME, Helper.currentMethod());
        return blockService.queryBlocksByPage(pageSize, pageNumber);
    }

    @ApiOperation(value = "Get block detail by height or hash")
    @GetMapping(value = "/blocks/{param}")
    public ResponseBean getBlock(@PathVariable("param") String param) {

        log.info("####{}.{} begin...param:{}",CLASS_NAME, Helper.currentMethod(),param);

        if (param.length() == 64) {
            return blockService.queryBlockByHash(param);
        }
        try {
            int blockHeight = Integer.valueOf(param);
            return blockService.queryBlockByHeight(blockHeight);
        } catch (NumberFormatException e) {
            return new ResponseBean(ErrorInfo.PARAM_ERROR.code(), ErrorInfo.PARAM_ERROR.desc(), null);
        }
    }

    @ApiOperation(value = "Get generate block time")
    @GetMapping(value = "/blocks/generate-time")
    public ResponseBean queryBlockGenerateTime(@RequestParam("count") @Max(100) @Min(1) Integer count) {

        log.info("####{}.{} begin...",CLASS_NAME, Helper.currentMethod());

        return blockService.queryBlockGenerateTime(count);
    }



}
