package com.workbei.manager.user.impl;

import com.workbei.manager.user.AccountManager;
import org.apache.commons.lang.SerializationUtils;
import com.workbei.BaseUnitTest;
import com.workbei.constant.WbConstant;
import com.workbei.model.domain.user.*;
import com.workbei.model.view.autocreate.AutoCreateUserVO;
import com.workbei.manager.user.TeamManager;
import com.workbei.manager.user.UserManager;
import com.workbei.util.TestTeamFactory;
import com.workbei.util.TestUserFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.assertj.core.api.Assertions.*;

@Transactional(transactionManager = "transactionManager")
@Rollback
public class UserManagerImplTest extends BaseUnitTest {
    @Autowired
    private UserManager userManager;
    @Autowired
    private TeamManager teamManager;
    @Autowired
    private AccountManager accountManager;

    private WbAccountDO globalAccount;
    private WbTeamDO globalTeam;
    private WbOuterDataTeamDO globalOuterDataTeam;

    @Before
    public void setUp() throws Exception {
        WbTeamDO teamDO = TestTeamFactory.getTeamDO();
        teamManager.saveOrUpdateTeam(teamDO);
        WbAccountDO accountDO = TestUserFactory.getAccountDO();
        accountManager.saveOrUpdateAccount(accountDO);
        WbOuterDataTeamDO outerDataTeamDO = TestTeamFactory.getOuterDataTeam(teamDO.getId());
        teamManager.saveOrUpdateOuterDataTeam(outerDataTeamDO);
        globalAccount = accountDO;
        globalTeam = teamDO;
        globalOuterDataTeam = outerDataTeamDO;
    }

    @Test
    public void testSaveUserInfo() throws Exception {
        Date now = new Date();
        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterCorpId(globalOuterDataTeam.getOuterId());
        Long teamId = globalTeam.getId();
        userManager.saveUserInfo(teamId, globalAccount.getId(), userVO);
        WbUserDO userDO = userManager.getUserByClientAndOuterId(userVO.getClient(), userVO.getOuterCombineId());
        assertThat(userDO).isNotNull();
        assertThat(userDO.getId()).isNotNull();
        assertThat(userDO.getAccountId()).isNotNull();
        assertThat(userDO.getUsername()).isNotNull();
        assertThat(userDO.getDisplay()).isTrue();
        assertThat(userDO.getParent()).isTrue();
        assertThat(userDO.getName()).isEqualTo(userVO.getName());
        assertThat(userDO.getTeamId()).isEqualTo(teamId);

        Long userId = userDO.getId();
        WbAccountDO accountDO = accountManager.getAccountById(userDO.getAccountId());
        assertThat(accountDO).isNotNull();
        assertThat(accountDO.getId()).isEqualTo(globalAccount.getId());

        WbOuterDataUserDO outerDataUserDO = userManager.getOuterDataUserByClientAndOuterId(
                userVO.getClient(), userVO.getOuterCombineId());
        assertThat(outerDataUserDO).isNotNull();
        checkDateCloseToNow(outerDataUserDO.getDateCreated(), outerDataUserDO.getLastUpdated());
        assertThat(outerDataUserDO.getUserId()).isEqualTo(userId);
        assertThat(outerDataUserDO.getOuterId()).isEqualTo(userVO.getOuterCombineId());
        assertThat(outerDataUserDO.getClient()).isEqualTo(userVO.getClient());

        WbUserGuideDO userGuideDO = userManager.getUserGuideByUserId(userId);
        assertThat(userGuideDO).isNotNull();
        assertThat(userGuideDO.getUserId()).isEqualTo(userId);
        assertThat(userGuideDO.getGuideKanbanFlag()).isTrue();
        assertThat(userGuideDO.getGuideNoteFlag()).isTrue();
        assertThat(userGuideDO.getGuideTaskFlag()).isTrue();
        assertThat(userGuideDO.getGuideWebFlag()).isTrue();

        WbUserDisplayOrderDO userDisplayOrderDO = userManager.getUserDisplayOrderByUserId(userId);
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

        WbUserUiSettingDO userUiSettingDO = userManager.getUserUiSettingByUserId(userId);
        assertThat(userUiSettingDO).isNotNull();
        checkDateCloseToNow(userUiSettingDO.getDateCreated(), userUiSettingDO.getLastUpdated());
        assertThat(userUiSettingDO.getUserId()).isEqualTo(userId);

        WbUserFunctionSettingDO userFunctionSettingDO = userManager.getUserFunctionSettingByUserId(userId);
        assertThat(userFunctionSettingDO).isNotNull();
        assertThat(userFunctionSettingDO.getUserId()).isEqualTo(userId);
    }

    @Test
    public void testUpdateUserInfo() throws Exception {
        Date now = new Date();
        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterCorpId(globalOuterDataTeam.getOuterId());
        userVO.setOuterUnionId("at_user_union_id_" + now.getTime());
        Long teamId = globalTeam.getId();
        Long accountId = globalAccount.getId();
        userManager.saveUserInfo(teamId, accountId, userVO);

        //  通过clone保存旧的object
        WbUserDO userDO = (WbUserDO) SerializationUtils.clone(
                userManager.getUserByClientAndOuterId(userVO.getClient(), userVO.getOuterCombineId()));
        Long userId = userDO.getId();
        WbOuterDataUserDO outerDataUserDO = (WbOuterDataUserDO) SerializationUtils.clone(
                userManager.getOuterDataUserByClientAndOuterId(userVO.getClient(), userVO.getOuterCombineId()));
        WbUserGuideDO userGuideDO = (WbUserGuideDO) SerializationUtils.clone(
                userManager.getUserGuideByUserId(userId));
        WbUserDisplayOrderDO userDisplayOrderDO = (WbUserDisplayOrderDO) SerializationUtils.clone(
                userManager.getUserDisplayOrderByUserId(userId));
        WbUserUiSettingDO userUiSettingDO = (WbUserUiSettingDO) SerializationUtils.clone(
                userManager.getUserUiSettingByUserId(userId));
        WbUserFunctionSettingDO userFunctionSettingDO = (WbUserFunctionSettingDO) SerializationUtils.clone(
                userManager.getUserFunctionSettingByUserId(userId));
//        WbUserDO cloned = (WbUserDO)SerializationUtils.clone(userDO);


        //  update after 100ms
        Thread.sleep(100);
        AutoCreateUserVO userVO2 = TestUserFactory.getAutoCreateUserVO();
        userVO2.setOuterUnionId("at_user_union_id_" + new Date().getTime());
        userVO2.setOuterCorpId(userVO.getOuterCorpId());
        userVO2.setOuterCombineId(userVO.getOuterCombineId());
        WbUserDO userDOForUpdate = userManager.getUserByClientAndOuterId(userVO.getClient(), userVO.getOuterCombineId());
        userManager.updateUserInfo(userVO2);
        WbUserDO userDO2 = userManager.getUserById(userDOForUpdate.getId());
        Long userId2 = userDO2.getId();
        WbOuterDataUserDO outerDataUserDO2 = userManager.getOuterDataUserByClientAndOuterId(
                userVO.getClient(), userVO.getOuterCombineId());
        WbUserGuideDO userGuideDO2 = userManager.getUserGuideByUserId(userId2);
        WbUserDisplayOrderDO userDisplayOrderDO2 = userManager.getUserDisplayOrderByUserId(userId2);
        WbUserUiSettingDO userUiSettingDO2 = userManager.getUserUiSettingByUserId(userId2);
        WbUserFunctionSettingDO userFunctionSettingDO2 = userManager.getUserFunctionSettingByUserId(userId2);

        assertThat(userDO2.getName()).isEqualTo(userVO2.getName());
        assertThat(userDO2.getName()).isNotEqualTo(userDO.getName());
        assertThat(userDO2).isEqualToIgnoringGivenFields(userDO, "name", "version");
        assertThat(outerDataUserDO2).isEqualToComparingFieldByFieldRecursively(outerDataUserDO);
        assertThat(userGuideDO2).isEqualToComparingFieldByFieldRecursively(userGuideDO);
        assertThat(userDisplayOrderDO2).isEqualToComparingFieldByFieldRecursively(userDisplayOrderDO);
        assertThat(userUiSettingDO2).isEqualToComparingFieldByFieldRecursively(userUiSettingDO);
        assertThat(userFunctionSettingDO2).isEqualToComparingFieldByFieldRecursively(userFunctionSettingDO);
    }
}