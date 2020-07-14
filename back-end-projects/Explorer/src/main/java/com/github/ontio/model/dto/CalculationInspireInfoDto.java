package com.github.ontio.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalculationInspireInfoDto {

    private Integer theLastConsensusRank;

    private Long theLastConsensusStake;

    private Long the_49thNodeStake;

}
