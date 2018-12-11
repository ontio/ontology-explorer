package com.github.ontio.model;


public class Oep8TxnDetail extends TransactionDetail {
    private String tokenid;

    public String getTokenid() {
        return tokenid;
    }

    public void setTokenid(String tokenid) {
        this.tokenid = tokenid == null ? null : tokenid.trim();
    }
}