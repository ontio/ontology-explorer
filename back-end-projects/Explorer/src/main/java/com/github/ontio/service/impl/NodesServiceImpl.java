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
import com.github.ontio.sdk.exception.SDKException;
import com.github.ontio.service.INodesService;
import com.github.ontio.util.ConstantParam;
import com.github.ontio.util.ErrorInfo;
import com.github.ontio.util.OntologySDKService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service("NodesService")
public class NodesServiceImpl implements INodesService {

    private static final String CALCULATION_NODE = "CALCULATION_NODE";
    private static final BigDecimal RELEASE_ONG = new BigDecimal(365 * 24 * 60 * 60);

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

    private final InspireCalculationParamsMapper inspireCalculationParamsMapper;

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
                            TokenServiceImpl tokenService,
                            InspireCalculationParamsMapper inspireCalculationParamsMapper
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
        this.inspireCalculationParamsMapper = inspireCalculationParamsMapper;
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
            list.forEach(one -> {
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
    public NodeInfoOffChain getCurrentOffChainInfoPublic(String publicKey, Integer openFlag) {
        try {
            return nodeInfoOffChainMapper.selectByPublicKey(publicKey, openFlag);
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

    @Override
    public CalculationInspireInfoDto getCalculationNodeIncentivesInfo() {
        NodeInfoOnChain theLastConsensusNode = nodeInfoOnChainMapper.selectTheLastConsensusNodeInfo();
        NodeInfoOnChain the49thNode = nodeInfoOnChainMapper.selectThe49thNodeInfo();
        CalculationInspireInfoDto resp = new CalculationInspireInfoDto();
        resp.setTheLastConsensusRank(theLastConsensusNode.getNodeRank());
        resp.setTheLastConsensusStake(theLastConsensusNode.getCurrentStake());
        resp.setThe_49thNodeStake(the49thNode.getCurrentStake());
        return resp;
    }

    @Override
    public InspireResultDto calculationNodeIncentives(NodeInspireCalculationDto dto) {
        Long initPos = dto.getInitPos();
        Integer nodeType = dto.getNodeType();
        String nodeProportionStr = dto.getNodeProportion();
        String proportion = nodeProportionStr.replace("%", "");
        BigDecimal nodeProportion = new BigDecimal(proportion).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        if (initPos < 0 || BigDecimal.ZERO.compareTo(nodeProportion) == 1 || new BigDecimal(1).compareTo(nodeProportion) == -1) {
            throw new ExplorerException(ErrorInfo.PARAM_ERROR);
        }
        List<NodeInfoOnChain> nodeInfoOnChains = nodeInfoOnChainMapper.selectAll();
        if (CollectionUtils.isEmpty(nodeInfoOnChains)) {
            return null;
        }
        NodeInfoOnChain newNode = new NodeInfoOnChain();
        newNode.setPublicKey(CALCULATION_NODE);
        newNode.setInitPos(initPos);
        newNode.setTotalPos(0L);
        newNode.setStatus(nodeType);
        newNode.setCurrentStake(initPos);
        nodeInfoOnChains.add(newNode);
        nodeInfoOnChains.sort((v1, v2) -> Long.compare(v2.getInitPos() + v2.getTotalPos(), v1.getInitPos() + v1.getTotalPos()));

        List<NodeInfoOnChain> consensusNodes = new ArrayList<>();
        List<NodeInfoOnChain> candidateNodes = new ArrayList<>();
        Long top49Stake = 0L;
        int nodeIndex = 0;
        // filter consensus and candidate node
        for (int i = 0; i < nodeInfoOnChains.size(); i++) {
            NodeInfoOnChain nodeInfoOnChain = nodeInfoOnChains.get(i);
            Integer status = nodeInfoOnChain.getStatus();
            if (status.equals(2)) {
                consensusNodes.add(nodeInfoOnChain);
            } else if (status.equals(1)) {
                candidateNodes.add(nodeInfoOnChain);
            }
            if (i < 49) {
                Long currentStake = nodeInfoOnChain.getCurrentStake();
                top49Stake += currentStake;
            }
            if (CALCULATION_NODE.equals(nodeInfoOnChain.getPublicKey())) {
                nodeIndex = i;
            }
        }

        // Top 49 节点的质押总和
        BigDecimal topStake = new BigDecimal(top49Stake);

        // 第一轮
        BigDecimal first = new BigDecimal(10000000).divide(topStake, 12, BigDecimal.ROUND_HALF_UP);
        // 第二轮 数据库获取
        List<InspireCalculationParams> inspireCalculationParams = inspireCalculationParamsMapper.selectAll();
        if (CollectionUtils.isEmpty(inspireCalculationParams)) {
            return null;
        }
        InspireCalculationParams params = inspireCalculationParams.get(0);
        BigDecimal totalFpFu = params.getTotalFpFu();
        BigDecimal totalSr = params.getTotalSr();
        BigDecimal ont = params.getOntPrice();
        BigDecimal ong = params.getOngPrice();
        BigDecimal subtract = topStake.subtract(totalFpFu);
        BigDecimal second = totalSr.divide(subtract, 12, BigDecimal.ROUND_HALF_UP);

        //  候选节点的质押总和
        BigDecimal candidateTotalStake = getTotalStake(candidateNodes);

        BigDecimal consensusTotalStake = getTotalStake(consensusNodes);
        BigDecimal consensusCount = new BigDecimal(consensusNodes.size());
        //  共识节点的平均质押量
        BigDecimal consensusAverageStake = consensusTotalStake.divide(consensusCount, 12, BigDecimal.ROUND_HALF_UP);

        // A 为所有共识节点的激励系数总和
        Map<String, BigDecimal> consensusInspireMap = new HashMap<>();
        BigDecimal totalConsensusInspire = BigDecimal.ZERO;
        if (nodeType.equals(2)) {
            totalConsensusInspire = getConsensusInspire(consensusAverageStake, consensusInspireMap, consensusNodes);
        }


        // 数据库获取预测一年累积的手续费总量
        BigDecimal commission = params.getGasFee();

        // 节点的收益计算
        BigDecimal oneHundred = new BigDecimal(100);

        InspireResultDto nodeInspire = new InspireResultDto();
        BigDecimal finalReleaseOng = BigDecimal.ZERO;
        BigDecimal finalCommission = BigDecimal.ZERO;
        BigDecimal foundationInspire = BigDecimal.ZERO;

        BigDecimal currentStake = new BigDecimal(initPos);
        BigDecimal nodeStake = new BigDecimal(initPos);

        if (nodeType.equals(2)) {
            BigDecimal consensusInspire = consensusInspireMap.get(CALCULATION_NODE);
            // 共识节点手续费和释放的 ONG
            finalReleaseOng = getReleaseAndCommissionOng(consensusInspire, RELEASE_ONG, totalConsensusInspire);
            finalCommission = getReleaseAndCommissionOng(consensusInspire, commission, totalConsensusInspire);
        } else if (nodeType.equals(1)) {
            // 候选节点手续费和释放的 ONG
            finalReleaseOng = getReleaseAndCommissionOng(currentStake, RELEASE_ONG, candidateTotalStake);
            finalCommission = getReleaseAndCommissionOng(currentStake, commission, candidateTotalStake);
        }

        if (nodeIndex < 49) {
            foundationInspire = first.multiply(currentStake).multiply(new BigDecimal(1).add(second));
        }
        BigDecimal finalNodeReleaseOng = finalReleaseOng.multiply(nodeProportion);
        BigDecimal finalNodeCommission = finalCommission.multiply(nodeProportion);

        BigDecimal nodeStakeUsd = nodeStake.multiply(ont);
        BigDecimal nodeReleaseUsd = finalNodeReleaseOng.multiply(ong);
        BigDecimal nodeCommissionUsd = finalNodeCommission.multiply(ong);
        BigDecimal nodeFoundationUsd = foundationInspire.multiply(ong);


        nodeInspire.setNodeReleasedOngIncentive(finalNodeReleaseOng.setScale(12, BigDecimal.ROUND_HALF_UP).toPlainString());
        nodeInspire.setNodeGasFeeIncentive(finalNodeCommission.setScale(12, BigDecimal.ROUND_HALF_UP).toPlainString());
        nodeInspire.setNodeFoundationBonusIncentive(foundationInspire.setScale(12, BigDecimal.ROUND_HALF_UP).toPlainString());

        nodeInspire.setNodeReleasedOngIncentiveRate(nodeReleaseUsd.divide(nodeStakeUsd, 12, BigDecimal.ROUND_HALF_UP).multiply(oneHundred).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "%");
        nodeInspire.setNodeGasFeeIncentiveRate(nodeCommissionUsd.divide(nodeStakeUsd, 12, BigDecimal.ROUND_HALF_UP).multiply(oneHundred).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "%");
        nodeInspire.setNodeFoundationBonusIncentiveRate(nodeFoundationUsd.divide(nodeStakeUsd, 12, BigDecimal.ROUND_HALF_UP).multiply(oneHundred).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "%");

        return nodeInspire;
    }

    @Override
    public InspireResultDto calculationUserIncentives(UserInspireCalculationDto dto) throws SDKException {
        String[] nodeSplit = paramsConfig.FOUNDATION_NODES.split(",");
        List<String> foundationNodes = Arrays.asList(nodeSplit);
        String[] addressSplit = paramsConfig.FOUNDATION_ADDRESSES.split(",");
        List<String> foundationAddresses = Arrays.asList(addressSplit);

        Long stakeAmount = dto.getStakeAmount();
        String publicKey = dto.getPublicKey();
        if (stakeAmount < 0) {
            throw new ExplorerException(ErrorInfo.PARAM_ERROR);
        }
        NodeInfoOnChain theLastConsensusNode = nodeInfoOnChainMapper.selectTheLastConsensusNodeInfo();
        String theLastConsensusNodePublicKey = theLastConsensusNode.getPublicKey();
        Long theLastConsensusNodeStake = theLastConsensusNode.getCurrentStake();
        List<NodeInfoOnChain> nodeInfoOnChains = nodeInfoOnChainMapper.selectAll();
        if (CollectionUtils.isEmpty(nodeInfoOnChains)) {
            return null;
        }
        NodeInfoOnChain calculationNode = new NodeInfoOnChain();
        for (NodeInfoOnChain one : nodeInfoOnChains) {
            if (one.getPublicKey().equals(publicKey)) {
                Long initPos = one.getInitPos();
                Long totalPos = one.getTotalPos();
                Long maxAuthorize = one.getMaxAuthorize();
                Long allowMaxStake = maxAuthorize - totalPos;
                Long newTotalPos = (totalPos + stakeAmount) > maxAuthorize ? maxAuthorize : (totalPos + stakeAmount);
                if (stakeAmount > allowMaxStake) {
                    stakeAmount = allowMaxStake;
                }

                Long newCurrentStake = initPos + newTotalPos;
                one.setTotalPos(newTotalPos);
                one.setCurrentStake(newCurrentStake);
                // 候选节点顶掉共识的情况
                if ((one.getStatus().equals(1) && newCurrentStake > theLastConsensusNodeStake)
                        || (one.getStatus().equals(1) && newCurrentStake.equals(theLastConsensusNodeStake) && publicKey.compareTo(theLastConsensusNodePublicKey) == 1)) {

                    one.setStatus(2);
                    for (NodeInfoOnChain consensus : nodeInfoOnChains) {
                        if (consensus.getPublicKey().equals(theLastConsensusNodePublicKey)) {
                            consensus.setStatus(1);
                            break;
                        }
                    }
                }
                calculationNode = one;
                break;
            }
        }
        nodeInfoOnChains.sort((v1, v2) -> Long.compare(v2.getInitPos() + v2.getTotalPos(), v1.getInitPos() + v1.getTotalPos()));
        BigDecimal stakeAmountDecimal = new BigDecimal(stakeAmount);

        List<NodeInfoOnChain> consensusNodes = new ArrayList<>();
        List<NodeInfoOnChain> candidateNodes = new ArrayList<>();
        Long top49Stake = 0L;
        BigDecimal fuFp = BigDecimal.ZERO;
        int nodeIndex = 0;

        if (foundationNodes.contains(publicKey)) {
            initSDK();
            Long fu = 0L;
            Long fp = calculationNode.getInitPos();
            for (String address : foundationAddresses) {
                String authorizeInfo = sdk.getAuthorizeInfo(publicKey, address);
                Long consensusPos = 0L;
                if (!StringUtils.isEmpty(authorizeInfo)) {
                    consensusPos = JSONObject.parseObject(authorizeInfo).getLong("consensusPos");
                }
                fu += consensusPos;
            }
            fuFp = new BigDecimal(fp).add(new BigDecimal(fu));
        }

        // filter consensus and candidate node
        for (int i = 0; i < nodeInfoOnChains.size(); i++) {
            NodeInfoOnChain nodeInfoOnChain = nodeInfoOnChains.get(i);
            Integer status = nodeInfoOnChain.getStatus();
            if (status.equals(2)) {
                consensusNodes.add(nodeInfoOnChain);
            } else if (status.equals(1)) {
                candidateNodes.add(nodeInfoOnChain);
            }

            if (i < 49) {
                Long currentStake = nodeInfoOnChain.getCurrentStake();
                top49Stake += currentStake;
            }
            if (publicKey.equals(nodeInfoOnChain.getPublicKey())) {
                nodeIndex = i;
            }
        }

        // Top 49 节点的质押总和
        BigDecimal topStake = new BigDecimal(top49Stake);

        // 第一轮
        BigDecimal first = new BigDecimal(10000000).divide(topStake, 12, BigDecimal.ROUND_HALF_UP);
        // 第二轮 数据库获取
        List<InspireCalculationParams> inspireCalculationParams = inspireCalculationParamsMapper.selectAll();
        if (CollectionUtils.isEmpty(inspireCalculationParams)) {
            return null;
        }
        InspireCalculationParams params = inspireCalculationParams.get(0);
        BigDecimal totalFpFu = params.getTotalFpFu();
        BigDecimal totalSr = params.getTotalSr();
        BigDecimal ont = params.getOntPrice();
        BigDecimal ong = params.getOngPrice();
        BigDecimal subtract = topStake.subtract(totalFpFu);
        BigDecimal second = totalSr.divide(subtract, 12, BigDecimal.ROUND_HALF_UP);

        //  候选节点的质押总和
        BigDecimal candidateTotalStake = getTotalStake(candidateNodes);

        BigDecimal consensusTotalStake = getTotalStake(consensusNodes);
        BigDecimal consensusCount = new BigDecimal(consensusNodes.size());
        //  共识节点的平均质押量
        BigDecimal consensusAverageStake = consensusTotalStake.divide(consensusCount, 12, BigDecimal.ROUND_HALF_UP);

        // A 为所有共识节点的激励系数总和
        Map<String, BigDecimal> consensusInspireMap = new HashMap<>();
        BigDecimal totalConsensusInspire = getConsensusInspire(consensusAverageStake, consensusInspireMap, consensusNodes);


        // 数据库获取预测一年累积的手续费总量
        BigDecimal commission = params.getGasFee();

        // 节点的收益计算
        BigDecimal oneHundred = new BigDecimal(100);

        InspireResultDto nodeInspire = new InspireResultDto();
        BigDecimal finalReleaseOng = BigDecimal.ZERO;
        BigDecimal finalCommission = BigDecimal.ZERO;
        BigDecimal foundationInspire = BigDecimal.ZERO;
        BigDecimal userFoundationInspire = BigDecimal.ZERO;

        Integer status = calculationNode.getStatus();
        String proportion = calculationNode.getNodeProportion().replace("%", "");
        BigDecimal userProportion = new BigDecimal(proportion).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal nodeProportion = new BigDecimal(1).subtract(userProportion);
        BigDecimal currentStake = new BigDecimal(calculationNode.getCurrentStake());
        BigDecimal nodeStake = new BigDecimal(calculationNode.getInitPos());
        BigDecimal totalPos = new BigDecimal(calculationNode.getTotalPos());
        BigDecimal userStake;

        if (status.equals(2)) {
            BigDecimal consensusInspire = consensusInspireMap.get(publicKey);
            // 共识节点手续费和释放的 ONG
            finalReleaseOng = getReleaseAndCommissionOng(consensusInspire, RELEASE_ONG, totalConsensusInspire);
            finalCommission = getReleaseAndCommissionOng(consensusInspire, commission, totalConsensusInspire);
        } else if (status.equals(1)) {
            // 候选节点手续费和释放的 ONG
            finalReleaseOng = getReleaseAndCommissionOng(currentStake, RELEASE_ONG, candidateTotalStake);
            finalCommission = getReleaseAndCommissionOng(currentStake, commission, candidateTotalStake);
        }
        if (foundationNodes.contains(publicKey)) {
            BigDecimal fp = new BigDecimal(calculationNode.getInitPos());
            BigDecimal siSubFp = currentStake.subtract(fp);
            foundationInspire = first.multiply(siSubFp).multiply(nodeProportion);
            // 用户收益
            userStake = currentStake.subtract(fuFp);
            BigDecimal siPb = currentStake.multiply(userProportion);
            BigDecimal add = siPb.divide(siSubFp, 12, BigDecimal.ROUND_HALF_UP).add(second);
            userFoundationInspire = first.multiply(stakeAmountDecimal).multiply(add);
        } else if (nodeIndex < 49) {
            foundationInspire = first.multiply(currentStake).multiply(new BigDecimal(1).add(second));
        }
        BigDecimal finalUserReleaseOng = finalReleaseOng.multiply(userProportion).multiply(stakeAmountDecimal).divide(totalPos, 12, BigDecimal.ROUND_HALF_UP);
        BigDecimal finalUserCommission = finalCommission.multiply(userProportion).multiply(stakeAmountDecimal).divide(totalPos, 12, BigDecimal.ROUND_HALF_UP);

        BigDecimal stakeAmountUsd = stakeAmountDecimal.multiply(ont);
        BigDecimal userReleaseUsd = finalUserReleaseOng.multiply(ong);
        BigDecimal userCommissionUsd = finalUserCommission.multiply(ong);
        BigDecimal userFoundationUsd = userFoundationInspire.multiply(ong);

        nodeInspire.setUserReleasedOngIncentive(finalUserReleaseOng.setScale(12, BigDecimal.ROUND_HALF_UP).toPlainString());
        nodeInspire.setUserGasFeeIncentive(finalUserCommission.setScale(12, BigDecimal.ROUND_HALF_UP).toPlainString());
        nodeInspire.setUserFoundationBonusIncentive(userFoundationInspire.setScale(12, BigDecimal.ROUND_HALF_UP).toPlainString());

        nodeInspire.setUserReleasedOngIncentiveRate(userReleaseUsd.divide(stakeAmountUsd, 12, BigDecimal.ROUND_HALF_UP).multiply(oneHundred).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "%");
        nodeInspire.setUserGasFeeIncentiveRate(userCommissionUsd.divide(stakeAmountUsd, 12, BigDecimal.ROUND_HALF_UP).multiply(oneHundred).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "%");
        nodeInspire.setUserFoundationBonusIncentiveRate(userFoundationUsd.divide(stakeAmountUsd, 12, BigDecimal.ROUND_HALF_UP).multiply(oneHundred).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "%");

        return nodeInspire;
    }

    private BigDecimal getTotalStake(List<NodeInfoOnChain> nodes) {
        Long totalStake = 0L;
        for (NodeInfoOnChain node : nodes) {
            Long currentStake = node.getCurrentStake();
            totalStake += currentStake;
        }
        return new BigDecimal(totalStake);
    }

    private BigDecimal getConsensusInspire(BigDecimal consensusAverageStake, Map<String, BigDecimal> consensusInspireMap, List<NodeInfoOnChain> consensusNodes) {
        BigDecimal totalConsensusInspire = BigDecimal.ZERO;
        for (NodeInfoOnChain nodeInfoOnChain : consensusNodes) {
            Long currentStake = nodeInfoOnChain.getCurrentStake();
            String publicKey = nodeInfoOnChain.getPublicKey();
            BigDecimal xi = new BigDecimal(currentStake * 0.5).divide(consensusAverageStake, 12, BigDecimal.ROUND_HALF_UP);
            double pow = Math.pow(Math.E, (BigDecimal.ZERO.subtract(xi)).doubleValue());
            BigDecimal consensusInspire = xi.multiply(new BigDecimal(pow)).setScale(12, BigDecimal.ROUND_HALF_UP);
            consensusInspireMap.put(publicKey, consensusInspire);
            totalConsensusInspire = totalConsensusInspire.add(consensusInspire);
        }
        return totalConsensusInspire;
    }

    private BigDecimal getReleaseAndCommissionOng(BigDecimal value, BigDecimal ong, BigDecimal totalConsensusInspire) {
        return new BigDecimal(0.5).multiply(ong).multiply(value).divide(totalConsensusInspire, 12, BigDecimal.ROUND_HALF_UP);
    }
}
