package com.github.ontio.model.dao;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tbl_decode_input_abi")
@Data
public class DecodeInputAbi {
    @Id
    @GeneratedValue(generator = "JDBC")
    private String contractHash;

    private String abi;
}