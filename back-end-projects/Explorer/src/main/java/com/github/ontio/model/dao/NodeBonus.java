package com.github.ontio.model.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_node_bonus")
public class NodeBonus {
    @Id
    @Column(name = "public_key")
    @GeneratedValue(generator = "JDBC")
    private String publicKey;

    @Id
    @Column(name = "unix_time")
    private Long unixTime;

    private String address;

    private String name;

    private Double bonus;

}