package com.github.ontio.model.dao;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tbl_token_daily_aggregation")
public class TokenDailyAggregation {
    /**
     * Token合约hash
     */
    @Column(name = "token_contract_hash")
    private String tokenContractHash;

    /**
     * 日期维度ID
     */
    @Column(name = "date_id")
    private Integer dateId;

    /**
     * 当天USD价格
     */
    @Column(name = "usd_price")
    private BigDecimal usdPrice;

    /**
     * 交易数量
     */
    @Column(name = "tx_count")
    private Integer txCount;

    /**
     * 交易金额
     */
    @Column(name = "tx_amount")
    private BigDecimal txAmount;

    /**
     * 去重入金地址数量
     */
    @Column(name = "deposit_address_count")
    private Integer depositAddressCount;

    /**
     * 去重出金地址数量
     */
    @Column(name = "withdraw_address_count")
    private Integer withdrawAddressCount;

    /**
     * 去重交易地址数量
     */
    @Column(name = "tx_address_count")
    private Integer txAddressCount;

    /**
     * 消耗手续费总额
     */
    @Column(name = "fee_amount")
    private BigDecimal feeAmount;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 获取Token合约hash
     *
     * @return token_contract_hash - Token合约hash
     */
    public String getTokenContractHash() {
        return tokenContractHash;
    }

    /**
     * 设置Token合约hash
     *
     * @param tokenContractHash Token合约hash
     */
    public void setTokenContractHash(String tokenContractHash) {
        this.tokenContractHash = tokenContractHash == null ? null : tokenContractHash.trim();
    }

    /**
     * 获取日期维度ID
     *
     * @return date_id - 日期维度ID
     */
    public Integer getDateId() {
        return dateId;
    }

    /**
     * 设置日期维度ID
     *
     * @param dateId 日期维度ID
     */
    public void setDateId(Integer dateId) {
        this.dateId = dateId;
    }

    /**
     * 获取当天USD价格
     *
     * @return usd_price - 当天USD价格
     */
    public BigDecimal getUsdPrice() {
        return usdPrice;
    }

    /**
     * 设置当天USD价格
     *
     * @param usdPrice 当天USD价格
     */
    public void setUsdPrice(BigDecimal usdPrice) {
        this.usdPrice = usdPrice;
    }

    /**
     * 获取交易数量
     *
     * @return tx_count - 交易数量
     */
    public Integer getTxCount() {
        return txCount;
    }

    /**
     * 设置交易数量
     *
     * @param txCount 交易数量
     */
    public void setTxCount(Integer txCount) {
        this.txCount = txCount;
    }

    /**
     * 获取交易金额
     *
     * @return tx_amount - 交易金额
     */
    public BigDecimal getTxAmount() {
        return txAmount;
    }

    /**
     * 设置交易金额
     *
     * @param txAmount 交易金额
     */
    public void setTxAmount(BigDecimal txAmount) {
        this.txAmount = txAmount;
    }

    /**
     * 获取去重入金地址数量
     *
     * @return deposit_address_count - 去重入金地址数量
     */
    public Integer getDepositAddressCount() {
        return depositAddressCount;
    }

    /**
     * 设置去重入金地址数量
     *
     * @param depositAddressCount 去重入金地址数量
     */
    public void setDepositAddressCount(Integer depositAddressCount) {
        this.depositAddressCount = depositAddressCount;
    }

    /**
     * 获取去重出金地址数量
     *
     * @return withdraw_address_count - 去重出金地址数量
     */
    public Integer getWithdrawAddressCount() {
        return withdrawAddressCount;
    }

    /**
     * 设置去重出金地址数量
     *
     * @param withdrawAddressCount 去重出金地址数量
     */
    public void setWithdrawAddressCount(Integer withdrawAddressCount) {
        this.withdrawAddressCount = withdrawAddressCount;
    }

    /**
     * 获取去重交易地址数量
     *
     * @return tx_address_count - 去重交易地址数量
     */
    public Integer getTxAddressCount() {
        return txAddressCount;
    }

    /**
     * 设置去重交易地址数量
     *
     * @param txAddressCount 去重交易地址数量
     */
    public void setTxAddressCount(Integer txAddressCount) {
        this.txAddressCount = txAddressCount;
    }

    /**
     * 获取消耗手续费总额
     *
     * @return fee_amount - 消耗手续费总额
     */
    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    /**
     * 设置消耗手续费总额
     *
     * @param feeAmount 消耗手续费总额
     */
    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}