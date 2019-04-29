package com.github.ontio.model.dao;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "tbl_contract")
public class Contract {
    /**
     * 合约hash值
     */
    @Id
    @Column(name = "contract_hash")
    private String contractHash;

    /**
     * 名称
     */
    private String name;

    /**
     * 创建时间戳
     */
    @Column(name = "create_time")
    private Integer createTime;

    /**
     * 更新时间戳
     */
    @Column(name = "update_time")
    private Integer updateTime;

    /**
     * 审核标识，1：审核通过 0：未审核
     */
    @Column(name = "audit_flag")
    private Boolean auditFlag;

    /**
     * 合约项目方联系信息.json格式字符串
     */
    @Column(name = "contact_info")
    private String contactInfo;

    /**
     * 合约描述
     */
    private String description;

    /**
     * 合约类型，oep4，oep5，oep8，others等
     */
    private String type;

    /**
     * 合约logo的url
     */
    private String logo;

    /**
     * 合约创建者
     */
    private String creator;

    /**
     * 该合约的总的地址数 
     */
    @Column(name = "address_count")
    private Integer addressCount;

    /**
     * 合约总的交易量
     */
    @Column(name = "tx_count")
    private Integer txCount;

    /**
     * 该合约的总的ont流通量
     */
    @Column(name = "ont_sum")
    private BigDecimal ontSum;

    /**
     * 该合约的总的ong流通量
     */
    @Column(name = "ong_sum")
    private BigDecimal ongSum;

    /**
     * 该合约的总的token流通量.json格式字符串
     */
    @Column(name = "token_sum")
    private String tokenSum;

    /**
     * 合约分类
     */
    private String category;

    /**
     * 合约所属Dapp名称
     */
    @Column(name = "dapp_name")
    private String dappName;

    /**
     * Dappstore审核标识。1：合约属于dappstore，0：合约不属于dappstore
     */
    @Column(name = "dappstore_flag")
    private Boolean dappstoreFlag;

    /**
     * 总激励
     */
    @Column(name = "total_reward")
    private BigDecimal totalReward;

    /**
     * 上周激励
     */
    @Column(name = "lastweek_reward")
    private BigDecimal lastweekReward;

    /**
     * 合约abi
     */
    private String abi;

    /**
     * 合约code
     */
    private String code;

    /**
     * 合约源码
     */
    @Column(name = "source_code")
    private String sourceCode;

    /**
     * 获取合约hash值
     *
     * @return contract_hash - 合约hash值
     */
    public String getContractHash() {
        return contractHash;
    }

    /**
     * 设置合约hash值
     *
     * @param contractHash 合约hash值
     */
    public void setContractHash(String contractHash) {
        this.contractHash = contractHash == null ? null : contractHash.trim();
    }

    /**
     * 获取名称
     *
     * @return name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取创建时间戳
     *
     * @return create_time - 创建时间戳
     */
    public Integer getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间戳
     *
     * @param createTime 创建时间戳
     */
    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间戳
     *
     * @return update_time - 更新时间戳
     */
    public Integer getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间戳
     *
     * @param updateTime 更新时间戳
     */
    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取审核标识，1：审核通过 0：未审核
     *
     * @return audit_flag - 审核标识，1：审核通过 0：未审核
     */
    public Boolean getAuditFlag() {
        return auditFlag;
    }

    /**
     * 设置审核标识，1：审核通过 0：未审核
     *
     * @param auditFlag 审核标识，1：审核通过 0：未审核
     */
    public void setAuditFlag(Boolean auditFlag) {
        this.auditFlag = auditFlag;
    }

    /**
     * 获取合约项目方联系信息.json格式字符串
     *
     * @return contact_info - 合约项目方联系信息.json格式字符串
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * 设置合约项目方联系信息.json格式字符串
     *
     * @param contactInfo 合约项目方联系信息.json格式字符串
     */
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo == null ? null : contactInfo.trim();
    }

    /**
     * 获取合约描述
     *
     * @return description - 合约描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置合约描述
     *
     * @param description 合约描述
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * 获取合约类型，oep4，oep5，oep8，others等
     *
     * @return type - 合约类型，oep4，oep5，oep8，others等
     */
    public String getType() {
        return type;
    }

    /**
     * 设置合约类型，oep4，oep5，oep8，others等
     *
     * @param type 合约类型，oep4，oep5，oep8，others等
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * 获取合约logo的url
     *
     * @return logo - 合约logo的url
     */
    public String getLogo() {
        return logo;
    }

    /**
     * 设置合约logo的url
     *
     * @param logo 合约logo的url
     */
    public void setLogo(String logo) {
        this.logo = logo == null ? null : logo.trim();
    }

    /**
     * 获取合约创建者
     *
     * @return creator - 合约创建者
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 设置合约创建者
     *
     * @param creator 合约创建者
     */
    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    /**
     * 获取该合约的总的地址数 
     *
     * @return address_count - 该合约的总的地址数 
     */
    public Integer getAddressCount() {
        return addressCount;
    }

    /**
     * 设置该合约的总的地址数 
     *
     * @param addressCount 该合约的总的地址数 
     */
    public void setAddressCount(Integer addressCount) {
        this.addressCount = addressCount;
    }

    /**
     * 获取合约总的交易量
     *
     * @return tx_count - 合约总的交易量
     */
    public Integer getTxCount() {
        return txCount;
    }

    /**
     * 设置合约总的交易量
     *
     * @param txCount 合约总的交易量
     */
    public void setTxCount(Integer txCount) {
        this.txCount = txCount;
    }

    /**
     * 获取该合约的总的ont流通量
     *
     * @return ont_sum - 该合约的总的ont流通量
     */
    public BigDecimal getOntSum() {
        return ontSum;
    }

    /**
     * 设置该合约的总的ont流通量
     *
     * @param ontSum 该合约的总的ont流通量
     */
    public void setOntSum(BigDecimal ontSum) {
        this.ontSum = ontSum;
    }

    /**
     * 获取该合约的总的ong流通量
     *
     * @return ong_sum - 该合约的总的ong流通量
     */
    public BigDecimal getOngSum() {
        return ongSum;
    }

    /**
     * 设置该合约的总的ong流通量
     *
     * @param ongSum 该合约的总的ong流通量
     */
    public void setOngSum(BigDecimal ongSum) {
        this.ongSum = ongSum;
    }

    /**
     * 获取该合约的总的token流通量.json格式字符串
     *
     * @return token_sum - 该合约的总的token流通量.json格式字符串
     */
    public String getTokenSum() {
        return tokenSum;
    }

    /**
     * 设置该合约的总的token流通量.json格式字符串
     *
     * @param tokenSum 该合约的总的token流通量.json格式字符串
     */
    public void setTokenSum(String tokenSum) {
        this.tokenSum = tokenSum == null ? null : tokenSum.trim();
    }

    /**
     * 获取合约分类
     *
     * @return category - 合约分类
     */
    public String getCategory() {
        return category;
    }

    /**
     * 设置合约分类
     *
     * @param category 合约分类
     */
    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    /**
     * 获取合约所属Dapp名称
     *
     * @return dapp_name - 合约所属Dapp名称
     */
    public String getDappName() {
        return dappName;
    }

    /**
     * 设置合约所属Dapp名称
     *
     * @param dappName 合约所属Dapp名称
     */
    public void setDappName(String dappName) {
        this.dappName = dappName == null ? null : dappName.trim();
    }

    /**
     * 获取Dappstore审核标识。1：合约属于dappstore，0：合约不属于dappstore
     *
     * @return dappstore_flag - Dappstore审核标识。1：合约属于dappstore，0：合约不属于dappstore
     */
    public Boolean getDappstoreFlag() {
        return dappstoreFlag;
    }

    /**
     * 设置Dappstore审核标识。1：合约属于dappstore，0：合约不属于dappstore
     *
     * @param dappstoreFlag Dappstore审核标识。1：合约属于dappstore，0：合约不属于dappstore
     */
    public void setDappstoreFlag(Boolean dappstoreFlag) {
        this.dappstoreFlag = dappstoreFlag;
    }

    /**
     * 获取总激励
     *
     * @return total_reward - 总激励
     */
    public BigDecimal getTotalReward() {
        return totalReward;
    }

    /**
     * 设置总激励
     *
     * @param totalReward 总激励
     */
    public void setTotalReward(BigDecimal totalReward) {
        this.totalReward = totalReward;
    }

    /**
     * 获取上周激励
     *
     * @return lastweek_reward - 上周激励
     */
    public BigDecimal getLastweekReward() {
        return lastweekReward;
    }

    /**
     * 设置上周激励
     *
     * @param lastweekReward 上周激励
     */
    public void setLastweekReward(BigDecimal lastweekReward) {
        this.lastweekReward = lastweekReward;
    }

    /**
     * 获取合约abi
     *
     * @return abi - 合约abi
     */
    public String getAbi() {
        return abi;
    }

    /**
     * 设置合约abi
     *
     * @param abi 合约abi
     */
    public void setAbi(String abi) {
        this.abi = abi == null ? null : abi.trim();
    }

    /**
     * 获取合约code
     *
     * @return code - 合约code
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置合约code
     *
     * @param code 合约code
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * 获取合约源码
     *
     * @return source_code - 合约源码
     */
    public String getSourceCode() {
        return sourceCode;
    }

    /**
     * 设置合约源码
     *
     * @param sourceCode 合约源码
     */
    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode == null ? null : sourceCode.trim();
    }
}