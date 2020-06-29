package com.github.ontio.model.dao;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tbl_node_overview_history")
public class NodeOverviewHistory {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @Column(name = "rnd_start_blk")
    private Long rndStartBlk;

    @Column(name = "rnd_end_blk")
    private Long rndEndBlk;

    @Column(name = "rnd_start_time")
    private Integer rndStartTime;

    @Column(name = "rnd_end_time")
    private Integer rndEndTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getRndStartBlk() {
        return rndStartBlk;
    }

    public void setRndStartBlk(Long rndStartBlk) {
        this.rndStartBlk = rndStartBlk;
    }

    public Long getRndEndBlk() {
        return rndEndBlk;
    }

    public void setRndEndBlk(Long rndEndBlk) {
        this.rndEndBlk = rndEndBlk;
    }

    public Integer getRndStartTime() {
        return rndStartTime;
    }

    public void setRndStartTime(Integer rndStartTime) {
        this.rndStartTime = rndStartTime;
    }

    public Integer getRndEndTime() {
        return rndEndTime;
    }

    public void setRndEndTime(Integer rndEndTime) {
        this.rndEndTime = rndEndTime;
    }
}