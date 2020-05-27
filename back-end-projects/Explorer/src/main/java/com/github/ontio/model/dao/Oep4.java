package com.github.ontio.model.dao;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tbl_oep4")
public class Oep4 {
    /**
     * 合约hash值
     */
    @Id
    @Column(name = "contract_hash")
    private String contractHash;

    /**
     * OEP4代币名称
     */
    private String name;

    /**
     * OEP4代币总量
     */
    @Column(name = "total_supply")
    private Long totalSupply;

    /**
     * OEP4代币符号
     */
    private String symbol;

    /**
     * OEP4代币精度
     */
    private Integer decimals;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 审核标识，1：审核通过 0：未审核
     */
    @Column(name = "audit_flag")
    private Boolean auditFlag;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 合约vm类型，分为neovm，wasmvm
     */
    @Column(name = "vm_category")
    private String vmCategory;


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
     * 获取OEP4代币名称
     *
     * @return name - OEP4代币名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置OEP4代币名称
     *
     * @param name OEP4代币名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取OEP4代币总量
     *
     * @return total_supply - OEP4代币总量
     */
    public Long getTotalSupply() {
        return totalSupply;
    }

    /**
     * 设置OEP4代币总量
     *
     * @param totalSupply OEP4代币总量
     */
    public void setTotalSupply(Long totalSupply) {
        this.totalSupply = totalSupply;
    }

    /**
     * 获取OEP4代币符号
     *
     * @return symbol - OEP4代币符号
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * 设置OEP4代币符号
     *
     * @param symbol OEP4代币符号
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol == null ? null : symbol.trim();
    }

    /**
     * 获取OEP4代币精度
     *
     * @return decimals - OEP4代币精度
     */
    public Integer getDecimals() {
        return decimals;
    }

    /**
     * 设置OEP4代币精度
     *
     * @param decimals OEP4代币精度
     */
    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
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
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取合约vm类型，分为neovm，wasmvm
     *
     * @return vm_category - 合约vm类型，分为neovm，wasmvm
     */
    public String getVmCategory() {
        return vmCategory;
    }

    /**
     * 设置合约vm类型，分为neovm，wasmvm
     *
     * @param vmCategory 合约vm类型，分为neovm，wasmvm
     */
    public void setVmCategory(String vmCategory) {
        this.vmCategory = vmCategory == null ? null : vmCategory.trim();
    }
}