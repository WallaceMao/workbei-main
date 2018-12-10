package com.workbei.service.autocreate.impl;

import com.workbei.constant.WbConstant;
import com.workbei.exception.ExceptionCode;
import com.workbei.exception.WorkbeiServiceException;
import com.workbei.manager.user.*;
import com.workbei.model.domain.user.*;
import com.workbei.model.view.autocreate.AutoCreateDepartmentVO;
import com.workbei.model.view.autocreate.AutoCreateTeamVO;
import com.workbei.model.view.autocreate.AutoCreateUserVO;
import com.workbei.service.autocreate.AutoCreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 14:44
 */
@Service
public class AutoCreateServiceImpl implements AutoCreateService {
    private RoleManager roleManager;
    private AccountManager accountManager;
    private TeamManager teamManager;
    private DepartmentManager departmentManager;
    private UserManager userManager;

    public AutoCreateServiceImpl(RoleManager roleManager, AccountManager accountManager, TeamManager teamManager, DepartmentManager departmentManager, UserManager userManager) {
        this.roleManager = roleManager;
        this.accountManager = accountManager;
        this.teamManager = teamManager;
        this.departmentManager = departmentManager;
        this.userManager = userManager;
    }

    /**
     * 创建团队：
     * 1  如果outerDataTeam中能找到，那么就直接返回
     * 2  如果没有创建者，那么就默认一个创建者
     * 3  新增创建者
     * 4  新增团队
     * 5  保存outerDataTeam
     * 6  返回team数据
     *
     * @param teamVO
     * @return
     */
    @Override
    public void createTeam(AutoCreateTeamVO teamVO) {
        if (teamVO.getClient() == null) {
            teamVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        }
        WbTeamDO teamDO = teamManager.getTeamByClientAndOuterId(
                teamVO.getClient(), teamVO.getOuterCorpId());
        if (teamDO == null) {
            //  新建team
            teamDO = teamManager.saveTeamInfo(teamVO);
            //  新建teamDefaultDepartment
            departmentManager.saveTeamDefaultDepartment(teamDO.getId(), teamDO.getName());
            //  新建user
            if (teamVO.getCreator() != null) {
                AutoCreateUserVO creatorVO = teamVO.getCreator();
                WbAccountDO accountDO = null;
                if (creatorVO.getOuterUnionId() != null) {
                    accountDO = accountManager.getAccountByDdUnionId(creatorVO.getOuterUnionId());
                }
                if (accountDO == null) {
                    if (creatorVO.getClient() == null) {
                        creatorVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
                    }
                    accountDO = accountManager.saveAccountInfo(creatorVO);
                }
                WbUserDO creatorDO = userManager.saveUserInfo(teamDO.getId(), accountDO.getId(), teamVO.getCreator());
                //  保存人员的roleGroup
                roleManager.saveOrUpdateUserCommonRoleGroup(creatorDO.getId());
                //  保存team与人员的关联
                teamManager.saveTeamCreatorRole(teamDO.getId(), creatorDO.getId());
                //  保存部门与人员的关联
                WbDepartmentDO departmentDO = departmentManager.getTeamUnassignedDepartment(teamDO.getId());
                departmentManager.saveDepartmentUser(departmentDO.getId(), creatorDO.getId());
            }
        }
        teamVO.setId(teamDO.getId());
    }

    @Override
    public void createUser(AutoCreateUserVO userVO) {
        if (userVO.getClient() == null) {
            userVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        }
        WbTeamDO teamDO = teamManager.getTeamByClientAndOuterId(
                userVO.getClient(), userVO.getOuterCorpId());
        if (teamDO == null) {
            throw new WorkbeiServiceException(
                    ExceptionCode.getMessage(ExceptionCode.TEAM_NOT_FOUND, userVO)
            );
        }
        Long teamId = teamDO.getId();
        //  如果根据outerId能找到user，那么会直接重用user
        WbUserDO userDO = userManager.getUserByClientAndOuterId(userVO.getClient(), userVO.getOuterCombineId());
        if (userDO == null) {
            //  只有当userDO找不到的情况下，才执行新增逻辑
            //  1. 新增account
            //  如果unionId在数据库中存在，那么就根据unionId获取account，否则就新增account，并且保存unionId
            WbAccountDO accountDO = null;
            if (userVO.getOuterUnionId() != null) {
                accountDO = accountManager.getAccountByDdUnionId(userVO.getOuterUnionId());
            }
            if (accountDO == null) {
                accountDO = accountManager.saveAccountInfo(userVO);
            }
            Long accountId = accountDO.getId();
            //  2. 新增user
            userDO = userManager.saveUserInfo(teamId, accountId, userVO);
        }
        //  如果userDO.teamId为null，那么保存teamId
        if (userDO.getTeamId() == null) {
            userDO.setTeamId(teamId);
            userManager.saveOrUpdateUser(userDO);
        }

        Long userId = userDO.getId();
        userVO.setId(userId);
        //  保存人员的roleGroup
        roleManager.saveOrUpdateUserCommonRoleGroup(userId);
        //  保存team与人员的关联
        teamManager.saveTeamCommonUserRole(teamId, userId);
        //  保存部门与人员的关联
        if (userVO.getOuterCombineDeptIdList() != null) {
            List<String> deptIdList = userVO.getOuterCombineDeptIdList();
            for (String outerDeptId : deptIdList) {
                WbDepartmentDO departmentDO =
                        departmentManager.getDepartmentByClientAndOuterId(userVO.getClient(), outerDeptId);
                if (departmentDO == null) {
                    continue;
                }
                departmentManager.saveDepartmentUser(departmentDO.getId(), userId);
            }
        }
    }

    @Override
    public void updateUser(AutoCreateUserVO userVO) {
        if (userVO.getClient() == null) {
            userVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        }
        WbUserDO userDO = userManager.updateUserInfo(userVO);
        accountManager.updateAccountInfo(userVO);
        Long userId = userDO.getId();
        //  更新部门
        if (userVO.getOuterCombineDeptIdList() != null) {
            List<String> newOuterDeptIdList = userVO.getOuterCombineDeptIdList();
            List<Long> newDeptList = new ArrayList<>(newOuterDeptIdList.size());
            List<Long> oldDeptList = departmentManager.listUserDeptDepartmentIdByUserId(userId);

            //  遍历newOuterDeptIdList，获取到对应的newDeptId
            for (String outerDeptId : newOuterDeptIdList) {
                Long deptId = departmentManager.getOuterDataDepartmentDepartmentIdByClientAndOuterId(
                        userVO.getClient(), outerDeptId
                );
                if (deptId != null) {
                    newDeptList.add(deptId);
                }
            }
            //  遍历newDeptList，如果newDeptId在oldDeptId中不存在，说明是新增的
            for (Long newDeptId : newDeptList) {
                if (!oldDeptList.contains(newDeptId)) {
                    departmentManager.saveDepartmentUser(newDeptId, userId);
                }
            }

            //  遍历oldDeptList，如果oldDeptId在newDeptId中不存在，说明是需要删除的
            for (Long oldDeptId : oldDeptList) {
                if (!newDeptList.contains(oldDeptId)) {
                    departmentManager.deleteDepartmentUser(oldDeptId, userId);
                }
            }
        }
    }

    @Override
    public void updateUserLeaveTeam(AutoCreateUserVO userVO) {
        if (userVO.getClient() == null) {
            userVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        }
        WbUserDO userDO = userManager.getUserByClientAndOuterId(
                userVO.getClient(), userVO.getOuterCombineId()
        );
        if (userDO == null) {
            throw new WorkbeiServiceException(
                    ExceptionCode.getMessage(ExceptionCode.USER_NOT_FOUND, userVO)
            );
        }
        Long userId = userDO.getId();
        //  删除user与team中的department的关联
        List<Long> deptIdList = departmentManager.listUserDeptDepartmentIdByUserId(userId);
        for (Long deptId : deptIdList) {
            departmentManager.deleteDepartmentUser(deptId, userId);
        }
        //  删除user与team的关联
        if (userDO.getTeamId() != null) {
            WbTeamDO teamDO = teamManager.getTeamById(userDO.getTeamId());
            teamManager.deleteTeamUserRole(teamDO.getId(), userDO.getId());
            //  将user中的teamId设置为null
            userDO.setTeamId(null);
            userManager.saveOrUpdateUser(userDO);
        }
    }

    @Override
    public void updateUserSetAdmin(AutoCreateUserVO userVO) {
        if (userVO.getClient() == null) {
            userVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        }
        WbUserDO userDO = userManager.getUserByClientAndOuterId(
                userVO.getClient(), userVO.getOuterCombineId()
        );
        if (userDO == null) {
            throw new WorkbeiServiceException(
                    ExceptionCode.getMessage(ExceptionCode.USER_NOT_FOUND, userVO)
            );
        }
        teamManager.updateTeamAdmin(userDO.getTeamId(), userDO.getId(), userVO.getAdmin());
    }

    @Override
    public void createDepartment(AutoCreateDepartmentVO departmentVO) {
        if (departmentVO.getClient() == null) {
            departmentVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        }
        WbTeamDO teamDO = teamManager.getTeamByClientAndOuterId(
                departmentVO.getClient(), departmentVO.getOuterCorpId());
        if (teamDO == null) {
            throw new WorkbeiServiceException(
                    ExceptionCode.getMessage(ExceptionCode.TEAM_NOT_FOUND, departmentVO)
            );
        }
        WbDepartmentDO departmentDO = departmentManager.saveDepartmentInfo(teamDO.getId(), departmentVO);
        departmentVO.setId(departmentDO.getId());
    }

    @Override
    public void updateDepartment(AutoCreateDepartmentVO departmentVO) {
        if (departmentVO.getClient() == null) {
            departmentVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        }
        departmentManager.updateDepartmentInfo(departmentVO);
    }

    /**
     * 删除一个部门
     *
     * @param departmentVO
     */
    @Override
    public void deleteDepartment(AutoCreateDepartmentVO departmentVO) {
        if (departmentVO.getClient() == null) {
            departmentVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        }
        departmentManager.deleteDepartmentInfo(departmentVO);
    }
}
