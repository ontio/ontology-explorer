package com.github.ontio.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author lijie
 * @version 1.0
 * @date 2019/8/6
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NodeStakeDto {

    private String nodeName;

    private String nodePubKey;

    private String nodeWalletAddress;

    private String amount;

    private String processingAmount;

    // 1-待生效;2-质押中;3-可提取;4-取消中
    private int state;

    // 1-候选节点;2-共识节点
    private int nodeType;

    // 0-节点退出;1-节点运行中
    private int nodeState;

    private boolean allowStake;

    private long totalPos;

    private long maxAuthorize;

    private int currentRound;

    // user apr
    private String apr;

    private Integer feeSharingRatio;

    private Integer ontologyHarbinger;

    private Integer risky;

    private Integer badActor;
}
