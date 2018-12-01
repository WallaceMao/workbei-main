package com.workbei.model.domain.user;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 16:09
 */
public class WbUserDeptDO {
    private Long id;
    private Long userId;
    private Long departmentId;

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

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}
