package com.github.ontio.model.dao;

import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "tbl_orc721_tx_detail")
public class Orc721TxDetail {
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
     * 区块链交易类型，211 部署/ 调用ORC721 类型的合约
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
     * orc721 通证的id 标识
     */
    private BigDecimal amount;


    /**
     * 交易手续费 gasConsumed
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


    /**
     * orc721 通证的id 标识
     */
    private String tokenId;

    @Builder
    public Orc721TxDetail(String txHash, Integer txIndex, Integer txType, Integer txTime, Integer blockHeight, BigDecimal amount, String tokenId, BigDecimal fee, String assetName, String fromAddress, String toAddress, String description, Integer blockIndex, Integer confirmFlag, Integer eventType, String contractHash, String payer, String calledContractHash) {
        this.txHash = txHash;
        this.txIndex = txIndex;
        this.txType = txType;
        this.txTime = txTime;
        this.blockHeight = blockHeight;
        this.amount = amount;
        this.tokenId = tokenId;
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


}