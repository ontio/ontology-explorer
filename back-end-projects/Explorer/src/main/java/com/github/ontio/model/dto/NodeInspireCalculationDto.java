package com.github.ontio.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NodeInspireCalculationDto {

    private Long initPos;

    private String nodeProportion;

    private Integer nodeType;
}
