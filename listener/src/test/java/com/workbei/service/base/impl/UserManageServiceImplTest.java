package com.workbei.service.base.impl;

import org.apache.commons.lang.SerializationUtils;
import com.workbei.BaseUnitTest;
import com.workbei.constant.WbConstant;
import com.workbei.model.domain.user.*;
import com.workbei.model.view.autocreate.AutoCreateUserVO;
import com.workbei.service.base.TeamManageService;
import com.workbei.service.base.UserManageService;
import com.workbei.service.base.util.TestTeamFactory;
import com.workbei.service.base.util.TestUserFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.assertj.core.api.Assertions.*;

@Transactional(transactionManager = "transactionManager")
@Rollback
public class UserManageServiceImplTest extends BaseUnitTest {
    @Autowired
    private UserManageService userManageService;
    @Autowired
    private TeamManageService teamManageService;

    private WbTeamDO globalTeam;
    private WbOuterDataTeamDO globalOuterDataTeam;

    @Before
    public void setUp() throws Exception {
        WbTeamDO teamDO = TestTeamFactory.getTeamDO();
        teamManageService.saveOrUpdateTeam(teamDO);
        WbOuterDataTeamDO outerDataTeamDO = TestTeamFactory.getOuterDataTeam(teamDO.getId());
        teamManageService.saveOrUpdateOuterDataTeam(outerDataTeamDO);
        globalTeam = teamDO;
        globalOuterDataTeam = outerDataTeamDO;
    }

    @Test
    public void testSaveUserInfo() throws Exception {
        Date now = new Date();
        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterCorpId(globalOuterDataTeam.getOuterId());
        userVO.setOuterUnionId("at_user_union_id_" + now.getTime());
        Long teamId = globalTeam.getId();
        userManageService.saveUserInfo(teamId, userVO);
        WbUserDO userDO = userManageService.getUserByClientAndOuterId(userVO.getClient(), userVO.getOuterCombineId());
        assertThat(userDO).isNotNull();
        assertThat(userDO.getId()).isNotNull();
        assertThat(userDO.getAccountId()).isNotNull();
        assertThat(userDO.getUsername()).isNotNull();
        assertThat(userDO.getDisplay()).isTrue();
        assertThat(userDO.getParent()).isFalse();
        assertThat(userDO.getName()).isEqualTo(userVO.getName());
        assertThat(userDO.getTeamId()).isEqualTo(teamId);

        Long userId = userDO.getId();
        WbAccountDO accountDO = userManageService.getAccountById(userDO.getAccountId());
        assertThat(accountDO).isNotNull();
        assertThat(accountDO.getId()).isNotNull();
        assertThat(accountDO.getAvatar()).isEqualTo(userVO.getAvatar());
        assertThat(accountDO.getName()).isEqualTo(userVO.getName());
        assertThat(accountDO.getUuid()).isNotNull();

        Long accountId = accountDO.getId();
        WbUserRegisterDO userRegisterDO = userManageService.getUserRegisterByAccountId(accountId);
        assertThat(userRegisterDO).isNotNull();
        assertThat(userRegisterDO.getAccountId()).isEqualTo(accountId);
        checkDateCloseToNow(
                userRegisterDO.getDateCreated(),
                userRegisterDO.getLastUpdated(),
                userRegisterDO.getRegDate());
        assertThat(userRegisterDO.getSystem()).isFalse();
        assertThat(userRegisterDO.getMode()).isEqualTo(WbConstant.APP_DEFAULT_MODE);
        assertThat(userRegisterDO.getClient()).isEqualTo(WbConstant.APP_DEFAULT_CLIENT);

        WbUserOauthDO userOauthDO = userManageService.getUserOauthByAccountId(accountId);
        assertThat(userOauthDO).isNotNull();
        assertThat(userOauthDO.getAccountId()).isEqualTo(accountId);
        checkDateCloseToNow(userOauthDO.getDateCreated(), userOauthDO.getLastUpdated());
        assertThat(userOauthDO.getDdUnionId()).isEqualTo(userVO.getOuterUnionId());

        WbUserOauthDO userOauthUnionId = userManageService.getUserOauthByDdUnionId(userVO.getOuterUnionId());
        assertThat(userOauthUnionId).isNotNull();
        assertThat(userOauthUnionId.getId()).isEqualTo(userOauthDO.getId());
        assertThat(userOauthUnionId.getDdUnionId()).isEqualTo(userVO.getOuterUnionId());

        WbOuterDataUserDO outerDataUserDO = userManageService.getOuterDataUserByClientAndOuterId(
                userVO.getClient(), userVO.getOuterCombineId());
        assertThat(outerDataUserDO).isNotNull();
        checkDateCloseToNow(outerDataUserDO.getDateCreated(), outerDataUserDO.getLastUpdated());
        assertThat(outerDataUserDO.getUserId()).isEqualTo(userId);
        assertThat(outerDataUserDO.getOuterId()).isEqualTo(userVO.getOuterCombineId());
        assertThat(outerDataUserDO.getClient()).isEqualTo(userVO.getClient());

        WbUserGuideDO userGuideDO = userManageService.getUserGuideByUserId(userId);
        assertThat(userGuideDO).isNotNull();
        assertThat(userGuideDO.getUserId()).isEqualTo(userId);
        assertThat(userGuideDO.getGuideKanbanFlag()).isTrue();
        assertThat(userGuideDO.getGuideNoteFlag()).isTrue();
        assertThat(userGuideDO.getGuideTaskFlag()).isTrue();
        assertThat(userGuideDO.getGuideWebFlag()).isTrue();

        WbUserDisplayOrderDO userDisplayOrderDO = userManageService.getUserDisplayOrderByUserId(userId);
        assertThat(userDisplayOrderDO).isNotNull();
        checkDateCloseToNow(userDisplayOrderDO.getDateCreated(), userDisplayOrderDO.getLastUpdated());
        assertThat(userDisplayOrderDO.getUserId()).isEqualTo(userId);
        assertThat(userDisplayOrderDO.getMaxCorpusDisplayOrder()).isNotNull();
        assertThat(userDisplayOrderDO.getMaxKanbanDisplayOrder()).isNotNull();
        assertThat(userDisplayOrderDO.getMaxStarCorpusDisplayOrder()).isNotNull();
        assertThat(userDisplayOrderDO.getMaxStarKanbanDisplayOrder()).isNotNull();
        assertThat(userDisplayOrderDO.getMinCorpusDisplayOrder()).isNotNull();
        assertThat(userDisplayOrderDO.getMinKanbanDisplayOrder()).isNotNull();
        assertThat(userDisplayOrderDO.getMinStarCorpusDisplayOrder()).isNotNull();
        assertThat(userDisplayOrderDO.getMinStarKanbanDisplayOrder()).isNotNull();

        WbUserUiSettingDO userUiSettingDO = userManageService.getUserUiSettingByUserId(userId);
        assertThat(userUiSettingDO).isNotNull();
        checkDateCloseToNow(userUiSettingDO.getDateCreated(), userUiSettingDO.getLastUpdated());
        assertThat(userUiSettingDO.getUserId()).isEqualTo(userId);

        WbUserFunctionSettingDO userFunctionSettingDO = userManageService.getUserFunctionSettingByUserId(userId);
        assertThat(userFunctionSettingDO).isNotNull();
        assertThat(userFunctionSettingDO.getUserId()).isEqualTo(userId);
    }

    @Test
    public void testSaveUserInfoWithUnionIdExists() throws Exception {
        WbAccountDO orgAccountDO = TestUserFactory.getAccountDO();
        userManageService.saveOrUpdateAccount(orgAccountDO);
        WbUserOauthDO userOauthDO = TestUserFactory.getUserOauthDO(orgAccountDO.getId());
        userManageService.saveOrUpdateUserOauth(userOauthDO);
        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterCorpId(globalOuterDataTeam.getOuterId());
        userVO.setOuterUnionId(userOauthDO.getDdUnionId());
        Long teamId = globalTeam.getId();
        userManageService.saveUserInfo(teamId, userVO);
        WbUserDO userDO = userManageService.getUserByClientAndOuterId(userVO.getClient(), userVO.getOuterCombineId());
        assertThat(userDO).isNotNull();
        assertThat(userDO.getId()).isNotNull();
        assertThat(userDO.getAccountId()).isNotNull();
        assertThat(userDO.getUsername()).isNotNull();
        assertThat(userDO.getDisplay()).isTrue();
        assertThat(userDO.getParent()).isFalse();
        assertThat(userDO.getName()).isEqualTo(userVO.getName());
        assertThat(userDO.getTeamId()).isEqualTo(teamId);

        WbAccountDO accountDO = userManageService.getAccountById(userDO.getAccountId());
        assertThat(accountDO).isNotNull();
        assertThat(accountDO.getId()).isEqualTo(orgAccountDO.getId());
        assertThat(accountDO.getUuid()).isEqualTo(orgAccountDO.getUuid());
    }

    @Test
    public void testSaveUserInfoWithoutUnionId() throws Exception {
        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterCorpId(globalOuterDataTeam.getOuterId());
        Long teamId = globalTeam.getId();
        userManageService.saveUserInfo(teamId, userVO);
        WbUserDO userDO = userManageService.getUserByClientAndOuterId(userVO.getClient(), userVO.getOuterCombineId());
        assertThat(userDO).isNotNull();
        assertThat(userDO.getId()).isNotNull();
        assertThat(userDO.getAccountId()).isNotNull();
        assertThat(userDO.getUsername()).isNotNull();
        assertThat(userDO.getDisplay()).isTrue();
        assertThat(userDO.getParent()).isFalse();
        assertThat(userDO.getName()).isEqualTo(userVO.getName());
        assertThat(userDO.getTeamId()).isEqualTo(teamId);

        WbAccountDO accountDO = userManageService.getAccountById(userDO.getAccountId());
        assertThat(accountDO).isNotNull();
        assertThat(accountDO.getId()).isNotNull();
        assertThat(accountDO.getAvatar()).isEqualTo(userVO.getAvatar());
        assertThat(accountDO.getName()).isEqualTo(userVO.getName());
        assertThat(accountDO.getUuid()).isNotNull();

        //  如果没有unionId，那么不会保存userOauth
        WbUserOauthDO userOauthDO = userManageService.getUserOauthByAccountId(accountDO.getId());
        assertThat(userOauthDO).isNull();
    }

    @Test
    public void testUpdateUserInfo() throws Exception {
        Date now = new Date();
        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterCorpId(globalOuterDataTeam.getOuterId());
        userVO.setOuterUnionId("at_user_union_id_" + now.getTime());
        Long teamId = globalTeam.getId();
        userManageService.saveUserInfo(teamId, userVO);

        //  通过clone保存旧的object
        WbUserDO userDO = (WbUserDO) SerializationUtils.clone(
                userManageService.getUserByClientAndOuterId(userVO.getClient(), userVO.getOuterCombineId()));
        Long userId = userDO.getId();
        WbAccountDO accountDO = (WbAccountDO) SerializationUtils.clone(
                userManageService.getAccountById(userDO.getAccountId()));
        Long accountId = accountDO.getId();
        WbUserRegisterDO userRegisterDO = (WbUserRegisterDO) SerializationUtils.clone(
                userManageService.getUserRegisterByAccountId(accountId));
        WbUserOauthDO userOauthDO = (WbUserOauthDO) SerializationUtils.clone(
                userManageService.getUserOauthByAccountId(accountId));
        WbOuterDataUserDO outerDataUserDO = (WbOuterDataUserDO) SerializationUtils.clone(
                userManageService.getOuterDataUserByClientAndOuterId(userVO.getClient(), userVO.getOuterCombineId()));
        WbUserGuideDO userGuideDO = (WbUserGuideDO) SerializationUtils.clone(
                userManageService.getUserGuideByUserId(userId));
        WbUserDisplayOrderDO userDisplayOrderDO = (WbUserDisplayOrderDO) SerializationUtils.clone(
                userManageService.getUserDisplayOrderByUserId(userId));
        WbUserUiSettingDO userUiSettingDO = (WbUserUiSettingDO) SerializationUtils.clone(
                userManageService.getUserUiSettingByUserId(userId));
        WbUserFunctionSettingDO userFunctionSettingDO = (WbUserFunctionSettingDO) SerializationUtils.clone(
                userManageService.getUserFunctionSettingByUserId(userId));
//        WbUserDO cloned = (WbUserDO)SerializationUtils.clone(userDO);


        //  update after 100ms
        Thread.sleep(100);
        AutoCreateUserVO userVO2 = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterUnionId("at_user_union_id_" + new Date().getTime());
        WbUserDO userDOForUpdate = userManageService.getUserByClientAndOuterId(userVO.getClient(), userVO.getOuterCombineId());
        userManageService.updateUserInfo(userDOForUpdate, userVO2);
        WbUserDO userDO2 = userManageService.getUserById(userDOForUpdate.getId());
        Long userId2 = userDO2.getId();
        WbAccountDO accountDO2 = userManageService.getAccountById(userDO2.getAccountId());
        Long accountId2 = accountDO2.getId();
        WbUserRegisterDO userRegisterDO2 = userManageService.getUserRegisterByAccountId(accountId2);
        WbUserOauthDO userOauthDO2 = userManageService.getUserOauthByAccountId(accountId2);
        WbOuterDataUserDO outerDataUserDO2 = userManageService.getOuterDataUserByClientAndOuterId(
                userVO.getClient(), userVO.getOuterCombineId());
        WbUserGuideDO userGuideDO2 = userManageService.getUserGuideByUserId(userId2);
        WbUserDisplayOrderDO userDisplayOrderDO2 = userManageService.getUserDisplayOrderByUserId(userId2);
        WbUserUiSettingDO userUiSettingDO2 = userManageService.getUserUiSettingByUserId(userId2);
        WbUserFunctionSettingDO userFunctionSettingDO2 = userManageService.getUserFunctionSettingByUserId(userId2);

        assertThat(userDO2.getName()).isEqualTo(userVO2.getName());
        assertThat(userDO2.getName()).isNotEqualTo(userDO.getName());
        assertThat(accountDO2.getName()).isEqualTo(userVO2.getName());
        assertThat(accountDO2.getName()).isNotEqualTo(accountDO.getName());
        assertThat(accountDO2.getAvatar()).isEqualTo(userVO2.getAvatar());
        assertThat(accountDO2.getAvatar()).isNotEqualTo(accountDO.getAvatar());
        assertThat(userDO2).isEqualToIgnoringGivenFields(userDO, "name");
        assertThat(accountDO2).isEqualToIgnoringGivenFields(accountDO, "name", "avatar");
        assertThat(userOauthDO2).isEqualToIgnoringGivenFields(userOauthDO, "ddUnionId");
        assertThat(userRegisterDO2).isEqualToComparingFieldByFieldRecursively(userRegisterDO);
        assertThat(outerDataUserDO2).isEqualToComparingFieldByFieldRecursively(outerDataUserDO);
        assertThat(userGuideDO2).isEqualToComparingFieldByFieldRecursively(userGuideDO);
        assertThat(userDisplayOrderDO2).isEqualToComparingFieldByFieldRecursively(userDisplayOrderDO);
        assertThat(userUiSettingDO2).isEqualToComparingFieldByFieldRecursively(userUiSettingDO);
        assertThat(userFunctionSettingDO2).isEqualToComparingFieldByFieldRecursively(userFunctionSettingDO);
    }
}