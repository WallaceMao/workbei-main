package com.workbei.model.domain.user;

import java.io.Serializable;
import java.util.Date;

/**
 * teamId为唯一约束
 * @author Wallace Mao
 * Date: 2018-11-28 16:30
 */
public class WbTeamUserDO implements Serializable {
    private Long id;
    private Long version;
    private Long teamId;
    private Date dateCreated;
    private Date lastUpdated;

    /** 删除公司时公司里所有的员工的id */
    private String deletedTeamUser;
    //谁可以邀请公司同事，all-所有人,admin-管理员，creator-拥有者
    private String whoCanInviteColleague;

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

    public String getDeletedTeamUser() {
        return deletedTeamUser;
    }

    public void setDeletedTeamUser(String deletedTeamUser) {
        this.deletedTeamUser = deletedTeamUser;
    }

    public String getWhoCanInviteColleague() {
        return whoCanInviteColleague;
    }

    public void setWhoCanInviteColleague(String whoCanInviteColleague) {
        this.whoCanInviteColleague = whoCanInviteColleague;
    }

    @Override
    public String toString() {
        return "WbTeamUserDO{" +
                "id=" + id +
                ", version=" + version +
                ", teamId=" + teamId +
                ", dateCreated=" + dateCreated +
                ", lastUpdated=" + lastUpdated +
                ", deletedTeamUser='" + deletedTeamUser + '\'' +
                ", whoCanInviteColleague='" + whoCanInviteColleague + '\'' +
                '}';
    }
}
