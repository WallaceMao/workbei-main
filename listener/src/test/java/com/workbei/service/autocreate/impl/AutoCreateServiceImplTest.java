package com.workbei.service.autocreate.impl;

import com.workbei.BaseUnitTest;
import com.workbei.constant.TestV2Constant;
import com.workbei.constant.WbConstant;
import com.workbei.exception.ExceptionCode;
import com.workbei.exception.WorkbeiServiceException;
import com.workbei.manager.user.*;
import com.workbei.model.domain.user.*;
import com.workbei.model.view.autocreate.AutoCreateDepartmentVO;
import com.workbei.model.view.autocreate.AutoCreateTeamVO;
import com.workbei.model.view.autocreate.AutoCreateUserVO;
import com.workbei.service.autocreate.AutoCreateService;
import com.workbei.util.TestDepartmentFactory;
import com.workbei.util.TestTeamFactory;
import com.workbei.util.TestUserFactory;
import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.workbei.exception.ExceptionCode.*;
import static org.assertj.core.api.Assertions.*;

@Transactional(transactionManager = "transactionManager")
@Rollback
public class AutoCreateServiceImplTest extends BaseUnitTest {
    @Autowired
    private AutoCreateService autoCreateService;
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private TeamManager teamManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private RoleManager roleManager;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testCreateTeam() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        teamVO.setClient(null);
        assertThatThrownBy(() -> autoCreateService.createTeam(teamVO))
                .isInstanceOf(WorkbeiServiceException.class)
                .hasMessageStartingWith("13002");

        teamVO.setClient(TestV2Constant.CLIENT_FAKED);
        autoCreateService.createTeam(teamVO);
        WbTeamDO teamDO = teamManager.getTeamByClientAndOuterId(
                teamVO.getClient(), teamVO.getOuterCorpId()
        );
        Long teamId = teamDO.getId();
        assertThat(teamVO.getId()).isEqualTo(teamId);
        WbOuterDataTeamDO outerDataTeamDO = teamManager.getOuterDataTeamByClientAndOuterId(
                teamVO.getClient(), teamVO.getOuterCorpId());
        assertThat(outerDataTeamDO.getClient()).isEqualTo(teamVO.getClient());

        WbDepartmentDO topDepartment = departmentManager.getTeamTopDepartment(teamId);
        WbDepartmentDO unassignedDepartment = departmentManager.getTeamUnassignedDepartment(teamId);
        assertThat(topDepartment).isNotNull();
        assertThat(topDepartment.getTeamId()).isEqualTo(teamId);
        assertThat(unassignedDepartment).isNotNull();
        assertThat(unassignedDepartment.getTeamId()).isEqualTo(teamId);
    }

    @Test
    public void testCreateTeamWithCreator() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        //  测试当client为null的情况，可以使用系统默认的client
        userVO.setClient(null);
        teamVO.setCreator(userVO);
        autoCreateService.createTeam(teamVO);
        WbUserDO creatorDO = userManager.getUserByClientAndOuterId(
                userVO.getClient(), userVO.getOuterCombineId()
        );
        assertThat(creatorDO.getTeamId()).isEqualTo(teamVO.getId());
        WbRoleGroupDO roleGroupDO = roleManager.getCommonRoleGroup();
        WbUserRoleGroupDO creatorRoleGroupDO = roleManager.getUserRoleGroupByUserId(creatorDO.getId());
        assertThat(creatorRoleGroupDO.getRoleGroupId()).isEqualTo(roleGroupDO.getId());
        WbTeamUserRoleDO creatorRoleDO = teamManager.getTeamUserRoleByTeamIdAndUserIdAndRole(
                teamVO.getId(), creatorDO.getId(), WbConstant.TEAM_USER_ROLE_CREATOR
        );
        assertThat(creatorRoleDO).isNotNull();
        WbDepartmentDO departmentDO = departmentManager.getTeamUnassignedDepartment(teamVO.getId());
        WbUserDeptDO creatorDeptDO = departmentManager.getUserDeptByDepartmentIdAndUserId(
                departmentDO.getId(), creatorDO.getId()
        );
        assertThat(creatorDeptDO).isNotNull();
    }

    @Test
    public void testCreateDepartmentWithTeamNotExist() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        teamVO.setCreator(userVO);
        autoCreateService.createTeam(teamVO);

        AutoCreateDepartmentVO departmentVO = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        departmentVO.setOuterCorpId("at_test_team_random_outer_id");
        assertThatThrownBy(() -> autoCreateService.createDepartment(departmentVO))
                .hasMessageStartingWith(ExceptionCode.getMessage(TEAM_NOT_FOUND));
    }

    @Test
    public void testCreateDepartmentWithTeam() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        teamVO.setCreator(userVO);
        autoCreateService.createTeam(teamVO);

        AutoCreateDepartmentVO departmentVO = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        departmentVO.setClient(null);
        assertThatThrownBy(() -> autoCreateService.createDepartment(departmentVO))
                .isInstanceOf(WorkbeiServiceException.class)
                .hasMessageStartingWith("13002");
        departmentVO.setClient(TestV2Constant.CLIENT_FAKED);
        autoCreateService.createDepartment(departmentVO);
        assertThat(departmentVO.getId()).isNotNull();
        WbDepartmentDO departmentDO = departmentManager.getDepartmentById(departmentVO.getId());
        assertThat(departmentDO.getTeamId()).isEqualTo(teamVO.getId());
    }

    @Test
    public void testUpdateDepartment() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        teamVO.setCreator(userVO);
        autoCreateService.createTeam(teamVO);
        AutoCreateDepartmentVO departmentVO = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        autoCreateService.createDepartment(departmentVO);
        departmentVO.setName("at_new_department_name" + now.getTime());
        departmentVO.setDisplayOrder((double) now.getTime());
        departmentVO.setClient(null);
        assertThatThrownBy(() -> autoCreateService.updateDepartment(departmentVO))
                .isInstanceOf(WorkbeiServiceException.class)
                .hasMessageStartingWith("13002");
        departmentVO.setClient(TestV2Constant.CLIENT_FAKED);
        autoCreateService.updateDepartment(departmentVO);

        WbDepartmentDO departmentDO = departmentManager.getDepartmentById(departmentVO.getId());
        assertThat(departmentDO.getName()).isEqualTo(departmentVO.getName());
        assertThat(departmentDO.getDisplayOrder()).isEqualTo(departmentVO.getDisplayOrder());
    }

    @Test
    public void testDeleteDepartment() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        autoCreateService.createTeam(teamVO);
        AutoCreateDepartmentVO departmentVO = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        autoCreateService.createDepartment(departmentVO);
        WbDepartmentDO departmentDO = departmentManager.getDepartmentById(departmentVO.getId());
        assertThat(departmentDO).isNotNull();
        autoCreateService.deleteDepartment(departmentVO);
        departmentDO = departmentManager.getDepartmentById(departmentVO.getId());
        assertThat(departmentDO).isNull();
    }

    @Test
    public void testCreateUserWithTeamNotExist() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        autoCreateService.createTeam(teamVO);

        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterCorpId("at_other_team_outer_id");
        assertThatThrownBy(() -> autoCreateService.createUser(userVO))
                .hasMessageStartingWith(ExceptionCode.getMessage(TEAM_NOT_FOUND));
    }

    @Test
    public void testCreateUserWithUnionIdExist() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        autoCreateService.createTeam(teamVO);
        WbAccountDO orgAccountDO = TestUserFactory.getAccountDO();
        accountManager.saveOrUpdateAccount(orgAccountDO);
        WbUserOauthDO userOauthDO = TestUserFactory.getUserOauthDO(orgAccountDO.getId());
        accountManager.saveOrUpdateUserOauth(userOauthDO);
        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterCorpId(teamVO.getOuterCorpId());
        userVO.setOuterUnionId(userOauthDO.getDdUnionId());
        Long teamId = teamVO.getId();
        autoCreateService.createUser(userVO);

        WbUserDO userDO = userManager.getUserByClientAndOuterId(userVO.getClient(), userVO.getOuterCombineId());
        assertThat(userDO).isNotNull();
        assertThat(userDO.getId()).isNotNull();
        assertThat(userDO.getAccountId()).isNotNull();
        assertThat(userDO.getUsername()).isNotNull();
        assertThat(userDO.getDisplay()).isTrue();
        assertThat(userDO.getParent()).isTrue();
        assertThat(userDO.getName()).isEqualTo(userVO.getName());
        assertThat(userDO.getTeamId()).isEqualTo(teamId);

        WbAccountDO accountDO = accountManager.getAccountById(userDO.getAccountId());
        assertThat(accountDO).isNotNull();
        assertThat(accountDO.getId()).isEqualTo(orgAccountDO.getId());
        assertThat(accountDO.getUuid()).isEqualTo(orgAccountDO.getUuid());
    }

    @Test
    public void testCreateUserWithoutUnionId() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        autoCreateService.createTeam(teamVO);
        WbAccountDO orgAccountDO = TestUserFactory.getAccountDO();
        accountManager.saveOrUpdateAccount(orgAccountDO);
        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterCorpId(teamVO.getOuterCorpId());
        Long teamId = teamVO.getId();
        autoCreateService.createUser(userVO);

        WbUserDO userDO = userManager.getUserByClientAndOuterId(userVO.getClient(), userVO.getOuterCombineId());
        assertThat(userDO).isNotNull();
        assertThat(userDO.getId()).isNotNull();
        assertThat(userDO.getAccountId()).isNotNull();
        assertThat(userDO.getUsername()).isNotNull();
        assertThat(userDO.getDisplay()).isTrue();
        assertThat(userDO.getParent()).isTrue();
        assertThat(userDO.getName()).isEqualTo(userVO.getName());
        assertThat(userDO.getTeamId()).isEqualTo(teamId);

        WbAccountDO accountDO = accountManager.getAccountById(userDO.getAccountId());
        assertThat(accountDO).isNotNull();
        assertThat(accountDO.getId()).isNotNull();
        assertThat(accountDO.getAvatar()).isEqualTo(userVO.getAvatar());
        assertThat(accountDO.getName()).isEqualTo(userVO.getName());
        assertThat(accountDO.getUuid()).isNotNull();

        //  如果没有unionId，那么不会保存userOauth
        WbUserOauthDO userOauthDO = accountManager.getUserOauthByAccountId(accountDO.getId());
        assertThat(userOauthDO).isNull();
    }

    @Test
    public void testCreateUser() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        autoCreateService.createTeam(teamVO);
        Long teamId = teamVO.getId();

        AutoCreateDepartmentVO deptVO1 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO1.setTop(true);
        autoCreateService.createDepartment(deptVO1);
        AutoCreateDepartmentVO deptVO2 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO2.setOuterParentCombineId(deptVO1.getOuterCombineId());
        autoCreateService.createDepartment(deptVO2);

        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterCorpId(teamVO.getOuterCorpId());
        List<String> deptList = new ArrayList<>();
        deptList.add(deptVO1.getOuterCombineId());
        deptList.add(deptVO2.getOuterCombineId());
        deptList.add("at_test_random_dept_comid_" + now.getTime());
        userVO.setOuterCombineDeptIdList(deptList);
        autoCreateService.createUser(userVO);

        assertThat(userVO.getId()).isNotNull();
        Long userId = userVO.getId();
        WbRoleGroupDO roleGroupDO = roleManager.getCommonRoleGroup();
        WbUserRoleGroupDO userRoleGroupDO = roleManager.getUserRoleGroupByUserId(userId);
        assertThat(userRoleGroupDO.getRoleGroupId()).isEqualTo(roleGroupDO.getId());
        WbTeamUserRoleDO userRole = teamManager.getTeamUserRoleByTeamIdAndUserIdAndRole(
                teamId, userId, WbConstant.TEAM_USER_ROLE_COMMON
        );
        assertThat(userRole).isNotNull();
        List<Long> savedDeptList = departmentManager.listUserDeptDepartmentIdByUserId(userId);
        assertThat(savedDeptList).hasSize(2);
        assertThat(savedDeptList).containsExactlyInAnyOrder(
                deptVO1.getId(), deptVO2.getId());
        List<Long> userDeptAscriptionDeptIdList = departmentManager.listUserDeptAscriptionDepartmentIdByUserId(userId);
        assertThat(userDeptAscriptionDeptIdList).hasSize(2);
        assertThat(userDeptAscriptionDeptIdList).containsExactlyInAnyOrder(
                deptVO1.getId(), deptVO2.getId());
    }

    /**
     * 幂等性测试，当outerId已经存在的情况下
     *
     * @throws Exception
     */
    @Test
    public void testCreateUserWithOuterIdExist() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        autoCreateService.createTeam(teamVO);
        Long teamId = teamVO.getId();

        AutoCreateDepartmentVO deptVO1 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO1.setTop(true);
        autoCreateService.createDepartment(deptVO1);
        AutoCreateDepartmentVO deptVO2 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO2.setOuterParentCombineId(deptVO1.getOuterCombineId());
        autoCreateService.createDepartment(deptVO2);

        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterCorpId(teamVO.getOuterCorpId());
        List<String> deptList = new ArrayList<>();
        deptList.add(deptVO1.getOuterCombineId());
        deptList.add(deptVO2.getOuterCombineId());
        deptList.add("at_test_random_dept_comid_" + now.getTime());
        userVO.setOuterCombineDeptIdList(deptList);
        autoCreateService.createUser(userVO);

        assertThat(userVO.getId()).isNotNull();
        Long userId = userVO.getId();

        AutoCreateUserVO userVOCopy = (AutoCreateUserVO) SerializationUtils.clone(userVO);
        userVOCopy.setId(null);
        autoCreateService.createUser(userVOCopy);
        assertThat(userVOCopy.getId()).isEqualTo(userId);
    }

    /**
     * 创建用户时，如果用户的部门列表中的部门id在系统中都不存在，那么会将该部门放到未分配部门中
     * @throws Exception
     */
    @Test
    public void testCreateUserWithoutDepartment() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        autoCreateService.createTeam(teamVO);
        final Long teamId = teamVO.getId();

        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterCorpId(teamVO.getOuterCorpId());
        List<String> deptList = new ArrayList<>();
        // 设置为一个不存在的部门id
        deptList.add("at_test_random_dept_comid_" + now.getTime());
        userVO.setOuterCombineDeptIdList(deptList);
        autoCreateService.createUser(userVO);

        assertThat(userVO.getId()).isNotNull();
        Long userId = userVO.getId();
        List<Long> savedDeptList = departmentManager.listUserDeptDepartmentIdByUserId(userId);
        WbDepartmentDO unassignedDepartment = departmentManager.getTeamUnassignedDepartment(teamId);
        assertThat(savedDeptList).hasSize(1);
        assertThat(savedDeptList).contains(unassignedDepartment.getId());
    }

    /**
     * 如果create的user已存在，那么要走update流程
     * @throws Exception
     */
    @Test
    public void testCreateUserWithUserAlreadyExist() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        autoCreateService.createTeam(teamVO);
        Long teamId = teamVO.getId();

        AutoCreateDepartmentVO deptVO1 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO1.setTop(true);
        autoCreateService.createDepartment(deptVO1);
        AutoCreateDepartmentVO deptVO2 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO2.setOuterParentCombineId(deptVO1.getOuterCombineId());
        autoCreateService.createDepartment(deptVO2);
        AutoCreateDepartmentVO deptVO3 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO3.setOuterParentCombineId(deptVO1.getOuterCombineId());
        autoCreateService.createDepartment(deptVO3);

        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterCorpId(teamVO.getOuterCorpId());
        List<String> deptList = new ArrayList<>();
        deptList.add(deptVO1.getOuterCombineId());
        deptList.add(deptVO2.getOuterCombineId());
        userVO.setOuterCombineDeptIdList(deptList);
        autoCreateService.createUser(userVO);

        userVO.setName("at_new_user_name" + now.getTime());
        userVO.setAvatar("at_new_user_avatar" + now.getTime());
        List<String> newDeptList = new ArrayList<>();
        newDeptList.add(deptVO1.getOuterCombineId());
        newDeptList.add(deptVO3.getOuterCombineId());
        userVO.setOuterCombineDeptIdList(newDeptList);
        autoCreateService.createUser(userVO);

        WbUserDO userDO = userManager.getUserById(userVO.getId());
        assertThat(userDO.getName()).isEqualTo(userVO.getName());
        WbAccountDO accountDO = accountManager.getAccountById(userDO.getAccountId());
        assertThat(accountDO.getName()).isEqualTo(userVO.getName());
        assertThat(accountDO.getAvatar()).isEqualTo(userVO.getAvatar());

        List<Long> savedDeptList = departmentManager.listUserDeptDepartmentIdByUserId(userVO.getId());
        assertThat(savedDeptList).hasSize(2);
        assertThat(savedDeptList).contains(deptVO1.getId(), deptVO3.getId());
    }

    @Test
    public void testUpdateUser() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        autoCreateService.createTeam(teamVO);
        Long teamId = teamVO.getId();

        AutoCreateDepartmentVO deptVO1 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO1.setTop(true);
        autoCreateService.createDepartment(deptVO1);
        AutoCreateDepartmentVO deptVO2 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO2.setOuterParentCombineId(deptVO1.getOuterCombineId());
        autoCreateService.createDepartment(deptVO2);
        AutoCreateDepartmentVO deptVO3 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO3.setOuterParentCombineId(deptVO1.getOuterCombineId());
        autoCreateService.createDepartment(deptVO3);

        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterCorpId(teamVO.getOuterCorpId());
        List<String> deptList = new ArrayList<>();
        deptList.add(deptVO1.getOuterCombineId());
        deptList.add(deptVO2.getOuterCombineId());
        userVO.setOuterCombineDeptIdList(deptList);
        autoCreateService.createUser(userVO);

        userVO.setName("at_new_user_name" + now.getTime());
        userVO.setAvatar("at_new_user_avatar" + now.getTime());
        List<String> newDeptList = new ArrayList<>();
        newDeptList.add(deptVO1.getOuterCombineId());
        newDeptList.add(deptVO3.getOuterCombineId());
        userVO.setOuterCombineDeptIdList(newDeptList);
        autoCreateService.updateUser(userVO);

        WbUserDO userDO = userManager.getUserById(userVO.getId());
        assertThat(userDO.getName()).isEqualTo(userVO.getName());
        WbAccountDO accountDO = accountManager.getAccountById(userDO.getAccountId());
        assertThat(accountDO.getName()).isEqualTo(userVO.getName());
        assertThat(accountDO.getAvatar()).isEqualTo(userVO.getAvatar());

        List<Long> savedDeptList = departmentManager.listUserDeptDepartmentIdByUserId(userVO.getId());
        assertThat(savedDeptList).hasSize(2);
        assertThat(savedDeptList).contains(deptVO1.getId(), deptVO3.getId());
        List<Long> savedUserDeptAscriptionDeptIdList = departmentManager.listUserDeptAscriptionDepartmentIdByUserId(userVO.getId());
        assertThat(savedUserDeptAscriptionDeptIdList).hasSize(2);
        assertThat(savedUserDeptAscriptionDeptIdList).contains(deptVO1.getId(), deptVO3.getId());
    }

    /**
     * 如果更新时用户的
     */
    @Test
    public void testUpdateUserWithoutDepartment() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        autoCreateService.createTeam(teamVO);
        Long teamId = teamVO.getId();

        AutoCreateDepartmentVO deptVO1 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO1.setTop(true);
        autoCreateService.createDepartment(deptVO1);
        AutoCreateDepartmentVO deptVO2 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO2.setOuterParentCombineId(deptVO1.getOuterCombineId());
        autoCreateService.createDepartment(deptVO2);
        AutoCreateDepartmentVO deptVO3 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO3.setOuterParentCombineId(deptVO1.getOuterCombineId());
        autoCreateService.createDepartment(deptVO3);

        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterCorpId(teamVO.getOuterCorpId());
        List<String> deptList = new ArrayList<>();
        deptList.add(deptVO1.getOuterCombineId());
        deptList.add(deptVO2.getOuterCombineId());
        userVO.setOuterCombineDeptIdList(deptList);
        autoCreateService.createUser(userVO);

        List<String> newDeptList = new ArrayList<>();
        // 如果用户的deptId不存在，那么会将该用户放入到未分配部门中
        deptList.add("at_test_random_dept_comid_" + now.getTime());
        userVO.setOuterCombineDeptIdList(newDeptList);
        autoCreateService.updateUser(userVO);

        List<Long> savedDeptList = departmentManager.listUserDeptDepartmentIdByUserId(userVO.getId());
        WbDepartmentDO unassignedDepartment = departmentManager.getTeamUnassignedDepartment(teamId);
        assertThat(savedDeptList).hasSize(1);
        assertThat(savedDeptList).containsExactly(unassignedDepartment.getId());
    }

    @Test
    public void testUpdateUserWithUserNotFound() throws Exception {
        // 如果update的时候，用户不存在，那么应该走新增的流程
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        autoCreateService.createTeam(teamVO);
        Long teamId = teamVO.getId();

        AutoCreateDepartmentVO deptVO1 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO1.setTop(true);
        autoCreateService.createDepartment(deptVO1);
        AutoCreateDepartmentVO deptVO2 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO2.setOuterParentCombineId(deptVO1.getOuterCombineId());
        autoCreateService.createDepartment(deptVO2);

        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterCorpId(teamVO.getOuterCorpId());
        List<String> deptList = new ArrayList<>();
        deptList.add(deptVO1.getOuterCombineId());
        deptList.add(deptVO2.getOuterCombineId());
        deptList.add("at_test_random_dept_comid_" + now.getTime());
        userVO.setOuterCombineDeptIdList(deptList);
        // 用户更新的时候如果不存在，那么会直接创建
        autoCreateService.updateUser(userVO);

        assertThat(userVO.getId()).isNotNull();
        Long userId = userVO.getId();
        WbRoleGroupDO roleGroupDO = roleManager.getCommonRoleGroup();
        WbUserRoleGroupDO userRoleGroupDO = roleManager.getUserRoleGroupByUserId(userId);
        assertThat(userRoleGroupDO.getRoleGroupId()).isEqualTo(roleGroupDO.getId());
        WbTeamUserRoleDO userRole = teamManager.getTeamUserRoleByTeamIdAndUserIdAndRole(
                teamId, userId, WbConstant.TEAM_USER_ROLE_COMMON
        );
        assertThat(userRole).isNotNull();
        List<Long> savedDeptList = departmentManager.listUserDeptDepartmentIdByUserId(userId);
        assertThat(savedDeptList).hasSize(2);
        assertThat(savedDeptList).contains(deptVO1.getId(), deptVO2.getId());
    }

    @Test
    public void testUpdateUserLeaveTeamWithUserNotExist() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        autoCreateService.createTeam(teamVO);
        Long teamId = teamVO.getId();

        AutoCreateDepartmentVO deptVO1 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO1.setTop(true);
        autoCreateService.createDepartment(deptVO1);
        AutoCreateDepartmentVO deptVO2 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO2.setOuterParentCombineId(deptVO1.getOuterCombineId());
        autoCreateService.createDepartment(deptVO2);
        AutoCreateDepartmentVO deptVO3 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO3.setOuterParentCombineId(deptVO1.getOuterCombineId());
        autoCreateService.createDepartment(deptVO3);

        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterCorpId(teamVO.getOuterCorpId());
        List<String> deptList = new ArrayList<>();
        deptList.add(deptVO1.getOuterCombineId());
        deptList.add(deptVO2.getOuterCombineId());
        userVO.setOuterCombineDeptIdList(deptList);
        autoCreateService.createUser(userVO);

        userVO.setOuterCombineId("at_user_outer_random_comid_" + now.getTime());
        assertThatCode(() -> autoCreateService.updateUserLeaveTeam(userVO)).doesNotThrowAnyException();
    }

    @Test
    public void testUpdateUserSerLeaveTeam() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        autoCreateService.createTeam(teamVO);
        Long teamId = teamVO.getId();

        AutoCreateDepartmentVO deptVO1 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO1.setTop(true);
        autoCreateService.createDepartment(deptVO1);
        AutoCreateDepartmentVO deptVO2 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO2.setOuterParentCombineId(deptVO1.getOuterCombineId());
        autoCreateService.createDepartment(deptVO2);
        AutoCreateDepartmentVO deptVO3 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO3.setOuterParentCombineId(deptVO1.getOuterCombineId());
        autoCreateService.createDepartment(deptVO3);

        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterCorpId(teamVO.getOuterCorpId());
        List<String> deptList = new ArrayList<>();
        deptList.add(deptVO1.getOuterCombineId());
        deptList.add(deptVO2.getOuterCombineId());
        userVO.setOuterCombineDeptIdList(deptList);
        autoCreateService.createUser(userVO);
        Long userId = userVO.getId();

        userVO.setClient(null);
        assertThatThrownBy(() -> autoCreateService.updateUserLeaveTeam(userVO))
                .isInstanceOf(WorkbeiServiceException.class)
                .hasMessageStartingWith("13002");
        userVO.setClient(TestV2Constant.CLIENT_FAKED);
        autoCreateService.updateUserLeaveTeam(userVO);
        WbUserDO userDO = userManager.getUserById(userId);
        assertThat(userDO.getTeamId()).isNull();
        List<WbTeamUserRoleDO> teamUserRoleList = teamManager.listTeamUserRoleByTeamIdAndUserId(
                teamId, userId
        );
        assertThat(teamUserRoleList).hasSize(0);
        List<Long> userDeptList = departmentManager.listUserDeptDepartmentIdByUserId(userId);
        assertThat(userDeptList).hasSize(0);
    }

    /**
     * 测试用户移除团队，然后重新加入团队
     */
    @Test
    public void testUpdateUserLeaveTeamAndRejoinTeam() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        autoCreateService.createTeam(teamVO);
        Long teamId = teamVO.getId();

        AutoCreateDepartmentVO deptVO1 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO1.setTop(true);
        autoCreateService.createDepartment(deptVO1);
        AutoCreateDepartmentVO deptVO2 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO2.setOuterParentCombineId(deptVO1.getOuterCombineId());
        autoCreateService.createDepartment(deptVO2);
        AutoCreateDepartmentVO deptVO3 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO3.setOuterParentCombineId(deptVO1.getOuterCombineId());
        autoCreateService.createDepartment(deptVO3);

        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterCorpId(teamVO.getOuterCorpId());
        List<String> deptList = new ArrayList<>();
        deptList.add(deptVO1.getOuterCombineId());
        deptList.add(deptVO2.getOuterCombineId());
        userVO.setOuterCombineDeptIdList(deptList);
        autoCreateService.createUser(userVO);
        Long userId = userVO.getId();

        autoCreateService.updateUserLeaveTeam(userVO);

        AutoCreateUserVO userVOCopy = (AutoCreateUserVO) SerializationUtils.clone(userVO);
        userVOCopy.setId(null);
        autoCreateService.createUser(userVOCopy);
        assertThat(userVOCopy.getId()).isEqualTo(userId);
        WbUserDO userDO = userManager.getUserById(userId);
        assertThat(userDO.getTeamId()).isEqualTo(teamId);
    }

    @Test
    public void testUpdateUserSetAdminWithUserNotExist() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        autoCreateService.createTeam(teamVO);
        Long teamId = teamVO.getId();

        AutoCreateDepartmentVO deptVO1 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO1.setTop(true);
        autoCreateService.createDepartment(deptVO1);
        AutoCreateDepartmentVO deptVO2 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO2.setOuterParentCombineId(deptVO1.getOuterCombineId());
        autoCreateService.createDepartment(deptVO2);

        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterCorpId(teamVO.getOuterCorpId());
        List<String> deptList = new ArrayList<>();
        deptList.add(deptVO1.getOuterCombineId());
        deptList.add(deptVO2.getOuterCombineId());
        userVO.setOuterCombineDeptIdList(deptList);
        autoCreateService.createUser(userVO);

        userVO.setOuterCombineId("at_user_outer_random_comid_" + now.getTime());
        assertThatThrownBy(() -> autoCreateService.updateUserSetAdmin(userVO))
                .hasMessageStartingWith(ExceptionCode.getMessage(USER_NOT_FOUND));
    }

    @Test
    public void testUpdateUserSetAdmin() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        autoCreateService.createTeam(teamVO);
        Long teamId = teamVO.getId();

        AutoCreateDepartmentVO deptVO1 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO1.setTop(true);
        autoCreateService.createDepartment(deptVO1);
        AutoCreateDepartmentVO deptVO2 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO2.setOuterParentCombineId(deptVO1.getOuterCombineId());
        autoCreateService.createDepartment(deptVO2);

        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterCorpId(teamVO.getOuterCorpId());
        List<String> deptList = new ArrayList<>();
        deptList.add(deptVO1.getOuterCombineId());
        deptList.add(deptVO2.getOuterCombineId());
        userVO.setOuterCombineDeptIdList(deptList);
        userVO.setAdmin(true);
        autoCreateService.createUser(userVO);

        userVO.setClient(null);
        assertThatThrownBy(() -> autoCreateService.updateUserSetAdmin(userVO))
                .isInstanceOf(WorkbeiServiceException.class)
                .hasMessageStartingWith("13002");
        userVO.setClient(TestV2Constant.CLIENT_FAKED);
        autoCreateService.updateUserSetAdmin(userVO);
        WbTeamUserRoleDO adminRole = teamManager.getTeamUserRoleByTeamIdAndUserIdAndRole(
                teamId, userVO.getId(), WbConstant.TEAM_USER_ROLE_ADMIN
        );
        assertThat(adminRole).isNotNull();
    }

    /**
     * oldAdminOuterIdList: userOuterId1, userOuterId2
     * newAdminOuterIdList: userOuterId2, userOuterId3
     * @throws Exception
     */
    @Test
    public void testUpdateBatchUserSetAdmin() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        autoCreateService.createTeam(teamVO);
        String client = TestV2Constant.CLIENT_FAKED;
        String outerCorpId = teamVO.getOuterCorpId();
        Long teamId = teamVO.getId();

        AutoCreateDepartmentVO deptVO1 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO1.setTop(true);
        autoCreateService.createDepartment(deptVO1);
        AutoCreateDepartmentVO deptVO2 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO2.setOuterParentCombineId(deptVO1.getOuterCombineId());
        autoCreateService.createDepartment(deptVO2);

        AutoCreateUserVO userVO1 = TestUserFactory.getAutoCreateUserVO();
        userVO1.setOuterCorpId(teamVO.getOuterCorpId());
        List<String> deptList = new ArrayList<>();
        deptList.add(deptVO1.getOuterCombineId());
        deptList.add(deptVO2.getOuterCombineId());
        userVO1.setOuterCombineDeptIdList(deptList);
        userVO1.setAdmin(false);
        userVO1.setClient(client);
        autoCreateService.createUser(userVO1);

        AutoCreateUserVO userVO2 = TestUserFactory.getAutoCreateUserVO();
        userVO2.setOuterCorpId(teamVO.getOuterCorpId());
        userVO2.setOuterCombineDeptIdList(deptList);
        userVO2.setAdmin(false);
        userVO2.setClient(client);
        autoCreateService.createUser(userVO2);

        List<WbTeamUserRoleDO> adminList = teamManager.listTeamUserRoleByTeamIdAndRole(teamId, WbConstant.TEAM_USER_ROLE_ADMIN);
        //  没有管理员
        assertThat(adminList).hasSize(0);

        String randomUserOuterId = "auto_test_random_user_outer_id_" + now.getTime();
        String user1OuterId = userVO1.getOuterCombineId();
        String user2OuterId = userVO2.getOuterCombineId();

        List<String> userOuterIdList = new ArrayList<>();
        userOuterIdList.add(randomUserOuterId);
        userOuterIdList.add(user1OuterId);
        userOuterIdList.add(user2OuterId);

        autoCreateService.updateBatchUserSetAdmin(client, outerCorpId, userOuterIdList);
        adminList = teamManager.listTeamUserRoleByTeamIdAndRole(teamId, WbConstant.TEAM_USER_ROLE_ADMIN);

        assertThat(adminList).hasSize(2);
        assertThat(adminList).extracting("teamId", "userId", "role")
                .contains(tuple(teamId, userVO1.getId(), WbConstant.TEAM_USER_ROLE_ADMIN),
                        tuple(teamId, userVO2.getId(), WbConstant.TEAM_USER_ROLE_ADMIN));
    }
}