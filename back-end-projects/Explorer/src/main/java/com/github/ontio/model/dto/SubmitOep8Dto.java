package com.github.ontio.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/11/21
 */
@Data
public class SubmitOep8Dto {
    @NotEmpty
    private String tokenId;
    @NotEmpty
    private String tokenName;
    @NotEmpty
    private String totalSupply;
    @NotEmpty
    private String symbol;
}
