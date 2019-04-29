package com.github.ontio.blocksync.model;

import java.io.Serializable;

public class Oep8Key implements Serializable {
    private String contractHash;

    private String tokenId;

    private static final long serialVersionUID = 1L;

    public Oep8Key(String contractHash, String tokenId) {
        this.contractHash = contractHash;
        this.tokenId = tokenId;
    }

    public String getContractHash() {
        return contractHash;
    }

    public String getTokenId() {
        return tokenId;
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