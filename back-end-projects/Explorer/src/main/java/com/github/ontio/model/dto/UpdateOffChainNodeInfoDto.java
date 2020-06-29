package com.github.ontio.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class UpdateOffChainNodeInfoDto {

    private String nodeInfo;

    private String publicKey;

    private String signature;

    @Builder
    public UpdateOffChainNodeInfoDto(String nodeInfo, String publicKey, String signature) {
        this.nodeInfo = nodeInfo;
        this.publicKey = publicKey;
        this.signature = signature;
    }
}
