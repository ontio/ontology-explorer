package com.github.ontio.model;

import java.math.BigDecimal;

public class TransactionDetail{
    private String txnhash;

    private Integer txnindex;

    private Integer txntype;

    private Integer txntime;

    private Integer height;

    private BigDecimal amount;

    private BigDecimal fee;

    private String assetname;

    private String fromaddress;

    private String toaddress;

    private String description;

    private Integer blockindex;

    private Integer confirmflag;

    private Integer eventtype;

    private String contracthash;

    private String payer;

    public Integer getTxntype() {
        return txntype;
    }

    public void setTxntype(Integer txntype) {
        this.txntype = txntype;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getAssetname() {
        return assetname;
    }

    public void setAssetname(String assetname) {
        this.assetname = assetname == null ? null : assetname.trim();
    }

    public String getFromaddress() {
        return fromaddress;
    }

    public void setFromaddress(String fromaddress) {
        this.fromaddress = fromaddress == null ? null : fromaddress.trim();
    }

    public String getToaddress() {
        return toaddress;
    }

    public void setToaddress(String toaddress) {
        this.toaddress = toaddress == null ? null : toaddress.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getBlockindex() {
        return blockindex;
    }

    public void setBlockindex(Integer blockindex) {
        this.blockindex = blockindex;
    }

    public Integer getConfirmflag() {
        return confirmflag;
    }

    public void setConfirmflag(Integer confirmflag) {
        this.confirmflag = confirmflag;
    }

    public Integer getEventtype() {
        return eventtype;
    }

    public void setEventtype(Integer eventtype) {
        this.eventtype = eventtype;
    }

    public String getContracthash() {
        return contracthash;
    }

    public void setContracthash(String contracthash) {
        this.contracthash = contracthash;
    }

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

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer == null ? null : payer.trim();
    }
}