package com.github.ontio.model.dao;

import javax.persistence.*;

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

    /**
     * @return node_rank
     */
    public Integer getNodeRank() {
        return nodeRank;
    }

    /**
     * @param nodeRank
     */
    public void setNodeRank(Integer nodeRank) {
        this.nodeRank = nodeRank;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * @return current_stake
     */
    public Long getCurrentStake() {
        return currentStake;
    }

    /**
     * @param currentStake
     */
    public void setCurrentStake(Long currentStake) {
        this.currentStake = currentStake;
    }

    /**
     * @return progress
     */
    public String getProgress() {
        return progress;
    }

    /**
     * @param progress
     */
    public void setProgress(String progress) {
        this.progress = progress == null ? null : progress.trim();
    }

    /**
     * @return detail_url
     */
    public String getDetailUrl() {
        return detailUrl;
    }

    /**
     * @param detailUrl
     */
    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl == null ? null : detailUrl.trim();
    }

    /**
     * @return public_key
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * @param publicKey
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey == null ? null : publicKey.trim();
    }

    /**
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * @return status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return init_pos
     */
    public Long getInitPos() {
        return initPos;
    }

    /**
     * @param initPos
     */
    public void setInitPos(Long initPos) {
        this.initPos = initPos;
    }

    /**
     * @return total_pos
     */
    public Long getTotalPos() {
        return totalPos;
    }

    /**
     * @param totalPos
     */
    public void setTotalPos(Long totalPos) {
        this.totalPos = totalPos;
    }

    /**
     * @return max_authorize
     */
    public Long getMaxAuthorize() {
        return maxAuthorize;
    }

    /**
     * @param maxAuthorize
     */
    public void setMaxAuthorize(Long maxAuthorize) {
        this.maxAuthorize = maxAuthorize;
    }

    /**
     * @return node_proportion
     */
    public String getNodeProportion() {
        return nodeProportion;
    }

    /**
     * @param nodeProportion
     */
    public void setNodeProportion(String nodeProportion) {
        this.nodeProportion = nodeProportion == null ? null : nodeProportion.trim();
    }
}