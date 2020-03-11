package com.github.ontio.model.dao;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tbl_address_daily_aggregation")
public class AddressDailyAggregation {
    /**
     * 地址
     */
    private String address;

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
     * 日终余额
     */
    private BigDecimal balance;

    /**
     * 当天USD价格
     */
    @Column(name = "usd_price")
    private BigDecimal usdPrice;

    /**
     * 最高余额
     */
    @Column(name = "max_balance")
    private BigDecimal maxBalance;

    /**
     * 最低余额
     */
    @Column(name = "min_balance")
    private BigDecimal minBalance;

    /**
     * 入金交易数量
     */
    @Column(name = "deposit_tx_count")
    private Integer depositTxCount;

    /**
     * 出金交易数量
     */
    @Column(name = "withdraw_tx_count")
    private Integer withdrawTxCount;

    /**
     * 入金金额
     */
    @Column(name = "deposit_amount")
    private BigDecimal depositAmount;

    /**
     * 出金金额
     */
    @Column(name = "withdraw_amount")
    private BigDecimal withdrawAmount;

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

    /**
     * 调用合约数量，只适用于虚拟Token统计
     */
    @Column(name = "contract_count")
    private Integer contractCount;

    /**
     * 是否为虚拟Token统计
     */
    @Column(name = "is_virtual")
    private Boolean isVirtual;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 获取地址
     *
     * @return address - 地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置地址
     *
     * @param address 地址
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

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
     * 获取日终余额
     *
     * @return balance - 日终余额
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * 设置日终余额
     *
     * @param balance 日终余额
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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
     * 获取最高余额
     *
     * @return max_balance - 最高余额
     */
    public BigDecimal getMaxBalance() {
        return maxBalance;
    }

    /**
     * 设置最高余额
     *
     * @param maxBalance 最高余额
     */
    public void setMaxBalance(BigDecimal maxBalance) {
        this.maxBalance = maxBalance;
    }

    /**
     * 获取最低余额
     *
     * @return min_balance - 最低余额
     */
    public BigDecimal getMinBalance() {
        return minBalance;
    }

    /**
     * 设置最低余额
     *
     * @param minBalance 最低余额
     */
    public void setMinBalance(BigDecimal minBalance) {
        this.minBalance = minBalance;
    }

    /**
     * 获取入金交易数量
     *
     * @return deposit_tx_count - 入金交易数量
     */
    public Integer getDepositTxCount() {
        return depositTxCount;
    }

    /**
     * 设置入金交易数量
     *
     * @param depositTxCount 入金交易数量
     */
    public void setDepositTxCount(Integer depositTxCount) {
        this.depositTxCount = depositTxCount;
    }

    /**
     * 获取出金交易数量
     *
     * @return withdraw_tx_count - 出金交易数量
     */
    public Integer getWithdrawTxCount() {
        return withdrawTxCount;
    }

    /**
     * 设置出金交易数量
     *
     * @param withdrawTxCount 出金交易数量
     */
    public void setWithdrawTxCount(Integer withdrawTxCount) {
        this.withdrawTxCount = withdrawTxCount;
    }

    /**
     * 获取入金金额
     *
     * @return deposit_amount - 入金金额
     */
    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    /**
     * 设置入金金额
     *
     * @param depositAmount 入金金额
     */
    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    /**
     * 获取出金金额
     *
     * @return withdraw_amount - 出金金额
     */
    public BigDecimal getWithdrawAmount() {
        return withdrawAmount;
    }

    /**
     * 设置出金金额
     *
     * @param withdrawAmount 出金金额
     */
    public void setWithdrawAmount(BigDecimal withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
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
     * 获取调用合约数量，只适用于虚拟Token统计
     *
     * @return contract_count - 调用合约数量，只适用于虚拟Token统计
     */
    public Integer getContractCount() {
        return contractCount;
    }

    /**
     * 设置调用合约数量，只适用于虚拟Token统计
     *
     * @param contractCount 调用合约数量，只适用于虚拟Token统计
     */
    public void setContractCount(Integer contractCount) {
        this.contractCount = contractCount;
    }

    /**
     * 获取是否为虚拟Token统计
     *
     * @return is_virtual - 是否为虚拟Token统计
     */
    public Boolean getIsVirtual() {
        return isVirtual;
    }

    /**
     * 设置是否为虚拟Token统计
     *
     * @param isVirtual 是否为虚拟Token统计
     */
    public void setIsVirtual(Boolean isVirtual) {
        this.isVirtual = isVirtual;
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