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
import com.github.ontio.config.ConfigParam;
import com.github.ontio.mapper.OntidTxDetailMapper;
import com.github.ontio.model.common.ClaimContextEnum;
import com.github.ontio.model.common.PageResponseBean;
import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.model.dto.CurrentDto;
import com.github.ontio.model.dto.OntidTxDetailDto;
import com.github.ontio.service.IOntIdService;
import com.github.ontio.util.ConstantParam;
import com.github.ontio.util.ErrorInfo;
import com.github.ontio.util.Helper;
import com.github.ontio.util.OntologySDKService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/2/27
 */
@Slf4j
@Service("OntIdService")
@MapperScan("com.github.ontio.dao")
public class OntIdServiceImpl implements IOntIdService {

    private static final String VERSION = "1.0";

    @Autowired
    private OntidTxDetailMapper ontidTxDetailMapper;
    @Autowired
    private com.github.ontio.mapper.CurrentMapper currentMapper;
    @Autowired
    private ConfigParam configParam;

    private OntologySDKService sdkService;

    private synchronized void initSDK() {
        if (sdkService == null) {
            sdkService = OntologySDKService.getInstance(configParam);
        }
    }

    @Override
    public ResponseBean queryLatestOntIdTxs(int count) {

        List<OntidTxDetailDto> ontidTxDetailDtos = ontidTxDetailMapper.selectOntidTxsByPage("", 0, count);

        for (OntidTxDetailDto dto : ontidTxDetailDtos) {
            dto.setDescription(Helper.templateOntIdOperation(dto.getDescription()));
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), ontidTxDetailDtos);
    }

    @Override
    public ResponseBean queryOntidTxsByPage(int pageSize, int pageNumber) {

        int start = pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1);
        List<OntidTxDetailDto> ontidTxDetailDtos = ontidTxDetailMapper.selectOntidTxsByPage("", start, pageSize);
        for (OntidTxDetailDto dto :
                ontidTxDetailDtos) {
            dto.setDescription(Helper.templateOntIdOperation(dto.getDescription()));
        }

        CurrentDto currentDto = currentMapper.selectSummaryInfo();
        int nonontidTxCount = currentDto.getNonontidTxCount();

        PageResponseBean pageResponseBean = new PageResponseBean(ontidTxDetailDtos, nonontidTxCount);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), pageResponseBean);
    }

    @Override
    public ResponseBean queryOntIdDetail(String ontId, int pageSize, int pageNumber) {

        int start = (pageNumber - 1) * pageSize < 0 ? 0 : (pageNumber - 1) * pageSize;


        OntidTxDetailDto ontidTxDetailDto = OntidTxDetailDto.builder()
                .ontid(ontId)
                .build();
        Integer count = ontidTxDetailMapper.selectCount(ontidTxDetailDto);


        int start = pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1);
        List<OntidTxDetailDto> ontidTxDetailDtos = ontidTxDetailMapper.selectOntidTxsByPage("", 0, count);

        for (OntidTxDetailDto dto : ontidTxDetailDtos) {
            dto.setDescription(Helper.templateOntIdOperation(dto.getDescription()));
        }

        PageHelper.startPage(start, pageSize);

        Example example = new Example(OntidTxDetailMapper.class);
        example.setOrderByClause("block_height desc");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ont_id", ontId);

        List<OntidTxDetailDto> result = ontidTxDetailMapper.selectByExample(example);


        Map<String, Object> param = new HashMap<>();
        param.put("PageSize", pageSize);
        param.put("Start", start);
        param.put("OntId", ontId);
        List<Map> ontIdList = ontIdMapper.selectOntId(param);
        if (ontIdList.size() == 0) {
            return Helper.result("QueryOntId", ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), VERSION, false);
        }

        for (Map map :
                ontIdList) {
            map.put("Description", Helper.templateOntIdOperation((String) map.get("Description")));
            map.put("Fee", ((BigDecimal) map.get("Fee")).toPlainString());
        }

        initSDK();
        String ddoStr = sdkService.getDDO(ontId);
        log.info("{} query ddo info:{}", ontId, ddoStr);

        JSONObject ddoObj = JSON.parseObject(ddoStr);
        if (ddoObj.containsKey("Attributes")) {
            List<Object> formatedAttrList = formatDDOAttribute(ddoObj);
            ddoObj.replace("Attributes", formatedAttrList);
        }

        int amount = ontIdMapper.selectCountByOntId(ontId);
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

                log.info("Attributes contains claim");
                String attrValue = attrObj.getString("Value");
                log.info("attrValue:{}", attrValue);
                JSONObject attrValueObj = JSON.parseObject(attrValue);
                String claimContext = attrValueObj.getString("Context");
                String issuer = attrValueObj.getString("Issuer");

                JSONObject claimObj = new JSONObject();
                claimObj.put("IssuerOntId", issuer);
                claimObj.put("ClaimContext", claimContext);
                claimObj.put("ClaimId", attrKey.substring(ConstantParam.CLAIM.length(), attrKey.length()));
                if (ClaimContextEnum.GITHUB_CLAIM.context().equals(claimContext)) {

                    claimObj.put("ContextDesc", ClaimContextEnum.GITHUB_CLAIM.desc());
                } else if (ClaimContextEnum.TWITTER_CLAIM.context().equals(claimContext)) {

                    claimObj.put("ContextDesc", ClaimContextEnum.TWITTER_CLAIM.desc());
                } else if (ClaimContextEnum.FACEBOOK_CLAIM.context().equals(claimContext)) {

                    claimObj.put("ContextDesc", ClaimContextEnum.FACEBOOK_CLAIM.desc());
                } else if (ClaimContextEnum.LINKEDIN_CLAIM.context().equals(claimContext)) {

                    claimObj.put("ContextDesc", ClaimContextEnum.LINKEDIN_CLAIM.desc());
                } else if (ClaimContextEnum.EMPLOYMENT_CLAIM.context().equals(claimContext)) {

                    claimObj.put("ContextDesc", ClaimContextEnum.EMPLOYMENT_CLAIM.desc());
                } else if (ClaimContextEnum.CFCA_CLAIM.context().equals(claimContext)) {

                    claimObj.put("ContextDesc", ClaimContextEnum.CFCA_CLAIM.desc());
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
