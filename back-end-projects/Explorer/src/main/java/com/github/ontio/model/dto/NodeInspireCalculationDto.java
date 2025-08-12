package com.github.ontio.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NodeInspireCalculationDto {

    private String publicKey;

    private Long initPos;

    private String initPosNodeProportion;

    private Long totalPos;

    private String totalPosNodeProportion;

    // Deprecated
    private String nodeProportion;
}
