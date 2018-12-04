package com.workbei.manager.user.impl;

import com.workbei.BaseUnitTest;
import com.workbei.constant.WbConstant;
import com.workbei.model.domain.user.*;
import com.workbei.model.view.autocreate.AutoCreateTeamVO;
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
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional(transactionManager = "transactionManager")
@Rollback
public class TeamManagerImplTest extends BaseUnitTest {
    @Autowired
    private TeamManager teamManager;
    @Autowired
    private UserManager userManager;

    private WbTeamDO globalTeam;
    private WbAccountDO globalAccount;
    private WbUserDO globalUser;

    @Before
    public void setUp() throws Exception {
        WbTeamDO teamDO = TestTeamFactory.getTeamDO();
        teamManager.saveOrUpdateTeam(teamDO);
        WbAccountDO accountDO = TestUserFactory.getAccountDO();
        userManager.saveOrUpdateAccount(accountDO);
        WbUserDO userDO = TestUserFactory.getUserDO();
        userDO.setTeamId(teamDO.getId());
        userDO.setAccountId(accountDO.getId());
        userManager.saveOrUpdateUser(userDO);
        globalTeam = teamDO;
        globalAccount = accountDO;
        globalUser = userDO;
    }

    @Test
    public void testSaveTeamInfo() throws Exception {
        //  检查team
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        WbTeamDO teamSaved = teamManager.saveTeamInfo(teamVO);
        WbTeamDO teamDO = teamManager.getTeamById(teamSaved.getId());
        checkDateCloseToNow(teamDO.getDateCreated(), teamDO.getLastUpdated());
        assertThat(teamDO).as("检查保存后返回的teamDO").isNotNull();
        assertThat(teamDO.getId()).as("检查保存后id").isNotNull();
        assertThat(teamDO.getUuid()).as("检查保存后uuid").isNotNull();
        assertThat(teamDO.getName()).as("检查name")
                .isEqualTo(teamVO.getName());
        assertThat(teamDO.getDisplay()).as("检查isDisplay")
                .isTrue();
        //  默认logo为null
        assertThat(teamDO.getLogo()).as("检查logo")
                .isEqualTo(WbConstant.TEAM_DEFAULT_LOGO);
        //  检查teamData
        WbTeamDataDO teamDataDO = teamManager.getTeamDataByTeamId(teamDO.getId());
        assertThat(teamDataDO).as("检查teamDataDO").isNotNull();
        checkDateCloseToNow(teamDataDO.getDateCreated(), teamDataDO.getLastUpdated());
        assertThat(teamDataDO.getTeamId()).as("检查teamId")
                .isEqualTo(teamDO.getId());
        assertThat(teamDataDO.getClient()).as("检查teamDataDO的client")
                .isEqualTo(teamVO.getClient());
        assertThat(teamDataDO.getOuterId()).as("检查teamDataDO的outerId")
                .isEqualTo(teamVO.getOuterCorpId());
        assertThat(teamDataDO.getCanTel()).as("检查teamDataDO的canTel")
                .isEqualTo(WbConstant.APP_DEFAULT_TEAM_CAN_TEL);
        assertThat(teamDataDO.getIndustry()).as("检查teamDataDO的industry")
                .isEqualTo(WbConstant.APP_DEFAULT_TEAM_INDUSTRY);
        assertThat(teamDataDO.getContacts()).as("检查teamDataDO的contacts")
                .isEqualTo(WbConstant.APP_DEFAULT_TEAM_CONTACT_NAME);
        //  检查teamUser
        WbTeamUserDO teamUserDO = teamManager.getTeamUserByTeamId(teamDO.getId());
        assertThat(teamUserDO).as("检查teamUser").isNotNull();
        checkDateCloseToNow(teamUserDO.getDateCreated(), teamUserDO.getLastUpdated());
        assertThat(teamUserDO.getTeamId()).as("检查teamId")
                .isEqualTo(teamDO.getId());
        assertThat(teamUserDO.getWhoCanInviteColleague()).as("检查whoCanInviteColleague")
                .isEqualTo(WbConstant.TEAM_WHO_CAN_INVITE_ALL);
    }

    @Test
    public void testSaveTeamInfoWithLogo() throws Exception {
        //  检查logo的存储
        Date now = new Date();
        String logo = "auto_create_team_logo_" + now.getTime();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setLogo(logo);
        WbTeamDO teamDO = teamManager.saveTeamInfo(teamVO);
        assertThat(teamDO).as("检查保存后返回的teamDO").isNotNull();
        assertThat(teamDO.getId()).as("检查保存后id").isNotNull();
        assertThat(teamDO.getLogo()).as("检查logo")
                .isEqualTo(teamVO.getLogo());
    }

    @Test
    public void testSaveTeamInfoCreator() throws Exception {
        //  检查contactName
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        teamVO.setCreator(userVO);
        WbTeamDO teamDO = teamManager.saveTeamInfo(teamVO);
        assertThat(teamDO).as("检查保存后返回的teamDO").isNotNull();
        assertThat(teamDO.getId()).as("检查保存后id").isNotNull();

        WbTeamDataDO teamDataDO = teamManager.getTeamDataByTeamId(teamDO.getId());
        assertThat(teamDataDO.getContacts()).as("检查teamDataDO的contacts")
                .isEqualTo(userVO.getName());

        AutoCreateTeamVO teamVO2 = TestTeamFactory.getAutoCreateTeamVO();
        AutoCreateUserVO userVO2 = TestUserFactory.getAutoCreateUserVO();
        userVO2.setName(null);
        teamVO2.setCreator(userVO2);
        WbTeamDO teamDO2 = teamManager.saveTeamInfo(teamVO2);
        assertThat(teamDO2).as("检查保存后返回的teamDO2").isNotNull();
        assertThat(teamDO2.getId()).as("检查保存后id").isNotNull();

        WbTeamDataDO teamDataDO2 = teamManager.getTeamDataByTeamId(teamDO2.getId());
        assertThat(teamDataDO2.getContacts()).as("检查teamDataDO2的contacts")
                .isEqualTo(WbConstant.APP_DEFAULT_TEAM_CONTACT_NAME);
    }

    @Test
    public void testSaveTeamInfoOuterId() throws Exception {
        //  检查outerId存在时的outerDataTeam的存储
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        WbTeamDO teamDO = teamManager.saveTeamInfo(teamVO);
        WbOuterDataTeamDO outerDataTeamDO = teamManager.getOuterDataTeamByClientAndOuterId(
                teamVO.getClient(), teamVO.getOuterCorpId()
        );
        assertThat(outerDataTeamDO).as("检查outerDataTeam").isNotNull();
        assertThat(outerDataTeamDO.getId()).as("检查id").isNotNull();
        checkDateCloseToNow(outerDataTeamDO.getDateCreated(), outerDataTeamDO.getLastUpdated());
        assertThat(outerDataTeamDO.getTeamId()).as("检查outerDataTeam teamId")
                .isEqualTo(teamDO.getId());
        assertThat(outerDataTeamDO.getClient()).as("检查client")
                .isEqualTo(teamVO.getClient());
        assertThat(outerDataTeamDO.getOuterId()).as("检查outerId")
                .isEqualTo(teamVO.getOuterCorpId());
    }

    @Test
    public void testSaveTeamCreator() throws Exception {
        Long teamId = globalTeam.getId();
        Long userId = globalUser.getId();
        teamManager.saveTeamCreator(teamId, userId);
        List<WbTeamUserRoleDO> roleList = teamManager.listTeamUserRoleByTeamIdAndUserId(teamId, userId);
        assertThat(roleList).hasSize(2);
        WbTeamUserRoleDO teamUserRoleDO1 = roleList.get(0);
        checkDateCloseToNow(teamUserRoleDO1.getDateCreated(), teamUserRoleDO1.getLastUpdated());
        WbTeamUserRoleDO teamUserRoleDO2 = roleList.get(1);
        checkDateCloseToNow(teamUserRoleDO2.getDateCreated(), teamUserRoleDO2.getLastUpdated());
        assertThat(roleList).as("检查teamUserRoleList")
                .extracting("teamId", "userId", "role")
                .contains(tuple(teamId, userId, WbConstant.TEAM_USER_ROLE_CREATOR),
                        tuple(teamId, userId, WbConstant.TEAM_USER_ROLE_ADMIN));

        List<WbJoinAndQuitTeamRecordDO> recordList = teamManager.listJoinAndQuitTeamRecordByTeamIdAndUserId(
                teamId, userId);
        assertThat(recordList).as("检查joinAndQuitTeamRecordDO").hasSize(1);
        assertThat(recordList).as("检查joinAndQuitTeamRecordList")
                .extracting("teamId", "userId", "type")
                .contains(tuple(teamId, userId, WbConstant.TEAM_RECORD_TYPE_CREATE));
    }

    @Test
    public void testSaveTeamCommonUser() throws Exception {
        Long teamId = globalTeam.getId();
        Long userId = globalUser.getId();
        teamManager.saveTeamCommonUser(teamId, userId);
        List<WbTeamUserRoleDO> roleList = teamManager.listTeamUserRoleByTeamIdAndUserId(teamId, userId);
        assertThat(roleList).hasSize(0);

        List<WbJoinAndQuitTeamRecordDO> recordList = teamManager.listJoinAndQuitTeamRecordByTeamIdAndUserId(
                teamId, userId);
        assertThat(recordList).as("检查joinAndQuitTeamRecordDO").hasSize(1);
        assertThat(recordList).as("检查joinAndQuitTeamRecordList")
                .extracting("teamId", "userId", "type")
                .contains(tuple(teamId, userId, WbConstant.TEAM_RECORD_TYPE_JOIN));
    }

    @Test
    public void testRemoveTeamCreator() throws Exception {
        Long teamId = globalTeam.getId();
        Long userId = globalUser.getId();
        teamManager.saveTeamCreator(teamId, userId);
        teamManager.removeTeamUser(teamId, userId);
        List<WbTeamUserRoleDO> roleList = teamManager.listTeamUserRoleByTeamIdAndUserId(teamId, userId);
        assertThat(roleList).hasSize(0);

        List<WbJoinAndQuitTeamRecordDO> recordList = teamManager.listJoinAndQuitTeamRecordByTeamIdAndUserId(
                globalTeam.getId(), globalUser.getId()
        );
        assertThat(recordList).as("检查joinAndQuitTeamRecordDOList size").hasSize(2);
        assertThat(recordList).as("检查joinAndQuitTeamRecordDOList")
                .extracting("teamId", "userId", "type")
                .contains(tuple(teamId, userId, WbConstant.TEAM_RECORD_TYPE_CREATE),
                        tuple(teamId, userId, WbConstant.TEAM_RECORD_TYPE_QUIT));
    }

    @Test
    public void testRemoveTeamCommonUser() throws Exception {
        Long teamId = globalTeam.getId();
        Long userId = globalUser.getId();
        teamManager.saveTeamCommonUser(teamId, userId);

        teamManager.removeTeamUser(teamId, userId);
        List<WbTeamUserRoleDO> roleList = teamManager.listTeamUserRoleByTeamIdAndUserId(teamId, userId);
        assertThat(roleList).hasSize(0);

        List<WbJoinAndQuitTeamRecordDO> recordList = teamManager.listJoinAndQuitTeamRecordByTeamIdAndUserId(
                globalTeam.getId(), globalUser.getId()
        );
        assertThat(recordList).as("检查joinAndQuitTeamRecordDOList size").hasSize(2);
        assertThat(recordList).as("检查joinAndQuitTeamRecordDOList")
                .extracting("teamId", "userId", "type")
                .contains(tuple(teamId, userId, WbConstant.TEAM_RECORD_TYPE_JOIN),
                        tuple(teamId, userId, WbConstant.TEAM_RECORD_TYPE_QUIT));
    }

    @Test
    public void testUpdateAdminSetAdmin() throws Exception {
        Long teamId = globalTeam.getId();
        Long userId = globalUser.getId();
        teamManager.saveTeamCommonUser(teamId, userId);

        teamManager.updateTeamAdmin(teamId, userId, true);
        WbTeamUserRoleDO adminRole = teamManager.getTeamUserRoleByTeamIdAndUserIdAndRole(
                teamId, userId, WbConstant.TEAM_USER_ROLE_ADMIN);
        assertThat(adminRole).isNotNull();
        checkDateCloseToNow(adminRole.getDateCreated(), adminRole.getLastUpdated());
        List<WbTeamUserRoleDO> roleList = teamManager.listTeamUserRoleByTeamIdAndUserId(
                teamId, userId);

        teamManager.updateTeamAdmin(teamId, userId, true);
        List<WbTeamUserRoleDO> roleListAfterRepeatUpdate = teamManager.listTeamUserRoleByTeamIdAndUserId(
                teamId, userId);
        assertThat(roleListAfterRepeatUpdate).hasSameSizeAs(roleList);
        assertThat(roleListAfterRepeatUpdate).extracting("teamId", "userId", "role")
                .contains(tuple(teamId, userId, WbConstant.TEAM_USER_ROLE_ADMIN));

        teamManager.updateTeamAdmin(teamId, userId, false);
        WbTeamUserRoleDO adminRoleAfterDelete = teamManager.getTeamUserRoleByTeamIdAndUserIdAndRole(
                teamId, userId, WbConstant.TEAM_USER_ROLE_ADMIN);
        assertThat(adminRoleAfterDelete).isNull();
        List<WbTeamUserRoleDO> roleListAfterDelete = teamManager.listTeamUserRoleByTeamIdAndUserId(
                teamId, userId);

        teamManager.updateTeamAdmin(teamId, userId, false);
        List<WbTeamUserRoleDO> roleListAfterRepeatDelete = teamManager.listTeamUserRoleByTeamIdAndUserId(
                teamId, userId);
        assertThat(roleListAfterRepeatDelete).hasSameSizeAs(roleListAfterDelete);
        assertThat(roleListAfterRepeatDelete).extracting(WbTeamUserRoleDO::getRole)
                .doesNotContain(WbConstant.TEAM_USER_ROLE_ADMIN);
    }
}