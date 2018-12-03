package com.workbei.service.base.impl;

import com.workbei.BaseUnitTest;
import com.workbei.constant.WbConstant;
import com.workbei.exception.ExceptionCode;
import com.workbei.model.domain.user.*;
import com.workbei.model.view.autocreate.AutoCreateDepartmentVO;
import com.workbei.service.base.DepartmentManageService;
import com.workbei.service.base.TeamManageService;
import com.workbei.service.base.util.TestDepartmentFactory;
import com.workbei.service.base.util.TestTeamFactory;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.workbei.exception.ExceptionCode.*;
import static org.assertj.core.api.Assertions.*;

@Transactional(transactionManager = "transactionManager")
@Rollback
public class DepartmentManageServiceImplTest extends BaseUnitTest {
    @Autowired
    private TeamManageService teamManageService;
    @Autowired
    private DepartmentManageService departmentManageService;
    @Autowired
    private UserManageServiceImpl userManageService;

    //  主公司
    private WbTeamDO globalTeam;
    //  主公司的outerDataTeam
    private WbOuterDataTeamDO globalOuterDataTeam;
    //  主公司顶级部门
    private WbDepartmentDO globalTopDepartment;
    //  主公司的未分配部门
    private WbDepartmentDO globalUnassignedDepartment;
    //  主公司的普通二级部门
    private WbDepartmentDO globalCommonDepartment;
    //  主公司的普通二级部门的关联
    private WbOuterDataDepartmentDO globalCommonOuterDataDepartment;
    //
    private WbAccountDO globalAccount;
    private WbUserDO globalUser;

    @Before
    public void setUp() throws Exception {
        WbTeamDO teamDO = TestTeamFactory.getTeamDO();
        teamManageService.saveOrUpdateTeam(teamDO);
        WbOuterDataTeamDO outerDataTeamDO = TestTeamFactory.getOuterDataTeam(teamDO.getId());
        teamManageService.saveOrUpdateOuterDataTeam(outerDataTeamDO);
        departmentManageService.saveTeamDefaultDepartment(teamDO.getId(), teamDO.getName());
        WbDepartmentDO topDepartment = departmentManageService.getTeamTopDepartment(teamDO.getId());
        WbDepartmentDO unassignedDepartment = departmentManageService.getTeamUnassignedDepartment(teamDO.getId());
        WbDepartmentDO commonDepartment = TestDepartmentFactory.getDepartmentDO(
                teamDO.getId(), topDepartment.getId(), topDepartment.getLevel() + 1);
        departmentManageService.saveOrUpdateDepartment(commonDepartment, topDepartment.getCode());
        WbOuterDataDepartmentDO outerDataDepartmentDO = TestDepartmentFactory.getOuterDataDepartment(
                commonDepartment.getId());
        departmentManageService.saveOrUpdateOuterDataDepartment(outerDataDepartmentDO);

        globalTopDepartment = departmentManageService.getDepartmentById(topDepartment.getId());
        globalUnassignedDepartment = departmentManageService.getDepartmentById(unassignedDepartment.getId());
        globalCommonDepartment = departmentManageService.getDepartmentById(commonDepartment.getId());
        globalCommonOuterDataDepartment = departmentManageService.getOuterDataDepartmentByClientAndOuterId(
                outerDataDepartmentDO.getClient(), outerDataDepartmentDO.getOuterId());
        globalTeam = teamManageService.getTeamById(teamDO.getId());
        globalOuterDataTeam = teamManageService.getOuterDataTeamByClientAndOuterId(
                outerDataTeamDO.getClient(), outerDataTeamDO.getOuterId());
//        globalAccount = accountDO;
//        globalUser = userDO;
    }

    @Test
    public void testSaveDefaultDepartment() throws Exception {
        WbTeamDO teamDO = TestTeamFactory.getTeamDO();
        teamManageService.saveOrUpdateTeam(teamDO);
        departmentManageService.saveTeamDefaultDepartment(teamDO.getId(), teamDO.getName());
        Long teamId = teamDO.getId();
        WbDepartmentDO topDepartment = departmentManageService.getTeamTopDepartment(teamId);
        WbDepartmentDO unassignedDepartment = departmentManageService.getTeamUnassignedDepartment(teamId);
        assertThat(topDepartment).isNotNull();
        assertThat(topDepartment.getId()).isNotNull();
        assertThat(topDepartment.getUuid()).isNotNull();
        assertThat(topDepartment.getName()).isEqualTo(teamDO.getName());
        assertThat(topDepartment.getLevel()).isEqualTo(1);
        assertThat(topDepartment.getCode()).isEqualTo(String.valueOf(topDepartment.getId()));
        assertThat(topDepartment.getTeamId()).isEqualTo(teamId);
        assertThat(topDepartment.getType()).isEqualTo(WbConstant.DEPARTMENT_TYPE_TOP);
        assertThat(topDepartment.getParentId()).isNull();
        assertThat(unassignedDepartment).isNotNull();
        assertThat(unassignedDepartment.getId()).isNotNull();
        assertThat(unassignedDepartment.getUuid()).isNotNull();
        assertThat(unassignedDepartment.getName()).isEqualTo(WbConstant.DEPARTMENT_NAME_UNASSIGNED);
        assertThat(unassignedDepartment.getLevel()).isEqualTo(2);
        assertThat(unassignedDepartment.getCode()).isEqualTo(
                topDepartment.getId() + WbConstant.DEPARTMENT_CODE_SEPARATOR + unassignedDepartment.getId());
        assertThat(unassignedDepartment.getTeamId()).isEqualTo(teamId);
        assertThat(unassignedDepartment.getType()).isEqualTo(WbConstant.DEPARTMENT_TYPE_UNASSIGNED);
        assertThat(unassignedDepartment.getParentId()).isEqualTo(topDepartment.getId());

        List<WbDepartmentDO> departmentList = departmentManageService.listDepartmentByTeamId(teamId);
        assertThat(departmentList).hasSize(2);
        assertThat(departmentList).extracting(WbDepartmentDO::getType)
                .contains(WbConstant.DEPARTMENT_TYPE_TOP, WbConstant.DEPARTMENT_TYPE_UNASSIGNED);
    }

    @Test
    public void testSaveDepartmentInfoTop() throws Exception {
        //  如果top为true
        Long teamId = globalTeam.getId();
        AutoCreateDepartmentVO departmentVO = TestDepartmentFactory.getAutoCreateDepartmentVO(
                globalOuterDataTeam.getOuterId());
        departmentVO.setTop(true);
        departmentManageService.saveDepartmentInfo(teamId, departmentVO);
        WbDepartmentDO topDepartment = departmentManageService.getTeamTopDepartment(teamId);
        WbOuterDataDepartmentDO outerDataDepartmentDO = departmentManageService.getOuterDataDepartmentByClientAndOuterId(
                departmentVO.getClient(), departmentVO.getOuterCombineId());
        assertThat(outerDataDepartmentDO).isNotNull();
        checkDateCloseToNow(outerDataDepartmentDO.getDateCreated(), outerDataDepartmentDO.getLastUpdated());
        assertThat(outerDataDepartmentDO.getClient()).isEqualTo(departmentVO.getClient());
        assertThat(outerDataDepartmentDO.getOuterId()).isEqualTo(departmentVO.getOuterCombineId());
        assertThat(outerDataDepartmentDO.getDepartmentId()).isEqualTo(topDepartment.getId());
    }

    @Test
    public void testSaveDepartmentInfoCommonWithParent() throws Exception {
        //  如果top参数为false且可以找到父级部门
        Long teamId = globalTeam.getId();
        AutoCreateDepartmentVO departmentVO = TestDepartmentFactory.getAutoCreateDepartmentVO(
                globalOuterDataTeam.getOuterId());
        departmentVO.setOuterParentCombineId(globalCommonOuterDataDepartment.getOuterId());
        departmentManageService.saveDepartmentInfo(teamId, departmentVO);
        WbDepartmentDO departmentDO = departmentManageService.getDepartmentByClientAndOuterId(
                departmentVO.getClient(), departmentVO.getOuterCombineId());
        assertThat(departmentDO).isNotNull();
        assertThat(departmentDO.getUuid()).isNotNull();
        assertThat(departmentDO.getName()).isEqualTo(departmentVO.getName());
        assertThat(departmentDO.getDisplayOrder()).isEqualTo(departmentVO.getDisplayOrder());
        assertThat(departmentDO.getLevel()).isEqualTo(globalCommonDepartment.getLevel() + 1);
        assertThat(departmentDO.getCode()).isEqualTo(
                globalCommonDepartment.getCode() + WbConstant.DEPARTMENT_CODE_SEPARATOR + departmentDO.getId());
        assertThat(departmentDO.getParentId()).isEqualTo(globalCommonDepartment.getId());
        assertThat(departmentDO.getTeamId()).isEqualTo(teamId);
        assertThat(departmentDO.getType()).isEqualTo(WbConstant.DEPARTMENT_TYPE_COMMON);

        WbOuterDataDepartmentDO outerDataDepartmentDO = departmentManageService.getOuterDataDepartmentByClientAndOuterId(
                departmentVO.getClient(), departmentVO.getOuterCombineId());
        assertThat(outerDataDepartmentDO).isNotNull();
        checkDateCloseToNow(outerDataDepartmentDO.getDateCreated(), outerDataDepartmentDO.getLastUpdated());
        assertThat(outerDataDepartmentDO.getDepartmentId()).isEqualTo(departmentDO.getId());
        assertThat(outerDataDepartmentDO.getClient()).isEqualTo(departmentVO.getClient());
        assertThat(outerDataDepartmentDO.getOuterId()).isEqualTo(departmentVO.getOuterCombineId());
    }

    @Test
    public void testSaveDepartmentInfoCommonWithoutParent() throws Exception {
        //  如果top参数为false且找不到父级部门
        Long teamId = globalTeam.getId();
        AutoCreateDepartmentVO departmentVO = TestDepartmentFactory.getAutoCreateDepartmentVO(
                globalOuterDataTeam.getOuterId());
        departmentVO.setOuterParentCombineId("at_outer_id_" + new Date().getTime());
        departmentManageService.saveDepartmentInfo(teamId, departmentVO);

        WbDepartmentDO departmentDO = departmentManageService.getDepartmentByClientAndOuterId(
                departmentVO.getClient(), departmentVO.getOuterCombineId());
        assertThat(departmentDO).isNotNull();
        assertThat(departmentDO.getUuid()).isNotNull();
        assertThat(departmentDO.getName()).isEqualTo(departmentVO.getName());
        assertThat(departmentDO.getDisplayOrder()).isEqualTo(departmentVO.getDisplayOrder());
        assertThat(departmentDO.getLevel()).isEqualTo(globalTopDepartment.getLevel() + 1);
        assertThat(departmentDO.getCode()).isEqualTo(
                globalTopDepartment.getCode() + WbConstant.DEPARTMENT_CODE_SEPARATOR + departmentDO.getId());
        assertThat(departmentDO.getParentId()).isEqualTo(globalTopDepartment.getId());
        assertThat(departmentDO.getTeamId()).isEqualTo(teamId);
        assertThat(departmentDO.getType()).isEqualTo(WbConstant.DEPARTMENT_TYPE_COMMON);

        WbOuterDataDepartmentDO outerDataDepartmentDO = departmentManageService.getOuterDataDepartmentByClientAndOuterId(
                departmentVO.getClient(), departmentVO.getOuterCombineId());
        assertThat(outerDataDepartmentDO).isNotNull();
        checkDateCloseToNow(outerDataDepartmentDO.getDateCreated(), outerDataDepartmentDO.getLastUpdated());
        assertThat(outerDataDepartmentDO.getDepartmentId()).isEqualTo(departmentDO.getId());
        assertThat(outerDataDepartmentDO.getClient()).isEqualTo(departmentVO.getClient());
        assertThat(outerDataDepartmentDO.getOuterId()).isEqualTo(departmentVO.getOuterCombineId());
    }

    @Test
    public void testSaveDepartmentUser() throws Exception {
        Long userId = RandomUtils.nextLong();
        WbDepartmentDO targetDepartment = TestDepartmentFactory.getDepartmentDO(
                globalTeam.getId(), globalCommonDepartment.getId(), globalCommonDepartment.getLevel() + 1);
        departmentManageService.saveOrUpdateDepartment(targetDepartment, globalCommonDepartment.getCode());
        Long departmentId = targetDepartment.getId();

        departmentManageService.saveDepartmentUser(departmentId, userId);
        WbUserDeptDO userDeptDO = departmentManageService.getUserDeptByDepartmentIdAndUserId(departmentId, userId);
        assertThat(userDeptDO).isNotNull();
        assertThat(userDeptDO.getDepartmentId()).isEqualTo(departmentId);
        assertThat(userDeptDO.getUserId()).isEqualTo(userId);

        List<Long> ascriptionDeptIdList = departmentManageService.listUserDeptAscriptionDepartmentIdByUserId(userId);
        assertThat(ascriptionDeptIdList).contains(
                targetDepartment.getId(),
                globalCommonDepartment.getId(),
                globalTopDepartment.getId());
    }

    @Test
    public void testDeleteDepartmentUser() throws Exception {
        Long userId = RandomUtils.nextLong();
        WbDepartmentDO targetDepartment = TestDepartmentFactory.getDepartmentDO(
                globalTeam.getId(), globalCommonDepartment.getId(), globalCommonDepartment.getLevel() + 1);
        departmentManageService.saveOrUpdateDepartment(targetDepartment, globalCommonDepartment.getCode());
        Long departmentId = targetDepartment.getId();

        departmentManageService.saveDepartmentUser(departmentId, userId);
        departmentManageService.deleteDepartmentUser(departmentId, userId);

        WbUserDeptDO userDeptDO = departmentManageService.getUserDeptByDepartmentIdAndUserId(departmentId, userId);
        assertThat(userDeptDO).isNull();

        List<Long> ascriptionDeptIdList = departmentManageService.listUserDeptAscriptionDepartmentIdByUserId(userId);
        assertThat(ascriptionDeptIdList).hasSize(0);
    }

    @Test
    public void testUpdateDepartmentInfoBase() throws Exception {
        AutoCreateDepartmentVO autoCreateDepartmentVO = TestDepartmentFactory.getAutoCreateDepartmentVO(
                globalOuterDataTeam.getOuterId());
        assertThatThrownBy(() -> departmentManageService.updateDepartmentInfo(autoCreateDepartmentVO))
                .hasMessageStartingWith(ExceptionCode.getMessage(DEPT_NOT_FOUND));

        autoCreateDepartmentVO.setOuterCorpId(globalOuterDataTeam.getOuterId());
        autoCreateDepartmentVO.setOuterCombineId(globalCommonOuterDataDepartment.getOuterId());
        WbDepartmentDO oldDepartment = (WbDepartmentDO) SerializationUtils.clone(globalCommonDepartment);
        departmentManageService.updateDepartmentInfo(autoCreateDepartmentVO);
        globalCommonDepartment = departmentManageService.getDepartmentById(globalCommonDepartment.getId());
        assertThat(globalCommonDepartment.getName()).isEqualTo(autoCreateDepartmentVO.getName());
        assertThat(globalCommonDepartment.getName()).isNotEqualTo(oldDepartment.getName());
        assertThat(globalCommonDepartment.getDisplayOrder()).isEqualTo(autoCreateDepartmentVO.getDisplayOrder());
        assertThat(globalCommonDepartment.getDisplayOrder()).isNotEqualTo(oldDepartment.getDisplayOrder());
        assertThat(globalCommonDepartment).isEqualToIgnoringGivenFields(
                oldDepartment, "name", "displayOrder", "version");
    }

    @Test
    public void testUpdateDepartmentInfoWithoutParent() throws Exception {
        //  parent部门找不到，那么就放到顶级部门下面
        WbDepartmentDO targetDepartment = TestDepartmentFactory.getDepartmentDO(
                globalTeam.getId(), globalCommonDepartment.getId(), globalCommonDepartment.getLevel() + 1);
        departmentManageService.saveOrUpdateDepartment(targetDepartment, globalCommonDepartment.getCode());
        WbOuterDataDepartmentDO targetOuterDataDepartment = TestDepartmentFactory.getOuterDataDepartment(
                targetDepartment.getId());
        departmentManageService.saveOrUpdateOuterDataDepartment(targetOuterDataDepartment);

        AutoCreateDepartmentVO autoCreateDepartmentVO = new AutoCreateDepartmentVO();
        autoCreateDepartmentVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        autoCreateDepartmentVO.setOuterCorpId(globalOuterDataTeam.getOuterId());
        autoCreateDepartmentVO.setOuterCombineId(targetOuterDataDepartment.getOuterId());
        Date now = new Date();
        //  任意给定一个outerId，找不到outerId
        autoCreateDepartmentVO.setOuterParentCombineId("at_test_outer_parent_comid_" + now.getTime());
        WbDepartmentDO oldDepartment = (WbDepartmentDO) SerializationUtils.clone(targetDepartment);
        departmentManageService.updateDepartmentInfo(autoCreateDepartmentVO);

        WbDepartmentDO newDepartment = departmentManageService.getDepartmentById(oldDepartment.getId());
        assertThat(newDepartment).isNotNull();
        assertThat(newDepartment.getParentId()).isEqualTo(globalTopDepartment.getId());
        assertThat(newDepartment.getLevel()).isEqualTo(globalTopDepartment.getLevel() + 1);
        assertThat(newDepartment.getCode()).isEqualTo(
                globalTopDepartment.getCode() + WbConstant.DEPARTMENT_CODE_SEPARATOR + newDepartment.getId());
        assertThat(newDepartment).isEqualToIgnoringGivenFields(
                oldDepartment,
                "version", "parentId", "level", "code");
    }

    @Test
    public void testUpdateDepartmentInfoWithTopDepartment() throws Exception {
        //  顶级部门不允许移动
        WbOuterDataDepartmentDO topOuterDataDepartmentDO = TestDepartmentFactory.getOuterDataDepartment(
                globalTopDepartment.getId());
        departmentManageService.saveOrUpdateOuterDataDepartment(topOuterDataDepartmentDO);

        AutoCreateDepartmentVO autoCreateDepartmentVO = new AutoCreateDepartmentVO();
        autoCreateDepartmentVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        autoCreateDepartmentVO.setOuterCorpId(globalOuterDataTeam.getOuterId());
        autoCreateDepartmentVO.setOuterCombineId(topOuterDataDepartmentDO.getOuterId());
        //  给定globalCommonOuterDataDepartment
        autoCreateDepartmentVO.setOuterParentCombineId(globalCommonOuterDataDepartment.getOuterId());
        assertThatThrownBy(() -> departmentManageService.updateDepartmentInfo(autoCreateDepartmentVO))
                .hasMessageStartingWith(ExceptionCode.getMessage(DEPT_TOP_CANNOT_MOVE));
    }

    @Test
    public void testUpdateDepartmentInfoWithUnassignedDepartment() throws Exception {
        //  未分配部门不允许移动
        WbOuterDataDepartmentDO unassignedOuterDataDepartmentDO = TestDepartmentFactory.getOuterDataDepartment(
                globalUnassignedDepartment.getId());
        departmentManageService.saveOrUpdateOuterDataDepartment(unassignedOuterDataDepartmentDO);

        AutoCreateDepartmentVO autoCreateDepartmentVO = new AutoCreateDepartmentVO();
        autoCreateDepartmentVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        autoCreateDepartmentVO.setOuterCorpId(globalOuterDataTeam.getOuterId());
        autoCreateDepartmentVO.setOuterCombineId(unassignedOuterDataDepartmentDO.getOuterId());
        //  给定globalCommonOuterDataDepartment
        autoCreateDepartmentVO.setOuterParentCombineId(globalCommonOuterDataDepartment.getOuterId());
        assertThatThrownBy(() -> departmentManageService.updateDepartmentInfo(autoCreateDepartmentVO))
                .hasMessageStartingWith(ExceptionCode.getMessage(DEPT_UNASSIGNED_CANNOT_MOVE));
    }

    /**
     * top:
     *   unassigned
     *   common:
     *     DeptA1:
     *       DeptB1
     *     DeptA2
     * 测试将DeptA1移动到DeptB1下
     * @throws Exception
     */
    @Test
    public void testUpdateDepartmentInfoWithChildDepartment() throws Exception {
        Long teamId = globalTeam.getId();
        //  deptA1
        AutoCreateDepartmentVO deptA1 = TestDepartmentFactory.getAutoCreateDepartmentVO(
                globalOuterDataTeam.getOuterId());
        deptA1.setOuterParentCombineId(globalCommonOuterDataDepartment.getOuterId());
        departmentManageService.saveDepartmentInfo(teamId, deptA1);
        //  deptB1
        AutoCreateDepartmentVO deptB1 = TestDepartmentFactory.getAutoCreateDepartmentVO(
                globalOuterDataTeam.getOuterId());
        deptB1.setOuterParentCombineId(deptA1.getOuterCombineId());
        departmentManageService.saveDepartmentInfo(teamId, deptB1);

        //  不允许移动到子部门
        AutoCreateDepartmentVO autoCreateDepartmentVO = new AutoCreateDepartmentVO();
        autoCreateDepartmentVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        autoCreateDepartmentVO.setOuterCorpId(globalOuterDataTeam.getOuterId());
        autoCreateDepartmentVO.setOuterCombineId(deptA1.getOuterCombineId());
        //  给定globalCommonOuterDataDepartment
        autoCreateDepartmentVO.setOuterParentCombineId(deptB1.getOuterCombineId());
        assertThatThrownBy(() -> departmentManageService.updateDepartmentInfo(autoCreateDepartmentVO))
                .hasMessageStartingWith(ExceptionCode.getMessage(DEPT_PARENT_CANNOT_MOVE_TO_CHILD));
    }

    /**
     * top:
     *   unassigned
     *   common:
     *     DeptA1:  user1
     *       DeptB1:  user2 user3
     *         DeptC1: user4 user5
     *         DeptC2: user6
     *     DeptA2: user7
     *       DeptB2: user8
     * 测试将DeptB1移动到DeptB2下, userDept/userDeptAscription会做同步更新
     * @throws Exception
     */
    @Test
    public void testUpdateDepartmentInfoWithParentMoveDownward() throws Exception {
        Long teamId = globalTeam.getId();
        //  deptA1
        AutoCreateDepartmentVO deptA1 = TestDepartmentFactory.getAutoCreateDepartmentVO(
                globalOuterDataTeam.getOuterId());
        deptA1.setOuterParentCombineId(globalCommonOuterDataDepartment.getOuterId());
        departmentManageService.saveDepartmentInfo(teamId, deptA1);
        WbDepartmentDO deptDOA1 = departmentManageService.getDepartmentByClientAndOuterId(
                deptA1.getClient(), deptA1.getOuterCombineId());
        Long userId1 = RandomUtils.nextLong();
        departmentManageService.saveDepartmentUser(deptDOA1.getId(), userId1);
        //  deptA1-deptB1
        AutoCreateDepartmentVO deptB1 = TestDepartmentFactory.getAutoCreateDepartmentVO(
                globalOuterDataTeam.getOuterId());
        deptB1.setOuterParentCombineId(deptA1.getOuterCombineId());
        departmentManageService.saveDepartmentInfo(teamId, deptB1);
        WbDepartmentDO deptDOB1 = departmentManageService.getDepartmentByClientAndOuterId(
                deptB1.getClient(), deptB1.getOuterCombineId());
        Long userId2 = RandomUtils.nextLong();
        Long userId3 = RandomUtils.nextLong();
        departmentManageService.saveDepartmentUser(deptDOB1.getId(), userId2);
        departmentManageService.saveDepartmentUser(deptDOB1.getId(), userId3);
        //  deptA1-deptB1-deptC1
        AutoCreateDepartmentVO deptC1 = TestDepartmentFactory.getAutoCreateDepartmentVO(
                globalOuterDataTeam.getOuterId());
        deptC1.setOuterParentCombineId(deptB1.getOuterCombineId());
        departmentManageService.saveDepartmentInfo(teamId, deptC1);
        WbDepartmentDO deptDOC1 = departmentManageService.getDepartmentByClientAndOuterId(
                deptC1.getClient(), deptC1.getOuterCombineId());
        Long userId4 = RandomUtils.nextLong();
        Long userId5 = RandomUtils.nextLong();
        departmentManageService.saveDepartmentUser(deptDOC1.getId(), userId4);
        departmentManageService.saveDepartmentUser(deptDOC1.getId(), userId5);
        //  deptA1-deptB1-deptC2
        AutoCreateDepartmentVO deptC2 = TestDepartmentFactory.getAutoCreateDepartmentVO(
                globalOuterDataTeam.getOuterId());
        deptC2.setOuterParentCombineId(deptB1.getOuterCombineId());
        departmentManageService.saveDepartmentInfo(teamId, deptC2);
        WbDepartmentDO deptDOC2 = departmentManageService.getDepartmentByClientAndOuterId(
                deptC2.getClient(), deptC2.getOuterCombineId());
        Long userId6 = RandomUtils.nextLong();
        departmentManageService.saveDepartmentUser(deptDOC2.getId(), userId6);
        //  deptA2
        AutoCreateDepartmentVO deptA2 = TestDepartmentFactory.getAutoCreateDepartmentVO(
                globalOuterDataTeam.getOuterId());
        deptA2.setOuterParentCombineId(globalCommonOuterDataDepartment.getOuterId());
        departmentManageService.saveDepartmentInfo(teamId, deptA2);
        WbDepartmentDO deptDOA2 = departmentManageService.getDepartmentByClientAndOuterId(
                deptA2.getClient(), deptA2.getOuterCombineId());
        Long userId7 = RandomUtils.nextLong();
        departmentManageService.saveDepartmentUser(deptDOA2.getId(), userId7);
        //  deptA2-deptB2
        AutoCreateDepartmentVO deptB2 = TestDepartmentFactory.getAutoCreateDepartmentVO(
                globalOuterDataTeam.getOuterId());
        deptB2.setOuterParentCombineId(deptA2.getOuterCombineId());
        departmentManageService.saveDepartmentInfo(teamId, deptB2);
        WbDepartmentDO deptDOB2 = departmentManageService.getDepartmentByClientAndOuterId(
                deptB2.getClient(), deptB2.getOuterCombineId());
        Long userId8 = RandomUtils.nextLong();
        departmentManageService.saveDepartmentUser(deptDOB2.getId(), userId8);

        //  deptB1 move to deptB2
        AutoCreateDepartmentVO autoCreateDepartmentVO = new AutoCreateDepartmentVO();
        autoCreateDepartmentVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        autoCreateDepartmentVO.setOuterCorpId(globalOuterDataTeam.getOuterId());
        autoCreateDepartmentVO.setOuterCombineId(deptB1.getOuterCombineId());
        autoCreateDepartmentVO.setOuterParentCombineId(deptB2.getOuterCombineId());
        departmentManageService.updateDepartmentInfo(autoCreateDepartmentVO);
        WbDepartmentDO currentDept = departmentManageService.getDepartmentByClientAndOuterId(
                deptB1.getClient(), deptB1.getOuterCombineId());
        WbDepartmentDO currentParentDept = deptDOB2;

        assertThat(currentDept).isNotNull();
        assertThat(currentDept.getParentId()).isEqualTo(currentParentDept.getId());
        assertThat(currentDept.getLevel()).isEqualTo(currentParentDept.getLevel() + 1);
        assertThat(currentDept.getCode()).isEqualTo(
                currentParentDept.getCode() + WbConstant.DEPARTMENT_CODE_SEPARATOR + currentDept.getId());
        //  check C1 and C2 department and userDeptAscription
        WbDepartmentDO newDeptDOC1 = departmentManageService.getDepartmentById(deptDOC1.getId());
        assertThat(newDeptDOC1.getParentId()).isEqualTo(currentDept.getId());
        assertThat(newDeptDOC1.getLevel()).isEqualTo(currentDept.getLevel() + 1);
        assertThat(newDeptDOC1.getCode()).isEqualTo(
                currentDept.getCode() + WbConstant.DEPARTMENT_CODE_SEPARATOR + newDeptDOC1.getId());
        WbDepartmentDO newDeptDOC2 = departmentManageService.getDepartmentById(deptDOC2.getId());
        assertThat(newDeptDOC2.getParentId()).isEqualTo(currentDept.getId());
        assertThat(newDeptDOC2.getLevel()).isEqualTo(currentDept.getLevel() + 1);
        assertThat(newDeptDOC2.getCode()).isEqualTo(
                currentDept.getCode() + WbConstant.DEPARTMENT_CODE_SEPARATOR + newDeptDOC2.getId());
        List<Long> user2ascriptionList = departmentManageService.listUserDeptAscriptionDepartmentIdByUserId(userId2);
        assertThat(user2ascriptionList).hasSize(5);
        assertThat(user2ascriptionList).contains(
                deptDOB1.getId(), deptDOB2.getId(), deptDOA2.getId(), globalCommonDepartment.getId(), globalTopDepartment.getId());
        List<Long> user3ascriptionList = departmentManageService.listUserDeptAscriptionDepartmentIdByUserId(userId3);
        assertThat(user3ascriptionList).hasSize(5);
        assertThat(user3ascriptionList).contains(
                deptDOB1.getId(), deptDOB2.getId(), deptDOA2.getId(), globalCommonDepartment.getId(), globalTopDepartment.getId());
        List<Long> user4ascriptionList = departmentManageService.listUserDeptAscriptionDepartmentIdByUserId(userId4);
        assertThat(user4ascriptionList).hasSize(6);
        assertThat(user4ascriptionList).contains(
                deptDOC1.getId(), deptDOB1.getId(), deptDOB2.getId(), deptDOA2.getId(), globalCommonDepartment.getId(), globalTopDepartment.getId());
        List<Long> user5ascriptionList = departmentManageService.listUserDeptAscriptionDepartmentIdByUserId(userId5);
        assertThat(user5ascriptionList).hasSize(6);
        assertThat(user5ascriptionList).contains(
                deptDOC1.getId(), deptDOB1.getId(), deptDOB2.getId(), deptDOA2.getId(), globalCommonDepartment.getId(), globalTopDepartment.getId());
        List<Long> user6ascriptionList = departmentManageService.listUserDeptAscriptionDepartmentIdByUserId(userId6);
        assertThat(user6ascriptionList).hasSize(6);
        assertThat(user6ascriptionList).contains(
                deptDOC2.getId(), deptDOB1.getId(), deptDOB2.getId(), deptDOA2.getId(), globalCommonDepartment.getId(), globalTopDepartment.getId());
    }

    /**
     * top:
     *   unassigned
     *   common:
     *     DeptA1:  user1
     *       DeptB1:  user2 user3
     *         DeptC1: user4 user5
     *         DeptC2: user6
     *     DeptA2: user7
     *       DeptB2: user8
     * 测试将DeptB1移动到top下, userDept/userDeptAscription会做同步更新
     * @throws Exception
     */
    @Test
    public void testUpdateDepartmentInfoWithParentMoveUpward() throws Exception {
        Long teamId = globalTeam.getId();
        //  deptA1
        AutoCreateDepartmentVO deptA1 = TestDepartmentFactory.getAutoCreateDepartmentVO(
                globalOuterDataTeam.getOuterId());
        deptA1.setOuterParentCombineId(globalCommonOuterDataDepartment.getOuterId());
        departmentManageService.saveDepartmentInfo(teamId, deptA1);
        WbDepartmentDO deptDOA1 = departmentManageService.getDepartmentByClientAndOuterId(
                deptA1.getClient(), deptA1.getOuterCombineId());
        Long userId1 = RandomUtils.nextLong();
        departmentManageService.saveDepartmentUser(deptDOA1.getId(), userId1);
        //  deptA1-deptB1
        AutoCreateDepartmentVO deptB1 = TestDepartmentFactory.getAutoCreateDepartmentVO(
                globalOuterDataTeam.getOuterId());
        deptB1.setOuterParentCombineId(deptA1.getOuterCombineId());
        departmentManageService.saveDepartmentInfo(teamId, deptB1);
        WbDepartmentDO deptDOB1 = departmentManageService.getDepartmentByClientAndOuterId(
                deptB1.getClient(), deptB1.getOuterCombineId());
        Long userId2 = RandomUtils.nextLong();
        Long userId3 = RandomUtils.nextLong();
        departmentManageService.saveDepartmentUser(deptDOB1.getId(), userId2);
        departmentManageService.saveDepartmentUser(deptDOB1.getId(), userId3);
        //  deptA1-deptB1-deptC1
        AutoCreateDepartmentVO deptC1 = TestDepartmentFactory.getAutoCreateDepartmentVO(
                globalOuterDataTeam.getOuterId());
        deptC1.setOuterParentCombineId(deptB1.getOuterCombineId());
        departmentManageService.saveDepartmentInfo(teamId, deptC1);
        WbDepartmentDO deptDOC1 = departmentManageService.getDepartmentByClientAndOuterId(
                deptC1.getClient(), deptC1.getOuterCombineId());
        Long userId4 = RandomUtils.nextLong();
        Long userId5 = RandomUtils.nextLong();
        departmentManageService.saveDepartmentUser(deptDOC1.getId(), userId4);
        departmentManageService.saveDepartmentUser(deptDOC1.getId(), userId5);
        //  deptA1-deptB1-deptC2
        AutoCreateDepartmentVO deptC2 = TestDepartmentFactory.getAutoCreateDepartmentVO(
                globalOuterDataTeam.getOuterId());
        deptC2.setOuterParentCombineId(deptB1.getOuterCombineId());
        departmentManageService.saveDepartmentInfo(teamId, deptC2);
        WbDepartmentDO deptDOC2 = departmentManageService.getDepartmentByClientAndOuterId(
                deptC2.getClient(), deptC2.getOuterCombineId());
        Long userId6 = RandomUtils.nextLong();
        departmentManageService.saveDepartmentUser(deptDOC2.getId(), userId6);
        //  deptA2
        AutoCreateDepartmentVO deptA2 = TestDepartmentFactory.getAutoCreateDepartmentVO(
                globalOuterDataTeam.getOuterId());
        deptA2.setOuterParentCombineId(globalCommonOuterDataDepartment.getOuterId());
        departmentManageService.saveDepartmentInfo(teamId, deptA2);
        WbDepartmentDO deptDOA2 = departmentManageService.getDepartmentByClientAndOuterId(
                deptA2.getClient(), deptA2.getOuterCombineId());
        Long userId7 = RandomUtils.nextLong();
        departmentManageService.saveDepartmentUser(deptDOA2.getId(), userId7);
        //  deptA2-deptB2
        AutoCreateDepartmentVO deptB2 = TestDepartmentFactory.getAutoCreateDepartmentVO(
                globalOuterDataTeam.getOuterId());
        deptB2.setOuterParentCombineId(deptA2.getOuterCombineId());
        departmentManageService.saveDepartmentInfo(teamId, deptB2);
        WbDepartmentDO deptDOB2 = departmentManageService.getDepartmentByClientAndOuterId(
                deptB2.getClient(), deptB2.getOuterCombineId());
        Long userId8 = RandomUtils.nextLong();
        departmentManageService.saveDepartmentUser(deptDOB2.getId(), userId8);

        //  deptB1 move to top
        AutoCreateDepartmentVO autoCreateDepartmentVO = new AutoCreateDepartmentVO();
        autoCreateDepartmentVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        autoCreateDepartmentVO.setOuterCorpId(globalOuterDataTeam.getOuterId());
        autoCreateDepartmentVO.setOuterCombineId(deptB1.getOuterCombineId());
        autoCreateDepartmentVO.setOuterParentCombineId(globalOuterDataTeam.getOuterId());
        departmentManageService.updateDepartmentInfo(autoCreateDepartmentVO);
        WbDepartmentDO currentDept = departmentManageService.getDepartmentByClientAndOuterId(
                deptB1.getClient(), deptB1.getOuterCombineId());
        WbDepartmentDO currentParentDept = globalTopDepartment;

        assertThat(currentDept).isNotNull();
        assertThat(currentDept.getParentId()).isEqualTo(currentParentDept.getId());
        assertThat(currentDept.getLevel()).isEqualTo(currentParentDept.getLevel() + 1);
        assertThat(currentDept.getCode()).isEqualTo(
                currentParentDept.getCode() + WbConstant.DEPARTMENT_CODE_SEPARATOR + currentDept.getId());
        //  check C1 and C2 department and userDeptAscription
        WbDepartmentDO newDeptDOC1 = departmentManageService.getDepartmentById(deptDOC1.getId());
        assertThat(newDeptDOC1.getParentId()).isEqualTo(currentDept.getId());
        assertThat(newDeptDOC1.getLevel()).isEqualTo(currentDept.getLevel() + 1);
        assertThat(newDeptDOC1.getCode()).isEqualTo(
                currentDept.getCode() + WbConstant.DEPARTMENT_CODE_SEPARATOR + newDeptDOC1.getId());
        WbDepartmentDO newDeptDOC2 = departmentManageService.getDepartmentById(deptDOC2.getId());
        assertThat(newDeptDOC2.getParentId()).isEqualTo(currentDept.getId());
        assertThat(newDeptDOC2.getLevel()).isEqualTo(currentDept.getLevel() + 1);
        assertThat(newDeptDOC2.getCode()).isEqualTo(
                currentDept.getCode() + WbConstant.DEPARTMENT_CODE_SEPARATOR + newDeptDOC2.getId());
        List<Long> user2ascriptionList = departmentManageService.listUserDeptAscriptionDepartmentIdByUserId(userId2);
        assertThat(user2ascriptionList).hasSize(2);
        assertThat(user2ascriptionList).contains(
                deptDOB1.getId(), globalTopDepartment.getId());
        List<Long> user3ascriptionList = departmentManageService.listUserDeptAscriptionDepartmentIdByUserId(userId3);
        assertThat(user3ascriptionList).hasSize(2);
        assertThat(user3ascriptionList).contains(
                deptDOB1.getId(), globalTopDepartment.getId());
        List<Long> user4ascriptionList = departmentManageService.listUserDeptAscriptionDepartmentIdByUserId(userId4);
        assertThat(user4ascriptionList).hasSize(3);
        assertThat(user4ascriptionList).contains(
                deptDOC1.getId(), deptDOB1.getId(), globalTopDepartment.getId());
        List<Long> user5ascriptionList = departmentManageService.listUserDeptAscriptionDepartmentIdByUserId(userId5);
        assertThat(user5ascriptionList).hasSize(3);
        assertThat(user5ascriptionList).contains(
                deptDOC1.getId(), deptDOB1.getId(), globalTopDepartment.getId());
        List<Long> user6ascriptionList = departmentManageService.listUserDeptAscriptionDepartmentIdByUserId(userId6);
        assertThat(user6ascriptionList).hasSize(3);
        assertThat(user6ascriptionList).contains(
                deptDOC2.getId(), deptDOB1.getId(), globalTopDepartment.getId());

    }

    @Test
    public void testDeleteDepartmentNotExist() throws Exception {
        AutoCreateDepartmentVO autoCreateDepartmentVO = new AutoCreateDepartmentVO();
        autoCreateDepartmentVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        autoCreateDepartmentVO.setOuterCorpId(globalOuterDataTeam.getOuterId());
        autoCreateDepartmentVO.setOuterCombineId("at_department_outer_comid_" + new Date().getTime());
        assertThatThrownBy(() -> departmentManageService.deleteDepartmentInfo(autoCreateDepartmentVO))
                .hasMessageStartingWith(ExceptionCode.getMessage(DEPT_NOT_FOUND));
    }

    /**
     * top:
     *   unassigned
     *   common:
     *     DeptA1:  user1
     *       DeptB1:  user2 user3
     *         DeptC1: user4 user5
     *         DeptC2: user6
     *     DeptA2: user7
     *       DeptB2: user8
     * 测试将DeptB1删除，那么user2、user3、user4、user5、user6都会被关联到unassigned部门，且userDeptAscription会做相应删除
     * @throws Exception
     */
    @Test
    public void testDeleteDepartmentInfo() throws Exception {
        Long teamId = globalTeam.getId();
        //  deptA1
        AutoCreateDepartmentVO deptA1 = TestDepartmentFactory.getAutoCreateDepartmentVO(
                globalOuterDataTeam.getOuterId());
        deptA1.setOuterParentCombineId(globalCommonOuterDataDepartment.getOuterId());
        departmentManageService.saveDepartmentInfo(teamId, deptA1);
        WbDepartmentDO deptDOA1 = departmentManageService.getDepartmentByClientAndOuterId(
                deptA1.getClient(), deptA1.getOuterCombineId());
        Long userId1 = RandomUtils.nextLong();
        departmentManageService.saveDepartmentUser(deptDOA1.getId(), userId1);
        //  deptA1-deptB1
        AutoCreateDepartmentVO deptB1 = TestDepartmentFactory.getAutoCreateDepartmentVO(
                globalOuterDataTeam.getOuterId());
        deptB1.setOuterParentCombineId(deptA1.getOuterCombineId());
        departmentManageService.saveDepartmentInfo(teamId, deptB1);
        WbDepartmentDO deptDOB1 = departmentManageService.getDepartmentByClientAndOuterId(
                deptB1.getClient(), deptB1.getOuterCombineId());
        Long userId2 = RandomUtils.nextLong();
        Long userId3 = RandomUtils.nextLong();
        departmentManageService.saveDepartmentUser(deptDOB1.getId(), userId2);
        departmentManageService.saveDepartmentUser(deptDOB1.getId(), userId3);
        //  deptA1-deptB1-deptC1
        AutoCreateDepartmentVO deptC1 = TestDepartmentFactory.getAutoCreateDepartmentVO(
                globalOuterDataTeam.getOuterId());
        deptC1.setOuterParentCombineId(deptB1.getOuterCombineId());
        departmentManageService.saveDepartmentInfo(teamId, deptC1);
        WbDepartmentDO deptDOC1 = departmentManageService.getDepartmentByClientAndOuterId(
                deptC1.getClient(), deptC1.getOuterCombineId());
        Long userId4 = RandomUtils.nextLong();
        Long userId5 = RandomUtils.nextLong();
        departmentManageService.saveDepartmentUser(deptDOC1.getId(), userId4);
        departmentManageService.saveDepartmentUser(deptDOC1.getId(), userId5);
        //  deptA1-deptB1-deptC2
        AutoCreateDepartmentVO deptC2 = TestDepartmentFactory.getAutoCreateDepartmentVO(
                globalOuterDataTeam.getOuterId());
        deptC2.setOuterParentCombineId(deptB1.getOuterCombineId());
        departmentManageService.saveDepartmentInfo(teamId, deptC2);
        WbDepartmentDO deptDOC2 = departmentManageService.getDepartmentByClientAndOuterId(
                deptC2.getClient(), deptC2.getOuterCombineId());
        Long userId6 = RandomUtils.nextLong();
        departmentManageService.saveDepartmentUser(deptDOC2.getId(), userId6);
        //  deptA2
        AutoCreateDepartmentVO deptA2 = TestDepartmentFactory.getAutoCreateDepartmentVO(
                globalOuterDataTeam.getOuterId());
        deptA2.setOuterParentCombineId(globalCommonOuterDataDepartment.getOuterId());
        departmentManageService.saveDepartmentInfo(teamId, deptA2);
        WbDepartmentDO deptDOA2 = departmentManageService.getDepartmentByClientAndOuterId(
                deptA2.getClient(), deptA2.getOuterCombineId());
        Long userId7 = RandomUtils.nextLong();
        departmentManageService.saveDepartmentUser(deptDOA2.getId(), userId7);
        //  deptA2-deptB2
        AutoCreateDepartmentVO deptB2 = TestDepartmentFactory.getAutoCreateDepartmentVO(
                globalOuterDataTeam.getOuterId());
        deptB2.setOuterParentCombineId(deptA2.getOuterCombineId());
        departmentManageService.saveDepartmentInfo(teamId, deptB2);
        WbDepartmentDO deptDOB2 = departmentManageService.getDepartmentByClientAndOuterId(
                deptB2.getClient(), deptB2.getOuterCombineId());
        Long userId8 = RandomUtils.nextLong();
        departmentManageService.saveDepartmentUser(deptDOB2.getId(), userId8);

        //  delete deptB1
        AutoCreateDepartmentVO autoCreateDepartmentVO = new AutoCreateDepartmentVO();
        autoCreateDepartmentVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        autoCreateDepartmentVO.setOuterCorpId(globalOuterDataTeam.getOuterId());
        autoCreateDepartmentVO.setOuterCombineId(deptB1.getOuterCombineId());
        departmentManageService.deleteDepartmentInfo(autoCreateDepartmentVO);

        WbDepartmentDO newDeptDOB1 = departmentManageService.getDepartmentById(deptDOB1.getId());
        WbOuterDataDepartmentDO newOuterDataDepartmentDOB1 = departmentManageService.getOuterDataDepartmentByClientAndOuterId(
                deptB1.getClient(), deptB1.getOuterCombineId()
        );
        assertThat(newDeptDOB1).isNull();
        assertThat(newOuterDataDepartmentDOB1).isNull();
        WbDepartmentDO newDeptDOC1 = departmentManageService.getDepartmentById(deptDOC1.getId());
        WbOuterDataDepartmentDO newOuterDataDepartmentDOC1 = departmentManageService.getOuterDataDepartmentByClientAndOuterId(
                deptC1.getClient(), deptC1.getOuterCombineId()
        );
        assertThat(newDeptDOC1).isNull();
        assertThat(newOuterDataDepartmentDOC1).isNull();
        WbDepartmentDO newDeptDOC2 = departmentManageService.getDepartmentById(deptDOC2.getId());
        WbOuterDataDepartmentDO newOuterDataDepartmentDOC2 = departmentManageService.getOuterDataDepartmentByClientAndOuterId(
                deptC2.getClient(), deptC2.getOuterCombineId()
        );
        assertThat(newDeptDOC2).isNull();
        assertThat(newOuterDataDepartmentDOC2).isNull();
        List<Long> user2DepartmentIdList = departmentManageService.listUserDeptDepartmentIdByUser(userId2);
        assertThat(user2DepartmentIdList).containsExactly(globalUnassignedDepartment.getId());
        List<Long> user2AscriptionDepartmentIdList = departmentManageService.listUserDeptAscriptionDepartmentIdByUserId(userId2);
        assertThat(user2AscriptionDepartmentIdList).hasSize(1);
        assertThat(user2AscriptionDepartmentIdList).contains(globalTopDepartment.getId());
    }
}