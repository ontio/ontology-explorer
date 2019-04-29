package com.github.ontio.blocksync.model;

import java.io.Serializable;

public class Oep8Key implements Serializable {
    private String contractHash;

    private String tokenId;

    private static final long serialVersionUID = 1L;

    public String getContractHash() {
        return contractHash;
    }

    public Oep8Key withContractHash(String contractHash) {
        this.setContractHash(contractHash);
        return this;
    }

    public void setContractHash(String contractHash) {
        this.contractHash = contractHash == null ? null : contractHash.trim();
    }

    public String getTokenId() {
        return tokenId;
    }

    public Oep8Key withTokenId(String tokenId) {
        this.setTokenId(tokenId);
        return this;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId == null ? null : tokenId.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", contractHash=").append(contractHash);
        sb.append(", tokenId=").append(tokenId);
        sb.append("]");
        return sb.toString();
    }
}