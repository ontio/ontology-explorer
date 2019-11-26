package com.github.ontio.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/11/21
 */
@Data
public class SubmitOep8Dto {
    @NotEmpty
    @Length(max = 255)
    private String tokenId;
    @NotEmpty
    @Length(max = 255)
    private String tokenName;
    @NotEmpty
    @Length(max = 15)
    @Pattern(regexp = "^[1-9]\\d*$")
    private String totalSupply;
    @NotEmpty
    @Length(max = 255)
    private String symbol;
}
