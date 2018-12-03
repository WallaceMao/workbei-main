package com.workbei.model.domain.user;

import java.io.Serializable;

/**
 * @author Wallace Mao
 * Date: 2018-11-28 15:08
 */
public class WbUserRoleGroupDO implements Serializable {
    private Long id;
    private Long version;
    private Long userId;
    private Long roleGroupId;

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

    public Long getRoleGroupId() {
        return roleGroupId;
    }

    public void setRoleGroupId(Long roleGroupId) {
        this.roleGroupId = roleGroupId;
    }

    @Override
    public String toString() {
        return "WbUserRoleGroupDO{" +
                "id=" + id +
                ", version=" + version +
                ", userId=" + userId +
                ", roleGroupId=" + roleGroupId +
                '}';
    }
}
