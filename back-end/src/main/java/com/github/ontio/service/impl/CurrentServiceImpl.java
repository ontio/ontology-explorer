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



package com.github.ontio.service.impl;

import com.github.ontio.paramBean.Result;
import com.github.ontio.dao.BlockMapper;
import com.github.ontio.dao.CurrentMapper;
import com.github.ontio.dao.OntIdMapper;
import com.github.ontio.service.ICurrentService;
import com.github.ontio.utils.ConfigParam;
import com.github.ontio.utils.ErrorInfo;
import com.github.ontio.utils.Helper;
import com.github.ontio.utils.OntologySDKService;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/2/27
 */
@Service("CurrentService")
@MapperScan("com.github.ontio.dao")
public class CurrentServiceImpl implements ICurrentService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String VERSION = "1.0";

    @Autowired
    private CurrentMapper currentMapper;
    @Autowired
    private OntIdMapper ontIdMapper;
    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private ConfigParam configParam;

    private OntologySDKService sdk;

    private synchronized void initSDK() {
        if (sdk == null) {
            sdk = OntologySDKService.getInstance(configParam);
        }
    }

    @Override
    public Result querySummaryInfo() {

        Map summary = currentMapper.selectSummaryInfo();
        Map blockInfo = blockMapper.selectBlockByHeight(1);
        int ontIdCount = ontIdMapper.selectOntIdCount();

        int blockTime = (Integer) blockInfo.get("BlockTime");
        long runningTime = System.currentTimeMillis()/1000L - blockTime;

        initSDK();
        int nodeCount = sdk.getNodeCount();
        int generateBlockTime = sdk.getGenerateBlockTime();

        Map<String, Object> rs = new HashMap();
        rs.put("BlockInterval",generateBlockTime);
        rs.put("NodeCount", nodeCount + 1);
        rs.put("CurrentHeight", summary.get("Height"));
        rs.put("TxnCount", summary.get("TxnCount"));
        rs.put("RunningTime",runningTime);
        rs.put("OntIdCount", ontIdCount);

        return Helper.result("QueryCurrentInfo", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);
    }
}
