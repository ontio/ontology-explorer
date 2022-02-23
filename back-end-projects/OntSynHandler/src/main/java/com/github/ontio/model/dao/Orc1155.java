package com.github.ontio.model.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tbl_orc1155")
@Data
public class Orc1155 {
    /**
     * 合约hash值
     */
    @Id
    @Column(name = "contract_hash")
    private String contractHash;

    /**
     * ORC1155的token id
     */
    @Id
    @Column(name = "token_id")
    private String tokenId;

    /**
     * ORC1155代币名称
     */
    private String name;

    /**
     * ORC1155代币总量
     */
    @Column(name = "total_supply")
    private Long totalSupply;

    /**
     * ORC1155代币符号
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

    /**
     * 更新时间，yyyy-MM-dd
     */
    @Column(name = "vm_category")
    private String vmCategory;
}