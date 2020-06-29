package com.github.ontio.model;

import java.math.BigDecimal;
import java.util.Date;

public class Oep4 extends Oep4Key {
    private BigDecimal totalsupply;

    private String symbol;

    private BigDecimal decimals;

    private String description;

    private String contactinfo;

    private Date createtime;

    private Integer auditflag;

    private Date updatetime;

    public BigDecimal getTotalsupply() {
        return totalsupply;
    }

    public void setTotalsupply(BigDecimal totalsupply) {
        this.totalsupply = totalsupply;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol == null ? null : symbol.trim();
    }

    public BigDecimal getDecimals() {
        return decimals;
    }

    public void setDecimals(BigDecimal decimals) {
        this.decimals = decimals;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getContactinfo() {
        return contactinfo;
    }

    public void setContactinfo(String contactinfo) {
        this.contactinfo = contactinfo == null ? null : contactinfo.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getAuditflag() {
        return auditflag;
    }

    public void setAuditflag(Integer auditflag) {
        this.auditflag = auditflag;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}