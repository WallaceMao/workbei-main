package com.workbei.service.biz.impl;

import com.workbei.constant.WbConstant;
import com.workbei.model.domain.user.*;
import com.workbei.model.view.autocreate.AutoCreateDepartmentVO;
import com.workbei.model.view.autocreate.AutoCreateTeamVO;
import com.workbei.model.view.autocreate.AutoCreateUserVO;
import com.workbei.service.base.DepartmentManageService;
import com.workbei.service.base.TeamManageService;
import com.workbei.service.base.UserManageService;
import com.workbei.service.biz.AutoCreateBizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 14:44
 */
@Service
public class AutoCreateBizServiceImpl implements AutoCreateBizService {
    @Autowired
    private TeamManageService teamManageService;
    @Autowired
    private DepartmentManageService departmentManageService;
    @Autowired
    private UserManageService userManageService;

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
        WbOuterDataTeamDO outerDataTeamDO = teamManageService.getOuterDataTeamByClientAndOuterId(
                teamVO.getClient(), teamVO.getOuterCorpId());
        if(outerDataTeamDO != null){
            return;
        }
        //  新建team
        WbTeamDO teamDO = teamManageService.saveTeamInfo(teamVO);
        //  新建teamDefaultDepartment
        departmentManageService.saveTeamDefaultDepartment(teamDO);
        //  新建user
        if(teamVO.getCreator() != null){
            WbRoleGroupDO roleGroupDO = userManageService.getCommonRoleGroup();
            WbUserDO creatorDO = userManageService.saveUserInfo(teamVO.getCreator(), teamDO, roleGroupDO);
            //  保存team与人员的关联
            teamManageService.saveTeamCreator(teamDO.getId(), creatorDO.getId());
            //  保存部门与人员的关联
            WbDepartmentDO departmentDO = departmentManageService.getTeamUnassignedDepartment(teamDO.getId());
            departmentManageService.saveDepartmentUser(departmentDO.getId(), creatorDO.getId());
        }
    }

    @Override
    public void createUser(AutoCreateUserVO userVO) {
        if(userVO.getClient() == null){
            userVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        }
        WbOuterDataTeamDO outerDataTeamDO = teamManageService.getOuterDataTeamByClientAndOuterId(
                userVO.getClient(), userVO.getOuterCorpId());
        if(outerDataTeamDO == null){
            return;
        }
        WbTeamDO teamDO = teamManageService.getTeamById(outerDataTeamDO.getTeamId());
        if(teamDO == null){
            return;
        }
        WbRoleGroupDO roleGroupDO = userManageService.getCommonRoleGroup();
        WbUserDO userDO = userManageService.saveUserInfo(userVO, teamDO, roleGroupDO);
        //  保存team与人员的关联
        teamManageService.saveTeamCommonUser(teamDO.getId(), userDO.getId());
        //  保存部门与人员的关联
        if(userVO.getOuterCombineDeptIdList() != null){
            List<String> deptIdList = userVO.getOuterCombineDeptIdList();
            for(String outerDeptId : deptIdList){
                WbOuterDataDepartmentDO outerDataDepartmentDO =
                        departmentManageService.getOuterDataDepartmentClientAndOuterId(userVO.getClient(), outerDeptId);
                if(outerDataDepartmentDO == null){
                    continue;
                }
                WbDepartmentDO departmentDO = departmentManageService.getDepartmentById(outerDataDepartmentDO.getDepartmentId());
                if(departmentDO == null){
                    continue;
                }
                departmentManageService.saveDepartmentUser(departmentDO.getId(), userDO.getId());
            }
        }
    }

    @Override
    public void updateUser(AutoCreateUserVO userVO) {
        if(userVO.getClient() == null){
            userVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        }
        WbOuterDataUserDO outerDataUserDO = userManageService.getOuterDataUserByClientAndOuterId(
                userVO.getClient(), userVO.getOuterCombineId()
        );
        if(outerDataUserDO == null){
            return;
        }
        WbUserDO userDO = userManageService.getUserById(outerDataUserDO.getUserId());
        if(userDO == null){
            return;
        }
        Long userId = userDO.getId();
        userManageService.updateUserInfo(userVO, userDO);
        //  更新部门
        if(userVO.getOuterCombineDeptIdList() != null){
            List<String> newOuterDeptIdList = userVO.getOuterCombineDeptIdList();
            List<Long> newDeptList = new ArrayList<>(newOuterDeptIdList.size());
            List<Long> oldDeptList =  departmentManageService.listUserDeptDepartmentIdByUser(userDO.getId());

            //  遍历newOuterDeptIdList，获取到对应的newDeptId
            for(String outerDeptId : newOuterDeptIdList){
                Long deptId = departmentManageService.getOuterDataDepartmentDepartmentIdByClientAndOuterId(
                        userVO.getClient(), outerDeptId
                );
                if(deptId != null){
                    newDeptList.add(deptId);
                }
            }
            //  遍历newDeptList，如果newDeptId在oldDeptId中不存在，说明是新增的
            for(Long newDeptId : newDeptList){
                if(!oldDeptList.contains(newDeptId)){
                    departmentManageService.saveDepartmentUser(newDeptId, userId);
                }
            }

            //  遍历oldDeptList，如果oldDeptId在newDeptId中不存在，说明是需要删除的
            for(Long oldDeptId : oldDeptList){
                if(!newDeptList.contains(oldDeptId)){
                    departmentManageService.removeDepartmentUser(oldDeptId, userId);
                }
            }
        }
    }

    @Override
    public void userLeaveTeam(AutoCreateUserVO userVO) {
        if(userVO.getClient() == null){
            userVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        }
        WbOuterDataUserDO outerDataUserDO = userManageService.getOuterDataUserByClientAndOuterId(
                userVO.getClient(), userVO.getOuterCombineId()
        );
        if(outerDataUserDO == null){
            return;
        }
        WbUserDO userDO = userManageService.getUserById(outerDataUserDO.getUserId());
        Long userId = userDO.getId();
        //  删除user与team中的department的关联
        List<Long> deptIdList = departmentManageService.listUserDeptDepartmentIdByUser(userId);
        for(Long deptId : deptIdList){
            departmentManageService.removeDepartmentUser(deptId, userId);
        }
        //  删除user与team的关联
        WbTeamDO teamDO = teamManageService.getTeamById(userDO.getTeamId());
        teamManageService.removeTeamUser(teamDO.getId(), userDO.getId());
        //  将user中的teamId设置为null
        userDO.setTeamId(null);
        userManageService.saveOrUpdateUser(userDO);
    }

    @Override
    public void userSetAdmin(AutoCreateUserVO userVO) {
        if(userVO.getClient() == null){
            userVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        }
        WbOuterDataUserDO outerDataUserDO = userManageService.getOuterDataUserByClientAndOuterId(
                userVO.getClient(), userVO.getOuterCombineId()
        );
        if(outerDataUserDO == null){
            return;
        }
        WbUserDO userDO = userManageService.getUserById(outerDataUserDO.getUserId());
        teamManageService.updateTeamAdmin(userDO.getTeamId(), userDO.getId(), userVO.getAdmin());
    }

    @Override
    public void createDepartment(AutoCreateDepartmentVO departmentVO) {
        if(departmentVO.getClient() == null){
            departmentVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        }
        WbOuterDataTeamDO outerDataTeamDO = teamManageService.getOuterDataTeamByClientAndOuterId(
                departmentVO.getClient(), departmentVO.getOuterCorpId());
        if(outerDataTeamDO == null){
            return;
        }
        WbTeamDO teamDO = teamManageService.getTeamById(outerDataTeamDO.getTeamId());
        if(teamDO == null){
            return;
        }
        departmentManageService.saveDepartmentInfo(departmentVO, teamDO);
    }

    @Override
    public void updateDepartment(AutoCreateDepartmentVO departmentVO) {
        if(departmentVO.getClient() == null){
            departmentVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        }
        WbDepartmentDO departmentDO = departmentManageService.getDepartmentByOuterId(
                departmentVO.getClient(), departmentVO.getOuterCombineId()
        );
        if(departmentDO == null){
            return;
        }
        departmentManageService.updateDepartmentInfo(departmentVO, departmentDO);
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
        WbDepartmentDO departmentDO = departmentManageService.getDepartmentByOuterId(
                departmentVO.getClient(), departmentVO.getOuterCombineId()
        );
        if(departmentDO == null){
            return;
        }
        departmentManageService.deleteDepartmentInfo(departmentDO);
    }
}
