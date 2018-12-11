package com.github.ontio.dao;

import com.github.ontio.model.Oep8TxnDetail;
import org.springframework.stereotype.Component;

@Component
public interface Oep8TxnDetailMapper {
    int insert(Oep8TxnDetail record);

    int insertSelective(Oep8TxnDetail record);

    int updateByPrimaryKeySelective(Oep8TxnDetail record);

    int updateByPrimaryKey(Oep8TxnDetail record);
}