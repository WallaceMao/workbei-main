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

    WbUserOauthDO getUserOauthByDdUnionId(String ddUnionId);

    WbAccountDO getAccountById(Long id);

    WbRoleGroupDO getCommonRoleGroup();

    void saveOrUpdateAccount(WbAccountDO accountDO);

    void saveOrUpdateUserOauth(WbUserOauthDO userOauthDO);

    WbUserDO saveUserInfo(AutoCreateUserVO userVO, WbTeamDO teamDO, WbRoleGroupDO roleGroupDO);

    WbUserDO updateUserInfo(AutoCreateUserVO userVO, WbUserDO userDO);

    void saveOrUpdateUser(WbUserDO userDO);
}
