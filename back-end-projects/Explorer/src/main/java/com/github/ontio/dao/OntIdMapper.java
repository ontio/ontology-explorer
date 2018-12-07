package com.github.ontio.dao;

import com.github.ontio.model.OntId;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Mapper
@Component(value = "OntIdMapper")
public interface OntIdMapper {
    int deleteByPrimaryKey(String txnhash);

    int insert(OntId record);

    int insertSelective(OntId record);

    OntId selectByPrimaryKey(String txnhash);

    int updateByPrimaryKeySelective(OntId record);

    int updateByPrimaryKey(OntId record);


    List<Map> selectOntIdByPage(int start, int pageSize);

    int selectOntIdTxnCount();

    List<Map> selectOntId(Map<String, Object> param);

    int selectCountByOntId(String ontId);


    int selectOntIdCountInOneDay(@Param("StartTime") long startTime, @Param("EndTime") long endTime, @Param("Description") String description);

}