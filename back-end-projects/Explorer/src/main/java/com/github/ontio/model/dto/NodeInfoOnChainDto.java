package com.github.ontio.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ontio.model.dao.NodeInfoOnChain;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "tbl_node_info_on_chain")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class NodeInfoOnChainDto extends NodeInfoOnChain {

    @Transient
    private String introduction;

    @Transient
    private String logoUrl;

    @Transient
    private Integer feeSharingRatio;

    @Transient
    private Integer ontologyHarbinger;

    @Transient
    private Integer risky;

    @Transient
    private Integer badActor;

    @Transient
    private String apr;

    @Transient
    private String feeSharingRatioNodeT;

    @Transient
    private String feeSharingRatioNodeT1;

    @Transient
    private String feeSharingRatioUserT;

    @Transient
    private String feeSharingRatioUserT1;

    @Transient
    private Boolean lastBadNode;

    @Transient
    private Boolean newBadNode;

    public NodeInfoOnChainDto() {

    }

    public NodeInfoOnChainDto(NodeInfoOnChainDto nodeInfoOnChain) {
        super(nodeInfoOnChain);
        this.introduction = nodeInfoOnChain.getIntroduction();
        this.logoUrl = nodeInfoOnChain.getLogoUrl();
        this.feeSharingRatio = nodeInfoOnChain.getFeeSharingRatio();
        this.ontologyHarbinger = nodeInfoOnChain.getOntologyHarbinger();
        this.risky = nodeInfoOnChain.getRisky();
        this.badActor = nodeInfoOnChain.getBadActor();
        this.apr = nodeInfoOnChain.getApr();
        this.lastBadNode = nodeInfoOnChain.getLastBadNode();
        this.newBadNode = nodeInfoOnChain.getNewBadNode();
    }
}