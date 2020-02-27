package com.github.ontio.model.dto.aggregation;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author LiuQi
 */
@Getter
@Setter
public class AddressAggregationDto extends BaseAggregationDto {

	private BigDecimal balance;

	private Integer depositCount;

	private Integer withdrawCount;

	private Integer txCount;

	private Integer depositAddr;

	private Integer withdrawAddr;

	private Integer txAddr;

	private Integer contractCount;

}
