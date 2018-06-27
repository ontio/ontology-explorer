package com.github.ontio.model;

import java.math.BigDecimal;

public class OntId {
    private String txnhash;

    private Integer txntype;

    private String ontid;

    private Integer txntime;

    private Integer height;

    private String description;

    private BigDecimal fee;

    public String getTxnhash() {
        return txnhash;
    }

    public void setTxnhash(String txnhash) {
        this.txnhash = txnhash == null ? null : txnhash.trim();
    }

    public Integer getTxntype() {
        return txntype;
    }

    public void setTxntype(Integer txntype) {
        this.txntype = txntype;
    }

    public String getOntid() {
        return ontid;
    }

    public void setOntid(String ontid) {
        this.ontid = ontid == null ? null : ontid.trim();
    }

    public Integer getTxntime() {
        return txntime;
    }

    public void setTxntime(Integer txntime) {
        this.txntime = txntime;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }
}