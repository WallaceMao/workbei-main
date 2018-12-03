package com.workbei.model.domain.user;

import java.io.Serializable;
import java.util.Date;

/**
 * 无为唯一约束
 * @author Wallace Mao
 * Date: 2018-11-28 17:33
 */
public class WbJoinAndQuitTeamRecordDO implements Serializable {
    private Long id;
    private Long version;
    private Long userId;
    private Long teamId;
    private Date dateCreated;
    private Date lastUpdated;

    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "WbJoinAndQuitTeamRecordDO{" +
                "id=" + id +
                ", version=" + version +
                ", userId=" + userId +
                ", teamId=" + teamId +
                ", dateCreated=" + dateCreated +
                ", lastUpdated=" + lastUpdated +
                ", type='" + type + '\'' +
                '}';
    }
}
