package com.github.ontio.model.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Data
@Table(name = "tbl_contract_compile_info")
public class ContractCompileInfo {
    /**
     * 合约hash值
     */
    @Id
    @Column(name = "contract_hash")
    private String contractHash;

    private String description;

    @Column(name = "compiler_type")
    private String compilerType;

    @Column(name = "compiler_version")
    private String compilerVersion;

    @Column(name = "evm_version")
    private String evmVersion;

    @Column(name = "license_type")
    private String licenseType;

    /**
     * 是否优化
     */
    private Boolean optimization;

    /**
     * 优化步数
     */
    @Column(name = "optimization_run")
    private Integer optimizationRun;

    /**
     * 构造参数
     */
    @Column(name = "constructor_argument")
    private String constructorArgument;

    /**
     * 合约库
     */
    @Column(name = "contact_library")
    private String contractLibrary;

    /**
     * 审核标识，1：审核通过 0：未审核 2:审核未通过
     */
    @Column(name = "audit_flag")
    private Integer auditFlag;

    private String setting;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;
}