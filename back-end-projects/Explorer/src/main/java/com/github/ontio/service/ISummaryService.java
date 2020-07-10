package com.github.ontio.service;

import com.github.ontio.model.common.ResponseBean;

import java.math.BigDecimal;

public interface ISummaryService {

    ResponseBean getBlockChainLatestInfo();

    ResponseBean getBlockChainTps();

    ResponseBean getBlockChainDailySummary(Long startTime, Long endTime);

    ResponseBean getContractDailySummary(String contractHash, Long startTime, Long endTime);

    ResponseBean getNativeTotalSupply();

    BigDecimal queryNativeTotalCirculatingSupply(String token);

}
