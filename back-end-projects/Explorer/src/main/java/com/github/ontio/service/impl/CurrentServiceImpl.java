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

import com.alibaba.fastjson.JSONObject;
import com.github.ontio.dao.CurrentMapper;
import com.github.ontio.dao.DailyMapper;
import com.github.ontio.dao.Oep4Mapper;
import com.github.ontio.dao.TransactionDetailMapper;
import com.github.ontio.model.Oep4;
import com.github.ontio.model.Oep4Key;
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    private Oep4Mapper oep4Mapper;
    @Autowired
    private DailyMapper dailyMapper;
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


    @Override
    public Result registerOep4Info(JSONObject reqObj) {

        initSDK();
        //TODO 需要联系信息
        String codeHash = reqObj.getString("contractHash");

        JSONObject oep4Info = sdk.queryOep4Info(codeHash);

        Oep4Key oep4KeyDAO = new Oep4Key();
        oep4KeyDAO.setContract(codeHash);
        oep4KeyDAO.setName(oep4Info.getString("Name"));

        Oep4 oep4DAO = oep4Mapper.selectByPrimaryKey(oep4KeyDAO);

        if (!Helper.isEmptyOrNull(oep4DAO)) {
            return Helper.result("RegisterOep4", ErrorInfo.ALREADY_EXIST.code(), ErrorInfo.ALREADY_EXIST.desc(), VERSION, false);
        }

        oep4DAO = new Oep4();
        oep4DAO.setSymbol(oep4Info.getString("Symbol"));
        oep4DAO.setName(oep4Info.getString("Name"));
        oep4DAO.setDescription(oep4Info.getString("Name"));
        oep4DAO.setTotalsupply(new BigDecimal(oep4Info.getString("TotalSupply")));
        oep4DAO.setDecimals(new BigDecimal(oep4Info.getString("Decimal")));
        oep4DAO.setAuditflag(1);
        oep4DAO.setCreatetime(new Date());
        oep4DAO.setContract(codeHash);
        oep4DAO.setContactinfo("");
        oep4Mapper.insertSelective(oep4DAO);

        return Helper.result("RegisterOep4", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, true);

    }

    @Override
    public Result queryDailyInfo(long startTime, long endTime) {

        List<Map> dailyList = dailyMapper.selectDailyInfo(startTime, endTime);
        return Helper.result("QueryDailyInfo", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, dailyList);
    }
}
