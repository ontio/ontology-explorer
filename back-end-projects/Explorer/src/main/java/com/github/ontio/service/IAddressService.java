package com.github.ontio.service;

import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.sdk.exception.SDKException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface IAddressService {

    ResponseBean queryAddressBalance(String address, String tokenType);

    ResponseBean queryEVMAddressBalance(String address, String tokenType);

    ResponseBean queryAddressBalanceByAssetName(String address, String assetName);

    ResponseBean queryAddressBalanceByContractHash(String address, String contractHash);

    ResponseBean queryAddressBalanceByAssetName4Onto(String address, String assetName);

    ResponseBean queryTransferTxsByPage(String address, String assetName, Integer pageNumber, Integer pageSize);

    ResponseBean queryTransferTxsByTime(String address, String assetName, Long beginTime, Long endTime);

    ResponseBean queryTransferTxsByTime4Onto(String address, String assetName, Long beginTime, Long endTime, String addressType);

    ResponseBean queryTransferTxsByTimeAndPage4Onto(String address, String assetName, Long endTime, Integer pageSize,
                                                    String addressType);

    ResponseBean queryDailyAggregation(String address, String token, Date from, Date to) throws SDKException;

    ResponseBean queryDailyAggregationOfTokenType(String address, String tokenType, Date from, Date to);

    ResponseBean queryRankings(List<Short> rankingIds, short duration);

    ResponseBean queryTransferTxsWithTotalByPage(String address, String assetName, Integer pageNumber, Integer pageSize);

    ResponseBean queryTransferTxsOfTokenTypeByPage(String address, String tokenType, Integer pageNumber, Integer pageSize);

    void exportAddressTransferTxs(String token, String language, String address, Integer start, Integer end, HttpServletResponse resp) throws IOException;

    ResponseBean getAddressStakingInfo(String address);

    ResponseBean getAddressStakingInfoWhenRoundStart(String address);
}
