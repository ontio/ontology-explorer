package com.github.ontio.model.dto.aggregation;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.ontio.util.TxAmountSerializer;
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

	@JsonSerialize(using = TxAmountSerializer.class)
	private BigDecimal txAmount;

	private Integer depositAddr;

	private Integer withdrawAddr;

	private Integer txAddr;

}
