package com.github.ontio.model.dto.aggregation;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author LiuQi
 */
@Getter
@Setter
public class ExtremeBalanceDto extends BaseAggregationDto {

	private BigDecimal balance;

}
