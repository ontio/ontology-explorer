package com.github.ontio.model.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "tbl_inspire_calculation_params")
@Data
public class InspireCalculationParams {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @Column(name = "total_fp_fu")
    private BigDecimal totalFpFu;

    @Column(name = "total_sr")
    private BigDecimal totalSr;

    @Column(name = "gas_fee")
    private BigDecimal gasFee;

    @Column(name = "ont_price")
    private BigDecimal ontPrice;

    @Column(name = "ong_price")
    private BigDecimal ongPrice;
}