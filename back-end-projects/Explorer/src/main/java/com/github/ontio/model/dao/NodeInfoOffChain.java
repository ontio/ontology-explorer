package com.github.ontio.model.dao;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_node_info_off_chain")
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

    private Integer risky;
    @Column(name = "bad_actor")
    private Integer badActor;

    public Integer getContactInfoVerified() {
        return contactInfoVerified;
    }

    public void setContactInfoVerified(Integer contactInfoVerified) {
        this.contactInfoVerified = contactInfoVerified;
    }

    public Integer getFeeSharingRatio() {
        return feeSharingRatio;
    }

    public void setFeeSharingRatio(Integer feeSharingRatio) {
        this.feeSharingRatio = feeSharingRatio;
    }

    public Integer getOntologyHarbinger() {
        return ontologyHarbinger;
    }

    public void setOntologyHarbinger(Integer ontologyHarbinger) {
        this.ontologyHarbinger = ontologyHarbinger;
    }

    public Integer getOldNode() {
        return oldNode;
    }

    public void setOldNode(Integer oldNode) {
        this.oldNode = oldNode;
    }

    @Column(name = "old_node")
    private Integer oldNode;



    /**
     * @return public_key
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * @param publicKey
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey == null ? null : publicKey.trim();
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * @return ont_id
     */
    public String getOntId() {
        return ontId;
    }

    /**
     * @param ontId
     */
    public void setOntId(String ontId) {
        this.ontId = ontId == null ? null : ontId.trim();
    }

    /**
     * @return node_type
     */
    public Integer getNodeType() {
        return nodeType;
    }

    /**
     * @param nodeType
     */
    public void setNodeType(Integer nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * @return introduction
     */
    public String getIntroduction() {
        return introduction;
    }

    /**
     * @param introduction
     */
    public void setIntroduction(String introduction) {
        this.introduction = introduction == null ? null : introduction.trim();
    }

    /**
     * @return logo_url
     */
    public String getLogoUrl() {
        return logoUrl;
    }

    /**
     * @param logoUrl
     */
    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl == null ? null : logoUrl.trim();
    }

    /**
     * @return region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region
     */
    public void setRegion(String region) {
        this.region = region == null ? null : region.trim();
    }

    /**
     * @return longitude
     */
    public BigDecimal getLongitude() {
        return longitude;
    }

    /**
     * @param longitude
     */
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    /**
     * @return latitude
     */
    public BigDecimal getLatitude() {
        return latitude;
    }

    /**
     * @param latitude
     */
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    /**
     * @return ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    /**
     * @return website
     */
    public String getWebsite() {
        return website;
    }

    /**
     * @param website
     */
    public void setWebsite(String website) {
        this.website = website == null ? null : website.trim();
    }

    /**
     * @return social_media
     */
    public String getSocialMedia() {
        return socialMedia;
    }

    /**
     * @param socialMedia
     */
    public void setSocialMedia(String socialMedia) {
        this.socialMedia = socialMedia == null ? null : socialMedia.trim();
    }

    /**
     * @return telegram
     */
    public String getTelegram() {
        return telegram;
    }

    /**
     * @param telegram
     */
    public void setTelegram(String telegram) {
        this.telegram = telegram == null ? null : telegram.trim();
    }

    /**
     * @return twitter
     */
    public String getTwitter() {
        return twitter;
    }

    /**
     * @param twitter
     */
    public void setTwitter(String twitter) {
        this.twitter = twitter == null ? null : twitter.trim();
    }

    /**
     * @return facebook
     */
    public String getFacebook() {
        return facebook;
    }

    /**
     * @param facebook
     */
    public void setFacebook(String facebook) {
        this.facebook = facebook == null ? null : facebook.trim();
    }

    /**
     * @return openMail
     */
    public String getOpenMail() {
        return openMail;
    }

    /**
     * @param openMail
     */
    public void setOpenMail(String openMail) {
        this.openMail = openMail == null ? null : openMail.trim();
    }

    /**
     * @return contactMail
     */
    public String getContactMail() {
        return contactMail;
    }

    /**
     * @param contactMail
     */
    public void setContactMail(String contactMail) {
        this.contactMail = contactMail == null ? null : contactMail.trim();
    }

    public Boolean getOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(Boolean openFlag) {
        this.openFlag = openFlag;
    }

    public Integer getVerification() {
        return verification;
    }

    public void setVerification(Integer verification) {
        this.verification = verification;
    }

    public Integer getRisky() {
        return risky;
    }

    public void setRisky(Integer risky) {
        this.risky = risky;
    }

    public Integer getBadActor() {
        return badActor;
    }

    public void setBadActor(Integer badActor) {
        this.badActor = badActor;
    }
}