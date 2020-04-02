package com.github.ontio.model.dto.ranking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.ontio.util.TxAmountSerializer;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author LiuQi
 */
@Getter
@Setter
public abstract class BaseRankingDto implements Serializable {

	@JsonIgnore
	private short rankingId;

	private short ranking;

	@JsonSerialize(using = TxAmountSerializer.class)
	private BigDecimal amount;

	@JsonSerialize(using = TxAmountSerializer.class)
	private BigDecimal percentage;

}
