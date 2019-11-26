<<<<<<< Updated upstream
package com.github.ontio.service.impl;

import com.github.ontio.config.ParamsConfig;
import com.github.ontio.exception.ExplorerException;
import com.github.ontio.mapper.*;
import com.github.ontio.model.common.PageResponseBean;
import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.model.dao.Oep4;
import com.github.ontio.model.dao.Oep5;
import com.github.ontio.model.dao.Oep8;
import com.github.ontio.model.dto.*;
import com.github.ontio.service.ITokenService;
import com.github.ontio.util.ConstantParam;
import com.github.ontio.util.ErrorInfo;
import com.github.ontio.util.Helper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/5/8
 */
@Service("TokenService")
@Slf4j
public class TokenServiceImpl implements ITokenService {

    private final ContractMapper contractMapper;
    private final Oep4Mapper oep4Mapper;
    private final Oep5Mapper oep5Mapper;
    private final Oep8Mapper oep8Mapper;
    private final Oep8TxDetailMapper oep8TxDetailMapper;
    private final AmazonS3Service amazonS3Service;
    private final ParamsConfig paramsConfig;

    @Autowired
    public TokenServiceImpl(Oep4Mapper oep4Mapper, Oep5Mapper oep5Mapper, Oep8Mapper oep8Mapper, Oep8TxDetailMapper oep8TxDetailMapper, ContractMapper contractMapper, AmazonS3Service amazonS3Service, ParamsConfig paramsConfig) {
        this.contractMapper = contractMapper;
        this.oep4Mapper = oep4Mapper;
        this.oep5Mapper = oep5Mapper;
        this.oep8Mapper = oep8Mapper;
        this.oep8TxDetailMapper = oep8TxDetailMapper;
        this.amazonS3Service = amazonS3Service;
        this.paramsConfig = paramsConfig;
    }

    @Override
    public ResponseBean queryTokensByPage(String tokenType, Integer pageNumber, Integer pageSize) {

        int total = 0;
        PageResponseBean pageResponseBean = new PageResponseBean(new ArrayList(), total);

        switch (tokenType.toLowerCase()) {
            case ConstantParam.ASSET_TYPE_OEP4:
                PageHelper.startPage(pageNumber, pageSize);
                List<Oep4DetailDto> oep4DetailDtos = oep4Mapper.selectOep4Tokens();
                total = ((Long) ((Page) oep4DetailDtos).getTotal()).intValue();
                pageResponseBean.setTotal(total);
                pageResponseBean.setRecords(oep4DetailDtos);
                break;
            case ConstantParam.ASSET_TYPE_OEP5:
                PageHelper.startPage(pageNumber, pageSize);
                List<Oep5DetailDto> oep5DetailDtos = oep5Mapper.selectOep5Tokens();
                total = ((Long) ((Page) oep5DetailDtos).getTotal()).intValue();
                pageResponseBean.setTotal(total);
                pageResponseBean.setRecords(oep5DetailDtos);
                break;
            case ConstantParam.ASSET_TYPE_OEP8:
                PageHelper.startPage(pageNumber, pageSize);
                List<Oep8DetailDto> oep8DetailDtos = oep8Mapper.selectOep8Tokens();
                total = ((Long) ((Page) oep8DetailDtos).getTotal()).intValue();
                //OEP8同一个合约hash有多种token，需要根据tokenId分类
                oep8DetailDtos.forEach(item -> {
                    item = formatOep8DetailDto(item);
                });

                pageResponseBean.setTotal(total);
                pageResponseBean.setRecords(oep8DetailDtos);
                break;
        }

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), pageResponseBean);
    }

    /**
     * 根据tokenid格式化oep8 token信息
     *
     * @param oep8DetailDto
     * @return
     */
    private Oep8DetailDto formatOep8DetailDto(Oep8DetailDto oep8DetailDto) {

        Map tokenIdMap = new HashMap();
        Map totalSupplyMap = new HashMap();
        Map symbolMap = new HashMap();
        Map tokenNameMap = new HashMap();

        String[] tokenIds = ((String) oep8DetailDto.getTokenId()).split(",");
        String[] totalSupplys = ((String) oep8DetailDto.getTotalSupply()).split(",");
        String[] symbols = ((String) oep8DetailDto.getSymbol()).split(",");
        String[] tokenNames = ((String) oep8DetailDto.getTokenName()).split(",");

        for (int i = 0; i < tokenIds.length; i++) {
            tokenIdMap.put(tokenIds[i], tokenIds[i]);
            totalSupplyMap.put(tokenIds[i], totalSupplys[i]);
            symbolMap.put(tokenIds[i], symbols[i]);
            tokenNameMap.put(tokenIds[i], tokenNames[i]);
        }
        oep8DetailDto.setTotalSupply(totalSupplyMap);
        oep8DetailDto.setSymbol(symbolMap);
        oep8DetailDto.setTokenName(tokenNameMap);
        oep8DetailDto.setTokenId(tokenIdMap);

        return oep8DetailDto;
    }


    @Override
    public ResponseBean queryTokenDetail(String tokenType, String contractHash) {

        Object obj = new Object();

        switch (tokenType.toLowerCase()) {
            case ConstantParam.ASSET_TYPE_OEP4:
                Oep4DetailDto oep4DetailDto = oep4Mapper.selectOep4TokenDetail(contractHash);
                obj = oep4DetailDto;
                break;
            case ConstantParam.ASSET_TYPE_OEP5:
                Oep5DetailDto oep5DetailDto = oep5Mapper.selectOep5TokenDetail(contractHash);
                obj = oep5DetailDto;
                break;
            case ConstantParam.ASSET_TYPE_OEP8:
                Oep8DetailDto oep8DetailDto = oep8Mapper.selectOep8TokenDetail(contractHash);
                if (Helper.isNotEmptyAndNull(oep8DetailDto)) {
                    oep8DetailDto = formatOep8DetailDto(oep8DetailDto);
                }
                obj = oep8DetailDto;
                break;
        }
        if (Helper.isEmptyOrNull(obj)) {
            return new ResponseBean(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), false);
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), obj);
    }

    @Override
    public ResponseBean queryOep8TxsByPage(String contractHash, String tokenName, Integer pageNumber, Integer pageSize) {

        int start = pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1);

        List<TxDetailDto> txDetailDtos = oep8TxDetailMapper.selectTxsByCalledContractHashAndTokenName(contractHash, tokenName, start, pageSize);
        Integer count = oep8TxDetailMapper.selectCountByCalledContracthashAndTokenName(contractHash, tokenName);

        PageResponseBean pageResponseBean = new PageResponseBean(txDetailDtos, count);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), pageResponseBean);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBean submitOep4(SubmitContractDto submitContractDto) {

        String contractHash = submitContractDto.getContractHash();
        ContractDto contractDtoTemp = contractMapper.selectByPrimaryKey(contractHash);
        if (Helper.isEmptyOrNull(contractDtoTemp)) {
            throw new ExplorerException(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), false);
        } else if (contractDtoTemp.getAuditFlag() == 1) {
            throw new ExplorerException(ErrorInfo.ALREADY_AUDITPASS.code(), ErrorInfo.ALREADY_AUDITPASS.desc(), false);
        }

        String fileName = contractHash + ConstantParam.LOGO_PNG_SUFFIX;
        amazonS3Service.uploadFile2S3(submitContractDto.getLogo(), fileName);

        ContractDto contractDto = ContractDto.builder()
                .contractHash(contractHash)
                .name(submitContractDto.getName())
                .description(submitContractDto.getDescription())
                .abi(submitContractDto.getAbi())
                .code(submitContractDto.getCode())
                .contactInfo(submitContractDto.getContactInfo().toJSONString())
                .auditFlag(0)
                .channel(ConstantParam.CONTRACT_CHANNEL_USER)
                .logo(paramsConfig.LOGO_URL_PREFIX + fileName)
                .dappName(submitContractDto.getDappName())
                .category(submitContractDto.getCategory())
                .type(ConstantParam.CONTRACT_TYPE_OEP4)
                .build();
        int i = contractMapper.updateByPrimaryKeySelective(contractDto);

        Oep4 oep4 = new Oep4();
        oep4.setContractHash(contractHash);
        oep4.setName(submitContractDto.getName());
        oep4.setTotalSupply(new BigDecimal(submitContractDto.getTotalSupply()).longValue());
        oep4.setSymbol(submitContractDto.getSymbol());
        oep4.setDecimals(submitContractDto.getDecimals());
        oep4.setAuditFlag(false);
        oep4.setCreateTime(new Date());
        oep4.setUpdateTime(new Date());
        oep4.setVmCategory(submitContractDto.getVmCategory());

        Oep4 oep4Temp = oep4Mapper.selectByPrimaryKey(contractHash);
        if (Helper.isEmptyOrNull(oep4Temp)) {
            oep4Mapper.insertSelective(oep4);
        } else if (oep4Temp.getAuditFlag() == true) {
            throw new ExplorerException(ErrorInfo.ALREADY_AUDITPASS.code(), ErrorInfo.ALREADY_AUDITPASS.desc(), false);
        }else {
            oep4Mapper.updateByPrimaryKeySelective(oep4);
        }

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), true);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBean submitOep5(SubmitContractDto submitContractDto) {

        String contractHash = submitContractDto.getContractHash();
        ContractDto contractDtoTemp = contractMapper.selectByPrimaryKey(contractHash);
        if (Helper.isEmptyOrNull(contractDtoTemp)) {
            throw new ExplorerException(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), false);
        } else if (contractDtoTemp.getAuditFlag() == 1) {
            throw new ExplorerException(ErrorInfo.ALREADY_AUDITPASS.code(), ErrorInfo.ALREADY_AUDITPASS.desc(), false);
        }

        String fileName = contractHash + ConstantParam.LOGO_PNG_SUFFIX;
        amazonS3Service.uploadFile2S3(submitContractDto.getLogo(), fileName);

        ContractDto contractDto = ContractDto.builder()
                .contractHash(contractHash)
                .name(submitContractDto.getName())
                .description(submitContractDto.getDescription())
                .abi(submitContractDto.getAbi())
                .code(submitContractDto.getCode())
                .contactInfo(submitContractDto.getContactInfo().toJSONString())
                .auditFlag(0)
                .channel(ConstantParam.CONTRACT_CHANNEL_USER)
                .logo(paramsConfig.LOGO_URL_PREFIX + fileName)
                .dappName(submitContractDto.getDappName())
                .category(submitContractDto.getCategory())
                .type(ConstantParam.CONTRACT_TYPE_OEP5)
                .build();
        int i = contractMapper.updateByPrimaryKeySelective(contractDto);

        Oep5 oep5 = new Oep5();
        oep5.setContractHash(contractHash);
        oep5.setName(submitContractDto.getName());
        oep5.setTotalSupply(new BigDecimal(submitContractDto.getTotalSupply()).longValue());
        oep5.setSymbol(submitContractDto.getSymbol());
        oep5.setAuditFlag(false);
        oep5.setCreateTime(new Date());
        oep5.setUpdateTime(new Date());
        oep5.setVmCategory(submitContractDto.getVmCategory());

        Oep5 oep5Temp = oep5Mapper.selectByPrimaryKey(contractHash);
        if (Helper.isEmptyOrNull(oep5Temp)) {
            oep5Mapper.insertSelective(oep5);
        } else if (oep5Temp.getAuditFlag() == true) {
            throw new ExplorerException(ErrorInfo.ALREADY_AUDITPASS.code(), ErrorInfo.ALREADY_AUDITPASS.desc(), false);
        }else {
            oep5Mapper.updateByPrimaryKeySelective(oep5);
        }

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), true);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBean submitOep8(SubmitContractDto submitContractDto) {

        String contractHash = submitContractDto.getContractHash();
        ContractDto contractDtoTemp = contractMapper.selectByPrimaryKey(contractHash);
        if (Helper.isEmptyOrNull(contractDtoTemp)) {
            throw new ExplorerException(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), false);
        } else if (contractDtoTemp.getAuditFlag() == 1) {
            throw new ExplorerException(ErrorInfo.ALREADY_AUDITPASS.code(), ErrorInfo.ALREADY_AUDITPASS.desc(), false);
        }

        String fileName = contractHash + ConstantParam.LOGO_PNG_SUFFIX;
        amazonS3Service.uploadFile2S3(submitContractDto.getLogo(), fileName);

        ContractDto contractDto = ContractDto.builder()
                .contractHash(contractHash)
                .name(submitContractDto.getName())
                .description(submitContractDto.getDescription())
                .abi(submitContractDto.getAbi())
                .code(submitContractDto.getCode())
                .contactInfo(submitContractDto.getContactInfo().toJSONString())
                .auditFlag(0)
                .channel(ConstantParam.CONTRACT_CHANNEL_USER)
                .logo(paramsConfig.LOGO_URL_PREFIX + fileName)
                .dappName(submitContractDto.getDappName())
                .category(submitContractDto.getCategory())
                .type(ConstantParam.CONTRACT_TYPE_OEP8)
                .build();
        int i = contractMapper.updateByPrimaryKeySelective(contractDto);

        Oep8 oep8Temp = new Oep8();
        oep8Temp.setContractHash(contractHash);
        int count = oep8Mapper.selectCount(oep8Temp);
        if (count > 0) {
            throw new ExplorerException(ErrorInfo.ALREADY_EXSIT.code(), ErrorInfo.ALREADY_EXSIT.desc(), false);
        }

        submitContractDto.getTokens().forEach(item -> {
            Oep8 oep8 = new Oep8();
            oep8.setContractHash(contractHash);
            oep8.setTokenId(item.getTokenId());
            oep8.setName(item.getTokenName());
            oep8.setTotalSupply(new BigDecimal(item.getTotalSupply()).longValue());
            oep8.setSymbol(item.getSymbol());
            oep8.setAuditFlag(false);
            oep8.setCreateTime(new Date());
            oep8.setUpdateTime(new Date());
            oep8.setVmCategory(submitContractDto.getVmCategory());
            oep8Mapper.insertSelective(oep8);
        });

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), true);
    }

}
=======
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
import com.github.ontio.util.Helper;
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

        switch (tokenType.toLowerCase()) {
            case ConstantParam.ASSET_TYPE_OEP4:
                PageHelper.startPage(pageNumber, pageSize);
                List<Oep4DetailDto> oep4DetailDtos = oep4Mapper.selectOep4Tokens();
                total = ((Long) ((Page) oep4DetailDtos).getTotal()).intValue();
                pageResponseBean.setTotal(total);
                pageResponseBean.setRecords(oep4DetailDtos);
                break;
            case ConstantParam.ASSET_TYPE_OEP5:
                PageHelper.startPage(pageNumber, pageSize);
                List<Oep5DetailDto> oep5DetailDtos = oep5Mapper.selectOep5Tokens();
                total = ((Long) ((Page) oep5DetailDtos).getTotal()).intValue();
                pageResponseBean.setTotal(total);
                pageResponseBean.setRecords(oep5DetailDtos);
                break;
            case ConstantParam.ASSET_TYPE_OEP8:
                PageHelper.startPage(pageNumber, pageSize);
                List<Oep8DetailDto> oep8DetailDtos = oep8Mapper.selectOep8Tokens();
                total = ((Long) ((Page) oep8DetailDtos).getTotal()).intValue();
                //OEP8同一个合约hash有多种token，需要根据tokenId分类
                oep8DetailDtos.forEach(item -> {
                    item = formatOep8DetailDto(item);
                });

                pageResponseBean.setTotal(total);
                pageResponseBean.setRecords(oep8DetailDtos);
                break;
        }

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), pageResponseBean);
    }

    /**
     * 根据tokenid格式化oep8 token信息
     * @param oep8DetailDto
     * @return
     */
    private Oep8DetailDto formatOep8DetailDto(Oep8DetailDto oep8DetailDto) {

        Map tokenIdMap = new HashMap();
        Map totalSupplyMap = new HashMap();
        Map symbolMap = new HashMap();
        Map tokenNameMap = new HashMap();

        String[] tokenIds = ((String) oep8DetailDto.getTokenId()).split(",");
        String[] totalSupplys = ((String) oep8DetailDto.getTotalSupply()).split(",");
        String[] symbols = ((String) oep8DetailDto.getSymbol()).split(",");
        String[] tokenNames = ((String) oep8DetailDto.getTokenName()).split(",");

        for (int i = 0; i < tokenIds.length; i++) {
            tokenIdMap.put(tokenIds[i], tokenIds[i]);
            totalSupplyMap.put(tokenIds[i], totalSupplys[i]);
            symbolMap.put(tokenIds[i], symbols[i]);
            tokenNameMap.put(tokenIds[i], tokenNames[i]);
        }
        oep8DetailDto.setTotalSupply(totalSupplyMap);
        oep8DetailDto.setSymbol(symbolMap);
        oep8DetailDto.setTokenName(tokenNameMap);
        oep8DetailDto.setTokenId(tokenIdMap);

        return oep8DetailDto;
    }


    @Override
    public ResponseBean queryTokenDetail(String tokenType, String contractHash) {

        Object obj = new Object();

        switch (tokenType.toLowerCase()) {
            case ConstantParam.ASSET_TYPE_OEP4:
                Oep4DetailDto oep4DetailDto = oep4Mapper.selectOep4TokenDetail(contractHash);
                obj = oep4DetailDto;
                break;
            case ConstantParam.ASSET_TYPE_OEP5:
                Oep5DetailDto oep5DetailDto = oep5Mapper.selectOep5TokenDetail(contractHash);
                obj = oep5DetailDto;
                break;
            case ConstantParam.ASSET_TYPE_OEP8:
                Oep8DetailDto oep8DetailDto = oep8Mapper.selectOep8TokenDetail(contractHash);
                if (Helper.isNotEmptyOrNull(oep8DetailDto)) {
                    oep8DetailDto = formatOep8DetailDto(oep8DetailDto);
                }
                obj = oep8DetailDto;
                break;
        }
        if (Helper.isEmptyOrNull(obj)) {
            return new ResponseBean(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), false);
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), obj);
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
>>>>>>> Stashed changes
