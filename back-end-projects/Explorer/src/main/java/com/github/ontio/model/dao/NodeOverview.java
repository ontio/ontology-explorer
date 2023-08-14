package com.github.ontio.model.dao;

import javax.persistence.*;

@Table(name = "tbl_node_overview")
public class NodeOverview {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @Column(name = "blk_cnt_to_nxt_rnd")
    private Long blkCntToNxtRnd;

    @Column(name = "left_time_to_next_rnd")
    private Integer leftTimeToNextRnd;

    @Column(name = "rnd_start_time")
    private Integer rndStartTime;

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
     * @return blk_cnt_to_nxt_rnd
     */
    public Long getBlkCntToNxtRnd() {
        return blkCntToNxtRnd;
    }

    /**
     * @param blkCntToNxtRnd
     */
    public void setBlkCntToNxtRnd(Long blkCntToNxtRnd) {
        this.blkCntToNxtRnd = blkCntToNxtRnd;
    }

    public Integer getLeftTimeToNextRnd() {
        return leftTimeToNextRnd;
    }

    public void setLeftTimeToNextRnd(Integer leftTimeToNextRnd) {
        this.leftTimeToNextRnd = leftTimeToNextRnd;
    }

    public Integer getRndStartTime() {
        return rndStartTime;
    }

    public void setRndStartTime(Integer rndStartTime) {
        this.rndStartTime = rndStartTime;
    }
}