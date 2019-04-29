package com.github.ontio.blocksync.model;

import java.io.Serializable;

public class Block implements Serializable {
    private Integer blockHeight;

    private String blockHash;

    private String txsRoot;

    private Integer blockTime;

    private String consensusData;

    private String bookkeepers;

    private Integer txCount;

    private Integer blockSize;

    private static final long serialVersionUID = 1L;

    public Integer getBlockHeight() {
        return blockHeight;
    }

    public Block withBlockHeight(Integer blockHeight) {
        this.setBlockHeight(blockHeight);
        return this;
    }

    public void setBlockHeight(Integer blockHeight) {
        this.blockHeight = blockHeight;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public Block withBlockHash(String blockHash) {
        this.setBlockHash(blockHash);
        return this;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash == null ? null : blockHash.trim();
    }

    public String getTxsRoot() {
        return txsRoot;
    }

    public Block withTxsRoot(String txsRoot) {
        this.setTxsRoot(txsRoot);
        return this;
    }

    public void setTxsRoot(String txsRoot) {
        this.txsRoot = txsRoot == null ? null : txsRoot.trim();
    }

    public Integer getBlockTime() {
        return blockTime;
    }

    public Block withBlockTime(Integer blockTime) {
        this.setBlockTime(blockTime);
        return this;
    }

    public void setBlockTime(Integer blockTime) {
        this.blockTime = blockTime;
    }

    public String getConsensusData() {
        return consensusData;
    }

    public Block withConsensusData(String consensusData) {
        this.setConsensusData(consensusData);
        return this;
    }

    public void setConsensusData(String consensusData) {
        this.consensusData = consensusData == null ? null : consensusData.trim();
    }

    public String getBookkeepers() {
        return bookkeepers;
    }

    public Block withBookkeepers(String bookkeepers) {
        this.setBookkeepers(bookkeepers);
        return this;
    }

    public void setBookkeepers(String bookkeepers) {
        this.bookkeepers = bookkeepers == null ? null : bookkeepers.trim();
    }

    public Integer getTxCount() {
        return txCount;
    }

    public Block withTxCount(Integer txCount) {
        this.setTxCount(txCount);
        return this;
    }

    public void setTxCount(Integer txCount) {
        this.txCount = txCount;
    }

    public Integer getBlockSize() {
        return blockSize;
    }

    public Block withBlockSize(Integer blockSize) {
        this.setBlockSize(blockSize);
        return this;
    }

    public void setBlockSize(Integer blockSize) {
        this.blockSize = blockSize;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", blockHeight=").append(blockHeight);
        sb.append(", blockHash=").append(blockHash);
        sb.append(", txsRoot=").append(txsRoot);
        sb.append(", blockTime=").append(blockTime);
        sb.append(", consensusData=").append(consensusData);
        sb.append(", bookkeepers=").append(bookkeepers);
        sb.append(", txCount=").append(txCount);
        sb.append(", blockSize=").append(blockSize);
        sb.append("]");
        return sb.toString();
    }
}