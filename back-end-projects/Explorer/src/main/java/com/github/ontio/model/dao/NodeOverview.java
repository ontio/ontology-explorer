package com.github.ontio.model.dao;

import javax.persistence.*;

@Table(name = "tbl_node_overview")
public class NodeOverview {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @Column(name = "blk_cnt_to_nxt_rnd")
    private Long blkCntToNxtRnd;

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
}