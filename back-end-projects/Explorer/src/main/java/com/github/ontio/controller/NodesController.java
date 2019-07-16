package com.github.ontio.controller;

import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.model.dao.NodeInfoOffChain;
import com.github.ontio.model.dao.NodeInfoOnChain;
import com.github.ontio.service.impl.NodesServiceImpl;
import com.github.ontio.util.ErrorInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), nodeInfoList);
    }

    @ApiOperation(value = "Get candidate node information by public key")
    @GetMapping(value = "/current-stake/{public_key}")
    public ResponseBean getCurrentStake(@PathVariable("public_key") String publicKey) {
        NodeInfoOnChain nodeInfoList = nodesService.getCurrentOnChainInfo(publicKey);
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), nodeInfoList);
    }

    @ApiOperation(value = "Get nodes register information")
    @GetMapping(value = "/off-chain-infos")
    public ResponseBean getOffChainInfo() {
        List<NodeInfoOffChain> nodeInfoList = nodesService.getCurrentOffChainInfo();
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), nodeInfoList);
    }

    @ApiOperation(value = "Get node register information by public key")
    @GetMapping(value = "/off-chain-info/{public_key}")
    public ResponseBean getOffChainInfoByPublicKey(@PathVariable("public_key") String publicKey) {
        NodeInfoOffChain nodeInfoList = nodesService.getCurrentOffChainInfo(publicKey);
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), nodeInfoList);
    }
}
