package com.workbei.manager.user;

import com.workbei.model.domain.user.WbAccountDO;
import com.workbei.model.domain.user.WbUserOauthDO;
import com.workbei.model.domain.user.WbUserRegisterDO;
import com.workbei.model.view.autocreate.AutoCreateUserVO;

/**
 * @author Wallace Mao
 * Date: 2018-12-05 13:59
 */
public interface AccountManager {
    //  --------account-------
    void saveOrUpdateAccount(WbAccountDO accountDO);

    WbAccountDO getAccountById(Long id);

    WbAccountDO getAccountByClientAndOuterId(String client, String outerId);

    WbAccountDO getAccountByDdUnionId(String ddUnionId);

    //  --------userRegister--------
    WbUserRegisterDO getUserRegisterByAccountId(Long accountId);

    //  --------userOauth--------
    void saveOrUpdateUserOauth(WbUserOauthDO userOauthDO);

    WbUserOauthDO getUserOauthByAccountId(Long accountId);

    WbUserOauthDO getUserOauthByDdUnionId(String ddUnionId);

    //  --------
    WbAccountDO saveAccountInfo(AutoCreateUserVO userVO);

    WbAccountDO updateAccountInfo(AutoCreateUserVO userVO);
}
