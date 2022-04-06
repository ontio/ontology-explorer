package com.github.ontio.model.dto;

import lombok.Data;


@Data
public class ContractInfoDto {

    private String contractHash;

    private String name;

    private String type;

    private String logo;

    private String symbol;
}
