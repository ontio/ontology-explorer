package com.github.ontio.model.dao;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.ontio.util.TxAmountSerializer;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "tbl_daily_summary")
public class DailySummary {
    /**
     * 当天的UTC0点时间戳
     */
    @Id
    private Integer time;

    /**
     * 当天的区块数量
     */
    @Column(name = "block_count")
    private Integer blockCount;

    /**
     * 当天的交易数量
     */
    @Column(name = "tx_count")
    private Integer txCount;

    /**
     * 当天的活跃ONT ID数量
     */
    @Column(name = "active_ontid_count")
    private Integer activeOntidCount;

    /**
     * 当天的新ONT ID数量
     */
    @Column(name = "new_ontid_count")
    private Integer newOntidCount;

    /**
     * 当天的ont流通量
     */
    @JsonSerialize(using = TxAmountSerializer.class)
    @Column(name = "ont_sum")
    private BigDecimal ontSum;

    /**
     * 当天的ong流通量
     */
    @JsonSerialize(using = TxAmountSerializer.class)
    @Column(name = "ong_sum")
    private BigDecimal ongSum;

    /**
     * 当天的活跃地址数量
     */
    @Column(name = "active_address_count")
    private Integer activeAddressCount;

    /**
     * 当天的新地址数量
     */
    @Column(name = "new_address_count")
    private Integer newAddressCount;

    /**
     * 获取当天的UTC0点时间戳
     *
     * @return time - 当天的UTC0点时间戳
     */
    public Integer getTime() {
        return time;
    }

    /**
     * 设置当天的UTC0点时间戳
     *
     * @param time 当天的UTC0点时间戳
     */
    public void setTime(Integer time) {
        this.time = time;
    }

    /**
     * 获取当天的区块数量
     *
     * @return block_count - 当天的区块数量
     */
    public Integer getBlockCount() {
        return blockCount;
    }

    /**
     * 设置当天的区块数量
     *
     * @param blockCount 当天的区块数量
     */
    public void setBlockCount(Integer blockCount) {
        this.blockCount = blockCount;
    }

    /**
     * 获取当天的交易数量
     *
     * @return tx_count - 当天的交易数量
     */
    public Integer getTxCount() {
        return txCount;
    }

    /**
     * 设置当天的交易数量
     *
     * @param txCount 当天的交易数量
     */
    public void setTxCount(Integer txCount) {
        this.txCount = txCount;
    }

    /**
     * 获取当天的活跃ONT ID数量
     *
     * @return active_ontid_count - 当天的活跃ONT ID数量
     */
    public Integer getActiveOntidCount() {
        return activeOntidCount;
    }

    /**
     * 设置当天的活跃ONT ID数量
     *
     * @param activeOntidCount 当天的活跃ONT ID数量
     */
    public void setActiveOntidCount(Integer activeOntidCount) {
        this.activeOntidCount = activeOntidCount;
    }

    /**
     * 获取当天的新ONT ID数量
     *
     * @return new_ontid_count - 当天的新ONT ID数量
     */
    public Integer getNewOntidCount() {
        return newOntidCount;
    }

    /**
     * 设置当天的新ONT ID数量
     *
     * @param newOntidCount 当天的新ONT ID数量
     */
    public void setNewOntidCount(Integer newOntidCount) {
        this.newOntidCount = newOntidCount;
    }

    /**
     * 获取当天的ont流通量
     *
     * @return ont_sum - 当天的ont流通量
     */
    public BigDecimal getOntSum() {
        return ontSum;
    }

    /**
     * 设置当天的ont流通量
     *
     * @param ontSum 当天的ont流通量
     */
    public void setOntSum(BigDecimal ontSum) {
        this.ontSum = ontSum;
    }

    /**
     * 获取当天的ong流通量
     *
     * @return ong_sum - 当天的ong流通量
     */
    public BigDecimal getOngSum() {
        return ongSum;
    }

    /**
     * 设置当天的ong流通量
     *
     * @param ongSum 当天的ong流通量
     */
    public void setOngSum(BigDecimal ongSum) {
        this.ongSum = ongSum;
    }

    /**
     * 获取当天的活跃地址数量
     *
     * @return active_address_count - 当天的活跃地址数量
     */
    public Integer getActiveAddressCount() {
        return activeAddressCount;
    }

    /**
     * 设置当天的活跃地址数量
     *
     * @param activeAddressCount 当天的活跃地址数量
     */
    public void setActiveAddressCount(Integer activeAddressCount) {
        this.activeAddressCount = activeAddressCount;
    }

    /**
     * 获取当天的新地址数量
     *
     * @return new_address_count - 当天的新地址数量
     */
    public Integer getNewAddressCount() {
        return newAddressCount;
    }

    /**
     * 设置当天的新地址数量
     *
     * @param newAddressCount 当天的新地址数量
     */
    public void setNewAddressCount(Integer newAddressCount) {
        this.newAddressCount = newAddressCount;
    }
}