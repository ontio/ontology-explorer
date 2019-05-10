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

import com.github.ontio.mapper.*;
import com.github.ontio.model.common.PageResponseBean;
import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.model.dto.ContractDto;
import com.github.ontio.model.dto.TxDetailDto;
import com.github.ontio.service.IContractService;
import com.github.ontio.util.ConstantParam;
import com.github.ontio.util.ErrorInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("ContractService")
public class ContractServiceImpl implements IContractService {

    private final ContractMapper contractMapper;
    private final Oep4TxDetailMapper oep4TxDetailMapper;
    private final Oep5TxDetailMapper oep5TxDetailMapper;
    private final Oep8TxDetailMapper oep8TxDetailMapper;
    private final TxDetailMapper txDetailMapper;

    @Autowired
    public ContractServiceImpl(ContractMapper contractMapper, Oep4TxDetailMapper oep4TxDetailMapper, Oep5TxDetailMapper oep5TxDetailMapper, Oep8TxDetailMapper oep8TxDetailMapper, TxDetailMapper txDetailMapper) {
        this.contractMapper = contractMapper;
        this.oep4TxDetailMapper = oep4TxDetailMapper;
        this.oep5TxDetailMapper = oep5TxDetailMapper;
        this.oep8TxDetailMapper = oep8TxDetailMapper;
        this.txDetailMapper = txDetailMapper;

    }


    @Override
    public ResponseBean queryContractsByPage(Integer pageSize, Integer pageNumber) {

        int start = pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1);

        List<ContractDto> contractDtos = contractMapper.selectApprovedContract(start, pageSize);
        int count = contractMapper.selectApprovedContractCount();

        PageResponseBean pageResponseBean = new PageResponseBean(contractDtos, count);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), pageResponseBean);
    }


    @Override
    public ResponseBean queryContractDetail(String contractHash) {

        ContractDto contractDto = contractMapper.selectContractDetail(contractHash);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), contractDto);
    }


    @Override
    public ResponseBean queryTxsByContractHash(String contractType, String contractHash, Integer pageNumber, Integer pageSize) {

        List<TxDetailDto> txDetailDtos = new ArrayList<>();
        Integer count = 0;
        int start = pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1);

        switch (contractType.toLowerCase()) {
            case ConstantParam.CONTRACT_TYPE_OEP4:
                txDetailDtos = oep4TxDetailMapper.selectTxsByCalledContractHash(contractHash, start, pageSize);
                count = oep4TxDetailMapper.selectCountByCalledContracthash(contractHash);
                break;
            case ConstantParam.CONTRACT_TYPE_OEP5:
                txDetailDtos = oep5TxDetailMapper.selectTxsByCalledContractHash(contractHash, start, pageSize);
                count = oep5TxDetailMapper.selectCountByCalledContracthash(contractHash);
                break;
            case ConstantParam.CONTRACT_TYPE_OEP8:
                txDetailDtos = oep8TxDetailMapper.selectTxsByCalledContractHashAndTokenName(contractHash, "", start, pageSize);
                count = oep8TxDetailMapper.selectCountByCalledContracthashAndTokenName(contractHash, "");
                break;
            case ConstantParam.CONTRACT_TYPE_OTHER:
                txDetailDtos = txDetailMapper.selectTxsByCalledContractHash(contractHash, start, pageSize);
                count = txDetailMapper.selectCountByCalledContracthash(contractHash);
                break;
        }

        PageResponseBean pageResponseBean = new PageResponseBean(txDetailDtos, count);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), pageResponseBean);
    }

}
