package com.workbei.dao.user;

import com.workbei.model.domain.user.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Wallace Mao
 * Date: 2018-11-12 1:17
 */
@Repository("wbUserDao")
public interface WbUserDao {
    void saveOrUpdateUser(WbUserDO user);
    void saveOrUpdateUserGuide(WbUserGuideDO userGuideDO);
    void saveOrUpdateUserDisplayOrder(WbUserDisplayOrderDO userDisplayOrderDO);
    void saveOrUpdateUserRoleGroup(WbUserRoleGroupDO userRoleGroupDO);
    void saveOrUpdateUserUiSetting(WbUserUiSettingDO userUiSettingDO);
    void saveOrUpdateUserFunctionSetting(WbUserFunctionSettingDO userFunctionSettingDO);
    WbUserDO getUserById(@Param("id") Long id);
}
