package com.github.ontio.model.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_user_address")
public class UserAddress {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * ONT ID
     */
    @Column(name = "ont_id")
    private String ontId;

    /**
     * 地址
     */
    @NotEmpty
    @Pattern(regexp = "A[A-Za-z0-9]{33}")
    private String address;

    /**
     * 地址备注
     */
    private String note;

    /**
     * 监听策略。0:不监听 1:监听所有入金出金，2:只监听入金 3:只监听出金
     */
    @Min(0)
    @Max(3)
    private Integer strategy;

    /**
     * 1:监听oep资产  0:不监听oep资产
     */
    @Column(name = "include_oep_token")
    private Boolean includeOepToken;

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
     * 获取ONT ID
     *
     * @return ont_id - ONT ID
     */
    public String getOntId() {
        return ontId;
    }

    /**
     * 设置ONT ID
     *
     * @param ontId ONT ID
     */
    public void setOntId(String ontId) {
        this.ontId = ontId == null ? null : ontId.trim();
    }

    /**
     * 获取地址
     *
     * @return address - 地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置地址
     *
     * @param address 地址
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * 获取地址备注
     *
     * @return note - 地址备注
     */
    public String getNote() {
        return note;
    }

    /**
     * 设置地址备注
     *
     * @param note 地址备注
     */
    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    /**
     * 获取监听策略。0:不监听 1:监听所有入金出金，2:只监听入金 3:只监听出金
     *
     * @return strategy - 监听策略。0:不监听 1:监听所有入金出金，2:只监听入金 3:只监听出金
     */
    public Integer getStrategy() {
        return strategy;
    }

    /**
     * 设置监听策略。0:不监听 1:监听所有入金出金，2:只监听入金 3:只监听出金
     *
     * @param strategy 监听策略。0:不监听 1:监听所有入金出金，2:只监听入金 3:只监听出金
     */
    public void setStrategy(Integer strategy) {
        this.strategy = strategy;
    }

    /**
     * 获取1:监听oep资产  0:不监听oep资产
     *
     * @return include_oep_token - 1:监听oep资产  0:不监听oep资产
     */
    public Boolean getIncludeOepToken() {
        return includeOepToken;
    }

    /**
     * 设置1:监听oep资产  0:不监听oep资产
     *
     * @param includeOepToken 1:监听oep资产  0:不监听oep资产
     */
    public void setIncludeOepToken(Boolean includeOepToken) {
        this.includeOepToken = includeOepToken;
    }
}