package com.github.ontio.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ontio.model.dao.Contract;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Orc1155DetailDto extends Contract {

    private Object totalSupply;

    private Object symbol;

    private Object tokenName;

    private Object tokenId;

    private String vmCategory;

}
