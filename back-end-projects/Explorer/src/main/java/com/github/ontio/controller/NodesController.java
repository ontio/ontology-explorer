package com.github.ontio.controller;

import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.model.dao.NodeBonus;
import com.github.ontio.model.dao.NodeInfoOffChain;
import com.github.ontio.model.dao.NodeInfoOnChain;
import com.github.ontio.model.dao.NodeInfoOnChainWithBonus;
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

    @ApiOperation(value = "Get candidate nodes information")
    @GetMapping(value = "/current-stakes")
    public ResponseBean getCurrentStake() {
        List<NodeInfoOnChain> nodeInfoList = nodesService.getCurrentOnChainInfo();
        if (nodeInfoList.size() == 0) {
            return new ResponseBean(ErrorInfo.INNER_ERROR.code(), ErrorInfo.INNER_ERROR.desc(), nodeInfoList);
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), nodeInfoList);
    }

    @ApiOperation(value = "Get candidate and consensus node information by public key")
    @GetMapping(value = "/current-stake/{public_key}")
    public ResponseBean getCurrentStake(@PathVariable("public_key") @Length(min = 56, max = 128, message = "invalid public key") String publicKey) {
        NodeInfoOnChain nodeInfoList = nodesService.getCurrentOnChainInfo(publicKey);
        if (nodeInfoList == null) {
            return new ResponseBean(ErrorInfo.INNER_ERROR.code(), ErrorInfo.INNER_ERROR.desc(), "");
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), nodeInfoList);
    }

    @ApiOperation(value = "Get nodes register information")
    @GetMapping(value = "/off-chain-infos")
    public ResponseBean getOffChainInfo() {
        List<NodeInfoOffChain> nodeInfoList = nodesService.getCurrentOffChainInfo();
        if (nodeInfoList.size() == 0) {
            return new ResponseBean(ErrorInfo.INNER_ERROR.code(), ErrorInfo.INNER_ERROR.desc(), nodeInfoList);
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), nodeInfoList);
    }

    @ApiOperation(value = "Get node register information by public key")
    @GetMapping(value = "/off-chain-info/{public_key}")
    public ResponseBean getOffChainInfoByPublicKey(@PathVariable("public_key") @Length(min = 56, max = 128, message = "invalid public key") String publicKey) {
        NodeInfoOffChain nodeInfoList = nodesService.getCurrentOffChainInfo(publicKey);
        if (nodeInfoList == null) {
            return new ResponseBean(ErrorInfo.INNER_ERROR.code(), ErrorInfo.INNER_ERROR.desc(), "");
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
        return new ResponseBean(ErrorInfo.INNER_ERROR.code(), ErrorInfo.INNER_ERROR.desc(), nodeBonusList);
    }

    @ApiOperation(value = "Get latest reward per 10000 ONT stake unit by public key")
    @GetMapping(value = "/latest-bonus/{public_key}")
    public ResponseBean getLatestBonusByPublicKey(@PathVariable("public_key") @Length(min = 56, max = 128, message = "invalid public key") String publicKey) {
        NodeBonus nodeBonus = nodesService.getLatestBonusByPublicKey(publicKey);
        if (nodeBonus == null) {
            return new ResponseBean(ErrorInfo.INNER_ERROR.code(), ErrorInfo.INNER_ERROR.desc(), "");
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), nodeBonus);
    }

    @ApiOperation(value = "Get latest reward per 10000 ONT stake unit by public key")
    @GetMapping(value = "/latest-bonus/address/{address}")
    public ResponseBean getLatestBonusByAddress(@PathVariable("address") @Length(min = 34, max = 34, message = "invalid address") String address) {
        NodeBonus nodeBonus = nodesService.getLatestBonusByAddress(address);
        if (nodeBonus == null) {
            return new ResponseBean(ErrorInfo.INNER_ERROR.code(), ErrorInfo.INNER_ERROR.desc(), "");
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
        return new ResponseBean(ErrorInfo.INNER_ERROR.code(), ErrorInfo.INNER_ERROR.desc(), nodeBonusList);
    }

    @ApiOperation(value = "Get candidate nodes information by name")
    @GetMapping(value = "/latest-bonuses-with-infos/search/{name}")
    public ResponseBean searchNodeOnChainWithBonus(@PathVariable("name") @Length(min = 1, max = 100, message = "invalid name") String name) {
        NodeInfoOnChainWithBonus nodeInfoOnChainWithBonus = nodesService.searchNodeOnChainWithBonusByName(name);
        if (nodeInfoOnChainWithBonus == null){
            return new ResponseBean(ErrorInfo.INNER_ERROR.code(), ErrorInfo.INNER_ERROR.desc(), "");
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), nodeInfoOnChainWithBonus);
    }

}
