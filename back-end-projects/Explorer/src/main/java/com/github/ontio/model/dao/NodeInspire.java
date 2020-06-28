package com.github.ontio.model.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_node_inspire")
public class NodeInspire {
    @Id
    @Column(name = "public_key")
    @GeneratedValue(generator = "JDBC")
    private String publicKey;

    private String address;

    private String name;

    private Integer status;

    @Column(name = "current_stake")
    private Long currentStake;

    @Column(name = "node_release_inspire")
    private Long nodeReleaseInspire;

    @Column(name = "node_release_inspire_rate")
    private String nodeReleaseInspireRate;

    @Column(name = "user_release_inspire")
    private Long userReleaseInspire;

    @Column(name = "user_release_inspire_rate")
    private String userReleaseInspireRate;

    @Column(name = "node_commission_inspire")
    private Long nodeCommissionInspire;

    @Column(name = "node_commission_inspire_rate")
    private String nodeCommissionInspireRate;

    @Column(name = "user_commission_inspire")
    private Long userCommissionInspire;

    @Column(name = "user_commission_inspire_rate")
    private String userCommissionInspireRate;

    @Column(name = "node_foundation_inspire")
    private Long nodeFoundationInspire;

    @Column(name = "node_foundation_inspire_rate")
    private String nodeFoundationInspireRate;

    @Column(name = "user_foundation_inspire")
    private Long userFoundationInspire;

    @Column(name = "user_foundation_inspire_rate")
    private String userFoundationInspireRate;

}