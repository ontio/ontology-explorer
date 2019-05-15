package com.github.ontio.model.dao;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.ontio.util.TxAmountSerializer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "tbl_ontid_tx_detail")
@NoArgsConstructor
@AllArgsConstructor
public class OntidTxDetail {
    /**
     * 交易hash
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
     * ONT ID
     */
    private String ontid;

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
     * ONT ID交易描述
     */
    private String description;

    /**
     * 交易手续费
     */
    @JsonSerialize(using = TxAmountSerializer.class)
    private BigDecimal fee;

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
     * 获取ONT ID
     *
     * @return ontid - ONT ID
     */
    public String getOntid() {
        return ontid;
    }

    /**
     * 设置ONT ID
     *
     * @param ontid ONT ID
     */
    public void setOntid(String ontid) {
        this.ontid = ontid == null ? null : ontid.trim();
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
     * 获取ONT ID交易描述
     *
     * @return description - ONT ID交易描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置ONT ID交易描述
     *
     * @param description ONT ID交易描述
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
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
}