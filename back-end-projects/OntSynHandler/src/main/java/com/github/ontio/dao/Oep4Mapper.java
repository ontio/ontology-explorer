package com.github.ontio.dao;

import com.github.ontio.model.Oep4;
import com.github.ontio.model.Oep4Key;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface Oep4Mapper {
    int deleteByPrimaryKey(Oep4Key key);

    int insert(Oep4 record);

    int insertSelective(Oep4 record);

    Oep4 selectByPrimaryKey(Oep4Key key);

    int updateByPrimaryKeySelective(Oep4 record);

    int updateByPrimaryKey(Oep4 record);

    List<Map> selectAllKeyInfo();
}