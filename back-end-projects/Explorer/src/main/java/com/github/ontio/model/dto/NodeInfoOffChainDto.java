package com.github.ontio.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ontio.model.dao.NodeInfoOffChain;
import lombok.Builder;

import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "tbl_node_info_off_chain")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NodeInfoOffChainDto extends NodeInfoOffChain {

    @Builder
    public NodeInfoOffChainDto(String publicKey, String name, String address, String ontId, Integer nodeType,
                               String introduction, String logoUrl, String region, BigDecimal longitude,
                               BigDecimal latitude, String ip, String website, String socialMedia,
                               String telegram, String twitter, String facebook, String openMail,
                               String contactMail, Boolean openFlag, Integer verification) {
        super(publicKey, name, address, ontId, nodeType, introduction, logoUrl, region, longitude, latitude, ip, website, socialMedia,
                telegram, twitter, facebook, openMail, contactMail, openFlag, verification);
    }

}
