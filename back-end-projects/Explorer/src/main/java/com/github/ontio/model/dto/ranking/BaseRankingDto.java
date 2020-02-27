package com.github.ontio.model.dto.ranking;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
	
	private BigDecimal amount;
	
	private BigDecimal percentage;
	
}
