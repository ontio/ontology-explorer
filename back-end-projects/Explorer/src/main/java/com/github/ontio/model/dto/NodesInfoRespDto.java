package com.github.ontio.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NodesInfoRespDto {
    public String publicKey;
    public String address;
    public String name;
    public String annualizedRate;
    public Long currentStake;
    public Long totalStake;
    private String progress;
    private Integer feeSharingRatio;
    private Integer ontologyHarbinger;
    private Integer risky;
    private Integer badActor;
}
