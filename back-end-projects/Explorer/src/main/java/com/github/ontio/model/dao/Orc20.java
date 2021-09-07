package com.github.ontio.model.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tbl_orc20")
@Data

public class Orc20 {

    /**
     * 合约hash值
     */
    @Id
    @Column(name = "contract_hash")
    private String contractHash;


    /**
     * ORC20代币名称
     */
    private String name;

    /**
     * ORC20代币总量
     */
    @Column(name = "total_supply")
    private Long totalSupply;

    /**
     * ORC20 代币符号
     */
    private String symbol;

    /**
     * ORC20 代币精度
     */
    private Integer decimals;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 审核标识，1：审核通过 0：未审核
     */
    @Column(name = "audit_flag")
    private Boolean auditFlag;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;



    /**
     * 合约vm类型，分为neovm，wasmvm
     */
    @Column(name = "vm_category")
    private String vmCategory;




}
