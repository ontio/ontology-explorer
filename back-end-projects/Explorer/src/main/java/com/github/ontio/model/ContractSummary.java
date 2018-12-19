package com.github.ontio.model;

import java.math.BigDecimal;

public class ContractSummary {
    private Long id;

    private Integer time;

    private String contracthash;

    private Integer txncount;

    private BigDecimal ontcount;

    private BigDecimal ongcount;

    private Integer activeaddress;

    private Integer newaddress;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getContracthash() {
        return contracthash;
    }

    public void setContracthash(String contracthash) {
        this.contracthash = contracthash == null ? null : contracthash.trim();
    }

    public Integer getTxncount() {
        return txncount;
    }

    public void setTxncount(Integer txncount) {
        this.txncount = txncount;
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