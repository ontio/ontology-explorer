package com.github.ontio.model.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_net_node_info")
public class NetNodeInfo {
    @Id
    @GeneratedValue(generator = "JDBC")
    private String ip;

    private String version;

    @Column(name = "is_consensus")
    private Boolean isConsensus;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "last_active_time")
    private Long lastActiveTime;

    private String country;

    private String longitude;

    private String latitude;

}