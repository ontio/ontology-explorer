package com.github.ontio.dao;

import com.github.ontio.model.Current;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface CurrentMapper {
    int insert(Current record);

    int insertSelective(Current record);

    int selectDBHeight();

    Map<String,Integer> selectSummary();

    void update(Current current);
}