package com.github.ontio.controller;

import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.model.dao.*;
import com.github.ontio.model.dto.NodeInfoOnChainDto;
import com.github.ontio.service.impl.NodesServiceImpl;
import com.github.ontio.util.ErrorInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/v2/nodes")
public class NodesController {

    private final NodesServiceImpl nodesService;

    @Autowired
    public NodesController(NodesServiceImpl nodesService) {
        this.nodesService = nodesService;
    }

    @ApiOperation(value = "Get block count to next round")
    @GetMapping(value = "/block-count-to-next-round")
    public ResponseBean getBlkCountToNxtRnd() {
        long blkCountToNxtRnd = nodesService.getBlkCountToNxtRnd();
        if (blkCountToNxtRnd < 0) {
            return new ResponseBean(ErrorInfo.INNER_ERROR.code(), ErrorInfo.INNER_ERROR.desc(), "");
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), blkCountToNxtRnd);
    }

    @ApiOperation(value = "Get total ONT stakes")
    @GetMapping(value = "/current-stakes")
    public ResponseBean getCurrentTotalStake() {
        long curtTotalStake = nodesService.getCurrentTotalStake();
        if (curtTotalStake < 0) {
            return new ResponseBean(ErrorInfo.INNER_ERROR.code(), ErrorInfo.INNER_ERROR.desc(), curtTotalStake);
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), curtTotalStake);
    }

    @ApiOperation(value = "Get the current percentage total ONT stakes of total supply")
    @GetMapping(value = "/current-stakes-percent")
    public ResponseBean getCurrentTotalStakePercentage() {
        long curtTotalStake = nodesService.getCurrentTotalStake();
        if (curtTotalStake < 0) {
            return new ResponseBean(ErrorInfo.INNER_ERROR.code(), ErrorInfo.INNER_ERROR.desc(), curtTotalStake);
        }
        BigDecimal percent = new BigDecimal(curtTotalStake).multiply(new BigDecimal(100)).divide(new BigDecimal(1000000000), 4, RoundingMode.HALF_UP);
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), percent.toPlainString().concat("%"));
    }

    @ApiOperation(value = "Get candidate nodes information")
    @GetMapping(value = "/on-chain-infos")
    public ResponseBean getCurrentStake() {
        List<NodeInfoOnChainDto> nodeInfoList = nodesService.getCurrentOnChainInfo();
        if (nodeInfoList.size() == 0) {
            return new ResponseBean(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), nodeInfoList);
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), nodeInfoList);
    }

    @ApiOperation(value = "Get nodes register information")
    @GetMapping(value = "/off-chain-infos")
    public ResponseBean getOffChainInfo() {
        List<NodeInfoOffChain> nodeInfoList = nodesService.getCurrentOffChainInfo();
        if (nodeInfoList.size() == 0) {
            return new ResponseBean(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), nodeInfoList);
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), nodeInfoList);
    }

    @ApiOperation(value = "Get candidate and consensus node information by public key")
    @GetMapping(value = "/on-chain-info/{public_key}")
    public ResponseBean getCurrentStakeByPublicKey(@PathVariable("public_key") @Length(min = 56, max = 128, message = "invalid public key") String publicKey) {
        NodeInfoOnChain nodeInfoList = nodesService.getCurrentOnChainInfo(publicKey);
        if (nodeInfoList == null) {
            return new ResponseBean(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), "");
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), nodeInfoList);
    }

    @ApiOperation(value = "Get node register information by public key")
    @GetMapping(value = "/off-chain-info/{public_key}")
    public ResponseBean getOffChainInfoByPublicKey(@PathVariable("public_key") @Length(min = 56, max = 128, message = "invalid public key") String publicKey) {
        NodeInfoOffChain nodeInfoList = nodesService.getCurrentOffChainInfo(publicKey);
        if (nodeInfoList == null) {
            return new ResponseBean(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), "");
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), nodeInfoList);
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
    @GetMapping(value = "/latest-bonus/{public_key}")
    public ResponseBean getLatestBonusByPublicKey(@PathVariable("public_key") @Length(min = 56, max = 128, message = "invalid public key") String publicKey) {
        NodeBonus nodeBonus = nodesService.getLatestBonusByPublicKey(publicKey);
        if (nodeBonus == null) {
            return new ResponseBean(ErrorInfo.NOT_FOUND.code(), ErrorInfo.NOT_FOUND.desc(), "");
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), nodeBonus);
    }

    @ApiOperation(value = "Get latest reward per 10000 ONT stake unit by public key")
    @GetMapping(value = "/latest-bonus/address/{address}")
    public ResponseBean getLatestBonusByAddress(@PathVariable("address") @Length(min = 34, max = 34, message = "invalid address") String address) {
        NodeBonus nodeBonus = nodesService.getLatestBonusByAddress(address);
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
    @GetMapping(value = "/latest-bonuses-with-infos/search/{name}")
    public ResponseBean searchNodeOnChainWithBonus(@PathVariable("name") @Length(min = 1, max = 100, message = "invalid name") String name) {
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
    @GetMapping(value = "/node-count")
    public ResponseBean getNodeCount() {
        long syncNodeCount = nodesService.getSyncNodeCount();
        long consensusNodeCount = nodesService.getConsensusNodeCount();
        long candidateNodeCount = nodesService.getCandidateNodeCount();
        if (syncNodeCount < 0 || consensusNodeCount < 0 || candidateNodeCount < 0) {
            return new ResponseBean(ErrorInfo.INNER_ERROR.code(), ErrorInfo.INNER_ERROR.desc(), "");
        }
        long count = syncNodeCount + consensusNodeCount + candidateNodeCount;
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), count);
    }

}
