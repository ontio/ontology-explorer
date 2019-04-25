package com.github.ontio.model.dao;

import javax.persistence.*;

@Table(name = "tbl_tx_eventlog")
public class TxEventLog {
    /**
     * 交易hash值
     */
    @Id
    @Column(name = "tx_hash")
    private String txHash;

    /**
     * 区块链交易类型，208：部署合约交易 209：调用合约交易
     */
    @Column(name = "tx_type")
    private Integer txType;

    /**
     * 交易时间戳
     */
    @Column(name = "tx_time")
    private Integer txTime;

    /**
     * 区块高度
     */
    @Column(name = "block_height")
    private Integer blockHeight;

    /**
     * 交易在区块里的索引
     */
    @Column(name = "block_index")
    private Integer blockIndex;

    /**
     * 该交易真正调用的合约hash
     */
    @Column(name = "called_contract_hash")
    private String calledContractHash;

    /**
     * 交易的event log
     */
    @Column(name = "event_log")
    private String eventLog;

    /**
     * 获取交易hash值
     *
     * @return tx_hash - 交易hash值
     */
    public String getTxHash() {
        return txHash;
    }

    /**
     * 设置交易hash值
     *
     * @param txHash 交易hash值
     */
    public void setTxHash(String txHash) {
        this.txHash = txHash == null ? null : txHash.trim();
    }

    /**
     * 获取区块链交易类型，208：部署合约交易 209：调用合约交易
     *
     * @return tx_type - 区块链交易类型，208：部署合约交易 209：调用合约交易
     */
    public Integer getTxType() {
        return txType;
    }

    /**
     * 设置区块链交易类型，208：部署合约交易 209：调用合约交易
     *
     * @param txType 区块链交易类型，208：部署合约交易 209：调用合约交易
     */
    public void setTxType(Integer txType) {
        this.txType = txType;
    }

    /**
     * 获取交易时间戳
     *
     * @return tx_time - 交易时间戳
     */
    public Integer getTxTime() {
        return txTime;
    }

    /**
     * 设置交易时间戳
     *
     * @param txTime 交易时间戳
     */
    public void setTxTime(Integer txTime) {
        this.txTime = txTime;
    }

    /**
     * 获取区块高度
     *
     * @return block_height - 区块高度
     */
    public Integer getBlockHeight() {
        return blockHeight;
    }

    /**
     * 设置区块高度
     *
     * @param blockHeight 区块高度
     */
    public void setBlockHeight(Integer blockHeight) {
        this.blockHeight = blockHeight;
    }

    /**
     * 获取交易在区块里的索引
     *
     * @return block_index - 交易在区块里的索引
     */
    public Integer getBlockIndex() {
        return blockIndex;
    }

    /**
     * 设置交易在区块里的索引
     *
     * @param blockIndex 交易在区块里的索引
     */
    public void setBlockIndex(Integer blockIndex) {
        this.blockIndex = blockIndex;
    }

    /**
     * 获取该交易真正调用的合约hash
     *
     * @return called_contract_hash - 该交易真正调用的合约hash
     */
    public String getCalledContractHash() {
        return calledContractHash;
    }

    /**
     * 设置该交易真正调用的合约hash
     *
     * @param calledContractHash 该交易真正调用的合约hash
     */
    public void setCalledContractHash(String calledContractHash) {
        this.calledContractHash = calledContractHash == null ? null : calledContractHash.trim();
    }

    /**
     * 获取交易的event log
     *
     * @return event_log - 交易的event log
     */
    public String getEventLog() {
        return eventLog;
    }

    /**
     * 设置交易的event log
     *
     * @param eventLog 交易的event log
     */
    public void setEventLog(String eventLog) {
        this.eventLog = eventLog == null ? null : eventLog.trim();
    }
}