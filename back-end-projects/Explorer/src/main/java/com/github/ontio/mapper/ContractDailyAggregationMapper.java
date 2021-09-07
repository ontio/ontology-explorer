package com.github.ontio.mapper;

import com.github.ontio.model.dao.ContractDailyAggregation;
import com.github.ontio.model.dto.aggregation.ContractAggregationDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

@Repository
public interface ContractDailyAggregationMapper extends Mapper<ContractDailyAggregation> {

	List<ContractAggregationDto> findAggregations(@Param("contractHash") String contractHash,
                                                  @Param("tokenContractHash") String tokenContractHash, @Param("from") Date from, @Param("to") Date to);

	List<ContractAggregationDto> findAggregationsForToken(@Param("contractHash") String contractHash,
                                                          @Param("tokenContractHash") String tokenContractHash, @Param("from") Date from, @Param("to") Date to);
	
}