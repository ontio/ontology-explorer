package com.github.ontio.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.ontio.util.TxAmountSerializer;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/5/9
 */
@Data
public class TransferTxDetailDto {

    @JsonSerialize(using = TxAmountSerializer.class)
    private BigDecimal amount;

    private String fromAddress;

    private String toAddress;

    private String assetName;

    private String contractHash;


    @Builder
    public TransferTxDetailDto(BigDecimal amount, String fromAddress, String toAddress, String assetName, String contractHash) {
        this.amount = amount;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.assetName = assetName;
        this.contractHash = contractHash;
    }
}
