package com.github.ontio.model.dto;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ontio.model.common.VmTypeEnum;
import com.github.ontio.model.dao.TxDetail;
import lombok.*;

import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Table(name = "tbl_tx_detail")
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class TxDetailDto extends TxDetail {

    private JSONObject detail;

    private VmTypeEnum vmType;

    private Boolean isContractHash;

    private String tagName;

    private String assetType;


    @Builder
    public TxDetailDto(String txHash, Integer txIndex, Integer txType, Integer txTime, Integer blockHeight, BigDecimal amount, BigDecimal fee, String assetName, String fromAddress, String toAddress, String description, Integer blockIndex, Integer confirmFlag, Integer eventType, String contractHash, String payer, String calledContractHash, JSONObject detail, VmTypeEnum vmType, Boolean isContractHash, String tagName, String assetType) {
        super(txHash, txIndex, txType, txTime, blockHeight, amount, fee, assetName, fromAddress, toAddress, description, blockIndex, confirmFlag, eventType, contractHash, payer, calledContractHash);
        this.detail = detail;
        this.vmType = vmType;
        this.isContractHash = isContractHash;
        this.tagName = tagName;
        this.assetType = assetType;
    }


}
