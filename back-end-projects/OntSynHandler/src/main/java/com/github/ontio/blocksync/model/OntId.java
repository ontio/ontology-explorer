package com.github.ontio.blocksync.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class OntId implements Serializable {
    private String txHash;

    private Integer txType;

    private String ontid;

    private Integer txTime;

    private Integer blockHeight;

    private String description;

    private BigDecimal fee;

    private static final long serialVersionUID = 1L;

    public String getTxHash() {
        return txHash;
    }

    public OntId withTxHash(String txHash) {
        this.setTxHash(txHash);
        return this;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash == null ? null : txHash.trim();
    }

    public Integer getTxType() {
        return txType;
    }

    public OntId withTxType(Integer txType) {
        this.setTxType(txType);
        return this;
    }

    public void setTxType(Integer txType) {
        this.txType = txType;
    }

    public String getOntid() {
        return ontid;
    }

    public OntId withOntid(String ontid) {
        this.setOntid(ontid);
        return this;
    }

    public void setOntid(String ontid) {
        this.ontid = ontid == null ? null : ontid.trim();
    }

    public Integer getTxTime() {
        return txTime;
    }

    public OntId withTxTime(Integer txTime) {
        this.setTxTime(txTime);
        return this;
    }

    public void setTxTime(Integer txTime) {
        this.txTime = txTime;
    }

    public Integer getBlockHeight() {
        return blockHeight;
    }

    public OntId withBlockHeight(Integer blockHeight) {
        this.setBlockHeight(blockHeight);
        return this;
    }

    public void setBlockHeight(Integer blockHeight) {
        this.blockHeight = blockHeight;
    }

    public String getDescription() {
        return description;
    }

    public OntId withDescription(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public BigDecimal getFee() {
        return fee;
    }

    public OntId withFee(BigDecimal fee) {
        this.setFee(fee);
        return this;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", txHash=").append(txHash);
        sb.append(", txType=").append(txType);
        sb.append(", ontid=").append(ontid);
        sb.append(", txTime=").append(txTime);
        sb.append(", blockHeight=").append(blockHeight);
        sb.append(", description=").append(description);
        sb.append(", fee=").append(fee);
        sb.append("]");
        return sb.toString();
    }
}