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
@Table(name = "tbl_bad_node")
public class BadNode {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @Column(name = "public_key")
    private String publicKey;

    private String name;

    private Integer cycle;
}