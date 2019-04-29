package com.github.ontio.mapper;

import com.github.ontio.model.Oep5;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface Oep5Mapper {
    int deleteByPrimaryKey(String contract);

    int insert(Oep5 record);

    int insertSelective(Oep5 record);

    Oep5 selectByPrimaryKey(String contract);

    int updateByPrimaryKeySelective(Oep5 record);

    int updateByPrimaryKey(Oep5 record);

    List<Map> selectApprovedKeyInfo();
}