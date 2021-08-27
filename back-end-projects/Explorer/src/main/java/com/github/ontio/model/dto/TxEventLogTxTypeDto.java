package com.github.ontio.model.dto;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.ontio.model.common.TxTypeEnum;
import com.github.ontio.model.common.VmTypeEnum;
import com.github.ontio.util.TxAmountSerializer;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author LiuQi
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TxEventLogTxTypeDto implements Serializable {

	/**
	 * 交易hash值
	 */
	private String txHash;

	/**
	 * 交易时间戳
	 */
	private Integer txTime;

	/**
	 * 区块高度
	 */
	private Integer blockHeight;

	/**
	 * 交易在区块里的索引
	 */
	private Integer blockIndex;

	/**
	 * 交易落账标识  1：成功 0：失败
	 */
	private Integer confirmFlag;

	/**
	 * 交易手续费
	 */
	@JsonSerialize(using = TxAmountSerializer.class)
	private BigDecimal fee;

	/**
	 * 交易类型
	 */
	private TxTypeEnum txType;

	/**
	 * 交易使用虚拟机类型
	 */
	private VmTypeEnum vmType;


	private JSONObject detail;

	
}
