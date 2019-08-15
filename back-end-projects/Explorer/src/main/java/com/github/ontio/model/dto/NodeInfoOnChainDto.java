package com.github.ontio.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ontio.model.dao.NodeInfoOnChain;
import lombok.Builder;

import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "tbl_node_info_off_chain")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NodeInfoOnChainDto extends NodeInfoOnChain {

    public NodeInfoOnChainDto(Integer nodeRank, String name, Long currentStake, String progress, String detailUrl,
                              String publicKey, String address, Integer status, Long initPos, Long totalPos,
                              Long maxAuthorize, String nodeProportion, String currentStakePercentage) {
        super(nodeRank, name, currentStake, progress, detailUrl, publicKey, address, status, initPos, totalPos,
                maxAuthorize, nodeProportion, currentStakePercentage);
    }

}