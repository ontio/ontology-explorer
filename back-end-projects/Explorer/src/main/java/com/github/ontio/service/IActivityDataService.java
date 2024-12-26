package com.github.ontio.service;

import com.alibaba.fastjson.JSONObject;
import com.github.ontio.model.dto.Anniversary6thDataDto;

public interface IActivityDataService {

    Anniversary6thDataDto queryAddress6thAnniversaryData(String address);

    Integer queryAddressTxCountInPeriod(String address, Integer startTime, Integer endTime);

    String queryAddressCertainTimeBalance(String address, Integer timestamp);

    JSONObject queryAddressActiveTime(String address);

    JSONObject queryAddressSendTxInfo(String address);

    JSONObject queryRunningNodeInfo(String address);
}
