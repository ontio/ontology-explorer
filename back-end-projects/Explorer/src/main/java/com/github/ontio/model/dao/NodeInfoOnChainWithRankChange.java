package com.github.ontio.model.dao;

import com.github.ontio.model.dto.NodeInfoOnChainDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NodeInfoOnChainWithRankChange extends NodeInfoOnChain {

    private Integer rankChange;

    public NodeInfoOnChainWithRankChange() {
    }

    public NodeInfoOnChainWithRankChange(NodeInfoOnChainDto nodeInfoOnChain, int rankChange) {
        super(nodeInfoOnChain);
        this.rankChange = rankChange;
    }

    public NodeInfoOnChainWithRankChange(NodeInfoOnChainDto nodeInfoOnChain, NodeRankChange nodeRankChange) {
        super(nodeInfoOnChain);
        if (!nodeInfoOnChain.getPublicKey().equals(nodeRankChange.getPublicKey())) {
            this.rankChange = 0;
        } else {
            this.rankChange = nodeRankChange.getRankChange();
        }
    }

}
