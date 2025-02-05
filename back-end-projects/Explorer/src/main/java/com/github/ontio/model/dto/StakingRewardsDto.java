package com.github.ontio.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StakingRewardsDto {
    private String address;
    private String publicKey;
    private String rewards;
    private Integer staked;
    private Integer processing;
    private Integer unstaking;
    private Integer withdrawable;
    private Integer round;
}

