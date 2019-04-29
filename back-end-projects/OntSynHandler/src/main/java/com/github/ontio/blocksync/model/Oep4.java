package com.github.ontio.blocksync.model;

import java.io.Serializable;
import java.util.Date;

public class Oep4 implements Serializable {
    private String contractHash;

    private String name;

    private String totalSupply;

    private String symbol;

    private Integer decimals;

    private Date createTime;

    private Boolean auditFlag;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public String getContractHash() {
        return contractHash;
    }

    public Oep4 withContractHash(String contractHash) {
        this.setContractHash(contractHash);
        return this;
    }

    public void setContractHash(String contractHash) {
        this.contractHash = contractHash == null ? null : contractHash.trim();
    }

    public String getName() {
        return name;
    }

    public Oep4 withName(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getTotalSupply() {
        return totalSupply;
    }

    public Oep4 withTotalSupply(String totalSupply) {
        this.setTotalSupply(totalSupply);
        return this;
    }

    public void setTotalSupply(String totalSupply) {
        this.totalSupply = totalSupply == null ? null : totalSupply.trim();
    }

    public String getSymbol() {
        return symbol;
    }

    public Oep4 withSymbol(String symbol) {
        this.setSymbol(symbol);
        return this;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol == null ? null : symbol.trim();
    }

    public Integer getDecimals() {
        return decimals;
    }

    public Oep4 withDecimals(Integer decimals) {
        this.setDecimals(decimals);
        return this;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Oep4 withCreateTime(Date createTime) {
        this.setCreateTime(createTime);
        return this;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getAuditFlag() {
        return auditFlag;
    }

    public Oep4 withAuditFlag(Boolean auditFlag) {
        this.setAuditFlag(auditFlag);
        return this;
    }

    public void setAuditFlag(Boolean auditFlag) {
        this.auditFlag = auditFlag;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public Oep4 withUpdateTime(Date updateTime) {
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
        sb.append(", contractHash=").append(contractHash);
        sb.append(", name=").append(name);
        sb.append(", totalSupply=").append(totalSupply);
        sb.append(", symbol=").append(symbol);
        sb.append(", decimals=").append(decimals);
        sb.append(", createTime=").append(createTime);
        sb.append(", auditFlag=").append(auditFlag);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}