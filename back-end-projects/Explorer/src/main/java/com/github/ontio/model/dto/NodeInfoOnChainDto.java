package com.github.ontio.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ontio.model.dao.NodeInfoOnChain;
import lombok.NoArgsConstructor;

import javax.persistence.Table;

@Table(name = "tbl_node_info_on_chain")
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class NodeInfoOnChainDto extends NodeInfoOnChain {

    //@Builder
    public NodeInfoOnChainDto(Integer nodeRank, String name, Long currentStake, String progress, String detailUrl,
                              String publicKey, String address, Integer status, Long initPos, Long totalPos,
                              Long maxAuthorize, String nodeProportion, String userProportion, String currentStakePercentage) {
        super(nodeRank, name, currentStake, progress, detailUrl, publicKey, address, status, initPos, totalPos,
                maxAuthorize, nodeProportion, userProportion, currentStakePercentage);
    }

}