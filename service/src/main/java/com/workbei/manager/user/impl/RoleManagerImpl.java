package com.workbei.manager.user.impl;

import com.workbei.constant.WbConstant;
import com.workbei.dao.user.WbRoleDao;
import com.workbei.model.domain.user.WbRoleGroupDO;
import com.workbei.model.domain.user.WbUserDO;
import com.workbei.model.domain.user.WbUserRoleGroupDO;
import com.workbei.manager.user.RoleManager;
import com.workbei.factory.RoleFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Role的聚合
 * 包括的实体：
 * WbRoleGroupDO
 * 包括的关联：
 * WbUserRoleGroupDO
 * @author Wallace Mao
 * Date: 2018-12-02 22:16
 */
public class RoleManagerImpl implements RoleManager {
    @Autowired
    private WbRoleDao wbRoleDao;

    @Override
    public WbRoleGroupDO getCommonRoleGroup(){
        return wbRoleDao.getRoleGroupByName(WbConstant.APP_ROLE_GROUP_USER);
    }

    @Override
    public void saveOrUpdateUserCommonRoleGroup(WbUserDO userDO){
        WbUserRoleGroupDO userRoleGroupDO = RoleFactory.getUserRoleGroupDO();
        WbRoleGroupDO roleGroup = wbRoleDao.getRoleGroupByName(WbConstant.APP_ROLE_GROUP_USER);
        userRoleGroupDO.setUserId(userDO.getId());
        userRoleGroupDO.setRoleGroupId(roleGroup.getId());
        wbRoleDao.saveOrUpdateUserRoleGroup(userRoleGroupDO);
    }

    @Override
    public WbUserRoleGroupDO getUserRoleGroupByUserId(Long userId){
        return wbRoleDao.getUserRoleGroupByUserId(userId);
    }
}
