package com.workbei.manager.user;

import com.workbei.model.domain.user.WbRoleGroupDO;
import com.workbei.model.domain.user.WbUserRoleGroupDO;

/**
 * @author Wallace Mao
 * Date: 2018-12-02 22:17
 */
public interface RoleManager {
    //  --------roleGroup--------
    WbRoleGroupDO getCommonRoleGroup();

    //  --------userRoleGroup--------
    void saveOrUpdateUserCommonRoleGroup(Long userId);

    WbUserRoleGroupDO getUserRoleGroupByUserId(Long userId);
}
