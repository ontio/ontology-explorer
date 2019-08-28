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

package com.github.ontio.service.impl;

import com.github.ontio.config.ParamsConfig;
import com.github.ontio.mapper.*;
import com.github.ontio.model.dao.*;
import com.github.ontio.model.dto.NodeInfoOnChainDto;
import com.github.ontio.service.INodesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("NodesService")
public class NodesServiceImpl implements INodesService {

    private final ParamsConfig paramsConfig;

    private final NodeBonusMapper nodeBonusMapper;

    private final NetNodeInfoMapper netNodeInfoMapper;

    private final NodeOverviewMapper nodeOverviewMapper;

    private final NodeRankChangeMapper nodeRankChangeMapper;

    private final NodeRankHistoryMapper nodeRankHistoryMapper;

    private final NodeInfoOnChainMapper nodeInfoOnChainMapper;

    private final NodeInfoOffChainMapper nodeInfoOffChainMapper;

    @Autowired
    public NodesServiceImpl(ParamsConfig paramsConfig,
                            NodeBonusMapper nodeBonusMapper,
                            NetNodeInfoMapper netNodeInfoMapper,
                            NodeOverviewMapper nodeOverviewMapper,
                            NodeRankChangeMapper nodeRankChangeMapper,
                            NodeInfoOnChainMapper nodeInfoOnChainMapper,
                            NodeRankHistoryMapper nodeRankHistoryMapper,
                            NodeInfoOffChainMapper nodeInfoOffChainMapper) {
        this.paramsConfig = paramsConfig;
        this.nodeBonusMapper = nodeBonusMapper;
        this.netNodeInfoMapper = netNodeInfoMapper;
        this.nodeOverviewMapper = nodeOverviewMapper;
        this.nodeRankChangeMapper = nodeRankChangeMapper;
        this.nodeInfoOnChainMapper = nodeInfoOnChainMapper;
        this.nodeRankHistoryMapper = nodeRankHistoryMapper;
        this.nodeInfoOffChainMapper = nodeInfoOffChainMapper;
    }

    public long getBlkCountToNxtRnd() {
        try {
            return nodeOverviewMapper.selectBlkCountToNxtRnd();
        } catch (Exception e) {
            log.warn("Getting block count to next round failed: {}", e.getMessage());
            return -1;
        }
    }

    @Override
    public List<NodeInfoOnChainDto> getCurrentOnChainInfo() {
        try {
            return nodeInfoOnChainMapper.selectAllInfo();
        } catch (Exception e) {
            log.warn("Select node infos in chain failed: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public long getCurrentTotalStake() {
        try {
            return nodeInfoOnChainMapper.selectTotalStake();
        } catch (Exception e) {
            log.warn("Selecting current total stake failed: {}", e.getMessage());
            return -1;
        }
    }

    @Override
    public NodeInfoOnChain getCurrentOnChainInfo(String publicKey) {
        try {
            return nodeInfoOnChainMapper.selectByPublicKey(publicKey);
        } catch (Exception e) {
            log.warn("Select node off chain info by public key {} failed: {}", publicKey, e.getMessage());
            return new NodeInfoOnChain();
        }
    }

    @Override
    public List<NodeInfoOffChain> getCurrentOffChainInfo() {
        try {
            return nodeInfoOffChainMapper.selectAll();
        } catch (Exception e) {
            log.error("Select node infos off chain failed: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<NodeInfoOffChain> getCurrentCandidateOffChainInfo() {
        try {
            return nodeInfoOffChainMapper.selectAllCandidateNodeInfo();
        } catch (Exception e) {
            log.error("Select candidate node infos off chain failed: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<NodeInfoOffChain> getCurrentConsensusOffChainInfo() {
        try {
            return nodeInfoOffChainMapper.selectAllConsensusNodeInfo();
        } catch (Exception e) {
            log.error("Select consensus node infos off chain failed: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public NodeInfoOffChain getCurrentOffChainInfo(String publicKey) {
        try {
            return nodeInfoOffChainMapper.selectByPublicKey(publicKey);
        } catch (Exception e) {
            log.warn("Select node off chain info by public key {} failed: {}", publicKey, e.getMessage());
            return new NodeInfoOffChain();
        }
    }

    @Override
    public List<NodeBonus> getNodeBonusHistories() {
        try {
            int nodeCount = nodeBonusMapper.selectNodeCount();
            return nodeBonusMapper.selectLatestNodeBonusList(nodeCount);
        } catch (Exception e) {
            log.warn("Select node bonus histories failed: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public NodeBonus getLatestBonusByPublicKey(String publicKey) {
        try {
            return nodeBonusMapper.selectLatestNodeBonusByPublicKey(publicKey);
        } catch (Exception e) {
            log.warn("Select latest node bonus by public key {} failed: {}", publicKey, e.getMessage());
            return new NodeBonus();
        }
    }

    @Override
    public NodeBonus getLatestBonusByAddress(String address) {
        try {
            return nodeBonusMapper.selectLatestNodeBonusByAddress(address);
        } catch (Exception e) {
            log.warn("Select latest node bonus by address {} failed: {}", address, e.getMessage());
            return new NodeBonus();
        }
    }

    @Override
    public List<NodeInfoOnChainWithBonus> getLatestBonusesWithInfos() {
        List<NodeInfoOnChainDto> nodeInfoOnChainLst;
        List<NodeBonus> nodeBonusLst;
        try {
            nodeInfoOnChainLst = nodeInfoOnChainMapper.selectAllInfo();
            int nodeCount = nodeBonusMapper.selectNodeCount();
            nodeBonusLst = nodeBonusMapper.selectLatestNodeBonusList(nodeCount);
        } catch (Exception e) {
            log.error("Get latest bonuses with infos: {}", e.getMessage());
            return new ArrayList<>();
        }
        return generateNodeInfoOnChainWithBonusList(nodeInfoOnChainLst, nodeBonusLst);
    }

    @Override
    public List<NodeInfoOnChainWithBonus> searchNodeOnChainWithBonusByName(String name) {
        List<NodeInfoOnChainDto> nodeInfoOnChainLst;
        try {
            nodeInfoOnChainLst = nodeInfoOnChainMapper.searchByName(name);
        } catch (Exception e) {
            log.warn("Search node info on chain with name {} failed: {}", name, e.getMessage());
            return new ArrayList<>();
        }
        List<NodeBonus> nodeBonusLst;
        try {
            nodeBonusLst = nodeBonusMapper.searchLatestNodeBonusListByName(name);
        } catch (Exception e) {
            log.warn("Search node bonus with name {} failed: {}", name, e.getMessage());
            return new ArrayList<>();
        }
        return generateNodeInfoOnChainWithBonusList(nodeInfoOnChainLst, nodeBonusLst);
    }

    private List<NodeInfoOnChainWithBonus> generateNodeInfoOnChainWithBonusList(List<NodeInfoOnChainDto> nodeInfoOnChainLst,
                                                                                List<NodeBonus> nodeBonusLst) {
        List<NodeInfoOnChainWithBonus> nodeInfoOnChainWithBonuses = new ArrayList<>();
        for (NodeInfoOnChainDto nodeInfoOnChain : nodeInfoOnChainLst) {
            String publicKey = nodeInfoOnChain.getPublicKey();
            boolean isMatch = false;
            for (int i = 0; i < nodeBonusLst.size(); i++) {
                NodeBonus nodeBonus = nodeBonusLst.get(i);
                if (nodeBonus.getPublicKey().equals(publicKey)) {
                    isMatch = true;
                    nodeInfoOnChainWithBonuses.add(new NodeInfoOnChainWithBonus(nodeInfoOnChain, nodeBonus));
                    nodeBonusLst.remove(i);
                    break;
                }
            }
            if (!isMatch) {
                nodeInfoOnChainWithBonuses.add(new NodeInfoOnChainWithBonus(nodeInfoOnChain));
            }
        }
        return nodeInfoOnChainWithBonuses;
    }

    @Override
    public List<NetNodeInfo> getActiveNetNodes() {
        try {
            return netNodeInfoMapper.selectAllActiveNodes();
        } catch (Exception e) {
            log.warn("Selecting all active nodes failed {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<NetNodeInfo> getAllNodes() {
        try {
            return netNodeInfoMapper.selectAllNodes();
        } catch (Exception e) {
            log.warn("Selecting all nodes failed: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public long getSyncNodeCount() {
        try {
            return netNodeInfoMapper.selectSyncNodeCount();
        } catch (Exception e) {
            log.warn("Selecting sync node count failed: {}", e.getMessage());
            return -1;
        }
    }

    @Override
    public long getCandidateNodeCount() {
        try {
            return nodeInfoOnChainMapper.selectCandidateNodeCount();
        } catch (Exception e) {
            log.warn("Selecting candidate node count failed: {}", e.getMessage());
            return -1;
        }
    }

    @Override
    public long getSyncNodesCount() {
        try {
            return nodeInfoOnChainMapper.selectSyncNodesCount();
        } catch (Exception e) {
            log.warn("Selecting consensus node count failed: {}", e.getMessage());
            return -1;
        }
    }

    @Override
    public long getConsensusNodeCount() {
        try {
            return nodeInfoOnChainMapper.selectConsensusNodeCount();
        } catch (Exception e) {
            log.warn("Selecting consensus node count failed: {}", e.getMessage());
            return -1;
        }
    }

    @Override
    public List<NodeRankChange> getNodeRankChange(boolean isDesc) {
        try {
            if (isDesc) {
                return nodeRankChangeMapper.selectAllChangeInfoInDesc();
            }
            return nodeRankChangeMapper.selectAllChangeInfoInAsc();
        } catch (Exception e) {
            log.warn("Selecting node rank change info failed: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<NodeRankHistory> getRecentNodeRankHistory() {
        try {
            long currentRoundBlockHeight = nodeRankHistoryMapper.selectCurrentRoundBlockHeight();
            long firstRoundBlockHeight = currentRoundBlockHeight - 3 * paramsConfig.newStakingRoundBlockCount;
            return nodeRankHistoryMapper.selectRecentNodeRankHistory(firstRoundBlockHeight);
        } catch (Exception e) {
            log.warn("Selecting node rank change info failed: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

}
