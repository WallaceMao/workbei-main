package com.workbei.manager.user.impl;

import com.workbei.BaseUnitTest;
import com.workbei.constant.WbConstant;
import com.workbei.model.domain.user.WbAccountDO;
import com.workbei.model.domain.user.WbUserDO;
import com.workbei.model.domain.user.WbUserOauthDO;
import com.workbei.model.domain.user.WbUserRegisterDO;
import com.workbei.model.view.autocreate.AutoCreateUserVO;
import com.workbei.util.TestUserFactory;
import org.apache.commons.lang.SerializationUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Wallace Mao
 * Date: 2018-12-05 15:16
 */
@Transactional(transactionManager = "transactionManager")
@Rollback
public class AccountManagerImplTest extends BaseUnitTest {
    @Autowired
    private AccountManagerImpl accountManager;

    @Test
    public void testSaveAccountInfo() throws Exception {
        Date now = new Date();
        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterUnionId("at_user_union_id_" + now.getTime());
        WbAccountDO resultAccountDO = accountManager.saveAccountInfo(userVO);
        WbAccountDO accountDO = accountManager.getAccountById(resultAccountDO.getId());

        assertThat(accountDO).isNotNull();
        assertThat(accountDO).isEqualToIgnoringGivenFields(resultAccountDO, "version");
        assertThat(accountDO.getAvatar()).isEqualTo(userVO.getAvatar());
        assertThat(accountDO.getName()).isEqualTo(userVO.getName());
        assertThat(accountDO.getUuid()).isNotNull();

        Long accountId = accountDO.getId();
        WbUserRegisterDO userRegisterDO = accountManager.getUserRegisterByAccountId(accountId);
        assertThat(userRegisterDO).isNotNull();
        assertThat(userRegisterDO.getAccountId()).isEqualTo(accountId);
        checkDateCloseToNow(
                userRegisterDO.getDateCreated(),
                userRegisterDO.getLastUpdated(),
                userRegisterDO.getRegDate());
        assertThat(userRegisterDO.getSystem()).isFalse();
        assertThat(userRegisterDO.getMode()).isEqualTo(WbConstant.APP_DEFAULT_MODE);
        assertThat(userRegisterDO.getClient()).isEqualTo(WbConstant.APP_DEFAULT_CLIENT);

        WbUserOauthDO userOauthDO = accountManager.getUserOauthByAccountId(accountId);
        assertThat(userOauthDO).isNotNull();
        assertThat(userOauthDO.getAccountId()).isEqualTo(accountId);
        checkDateCloseToNow(userOauthDO.getDateCreated(), userOauthDO.getLastUpdated());
        assertThat(userOauthDO.getDdUnionId()).isEqualTo(userVO.getOuterUnionId());

        WbUserOauthDO userOauthUnionId = accountManager.getUserOauthByDdUnionId(userVO.getOuterUnionId());
        assertThat(userOauthUnionId).isNotNull();
        assertThat(userOauthUnionId.getId()).isEqualTo(userOauthDO.getId());
        assertThat(userOauthUnionId.getDdUnionId()).isEqualTo(userVO.getOuterUnionId());
    }

    @Test
    public void testUpdateAccountInfo() throws Exception {
        Date now = new Date();
        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterUnionId("at_user_union_id_" + now.getTime());
        accountManager.saveAccountInfo(userVO);
        WbAccountDO savedAccountDO = accountManager.getAccountByDdUnionId(userVO.getOuterUnionId());

        WbAccountDO accountDO = (WbAccountDO) SerializationUtils.clone(savedAccountDO);
        Long accountId = accountDO.getId();
        WbUserRegisterDO userRegisterDO = (WbUserRegisterDO) SerializationUtils.clone(
                accountManager.getUserRegisterByAccountId(accountId));
        WbUserOauthDO userOauthDO = (WbUserOauthDO) SerializationUtils.clone(
                accountManager.getUserOauthByAccountId(accountId));

        //  update after 100ms
        Thread.sleep(100);
        AutoCreateUserVO userVO2 = TestUserFactory.getAutoCreateUserVO();
        userVO2.setOuterUnionId("at_user_union_id_" + new Date().getTime());
        accountManager.updateAccountInfo(userVO2);
        WbAccountDO accountDO2 = accountManager.getAccountById(savedAccountDO.getId());
        Long accountId2 = accountDO2.getId();
        WbUserRegisterDO userRegisterDO2 = accountManager.getUserRegisterByAccountId(accountId2);
        WbUserOauthDO userOauthDO2 = accountManager.getUserOauthByAccountId(accountId2);
        assertThat(accountDO2.getName()).isEqualTo(userVO2.getName());
        assertThat(accountDO2.getName()).isNotEqualTo(accountDO.getName());
        assertThat(accountDO2.getAvatar()).isEqualTo(userVO2.getAvatar());
        assertThat(accountDO2.getAvatar()).isNotEqualTo(accountDO.getAvatar());
        assertThat(accountDO2).isEqualToIgnoringGivenFields(accountDO, "name", "avatar");
        assertThat(userOauthDO2).isEqualToIgnoringGivenFields(userOauthDO, "ddUnionId");
        assertThat(userRegisterDO2).isEqualToComparingFieldByFieldRecursively(userRegisterDO);
    }
}
