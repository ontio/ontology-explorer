package com.github.ontio.model;

import java.util.Date;

public class Daily {
    private Date time;

    private Integer txncount;

    private Integer ontidcount;

    private Integer blockcount;

    private Integer addresscount;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getTxncount() {
        return txncount;
    }

    public void setTxncount(Integer txncount) {
        this.txncount = txncount;
    }

    public Integer getOntidcount() {
        return ontidcount;
    }

    public void setOntidcount(Integer ontidcount) {
        this.ontidcount = ontidcount;
    }

    public Integer getBlockcount() {
        return blockcount;
    }

    public void setBlockcount(Integer blockcount) {
        this.blockcount = blockcount;
    }

    public Integer getAddresscount() {
        return addresscount;
    }

    public void setAddresscount(Integer addresscount) {
        this.addresscount = addresscount;
    }
}