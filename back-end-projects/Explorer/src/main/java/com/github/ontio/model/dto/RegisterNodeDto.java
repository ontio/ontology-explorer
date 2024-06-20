package com.github.ontio.model.dto;

import com.github.ontio.model.dao.NodeInfoOffChain;
import lombok.Data;

import java.util.List;

/**
 * @author lijie
 * @version 1.0
 * @date 2024/5/30
 */
@Data
public class RegisterNodeDto {

    List<NodeInfoOffChain> registerNodeList;

    private String reward = "0";
}
