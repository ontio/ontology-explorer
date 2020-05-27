package com.github.ontio.model.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/11/21
 */
@Data
public class SubmitContractDto {

    @NotEmpty
    @Length(max = 255)
    private String contractHash;
    @NotEmpty
    @Length(max = 255)
    private String name;
    @NotEmpty
    @Length(max = 1000)
    private String description;
    @NotEmpty
    private String abi;
    @NotEmpty
    private String code;

    private JSONObject contactInfo;
    @NotEmpty
    private String logo;

    @Length(max = 255)
    private String dappName;
    @Length(max = 255)
    private String category;

    @Length(max = 15)
    @Pattern(regexp = "^[1-9]\\d*$")
    private String totalSupply;

    @Max(100000000000L)
    private int decimals;

    @Length(max = 255)
    private String symbol;

    @Pattern(regexp = "neovm|wasmvm")
    private String vmCategory;

    @Valid
    private List<SubmitOep8Dto> tokens;

}
