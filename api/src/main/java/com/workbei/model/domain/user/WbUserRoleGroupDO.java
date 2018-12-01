package com.workbei.model.domain.user;

/**
 * @author Wallace Mao
 * Date: 2018-11-28 15:08
 */
public class WbUserRoleGroupDO {
    private Long id;
    private Long userId;
    private Long roleGroupId;

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

    public Long getRoleGroupId() {
        return roleGroupId;
    }

    public void setRoleGroupId(Long roleGroupId) {
        this.roleGroupId = roleGroupId;
    }
}
