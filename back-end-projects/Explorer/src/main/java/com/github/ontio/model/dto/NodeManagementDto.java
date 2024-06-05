package com.github.ontio.model.dto;

import lombok.Data;

/**
 * @author lijie
 * @version 1.0
 * @date 2024/5/30
 */
@Data
public class NodeManagementDto {

    private String nodeApr = "0.00%";

    private String userApr = "0.00%";

    private String feeSharingRatioNodeT = "0%";

    private String feeSharingRatioNodeT1 = "0%";

    private String feeSharingRatioNodeT2 = "0%";

    private String feeSharingRatioUserT = "0%";

    private String feeSharingRatioUserT1 = "0%";

    private String feeSharingRatioUserT2 = "0%";

    private String promiseStake = "0";

    private String nodeStake = "0";

    private String userStake = "0";

    private String totalStake = "0";

    private String cap = "0";

    private String reward = "0";

    private String withdrawableAmount;

    private String lockedAmount;

    private int currentRound;
}
