package com.github.ontio.service;

import com.github.ontio.model.common.ResponseBean;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/4/28
 */
public interface IAddressService {

    ResponseBean queryAddressBalance(String address, String tokenType);

    ResponseBean queryAddressBalanceByAssetName(String address, String assetName);

    ResponseBean queryAddressBalanceByAssetName4Onto(String address, String assetName);

    ResponseBean queryTransferTxsByPage(String address, String assetName, Integer pageNumber, Integer pageSize);

    ResponseBean queryTransferTxsByTime(String address, String assetName, Long beginTime, Long endTime);

    ResponseBean queryTransferTxsByTime4Onto(String address, String assetName, Long beginTime, Long endTime);

    ResponseBean queryTransferTxsByTimeAndPage4Onto(String address, String assetName, Long endTime, Integer pageSize);


}
