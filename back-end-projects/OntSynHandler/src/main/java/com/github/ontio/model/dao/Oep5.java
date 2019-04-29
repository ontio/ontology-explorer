package com.github.ontio.model.dao;

import java.util.Date;
import javax.persistence.*;

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
    private Date createtime;

    /**
     * 审核标识，1：审核通过 0：未审核
     */
    private Boolean auditflag;

    /**
     * 更新时间，yyyy-MM-dd
     */
    private Date updatetime;

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
     * @return createtime - 创建时间，yyyy-MM-dd
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 设置创建时间，yyyy-MM-dd
     *
     * @param createtime 创建时间，yyyy-MM-dd
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 获取审核标识，1：审核通过 0：未审核
     *
     * @return auditflag - 审核标识，1：审核通过 0：未审核
     */
    public Boolean getAuditflag() {
        return auditflag;
    }

    /**
     * 设置审核标识，1：审核通过 0：未审核
     *
     * @param auditflag 审核标识，1：审核通过 0：未审核
     */
    public void setAuditflag(Boolean auditflag) {
        this.auditflag = auditflag;
    }

    /**
     * 获取更新时间，yyyy-MM-dd
     *
     * @return updatetime - 更新时间，yyyy-MM-dd
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 设置更新时间，yyyy-MM-dd
     *
     * @param updatetime 更新时间，yyyy-MM-dd
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}