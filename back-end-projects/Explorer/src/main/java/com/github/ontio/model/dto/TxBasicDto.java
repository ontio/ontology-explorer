package com.github.ontio.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/5/7
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class TxBasicDto {

    private String txHash;

    private Integer txType;

    private Integer txTime;

    private Integer confirmFlag;

}
