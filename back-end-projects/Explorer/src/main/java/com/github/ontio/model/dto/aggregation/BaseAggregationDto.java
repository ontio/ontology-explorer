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

	/**
	 * 虚拟合约，表示对所有合约加总的统计
	 */
	public static final String VIRTUAL_CONTRACT_ALL = "$$ALL$$";

	/**
	 * 虚拟合约，表示对 ONT 和 ONG 加总的统计
	 */
	public static final String VIRTUAL_CONTRACT_NATIVE = "$$NATIVE$$";

	/**
	 * 虚拟合约，表示对所有 OEP4 合约加总的统计
	 */
	public static final String VIRTUAL_CONTRACT_OEP4 = "$$OEP4$$";

	private String date;

	public String getDate() {
		return date.replaceAll("-", "");
	}

}
