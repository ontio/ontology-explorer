package com.github.ontio.model.dao;

import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tbl_oep5")
public class Oep5 {
    /**
     * 合约hash值
     */
    @Id
    @Column(name = "contract_hash")
    private String contractHash;

    /**
     * OEP5代币名称
     */
    private String name;

    /**
     * OEP5代币总量
     */
    @Column(name = "total_supply")
    private Long totalSupply;

    /**
     * OEP5代币符号
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

    @Builder
    public Oep5(String contractHash, String name, Long totalSupply, String symbol, Date createTime, Boolean auditFlag, Date updateTime) {
        this.contractHash = contractHash;
        this.name = name;
        this.totalSupply = totalSupply;
        this.symbol = symbol;
        this.createTime = createTime;
        this.auditFlag = auditFlag;
        this.updateTime = updateTime;
    }

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
     * 获取OEP5代币名称
     *
     * @return name - OEP5代币名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置OEP5代币名称
     *
     * @param name OEP5代币名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取OEP5代币总量
     *
     * @return total_supply - OEP5代币总量
     */
    public Long getTotalSupply() {
        return totalSupply;
    }

    /**
     * 设置OEP5代币总量
     *
     * @param totalSupply OEP5代币总量
     */
    public void setTotalSupply(Long totalSupply) {
        this.totalSupply = totalSupply;
    }

    /**
     * 获取OEP5代币符号
     *
     * @return symbol - OEP5代币符号
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * 设置OEP5代币符号
     *
     * @param symbol OEP5代币符号
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