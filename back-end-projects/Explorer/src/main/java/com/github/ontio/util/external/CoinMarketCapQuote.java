package com.github.ontio.util.external;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author LiuQi
 */
@Getter
@Setter
public class CoinMarketCapQuote implements Serializable {

	private BigDecimal price;
	
	@JSONField(name = "percent_change_1h")
	private BigDecimal percentageOneHour;
	
	@JSONField(name = "percent_change_24h")
	private BigDecimal percentageOneDay;
	
	@JSONField(name = "percent_change_7d")
	private BigDecimal percentageSevenDays;
	
}
