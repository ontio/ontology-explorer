package com.github.ontio.dao;

import com.github.ontio.model.OntId;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "OntIdMapper")
public interface OntIdMapper {
    int deleteByPrimaryKey(String txnhash);

    int insert(OntId record);

    int insertSelective(OntId record);

    OntId selectByPrimaryKey(String txnhash);

    int updateByPrimaryKeySelective(OntId record);

    int updateByPrimaryKey(OntId record);

    void deleteByHeight(int height);

    int selectCountByHeight(@Param("height") int height);

}