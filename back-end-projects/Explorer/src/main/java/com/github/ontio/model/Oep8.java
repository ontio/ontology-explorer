package com.github.ontio.model;

public class Oep8 extends Oep{
    private String tokenid;

    public String getTokenid() {
        return tokenid;
    }

    public void setTokenid(String tokenid) {
        this.tokenid = tokenid == null ? null : tokenid.trim();
    }
}