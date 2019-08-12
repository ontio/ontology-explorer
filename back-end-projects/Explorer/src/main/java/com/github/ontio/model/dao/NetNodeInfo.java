package com.github.ontio.model.dao;

import javax.persistence.*;

@Table(name = "tbl_net_node_info")
public class NetNodeInfo {
    @Id
    @GeneratedValue(generator = "JDBC")
    private String ip;

    private String version;

    @Column(name = "is_consensus")
    private Boolean isConsensus;

    private Boolean active;

    @Column(name = "last_active_time")
    private Long lastActiveTime;

    private String country;

    private String longitude;

    private String latitude;

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
     * @return version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version
     */
    public void setVersion(String version) {
        this.version = version == null ? null : version.trim();
    }

    /**
     * @return is_consensus
     */
    public Boolean getIsConsensus() {
        return isConsensus;
    }

    /**
     * @param isConsensus
     */
    public void setIsConsensus(Boolean isConsensus) {
        this.isConsensus = isConsensus;
    }

    /**
     * @return active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * @param active
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * @return last_active_time
     */
    public Long getLastActiveTime() {
        return lastActiveTime;
    }

    /**
     * @param lastActiveTime
     */
    public void setLastActiveTime(Long lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    /**
     * @return country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country
     */
    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    /**
     * @return longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * @param longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? null : longitude.trim();
    }

    /**
     * @return latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * @param latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? null : latitude.trim();
    }
}