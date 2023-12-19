package com.github.ontio.service;

import com.github.ontio.model.dto.Anniversary6thDataDto;

public interface IActivityDataService {

    Anniversary6thDataDto queryAddress6thAnniversaryData(String address);

    Integer queryAddressTxCountInPeriod(String address, Integer startTime, Integer endTime);

    String queryAddressCertainTimeBalance(String address, Integer timestamp);
}
