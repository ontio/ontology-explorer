package com.github.ontio.util.external;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.math.BigDecimal;

/**
 * @author LiuQi
 */
public interface CoinMarketCapApi {

	String ONT_ID = "2566";

	String ONG_ID = "3217";

	@GET("/v1/tools/price-conversion")
	Call<CoinMarketCapResponse<CoinMarketCapPrice>> convertPrice(@Query("id") String id, @Query("amount") BigDecimal amount,
			@Query("convert") String convert);

	default Call<CoinMarketCapResponse<CoinMarketCapPrice>> getPrice(String token, String fiat) {
		String id;
		switch (token.toUpperCase()) {
			case "ONT":
				id = ONT_ID;
				break;
			case "ONG":
				id = ONG_ID;
				break;
			default:
				throw new IllegalArgumentException("unsupported token: " + token);
		}
		return convertPrice(id, BigDecimal.ONE, fiat.toUpperCase());
	}

}
