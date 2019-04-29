package com.github.ontio.mapper;

import com.github.ontio.model.Current;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface CurrentMapper {
    int insert(Current record);

    int insertSelective(Current record);

    Integer selectDBHeight();

    Map<String,Integer> selectSummary();

    void update(Current current);
}