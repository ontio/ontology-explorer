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
import com.github.ontio.mapper.CurrentMapper;
import com.github.ontio.mapper.OntidTxDetailMapper;
import com.github.ontio.mapper.TxDetailMapper;
import com.github.ontio.mapper.TxEventLogMapper;
import com.github.ontio.model.common.EventTypeEnum;
import com.github.ontio.model.common.PageResponseBean;
import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.model.dto.CurrentDto;
import com.github.ontio.model.dto.OntidTxDetailDto;
import com.github.ontio.model.dto.TxDetailDto;
import com.github.ontio.model.dto.TxEventLogDto;
import com.github.ontio.service.ITransactionService;
import com.github.ontio.util.ConstantParam;
import com.github.ontio.util.ErrorInfo;
import com.github.ontio.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/2/27
 */
@Service("TransactionService")
@MapperScan("com.github.ontio.dao")
@Slf4j
public class TransactionServiceImpl implements ITransactionService {

    private final TxDetailMapper txDetailMapper;
    private final CurrentMapper currentMapper;
    private final OntidTxDetailMapper ontidTxDetailMapper;
    private final TxEventLogMapper txEventLogMapper;

    @Autowired
    public TransactionServiceImpl(TxDetailMapper txDetailMapper, CurrentMapper currentMapper, OntidTxDetailMapper ontidTxDetailMapper, TxEventLogMapper txEventLogMapper) {
        this.txDetailMapper = txDetailMapper;
        this.currentMapper = currentMapper;
        this.ontidTxDetailMapper = ontidTxDetailMapper;
        this.txEventLogMapper = txEventLogMapper;
    }


    @Override
    public ResponseBean queryLatestTxs(int count) {

        List<TxEventLogDto> txEventLogDtos = txEventLogMapper.selectTxsByPage(0, count);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), txEventLogDtos);
    }

    @Override
    public ResponseBean queryTxsByPage(int pageNumber, int pageSize) {

        int start = pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1);

        List<TxEventLogDto> txEventLogDtos = txEventLogMapper.selectTxsByPage(start, pageSize);

        CurrentDto currentDto = currentMapper.selectSummaryInfo();

        PageResponseBean pageResponseBean = new PageResponseBean(txEventLogDtos, currentDto.getTxCount());

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), pageResponseBean);
    }

    @Override
    public ResponseBean queryLatestNonontidTxs(int count) {

        List<TxEventLogDto> txEventLogDtos = txEventLogMapper.selectNonontidTxsByPage(0, count);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), txEventLogDtos);
    }

    @Override
    public ResponseBean queryNonontidTxsByPage(int pageNumber, int pageSize) {

        int start = pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1);

        List<TxEventLogDto> txEventLogDtos = txEventLogMapper.selectNonontidTxsByPage(start, pageSize);

        CurrentDto currentDto = currentMapper.selectSummaryInfo();

        PageResponseBean pageResponseBean = new PageResponseBean(txEventLogDtos, currentDto.getTxCount() - currentDto.getOntidTxCount());

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), pageResponseBean);
    }

    @Override
    public ResponseBean queryTxDetailByHash(String txHash) {

        TxDetailDto txDetailDto = txDetailMapper.selectTxByHash(txHash);
        if (Helper.isEmptyOrNull(txDetailDto)) {
            return new ResponseBean(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), false);
        }

        JSONObject detailObj = new JSONObject();
        int eventType = txDetailDto.getEventType();
        //转账or权限交易，获取转账详情
        if (EventTypeEnum.Transfer.getType() == eventType || EventTypeEnum.Auth.getType() == eventType) {

            List<TxDetailDto> txDetailDtos = txDetailMapper.selectTransferTxDetailByHash(txHash);

            txDetailDtos.forEach(item -> {
                //ONG转换好精度给前端
                String assetName = item.getAssetName();
                if (ConstantParam.ONG.equals(assetName)) {
                    item.setAmount(item.getAmount().divide(ConstantParam.ONG_TOTAL));
                }
            });
            detailObj.put("transfers", txDetailDtos);
        } else if (EventTypeEnum.Ontid.getType() == eventType) {
            //ONTID交易获取ONTID动作详情
            OntidTxDetailDto ontidTxDetailDto = ontidTxDetailMapper.selectOneByTxHash(txHash);

            String ontIdDes = Helper.templateOntIdOperation(ontidTxDetailDto.getDescription());

            detailObj.put("ontid", ontidTxDetailDto.getOntid());
            detailObj.put("description", ontIdDes);
        }
        txDetailDto.setDetail(detailObj);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), txDetailDto);
    }


}
