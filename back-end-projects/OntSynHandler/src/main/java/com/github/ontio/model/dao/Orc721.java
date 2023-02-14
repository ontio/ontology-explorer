package com.github.ontio.model.dao;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tbl_orc721")
@Data
@ToString
@NoArgsConstructor
public class Orc721 {

    /**
     * 合约hash值
     */
    @Id
    @Column(name = "contract_hash")
    private String contractHash;


    /**
     * ORC721代币名称
     */
    private String name;

    /**
     * ORC721 代币总量
     */
    @Column(name = "total_supply")
    private Long totalSupply;

    /**
     * ORC20 代币符号
     */
    private String symbol;

    /**
     * 创建时间，yyyy-MM-dd
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 审核标识，1：审核通过 0：未审核
     */
    @Column(name = "audit_flag")
    private Boolean auditFlag;

    /**
     * 更新时间，yyyy-MM-dd
     */
    @Column(name = "update_time")
    private Date updateTime;

    @Builder
    public Orc721(String contractHash, String name, Long totalSupply, String symbol, Date createTime, Boolean auditFlag, Date updateTime) {
        this.contractHash = contractHash;
        this.name = name;
        this.totalSupply = totalSupply;
        this.symbol = symbol;
        this.createTime = createTime;
        this.auditFlag = auditFlag;
        this.updateTime = updateTime;
    }


}
