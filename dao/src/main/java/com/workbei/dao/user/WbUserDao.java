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
    WbUserDO getUserById(
            @Param("id") Long id);
    WbUserDO getUserByClientAndOuterId(
            @Param("client") String client,
            @Param("outerId") String outerId);
    WbUserDO getUserWithAccountById(
            @Param("id") Long id);

    void saveOrUpdateUserGuide(WbUserGuideDO userGuideDO);
    WbUserGuideDO getUserGuideByUserId(@Param("userId") Long userId);

    void saveOrUpdateUserDisplayOrder(WbUserDisplayOrderDO userDisplayOrderDO);
    WbUserDisplayOrderDO getUserDisplayOrderByUserId(@Param("userId") Long userId);

    void saveOrUpdateUserUiSetting(WbUserUiSettingDO userUiSettingDO);
    WbUserUiSettingDO getUserUiSettingByUserId(@Param("userId") Long userId);

    void saveOrUpdateUserFunctionSetting(WbUserFunctionSettingDO userFunctionSettingDO);
    WbUserFunctionSettingDO getUserFunctionSettingByUserId(@Param("userId") Long userId);
}
