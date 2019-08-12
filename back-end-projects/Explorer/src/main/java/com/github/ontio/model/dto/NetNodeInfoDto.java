package com.github.ontio.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ontio.model.dao.NetNodeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;

@Data
@Table(name = "tbl_net_node_info")
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NetNodeInfoDto extends NetNodeInfo {

    public NetNodeInfoDto(String ip, String version, Boolean isConsensus, Boolean isActive, Long lastActiveTime,
                          String country, String longitude, String latitude) {
        super(ip, version, isConsensus, isActive, lastActiveTime, country, longitude, latitude);
    }
}
