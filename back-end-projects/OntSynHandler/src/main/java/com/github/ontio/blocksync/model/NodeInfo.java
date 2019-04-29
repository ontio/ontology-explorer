package com.github.ontio.blocksync.model;

import java.io.Serializable;

public class NodeInfo implements Serializable {
    private Long id;

    private String name;

    private Integer nodeRank;

    private Long currentStake;

    private String progress;

    private String detailUrl;

    private Integer nodeIndex;

    private String publicKey;

    private String address;

    private Integer status;

    private Long initPos;

    private Long totalPos;

    private Long maxAuthorize;

    private String nodeProportion;

    private static final long serialVersionUID = 1L;

    public NodeInfo(Long id, String name, Integer nodeRank, Long currentStake, String progress, String detailUrl, Integer nodeIndex, String publicKey, String address, Integer status, Long initPos, Long totalPos, Long maxAuthorize, String nodeProportion) {
        this.id = id;
        this.name = name;
        this.nodeRank = nodeRank;
        this.currentStake = currentStake;
        this.progress = progress;
        this.detailUrl = detailUrl;
        this.nodeIndex = nodeIndex;
        this.publicKey = publicKey;
        this.address = address;
        this.status = status;
        this.initPos = initPos;
        this.totalPos = totalPos;
        this.maxAuthorize = maxAuthorize;
        this.nodeProportion = nodeProportion;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getNodeRank() {
        return nodeRank;
    }

    public Long getCurrentStake() {
        return currentStake;
    }

    public String getProgress() {
        return progress;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public Integer getNodeIndex() {
        return nodeIndex;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getAddress() {
        return address;
    }

    public Integer getStatus() {
        return status;
    }

    public Long getInitPos() {
        return initPos;
    }

    public Long getTotalPos() {
        return totalPos;
    }

    public Long getMaxAuthorize() {
        return maxAuthorize;
    }

    public String getNodeProportion() {
        return nodeProportion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", nodeRank=").append(nodeRank);
        sb.append(", currentStake=").append(currentStake);
        sb.append(", progress=").append(progress);
        sb.append(", detailUrl=").append(detailUrl);
        sb.append(", nodeIndex=").append(nodeIndex);
        sb.append(", publicKey=").append(publicKey);
        sb.append(", address=").append(address);
        sb.append(", status=").append(status);
        sb.append(", initPos=").append(initPos);
        sb.append(", totalPos=").append(totalPos);
        sb.append(", maxAuthorize=").append(maxAuthorize);
        sb.append(", nodeProportion=").append(nodeProportion);
        sb.append("]");
        return sb.toString();
    }
}