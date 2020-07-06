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

import com.alibaba.fastjson.JSONObject;
import com.github.ontio.common.Helper;
import com.github.ontio.config.ParamsConfig;
import com.github.ontio.exception.ExplorerException;
import com.github.ontio.mapper.*;
import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.model.dao.*;
import com.github.ontio.model.dto.*;
import com.github.ontio.mapper.CommonMapper;
import com.github.ontio.mapper.NetNodeInfoMapper;
import com.github.ontio.mapper.NodeBonusMapper;
import com.github.ontio.mapper.NodeInfoOffChainMapper;
import com.github.ontio.mapper.NodeInfoOnChainMapper;
import com.github.ontio.mapper.NodeOverviewMapper;
import com.github.ontio.mapper.NodeRankChangeMapper;
import com.github.ontio.mapper.NodeRankHistoryMapper;
import com.github.ontio.model.common.PageResponseBean;
import com.github.ontio.model.dao.NetNodeInfo;
import com.github.ontio.model.dao.NodeBonus;
import com.github.ontio.model.dao.NodeInfoOffChain;
import com.github.ontio.model.dao.NodeInfoOnChain;
import com.github.ontio.model.dao.NodeInfoOnChainWithBonus;
import com.github.ontio.model.dao.NodeInfoOnChainWithRankChange;
import com.github.ontio.model.dao.NodeRankChange;
import com.github.ontio.model.dao.NodeRankHistory;
import com.github.ontio.service.INodesService;
import com.github.ontio.util.ConstantParam;
import com.github.ontio.util.ErrorInfo;
import com.github.ontio.util.OntologySDKService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final CommonMapper commonMapper;

    private final NodeOverviewHistoryMapper nodeOverviewHistoryMapper;

    private final NodeInspireMapper nodeInspireMapper;

    private final TokenServiceImpl tokenService;

    @Autowired
    public NodesServiceImpl(ParamsConfig paramsConfig,
                            NodeBonusMapper nodeBonusMapper,
                            NetNodeInfoMapper netNodeInfoMapper,
                            NodeOverviewMapper nodeOverviewMapper,
                            NodeRankChangeMapper nodeRankChangeMapper,
                            NodeInfoOnChainMapper nodeInfoOnChainMapper,
                            NodeRankHistoryMapper nodeRankHistoryMapper,
                            NodeInfoOffChainMapper nodeInfoOffChainMapper,
                            CommonMapper commonMapper,
                            NodeOverviewHistoryMapper nodeOverviewHistoryMapper,
                            NodeInspireMapper nodeInspireMapper,
                            TokenServiceImpl tokenService
    ) {
        this.paramsConfig = paramsConfig;
        this.nodeBonusMapper = nodeBonusMapper;
        this.netNodeInfoMapper = netNodeInfoMapper;
        this.nodeOverviewMapper = nodeOverviewMapper;
        this.nodeRankChangeMapper = nodeRankChangeMapper;
        this.nodeInfoOnChainMapper = nodeInfoOnChainMapper;
        this.nodeRankHistoryMapper = nodeRankHistoryMapper;
        this.nodeInfoOffChainMapper = nodeInfoOffChainMapper;
        this.commonMapper = commonMapper;
        this.nodeOverviewHistoryMapper = nodeOverviewHistoryMapper;
        this.nodeInspireMapper = nodeInspireMapper;
        this.tokenService = tokenService;
    }

    private OntologySDKService sdk;

    private synchronized void initSDK() {
        if (sdk == null) {
            sdk = OntologySDKService.getInstance(paramsConfig);
        }
    }

    public long getBlkCountToNxtRnd() {
        try {
            return nodeOverviewMapper.selectBlkCountToNxtRnd();
        } catch (Exception e) {
            log.warn("Getting block count to next round failed: {}", e.getMessage());
            return -1;
        }
    }

    private Map<String, NodeRankChange> getNodeRankChange() {
        List<NodeRankChange> nodeRankChangeList = nodeRankChangeMapper.selectAll();
        Map<String, NodeRankChange> nodeRankChangeMap = new HashMap<>();
        for (NodeRankChange node : nodeRankChangeList) {
            nodeRankChangeMap.put(node.getPublicKey(), node);
        }
        return nodeRankChangeMap;
    }

    @Override
    public List<NodeInfoOnChainWithRankChange> getCurrentOnChainInfo() {
        try {
            List<NodeInfoOnChainDto> nodeInfoOnChainList = nodeInfoOnChainMapper.selectAllInfo();
            Map<String, NodeRankChange> nodeRankChangeMap = getNodeRankChange();
            List<NodeInfoOnChainWithRankChange> nodeInfoOnChainWithRankChanges = new ArrayList<>();
            for (NodeInfoOnChainDto nodeInfo : nodeInfoOnChainList) {
                NodeRankChange nodeRankChange = nodeRankChangeMap.getOrDefault(nodeInfo.getPublicKey(), null);
                if (nodeRankChange == null) {
                    nodeInfoOnChainWithRankChanges.add(new NodeInfoOnChainWithRankChange(nodeInfo, 0));
                } else {
                    nodeInfoOnChainWithRankChanges.add(new NodeInfoOnChainWithRankChange(nodeInfo, nodeRankChange));
                }
            }
            return nodeInfoOnChainWithRankChanges;
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
            NodeInfoOffChainDto nodeInfoOffChainDto = NodeInfoOffChainDto.builder()
                    .build();
            List<NodeInfoOffChain> list = nodeInfoOffChainMapper.select(nodeInfoOffChainDto);
            list.forEach(one->{
                if (!one.getOpenFlag()) {
                    one.setContactMail("");
                    one.setFacebook("");
                    one.setIntroduction("");
                    one.setIp("");
                    one.setLatitude(BigDecimal.ZERO);
                    one.setLongitude(BigDecimal.ZERO);
                    one.setLogoUrl("");
                    one.setRegion("");
                    one.setSocialMedia("");
                    one.setTelegram("");
                    one.setTwitter("");
                    one.setWebsite("");
                    one.setOpenMail("");
                }
            });
            return list;
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
    public NodeInfoOffChain getCurrentOffChainInfo(String publicKey, Integer openFlag) {
        try {
            NodeInfoOffChainDto one = nodeInfoOffChainMapper.selectByPublicKey(publicKey, openFlag);
            if (!one.getOpenFlag()) {
                one.setContactMail("");
                one.setFacebook("");
                one.setIntroduction("");
                one.setIp("");
                one.setLatitude(BigDecimal.ZERO);
                one.setLongitude(BigDecimal.ZERO);
                one.setLogoUrl("");
                one.setRegion("");
                one.setSocialMedia("");
                one.setTelegram("");
                one.setTwitter("");
                one.setWebsite("");
                one.setOpenMail("");
            }
            return one;
        } catch (Exception e) {
            log.warn("Select node off chain info by public key {} failed: {}", publicKey, e.getMessage());
            return new NodeInfoOffChain();
        }
    }

    @Override
    public ResponseBean updateOffChainInfoByPublicKey(UpdateOffChainNodeInfoDto updateOffChainNodeInfoDto) throws Exception {
        String nodeInfo = updateOffChainNodeInfoDto.getNodeInfo();
        String stakePublicKey = updateOffChainNodeInfoDto.getPublicKey();
        String signature = updateOffChainNodeInfoDto.getSignature();

        byte[] nodeInfoBytes = Helper.hexToBytes(nodeInfo);
        initSDK();
        boolean verify = sdk.verifySignatureByPublicKey(stakePublicKey, nodeInfoBytes, signature);
        if (!verify) {
            return new ResponseBean(ErrorInfo.VERIFY_SIGN_FAILED.code(), ErrorInfo.VERIFY_SIGN_FAILED.desc(), "");
        }
        String nodeInfoStr = new String(nodeInfoBytes, "UTF-8");
        NodeInfoOffChain nodeInfoOffChain = JSONObject.parseObject(nodeInfoStr, NodeInfoOffChain.class);
        nodeInfoOffChain.setVerification(ConstantParam.NODE_NOT_VERIFIED);
        nodeInfoOffChain.setOntId("");
        nodeInfoOffChain.setNodeType(ConstantParam.CANDIDATE_NODE);
        String nodePublicKey = nodeInfoOffChain.getPublicKey();
        NodeInfoOffChainDto nodeInfoOffChainDto = nodeInfoOffChainMapper.selectByPublicKey(nodePublicKey, null);
        if (null == nodeInfoOffChainDto) {
            // insert
            nodeInfoOffChainMapper.insertSelective(nodeInfoOffChain);
        } else {
            // update
            nodeInfoOffChainMapper.updateByPrimaryKeySelective(nodeInfoOffChain);
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), ErrorInfo.SUCCESS.desc());
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

    @Override
    public PageResponseBean getGovernanceInfo(String pubKey, Integer pageNum, Integer pageSize) {
        int start = Math.max(pageSize * (pageNum - 1), 0);
        List<GovernanceInfoDto> result = commonMapper.findGovernanceInfo(pubKey, start, pageSize);
        int count = commonMapper.countGovernanceInfo(pubKey);
        return new PageResponseBean(result, count);
    }

    @Override
    public JSONObject getRndHistory(int pageSize, int pageNumber) {
        int start = pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1);
        List<NodeOverviewHistory> list = nodeOverviewHistoryMapper.selectNodeRndHistory(start, pageSize);
        int count = nodeOverviewHistoryMapper.selectNodeRndHistoryCount();
        JSONObject result = new JSONObject();
        result.put("rnd_history_list", list);
        result.put("count", count);
        return result;
    }

    @Override
    public PageResponseBean getNodesInspire(Integer pageNum, Integer pageSize) {
        int start = Math.max(pageSize * (pageNum - 1), 0);
        List<NodeInspire> result = nodeInspireMapper.selectNodesInspire(start, pageSize);
        int count = nodeInspireMapper.selectNodesInspireCount();
        return new PageResponseBean(result, count);
    }

    @Override
    public NodeInspire getNodesInspireByPublicKey(String publicKey) {
        NodeInspire nodeInspire = nodeInspireMapper.selectByPrimaryKey(publicKey);
        if (nodeInspire == null) {
            throw new ExplorerException(ErrorInfo.NOT_FOUND);
        }
        return nodeInspire;
    }

}
