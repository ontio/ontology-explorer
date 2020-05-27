package com.github.ontio.mapper;

import com.github.ontio.model.dto.ContractDailySummaryDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface ContractDailySummaryMapper extends Mapper<ContractDailySummaryDto> {

    // self-defined SQL

    List<ContractDailySummaryDto> selectDailySummaryByContractHash(@Param("contractHash") String contractHash, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

    List<Map> selectDappstoreDappOneWeekInfo(Map<String, Object> map);

    List<Map> selectDappstoreContractYesterdayInfo(Map<String, Object> map);

    Map selectAllDappstoreDappYesterdayInfo(Map map);

}