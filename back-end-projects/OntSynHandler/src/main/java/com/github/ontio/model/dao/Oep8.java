package com.github.ontio.model.dao;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_oep8")
public class Oep8 {
    /**
     * 合约hash值
     */
    @Id
    @Column(name = "contract_hash")
    private String contractHash;

    /**
     * OEP8的token id
     */
    @Id
    @Column(name = "token_id")
    private String tokenId;

    /**
     * OEP8代币名称
     */
    private String name;

    /**
     * OEP8代币总量
     */
    @Column(name = "total_supply")
    private BigDecimal totalSupply;

    /**
     * OEP8代币符号
     */
    private String symbol;

    /**
     * 创建时间，yyyy-MM-dd
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 审核标识，1：审核通过 0：未审核
     */
    @Column(name = "audit_flag")
    private Boolean auditFlag;

    /**
     * 更新时间，yyyy-MM-dd
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 获取合约hash值
     *
     * @return contract_hash - 合约hash值
     */
    public String getContractHash() {
        return contractHash;
    }

    /**
     * 设置合约hash值
     *
     * @param contractHash 合约hash值
     */
    public void setContractHash(String contractHash) {
        this.contractHash = contractHash == null ? null : contractHash.trim();
    }

    /**
     * 获取OEP8的token id
     *
     * @return token_id - OEP8的token id
     */
    public String getTokenId() {
        return tokenId;
    }

    /**
     * 设置OEP8的token id
     *
     * @param tokenId OEP8的token id
     */
    public void setTokenId(String tokenId) {
        this.tokenId = tokenId == null ? null : tokenId.trim();
    }

    /**
     * 获取OEP8代币名称
     *
     * @return name - OEP8代币名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置OEP8代币名称
     *
     * @param name OEP8代币名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取OEP8代币总量
     *
     * @return total_supply - OEP8代币总量
     */
    public BigDecimal getTotalSupply() {
        return totalSupply;
    }

    /**
     * 设置OEP8代币总量
     *
     * @param totalSupply OEP8代币总量
     */
    public void setTotalSupply(BigDecimal totalSupply) {
        this.totalSupply = totalSupply;
    }

    /**
     * 获取OEP8代币符号
     *
     * @return symbol - OEP8代币符号
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * 设置OEP8代币符号
     *
     * @param symbol OEP8代币符号
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol == null ? null : symbol.trim();
    }

    /**
     * 获取创建时间，yyyy-MM-dd
     *
     * @return create_time - 创建时间，yyyy-MM-dd
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间，yyyy-MM-dd
     *
     * @param createTime 创建时间，yyyy-MM-dd
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取审核标识，1：审核通过 0：未审核
     *
     * @return audit_flag - 审核标识，1：审核通过 0：未审核
     */
    public Boolean getAuditFlag() {
        return auditFlag;
    }

    /**
     * 设置审核标识，1：审核通过 0：未审核
     *
     * @param auditFlag 审核标识，1：审核通过 0：未审核
     */
    public void setAuditFlag(Boolean auditFlag) {
        this.auditFlag = auditFlag;
    }

    /**
     * 获取更新时间，yyyy-MM-dd
     *
     * @return update_time - 更新时间，yyyy-MM-dd
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间，yyyy-MM-dd
     *
     * @param updateTime 更新时间，yyyy-MM-dd
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}