package com.github.ontio.model.dto.aggregation;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author LiuQi
 */
@Getter
@Setter
public abstract class BaseAggregationDto implements Serializable {

	private String date;

	public String getDate() {
		return date.replaceAll("-", "");
	}

}
