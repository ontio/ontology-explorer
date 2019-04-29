package com.github.ontio.blocksync.model;

import java.io.Serializable;

public class Current implements Serializable {
    private Integer blockHeight;

    private Integer txCount;

    private Integer ontidCount;

    private Integer nonontidTxCount;

    private static final long serialVersionUID = 1L;

    public Integer getBlockHeight() {
        return blockHeight;
    }

    public Current withBlockHeight(Integer blockHeight) {
        this.setBlockHeight(blockHeight);
        return this;
    }

    public void setBlockHeight(Integer blockHeight) {
        this.blockHeight = blockHeight;
    }

    public Integer getTxCount() {
        return txCount;
    }

    public Current withTxCount(Integer txCount) {
        this.setTxCount(txCount);
        return this;
    }

    public void setTxCount(Integer txCount) {
        this.txCount = txCount;
    }

    public Integer getOntidCount() {
        return ontidCount;
    }

    public Current withOntidCount(Integer ontidCount) {
        this.setOntidCount(ontidCount);
        return this;
    }

    public void setOntidCount(Integer ontidCount) {
        this.ontidCount = ontidCount;
    }

    public Integer getNonontidTxCount() {
        return nonontidTxCount;
    }

    public Current withNonontidTxCount(Integer nonontidTxCount) {
        this.setNonontidTxCount(nonontidTxCount);
        return this;
    }

    public void setNonontidTxCount(Integer nonontidTxCount) {
        this.nonontidTxCount = nonontidTxCount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", blockHeight=").append(blockHeight);
        sb.append(", txCount=").append(txCount);
        sb.append(", ontidCount=").append(ontidCount);
        sb.append(", nonontidTxCount=").append(nonontidTxCount);
        sb.append("]");
        return sb.toString();
    }
}