package com.github.ontio.model.dao;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tbl_erc20")
public class Erc721 {
    /**
     * 合约hash值
     */
    @Id
    @Column(name = "contract_hash")
    private String contractHash;


    /**
     * ERC20代币名称
     */
    private String name;

    /**
     * ERC20代币总量
     */
    @Column(name = "total_supply")
    private Long totalSupply;

}
