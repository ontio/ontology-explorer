package com.github.ontio.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NodesInfoRespDto {
    public String publicKey;
    public String address;
    public String name;
    public String annualizedRate;
    public Long currentStake;
    public Long totalStake;
    private String progress;
}
