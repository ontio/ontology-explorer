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

    public Block(Integer blockHeight, String blockHash, String txsRoot, Integer blockTime, String consensusData, String bookkeepers, Integer txCount, Integer blockSize) {
        this.blockHeight = blockHeight;
        this.blockHash = blockHash;
        this.txsRoot = txsRoot;
        this.blockTime = blockTime;
        this.consensusData = consensusData;
        this.bookkeepers = bookkeepers;
        this.txCount = txCount;
        this.blockSize = blockSize;
    }

    public Integer getBlockHeight() {
        return blockHeight;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public String getTxsRoot() {
        return txsRoot;
    }

    public Integer getBlockTime() {
        return blockTime;
    }

    public String getConsensusData() {
        return consensusData;
    }

    public String getBookkeepers() {
        return bookkeepers;
    }

    public Integer getTxCount() {
        return txCount;
    }

    public Integer getBlockSize() {
        return blockSize;
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