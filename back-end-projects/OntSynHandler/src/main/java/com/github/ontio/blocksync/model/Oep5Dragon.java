package com.github.ontio.blocksync.model;

import java.io.Serializable;

public class Oep5Dragon implements Serializable {
    private Integer id;

    private String contractHash;

    private String assetName;

    private String jsonUrl;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public Oep5Dragon withId(Integer id) {
        this.setId(id);
        return this;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContractHash() {
        return contractHash;
    }

    public Oep5Dragon withContractHash(String contractHash) {
        this.setContractHash(contractHash);
        return this;
    }

    public void setContractHash(String contractHash) {
        this.contractHash = contractHash == null ? null : contractHash.trim();
    }

    public String getAssetName() {
        return assetName;
    }

    public Oep5Dragon withAssetName(String assetName) {
        this.setAssetName(assetName);
        return this;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName == null ? null : assetName.trim();
    }

    public String getJsonUrl() {
        return jsonUrl;
    }

    public Oep5Dragon withJsonUrl(String jsonUrl) {
        this.setJsonUrl(jsonUrl);
        return this;
    }

    public void setJsonUrl(String jsonUrl) {
        this.jsonUrl = jsonUrl == null ? null : jsonUrl.trim();
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