package com.workbei.service.base.util;

import com.workbei.constant.WbConstant;
import com.workbei.model.domain.user.WbAccountDO;
import com.workbei.model.domain.user.WbUserDO;
import com.workbei.model.domain.user.WbUserOauthDO;
import com.workbei.model.view.autocreate.AutoCreateUserVO;
import com.workbei.factory.UserFactory;

import java.util.Date;

/**
 * @author Wallace Mao
 * Date: 2018-12-01 17:01
 */
public class TestUserFactory {
    public static AutoCreateUserVO getAutoCreateUserVO(){
        AutoCreateUserVO user = new AutoCreateUserVO();
        Date now = new Date();
        user.setName("auto_test_user_name_" + now.getTime());
        user.setAvatar("auto_test_user_avatar" + now.getTime());
        user.setClient(WbConstant.APP_DEFAULT_CLIENT);
        user.setAdmin(false);
        user.setOuterUserId("at_user_outer_id_" + now.getTime());
        user.setOuterCombineId("at_user_comid_" + now.getTime());
        return user;
    }

    public static WbAccountDO getAccountDO(){
        Date now = new Date();
        WbAccountDO accountDO = UserFactory.getAccountDO();
        accountDO.setAvatar("at_account_avatar_" + now.getTime());
        accountDO.setPhoneNumber("138" + now.getTime() % 100000000L);
        accountDO.setPassword("qwerqwerqwre");
        accountDO.setEmail("at_account_email@" + now.getTime() + ".com");
        accountDO.setName("at_account_name_" + now.getTime());
        return accountDO;
    }

    public static WbUserDO getUserDO(){
        Date now = new Date();
        WbUserDO userDO = UserFactory.getUserDO();
        userDO.setName("at_user_name_" + now.getTime());
        return userDO;
    }

    public static WbUserOauthDO getUserOauthDO(Long accountId) {
        WbUserOauthDO userOauthDO = new WbUserOauthDO();
        userOauthDO.setAccountId(accountId);
        userOauthDO.setDdUnionId("at_dd_union_id" + new Date().getTime());
        return userOauthDO;
    }
}
