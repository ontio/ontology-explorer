package com.github.ontio.model.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tbl_contract_tag")
public class ContractTag {

    @Id
    @Column(name = "contract_hash")
    private String contractHash;


    /**
     * 合约标签
     */
    private String name;


}
