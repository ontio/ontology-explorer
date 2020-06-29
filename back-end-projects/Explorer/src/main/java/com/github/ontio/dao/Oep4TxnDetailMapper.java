package com.github.ontio.dao;

import com.github.ontio.model.Oep4TxnDetail;
import com.github.ontio.model.Oep4TxnDetailKey;

public interface Oep4TxnDetailMapper {
    int deleteByPrimaryKey(Oep4TxnDetailKey key);

    int insert(Oep4TxnDetail record);

    int insertSelective(Oep4TxnDetail record);

    Oep4TxnDetail selectByPrimaryKey(Oep4TxnDetailKey key);

    int updateByPrimaryKeySelective(Oep4TxnDetail record);

    int updateByPrimaryKey(Oep4TxnDetail record);
}