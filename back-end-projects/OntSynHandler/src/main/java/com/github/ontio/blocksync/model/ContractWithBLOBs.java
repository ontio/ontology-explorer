package com.github.ontio.blocksync.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class ContractWithBLOBs extends Contract implements Serializable {
    private String abi;

    private String code;

    private String sourceCode;

    private static final long serialVersionUID = 1L;

    public ContractWithBLOBs(String contractHash, String name, Integer createTime, Integer updateTime, Integer auditFlag, String contactInfo, String description, String type, String logo, String creator, Integer addressCount, Integer txCount, BigDecimal ontSum, BigDecimal ongSum, String tokenSum, String category, String dappName, Integer dappstoreFlag, BigDecimal totalReward, BigDecimal lastweekReward, String abi, String code, String sourceCode) {
        super(contractHash, name, createTime, updateTime, auditFlag, contactInfo, description, type, logo, creator, addressCount, txCount, ontSum, ongSum, tokenSum, category, dappName, dappstoreFlag, totalReward, lastweekReward);
        this.abi = abi;
        this.code = code;
        this.sourceCode = sourceCode;
    }

    public String getAbi() {
        return abi;
    }

    public String getCode() {
        return code;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", abi=").append(abi);
        sb.append(", code=").append(code);
        sb.append(", sourceCode=").append(sourceCode);
        sb.append("]");
        return sb.toString();
    }
}