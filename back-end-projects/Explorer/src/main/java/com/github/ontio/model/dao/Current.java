package com.github.ontio.model.dao;

import javax.persistence.*;

@Table(name = "tbl_current")
public class Current {
    /**
     * 当前同步的最新区块高度
     */
    @Column(name = "block_height")
    private Integer blockHeight;

    /**
     * 当前同步的最新交易数量
     */
    @Column(name = "tx_count")
    private Integer txCount;

    /**
     * 当前同步的最新ONT ID数量
     */
    @Column(name = "ontid_count")
    private Integer ontidCount;

    /**
     * 当前同步的最新ONT ID相关的交易数量
     */
    @Column(name = "ontid_tx_count")
    private Integer ontidTxCount;

    /**
     * 获取当前同步的最新区块高度
     *
     * @return block_height - 当前同步的最新区块高度
     */
    public Integer getBlockHeight() {
        return blockHeight;
    }

    /**
     * 设置当前同步的最新区块高度
     *
     * @param blockHeight 当前同步的最新区块高度
     */
    public void setBlockHeight(Integer blockHeight) {
        this.blockHeight = blockHeight;
    }

    /**
     * 获取当前同步的最新交易数量
     *
     * @return tx_count - 当前同步的最新交易数量
     */
    public Integer getTxCount() {
        return txCount;
    }

    /**
     * 设置当前同步的最新交易数量
     *
     * @param txCount 当前同步的最新交易数量
     */
    public void setTxCount(Integer txCount) {
        this.txCount = txCount;
    }

    /**
     * 获取当前同步的最新ONT ID数量
     *
     * @return ontid_count - 当前同步的最新ONT ID数量
     */
    public Integer getOntidCount() {
        return ontidCount;
    }

    /**
     * 设置当前同步的最新ONT ID数量
     *
     * @param ontidCount 当前同步的最新ONT ID数量
     */
    public void setOntidCount(Integer ontidCount) {
        this.ontidCount = ontidCount;
    }

    /**
     * 获取当前同步的最新ONT ID相关的交易数量
     *
     * @return ontid_tx_count - 当前同步的最新ONT ID相关的交易数量
     */
    public Integer getOntidTxCount() {
        return ontidTxCount;
    }

    /**
     * 设置当前同步的最新ONT ID相关的交易数量
     *
     * @param ontidTxCount 当前同步的最新ONT ID相关的交易数量
     */
    public void setOntidTxCount(Integer ontidTxCount) {
        this.ontidTxCount = ontidTxCount;
    }
}