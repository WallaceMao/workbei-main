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
            throw new WorkbeiServiceException(ExceptionCode.getMessage(ExceptionCode.CLIENT_NOT_FOUND));
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
                if (creatorVO.getClient() == null) {
                    creatorVO.setClient(teamVO.getClient());
                }
                WbAccountDO accountDO = searchOrSaveAccount(creatorVO);

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

    /**
     * 创建用户，如果用户存在，那么走的是更新方法
     * @param userVO
     */
    @Override
    public void createUser(AutoCreateUserVO userVO) {
        if (userVO.getClient() == null) {
            throw new WorkbeiServiceException(ExceptionCode.getMessage(ExceptionCode.CLIENT_NOT_FOUND));
        }
        createOrUpdateUser(userVO);
    }

    @Override
    public void updateUser(AutoCreateUserVO userVO) {
        if (userVO.getClient() == null) {
            throw new WorkbeiServiceException(ExceptionCode.getMessage(ExceptionCode.CLIENT_NOT_FOUND));
        }
        createOrUpdateUser(userVO);
    }

    @Override
    public void updateUserLeaveTeam(AutoCreateUserVO userVO) {
        if (userVO.getClient() == null) {
            throw new WorkbeiServiceException(ExceptionCode.getMessage(ExceptionCode.CLIENT_NOT_FOUND));
        }
        WbUserDO userDO = userManager.getUserByClientAndOuterId(
                userVO.getClient(), userVO.getOuterCombineId()
        );
        if (userDO == null) {
            return;
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
            throw new WorkbeiServiceException(ExceptionCode.getMessage(ExceptionCode.CLIENT_NOT_FOUND));
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
    public void updateBatchUserSetAdmin(String client, String corpOuterId, List<String> userOuterIdList) {
        if (client == null) {
            throw new WorkbeiServiceException(ExceptionCode.getMessage(ExceptionCode.CLIENT_NOT_FOUND));
        }
        Long teamId = null;
        List<Long> userIdList = new ArrayList<>(userOuterIdList.size());
        for (String userOuterId : userOuterIdList) {
            WbOuterDataUserDO outerDataUserDO = userManager.getOuterDataUserByClientAndOuterId(
                    client, userOuterId);
            if (outerDataUserDO != null) {
                userIdList.add(outerDataUserDO.getUserId());
                if (teamId == null) {
                    WbUserDO userDO = userManager.getUserByClientAndOuterId(
                            client, userOuterId
                    );
                    if (userDO != null) {
                        teamId = userDO.getTeamId();
                    }
                }
            }
        }
        if (teamId != null) {
            teamManager.updateBatchTeamAdmin(teamId, userIdList);
        }
    }

    @Override
    public void createDepartment(AutoCreateDepartmentVO departmentVO) {
        if (departmentVO.getClient() == null) {
            throw new WorkbeiServiceException(ExceptionCode.getMessage(ExceptionCode.CLIENT_NOT_FOUND));
        }
        WbTeamDO teamDO = checkTeam(departmentVO.getClient(), departmentVO.getOuterCorpId());
        WbDepartmentDO departmentDO = departmentManager.saveDepartmentInfo(teamDO.getId(), departmentVO);
        departmentVO.setId(departmentDO.getId());
    }

    @Override
    public void updateDepartment(AutoCreateDepartmentVO departmentVO) {
        if (departmentVO.getClient() == null) {
            throw new WorkbeiServiceException(ExceptionCode.getMessage(ExceptionCode.CLIENT_NOT_FOUND));
        }
        // 修改部门的时候，如果部门不存在，那么直接创建
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
            throw new WorkbeiServiceException(ExceptionCode.getMessage(ExceptionCode.CLIENT_NOT_FOUND));
        }
        departmentManager.deleteDepartmentInfo(departmentVO);
    }

    private void createOrUpdateUser(AutoCreateUserVO userVO) {
        WbTeamDO teamDO = checkTeam(userVO.getClient(), userVO.getOuterCorpId());
        Long teamId = teamDO.getId();

        // 判断如果user不存在，那么走createUser的方法
        WbUserDO userDO = userManager.getUserByClientAndOuterId(userVO.getClient(), userVO.getOuterCombineId());
        if (userDO == null) {
            createNotExistedUser(userVO, teamDO);
        }else {
            updateExistedUser(userVO, teamDO);
        }
    }
    /**
     * 创建用户
     */
    private void createNotExistedUser(AutoCreateUserVO userVO, WbTeamDO teamDO){
        Long teamId = teamDO.getId();
        //  1. 新增account
        WbAccountDO accountDO = searchOrSaveAccount(userVO);
        Long accountId = accountDO.getId();
        //  2. 新增user
        WbUserDO userDO = userManager.saveUserInfo(teamId, accountId, userVO);

        Long userId = userDO.getId();
        userVO.setId(userId);
        //  保存人员的roleGroup
        roleManager.saveOrUpdateUserCommonRoleGroup(userId);
        //  保存team与人员的关联
        teamManager.saveTeamCommonUserRole(teamId, userId);
        //  保存部门与人员的关联
        if (userVO.getOuterCombineDeptIdList() != null) {
            correctDepartment(userVO.getClient(), userDO, userVO.getOuterCombineDeptIdList());

            // deprecated：统一使用correctDepartment的算法，不在单独处理
            // List<String> deptIdList = userVO.getOuterCombineDeptIdList();
            // boolean hasDept = false;
            // for (String outerDeptId : deptIdList) {
            //     WbDepartmentDO departmentDO =
            //             departmentManager.getDepartmentByClientAndOuterId(userVO.getClient(), outerDeptId);
            //     if (departmentDO == null) {
            //         continue;
            //     }
            //     hasDept = true;
            //     departmentManager.saveDepartmentUser(departmentDO.getId(), userId);
            // }
            // // 如果没有找到部门，那么将用户保存到未分配部门中
            // if (!hasDept) {
            //     WbDepartmentDO unassignedDepartment = departmentManager.getTeamUnassignedDepartment(teamId);
            //     departmentManager.saveDepartmentUser(unassignedDepartment.getId(), userId);
            // }
        }
    }

    /**
     * 更新用户
     */
    private void updateExistedUser(AutoCreateUserVO userVO, WbTeamDO teamDO){
        Long teamId = teamDO.getId();

        WbUserDO userDO = userManager.updateUserInfo(userVO);
        accountManager.updateAccountInfo(userVO);
        userVO.setId(userDO.getId());

        //  如果userDO.teamId为null，那么保存teamId
        if (userDO.getTeamId() == null) {
            userDO.setTeamId(teamId);
            userManager.saveOrUpdateUser(userDO);
        }
        //  更新部门
        if (userVO.getOuterCombineDeptIdList() != null) {
            correctDepartment(userVO.getClient(), userDO, userVO.getOuterCombineDeptIdList());
        }
    }

    /**
     * 校正userDO所属的部门
     * @param userDO
     * @param newOuterDeptIdList
     */
    private void correctDepartment(String client, WbUserDO userDO, List<String> newOuterDeptIdList){
        Long userId = userDO.getId();
        List<Long> newDeptList = new ArrayList<>(newOuterDeptIdList.size());
        List<Long> oldDeptList = departmentManager.listUserDeptDepartmentIdByUserId(userId);

        //  遍历newOuterDeptIdList，获取到对应的newDeptId
        for (String outerDeptId : newOuterDeptIdList) {
            Long deptId = departmentManager.getOuterDataDepartmentDepartmentIdByClientAndOuterId(
                    client, outerDeptId
            );
            if (deptId != null) {
                newDeptList.add(deptId);
            }
        }
        //  遍历oldDeptList，如果oldDeptId在newDeptId中不存在，说明是需要删除的
        for (Long oldDeptId : oldDeptList) {
            if (!newDeptList.contains(oldDeptId)) {
                departmentManager.deleteDepartmentUser(oldDeptId, userId);
            }
        }
        //  遍历newDeptList，如果newDeptId在oldDeptId中不存在，说明是新增的
        for (Long newDeptId : newDeptList) {
            if (!oldDeptList.contains(newDeptId)) {
                departmentManager.saveDepartmentUser(newDeptId, userId);
            }
        }
        // 如果最终这个用户没有任何部门关联，那么会将其放到未分配部门中
        List<Long> finalDeptList = departmentManager.listUserDeptDepartmentIdByUserId(userId);
        if (finalDeptList.size() == 0) {
            WbDepartmentDO unassignedDepartment = departmentManager.getTeamUnassignedDepartment(userDO.getTeamId());
            departmentManager.saveDepartmentUser(unassignedDepartment.getId(), userId);
        }
    }

    /**
     * 如果unionId在数据库中存在，那么就根据unionId获取account，否则就新增account，并且保存unionId
     * @param userVO
     * @return
     */
    private WbAccountDO searchOrSaveAccount(AutoCreateUserVO userVO) {
        WbAccountDO accountDO = null;
        if (userVO.getOuterUnionId() != null) {
            accountDO = accountManager.getAccountByDdUnionId(userVO.getOuterUnionId());
        }
        if (accountDO == null) {
            accountDO = accountManager.saveAccountInfo(userVO);
        }
        return accountDO;
    }

    private WbTeamDO checkTeam(String client, String corpId) {
        WbTeamDO teamDO = teamManager.getTeamByClientAndOuterId(client, corpId);
        if (teamDO == null) {
            throw new WorkbeiServiceException(
                    ExceptionCode.getMessage(ExceptionCode.TEAM_NOT_FOUND, corpId)
            );
        }
        return teamDO;
    }
}
