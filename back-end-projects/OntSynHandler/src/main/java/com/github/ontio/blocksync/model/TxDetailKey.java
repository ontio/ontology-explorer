package com.github.ontio.blocksync.model;

import java.io.Serializable;

public class TxDetailKey implements Serializable {
    private String txHash;

    private Integer txIndex;

    private static final long serialVersionUID = 1L;

    public TxDetailKey(String txHash, Integer txIndex) {
        this.txHash = txHash;
        this.txIndex = txIndex;
    }

    public String getTxHash() {
        return txHash;
    }

    public Integer getTxIndex() {
        return txIndex;
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