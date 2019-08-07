package com.github.ontio.model.dao;

import lombok.*;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NodeInfoOnChainWithBonus extends NodeInfoOnChain {

    private Long unixTime;

    private Double bonus;

    public NodeInfoOnChainWithBonus() {
    }

    public NodeInfoOnChainWithBonus(NodeInfoOnChain nodeInfoOnChain) {
        super(nodeInfoOnChain);
        this.unixTime = 0L;
        this.bonus = 0D;
    }

    public NodeInfoOnChainWithBonus(NodeInfoOnChain nodeInfoOnChain, NodeBonus nodeBonus) {
        super(nodeInfoOnChain);
        if (!nodeInfoOnChain.getPublicKey().equals(nodeBonus.getPublicKey())) {
            return;
        }
        this.unixTime = nodeBonus.getUnixTime();
        this.bonus = nodeBonus.getBonus();
    }

}
