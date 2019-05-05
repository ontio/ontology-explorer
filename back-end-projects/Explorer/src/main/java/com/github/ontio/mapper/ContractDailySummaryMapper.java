package com.github.ontio.mapper;

import com.github.ontio.model.dao.ContractDailySummary;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface ContractDailySummaryMapper extends Mapper<ContractDailySummary> {

    // self-defined SQL

    List<Map> selectDailySummaryByContractHash(Map<String, Object> paramMap);
}