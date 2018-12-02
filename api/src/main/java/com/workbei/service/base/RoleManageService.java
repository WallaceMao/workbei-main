package com.workbei.service.base;

import com.workbei.model.domain.user.WbRoleGroupDO;
import com.workbei.model.domain.user.WbUserDO;

/**
 * @author Wallace Mao
 * Date: 2018-12-02 22:17
 */
public interface RoleManageService {
    WbRoleGroupDO getCommonRoleGroup();

    void saveOrUpdateUserCommonRoleGroup(WbUserDO userDO);
}
