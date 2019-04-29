package com.github.ontio.blocksync.model;

import java.io.Serializable;
import java.util.Date;

public class Oep5 implements Serializable {
    private String contractHash;

    private String name;

    private Long totalSupply;

    private String symbol;

    private Date createTime;

    private Integer auditFlag;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Oep5(String contractHash, String name, Long totalSupply, String symbol, Date createTime, Integer auditFlag, Date updateTime) {
        this.contractHash = contractHash;
        this.name = name;
        this.totalSupply = totalSupply;
        this.symbol = symbol;
        this.createTime = createTime;
        this.auditFlag = auditFlag;
        this.updateTime = updateTime;
    }

    public String getContractHash() {
        return contractHash;
    }

    public String getName() {
        return name;
    }

    public Long getTotalSupply() {
        return totalSupply;
    }

    public String getSymbol() {
        return symbol;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Integer getAuditFlag() {
        return auditFlag;
    }

    public Date getUpdateTime() {
        return updateTime;
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
        sb.append(", createTime=").append(createTime);
        sb.append(", auditFlag=").append(auditFlag);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}