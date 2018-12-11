package com.github.ontio.model;

import java.math.BigDecimal;

public class Contracts {
    private String contract;

    private String name;

    private Integer txcount;

    private String abi;

    private String code;

    private Integer createtime;

    private Integer auditflag;

    private Integer updatetime;

    private String contactinfo;

    private String type;

    private String description;

    private String logo;

    private String creator;

    private Integer addresscount;

    private BigDecimal ontcount;

    private BigDecimal ongcount;

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract == null ? null : contract.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getTxcount() {
        return txcount;
    }

    public void setTxcount(Integer txcount) {
        this.txcount = txcount;
    }

    public String getAbi() {
        return abi;
    }

    public void setAbi(String abi) {
        this.abi = abi == null ? null : abi.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Integer getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Integer createtime) {
        this.createtime = createtime;
    }

    public Integer getAuditflag() {
        return auditflag;
    }

    public void setAuditflag(Integer auditflag) {
        this.auditflag = auditflag;
    }

    public Integer getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Integer updatetime) {
        this.updatetime = updatetime;
    }

    public String getContactinfo() {
        return contactinfo;
    }

    public void setContactinfo(String contactinfo) {
        this.contactinfo = contactinfo == null ? null : contactinfo.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo == null ? null : logo.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getAddresscount() {
        return addresscount;
    }

    public void setAddresscount(Integer addresscount) {
        this.addresscount = addresscount;
    }

    public BigDecimal getOntcount() {
        return ontcount;
    }

    public void setOntcount(BigDecimal ontcount) {
        this.ontcount = ontcount;
    }

    public BigDecimal getOngcount() {
        return ongcount;
    }

    public void setOngcount(BigDecimal ongcount) {
        this.ongcount = ongcount;
    }
}