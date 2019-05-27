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
import com.github.ontio.config.ParamsConfig;
import com.github.ontio.mapper.CurrentMapper;
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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service("OntIdService")
public class OntIdServiceImpl implements IOntIdService {


    private final OntidTxDetailMapper ontidTxDetailMapper;
    private final CurrentMapper currentMapper;
    private final ParamsConfig paramsConfig;

    private OntologySDKService sdkService;

    @Autowired
    public OntIdServiceImpl(OntidTxDetailMapper ontidTxDetailMapper, CurrentMapper currentMapper, ParamsConfig paramsConfig) {
        this.ontidTxDetailMapper = ontidTxDetailMapper;
        this.currentMapper = currentMapper;
        this.paramsConfig = paramsConfig;
    }

    private synchronized void initSDK() {
        if (sdkService == null) {
            sdkService = OntologySDKService.getInstance(paramsConfig);
        }
    }

    @Override
    public ResponseBean queryLatestOntIdTxs(int count) {

        List<OntidTxDetailDto> ontidTxDetailDtos = ontidTxDetailMapper.selectOntidTxsByPage("", 0, count);

        ontidTxDetailDtos.forEach(item -> {
            item.setDescription(Helper.templateOntIdOperation(item.getDescription()));
        });

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), ontidTxDetailDtos);
    }

    @Override
    public ResponseBean queryOntidTxsByPage(int pageSize, int pageNumber) {

        int start = pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1);
        List<OntidTxDetailDto> ontidTxDetailDtos = ontidTxDetailMapper.selectOntidTxsByPage("", start, pageSize);

        ontidTxDetailDtos.forEach(item -> {
            item.setDescription(Helper.templateOntIdOperation(item.getDescription()));
        });

        CurrentDto currentDto = currentMapper.selectSummaryInfo();

        PageResponseBean pageResponseBean = new PageResponseBean(ontidTxDetailDtos, currentDto.getOntidTxCount());

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), pageResponseBean);
    }

    @Override
    public ResponseBean queryOntIdTxsByOntid(String ontId, int pageSize, int pageNumber) {

        int start = pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1);
        List<OntidTxDetailDto> ontidTxDetailDtos = ontidTxDetailMapper.selectOntidTxsByPage(ontId, start, pageSize);

        ontidTxDetailDtos.forEach(item -> {
            item.setDescription(Helper.templateOntIdOperation(item.getDescription()));
        });

        Integer count = ontidTxDetailMapper.selectTxCountByOntid(ontId);

        PageResponseBean pageResponseBean = new PageResponseBean(ontidTxDetailDtos, count);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), pageResponseBean);


    }

    @Override
    public ResponseBean queryOntidDdo(String ontId) {

        initSDK();
        String ddoStr = sdkService.getDDO(ontId);
        log.info("{} query ddo info:{}", ontId, ddoStr);
        if (Helper.isEmptyOrNull(ddoStr)) {
            return new ResponseBean(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), false);
        }

        JSONObject ddoObj = JSON.parseObject(ddoStr);
        if (ddoObj.containsKey("Attributes")) {
            List<Object> formatedAttrList = formatDDOAttribute(ddoObj);
            ddoObj.replace("Attributes", formatedAttrList);
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), ddoObj);
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
                } else if (ClaimContextEnum.SFP_DL_CLAIM.context().equals(claimContext)) {

                    claimObj.put("ContextDesc", ClaimContextEnum.SFP_DL_CLAIM.desc());
                } else if (ClaimContextEnum.SFP_ID_CLAIM.context().equals(claimContext)) {

                    claimObj.put("ContextDesc", ClaimContextEnum.SFP_ID_CLAIM.desc());
                } else if (ClaimContextEnum.SFP_PP_CLAIM.context().equals(claimContext)) {

                    claimObj.put("ContextDesc", ClaimContextEnum.SFP_PP_CLAIM.desc());
                } else if (ClaimContextEnum.IDM_DL_CLAIM.context().equals(claimContext)) {

                    claimObj.put("ContextDesc", ClaimContextEnum.IDM_DL_CLAIM.desc());
                } else if (ClaimContextEnum.IDM_ID_CLAIM.context().equals(claimContext)) {

                    claimObj.put("ContextDesc", ClaimContextEnum.IDM_ID_CLAIM.desc());
                } else if (ClaimContextEnum.IDM_PP_CLAIM.context().equals(claimContext)) {

                    claimObj.put("ContextDesc", ClaimContextEnum.IDM_PP_CLAIM.desc());
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
