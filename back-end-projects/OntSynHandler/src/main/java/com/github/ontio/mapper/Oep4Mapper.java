package com.github.ontio.mapper;

import com.github.ontio.model.Oep4;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface Oep4Mapper {
    int insert(Oep4 record);

    int insertSelective(Oep4 record);

    int updateByPrimaryKeySelective(Oep4 record);

    int updateByPrimaryKey(Oep4 record);

    List<Map> selectApprovedKeyInfo();
}