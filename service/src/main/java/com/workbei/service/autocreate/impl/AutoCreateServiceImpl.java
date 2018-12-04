package com.workbei.service.autocreate.impl;

import com.workbei.constant.WbConstant;
import com.workbei.model.domain.user.*;
import com.workbei.model.view.autocreate.AutoCreateDepartmentVO;
import com.workbei.model.view.autocreate.AutoCreateTeamVO;
import com.workbei.model.view.autocreate.AutoCreateUserVO;
import com.workbei.manager.user.DepartmentManager;
import com.workbei.manager.user.RoleManager;
import com.workbei.manager.user.TeamManager;
import com.workbei.manager.user.UserManager;
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
    @Autowired
    private TeamManager teamManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private RoleManager roleManager;

    /**
     * 创建团队：
     * 1  如果outerDataTeam中能找到，那么就直接返回
     * 2  如果没有创建者，那么就默认一个创建者
     * 3  新增创建者
     * 4  新增团队
     * 5  保存outerDataTeam
     * 6  返回team数据
     * @param teamVO
     * @return
     */
    @Override
    public void createTeam(AutoCreateTeamVO teamVO) {
        if(teamVO.getClient() == null){
            teamVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        }
        WbOuterDataTeamDO outerDataTeamDO = teamManager.getOuterDataTeamByClientAndOuterId(
                teamVO.getClient(), teamVO.getOuterCorpId());
        if(outerDataTeamDO != null){
            return;
        }
        //  新建team
        WbTeamDO teamDO = teamManager.saveTeamInfo(teamVO);
        //  新建teamDefaultDepartment
        departmentManager.saveTeamDefaultDepartment(teamDO.getId(), teamDO.getName());
        //  新建user
        if(teamVO.getCreator() != null){
            WbUserDO creatorDO = userManager.saveUserInfo(teamDO.getId(), teamVO.getCreator());
            //  保存人员的roleGroup
            roleManager.saveOrUpdateUserCommonRoleGroup(creatorDO);
            //  保存team与人员的关联
            teamManager.saveTeamCreator(teamDO.getId(), creatorDO.getId());
            //  保存部门与人员的关联
            WbDepartmentDO departmentDO = departmentManager.getTeamUnassignedDepartment(teamDO.getId());
            departmentManager.saveDepartmentUser(departmentDO.getId(), creatorDO.getId());
        }
    }

    @Override
    public void createUser(AutoCreateUserVO userVO) {
        if(userVO.getClient() == null){
            userVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        }
        //  如果outerDataUser中存在，那么就不创建，直接返回
        WbOuterDataUserDO outerDataUserDO = userManager.getOuterDataUserByClientAndOuterId(
                userVO.getClient(), userVO.getOuterCombineId()
        );
        if(outerDataUserDO != null){
            return ;
        }
        WbOuterDataTeamDO outerDataTeamDO = teamManager.getOuterDataTeamByClientAndOuterId(
                userVO.getClient(), userVO.getOuterCorpId());
        if(outerDataTeamDO == null){
            return;
        }
        WbTeamDO teamDO = teamManager.getTeamById(outerDataTeamDO.getTeamId());
        if(teamDO == null){
            return;
        }
        WbUserDO userDO = userManager.saveUserInfo(teamDO.getId(), userVO);
        //  保存人员的roleGroup
        roleManager.saveOrUpdateUserCommonRoleGroup(userDO);
        //  保存team与人员的关联
        teamManager.saveTeamCommonUser(teamDO.getId(), userDO.getId());
        //  保存部门与人员的关联
        if(userVO.getOuterCombineDeptIdList() != null){
            List<String> deptIdList = userVO.getOuterCombineDeptIdList();
            for(String outerDeptId : deptIdList){
                WbOuterDataDepartmentDO outerDataDepartmentDO =
                        departmentManager.getOuterDataDepartmentByClientAndOuterId(userVO.getClient(), outerDeptId);
                if(outerDataDepartmentDO == null){
                    continue;
                }
                WbDepartmentDO departmentDO = departmentManager.getDepartmentById(outerDataDepartmentDO.getDepartmentId());
                if(departmentDO == null){
                    continue;
                }
                departmentManager.saveDepartmentUser(departmentDO.getId(), userDO.getId());
            }
        }
    }

    @Override
    public void updateUser(AutoCreateUserVO userVO) {
        if(userVO.getClient() == null){
            userVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        }
        WbOuterDataUserDO outerDataUserDO = userManager.getOuterDataUserByClientAndOuterId(
                userVO.getClient(), userVO.getOuterCombineId()
        );
        if(outerDataUserDO == null){
            return;
        }
        WbUserDO userDO = userManager.getUserById(outerDataUserDO.getUserId());
        if(userDO == null){
            return;
        }
        Long userId = userDO.getId();
        userManager.updateUserInfo(userDO, userVO);
        //  更新部门
        if(userVO.getOuterCombineDeptIdList() != null){
            List<String> newOuterDeptIdList = userVO.getOuterCombineDeptIdList();
            List<Long> newDeptList = new ArrayList<>(newOuterDeptIdList.size());
            List<Long> oldDeptList =  departmentManager.listUserDeptDepartmentIdByUser(userDO.getId());

            //  遍历newOuterDeptIdList，获取到对应的newDeptId
            for(String outerDeptId : newOuterDeptIdList){
                Long deptId = departmentManager.getOuterDataDepartmentDepartmentIdByClientAndOuterId(
                        userVO.getClient(), outerDeptId
                );
                if(deptId != null){
                    newDeptList.add(deptId);
                }
            }
            //  遍历newDeptList，如果newDeptId在oldDeptId中不存在，说明是新增的
            for(Long newDeptId : newDeptList){
                if(!oldDeptList.contains(newDeptId)){
                    departmentManager.saveDepartmentUser(newDeptId, userId);
                }
            }

            //  遍历oldDeptList，如果oldDeptId在newDeptId中不存在，说明是需要删除的
            for(Long oldDeptId : oldDeptList){
                if(!newDeptList.contains(oldDeptId)){
                    departmentManager.deleteDepartmentUser(oldDeptId, userId);
                }
            }
        }
    }

    @Override
    public void userLeaveTeam(AutoCreateUserVO userVO) {
        if(userVO.getClient() == null){
            userVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        }
        WbOuterDataUserDO outerDataUserDO = userManager.getOuterDataUserByClientAndOuterId(
                userVO.getClient(), userVO.getOuterCombineId()
        );
        if(outerDataUserDO == null){
            return;
        }
        WbUserDO userDO = userManager.getUserById(outerDataUserDO.getUserId());
        Long userId = userDO.getId();
        //  删除user与team中的department的关联
        List<Long> deptIdList = departmentManager.listUserDeptDepartmentIdByUser(userId);
        for(Long deptId : deptIdList){
            departmentManager.deleteDepartmentUser(deptId, userId);
        }
        //  删除user与team的关联
        WbTeamDO teamDO = teamManager.getTeamById(userDO.getTeamId());
        teamManager.removeTeamUser(teamDO.getId(), userDO.getId());
        //  将user中的teamId设置为null
        userDO.setTeamId(null);
        userManager.saveOrUpdateUser(userDO);
    }

    @Override
    public void userSetAdmin(AutoCreateUserVO userVO) {
        if(userVO.getClient() == null){
            userVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        }
        WbOuterDataUserDO outerDataUserDO = userManager.getOuterDataUserByClientAndOuterId(
                userVO.getClient(), userVO.getOuterCombineId()
        );
        if(outerDataUserDO == null){
            return;
        }
        WbUserDO userDO = userManager.getUserById(outerDataUserDO.getUserId());
        teamManager.updateTeamAdmin(userDO.getTeamId(), userDO.getId(),userVO.getAdmin());
    }

    @Override
    public void createDepartment(AutoCreateDepartmentVO departmentVO) {
        if(departmentVO.getClient() == null){
            departmentVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        }
        WbOuterDataTeamDO outerDataTeamDO = teamManager.getOuterDataTeamByClientAndOuterId(
                departmentVO.getClient(), departmentVO.getOuterCorpId());
        if(outerDataTeamDO == null){
            return;
        }
        WbTeamDO teamDO = teamManager.getTeamById(outerDataTeamDO.getTeamId());
        if(teamDO == null){
            return;
        }
        departmentManager.saveDepartmentInfo(teamDO.getId(), departmentVO);
    }

    @Override
    public void updateDepartment(AutoCreateDepartmentVO departmentVO) {
        if(departmentVO.getClient() == null){
            departmentVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        }
        departmentManager.updateDepartmentInfo(departmentVO);
    }

    /**
     * 删除一个部门
     * @param departmentVO
     */
    @Override
    public void deleteDepartment(AutoCreateDepartmentVO departmentVO) {
        if(departmentVO.getClient() == null){
            departmentVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        }
        departmentManager.deleteDepartmentInfo(departmentVO);
    }
}
