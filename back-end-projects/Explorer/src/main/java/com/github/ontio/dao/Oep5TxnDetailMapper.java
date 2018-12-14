package com.github.ontio.dao;

import com.github.ontio.model.Oep5TxnDetail;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface Oep5TxnDetailMapper {
    int insert(Oep5TxnDetail record);

    int insertSelective(Oep5TxnDetail record);

    int updateByPrimaryKeySelective(Oep5TxnDetail record);

    int updateByPrimaryKey(Oep5TxnDetail record);

    List<Map> selectContractByHash(Map<String, Object> param);
}