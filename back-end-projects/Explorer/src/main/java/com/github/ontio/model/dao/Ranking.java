package com.github.ontio.model.dao;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tbl_ranking")
public class Ranking {
    /**
     * 排名分组，由业务系统决定
     */
    @Column(name = "ranking_group")
    private Short rankingGroup;

    /**
     * 具体排名，由业务系统决定
     */
    @Column(name = "ranking_id")
    private Short rankingId;

    /**
     * 排名区间，1：24小时；3：3天；7：7天
     */
    @Column(name = "ranking_duration")
    private Short rankingDuration;

    /**
     * 排名数，1 - N
     */
    private Short ranking;

    /**
     * 排名对象，根据不同的排名有不同含义
     */
    private String member;

    /**
     * 排名数值
     */
    private BigDecimal amount;

    /**
     * 占比
     */
    private BigDecimal percentage;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 获取排名分组，由业务系统决定
     *
     * @return ranking_group - 排名分组，由业务系统决定
     */
    public Short getRankingGroup() {
        return rankingGroup;
    }

    /**
     * 设置排名分组，由业务系统决定
     *
     * @param rankingGroup 排名分组，由业务系统决定
     */
    public void setRankingGroup(Short rankingGroup) {
        this.rankingGroup = rankingGroup;
    }

    /**
     * 获取具体排名，由业务系统决定
     *
     * @return ranking_id - 具体排名，由业务系统决定
     */
    public Short getRankingId() {
        return rankingId;
    }

    /**
     * 设置具体排名，由业务系统决定
     *
     * @param rankingId 具体排名，由业务系统决定
     */
    public void setRankingId(Short rankingId) {
        this.rankingId = rankingId;
    }

    /**
     * 获取排名区间，1：24小时；3：3天；7：7天
     *
     * @return ranking_duration - 排名区间，1：24小时；3：3天；7：7天
     */
    public Short getRankingDuration() {
        return rankingDuration;
    }

    /**
     * 设置排名区间，1：24小时；3：3天；7：7天
     *
     * @param rankingDuration 排名区间，1：24小时；3：3天；7：7天
     */
    public void setRankingDuration(Short rankingDuration) {
        this.rankingDuration = rankingDuration;
    }

    /**
     * 获取排名数，1 - N
     *
     * @return ranking - 排名数，1 - N
     */
    public Short getRanking() {
        return ranking;
    }

    /**
     * 设置排名数，1 - N
     *
     * @param ranking 排名数，1 - N
     */
    public void setRanking(Short ranking) {
        this.ranking = ranking;
    }

    /**
     * 获取排名对象，根据不同的排名有不同含义
     *
     * @return member - 排名对象，根据不同的排名有不同含义
     */
    public String getMember() {
        return member;
    }

    /**
     * 设置排名对象，根据不同的排名有不同含义
     *
     * @param member 排名对象，根据不同的排名有不同含义
     */
    public void setMember(String member) {
        this.member = member == null ? null : member.trim();
    }

    /**
     * 获取排名数值
     *
     * @return amount - 排名数值
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置排名数值
     *
     * @param amount 排名数值
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 获取占比
     *
     * @return percentage - 占比
     */
    public BigDecimal getPercentage() {
        return percentage;
    }

    /**
     * 设置占比
     *
     * @param percentage 占比
     */
    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}