package com.github.ontio.model;

import java.math.BigDecimal;

public class DailySummary {
    private Integer time;

    private Integer blockcount;

    private Integer txncount;

    private Integer ontidactivecount;

    private Integer ontidnewcount;

    private BigDecimal ontcount;

    private BigDecimal ongcount;

    private Integer activeaddress;

    private Integer newaddress;

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getBlockcount() {
        return blockcount;
    }

    public void setBlockcount(Integer blockcount) {
        this.blockcount = blockcount;
    }

    public Integer getTxncount() {
        return txncount;
    }

    public void setTxncount(Integer txncount) {
        this.txncount = txncount;
    }

    public Integer getOntidactivecount() {
        return ontidactivecount;
    }

    public void setOntidactivecount(Integer ontidactivecount) {
        this.ontidactivecount = ontidactivecount;
    }

    public Integer getOntidnewcount() {
        return ontidnewcount;
    }

    public void setOntidnewcount(Integer ontidnewcount) {
        this.ontidnewcount = ontidnewcount;
    }

    public BigDecimal getOntcount() {
        return ontcount;
    }

    public void setOntcount(BigDecimal ontcount) {
        this.ontcount = ontcount;
    }

    public BigDecimal getOngcount() {
        return ongcount;
    }

    public void setOngcount(BigDecimal ongcount) {
        this.ongcount = ongcount;
    }

    public Integer getActiveaddress() {
        return activeaddress;
    }

    public void setActiveaddress(Integer activeaddress) {
        this.activeaddress = activeaddress;
    }

    public Integer getNewaddress() {
        return newaddress;
    }

    public void setNewaddress(Integer newaddress) {
        this.newaddress = newaddress;
    }
}