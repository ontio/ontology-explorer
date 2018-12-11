package com.github.ontio.dao;

import com.github.ontio.model.Oep8TxnDetail;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface Oep8TxnDetailMapper {
    int insert(Oep8TxnDetail record);

    int insertSelective(Oep8TxnDetail record);

    int updateByPrimaryKeySelective(Oep8TxnDetail record);

    int updateByPrimaryKey(Oep8TxnDetail record);

    List<Map> selectContractByHash(Map<String, Object> param);

    int selectContractByHashAmount(Map<String, Object> param);

}