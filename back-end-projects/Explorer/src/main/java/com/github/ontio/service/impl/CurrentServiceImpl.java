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

import com.github.ontio.dao.CurrentMapper;
import com.github.ontio.dao.TransactionDetailMapper;
import com.github.ontio.paramBean.Result;
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

    private static final Logger logger = LoggerFactory.getLogger(CurrentServiceImpl.class);

    private static final String VERSION = "1.0";

    @Autowired
    private CurrentMapper currentMapper;
    @Autowired
    TransactionDetailMapper transactionDetailMapper;
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
       // List<String> addrList = transactionDetailMapper.selectAllAddress();

        initSDK();
        int nodeCount = sdk.getNodeCount();

        Map<String, Object> rs = new HashMap();

        rs.put("NodeCount", nodeCount);
        rs.put("CurrentHeight", summary.get("Height"));
        rs.put("TxnCount", summary.get("TxnCount"));
        rs.put("OntIdCount", summary.get("OntIdCount"));
       // rs.put("AddrCount", addrList.size());

        return Helper.result("QueryCurrentInfo", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);
    }
}
