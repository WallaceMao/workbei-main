package com.workbei.service.base.impl;

import com.workbei.BaseUnitTest;
import com.workbei.constant.WbConstant;
import com.workbei.model.domain.user.*;
import com.workbei.model.view.autocreate.AutoCreateDepartmentVO;
import com.workbei.service.base.DepartmentManageService;
import com.workbei.service.base.TeamManageService;
import com.workbei.service.base.util.TestDepartmentFactory;
import com.workbei.service.base.util.TestTeamFactory;
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
public class DepartmentManageServiceImplTest extends BaseUnitTest {
    @Autowired
    private TeamManageService teamManageService;
    @Autowired
    private DepartmentManageService departmentManageService;
    @Autowired
    private UserManageServiceImpl userManageService;

    private WbTeamDO globalTeam;
    private WbOuterDataTeamDO globalOuterDataTeam;
    private WbDepartmentDO globalTopDepartment;
    private WbDepartmentDO globalUnassignedDepartment;
    private WbDepartmentDO globalCommonDepartment;
    private WbOuterDataDepartmentDO globalCommonOuterDataDepartment;
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
        departmentManageService.saveOrUpdateDepartment(commonDepartment);
        WbOuterDataDepartmentDO outerDataDepartmentDO = TestDepartmentFactory.getOuterDataDepartment(
                commonDepartment.getId());
        departmentManageService.saveOrUpdateOuterDataDepartment(outerDataDepartmentDO);

        globalTopDepartment = topDepartment;
        globalUnassignedDepartment = unassignedDepartment;
        globalCommonDepartment = commonDepartment;
        globalCommonOuterDataDepartment = outerDataDepartmentDO;
        globalTeam = teamDO;
        globalOuterDataTeam = outerDataTeamDO;
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
        WbOuterDataDepartmentDO outerDataDepartmentDO = departmentManageService.getOuterDataDepartmentClientAndOuterId(
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
        WbDepartmentDO departmentDO = departmentManageService.getDepartmentByOuterId(
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

        WbOuterDataDepartmentDO outerDataDepartmentDO = departmentManageService.getOuterDataDepartmentClientAndOuterId(
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

        WbDepartmentDO departmentDO = departmentManageService.getDepartmentByOuterId(
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

        WbOuterDataDepartmentDO outerDataDepartmentDO = departmentManageService.getOuterDataDepartmentClientAndOuterId(
                departmentVO.getClient(), departmentVO.getOuterCombineId());
        assertThat(outerDataDepartmentDO).isNotNull();
        checkDateCloseToNow(outerDataDepartmentDO.getDateCreated(), outerDataDepartmentDO.getLastUpdated());
        assertThat(outerDataDepartmentDO.getDepartmentId()).isEqualTo(departmentDO.getId());
        assertThat(outerDataDepartmentDO.getClient()).isEqualTo(departmentVO.getClient());
        assertThat(outerDataDepartmentDO.getOuterId()).isEqualTo(departmentVO.getOuterCombineId());
    }


}