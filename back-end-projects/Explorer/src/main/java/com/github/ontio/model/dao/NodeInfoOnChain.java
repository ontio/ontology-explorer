package com.github.ontio.model.dao;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@Table(name = "tbl_node_info_on_chain")
public class NodeInfoOnChain {
    @Id
    @Column(name = "node_rank")
    @GeneratedValue(generator = "JDBC")
    private Integer nodeRank;

    private String name;

    @Column(name = "current_stake")
    private Long currentStake;

    private String progress;

    @Column(name = "detail_url")
    private String detailUrl;

    @Column(name = "public_key")
    private String publicKey;

    private String address;

    private Integer status;

    @Column(name = "init_pos")
    private Long initPos;

    @Column(name = "total_pos")
    private Long totalPos;

    @Column(name = "max_authorize")
    private Long maxAuthorize;

    @Column(name = "node_proportion")
    private String nodeProportion;

    @Column(name = "user_proportion")
    private String userProportion;

    @Column(name = "current_stake_percentage")
    private String currentStakePercentage;

    public NodeInfoOnChain() {

    }

    public NodeInfoOnChain(Integer nodeRank, String name, Long currentStake, String progress, String detailUrl,
                           String publicKey, String address, Integer status, Long initPos, Long totalPos,
                           Long maxAuthorize, String nodeProportion, String userProportion,String currentStakePercentage) {
        this.nodeRank = nodeRank;
        this.name = name;
        this.currentStake = currentStake;
        this.progress = progress;
        this.detailUrl = detailUrl;
        this.publicKey = publicKey;
        this.address = address;
        this.status = status;
        this.initPos = initPos;
        this.totalPos = totalPos;
        this.maxAuthorize = maxAuthorize;
        this.nodeProportion = nodeProportion;
        this.userProportion = userProportion;
        this.currentStakePercentage = currentStakePercentage;
    }

    public NodeInfoOnChain(NodeInfoOnChain nodeInfoOnChain) {
        this.nodeRank = nodeInfoOnChain.getNodeRank();
        this.name = nodeInfoOnChain.getName();
        this.currentStake = nodeInfoOnChain.getCurrentStake();
        this.progress = nodeInfoOnChain.getProgress();
        this.detailUrl = nodeInfoOnChain.getDetailUrl();
        this.publicKey = nodeInfoOnChain.getPublicKey();
        this.address = nodeInfoOnChain.getAddress();
        this.status = nodeInfoOnChain.getStatus();
        this.initPos = nodeInfoOnChain.getInitPos();
        this.totalPos = nodeInfoOnChain.getTotalPos();
        this.maxAuthorize = nodeInfoOnChain.getMaxAuthorize();
        this.nodeProportion = nodeInfoOnChain.getNodeProportion();
        this.userProportion = nodeInfoOnChain.getUserProportion();
        this.currentStakePercentage = nodeInfoOnChain.getCurrentStakePercentage();
    }

}