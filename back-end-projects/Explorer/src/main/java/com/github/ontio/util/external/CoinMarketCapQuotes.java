package com.github.ontio.util.external;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * @author LiuQi
 */
@Getter
@Setter
public class CoinMarketCapQuotes implements Serializable {

	private String id;
	
	private String symbol;

	@JSONField(name = "cmc_rank")
	private int rank;
	
	@JSONField(name = "quote")
	private Map<String, CoinMarketCapQuote> quotes;

}
