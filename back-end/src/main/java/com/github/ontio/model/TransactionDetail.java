package com.github.ontio.model;

import java.math.BigDecimal;

public class TransactionDetail extends TransactionDetailKey {
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
}