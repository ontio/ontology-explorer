package com.github.ontio.model.dao;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tbl_node_authorize_info")
@Data
public class NodeAuthorizeInfo {
    /**
     * 交易hash
     */
    @Id
    @Column(name = "public_key")
    @JSONField(name = "peerPubkey")
    private String publicKey;

    /**
     * 质押地址
     */
    @Id
    private String address;

    @Column(name = "consensus_pos")
    private Integer consensusPos = 0;

    @Column(name = "freeze_pos")
    private Integer freezePos = 0;

    @Column(name = "new_pos")
    private Integer newPos = 0;

    @Column(name = "withdraw_pos")
    private Integer withdrawPos = 0;

    @Column(name = "withdraw_freeze_pos")
    private Integer withdrawFreezePos = 0;

    @Column(name = "withdraw_unfreeze_pos")
    private Integer withdrawUnfreezePos = 0;

    /**
     * 状态:0-已无记录,1-有记录
     */
    private Integer status;

    @Column(name = "update_time")
    private Date updateTime;
}