package com.github.ontio.util.external;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.io.IOException;
import java.util.Map;

/**
 * @author LiuQi
 */
public interface CoinMarketCapApi {

	String ONT_ID = "2566";

	String ONG_ID = "3217";

	@GET("/v1/cryptocurrency/quotes/latest")
	Call<CoinMarketCapResponse<Map<String, CoinMarketCapQuotes>>> getCoinMarketCapQuotes(@Query("id") String id,
                                                                                         @Query("convert") String convert);

	default CoinMarketCapQuotes getQuotes(String token, String fiat) throws IOException {
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
		CoinMarketCapResponse<Map<String, CoinMarketCapQuotes>> response =
				getCoinMarketCapQuotes(id, fiat.toUpperCase()).execute().body();
		if (response == null) {
			throw new IOException("cannot get " + fiat + " price of " + token + " from coinMarketCap");
		}
		CoinMarketCapStatus status = response.getStatus();
		if (!status.successful()) {
			throw new IOException("coinMarketCap api error: " + status.getErrorMessage());
		}
		Map<String, CoinMarketCapQuotes> quotes = response.getData();
		if (quotes.containsKey(id)) {
			return quotes.get(id);
		}
		throw new IOException("cannot get " + fiat + " price of " + token + " from coinMarketCap");
	}

}
