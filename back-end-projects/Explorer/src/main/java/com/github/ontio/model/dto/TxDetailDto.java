package com.github.ontio.model.dto;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ontio.model.dao.TxDetail;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/4/26
 */
@Data
@Table(name = "tbl_tx_detail")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TxDetailDto extends TxDetail {

    private JSONObject detail;

    @Builder
    public TxDetailDto(String txHash, Integer txIndex, Integer txType, Integer txTime, Integer blockHeight, BigDecimal amount, BigDecimal fee, String assetName, String fromAddress, String toAddress, String description, Integer blockIndex, Integer confirmFlag, Integer eventType, String contractHash, String payer, String calledContractHash, JSONObject detail) {
        super(txHash, txIndex, txType, txTime, blockHeight, amount, fee, assetName, fromAddress, toAddress, description, blockIndex, confirmFlag, eventType, contractHash, payer, calledContractHash);
        this.detail = detail;
    }
}
