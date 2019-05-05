package com.github.ontio.service;

import com.github.ontio.model.common.ResponseBean;

public interface ISummaryService {

    ResponseBean getLatestInfo();

    ResponseBean getTps();

    ResponseBean getChainSummary(String type, int startTime, int endTime);

    ResponseBean getContractSummary(String contractHash, String type, int startTime, int endTime);
}
