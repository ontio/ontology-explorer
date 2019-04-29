package com.github.ontio.blocksync.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Oep8 extends Oep8Key implements Serializable {
    private String name;

    private BigDecimal totalSupply;

    private String symbol;

    private Date createTime;

    private Integer auditFlag;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public String getName() {
        return name;
    }

    public Oep8 withName(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public BigDecimal getTotalSupply() {
        return totalSupply;
    }

    public Oep8 withTotalSupply(BigDecimal totalSupply) {
        this.setTotalSupply(totalSupply);
        return this;
    }

    public void setTotalSupply(BigDecimal totalSupply) {
        this.totalSupply = totalSupply;
    }

    public String getSymbol() {
        return symbol;
    }

    public Oep8 withSymbol(String symbol) {
        this.setSymbol(symbol);
        return this;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol == null ? null : symbol.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Oep8 withCreateTime(Date createTime) {
        this.setCreateTime(createTime);
        return this;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getAuditFlag() {
        return auditFlag;
    }

    public Oep8 withAuditFlag(Integer auditFlag) {
        this.setAuditFlag(auditFlag);
        return this;
    }

    public void setAuditFlag(Integer auditFlag) {
        this.auditFlag = auditFlag;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public Oep8 withUpdateTime(Date updateTime) {
        this.setUpdateTime(updateTime);
        return this;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", name=").append(name);
        sb.append(", totalSupply=").append(totalSupply);
        sb.append(", symbol=").append(symbol);
        sb.append(", createTime=").append(createTime);
        sb.append(", auditFlag=").append(auditFlag);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}