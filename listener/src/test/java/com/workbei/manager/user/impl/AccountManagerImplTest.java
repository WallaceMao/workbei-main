package com.workbei.manager.user.impl;

import com.workbei.BaseUnitTest;
import com.workbei.constant.WbConstant;
import com.workbei.manager.user.AccountManager;
import com.workbei.manager.user.TeamManager;
import com.workbei.manager.user.UserManager;
import com.workbei.model.domain.user.*;
import com.workbei.model.view.autocreate.AutoCreateTeamVO;
import com.workbei.model.view.autocreate.AutoCreateUserVO;
import com.workbei.util.TestTeamFactory;
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
    private AccountManager accountManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private TeamManager teamManager;

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
    public void testSaveAccountInfoWithNoAvatar() throws Exception {
        Date now = new Date();
        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterUnionId("at_user_union_id_" + now.getTime());
        // 如果传入的头像为null，那么需要使用系统默认头像
        userVO.setAvatar(null);
        WbAccountDO resultAccountDO = accountManager.saveAccountInfo(userVO);
        WbAccountDO accountDO = accountManager.getAccountById(resultAccountDO.getId());

        assertThat(accountDO).isNotNull();
        assertThat(accountDO.getAvatar()).isEqualTo(WbConstant.USER_DEFAULT_USER_AVATAR);

        AutoCreateUserVO userVO2 = TestUserFactory.getAutoCreateUserVO();
        userVO2.setOuterUnionId("at_user_union_id_" + now.getTime());
        // 如果传入的头像为""(空字符串)，那么需要使用系统默认头像
        userVO2.setAvatar("");
        WbAccountDO resultAccountDO2 = accountManager.saveAccountInfo(userVO2);
        WbAccountDO accountDO2 = accountManager.getAccountById(resultAccountDO2.getId());

        assertThat(accountDO2).isNotNull();
        assertThat(accountDO2.getAvatar()).isEqualTo(WbConstant.USER_DEFAULT_USER_AVATAR);
    }

    @Test
    public void testUpdateAccountInfo() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        WbTeamDO teamSaved = teamManager.saveTeamInfo(teamVO);

        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterUnionId("at_user_union_id_" + now.getTime());
        userVO.setOuterCorpId(teamVO.getOuterCorpId());
        accountManager.saveAccountInfo(userVO);
        WbAccountDO savedAccountDO = accountManager.getAccountByDdUnionId(userVO.getOuterUnionId());
        userManager.saveUserInfo(teamSaved.getId(), savedAccountDO.getId(), userVO);

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
        userVO2.setOuterCombineId(userVO.getOuterCombineId());
        userVO2.setOuterCorpId(teamVO.getOuterCorpId());
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
        assertThat(userOauthDO2).isEqualToIgnoringGivenFields(userOauthDO, "ddUnionId", "version", "lastUpdated");
        assertThat(userRegisterDO2).isEqualToComparingFieldByFieldRecursively(userRegisterDO);
    }

    @Test
    public void testUpdateAccountInfoWithNoAvatar() {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        WbTeamDO teamSaved = teamManager.saveTeamInfo(teamVO);

        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterUnionId("at_user_union_id_" + now.getTime());
        userVO.setOuterCorpId(teamVO.getOuterCorpId());
        accountManager.saveAccountInfo(userVO);
        WbAccountDO savedAccountDO = accountManager.getAccountByDdUnionId(userVO.getOuterUnionId());
        userManager.saveUserInfo(teamSaved.getId(), savedAccountDO.getId(), userVO);

        WbAccountDO accountDO = (WbAccountDO) SerializationUtils.clone(savedAccountDO);
        Long accountId = accountDO.getId();
        WbUserOauthDO userOauthDO = (WbUserOauthDO) SerializationUtils.clone(
                accountManager.getUserOauthByAccountId(accountId));

        AutoCreateUserVO userVO2 = TestUserFactory.getAutoCreateUserVO();
        userVO2.setOuterCombineId(userVO.getOuterCombineId());
        userVO2.setOuterCorpId(teamVO.getOuterCorpId());
        // 更新时，如果avatar为null，那么不更新
        userVO2.setAvatar(null);
        userVO2.setName(null);
        userVO2.setOuterUnionId(null);
        accountManager.updateAccountInfo(userVO2);
        WbAccountDO accountDO2 = accountManager.getAccountById(savedAccountDO.getId());
        assertThat(accountDO2.getAvatar()).isEqualTo(accountDO.getAvatar());
        assertThat(accountDO2.getName()).isEqualTo(accountDO.getName());
        WbUserOauthDO userOauthDO2 = accountManager.getUserOauthByAccountId(accountDO2.getId());
        assertThat(userOauthDO2.getDdUnionId()).isEqualTo(userOauthDO.getDdUnionId());

        AutoCreateUserVO userVO3 = TestUserFactory.getAutoCreateUserVO();
        userVO3.setOuterUnionId("at_user_union_id_" + new Date().getTime());
        userVO3.setOuterCombineId(userVO.getOuterCombineId());
        userVO3.setOuterCorpId(teamVO.getOuterCorpId());
        // 更新时如果avatar为空字符串，那么也不更新
        userVO3.setAvatar("");
        userVO3.setName("");
        userVO3.setOuterUnionId("");
        accountManager.updateAccountInfo(userVO3);
        WbAccountDO accountDO3 = accountManager.getAccountById(savedAccountDO.getId());
        assertThat(accountDO3.getAvatar()).isEqualTo(accountDO.getAvatar());
        assertThat(accountDO3.getName()).isEqualTo(accountDO.getName());
        WbUserOauthDO userOauthDO3 = accountManager.getUserOauthByAccountId(accountDO3.getId());
        assertThat(userOauthDO3.getDdUnionId()).isEqualTo(userOauthDO.getDdUnionId());
    }
}
