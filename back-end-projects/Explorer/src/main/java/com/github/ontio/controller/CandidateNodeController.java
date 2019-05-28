package com.github.ontio.controller;

import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.model.dao.CandidateNodeSummary;
import com.github.ontio.service.impl.CandidateNodeServiceImpl;
import com.github.ontio.util.ErrorInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/v2/candidate-nodes")
public class CandidateNodeController {

    private final CandidateNodeServiceImpl candidateNodeService;

    @Autowired
    public CandidateNodeController(CandidateNodeServiceImpl candidateNodeService) {
        this.candidateNodeService = candidateNodeService;
    }

    @ApiOperation(value = "Get blockchain latest summary information")
    @GetMapping(value = "/current_stake")
    public ResponseBean getBlockChainLatestInfo() {
        List<CandidateNodeSummary> nodeInfoList = candidateNodeService.getCandidateNodesInfo();
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), nodeInfoList);
    }
}
