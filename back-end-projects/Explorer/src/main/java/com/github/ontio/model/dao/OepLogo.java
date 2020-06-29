package com.github.ontio.model.dao;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.ontio.util.TxDateSerializer;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tbl_oep_logo")
public class OepLogo {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 合约hash
     */
    @Column(name = "contract_hash")
    private String contractHash;

    /**
     * oep5，oep8
     */
    private String type;

    /**
     * 合约token id
     */
    @Column(name = "token_id")
    private String tokenId;

    /**
     * logo链接
     */
    private String logo;

    /**
     * token名称
     */
    private String name;

    @JsonSerialize(using = TxDateSerializer.class)
    @Column(name = "create_time")
    private Date createTime;

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
     * 获取oep5，oep8
     *
     * @return type - oep5，oep8
     */
    public String getType() {
        return type;
    }

    /**
     * 设置oep5，oep8
     *
     * @param type oep5，oep8
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * 获取合约token id
     *
     * @return token_id - 合约token id
     */
    public String getTokenId() {
        return tokenId;
    }

    /**
     * 设置合约token id
     *
     * @param tokenId 合约token id
     */
    public void setTokenId(String tokenId) {
        this.tokenId = tokenId == null ? null : tokenId.trim();
    }

    /**
     * 获取logo链接
     *
     * @return logo - logo链接
     */
    public String getLogo() {
        return logo;
    }

    /**
     * 设置logo链接
     *
     * @param logo logo链接
     */
    public void setLogo(String logo) {
        this.logo = logo == null ? null : logo.trim();
    }

    /**
     * 获取token名称
     *
     * @return name - token名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置token名称
     *
     * @param name token名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}