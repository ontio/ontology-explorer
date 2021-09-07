package com.github.ontio.model.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author LiuQi
 */
@Getter
@Setter
public class ContractType implements Serializable {

	public static final ContractType NULL = new ContractType();

	private String contractHash;

	private boolean oep4;

	private boolean oep5;

	private boolean oep8;

	private boolean orc20;

	private boolean orc721;

}
