package com.workbei.model.domain.user;

import java.io.Serializable;
import java.util.Date;

/**
 * uuid为唯一约束
 * @author Wallace Mao
 * Date: 2018-11-27 15:20
 */
public class WbTeamDO implements Serializable {
    private Long id;
    private String uuid;
    private Long version;
    private Date dateCreated;
    private Date lastUpdated;
    /** 公司名 */
    private String name;

    //公司 logo
    private String logo;
    // 是否存在
    private Boolean isDisplay;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Boolean getDisplay() {
        return isDisplay;
    }

    public void setDisplay(Boolean display) {
        isDisplay = display;
    }

    @Override
    public String toString() {
        return "WbTeamDO{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", version=" + version +
                ", dateCreated=" + dateCreated +
                ", lastUpdated=" + lastUpdated +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", isDisplay=" + isDisplay +
                '}';
    }
}
