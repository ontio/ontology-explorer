package com.github.ontio.model.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

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
    private String contractHash;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotEmpty
    private String abi;
    @NotEmpty
    private String code;

    private JSONObject contactInfo;
    @NotEmpty
    private String logo;

    private String dappName;

    private String category;

    private String totalSupply;

    private int decimals;

    private String symbol;

    @Pattern(regexp = "neovm|wasmvm")
    private String vmCategory;

    private List<SubmitOep8Dto> tokens;

}
