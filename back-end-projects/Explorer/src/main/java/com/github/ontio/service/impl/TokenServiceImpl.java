package com.github.ontio.service.impl;

import com.github.ontio.mapper.Oep4Mapper;
import com.github.ontio.mapper.Oep5Mapper;
import com.github.ontio.mapper.Oep8Mapper;
import com.github.ontio.mapper.Oep8TxDetailMapper;
import com.github.ontio.model.common.PageResponseBean;
import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.model.dto.Oep4DetailDto;
import com.github.ontio.model.dto.Oep5DetailDto;
import com.github.ontio.model.dto.Oep8DetailDto;
import com.github.ontio.model.dto.TxDetailDto;
import com.github.ontio.service.ITokenService;
import com.github.ontio.util.ConstantParam;
import com.github.ontio.util.ErrorInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
 * @date 2019/5/8
 */
@Service("TokenService")
@Slf4j
public class TokenServiceImpl implements ITokenService {

    private final Oep4Mapper oep4Mapper;
    private final Oep5Mapper oep5Mapper;
    private final Oep8Mapper oep8Mapper;
    private final Oep8TxDetailMapper oep8TxDetailMapper;

    @Autowired
    public TokenServiceImpl(Oep4Mapper oep4Mapper, Oep5Mapper oep5Mapper, Oep8Mapper oep8Mapper, Oep8TxDetailMapper oep8TxDetailMapper) {
        this.oep4Mapper = oep4Mapper;
        this.oep5Mapper = oep5Mapper;
        this.oep8Mapper = oep8Mapper;
        this.oep8TxDetailMapper = oep8TxDetailMapper;
    }

    @Override
    public ResponseBean queryTokensByPage(String tokenType, Integer pageNumber, Integer pageSize) {

        int total = 0;
        PageResponseBean pageResponseBean = new PageResponseBean(new ArrayList(), total);

        int start = pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1);

        switch (tokenType.toLowerCase()) {
            case ConstantParam.ASSET_TYPE_OEP4:
                PageHelper.startPage(start, pageSize);
                List<Oep4DetailDto> oep4DetailDtos = oep4Mapper.selectOep4Tokens();
                total = ((Long) ((Page) oep4DetailDtos).getTotal()).intValue();
                pageResponseBean.setTotal(total);
                pageResponseBean.setRecords(oep4DetailDtos);
                break;
            case ConstantParam.ASSET_TYPE_OEP5:
                PageHelper.startPage(start, pageSize);
                List<Oep5DetailDto> oep5DetailDtos = oep5Mapper.selectOep5Tokens();
                total = ((Long) ((Page) oep5DetailDtos).getTotal()).intValue();
                pageResponseBean.setTotal(total);
                pageResponseBean.setRecords(oep5DetailDtos);
                break;
            case ConstantParam.ASSET_TYPE_OEP8:
                PageHelper.startPage(start, pageSize);
                List<Oep8DetailDto> oep8DetailDtos = oep8Mapper.selectOep8Tokens();
                total = ((Long) ((Page) oep8DetailDtos).getTotal()).intValue();
                //OEP8同一个合约hash有多种token，需要根据tokenId分类
                oep8DetailDtos.forEach(item -> {

                    Map tokenIdMap = new HashMap();
                    Map totalSupplyMap = new HashMap();
                    Map symbolMap = new HashMap();
                    Map tokenNameMap = new HashMap();

                    String[] tokenIds = ((String) item.getTokenId()).split(",");
                    String[] totalSupplys = ((String) item.getTotalSupply()).split(",");
                    String[] symbols = ((String) item.getSymbol()).split(",");
                    String[] tokenNames = ((String) item.getTokenName()).split(",");

                    for (int i = 0; i < tokenIds.length; i++) {
                        tokenIdMap.put(tokenIds[i], tokenIds[i]);
                        totalSupplyMap.put(tokenIds[i], totalSupplys[i]);
                        symbolMap.put(tokenIds[i], symbols[i]);
                        tokenNameMap.put(tokenIds[i], tokenNames[i]);
                    }
                    item.setTotalSupply(totalSupplyMap);
                    item.setSymbol(symbolMap);
                    item.setTokenName(tokenNameMap);
                    item.setTokenId(tokenIdMap);
                });

                pageResponseBean.setTotal(total);
                pageResponseBean.setRecords(oep8DetailDtos);
                break;
        }

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), pageResponseBean);
    }

    @Override
    public ResponseBean queryOep8TxsByPage(String contractHash, String tokenName, Integer pageNumber, Integer pageSize) {

        int start = pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1);

        List<TxDetailDto> txDetailDtos = oep8TxDetailMapper.selectTxsByCalledContractHashAndTokenName(contractHash, tokenName, start, pageSize);
        Integer count = oep8TxDetailMapper.selectCountByCalledContracthashAndTokenName(contractHash, tokenName);

        PageResponseBean pageResponseBean = new PageResponseBean(txDetailDtos, count);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), pageResponseBean);
    }
}
