package com.github.ontio.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.ontio.util.TxAmountSerializer;
import com.github.ontio.util.external.CoinMarketCapQuotes;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LiuQi
 */
@Getter
@Setter
public class TokenPriceDto implements Serializable {

	private String token;

	private int rank;

	private Map<String, Price> prices;

	@Getter
	@RequiredArgsConstructor
	public static class Price implements Serializable {
		@JsonSerialize(using = TxAmountSerializer.class)
		private final BigDecimal price;
		@JsonSerialize(using = TxAmountSerializer.class)
		private final BigDecimal percentage;
	}

	public static TokenPriceDto from(String token, CoinMarketCapQuotes quotes) {
		TokenPriceDto dto = new TokenPriceDto();
		dto.setToken(token);
		dto.setRank(quotes.getRank());
		Map<String, Price> prices = new HashMap<>();
		quotes.getQuotes().forEach((fiat, quote) -> {
			Price price = new Price(quote.getPrice(), quote.getPercentageOneDay());
			prices.put(fiat, price);
		});
		dto.setPrices(prices);
		return dto;
	}

}
