package com.github.ontio.blocksync.model;

import java.io.Serializable;

public class ContractWithBLOBs extends Contract implements Serializable {
    private String abi;

    private String code;

    private String sourceCode;

    private static final long serialVersionUID = 1L;

    public String getAbi() {
        return abi;
    }

    public ContractWithBLOBs withAbi(String abi) {
        this.setAbi(abi);
        return this;
    }

    public void setAbi(String abi) {
        this.abi = abi == null ? null : abi.trim();
    }

    public String getCode() {
        return code;
    }

    public ContractWithBLOBs withCode(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public ContractWithBLOBs withSourceCode(String sourceCode) {
        this.setSourceCode(sourceCode);
        return this;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode == null ? null : sourceCode.trim();
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