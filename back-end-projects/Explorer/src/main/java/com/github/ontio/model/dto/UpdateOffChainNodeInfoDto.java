package com.github.ontio.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
