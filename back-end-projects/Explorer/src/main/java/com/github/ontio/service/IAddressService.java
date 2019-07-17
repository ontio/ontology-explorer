package com.github.ontio.service;

import com.github.ontio.model.common.ResponseBean;

public interface IAddressService {

    ResponseBean queryAddressBalance(String address, String tokenType);

    ResponseBean queryTransferTxsByPage(String address, String assetName, Integer pageNumber, Integer pageSize);

    ResponseBean queryTransferTxsByTime(String address, String assetName, Long beginTime, Long endTime);

    ResponseBean queryTransferTxsByTimeAndPage(String address, String assetName, Long endTime, Integer pageSize);

}
