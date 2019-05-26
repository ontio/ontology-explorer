package com.github.ontio.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ontio.model.dao.Oep5TxDetail;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/5/7
 */
@Data
@Table(name = "tbl_oep5_tx_detail")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Oep5TxDetailDto extends Oep5TxDetail {

    //TODO 云斗龙特殊字段，后续oep5最好统一规范
    private String jsonUrl;

    @Builder
    public Oep5TxDetailDto(String txHash, Integer txIndex, Integer txType, Integer txTime, Integer blockHeight, BigDecimal amount, BigDecimal fee, String assetName, String fromAddress, String toAddress, String description, Integer blockIndex, Integer confirmFlag, Integer eventType, String contractHash, String payer, String calledContractHash, String jsonUrl) {
        super(txHash, txIndex, txType, txTime, blockHeight, amount, fee, assetName, fromAddress, toAddress, description, blockIndex, confirmFlag, eventType, contractHash, payer, calledContractHash);
        this.jsonUrl = jsonUrl;
    }
}
