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

    @Column(name = "init_pos")
    private Long initPos;

    @Column(name = "total_pos")
    private Long totalPos;

    @Column(name = "node_released_ong_incentive")
    private Long nodeReleasedOngIncentive;

    @Column(name = "node_released_ong_incentive_rate")
    private String nodeReleasedOngIncentiveRate;

    @Column(name = "user_released_ong_incentive")
    private Long userReleasedOngIncentive;

    @Column(name = "user_released_ong_incentive_rate")
    private String userReleasedOngIncentiveRate;

    @Column(name = "node_gas_fee_incentive")
    private Long nodeGasFeeIncentive;

    @Column(name = "node_gas_fee_incentive_rate")
    private String nodeGasFeeIncentiveRate;

    @Column(name = "user_gas_fee_incentive")
    private Long userGasFeeIncentive;

    @Column(name = "user_gas_fee_incentive_rate")
    private String userGasFeeIncentiveRate;

    @Column(name = "node_foundation_bonus_incentive")
    private Long nodeFoundationBonusIncentive;

    @Column(name = "node_foundation_bonus_incentive_rate")
    private String nodeFoundationBonusIncentiveRate;

    @Column(name = "user_foundation_bonus_incentive")
    private Long userFoundationBonusIncentive;

    @Column(name = "user_foundation_bonus_incentive_rate")
    private String userFoundationBonusIncentiveRate;

    @Column(name = "node_apy")
    private String nodeApy;

    @Column(name = "user_apy")
    private String userApy;
}