package com.github.ontio.model.dao;

import javax.persistence.*;

@Table(name = "tbl_address_daily_summary")
public class AddressDailySummary {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 当天UTC0点时间戳
     */
    private Integer time;

    /**
     * 合约hash值
     */
    @Column(name = "contract_hash")
    private String contractHash;

    private String address;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取当天UTC0点时间戳
     *
     * @return time - 当天UTC0点时间戳
     */
    public Integer getTime() {
        return time;
    }

    /**
     * 设置当天UTC0点时间戳
     *
     * @param time 当天UTC0点时间戳
     */
    public void setTime(Integer time) {
        this.time = time;
    }

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
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }
}