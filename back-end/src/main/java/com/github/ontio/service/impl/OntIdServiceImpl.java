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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.ontio.dao.OntIdMapper;
import com.github.ontio.paramBean.Result;
import com.github.ontio.service.IOntIdService;
import com.github.ontio.utils.*;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/2/27
 */
@Service("OntIdService")
@MapperScan("com.github.ontio.dao")
public class OntIdServiceImpl implements IOntIdService {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private static final String VERSION = "1.0";

    @Autowired
    private OntIdMapper ontIdMapper;
    @Autowired
    private ConfigParam configParam;

    private OntologySDKService sdkService;

    private synchronized void initSDK() {
        if (sdkService == null) {
            sdkService = OntologySDKService.getInstance(configParam);
        }
    }

    @Override
    public Result queryOntIdList(int amount) {

        List<Map> ontIdList = ontIdMapper.selectOntIdByPage(0, amount);

        for (Map map :
                ontIdList) {
            map.put("Description", Helper.templateOntIdOperation((String) map.get("Description")));
        }

        return Helper.result("QueryOntIdList", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, ontIdList);
    }

    @Override
    public Result queryOntIdList(int pageSize, int pageNumber) {

        int start = pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1);

        int count = ontIdMapper.selectOntIdCount();
        List<Map> ontIdList = ontIdMapper.selectOntIdByPage(start, pageSize);

        if (ontIdList.size() > 0) {
            for (Map map :
                    ontIdList) {
                map.put("Description", Helper.templateOntIdOperation((String) map.get("Description")));
            }
        }

        Map<String, Object> rs = new HashMap<>();
        rs.put("OntIdList", ontIdList);
        rs.put("Total", count);

        return Helper.result("QueryOntIdList", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);
    }

    @Override
    public Result queryOntIdDetail(String ontId, int pageSize, int pageNumber) {

        int start = (pageNumber - 1) * pageSize < 0 ? 0 : (pageNumber - 1) * pageSize;

        Map<String, Object> param = new HashMap<>();
        param.put("PageSize", pageSize);
        param.put("Start", start);
        param.put("OntId", ontId);
        List<Map> ontIdList = ontIdMapper.selectOntId(param);
        int amount = ontIdMapper.selectCountByOntId(ontId);

        if (ontIdList.size() == 0) {
            return Helper.result("QueryOntId", ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), VERSION, false);
        }
        for (Map map :
                ontIdList) {
            map.put("Description", Helper.templateOntIdOperation((String) map.get("Description")));
        }

        initSDK();
        String ddoStr = sdkService.getDDO(ontId);
        logger.info("{} query ddo info:{}", ddoStr);

        JSONObject ddoObj = JSON.parseObject(ddoStr);
        if (ddoObj.containsKey("Attributes")) {
            List<Object> formatedAttrList = formatDDOAttribute(ddoObj);
            ddoObj.replace("Attributes", formatedAttrList);
        }

        Map<String, Object> rs = new HashMap<>();
        rs.put("Ddo", ddoObj);
        rs.put("TxnList", ontIdList);
        rs.put("TxnTotal", amount);

        return Helper.result("QueryOntId", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);
    }

    /**
     * format ddo attribute
     *
     * @param ddoObj
     * @return
     */
    private List<Object> formatDDOAttribute(JSONObject ddoObj) {

        List<Object> formatedAttrList = new ArrayList<>();

        JSONArray attrList = ddoObj.getJSONArray("Attributes");

        for (Object obj :
                attrList) {
            JSONObject attrObj = (JSONObject) obj;
            String attrKey = attrObj.getString("Key");
            //standard claim attribute
            if (attrKey.startsWith(ConstantParam.CLAIM)) {

                logger.info("Attributes contains claim");
                String attrValue = attrObj.getString("Value");
                logger.info("attrValue:{}", attrValue);
                JSONObject attrValueObj = JSON.parseObject(attrValue);
                String claimContext = attrValueObj.getString("Context");
                String issuer = attrValueObj.getString("Issuer");

                JSONObject claimObj = new JSONObject();
                claimObj.put("IssuerOntId", issuer);
                claimObj.put("ClaimContext", claimContext);
                claimObj.put("ClaimId", attrKey.substring(ConstantParam.CLAIM.length(), attrKey.length()));
                if (ClaimContextType.GITHUB_CLAIM.context().equals(claimContext)) {

                    claimObj.put("ContextDesc", ClaimContextType.GITHUB_CLAIM.desc());
                } else if (ClaimContextType.TWITTER_CLAIM.context().equals(claimContext)) {

                    claimObj.put("ContextDesc", ClaimContextType.TWITTER_CLAIM.desc());
                } else if (ClaimContextType.FACEBOOK_CLAIM.context().equals(claimContext)) {

                    claimObj.put("ContextDesc", ClaimContextType.FACEBOOK_CLAIM.desc());
                } else if (ClaimContextType.LINKEDIN_CLAIM.context().equals(claimContext)) {

                    claimObj.put("ContextDesc", ClaimContextType.LINKEDIN_CLAIM.desc());
                } else if (ClaimContextType.EMPLOYMENT_CLAIM.context().equals(claimContext)) {

                    claimObj.put("ContextDesc", ClaimContextType.EMPLOYMENT_CLAIM.desc());
                } else if (ClaimContextType.CFCA_CLAIM.context().equals(claimContext)) {

                    claimObj.put("ContextDesc", ClaimContextType.CFCA_CLAIM.desc());
                }
                Map<String, Object> formatedAttrMap = new HashMap<>();
                formatedAttrMap.put("Claim", claimObj);
                formatedAttrList.add(formatedAttrMap);
            } else {
                //self-defined attribute
                Map<String, Object> unFormatedMap = new HashMap<>();
                unFormatedMap.put(attrKey, attrObj.getString("Value"));
                Map<String, Object> selfDefined = new HashMap<>();
                selfDefined.put("SelfDefined", unFormatedMap);
                formatedAttrList.add(selfDefined);
            }

        }

        return formatedAttrList;
    }


}
