package com.github.ontio.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class NodesInfoDto {
    private Integer pageNumber;
    private Integer pageSize;
    private String name;
    // nodeType; //  0 不限  1是候选节点，2是共识节点
    private Integer nodeType;
    // isStake;  // 0 不限   1 可质押  2 不可质押
    private Integer isStake;
    // tagList; // [0,1,2,3] 长期运营, 已验证联系方式   收益比例稳定 harbinger
    private List<Integer> tagList;
    // orderType; // 排序方式 0 默认根据节点排名排序  1根据收益率排序  2 根据当前质押量高的节点排序
    private Integer orderType;
}

