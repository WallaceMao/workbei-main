package com.workbei.service.base.impl;

import com.workbei.constant.WbConstant;
import com.workbei.dao.user.WbDepartmentDao;
import com.workbei.dao.user.WbOuterDataDepartmentDao;
import com.workbei.model.domain.user.*;
import com.workbei.model.view.autocreate.AutoCreateDepartmentVO;
import com.workbei.service.base.DepartmentManageService;
import factory.DepartmentFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 15:53
 */
public class DepartmentManageServiceImpl implements DepartmentManageService {
    @Autowired
    private WbDepartmentDao wbDepartmentDao;
    @Autowired
    private WbOuterDataDepartmentDao wbOuterDataDepartmentDao;

    @Override
    public void saveOrUpdateDepartment(WbDepartmentDO department) {
        wbDepartmentDao.saveOrUpdateDepartment(department);
    }

    @Override
    public void saveOrUpdateOuterDataDepartment(WbOuterDataDepartmentDO outerDataDepartmentDO) {
        wbOuterDataDepartmentDao.saveOrUpdateOuterDataDepartment(outerDataDepartmentDO);
    }

    @Deprecated
    @Override
    public WbOuterDataDepartmentDO getOuterDataDepartmentClientAndOuterId(String client, String outerCorpId){
        return wbOuterDataDepartmentDao.getOuterDataDepartmentByClientAndOuterId(client, outerCorpId);
    }

    @Override
    public Long getOuterDataDepartmentDepartmentIdByClientAndOuterId(String client, String outerId){
        return wbOuterDataDepartmentDao.getOuterDataDepartmentDepartmentIdByClientAndOuterId(client, outerId);
    }

    @Override
    public WbDepartmentDO getDepartmentById(Long id){
        return wbDepartmentDao.getDepartmentById(id);
    }

    @Override
    public WbDepartmentDO getDepartmentByClientAndOuterId(String client, String outerId){
        return wbDepartmentDao.getDepartmentByClientAndOuterId(client, outerId);
    }

    @Override
    public List<WbDepartmentDO> listDepartmentByTeamId(Long teamId){
        return wbDepartmentDao.listDepartmentByTeamId(teamId);
    }

    @Override
    public List<Long> listUserDeptDepartmentIdByUser(Long userId) {
        return wbDepartmentDao.listUserDeptDepartmentIdByUserId(userId);
    }

    @Override
    public WbDepartmentDO getTeamUnassignedDepartment(Long teamId) {
        return wbDepartmentDao.getUnassignedDepartment(teamId);
    }

    @Override
    public void saveTeamDefaultDepartment(Long teamId, String topDepartmentName) {
        //  创建顶级部门
        WbDepartmentDO topDept = DepartmentFactory.getDepartmentDO();
        topDept.setName(topDepartmentName);
        topDept.setLevel(1);
        topDept.setCode("");
        topDept.setTeamId(teamId);
        topDept.setType(WbConstant.DEPARTMENT_TYPE_TOP);
        wbDepartmentDao.saveOrUpdateDepartment(topDept);

        topDept.setCode(String.valueOf(topDept.getId()));
        wbDepartmentDao.saveOrUpdateDepartment(topDept);

        //  创建未分配部门
        WbDepartmentDO unassigned = DepartmentFactory.getDepartmentDO();
        unassigned.setName(WbConstant.DEPARTMENT_NAME_UNASSIGNED);
        unassigned.setLevel(2);
        unassigned.setCode("");
        unassigned.setTeamId(teamId);
        unassigned.setType(WbConstant.DEPARTMENT_TYPE_UNASSIGNED);
        unassigned.setParentId(topDept.getId());
        wbDepartmentDao.saveOrUpdateDepartment(unassigned);

        unassigned.setCode(topDept.getId() + WbConstant.DEPARTMENT_CODE_SEPARATOR + unassigned.getId());
        wbDepartmentDao.saveOrUpdateDepartment(unassigned);
    }

    @Override
    public void saveDepartmentInfo(Long teamId, AutoCreateDepartmentVO departmentVO){
        //  查找父级部门
        WbDepartmentDO dept = null;
        if(departmentVO.getTop()){
            //  说明是根部门，根部门不做处理，直接把topDepartment作为根部门
            dept = wbDepartmentDao.getTopDepartment(teamId);
        }else{
            //  说明是普通部门
            WbDepartmentDO parentDept = wbDepartmentDao.getDepartmentByClientAndOuterId(
                    departmentVO.getClient(), departmentVO.getOuterParentCombineId()
            );
            //  如果父级部门没找到，那么就直接挂到topDepartment下
            if(parentDept == null){
                parentDept = getTeamTopDepartment(teamId);
            }
            dept = DepartmentFactory.getDepartmentDO();
            dept.setName(departmentVO.getName());
            dept.setLevel(parentDept.getLevel() + 1);
            dept.setCode("");
            dept.setTeamId(teamId);
            dept.setDisplayOrder(departmentVO.getDisplayOrder());
            dept.setType(WbConstant.DEPARTMENT_TYPE_COMMON);
            dept.setParentId(parentDept.getId());
            wbDepartmentDao.saveOrUpdateDepartment(dept);

            dept.setCode(parentDept.getCode() + WbConstant.DEPARTMENT_CODE_SEPARATOR + dept.getId());
            wbDepartmentDao.saveOrUpdateDepartment(dept);
        }

        if(departmentVO.getOuterCombineId() != null){
            WbOuterDataDepartmentDO outerDataDepartmentDO = DepartmentFactory.getOuterDataDepartmentDO();
            outerDataDepartmentDO.setClient(departmentVO.getClient());
            outerDataDepartmentDO.setDepartmentId(dept.getId());
            outerDataDepartmentDO.setOuterId(departmentVO.getOuterCombineId());
            wbOuterDataDepartmentDao.saveOrUpdateOuterDataDepartment(outerDataDepartmentDO);
        }
    }

    /**
     * 首先新增userDept的关联
     * 然后循环新增userDeptAscription的关联
     * @param departmentId
     * @param userId
     */
    @Override
    public void saveDepartmentUser(Long departmentId, Long userId) {
        WbUserDeptDO userDeptDO = DepartmentFactory.getUserDeptDO();
        userDeptDO.setUserId(userId);
        userDeptDO.setDepartmentId(departmentId);
        wbDepartmentDao.saveOrUpdateUserDept(userDeptDO);

        WbDepartmentDO departmentDO = wbDepartmentDao.getDepartmentById(departmentId);
        String code = departmentDO.getCode();
        String[] idArray = code.split(WbConstant.DEPARTMENT_CODE_SEPARATOR);
        List<String> parentList = Arrays.asList(idArray);
        for(String parentDeptId : parentList){
            WbUserDeptAscriptionDO userDeptAscriptionDO = DepartmentFactory.getUserDeptAscriptionDO();
            userDeptAscriptionDO.setUserId(userId);
            userDeptAscriptionDO.setDepartmentId(Long.valueOf(parentDeptId));
            wbDepartmentDao.saveOrUpdateUserDeptAscription(userDeptAscriptionDO);
        }
    }

    @Override
    public WbDepartmentDO getTeamTopDepartment(Long teamId) {
        return wbDepartmentDao.getTopDepartment(teamId);
    }

    /**
     * 首先删除userDept的关联
     * 然后循环删除userDeptAscription的关联
     * @param departmentId
     * @param userId
     */
    @Override
    public void removeDepartmentUser(Long departmentId, Long userId){
        wbDepartmentDao.deleteUserDeptByUserIdAndDepartmentId(userId, departmentId);
        WbDepartmentDO departmentDO = wbDepartmentDao.getDepartmentById(departmentId);
        String code = departmentDO.getCode();
        String[] idArray = code.split(WbConstant.DEPARTMENT_CODE_SEPARATOR);
        List<String> parentList = Arrays.asList(idArray);
        for(String parentDeptId : parentList){
            wbDepartmentDao.deleteUserDeptAscriptionByUserIdAndDepartmentId(userId, Long.valueOf(parentDeptId));
        }
    }

    @Override
    public void updateDepartmentInfo(AutoCreateDepartmentVO departmentVO, WbDepartmentDO departmentDO) {
        //  修改基本信息
        boolean baseInfoChanged = false;
        if(departmentVO.getName() != null){
            baseInfoChanged = true;
            departmentDO.setName(departmentVO.getName());
        }
        if(departmentVO.getDisplayOrder() != null){
            baseInfoChanged = true;
            departmentDO.setDisplayOrder(departmentVO.getDisplayOrder());
        }
        if(baseInfoChanged){
            wbDepartmentDao.saveOrUpdateDepartment(departmentDO);
        }
        //  修改父部门信息
        if(departmentVO.getOuterParentCombineId() != null){
            WbDepartmentDO parentDept = getDepartmentByClientAndOuterId(
                    departmentVO.getClient(), departmentVO.getOuterParentCombineId()
            );
            //  如果parentDept为null，那么就放到顶级部门下
            if(parentDept == null){
                parentDept = wbDepartmentDao.getTopDepartment(departmentDO.getTeamId());
            }
            if(!departmentDO.getId().equals(parentDept.getId())){
                changeDepartment(departmentDO, parentDept);
            }
        }
    }

    private void changeDepartment(WbDepartmentDO dept, WbDepartmentDO parentDept){
        /*
        * 以下情况不能移动部门：
        * 1、要移动的部门是最顶级部门
        * 2、要移动的部门是未分配部门
        * 3、移动的父部门是要移动的部门的子部门
        * */
        if(dept.getType().equals(WbConstant.DEPARTMENT_TYPE_TOP) ||
                dept.getType().equals(WbConstant.DEPARTMENT_TYPE_UNASSIGNED)){
            return;
        }
        if(dept.getParentId().equals(parentDept.getId())){
            return;
        }
        List<Long> oldParentIdList = getParentDepartmentIdList(dept);
        Long deptId = dept.getId();
        //  修改dept的parentId
        dept.setParentId(parentDept.getId());
        wbDepartmentDao.saveOrUpdateDepartment(dept);
        List<Long> excludeIdList = new ArrayList<>();
        //  递归调整所有子部门的code和level，为防止死循环，需要加上excludeIdList做判断
        refreshSubDepartmentRecursive(dept, excludeIdList);
        //  处理userDeptAscription，逻辑如下：
        //  根据oldParentIdList和newParentIdList，做比对
        List<Long> newParentIdList = getParentDepartmentIdList(dept);
        Set<Long> deleteDeptIdSet = new HashSet<>(oldParentIdList.size());
        Set<Long> createDeptIdSet = new HashSet<>(newParentIdList.size());
        for(Long oldDeptId : oldParentIdList){
            if(!newParentIdList.contains(oldDeptId)){
                deleteDeptIdSet.add(oldDeptId);
            }
        }
        for(Long newDeptId : newParentIdList){
            if(!oldParentIdList.contains(newDeptId)){
                createDeptIdSet.add(newDeptId);
            }
        }
        //  读取当前部门下面的所有用户，根据之前查找到的需要删除的和需要新增的userDeptAscription，做相应操作
        List<Long> ascriptionIdList = wbDepartmentDao.listUserDeptAscriptionUserIdByDepartmentId(deptId);
        for(Long ascriptionUserId : ascriptionIdList){
            for(Long deleteDeptId : deleteDeptIdSet){
                wbDepartmentDao.deleteUserDeptAscriptionByUserIdAndDepartmentId(ascriptionUserId, deleteDeptId);
            }
            for(Long createDeptId : createDeptIdSet){
                WbUserDeptAscriptionDO userDeptAscriptionDO = DepartmentFactory.getUserDeptAscriptionDO();
                userDeptAscriptionDO.setUserId(ascriptionUserId);
                userDeptAscriptionDO.setDepartmentId(createDeptId);
                wbDepartmentDao.saveOrUpdateUserDeptAscription(userDeptAscriptionDO);
            }
        }
    }

    /**
     * 从departmentDO开始，重新生成该部门及其子部门的code和level
     * @param dept
     */
    private void refreshSubDepartmentRecursive(WbDepartmentDO dept, List<Long> excludeIdList){
        Long deptId = dept.getId();
        //  递归修改
        //  判断是否产生了循环（例如父部门被移动到另子部门下）
        if(excludeIdList.contains(deptId)){
            return;
        }
        //  先修改本部门
        WbDepartmentDO parentDept = wbDepartmentDao.getDepartmentById(dept.getParentId());
        dept.setLevel(parentDept.getLevel() + 1);
        dept.setCode(parentDept.getCode() + WbConstant.DEPARTMENT_CODE_SEPARATOR + dept.getId());
        wbDepartmentDao.saveOrUpdateDepartment(dept);

        excludeIdList.add(deptId);
        List<WbDepartmentDO> childDeptList = wbDepartmentDao.listDepartmentsByParentId(deptId);
        for(WbDepartmentDO childDept : childDeptList){
            refreshSubDepartmentRecursive(childDept, excludeIdList);
        }
    }

    private List<Long> getParentDepartmentIdList(WbDepartmentDO dept){
        WbDepartmentDO oldParentDept = wbDepartmentDao.getDepartmentById(dept.getParentId());
        //  获取到dept原来的所有父部门的id，用来做比对更新userDeptAscription
        List<Long> parentIdList = new ArrayList<>();
        collectParentDepartmentIdRecursive(oldParentDept, parentIdList);
        return parentIdList;
    }

    /**
     * 查找departmentDO所有的父级department的id
     * @param departmentDO
     * @return
     */
    private void collectParentDepartmentIdRecursive(WbDepartmentDO departmentDO, List<Long> parentIdList){
        parentIdList.add(departmentDO.getId());
        if(departmentDO.getType().equals(WbConstant.DEPARTMENT_TYPE_TOP)){
            return;
        }
        WbDepartmentDO parentDept = wbDepartmentDao.getDepartmentById(departmentDO.getParentId());
        collectParentDepartmentIdRecursive(parentDept, parentIdList);
    }

    @Override
    public void deleteDepartmentInfo(WbDepartmentDO departmentDO) {
        //  递归删除
        deleteDepartmentRecursive(departmentDO);
    }

    private void deleteDepartmentRecursive(WbDepartmentDO departmentDO){
        //  如果有子部门，先逐个调用子部门进行删除
        List<WbDepartmentDO> deptList = wbDepartmentDao.listDepartmentsByParentId(departmentDO.getId());
        for(WbDepartmentDO dept : deptList){
            deleteDepartmentRecursive(dept);
        }
        //  最后删除本部门
        deleteSingleDepartment(departmentDO);
    }

    private void deleteSingleDepartment(WbDepartmentDO departmentDO){
        Long teamId = departmentDO.getTeamId();
        Long deptId = departmentDO.getId();
        //  删除userDept，实际是将该部门的用户移动到未分配部门下
        WbDepartmentDO unassignedDept = wbDepartmentDao.getUnassignedDepartment(teamId);
        wbDepartmentDao.updateUserDeptDepartmentId(deptId, unassignedDept.getId());
        //  删除userDeptAscription
        wbDepartmentDao.deleteUserDeptAscriptionByDepartmentId(deptId);
        //  删除department
        wbDepartmentDao.deleteDepartmentById(deptId);
    }
}
