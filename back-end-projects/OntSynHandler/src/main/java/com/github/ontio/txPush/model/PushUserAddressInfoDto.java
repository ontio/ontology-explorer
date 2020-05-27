package com.github.ontio.txPush.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author zhouq
 * @version 1.0
 * @date 2020/3/26
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushUserAddressInfoDto {

    private String address;

    private String ontId;

    private String userName;

    private String email;

    private String note;

    private Integer strategy;

    private Boolean includeOepToken;

    private BigDecimal amountThreshold;

    private String channel;

}
