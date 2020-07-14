package com.github.ontio.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InspireResultDto {

    private Long nodeReleasedOngIncentive;

    private String nodeReleasedOngIncentiveRate;

    private Long userReleasedOngIncentive;

    private String userReleasedOngIncentiveRate;

    private Long nodeGasFeeIncentive;

    private String nodeGasFeeIncentiveRate;

    private Long userGasFeeIncentive;

    private String userGasFeeIncentiveRate;

    private Long nodeFoundationBonusIncentive;

    private String nodeFoundationBonusIncentiveRate;

    private Long userFoundationBonusIncentive;

    private String userFoundationBonusIncentiveRate;
}
