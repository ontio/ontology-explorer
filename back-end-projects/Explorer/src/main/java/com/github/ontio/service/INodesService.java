/*
 * Copyright (C) 2018 The ontology Authors
 * This file is part of The ontology library.
 *
 * The ontology is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The ontology is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with The ontology.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.ontio.service;

import com.github.ontio.model.common.PageResponseBean;
import com.github.ontio.model.dao.NetNodeInfo;
import com.github.ontio.model.dao.NodeBonus;
import com.github.ontio.model.dao.NodeInfoOffChain;
import com.github.ontio.model.dao.NodeInfoOnChain;
import com.github.ontio.model.dao.NodeInfoOnChainWithBonus;
import com.github.ontio.model.dao.NodeInfoOnChainWithRankChange;
import com.github.ontio.model.dao.NodeRankChange;
import com.github.ontio.model.dao.NodeRankHistory;
import com.alibaba.fastjson.JSONObject;
import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.model.dao.*;
import com.github.ontio.model.dto.*;
import com.github.ontio.sdk.exception.SDKException;

import java.util.List;

public interface INodesService {

    List<NodeInfoOnChainWithRankChange> getCurrentOnChainInfo();

    NodeInfoOffChain getCurrentOffChainInfo(String publicKey, Integer openFlag);

    NodeInfoOffChain getCurrentOffChainInfoPublic(String publicKey, Integer openFlag);

    ResponseBean updateOffChainInfoByPublicKey(UpdateOffChainNodeInfoDto nodeInfoOffChainDto) throws Exception;

    List<NodeInfoOffChain> getCurrentOffChainInfo();

    NodeInfoOnChain getCurrentOnChainInfo(String publicKey);

    List<NodeBonus> getNodeBonusHistories();

    NodeBonus getLatestBonusByPublicKey(String publicKey);

    NodeBonus getLatestBonusByAddress(String address);

    List<NodeInfoOnChainWithBonus> getLatestBonusesWithInfos();

    List<NodeInfoOnChainWithBonus> searchNodeOnChainWithBonusByName(String name);

    List<NetNodeInfo> getActiveNetNodes();

    List<NetNodeInfo> getAllNodes();

    long getSyncNodeCount();

    long getCandidateNodeCount();

    long getConsensusNodeCount();

    long getSyncNodesCount();

    List<NodeInfoOffChain> getCurrentCandidateOffChainInfo();

    List<NodeInfoOffChain> getCurrentConsensusOffChainInfo();

    List<NodeRankChange> getNodeRankChange(boolean isDesc);

    List<NodeRankHistory> getRecentNodeRankHistory();

    PageResponseBean getGovernanceInfo(String pubKey, Integer pageNum, Integer pageSize);


    JSONObject getRndHistory(int pageSize, int pageNumber);

    PageResponseBean getNodesInspire(Integer pageNum, Integer pageSize);

    NodeInspire getNodesInspireByPublicKey(String publicKey);

    CalculationInspireInfoDto getCalculationNodeIncentivesInfo();

    InspireResultDto calculationNodeIncentives(NodeInspireCalculationDto dto);

    InspireResultDto calculationUserIncentives(UserInspireCalculationDto dto) throws SDKException;

    List<NodeCycle> getNodeCycleData();

    List<NodeCycle> getNodeCycleByPubKey(String publicKey);


}
