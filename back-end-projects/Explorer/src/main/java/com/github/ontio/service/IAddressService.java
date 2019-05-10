package com.github.ontio.service;

import com.github.ontio.model.common.ResponseBean;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/4/28
 */
public interface IAddressService {

    ResponseBean queryAddressBalance(String address);

    ResponseBean queryTransferTxsByPage(String address, String assetName, Integer pageNumber, Integer pageSize);

    ResponseBean queryTransferTxsByTime(String address, String assetName, Long beginTime, Long endTime);


}
