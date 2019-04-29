package com.github.ontio.blocksync.model;

import java.io.Serializable;

public class Current implements Serializable {
    private Integer blockHeight;

    private Integer txCount;

    private Integer ontidCount;

    private Integer nonontidTxCount;

    private static final long serialVersionUID = 1L;

    public Current(Integer blockHeight, Integer txCount, Integer ontidCount, Integer nonontidTxCount) {
        this.blockHeight = blockHeight;
        this.txCount = txCount;
        this.ontidCount = ontidCount;
        this.nonontidTxCount = nonontidTxCount;
    }

    public Integer getBlockHeight() {
        return blockHeight;
    }

    public Integer getTxCount() {
        return txCount;
    }

    public Integer getOntidCount() {
        return ontidCount;
    }

    public Integer getNonontidTxCount() {
        return nonontidTxCount;
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