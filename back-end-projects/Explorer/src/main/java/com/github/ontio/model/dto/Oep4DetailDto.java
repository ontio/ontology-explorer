package com.github.ontio.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ontio.model.dao.Contract;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/5/8
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Oep4DetailDto extends Contract {

    private BigDecimal totalSupply;

    private String symbol;

    private Integer decimals;

    private String name;

}
