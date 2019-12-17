package com.github.ontio.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.ontio.util.TxAmountSerializer;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/4/28
 */
@Data
public class BalanceDto {

    @JsonSerialize(using = TxAmountSerializer.class)
    private BigDecimal balance;

    private String assetName;

    private String assetType;

    private String contractHash;

    @Builder
    public BalanceDto(BigDecimal balance, String assetName, String assetType, String contractHash) {
        this.balance = balance;
        this.assetName = assetName;
        this.assetType = assetType;
        this.contractHash = contractHash;
    }
}
