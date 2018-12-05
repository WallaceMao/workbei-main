package com.workbei.manager.user;

import com.workbei.model.domain.user.*;
import com.workbei.model.view.autocreate.AutoCreateUserVO;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 15:52
 */
public interface UserManager {
    //  --------user--------
    void saveOrUpdateUser(WbUserDO userDO);

    WbUserDO getUserById(Long id);

    WbUserDO getUserByClientAndOuterId(String client, String outerCombineId);

    //  --------userGuide--------
    WbUserGuideDO getUserGuideByUserId(Long userId);

    //  --------userDisplay-------
    WbUserDisplayOrderDO getUserDisplayOrderByUserId(Long userId);

    //  --------userUiSetting--------
    WbUserUiSettingDO getUserUiSettingByUserId(Long userId);

    //  --------userFunctionSetting--------
    WbUserFunctionSettingDO getUserFunctionSettingByUserId(Long userId);

    //  --------outerDataUser--------
    WbOuterDataUserDO getOuterDataUserByClientAndOuterId(String client, String outerId);

    WbUserDO saveUserInfo(Long teamId, Long accountId, AutoCreateUserVO userVO);

    WbUserDO updateUserInfo(AutoCreateUserVO userVO);
}
