package com.github.ontio.util.external;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author LiuQi
 */
@Getter
@Setter
public class CoinMarketCapStatus implements Serializable {

	@JSONField(name = "error_code")
	private int errorCode;

	@JSONField(name = "error_message")
	private String errorMessage;

	public boolean successful() {
		return errorCode == 0;
	}

}
