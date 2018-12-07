package com.github.ontio.dao;

import com.github.ontio.model.Daily;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DailyMapper {
    int deleteByPrimaryKey(Date time);

    int insert(Daily record);

    int insertSelective(Daily record);

    Daily selectByPrimaryKey(Date time);

    int updateByPrimaryKeySelective(Daily record);

    int updateByPrimaryKey(Daily record);


    List<Map> selectDailyInfo(@Param("StartTime") long startTime, @Param("EndTime") long endTime);

}