package com.github.ontio.mapper;

import com.github.ontio.model.dao.TokenDailyAggregation;
import com.github.ontio.model.dto.aggregation.TokenAggregationDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

@Repository
public interface TokenDailyAggregationMapper extends Mapper<TokenDailyAggregation> {

	List<TokenAggregationDto> findAggregations(@Param("contractHash") String contractHash, @Param("from") Date from,
			@Param("to") Date to);
	
}