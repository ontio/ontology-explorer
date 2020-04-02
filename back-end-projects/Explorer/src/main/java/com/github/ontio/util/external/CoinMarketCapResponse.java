package com.github.ontio.util.external;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author LiuQi
 */
@Getter
@Setter
public class CoinMarketCapResponse<T> implements Serializable {
	
	private T data;
	
	private CoinMarketCapStatus status;
	
}
