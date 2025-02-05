package com.github.ontio.model.dto;

import lombok.Data;

@Data
public class StakingInfoDto {
    private String address;
    private String publicKey;
    private Integer processing;
    private Integer unstaking;
    private Integer staked;
    private Integer withdrawable;
    private String rewards;
    private Integer round;
}

