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

    public Oep4TxDetail(String txHash, Integer txIndex, Integer txType, Integer txTime, Integer blockHeight, BigDecimal amount, BigDecimal fee, String assetName, String fromAddress, String toAddress, String description, Integer blockIndex, Integer confirmFlag, Integer eventType, String contractHash, String payer, String calledContractHash) {
        super(txHash, txIndex);
        this.txType = txType;
        this.txTime = txTime;
        this.blockHeight = blockHeight;
        this.amount = amount;
        this.fee = fee;
        this.assetName = assetName;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.description = description;
        this.blockIndex = blockIndex;
        this.confirmFlag = confirmFlag;
        this.eventType = eventType;
        this.contractHash = contractHash;
        this.payer = payer;
        this.calledContractHash = calledContractHash;
    }

    public Integer getTxType() {
        return txType;
    }

    public Integer getTxTime() {
        return txTime;
    }

    public Integer getBlockHeight() {
        return blockHeight;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public String getAssetName() {
        return assetName;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public String getDescription() {
        return description;
    }

    public Integer getBlockIndex() {
        return blockIndex;
    }

    public Integer getConfirmFlag() {
        return confirmFlag;
    }

    public Integer getEventType() {
        return eventType;
    }

    public String getContractHash() {
        return contractHash;
    }

    public String getPayer() {
        return payer;
    }

    public String getCalledContractHash() {
        return calledContractHash;
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