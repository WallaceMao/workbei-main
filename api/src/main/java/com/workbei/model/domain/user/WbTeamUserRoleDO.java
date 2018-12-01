package com.workbei.model.domain.user;

import java.util.Date;

/**
 * @author Wallace Mao
 * Date: 2018-11-28 16:30
 */
public class WbTeamUserRoleDO {
    private Long id;
    private Long userId;
    private Long teamId;
    private Date dateCreated;
    private Date lastUpdated;
    /*
    * 用户角色：
    * admin-管理员
    * superAdmin-超级管理员
    * */
    private String role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
