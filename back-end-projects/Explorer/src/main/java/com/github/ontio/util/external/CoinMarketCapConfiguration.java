package com.github.ontio.util.external;

import com.alibaba.fastjson.support.retrofit.Retrofit2ConverterFactory;
import com.github.ontio.config.ParamsConfig;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;

/**
 * @author LiuQi
 */
@Configuration
public class CoinMarketCapConfiguration {

	@Bean
	Interceptor coinMarketCapApiInterceptor(ParamsConfig config) {
		return chain -> {
			Request request = chain.request();
			request = request.newBuilder()
					.header("X-CMC_PRO_API_KEY", config.getCoinMarketCapApiKey())
					.header("Accept", "application/json")
					.build();
			return chain.proceed(request);
		};
	}

	@Bean
	CoinMarketCapApi coinMarketCapApi(ParamsConfig config, Interceptor coinMarketCapApiInterceptor) {
		OkHttpClient.Builder builder = new OkHttpClient.Builder()
				.addInterceptor(coinMarketCapApiInterceptor);
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(config.getCoinMarketCapApiHost())
				.client(builder.build())
				.addConverterFactory(new Retrofit2ConverterFactory())
				.build();
		return retrofit.create(CoinMarketCapApi.class);
	}

}
