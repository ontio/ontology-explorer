package com.github.ontio.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ontio.model.dao.Contract;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "tbl_contract")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractDto extends Contract {
    private Boolean optimization;
    private Integer optimizationRun;
    private String licenseType;
    private String setting;
    private String constructArgument;

    @Builder
    public ContractDto(String contractHash, String name, Integer createTime, Integer updateTime, Integer auditFlag, String contactInfo, String description, String type, String logo, String creator, Integer addressCount, Integer txCount, BigDecimal ontSum, BigDecimal ongSum, String tokenSum, String category, String dappName, Integer dappstoreFlag, BigDecimal totalReward, BigDecimal lastweekReward, String abi, String code, String sourceCode, String channel, String compilerType, String compilerVersion, String vmType, String vmVersion) {
        super(contractHash, name, createTime, updateTime, auditFlag, contactInfo, description, type, logo, creator, addressCount, txCount, ontSum, ongSum, tokenSum, category, dappName, dappstoreFlag, totalReward, lastweekReward, abi, code, sourceCode, channel, compilerType, compilerVersion, vmType, vmVersion);
    }
}
