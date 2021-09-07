package com.github.ontio.model.dao;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_node_cycle")
public class NodeCycle {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @Column(name = "public_key")
    private String publicKey;

    @Column(name = "address")
    private String address;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private Integer status;

    @Column(name = "node_type")
    private Integer nodeType;

    @Column(name = "node_proportion_t")
    private String nodeProportionT;

    @Column(name = "user_proportion_t")
    private String userProportionT;

    @Column(name = "node_proportion_t2")
    private String nodeProportionT2;

    @Column(name = "user_proportion_t2")
    private String userProportionT2;

    @Column(name = "bonus_ong")
    private BigDecimal bonusOng;

    @Column(name = "node_stake_ont")
    private Integer nodeStakeONT;

    @Column(name = "user_stake_ont")
    private Integer userStakeONT;

    @Column(name = "cycle")
    private Integer cycle;


}
