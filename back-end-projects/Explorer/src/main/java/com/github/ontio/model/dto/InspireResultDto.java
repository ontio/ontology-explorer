package com.github.ontio.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InspireResultDto {

    private String nodeReleasedOngIncentive;

    private String nodeReleasedOngIncentiveRate;

    private String userReleasedOngIncentive;

    private String userReleasedOngIncentiveRate;

    private String nodeGasFeeIncentive;

    private String nodeGasFeeIncentiveRate;

    private String userGasFeeIncentive;

    private String userGasFeeIncentiveRate;

    private String nodeFoundationBonusIncentive;

    private String nodeFoundationBonusIncentiveRate;

    private String userFoundationBonusIncentive;

    private String userFoundationBonusIncentiveRate;
}
