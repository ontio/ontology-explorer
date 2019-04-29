package com.github.ontio.blocksync.model;

import java.io.Serializable;

public class Oep5TxDetailKey implements Serializable {
    private String txHash;

    private Integer txIndex;

    private static final long serialVersionUID = 1L;

    public String getTxHash() {
        return txHash;
    }

    public Oep5TxDetailKey withTxHash(String txHash) {
        this.setTxHash(txHash);
        return this;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash == null ? null : txHash.trim();
    }

    public Integer getTxIndex() {
        return txIndex;
    }

    public Oep5TxDetailKey withTxIndex(Integer txIndex) {
        this.setTxIndex(txIndex);
        return this;
    }

    public void setTxIndex(Integer txIndex) {
        this.txIndex = txIndex;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", txHash=").append(txHash);
        sb.append(", txIndex=").append(txIndex);
        sb.append("]");
        return sb.toString();
    }
}