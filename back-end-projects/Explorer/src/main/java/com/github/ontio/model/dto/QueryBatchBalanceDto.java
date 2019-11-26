package com.github.ontio.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/7/9
 */
@Data
public class QueryBatchBalanceDto {

    @JsonProperty("Action")
    private String action;

    @JsonProperty("Version")
    private String version;

    @JsonProperty("Base58Addrs")
    private String base58Addrs;

    @JsonProperty("ContractAddrs")
    private String contractAddrs;

    @Builder
    public QueryBatchBalanceDto(String action, String version, String base58Addrs, String contractAddrs) {
        this.action = action;
        this.version = version;
        this.base58Addrs = base58Addrs;
        this.contractAddrs = contractAddrs;
    }
}
