package com.github.ontio.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenInfoDto {
    private String symbol;

    private String currencyCode;

    private BigDecimal price;

    private BigDecimal marketCap;

    private BigDecimal accTradePrice24h;

    private long circulatingSupply;

    private long maxSupply;

    private String provider = "Ontology Foundation";

    private long lastUpdatedTimestamp = System.currentTimeMillis();
}
