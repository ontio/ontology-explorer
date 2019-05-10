package com.github.ontio.model.dto;

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

    private BigDecimal amount;

    private String fromAddress;

    private String toAddress;

    private String assetName;


    @Builder
    public TransferTxDetailDto(BigDecimal amount, String fromAddress, String toAddress, String assetName) {
        this.amount = amount;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.assetName = assetName;
    }
}
