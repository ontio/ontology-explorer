package com.github.ontio.util.external;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author LiuQi
 */
@Getter
@Setter
public class CoinMarketCapPrice implements Serializable {
	
	private String symbol;
	
	private String id;
	
	private String name;
	
	private BigDecimal amount;
	
	@JSONField(name = "quote")
	private Map<String, Quote> quotes;
	
	@Getter
	@Setter
	public static class  Quote implements Serializable {
		
		private BigDecimal price;
		
	}
	
}
