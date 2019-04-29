package com.github.ontio.blocksync.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Oep4TxDetail extends Oep4TxDetailKey implements Serializable {
    private Integer txType;

    private Integer txTime;

    private Integer blockHeight;

    private BigDecimal amount;

    private BigDecimal fee;

    private String assetName;

    private String fromAddress;

    private String toAddress;

    private String description;

    private Integer blockIndex;

    private Integer confirmFlag;

    private Integer eventType;

    private String contractHash;

    private String payer;

    private String calledContractHash;

    private static final long serialVersionUID = 1L;

    public Integer getTxType() {
        return txType;
    }

    public Oep4TxDetail withTxType(Integer txType) {
        this.setTxType(txType);
        return this;
    }

    public void setTxType(Integer txType) {
        this.txType = txType;
    }

    public Integer getTxTime() {
        return txTime;
    }

    public Oep4TxDetail withTxTime(Integer txTime) {
        this.setTxTime(txTime);
        return this;
    }

    public void setTxTime(Integer txTime) {
        this.txTime = txTime;
    }

    public Integer getBlockHeight() {
        return blockHeight;
    }

    public Oep4TxDetail withBlockHeight(Integer blockHeight) {
        this.setBlockHeight(blockHeight);
        return this;
    }

    public void setBlockHeight(Integer blockHeight) {
        this.blockHeight = blockHeight;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Oep4TxDetail withAmount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public Oep4TxDetail withFee(BigDecimal fee) {
        this.setFee(fee);
        return this;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getAssetName() {
        return assetName;
    }

    public Oep4TxDetail withAssetName(String assetName) {
        this.setAssetName(assetName);
        return this;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName == null ? null : assetName.trim();
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public Oep4TxDetail withFromAddress(String fromAddress) {
        this.setFromAddress(fromAddress);
        return this;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress == null ? null : fromAddress.trim();
    }

    public String getToAddress() {
        return toAddress;
    }

    public Oep4TxDetail withToAddress(String toAddress) {
        this.setToAddress(toAddress);
        return this;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress == null ? null : toAddress.trim();
    }

    public String getDescription() {
        return description;
    }

    public Oep4TxDetail withDescription(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getBlockIndex() {
        return blockIndex;
    }

    public Oep4TxDetail withBlockIndex(Integer blockIndex) {
        this.setBlockIndex(blockIndex);
        return this;
    }

    public void setBlockIndex(Integer blockIndex) {
        this.blockIndex = blockIndex;
    }

    public Integer getConfirmFlag() {
        return confirmFlag;
    }

    public Oep4TxDetail withConfirmFlag(Integer confirmFlag) {
        this.setConfirmFlag(confirmFlag);
        return this;
    }

    public void setConfirmFlag(Integer confirmFlag) {
        this.confirmFlag = confirmFlag;
    }

    public Integer getEventType() {
        return eventType;
    }

    public Oep4TxDetail withEventType(Integer eventType) {
        this.setEventType(eventType);
        return this;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public String getContractHash() {
        return contractHash;
    }

    public Oep4TxDetail withContractHash(String contractHash) {
        this.setContractHash(contractHash);
        return this;
    }

    public void setContractHash(String contractHash) {
        this.contractHash = contractHash == null ? null : contractHash.trim();
    }

    public String getPayer() {
        return payer;
    }

    public Oep4TxDetail withPayer(String payer) {
        this.setPayer(payer);
        return this;
    }

    public void setPayer(String payer) {
        this.payer = payer == null ? null : payer.trim();
    }

    public String getCalledContractHash() {
        return calledContractHash;
    }

    public Oep4TxDetail withCalledContractHash(String calledContractHash) {
        this.setCalledContractHash(calledContractHash);
        return this;
    }

    public void setCalledContractHash(String calledContractHash) {
        this.calledContractHash = calledContractHash == null ? null : calledContractHash.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", txType=").append(txType);
        sb.append(", txTime=").append(txTime);
        sb.append(", blockHeight=").append(blockHeight);
        sb.append(", amount=").append(amount);
        sb.append(", fee=").append(fee);
        sb.append(", assetName=").append(assetName);
        sb.append(", fromAddress=").append(fromAddress);
        sb.append(", toAddress=").append(toAddress);
        sb.append(", description=").append(description);
        sb.append(", blockIndex=").append(blockIndex);
        sb.append(", confirmFlag=").append(confirmFlag);
        sb.append(", eventType=").append(eventType);
        sb.append(", contractHash=").append(contractHash);
        sb.append(", payer=").append(payer);
        sb.append(", calledContractHash=").append(calledContractHash);
        sb.append("]");
        return sb.toString();
    }
}