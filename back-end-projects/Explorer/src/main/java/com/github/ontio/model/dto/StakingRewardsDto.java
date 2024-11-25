package com.github.ontio.model.dto;

import lombok.Data;

@Data
public class StakingRewardsDto {
    private String address;
    private String publicKey;
    private String rewards;
    private int round;
}

