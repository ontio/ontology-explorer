package com.github.ontio.service;

import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.model.dto.TokenInfoDto;

import java.math.BigDecimal;
import java.util.List;

public interface ISummaryService {

    ResponseBean getBlockChainLatestInfo();

    ResponseBean getBlockChainTps();

    ResponseBean getBlockChainDailySummary(Long startTime, Long endTime);

    ResponseBean getContractDailySummary(String contractHash, Long startTime, Long endTime);

    ResponseBean getNativeTotalSupply();

    BigDecimal queryNativeTotalCirculatingSupply(String token);

    List<TokenInfoDto> queryTokenInfo(String token);
}
