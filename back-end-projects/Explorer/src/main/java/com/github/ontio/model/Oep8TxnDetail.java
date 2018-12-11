package com.github.ontio.model;

public class Oep8TxnDetail extends TransactionDetail {
    private String tokenname;

    public String getTokenname() {
        return tokenname;
    }

    public void setTokenname(String tokenname) {
        this.tokenname = tokenname == null ? null : tokenname.trim();
    }

}