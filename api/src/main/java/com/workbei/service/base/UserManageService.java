package com.workbei.service.base;

import com.workbei.model.domain.user.*;
import com.workbei.model.view.autocreate.AutoCreateUserVO;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 15:52
 */
public interface UserManageService {
    WbOuterDataUserDO getOuterDataUserByClientAndOuterId(String client, String outerId);

    WbUserDO getUserById(Long id);

    WbUserDO getUserByClientAndOuterId(String client, String outerCombineId);

    WbUserOauthDO getUserOauthByDdUnionId(String ddUnionId);

    WbAccountDO getAccountById(Long id);

    void saveOrUpdateAccount(WbAccountDO accountDO);

    void saveOrUpdateUserOauth(WbUserOauthDO userOauthDO);

    WbUserDO saveUserInfo(Long teamId, AutoCreateUserVO userVO);

    WbUserDO updateUserInfo(WbUserDO userDO, AutoCreateUserVO userVO);

    void saveOrUpdateUser(WbUserDO userDO);

    WbUserRegisterDO getUserRegisterByAccountId(Long accountId);

    WbUserOauthDO getUserOauthByAccountId(Long accountId);

    WbUserGuideDO getUserGuideByUserId(Long userId);

    WbUserDisplayOrderDO getUserDisplayOrderByUserId(Long userId);

    WbUserUiSettingDO getUserUiSettingByUserId(Long userId);

    WbUserFunctionSettingDO getUserFunctionSettingByUserId(Long userId);
}
