package com.github.ontio.model.dao;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tbl_contract_daily_aggregation")
public class ContractDailyAggregation {
    /**
     * 合约hash
     */
    @Column(name = "contract_hash")
    private String contractHash;

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
     * 去重入金交易地址数量
     */
    @Column(name = "deposit_address_count")
    private Integer depositAddressCount;

    /**
     * 去重出金交易地址数量
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
     * 获取合约hash
     *
     * @return contract_hash - 合约hash
     */
    public String getContractHash() {
        return contractHash;
    }

    /**
     * 设置合约hash
     *
     * @param contractHash 合约hash
     */
    public void setContractHash(String contractHash) {
        this.contractHash = contractHash == null ? null : contractHash.trim();
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
     * 获取去重入金交易地址数量
     *
     * @return deposit_address_count - 去重入金交易地址数量
     */
    public Integer getDepositAddressCount() {
        return depositAddressCount;
    }

    /**
     * 设置去重入金交易地址数量
     *
     * @param depositAddressCount 去重入金交易地址数量
     */
    public void setDepositAddressCount(Integer depositAddressCount) {
        this.depositAddressCount = depositAddressCount;
    }

    /**
     * 获取去重出金交易地址数量
     *
     * @return withdraw_address_count - 去重出金交易地址数量
     */
    public Integer getWithdrawAddressCount() {
        return withdrawAddressCount;
    }

    /**
     * 设置去重出金交易地址数量
     *
     * @param withdrawAddressCount 去重出金交易地址数量
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