package com.github.ontio.model.dao;

import javax.persistence.*;

@Table(name = "tbl_node_rank_history")
public class NodeRankHistory {
    @Id
    @Column(name = "public_key")
    @GeneratedValue(generator = "JDBC")
    private String publicKey;

    @Id
    @Column(name = "block_height")
    private Long blockHeight;

    private String address;

    private String name;

    @Column(name = "node_rank")
    private Integer nodeRank;

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
     * @return block_height
     */
    public Long getBlockHeight() {
        return blockHeight;
    }

    /**
     * @param blockHeight
     */
    public void setBlockHeight(Long blockHeight) {
        this.blockHeight = blockHeight;
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
}