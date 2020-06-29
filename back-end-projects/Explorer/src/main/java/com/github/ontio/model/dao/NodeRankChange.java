package com.github.ontio.model.dao;

import javax.persistence.*;

@Table(name = "tbl_node_rank_change")
public class NodeRankChange {
    @Id
    @Column(name = "public_key")
    @GeneratedValue(generator = "JDBC")
    private String publicKey;

    private String address;

    private String name;

    @Column(name = "rank_change")
    private Integer rankChange;

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
     * @return rank_change
     */
    public Integer getRankChange() {
        return rankChange;
    }

    /**
     * @param rankChange
     */
    public void setRankChange(Integer rankChange) {
        this.rankChange = rankChange;
    }
}