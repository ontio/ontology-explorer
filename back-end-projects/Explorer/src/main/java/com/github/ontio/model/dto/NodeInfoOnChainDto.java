package com.github.ontio.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ontio.model.dao.NodeInfoOnChain;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "tbl_node_info_on_chain")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class NodeInfoOnChainDto extends NodeInfoOnChain {

    @Column(name = "fee_sharing_ratio")
    private Integer feeSharingRatio;

    @Column(name = "ontology_harbinger")
    private Integer ontologyHarbinger;

    private Integer risky;

    @Column(name = "bad_actor")
    private Integer badActor;

    public NodeInfoOnChainDto() {

    }
    public NodeInfoOnChainDto(NodeInfoOnChainDto nodeInfoOnChain) {
        super(nodeInfoOnChain);
        this.feeSharingRatio = nodeInfoOnChain.getFeeSharingRatio();
        this.ontologyHarbinger = nodeInfoOnChain.getOntologyHarbinger();
        this.risky = nodeInfoOnChain.getRisky();
        this.badActor = nodeInfoOnChain.getBadActor();
    }
}