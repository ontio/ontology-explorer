package com.github.ontio.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author LiuQi
 */
@Getter
@Setter
public class GovernanceInfoDto implements Serializable {

    private String address;

    private Long consensusPos;

    private Long candidatePos;

    private Long newPos;

    private Long withdrawConsensusPos;

    private Long withdrawCandidatePos;

    private Long withdrawUnfreezePos;

    private String publicKey;

    private String name;

}
