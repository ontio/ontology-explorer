package com.github.ontio.mapper;

import com.github.ontio.model.dao.DailySummary;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;
@Component
public interface DailySummaryMapper extends Mapper<DailySummary> {
}