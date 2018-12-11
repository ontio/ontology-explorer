package com.github.ontio.dao;

import com.github.ontio.model.Oep8;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface Oep8Mapper {
    int deleteByPrimaryKey(Long id);

    int insert(Oep8 record);

    int insertSelective(Oep8 record);

    Oep8 selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Oep8 record);

    int updateByPrimaryKey(Oep8 record);

    List<Map> selectAllKeyInfo();

    int updateTotalSupply(Oep8 oep8);
}