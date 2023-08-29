package com.github.ontio.model.dto;

import lombok.Data;

/**
 * @author lijie
 * @version 1.0
 * @date 2019/8/6
 */
@Data
public class NodeStakeDto {

    private String nodeName;

    private String nodePubKey;

    private String amount;

    private int state;

}
