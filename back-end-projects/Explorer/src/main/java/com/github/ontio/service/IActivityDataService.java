package com.github.ontio.service;

import com.github.ontio.model.dto.Anniversary6thDataDto;

public interface IActivityDataService {

    Anniversary6thDataDto queryAddress6thAnniversaryData(String address);
}
