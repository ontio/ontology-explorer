package com.github.ontio.model.dao;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.ontio.util.TxDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_user")
public class User {
    /**
     * ONT ID
     */
    @Id
    @Column(name = "ont_id")
    @GeneratedValue(generator = "JDBC")
    private String ontId;

    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 邮箱
     */
    @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", message = "email format error")
    private String email;

    /**
     * 创建时间
     */
    @JsonSerialize(using = TxDateSerializer.class)
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 上次登录时间
     */
    @JsonSerialize(using = TxDateSerializer.class)
    @Column(name = "last_login_time")
    private Date lastLoginTime;

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
     * 获取用户名
     *
     * @return user_name - 用户名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置用户名
     *
     * @param userName 用户名
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * 获取邮箱
     *
     * @return email - 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取上次登录时间
     *
     * @return last_login_time - 上次登录时间
     */
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * 设置上次登录时间
     *
     * @param lastLoginTime 上次登录时间
     */
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}