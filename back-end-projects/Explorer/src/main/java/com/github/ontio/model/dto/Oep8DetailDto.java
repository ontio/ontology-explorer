package com.github.ontio.model.dto;

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

    private Object totalSupply;

    private Object symbol;

    private Object tokenName;

    private Object tokenId;

}
