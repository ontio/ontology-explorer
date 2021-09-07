package com.github.ontio.model.dto;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ontio.model.common.VmTypeEnum;
import com.github.ontio.model.dao.TxDetail;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.web3j.abi.datatypes.Bool;

import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Table(name = "tbl_tx_detail")
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TxDetailDto extends TxDetail {

    private JSONObject detail;

    private VmTypeEnum vmType;

    private Boolean isContractHash;

    private String tagName;


    @Builder
    public TxDetailDto(String txHash, Integer txIndex, Integer txType, Integer txTime, Integer blockHeight, BigDecimal amount, BigDecimal fee, String assetName, String fromAddress, String toAddress, String description, Integer blockIndex, Integer confirmFlag, Integer eventType, String contractHash, String payer, String calledContractHash, JSONObject detail, VmTypeEnum vmType, Boolean isContractHash, String tagName) {
        super(txHash, txIndex, txType, txTime, blockHeight, amount, fee, assetName, fromAddress, toAddress, description, blockIndex, confirmFlag, eventType, contractHash, payer, calledContractHash);
        this.detail = detail;
        this.vmType = vmType;
        this.isContractHash = isContractHash;
        this.tagName = tagName ;
    }


}
