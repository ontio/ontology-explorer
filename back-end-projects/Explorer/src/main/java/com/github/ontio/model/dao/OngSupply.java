package com.github.ontio.model.dao;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "tbl_ong_supply")
public class OngSupply {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @Column(name = "ong_supply")
    private BigDecimal ongSupply;

    private Integer cycle;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getOngSupply() {
        return ongSupply;
    }

    public void setOngSupply(BigDecimal ongSupply) {
        this.ongSupply = ongSupply;
    }

    public Integer getCycle() {
        return cycle;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }
}