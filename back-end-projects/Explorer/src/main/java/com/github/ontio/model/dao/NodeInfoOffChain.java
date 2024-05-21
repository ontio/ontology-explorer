package com.github.ontio.model.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_node_info_off_chain")
@Data
public class NodeInfoOffChain {
    @Id
    @Column(name = "public_key")
    @GeneratedValue(generator = "JDBC")
    private String publicKey;

    private String name;

    private String address;

    @Column(name = "ont_id")
    private String ontId;

    @Column(name = "node_type")
    private Integer nodeType;

    private String introduction;

    @Column(name = "logo_url")
    private String logoUrl;

    private String region;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private String ip;

    private String website;

    @Column(name = "social_media")
    private String socialMedia;

    private String telegram;

    private String twitter;

    private String facebook;

    @Column(name = "open_mail")
    private String openMail;

    @Column(name = "contact_mail")
    private String contactMail;

    @Column(name = "open_flag")
    private Boolean openFlag;

    private Integer verification;

    @Column(name = "contact_info_verified")
    private Integer contactInfoVerified;

    @Column(name = "fee_sharing_ratio")
    private Integer feeSharingRatio;

    @Column(name = "ontology_harbinger")
    private Integer ontologyHarbinger;

    @Column(name = "old_node")
    private Integer oldNode;

    private Integer risky;

    @Column(name = "bad_actor")
    private Integer badActor;

    @Transient
    private Integer status;

    @Transient
    private String progress;

    @Transient
    private String userApy;
}