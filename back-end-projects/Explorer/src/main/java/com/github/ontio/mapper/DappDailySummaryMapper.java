package com.github.ontio.mapper;

import com.github.ontio.model.dao.DappDailySummary;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface DappDailySummaryMapper extends Mapper<DappDailySummary> {

    // self-defined SQL

    List<Map> selectDailyInfo(Map<String, Object> param);

    Map selectAddressAndOntIdCount(Integer startTime);
}