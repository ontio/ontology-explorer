package com.github.ontio.blocksync.model;

import java.io.Serializable;

public class Oep5Dragon implements Serializable {
    private Integer id;

    private String contractHash;

    private String assetName;

    private String jsonUrl;

    private static final long serialVersionUID = 1L;

    public Oep5Dragon(Integer id, String contractHash, String assetName, String jsonUrl) {
        this.id = id;
        this.contractHash = contractHash;
        this.assetName = assetName;
        this.jsonUrl = jsonUrl;
    }

    public Integer getId() {
        return id;
    }

    public String getContractHash() {
        return contractHash;
    }

    public String getAssetName() {
        return assetName;
    }

    public String getJsonUrl() {
        return jsonUrl;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", contractHash=").append(contractHash);
        sb.append(", assetName=").append(assetName);
        sb.append(", jsonUrl=").append(jsonUrl);
        sb.append("]");
        return sb.toString();
    }
}