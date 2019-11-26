package com.github.ontio.model.dao;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.ontio.util.TxAmountSerializer;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "tbl_dapp_daily_summary")
public class DappDailySummary {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 当天的UTC0点时间戳
     */
    private Integer time;

    /**
     * 此合约当天的交易数量
     */
    @Column(name = "tx_count")
    private Integer txCount;

    /**
     * 此合约当天的ont流通量
     */
    @JsonSerialize(using = TxAmountSerializer.class)
    @Column(name = "ont_sum")
    private BigDecimal ontSum;

    /**
     * 此合约当天的ong流通量
     */
    @JsonSerialize(using = TxAmountSerializer.class)
    @Column(name = "ong_sum")
    private BigDecimal ongSum;

    /**
     * 此合约当天的活跃地址数
     */
    @Column(name = "active_address_count")
    private Integer activeAddressCount;

    /**
     * 此合约当天的新地址数
     */
    @Column(name = "new_address_count")
    private Integer newAddressCount;

    /**
     * 合约所属dapp名称
     */
    @Column(name = "dapp_name")
    private String dappName;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

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
     * 获取此合约当天的交易数量
     *
     * @return tx_count - 此合约当天的交易数量
     */
    public Integer getTxCount() {
        return txCount;
    }

    /**
     * 设置此合约当天的交易数量
     *
     * @param txCount 此合约当天的交易数量
     */
    public void setTxCount(Integer txCount) {
        this.txCount = txCount;
    }

    /**
     * 获取此合约当天的ont流通量
     *
     * @return ont_sum - 此合约当天的ont流通量
     */
    public BigDecimal getOntSum() {
        return ontSum;
    }

    /**
     * 设置此合约当天的ont流通量
     *
     * @param ontSum 此合约当天的ont流通量
     */
    public void setOntSum(BigDecimal ontSum) {
        this.ontSum = ontSum;
    }

    /**
     * 获取此合约当天的ong流通量
     *
     * @return ong_sum - 此合约当天的ong流通量
     */
    public BigDecimal getOngSum() {
        return ongSum;
    }

    /**
     * 设置此合约当天的ong流通量
     *
     * @param ongSum 此合约当天的ong流通量
     */
    public void setOngSum(BigDecimal ongSum) {
        this.ongSum = ongSum;
    }

    /**
     * 获取此合约当天的活跃地址数
     *
     * @return active_address_count - 此合约当天的活跃地址数
     */
    public Integer getActiveAddressCount() {
        return activeAddressCount;
    }

    /**
     * 设置此合约当天的活跃地址数
     *
     * @param activeAddressCount 此合约当天的活跃地址数
     */
    public void setActiveAddressCount(Integer activeAddressCount) {
        this.activeAddressCount = activeAddressCount;
    }

    /**
     * 获取此合约当天的新地址数
     *
     * @return new_address_count - 此合约当天的新地址数
     */
    public Integer getNewAddressCount() {
        return newAddressCount;
    }

    /**
     * 设置此合约当天的新地址数
     *
     * @param newAddressCount 此合约当天的新地址数
     */
    public void setNewAddressCount(Integer newAddressCount) {
        this.newAddressCount = newAddressCount;
    }

    /**
     * 获取合约所属dapp名称
     *
     * @return dapp_name - 合约所属dapp名称
     */
    public String getDappName() {
        return dappName;
    }

    /**
     * 设置合约所属dapp名称
     *
     * @param dappName 合约所属dapp名称
     */
    public void setDappName(String dappName) {
        this.dappName = dappName == null ? null : dappName.trim();
    }
}