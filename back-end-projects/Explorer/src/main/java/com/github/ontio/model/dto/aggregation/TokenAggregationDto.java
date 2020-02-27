package com.github.ontio.model.dto.aggregation;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author LiuQi
 */
@Getter
@Setter
public class TokenAggregationDto extends BaseAggregationDto {

	private Integer txCount;
	
	private BigDecimal txAmount;

	private Integer depositAddr;

	private Integer withdrawAddr;

	private Integer txAddr;
	
}
