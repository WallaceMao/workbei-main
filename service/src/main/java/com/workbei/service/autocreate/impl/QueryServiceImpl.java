package com.workbei.service.autocreate.impl;

import com.workbei.converter.AutoCreateConverter;
import com.workbei.manager.user.AccountManager;
import com.workbei.manager.user.DepartmentManager;
import com.workbei.manager.user.TeamManager;
import com.workbei.manager.user.UserManager;
import com.workbei.model.domain.user.*;
import com.workbei.model.view.autocreate.AutoCreateDepartmentVO;
import com.workbei.model.view.autocreate.AutoCreateTeamVO;
import com.workbei.model.view.autocreate.AutoCreateUserVO;
import com.workbei.service.autocreate.QueryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wallace Mao
 * Date: 2019-01-23 14:43
 */
public class QueryServiceImpl implements QueryService {

    private TeamManager teamManager;
    private DepartmentManager departmentManager;
    private AccountManager accountManager;
    private UserManager userManager;

    @Autowired
    public QueryServiceImpl(TeamManager teamManager, DepartmentManager departmentManager, AccountManager accountManager, UserManager userManager) {
        this.teamManager = teamManager;
        this.departmentManager = departmentManager;
        this.accountManager = accountManager;
        this.userManager = userManager;
    }

    @Override
    public AutoCreateTeamVO findTeamByOuterId(String client, String outerId) {
        WbOuterDataTeamDO outerDataTeamDO = teamManager.getOuterDataTeamByClientAndOuterId(client, outerId);
        WbTeamDO teamDO = teamManager.getTeamById(outerDataTeamDO.getTeamId());
        return AutoCreateConverter.teamDO2AutoCreateTeamVO(teamDO, outerDataTeamDO);
    }

    @Override
    public List<AutoCreateDepartmentVO> findAllTeamDepartmentsByOuterCorpId(String client, String outerCorpId) {
        WbOuterDataTeamDO outerDataTeamDO = teamManager.getOuterDataTeamByClientAndOuterId(client, outerCorpId);
        WbTeamDO teamDO = teamManager.getTeamById(outerDataTeamDO.getTeamId());
        List<WbDepartmentDO> departments = departmentManager.listDepartmentByTeamId(teamDO.getId());
        List<AutoCreateDepartmentVO> voList = new ArrayList<>(departments.size());
        for (WbDepartmentDO departmentDO : departments) {
            WbOuterDataDepartmentDO outerDataDepartmentDO = departmentManager.getOuterDataDepartmentByClientAndDepartmentId(
                    client, departmentDO.getId());
            WbOuterDataDepartmentDO parentOuterDataDepartmentDO = null;
            if (departmentDO.getParentId() != null) {
                parentOuterDataDepartmentDO = departmentManager.getOuterDataDepartmentByClientAndDepartmentId(
                        client, departmentDO.getParentId());
            }
            voList.add(AutoCreateConverter.departmentDO2AutoCreateTeamVO(
                    departmentDO, outerDataTeamDO, outerDataDepartmentDO, parentOuterDataDepartmentDO));
        }
        return  voList;
    }

    @Override
    public List<AutoCreateUserVO> findAllTeamStaffsByOuterCorpId(String client, String outerCorpId){
        WbOuterDataTeamDO outerDataTeamDO = teamManager.getOuterDataTeamByClientAndOuterId(client, outerCorpId);
        WbTeamDO teamDO = teamManager.getTeamById(outerDataTeamDO.getTeamId());
        List<WbUserDO> userList = userManager.listUserByTeamId(teamDO.getId());
        List<AutoCreateUserVO> voList = new ArrayList<>(userList.size());
        for (WbUserDO userDO : userList) {
            WbOuterDataUserDO outerDataUserDO = userManager.getOuterDataUserByClientAndUserId(client, userDO.getId());
            List<WbUserDeptDO> userDeptDOList = departmentManager.listUserDeptByUserId(userDO.getId());
            List<WbOuterDataDepartmentDO> outerDataDepartmentDOList = new ArrayList<>(userDeptDOList.size());
            for (WbUserDeptDO  userDeptDO : userDeptDOList) {
                WbOuterDataDepartmentDO outerDataDepartmentDO = departmentManager.getOuterDataDepartmentByClientAndDepartmentId(
                        client, userDeptDO.getDepartmentId());
                outerDataDepartmentDOList.add(outerDataDepartmentDO);
            }
            voList.add(AutoCreateConverter.userDO2AutoCreateUserVO(
                    userDO,
                    outerDataTeamDO,
                    outerDataUserDO,
                    outerDataDepartmentDOList));
        }
        return voList;
    }
}
