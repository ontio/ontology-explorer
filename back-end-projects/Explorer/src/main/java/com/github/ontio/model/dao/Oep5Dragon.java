package com.github.ontio.model.dao;

import javax.persistence.*;

@Table(name = "tbl_oep5_dragon")
public class Oep5Dragon {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 合约hash
     */
    @Column(name = "contract_hash")
    private String contractHash;

    /**
     * 该dragon的名称
     */
    @Column(name = "asset_name")
    private String assetName;

    /**
     * 该dragon的基本信息url
     */
    @Column(name = "json_url")
    private String jsonUrl;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取合约hash
     *
     * @return contract_hash - 合约hash
     */
    public String getContractHash() {
        return contractHash;
    }

    /**
     * 设置合约hash
     *
     * @param contractHash 合约hash
     */
    public void setContractHash(String contractHash) {
        this.contractHash = contractHash == null ? null : contractHash.trim();
    }

    /**
     * 获取该dragon的名称
     *
     * @return asset_name - 该dragon的名称
     */
    public String getAssetName() {
        return assetName;
    }

    /**
     * 设置该dragon的名称
     *
     * @param assetName 该dragon的名称
     */
    public void setAssetName(String assetName) {
        this.assetName = assetName == null ? null : assetName.trim();
    }

    /**
     * 获取该dragon的基本信息url
     *
     * @return json_url - 该dragon的基本信息url
     */
    public String getJsonUrl() {
        return jsonUrl;
    }

    /**
     * 设置该dragon的基本信息url
     *
     * @param jsonUrl 该dragon的基本信息url
     */
    public void setJsonUrl(String jsonUrl) {
        this.jsonUrl = jsonUrl == null ? null : jsonUrl.trim();
    }
}