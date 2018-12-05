package com.workbei.manager.user.impl;

import com.workbei.constant.WbConstant;
import com.workbei.dao.user.WbRoleDao;
import com.workbei.model.domain.user.WbRoleGroupDO;
import com.workbei.model.domain.user.WbUserRoleGroupDO;
import com.workbei.manager.user.RoleManager;
import com.workbei.factory.RoleFactory;

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
    private WbRoleDao wbRoleDao;

    public RoleManagerImpl(WbRoleDao wbRoleDao) {
        this.wbRoleDao = wbRoleDao;
    }

    //  --------roleGroup--------
    @Override
    public WbRoleGroupDO getCommonRoleGroup(){
        return wbRoleDao.getRoleGroupByName(WbConstant.APP_ROLE_GROUP_USER);
    }

    //  --------userRoleGroup--------
    @Override
    public void saveOrUpdateUserCommonRoleGroup(Long userId){
        WbUserRoleGroupDO userRoleGroupDO = RoleFactory.getUserRoleGroupDO();
        WbRoleGroupDO roleGroup = wbRoleDao.getRoleGroupByName(WbConstant.APP_ROLE_GROUP_USER);
        userRoleGroupDO.setUserId(userId);
        userRoleGroupDO.setRoleGroupId(roleGroup.getId());
        wbRoleDao.saveOrUpdateUserRoleGroup(userRoleGroupDO);
    }

    @Override
    public WbUserRoleGroupDO getUserRoleGroupByUserId(Long userId){
        return wbRoleDao.getUserRoleGroupByUserId(userId);
    }
}
