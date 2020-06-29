package com.github.ontio.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author LiuQi
 */
@Getter
@Setter
public class GovernanceInfo {

	@JsonProperty("peer_pub_key")
	private String peerPubKey;

	private String address;

	@JsonProperty("consensus_pos")
	private Long consensusPos;

	@JsonProperty("candidate_pos")
	private Long candidatePos;

	@JsonProperty("new_pos")
	private Long newPos;

	@JsonProperty("withdraw_consensus_pos")
	private Long withdrawConsensusPos;

	@JsonProperty("withdraw_candidate_pos")
	private Long withdrawCandidatePos;

	@JsonProperty("withdraw_unfreeze_pos")
	private Long withdrawUnfreezePos;

}
