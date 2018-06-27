package com.github.ontio.dao;

import com.github.ontio.model.OntId;

import java.util.List;
import java.util.Map;

public interface OntIdMapper {
    int deleteByPrimaryKey(String txnhash);

    int insert(OntId record);

    int insertSelective(OntId record);

    OntId selectByPrimaryKey(String txnhash);

    int updateByPrimaryKeySelective(OntId record);

    int updateByPrimaryKey(OntId record);

    List<Map> selectOntIdByPage(int start, int pageSize);

    int selectOntIdCount();

    List<Map> selectOntId(Map<String,Object> param);

    int selectCountByOntId(String ontId);
}