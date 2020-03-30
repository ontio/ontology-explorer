package com.github.ontio.mapper;

import com.github.ontio.model.dao.AddressDailyAggregation;
import com.github.ontio.model.dto.aggregation.AddressAggregationDto;
import com.github.ontio.model.dto.aggregation.ExtremeBalanceDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

@Repository
public interface AddressDailyAggregationMapper extends Mapper<AddressDailyAggregation> {

	List<AddressAggregationDto> findAggregations(@Param("address") String address,
			@Param("tokenContractHash") String tokenContractHash, @Param("from") Date from, @Param("to") Date to);

	List<AddressAggregationDto> findAggregationsForToken(@Param("address") String address,
			@Param("tokenContractHash") String tokenContractHash, @Param("from") Date from, @Param("to") Date to);

	ExtremeBalanceDto findMaxBalance(@Param("address") String address, @Param("tokenContractHash") String tokenContractHash);

	ExtremeBalanceDto findMinBalance(@Param("address") String address, @Param("tokenContractHash") String tokenContractHash);

	Integer countTotalTxOfAddress(@Param("address") String address, @Param("asset_name") String assetName);

}