package com.github.ontio.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.ontio.model.common.PageResponseBean;
import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.model.dao.*;
import com.github.ontio.model.dto.UpdateOffChainNodeInfoDto;
import com.github.ontio.service.impl.ConfigServiceImpl;
import com.github.ontio.service.impl.NodesServiceImpl;
import com.github.ontio.service.impl.OntSdkServiceImpl;
import com.github.ontio.util.ErrorInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping(value = "/v2/nodes")
public class NodesController {

    private final BigDecimal totalUnStakes = new BigDecimal(655000000);

    private final NodesServiceImpl nodesService;

    private final ConfigServiceImpl configService;

    private final OntSdkServiceImpl ontSdkService;

    @Autowired
    public NodesController(NodesServiceImpl nodesService,
                           ConfigServiceImpl configService,
                           OntSdkServiceImpl ontSdkService) {
        this.nodesService = nodesService;
        this.configService = configService;
        this.ontSdkService = ontSdkService;
    }

    @ApiOperation(value = "Get block count to next round")
    @GetMapping(value = "/block-count-to-next-round")
    public ResponseBean getBlkCountToNxtRnd() {
        long blkCountToNxtRnd = nodesService.getBlkCountToNxtRnd();
        long maxStakingChangeCount = configService.getMaxStakingChangeCount();
        if (blkCountToNxtRnd < 0 || maxStakingChangeCount <= 0) {
            return new ResponseBean(ErrorInfo.INNER_ERROR.code(), ErrorInfo.INNER_ERROR.desc(), "");
        }
        JSONObject result = new JSONObject();
        result.put("count_to_next_round", blkCountToNxtRnd);
        result.put("max_staking_change_count", maxStakingChangeCount);
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), result);
    }

    @ApiOperation(value = "Get block height and time of round history")
    @GetMapping(value = "/round-history")
    public ResponseBean getRndHistory(@RequestParam("page_size") @Max(10) @Min(1) int pageSize,
                                      @RequestParam("page_number") @Min(1) int pageNumber) {
        JSONObject result = nodesService.getRndHistory(pageSize, pageNumber);
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), result);
    }

    @ApiOperation(value = "Get the number of current staking round")
    @GetMapping(value = "/current-staking-cycle")
    public ResponseBean getCurrentStakingCycle() {
        int currentStakingView = ontSdkService.getGovernanceView();
        if (currentStakingView < 0) {
            return new ResponseBean(ErrorInfo.INNER_ERROR.code(), ErrorInfo.INNER_ERROR.desc(), "");
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), currentStakingView);
    }

    @ApiOperation(value = "Get total ONT stakes and percentage total ONT stakes of total supply")
    @GetMapping(value = "/current-total-stakes")
    public ResponseBean getCurrentTotalStake() {
        long curtTotalStake = nodesService.getCurrentTotalStake();
        if (curtTotalStake < 0) {
            return new ResponseBean(ErrorInfo.INNER_ERROR.code(), ErrorInfo.INNER_ERROR.desc(), curtTotalStake);
        }
        BigDecimal percent = new BigDecimal(curtTotalStake).multiply(new BigDecimal(100)).divide(totalUnStakes, 4, RoundingMode.HALF_UP);
        JSONObject result = new JSONObject();
        result.put("current_total_stakes", curtTotalStake);
        result.put("current_stakes_percent", percent.toPlainString().concat("%"));
        result.put("total_un_stakes", totalUnStakes.toPlainString());
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), result);
    }

    @ApiOperation(value = "Get candidate nodes information")
    @GetMapping(value = "/current-stakes")
    public ResponseBean getCurrentStake() {
        List<NodeInfoOnChainWithRankChange> nodeInfoList = nodesService.getCurrentOnChainInfo();
        if (nodeInfoList.size() == 0) {
            return new ResponseBean(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), nodeInfoList);
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), nodeInfoList);
    }

    @ApiOperation(value = "Get nodes register information")
    @GetMapping(value = "/off-chain-infos")
    public ResponseBean getOffChainInfo(@RequestParam(value = "node_type", defaultValue = "-1") Integer nodeType) {
        List<NodeInfoOffChain> nodeInfoList = new ArrayList<>();
        if (nodeType.equals(-1)) {
            nodeInfoList = nodesService.getCurrentOffChainInfo();
        } else if (nodeType.equals(1)) {
            nodeInfoList = nodesService.getCurrentCandidateOffChainInfo();
        } else if (nodeType.equals(2)) {
            nodeInfoList = nodesService.getCurrentConsensusOffChainInfo();
        } else {
            return new ResponseBean(ErrorInfo.PARAM_ERROR.code(), ErrorInfo.PARAM_ERROR.desc(), nodeInfoList);
        }
        if (nodeInfoList.size() == 0) {
            return new ResponseBean(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), nodeInfoList);
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), nodeInfoList);
    }

    @ApiOperation(value = "Get candidate and consensus node information by public key")
    @GetMapping(value = "/on-chain-info")
    public ResponseBean getCurrentStakeByPublicKey(@RequestParam("public_key") @Length(min = 56, max = 128, message = "invalid public key") String publicKey) {
        NodeInfoOnChain nodeInfoList = nodesService.getCurrentOnChainInfo(publicKey);
        if (nodeInfoList == null) {
            return new ResponseBean(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), "");
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), nodeInfoList);
    }

    @ApiOperation(value = "Get open node register information by public key")
    @GetMapping(value = "/off-chain-info")
    public ResponseBean getOpenOffChainInfoByPublicKey(@RequestParam("public_key") @Length(min = 56, max = 128, message = "invalid public key") String publicKey) {
        NodeInfoOffChain nodeInfoList = nodesService.getCurrentOffChainInfo(publicKey, 1);
        if (nodeInfoList == null) {
            return new ResponseBean(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), "");
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), nodeInfoList);
    }

    @ApiOperation(value = "Get node register information by public key")
    @GetMapping(value = "/off-chain-info/public")
    public ResponseBean getOffChainInfoByPublicKey(@RequestParam("public_key") @Length(min = 56, max = 128, message = "invalid public key") String publicKey) {
        NodeInfoOffChain nodeInfoList = nodesService.getCurrentOffChainInfo(publicKey, null);
        if (nodeInfoList == null) {
            return new ResponseBean(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), "");
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), nodeInfoList);
    }

    //    @ApiOperation(value = "insert or update node register information by public key")
//    @PostMapping(value = "/off-chain-info")
    public ResponseBean updateOffChainInfoByPublicKey(@RequestBody UpdateOffChainNodeInfoDto updateOffChainNodeInfoDto) throws Exception {
        ResponseBean responseBean = nodesService.updateOffChainInfoByPublicKey(updateOffChainNodeInfoDto);
        return responseBean;
    }

    @ApiOperation(value = "Get reward per 10000 ONT stake unit")
    @GetMapping(value = "/bonus-histories")
    public ResponseBean getBonusHistories() {
        List<NodeBonus> nodeBonusList = nodesService.getNodeBonusHistories();
        if (nodeBonusList.size() != 0) {
            return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), nodeBonusList);
        }
        return new ResponseBean(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), nodeBonusList);
    }

    @ApiOperation(value = "Get latest reward per 10000 ONT stake unit by public key")
    @GetMapping(value = "/latest-bonus")
    public ResponseBean getLatestBonusByAddress(
            @RequestParam(value = "address", defaultValue = "") @Length(min = 34, max = 34, message = "invalid address") String address,
            @RequestParam(value = "public_key", defaultValue = "") @Length(min = 56, max = 128, message = "invalid public key") String publicKey) {
        NodeBonus nodeBonus;
        if (address.length() == 34) {
            nodeBonus = nodesService.getLatestBonusByAddress(address);
        } else if (publicKey.length() != 0) {
            nodeBonus = nodesService.getLatestBonusByPublicKey(publicKey);
        } else {
            return new ResponseBean(ErrorInfo.PARAM_ERROR.code(), ErrorInfo.PARAM_ERROR.desc(), "");
        }
        if (nodeBonus == null) {
            return new ResponseBean(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), "");
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), nodeBonus);
    }

    @ApiOperation(value = "Get candidate and consensus nodes information with bonus")
    @GetMapping(value = "/latest-bonuses-with-infos")
    public ResponseBean getLatestBonusesWithInfos() {
        List<NodeInfoOnChainWithBonus> nodeBonusList = nodesService.getLatestBonusesWithInfos();
        if (nodeBonusList.size() != 0) {
            return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), nodeBonusList);
        }
        return new ResponseBean(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), nodeBonusList);
    }

    @ApiOperation(value = "Get candidate nodes information by name")
    @GetMapping(value = "/latest-bonuses-with-infos/search")
    public ResponseBean searchNodeOnChainWithBonus(@RequestParam("name") @Length(min = 1, max = 100, message = "invalid name") String name) {
        List<NodeInfoOnChainWithBonus> nodeInfoOnChainWithBonus = nodesService.searchNodeOnChainWithBonusByName(name);
        if (nodeInfoOnChainWithBonus.size() == 0) {
            return new ResponseBean(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), "");
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), nodeInfoOnChainWithBonus);
    }

    @ApiOperation(value = "Get all node in network")
    @GetMapping(value = "/all-in-network")
    public ResponseBean getAllNodes() {
        List<NetNodeInfo> netNodeInfoLst = nodesService.getAllNodes();
        if (netNodeInfoLst.size() == 0) {
            return new ResponseBean(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), "");
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), netNodeInfoLst);
    }

    @ApiOperation(value = "Get active node in network")
    @GetMapping(value = "/active-in-network")
    public ResponseBean getActiveNodes() {
        List<NetNodeInfo> netNodeInfoLst = nodesService.getActiveNetNodes();
        if (netNodeInfoLst.size() == 0) {
            return new ResponseBean(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), "");
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), netNodeInfoLst);
    }

    @ApiOperation(value = "Get the count of sync nodes")
    @GetMapping(value = "/sync-node-count")
    public ResponseBean getSyncNodeCount() {
        long count = nodesService.getSyncNodeCount();
        if (count < 0) {
            return new ResponseBean(ErrorInfo.INNER_ERROR.code(), ErrorInfo.INNER_ERROR.desc(), "");
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), count);
    }

    @ApiOperation(value = "Get the count of candidate nodes")
    @GetMapping(value = "/candidate-node-count")
    public ResponseBean getCandidateNodeCount() {
        long count = nodesService.getCandidateNodeCount();
        if (count < 0) {
            return new ResponseBean(ErrorInfo.INNER_ERROR.code(), ErrorInfo.INNER_ERROR.desc(), "");
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), count);
    }

    @ApiOperation(value = "Get the count of consensus nodes")
    @GetMapping(value = "/consensus-node-count")
    public ResponseBean getConsensusNodeCount() {
        long count = nodesService.getConsensusNodeCount();
        if (count < 0) {
            return new ResponseBean(ErrorInfo.INNER_ERROR.code(), ErrorInfo.INNER_ERROR.desc(), "");
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), count);
    }

    @ApiOperation(value = "Get the count of nodes")
    @GetMapping(value = "/count")
    public ResponseBean getNodeCount() {
        long syncNodeCount = nodesService.getSyncNodeCount();
        long consensusNodeCount = nodesService.getConsensusNodeCount();
        long candidateNodeCount = nodesService.getCandidateNodeCount();
        if (syncNodeCount < 0 || consensusNodeCount < 0 || candidateNodeCount < 0) {
            return new ResponseBean(ErrorInfo.INNER_ERROR.code(), ErrorInfo.INNER_ERROR.desc(), "");
        }
        long count = syncNodeCount + consensusNodeCount + candidateNodeCount;
        JSONObject result = new JSONObject();
        result.put("sync_node_count", syncNodeCount);
        result.put("consensus_node_count", consensusNodeCount);
        result.put("candidate_node_count", candidateNodeCount);
        result.put("total_node_count", count);
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), result);
    }

    @ApiOperation(value = "Get the nodes rank change info")
    @GetMapping(value = "/rank-change")
    public ResponseBean getNodeRankChange(@RequestParam(value = "is-desc", defaultValue = "true") Boolean isDesc) {
        List<NodeRankChange> nodePositionChangeList = nodesService.getNodeRankChange(isDesc);
        if (nodePositionChangeList.size() == 0) {
            return new ResponseBean(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), new ArrayList<>());
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), nodePositionChangeList);
    }

    @ApiOperation(value = "Get node rank in history")
    @GetMapping(value = "/ranks/history")
    public ResponseBean getNodeRankHistory() {
        List<NodeRankHistory> nodePositionChangeList = nodesService.getRecentNodeRankHistory();
        if (nodePositionChangeList.size() == 0) {
            return new ResponseBean(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), new ArrayList<>());
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), nodePositionChangeList);
    }

    @ApiOperation(value = "Get governance info by peer public key")
    @GetMapping(value = "/governance-info")
    public ResponseBean getGovernanceInfo(
            @RequestParam(value = "pk") @Pattern(regexp = "^[0-9a-f]{60,140}$", message = "Invalid public key") String publicKey,
            @RequestParam(value = "page_number") @Min(value = 1, message = "Invalid page number") Integer pageNum,
            @RequestParam(value = "page_size") @Min(value = 1, message = "Invalid page size") @Max(value = 50, message =
                    "Invalid page size") Integer pageSize
    ) {
        PageResponseBean response = nodesService.getGovernanceInfo(publicKey, pageNum, pageSize);
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), response);
    }

    @ApiOperation(value = "Get nodes inspire")
    @GetMapping(value = "/inspire/all")
    public ResponseBean getNodesInspire(
            @RequestParam(value = "page_number") @Min(value = 1, message = "Invalid page number") Integer pageNum,
            @RequestParam(value = "page_size") @Min(value = 1, message = "Invalid page size") @Max(value = 200, message =
                    "Invalid page size") Integer pageSize
    ) {
        PageResponseBean response = nodesService.getNodesInspire(pageNum, pageSize);
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), response);
    }

    @ApiOperation(value = "Get node inspire by public key")
    @GetMapping(value = "/inspire")
    public ResponseBean getNodesInspireByPublicKey(
            @RequestParam(value = "public_key") @Pattern(regexp = "^[0-9a-f]{60,140}$", message = "Invalid public key") String publicKey
    ) {
        NodeInspire response = nodesService.getNodesInspireByPublicKey(publicKey);
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), response);
    }
}
