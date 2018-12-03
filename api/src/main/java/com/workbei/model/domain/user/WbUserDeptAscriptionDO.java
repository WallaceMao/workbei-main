package com.workbei.model.domain.user;

import java.io.Serializable;

/**
 * departmentId/userId为唯一约束
 * @author Wallace Mao
 * Date: 2018-11-27 16:09
 */
public class WbUserDeptAscriptionDO implements Serializable {
    private Long id;
    private Long version;
    private Long userId;
    private Long departmentId;

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

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "WbUserDeptAscriptionDO{" +
                "id=" + id +
                ", version=" + version +
                ", userId=" + userId +
                ", departmentId=" + departmentId +
                '}';
    }
}
