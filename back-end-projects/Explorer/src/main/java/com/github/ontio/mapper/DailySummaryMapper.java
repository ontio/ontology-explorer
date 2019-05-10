package com.github.ontio.mapper;

import com.github.ontio.model.dto.DailySummaryDto;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface DailySummaryMapper extends Mapper<DailySummaryDto> {

    List<DailySummaryDto> selectSummaryByTime(@Param("startTime") Long startTime, @Param("endTime") Long endTime);

    Map selectAddrAndOntIdCount(@Param("startTime") Long startTime);


}