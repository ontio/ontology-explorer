package com.github.ontio.model.dao;

import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "tbl_oep8_tx_detail")
public class Oep8TxDetail {
    /**
     * 交易hash
     */
    @Id
    @Column(name = "tx_hash")
    private String txHash;

    /**
     * 该event在交易eventlog里的索引
     */
    @Id
    @Column(name = "tx_index")
    private Integer txIndex;

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
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 交易手续费
     */
    private BigDecimal fee;

    /**
     * 交易资产名
     */
    @Column(name = "asset_name")
    private String assetName;

    /**
     * 交易fromaddress
     */
    @Column(name = "from_address")
    private String fromAddress;

    /**
     * 交易toaddress
     */
    @Column(name = "to_address")
    private String toAddress;

    /**
     * 交易描述
     */
    private String description;

    /**
     * 交易在区块里的索引
     */
    @Column(name = "block_index")
    private Integer blockIndex;

    /**
     * 交易落账标识  1：成功 0：失败
     */
    @Column(name = "confirm_flag")
    private Integer confirmFlag;

    /**
     * 交易event类型 0:其他 1:部署合约 2:手续费 3:转账 4:ONT ID 5:存证 6:权限
     */
    @Column(name = "event_type")
    private Integer eventType;

    /**
     * 该event对应的合约hash
     */
    @Column(name = "contract_hash")
    private String contractHash;

    /**
     * 交易的payer
     */
    private String payer;

    /**
     * 该交易真正调用的合约hash
     */
    @Column(name = "called_contract_hash")
    private String calledContractHash;

    @Builder
    public Oep8TxDetail(String txHash, Integer txIndex, Integer txType, Integer txTime, Integer blockHeight, BigDecimal amount, BigDecimal fee, String assetName, String fromAddress, String toAddress, String description, Integer blockIndex, Integer confirmFlag, Integer eventType, String contractHash, String payer, String calledContractHash) {
        this.txHash = txHash;
        this.txIndex = txIndex;
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

    /**
     * 获取交易hash
     *
     * @return tx_hash - 交易hash
     */
    public String getTxHash() {
        return txHash;
    }

    /**
     * 设置交易hash
     *
     * @param txHash 交易hash
     */
    public void setTxHash(String txHash) {
        this.txHash = txHash == null ? null : txHash.trim();
    }

    /**
     * 获取该event在交易eventlog里的索引
     *
     * @return tx_index - 该event在交易eventlog里的索引
     */
    public Integer getTxIndex() {
        return txIndex;
    }

    /**
     * 设置该event在交易eventlog里的索引
     *
     * @param txIndex 该event在交易eventlog里的索引
     */
    public void setTxIndex(Integer txIndex) {
        this.txIndex = txIndex;
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
     * 获取交易金额
     *
     * @return amount - 交易金额
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置交易金额
     *
     * @param amount 交易金额
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 获取交易手续费
     *
     * @return fee - 交易手续费
     */
    public BigDecimal getFee() {
        return fee;
    }

    /**
     * 设置交易手续费
     *
     * @param fee 交易手续费
     */
    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    /**
     * 获取交易资产名
     *
     * @return asset_name - 交易资产名
     */
    public String getAssetName() {
        return assetName;
    }

    /**
     * 设置交易资产名
     *
     * @param assetName 交易资产名
     */
    public void setAssetName(String assetName) {
        this.assetName = assetName == null ? null : assetName.trim();
    }

    /**
     * 获取交易fromaddress
     *
     * @return from_address - 交易fromaddress
     */
    public String getFromAddress() {
        return fromAddress;
    }

    /**
     * 设置交易fromaddress
     *
     * @param fromAddress 交易fromaddress
     */
    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress == null ? null : fromAddress.trim();
    }

    /**
     * 获取交易toaddress
     *
     * @return to_address - 交易toaddress
     */
    public String getToAddress() {
        return toAddress;
    }

    /**
     * 设置交易toaddress
     *
     * @param toAddress 交易toaddress
     */
    public void setToAddress(String toAddress) {
        this.toAddress = toAddress == null ? null : toAddress.trim();
    }

    /**
     * 获取交易描述
     *
     * @return description - 交易描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置交易描述
     *
     * @param description 交易描述
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
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
     * 获取交易落账标识  1：成功 0：失败
     *
     * @return confirm_flag - 交易落账标识  1：成功 0：失败
     */
    public Integer getConfirmFlag() {
        return confirmFlag;
    }

    /**
     * 设置交易落账标识  1：成功 0：失败
     *
     * @param confirmFlag 交易落账标识  1：成功 0：失败
     */
    public void setConfirmFlag(Integer confirmFlag) {
        this.confirmFlag = confirmFlag;
    }

    /**
     * 获取交易event类型 0:其他 1:部署合约 2:手续费 3:转账 4:ONT ID 5:存证 6:权限
     *
     * @return event_type - 交易event类型 0:其他 1:部署合约 2:手续费 3:转账 4:ONT ID 5:存证 6:权限
     */
    public Integer getEventType() {
        return eventType;
    }

    /**
     * 设置交易event类型 0:其他 1:部署合约 2:手续费 3:转账 4:ONT ID 5:存证 6:权限
     *
     * @param eventType 交易event类型 0:其他 1:部署合约 2:手续费 3:转账 4:ONT ID 5:存证 6:权限
     */
    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    /**
     * 获取该event对应的合约hash
     *
     * @return contract_hash - 该event对应的合约hash
     */
    public String getContractHash() {
        return contractHash;
    }

    /**
     * 设置该event对应的合约hash
     *
     * @param contractHash 该event对应的合约hash
     */
    public void setContractHash(String contractHash) {
        this.contractHash = contractHash == null ? null : contractHash.trim();
    }

    /**
     * 获取交易的payer
     *
     * @return payer - 交易的payer
     */
    public String getPayer() {
        return payer;
    }

    /**
     * 设置交易的payer
     *
     * @param payer 交易的payer
     */
    public void setPayer(String payer) {
        this.payer = payer == null ? null : payer.trim();
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
}