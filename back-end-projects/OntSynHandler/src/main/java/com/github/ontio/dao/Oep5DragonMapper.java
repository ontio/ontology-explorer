package com.github.ontio.dao;

import com.github.ontio.model.Oep5Dragon;
import org.springframework.stereotype.Component;

@Component
public interface Oep5DragonMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Oep5Dragon record);

    int insertSelective(Oep5Dragon record);

    Oep5Dragon selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Oep5Dragon record);

    int updateByPrimaryKey(Oep5Dragon record);
}