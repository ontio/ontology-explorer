package com.github.ontio.model;

public class TransactionDetailKey {
    private String txnhash;

    private Integer txnindex;

    public String getTxnhash() {
        return txnhash;
    }

    public void setTxnhash(String txnhash) {
        this.txnhash = txnhash == null ? null : txnhash.trim();
    }

    public Integer getTxnindex() {
        return txnindex;
    }

    public void setTxnindex(Integer txnindex) {
        this.txnindex = txnindex;
    }
}