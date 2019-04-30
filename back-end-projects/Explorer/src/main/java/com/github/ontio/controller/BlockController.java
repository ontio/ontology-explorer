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
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Validated
@RestController
@RequestMapping(value = "/api/v2/blocks")
public class BlockController {
    
    @Autowired
    private IBlockService blockService;

    @PutMapping(value = "/latest-blocks/{count}")
    @ApiOperation(value = "Get latest block list")
    public ResponseBean getLatestBlock(@PathVariable("count") int count) {
        return blockService.queryBlockList(count);
    }

    @GetMapping(value = "/{page_size}/{page_number}")
    @ApiOperation(value = "Get block list by page")
    public ResponseBean getBlockByPage(@PathVariable("page_size") Integer pageSize, @PathVariable("page_number") Integer pageNumber) {
        return blockService.queryBlockList(pageSize, pageNumber);
    }

    @GetMapping(value = "/{param}")
    @ApiOperation(value = "Get block detail by height or hash")
    public ResponseBean getBlock(@PathVariable("param") String param) {
        if (param.length() == 64) {
            return blockService.queryBlockByHash(param);
        }
        try {
            int blockHeight = Integer.valueOf(param);
            return blockService.queryBlockByHeight(blockHeight);
        } catch (NumberFormatException e) {
            return new ResponseBean(ErrorInfo.PARAM_ERROR.code(), ErrorInfo.PARAM_ERROR.desc(), new HashMap<>());
        }
    }

    @GetMapping(value = "/generate-time/{amount}")
    @ApiOperation(value = "Get generate block time")
    public ResponseBean queryBlockGenerateTime(@PathVariable("amount") int amount) {
        return blockService.queryBlockGenerateTime(amount);
    }
}
