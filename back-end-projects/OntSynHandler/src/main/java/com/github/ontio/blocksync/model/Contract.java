package com.github.ontio.blocksync.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Contract implements Serializable {
    private String contractHash;

    private String name;

    private Integer createTime;

    private Integer updateTime;

    private Integer auditFlag;

    private String contactInfo;

    private String description;

    private String type;

    private String logo;

    private String creator;

    private Integer addressCount;

    private Integer txCount;

    private BigDecimal ontSum;

    private BigDecimal ongSum;

    private String tokenSum;

    private String category;

    private String dappName;

    private Integer dappstoreFlag;

    private BigDecimal totalReward;

    private BigDecimal lastweekReward;

    private static final long serialVersionUID = 1L;

    public String getContractHash() {
        return contractHash;
    }

    public Contract withContractHash(String contractHash) {
        this.setContractHash(contractHash);
        return this;
    }

    public void setContractHash(String contractHash) {
        this.contractHash = contractHash == null ? null : contractHash.trim();
    }

    public String getName() {
        return name;
    }

    public Contract withName(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public Contract withCreateTime(Integer createTime) {
        this.setCreateTime(createTime);
        return this;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public Contract withUpdateTime(Integer updateTime) {
        this.setUpdateTime(updateTime);
        return this;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getAuditFlag() {
        return auditFlag;
    }

    public Contract withAuditFlag(Integer auditFlag) {
        this.setAuditFlag(auditFlag);
        return this;
    }

    public void setAuditFlag(Integer auditFlag) {
        this.auditFlag = auditFlag;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public Contract withContactInfo(String contactInfo) {
        this.setContactInfo(contactInfo);
        return this;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo == null ? null : contactInfo.trim();
    }

    public String getDescription() {
        return description;
    }

    public Contract withDescription(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getType() {
        return type;
    }

    public Contract withType(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getLogo() {
        return logo;
    }

    public Contract withLogo(String logo) {
        this.setLogo(logo);
        return this;
    }

    public void setLogo(String logo) {
        this.logo = logo == null ? null : logo.trim();
    }

    public String getCreator() {
        return creator;
    }

    public Contract withCreator(String creator) {
        this.setCreator(creator);
        return this;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Integer getAddressCount() {
        return addressCount;
    }

    public Contract withAddressCount(Integer addressCount) {
        this.setAddressCount(addressCount);
        return this;
    }

    public void setAddressCount(Integer addressCount) {
        this.addressCount = addressCount;
    }

    public Integer getTxCount() {
        return txCount;
    }

    public Contract withTxCount(Integer txCount) {
        this.setTxCount(txCount);
        return this;
    }

    public void setTxCount(Integer txCount) {
        this.txCount = txCount;
    }

    public BigDecimal getOntSum() {
        return ontSum;
    }

    public Contract withOntSum(BigDecimal ontSum) {
        this.setOntSum(ontSum);
        return this;
    }

    public void setOntSum(BigDecimal ontSum) {
        this.ontSum = ontSum;
    }

    public BigDecimal getOngSum() {
        return ongSum;
    }

    public Contract withOngSum(BigDecimal ongSum) {
        this.setOngSum(ongSum);
        return this;
    }

    public void setOngSum(BigDecimal ongSum) {
        this.ongSum = ongSum;
    }

    public String getTokenSum() {
        return tokenSum;
    }

    public Contract withTokenSum(String tokenSum) {
        this.setTokenSum(tokenSum);
        return this;
    }

    public void setTokenSum(String tokenSum) {
        this.tokenSum = tokenSum == null ? null : tokenSum.trim();
    }

    public String getCategory() {
        return category;
    }

    public Contract withCategory(String category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public String getDappName() {
        return dappName;
    }

    public Contract withDappName(String dappName) {
        this.setDappName(dappName);
        return this;
    }

    public void setDappName(String dappName) {
        this.dappName = dappName == null ? null : dappName.trim();
    }

    public Integer getDappstoreFlag() {
        return dappstoreFlag;
    }

    public Contract withDappstoreFlag(Integer dappstoreFlag) {
        this.setDappstoreFlag(dappstoreFlag);
        return this;
    }

    public void setDappstoreFlag(Integer dappstoreFlag) {
        this.dappstoreFlag = dappstoreFlag;
    }

    public BigDecimal getTotalReward() {
        return totalReward;
    }

    public Contract withTotalReward(BigDecimal totalReward) {
        this.setTotalReward(totalReward);
        return this;
    }

    public void setTotalReward(BigDecimal totalReward) {
        this.totalReward = totalReward;
    }

    public BigDecimal getLastweekReward() {
        return lastweekReward;
    }

    public Contract withLastweekReward(BigDecimal lastweekReward) {
        this.setLastweekReward(lastweekReward);
        return this;
    }

    public void setLastweekReward(BigDecimal lastweekReward) {
        this.lastweekReward = lastweekReward;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", contractHash=").append(contractHash);
        sb.append(", name=").append(name);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", auditFlag=").append(auditFlag);
        sb.append(", contactInfo=").append(contactInfo);
        sb.append(", description=").append(description);
        sb.append(", type=").append(type);
        sb.append(", logo=").append(logo);
        sb.append(", creator=").append(creator);
        sb.append(", addressCount=").append(addressCount);
        sb.append(", txCount=").append(txCount);
        sb.append(", ontSum=").append(ontSum);
        sb.append(", ongSum=").append(ongSum);
        sb.append(", tokenSum=").append(tokenSum);
        sb.append(", category=").append(category);
        sb.append(", dappName=").append(dappName);
        sb.append(", dappstoreFlag=").append(dappstoreFlag);
        sb.append(", totalReward=").append(totalReward);
        sb.append(", lastweekReward=").append(lastweekReward);
        sb.append("]");
        return sb.toString();
    }
}