package com.github.ontio.model.dto;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ontio.model.dao.Contract;
import lombok.Data;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/5/8
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Oep8DetailDto extends Contract {

    private JSONObject totalSupply;

    private JSONObject symbol;

    private JSONObject tokenName;

    private JSONObject tokenId;

}
