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

    public Contract(String contractHash, String name, Integer createTime, Integer updateTime, Integer auditFlag, String contactInfo, String description, String type, String logo, String creator, Integer addressCount, Integer txCount, BigDecimal ontSum, BigDecimal ongSum, String tokenSum, String category, String dappName, Integer dappstoreFlag, BigDecimal totalReward, BigDecimal lastweekReward) {
        this.contractHash = contractHash;
        this.name = name;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.auditFlag = auditFlag;
        this.contactInfo = contactInfo;
        this.description = description;
        this.type = type;
        this.logo = logo;
        this.creator = creator;
        this.addressCount = addressCount;
        this.txCount = txCount;
        this.ontSum = ontSum;
        this.ongSum = ongSum;
        this.tokenSum = tokenSum;
        this.category = category;
        this.dappName = dappName;
        this.dappstoreFlag = dappstoreFlag;
        this.totalReward = totalReward;
        this.lastweekReward = lastweekReward;
    }

    public String getContractHash() {
        return contractHash;
    }

    public String getName() {
        return name;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public Integer getAuditFlag() {
        return auditFlag;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getLogo() {
        return logo;
    }

    public String getCreator() {
        return creator;
    }

    public Integer getAddressCount() {
        return addressCount;
    }

    public Integer getTxCount() {
        return txCount;
    }

    public BigDecimal getOntSum() {
        return ontSum;
    }

    public BigDecimal getOngSum() {
        return ongSum;
    }

    public String getTokenSum() {
        return tokenSum;
    }

    public String getCategory() {
        return category;
    }

    public String getDappName() {
        return dappName;
    }

    public Integer getDappstoreFlag() {
        return dappstoreFlag;
    }

    public BigDecimal getTotalReward() {
        return totalReward;
    }

    public BigDecimal getLastweekReward() {
        return lastweekReward;
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