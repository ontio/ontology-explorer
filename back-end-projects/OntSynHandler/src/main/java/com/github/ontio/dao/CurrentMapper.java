package com.github.ontio.dao;

import com.github.ontio.model.Current;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Map;


@Mapper
@Component(value = "CurrentMapper")
public interface CurrentMapper {
    int insert(Current record);

    int insertSelective(Current record);

    int selectDBHeight();

    Map<String,Integer> selectTxnCount();

    void update(Current current);

}