package com.github.ontio.mapper;

import com.github.ontio.model.dao.ContractDailySummary;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface ContractDailySummaryMapper extends Mapper<ContractDailySummary> {
}