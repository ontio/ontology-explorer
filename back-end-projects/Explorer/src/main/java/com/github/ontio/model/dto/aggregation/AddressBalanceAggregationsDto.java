package com.github.ontio.model.dto.aggregation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author LiuQi
 */
@RequiredArgsConstructor
@Getter
public class AddressBalanceAggregationsDto implements Serializable {
	
	private final ExtremeBalanceDto max;
	
	private final ExtremeBalanceDto min;
	
	private final List<AddressAggregationDto> records;
	
}
