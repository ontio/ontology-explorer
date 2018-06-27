package com.github.ontio.dao;

import com.github.ontio.model.TransactionDetail;
import com.github.ontio.model.TransactionDetailKey;

import java.util.List;
import java.util.Map;

public interface TransactionDetailMapper {
    int deleteByPrimaryKey(TransactionDetailKey key);

    int insert(TransactionDetail record);

    int insertSelective(TransactionDetail record);

    TransactionDetail selectByPrimaryKey(TransactionDetailKey key);

    int updateByPrimaryKeySelective(TransactionDetail record);

    int updateByPrimaryKey(TransactionDetail record);

    List<Map> selectTransferTxnDetailByParam(Map<String,String> param);

    List<Map> selectTransferTxnDetailByHash(String txnHash);

    List<Map> selectBalanceByAddress(String address);

    List<String> selectTxnHashByAddressInfo(Map<String,Object> param);

    int selectTxnAmountByAddressInfo(Map<String,Object> param);

}