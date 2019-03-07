package com.workbei.manager.user.impl;

import com.workbei.constant.WbConstant;
import com.workbei.dao.user.WbDepartmentDao;
import com.workbei.dao.user.WbOuterDataDepartmentDao;
import com.workbei.exception.ExceptionCode;
import com.workbei.exception.WorkbeiServiceException;
import com.workbei.factory.DepartmentFactory;
import com.workbei.model.domain.user.*;
import com.workbei.model.view.autocreate.AutoCreateDepartmentVO;
import com.workbei.manager.user.DepartmentManager;

import java.util.*;

import static com.workbei.exception.ExceptionCode.*;

/**
 * Department聚合，
 * 包括的实体：
 * WbDepartmentDO
 * WbOuterDataDepartmentDO
 * 包括的关联：
 * WbUserDeptDO
 * WbUserDeptAscriptionDO
 *
 * @author Wallace Mao
 * Date: 2018-11-27 15:53
 */
public class DepartmentManagerImpl implements DepartmentManager {
    private WbDepartmentDao wbDepartmentDao;
    private WbOuterDataDepartmentDao wbOuterDataDepartmentDao;

    public DepartmentManagerImpl(WbDepartmentDao wbDepartmentDao, WbOuterDataDepartmentDao wbOuterDataDepartmentDao) {
        this.wbDepartmentDao = wbDepartmentDao;
        this.wbOuterDataDepartmentDao = wbOuterDataDepartmentDao;
    }

    //  --------department--------

    /**
     * 注意，在使用该方法的时候，需要手动更新code值。如果不想手动更新，可以调用saveOrUpdateDepartment(WbDepartmentDO department, String parentCode)方法
     *
     * @param department
     */
    @Override
    public WbDepartmentDO saveOrUpdateDepartment(WbDepartmentDO department) {
        wbDepartmentDao.saveOrUpdateDepartment(department);
        return department;
    }

    /**
     * 如果指定了parentCode，那么会在保存department后更新code
     *  @param department
     * @param parentCode
     */
    @Override
    public WbDepartmentDO saveOrUpdateDepartment(WbDepartmentDO department, String parentCode) {
        wbDepartmentDao.saveOrUpdateDepartment(department);
        if (parentCode != null && department.getId() != null) {
            department.setCode(parentCode + WbConstant.DEPARTMENT_CODE_SEPARATOR + department.getId());
            wbDepartmentDao.saveOrUpdateDepartment(department);
        }
        return department;
    }

    @Override
    public WbDepartmentDO getDepartmentById(Long id) {
        return wbDepartmentDao.getDepartmentById(id);
    }

    @Override
    public WbDepartmentDO getDepartmentByClientAndOuterId(String client, String outerId) {
        return wbDepartmentDao.getDepartmentByClientAndOuterId(client, outerId);
    }

    @Override
    public WbDepartmentDO getTeamTopDepartment(Long teamId) {
        return wbDepartmentDao.getTopDepartment(teamId);
    }

    @Override
    public WbDepartmentDO getTeamUnassignedDepartment(Long teamId) {
        return wbDepartmentDao.getUnassignedDepartment(teamId);
    }

    @Override
    public List<WbDepartmentDO> listDepartmentByTeamId(Long teamId) {
        return wbDepartmentDao.listDepartmentByTeamId(teamId);
    }

    //  --------outerDataDepartment--------
    @Override
    public WbOuterDataDepartmentDO saveOrUpdateOuterDataDepartment(WbOuterDataDepartmentDO outerDataDepartmentDO) {
        wbOuterDataDepartmentDao.saveOrUpdateOuterDataDepartment(outerDataDepartmentDO);
        return outerDataDepartmentDO;
    }

    @Override
    public WbOuterDataDepartmentDO getOuterDataDepartmentByClientAndOuterId(String client, String outerId) {
        return wbOuterDataDepartmentDao.getOuterDataDepartmentByClientAndOuterId(client, outerId);
    }

    @Override
    public WbOuterDataDepartmentDO getOuterDataDepartmentByClientAndDepartmentId(String client, Long departmentId) {
        return wbOuterDataDepartmentDao.getOuterDataDepartmentByClientAndDepartmentId(client, departmentId);
    }
    @Override
    public Long getOuterDataDepartmentDepartmentIdByClientAndOuterId(String client, String outerId) {
        return wbOuterDataDepartmentDao.getOuterDataDepartmentDepartmentIdByClientAndOuterId(client, outerId);
    }

    //  --------userDept--------
    @Override
    public WbUserDeptDO saveOrUpdateUserDept(WbUserDeptDO userDeptDO) {
        wbDepartmentDao.saveOrUpdateUserDept(userDeptDO);
        return userDeptDO;
    }

    @Override
    public WbUserDeptDO getUserDeptByDepartmentIdAndUserId(Long departmentId, Long userId) {
        return wbDepartmentDao.getUserDeptByDepartmentIdAndUserId(departmentId, userId);
    }

    @Override
    public List<Long> listUserDeptDepartmentIdByUserId(Long userId) {
        return wbDepartmentDao.listUserDeptDepartmentIdByUserId(userId);
    }

    @Override
    public List<Long> listUserDeptUserIdByDepartmentId(Long departmentId) {
        return wbDepartmentDao.listUserDeptUserIdByDepartmentId(departmentId);
    }

    @Override
    public List<WbUserDeptDO> listUserDeptByUserId(Long userId) {
        return wbDepartmentDao.listUserDeptByUserId(userId);
    }

    //  --------userDeptAscription--------
    @Override
    public WbUserDeptAscriptionDO saveOrUpdateUserDeptAscription(WbUserDeptAscriptionDO userDeptAscriptionDO) {
        wbDepartmentDao.saveOrUpdateUserDeptAscription(userDeptAscriptionDO);
        return userDeptAscriptionDO;
    }

    @Override
    public WbUserDeptAscriptionDO getUserDeptAscriptionByDepartmentIdAndUserId(Long departmentId, Long userId) {
        return wbDepartmentDao.getUserDeptAscriptionByDepartmentIdAndUserId(departmentId, userId);
    }

    @Override
    public List<Long> listUserDeptAscriptionDepartmentIdByUserId(Long userId) {
        return wbDepartmentDao.listUserDeptAscriptionDepartmentIdByUserId(userId);
    }

    @Override
    public List<Long> listUserDeptAscriptionUserIdByDepartmentId(Long departmentId) {
        return wbDepartmentDao.listUserDeptAscriptionUserIdByDepartmentId(departmentId);
    }

    //  --------aggregate method--------

    /**
     * 保存team的默认的top和unassigned的department
     *  @param teamId
     * @param topDepartmentName
     */
    @Override
    public WbDepartmentDO saveTeamDefaultDepartment(Long teamId, String topDepartmentName) {
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

        return topDept;
    }

    /**
     * 保存部门信息
     *  @param teamId
     * @param departmentVO
     */
    @Override
    public WbDepartmentDO saveDepartmentInfo(Long teamId, AutoCreateDepartmentVO departmentVO) {

        WbDepartmentDO dept = searchAndSaveDepartment(teamId, departmentVO);

        if (departmentVO.getOuterCombineId() != null) {
            WbOuterDataDepartmentDO outerDataDepartmentDO = DepartmentFactory.getOuterDataDepartmentDO();
            outerDataDepartmentDO.setClient(departmentVO.getClient());
            outerDataDepartmentDO.setDepartmentId(dept.getId());
            outerDataDepartmentDO.setOuterId(departmentVO.getOuterCombineId());
            wbOuterDataDepartmentDao.saveOrUpdateOuterDataDepartment(outerDataDepartmentDO);
        }
        return dept;
    }

    /**
     * 修改departmentInfo
     *
     * @param departmentVO
     */
    @Override
    public WbDepartmentDO updateDepartmentInfo(AutoCreateDepartmentVO departmentVO) {
        WbDepartmentDO departmentDO = getDepartmentByClientAndOuterId(
                departmentVO.getClient(), departmentVO.getOuterCombineId());
        if (departmentDO == null) {
            throw new WorkbeiServiceException(
                    ExceptionCode.getMessage(DEPT_NOT_FOUND, "departmentVO: ", departmentVO));
        }
        //  修改基本信息
        updateDepartmentBaseInfo(departmentDO, departmentVO);
        //  修改父部门信息
        if (departmentVO.getOuterParentCombineId() != null) {
            WbDepartmentDO parentDept = getDepartmentByClientAndOuterId(
                    departmentVO.getClient(), departmentVO.getOuterParentCombineId()
            );
            //  如果parentDept为null，那么就放到顶级部门下
            if (parentDept == null) {
                parentDept = wbDepartmentDao.getTopDepartment(departmentDO.getTeamId());
            }
            //  根据parentCombineId获取到的parentDept的id为新的parentId
            //  根据departmentDO.getParentId获取到的是旧的parentId
            //  如果二者不一致，那么就移动部门
            if (!parentDept.getId().equals(departmentDO.getParentId())) {
                changeDepartment(departmentDO, parentDept);
            }
        }
        return departmentDO;
    }

    /**
     * 删除departmentInfo
     *
     * @param departmentVO
     */
    @Override
    public void deleteDepartmentInfo(AutoCreateDepartmentVO departmentVO) {
        WbDepartmentDO departmentDO = getDepartmentByClientAndOuterId(
                departmentVO.getClient(), departmentVO.getOuterCombineId()
        );
        // 为保持幂等性，多次删除这里直接return
        if (departmentDO == null) {
            return;
        }
        //  未分配部门不能删除
        String type = departmentDO.getType();
        if (WbConstant.DEPARTMENT_TYPE_UNASSIGNED.equals(type)) {
            return;
        }
        //  按照以下步骤进行：
        //  1. 删除所有除topDepartment之外的userDeptAscription
        //  2. 删除department及其关联，包括userDept、outerDataDepartment等
        //  3. 读取此时没有部门的user，将其所在的部门保存为unassigned（未分配部门）
        //  4. 重建userDeptAscription
        Long teamId = departmentDO.getTeamId();
        Long deptId = departmentDO.getId();
        //  删除userDeptAscription，以便后面重建
        List<Long> allDeptUserIdList = wbDepartmentDao.listUserDeptAscriptionUserIdByDepartmentId(deptId);
        for (Long userId : allDeptUserIdList) {
            wbDepartmentDao.deleteUserDeptAscriptionByUserIdAndDepartmentType(userId, WbConstant.DEPARTMENT_TYPE_COMMON);
            wbDepartmentDao.deleteUserDeptAscriptionByUserIdAndDepartmentType(userId, WbConstant.DEPARTMENT_TYPE_UNASSIGNED);
        }
        //  删除department及其关联，包括userDept、outerDataDepartment等
        deleteSingleDepartment(departmentDO);
        //  读取出没有部门的员工列表，将其关联为未分配部门中
        List<Long> userIdListWithoutDepartment = wbDepartmentDao.listUserUserIdWithoutDepartment(teamId);
        WbDepartmentDO unassignedDepartment = wbDepartmentDao.getUnassignedDepartment(teamId);
        for (Long userId : userIdListWithoutDepartment) {
            saveOrUpdateUserDept(unassignedDepartment.getId(), userId);
        }
        //  重建userDeptAscription
        for (Long userId : allDeptUserIdList) {
            List<Long> newDeptIdList = wbDepartmentDao.listUserDeptDepartmentIdByUserId(userId);
            for (Long newDeptId : newDeptIdList) {
                saveUserDeptAscriptionRecursive(newDeptId, userId);
            }
        }
    }

    /**
     * 首先新增userDept的关联
     * 然后循环新增userDeptAscription的关联
     *  @param departmentId
     * @param userId
     */
    @Override
    public WbUserDeptDO saveDepartmentUser(Long departmentId, Long userId) {
        WbUserDeptDO userDeptDO = saveOrUpdateUserDept(departmentId, userId);
        saveUserDeptAscriptionRecursive(departmentId, userId);
        return userDeptDO;
    }

    /**
     * 首先删除userDept的关联
     * 然后循环删除userDeptAscription的关联
     *
     * @param departmentId
     * @param userId
     */
    @Override
    public void deleteDepartmentUser(Long departmentId, Long userId) {
        wbDepartmentDao.deleteUserDeptByDepartmentIdAndUserId(departmentId, userId);
        deleteUserDeptAscriptionRecursive(departmentId, userId);
    }

    private void saveUserDeptAscriptionRecursive(Long departmentId, Long userId) {
        if (departmentId == null) {
            return;
        }
        WbDepartmentDO departmentDO = wbDepartmentDao.getDepartmentById(departmentId);
        if (departmentDO == null) {
            return;
        }
        WbUserDeptAscriptionDO userDeptAscriptionDO = DepartmentFactory.getUserDeptAscriptionDO();
        userDeptAscriptionDO.setUserId(userId);
        userDeptAscriptionDO.setDepartmentId(departmentDO.getId());
        wbDepartmentDao.saveOrUpdateUserDeptAscription(userDeptAscriptionDO);
        saveUserDeptAscriptionRecursive(departmentDO.getParentId(), userId);
    }

    private void deleteUserDeptAscriptionRecursive(Long departmentId, Long userId) {
        if (departmentId == null) {
            return;
        }
        WbDepartmentDO departmentDO = wbDepartmentDao.getDepartmentById(departmentId);
        if (departmentDO == null) {
            return;
        }
        wbDepartmentDao.deleteUserDeptAscriptionByDepartmentIdAndUserId(departmentId, userId);
        deleteUserDeptAscriptionRecursive(departmentDO.getParentId(), userId);
    }

    private void changeDepartment(WbDepartmentDO dept, WbDepartmentDO parentDept) {
        /*
        * 以下情况不能移动部门：
        * 1、要移动的部门是最顶级部门
        * 2、要移动的部门是未分配部门
        * 3、移动的父部门是要移动的部门的子部门
        * */
        if (dept.getType().equals(WbConstant.DEPARTMENT_TYPE_TOP)) {
            throw new WorkbeiServiceException(
                    ExceptionCode.getMessage(DEPT_TOP_CANNOT_MOVE, "dept: ", dept));
        }
        if (dept.getType().equals(WbConstant.DEPARTMENT_TYPE_UNASSIGNED)) {
            throw new WorkbeiServiceException(
                    ExceptionCode.getMessage(DEPT_UNASSIGNED_CANNOT_MOVE, "dept: ", dept));
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
        for (Long oldDeptId : oldParentIdList) {
            if (!newParentIdList.contains(oldDeptId)) {
                deleteDeptIdSet.add(oldDeptId);
            }
        }
        for (Long newDeptId : newParentIdList) {
            if (!oldParentIdList.contains(newDeptId)) {
                createDeptIdSet.add(newDeptId);
            }
        }
        //  读取当前部门下面的所有用户，根据之前查找到的需要删除的和需要新增的userDeptAscription，做相应操作
        List<Long> ascriptionIdList = wbDepartmentDao.listUserDeptAscriptionUserIdByDepartmentId(deptId);
        for (Long ascriptionUserId : ascriptionIdList) {
            for (Long deleteDeptId : deleteDeptIdSet) {
                wbDepartmentDao.deleteUserDeptAscriptionByDepartmentIdAndUserId(deleteDeptId, ascriptionUserId);
            }
            for (Long createDeptId : createDeptIdSet) {
                WbUserDeptAscriptionDO userDeptAscriptionDO = DepartmentFactory.getUserDeptAscriptionDO();
                userDeptAscriptionDO.setUserId(ascriptionUserId);
                userDeptAscriptionDO.setDepartmentId(createDeptId);
                wbDepartmentDao.saveOrUpdateUserDeptAscription(userDeptAscriptionDO);
            }
        }
    }

    /**
     * 从departmentDO开始，重新生成该部门及其子部门的code和level
     *
     * @param dept
     */
    private void refreshSubDepartmentRecursive(WbDepartmentDO dept, List<Long> excludeIdList) {
        Long deptId = dept.getId();
        //  递归修改
        //  判断是否产生了循环（例如父部门被移动到另子部门下）
        if (excludeIdList.contains(deptId)) {
            throw new WorkbeiServiceException(
                    ExceptionCode.getMessage(DEPT_PARENT_CANNOT_MOVE_TO_CHILD, dept)
            );
        }
        //  先修改本部门
        WbDepartmentDO parentDept = wbDepartmentDao.getDepartmentById(dept.getParentId());
        dept.setLevel(parentDept.getLevel() + 1);
        dept.setCode(parentDept.getCode() + WbConstant.DEPARTMENT_CODE_SEPARATOR + dept.getId());
        wbDepartmentDao.saveOrUpdateDepartment(dept);

        excludeIdList.add(deptId);
        List<WbDepartmentDO> childDeptList = wbDepartmentDao.listDepartmentByParentId(deptId);
        for (WbDepartmentDO childDept : childDeptList) {
            refreshSubDepartmentRecursive(childDept, excludeIdList);
        }
    }

    private List<Long> getParentDepartmentIdList(WbDepartmentDO dept) {
        WbDepartmentDO oldParentDept = wbDepartmentDao.getDepartmentById(dept.getParentId());
        //  获取到dept原来的所有父部门的id，用来做比对更新userDeptAscription，这里的oldParentDept可能为null！！
        List<Long> parentIdList = new ArrayList<>();
        collectParentDepartmentIdRecursive(oldParentDept, parentIdList);
        return parentIdList;
    }

    /**
     * 查找departmentDO所有的父级department的id
     *
     * @param departmentDO
     * @return
     */
    private void collectParentDepartmentIdRecursive(WbDepartmentDO departmentDO, List<Long> parentIdList) {
        parentIdList.add(departmentDO.getId());
        if (departmentDO.getType().equals(WbConstant.DEPARTMENT_TYPE_TOP)) {
            return;
        }
        WbDepartmentDO parentDept = wbDepartmentDao.getDepartmentById(departmentDO.getParentId());
        collectParentDepartmentIdRecursive(parentDept, parentIdList);
    }

    private void deleteDepartmentRecursive(WbDepartmentDO departmentDO) {
        //  如果有子部门，先逐个调用子部门进行删除
        List<WbDepartmentDO> deptList = wbDepartmentDao.listDepartmentByParentId(departmentDO.getId());
        for (WbDepartmentDO dept : deptList) {
            deleteDepartmentRecursive(dept);
        }
        //  最后删除本部门
        deleteSingleDepartment(departmentDO);
    }

    private void deleteSingleDepartment(WbDepartmentDO departmentDO) {
        Long deptId = departmentDO.getId();
        wbDepartmentDao.deleteUserDeptByDepartmentId(deptId);
        wbOuterDataDepartmentDao.deleteOuterDataDepartmentByDepartmentId(deptId);
        //  topDepartment不允许删除
        if (!WbConstant.DEPARTMENT_TYPE_TOP.equals(departmentDO.getType())) {
            wbDepartmentDao.deleteDepartmentById(deptId);
        }
    }

    private WbUserDeptDO saveOrUpdateUserDept(Long deptId, Long userId){
        WbUserDeptDO userDeptDO = DepartmentFactory.getUserDeptDO();
        userDeptDO.setUserId(userId);
        userDeptDO.setDepartmentId(deptId);
        wbDepartmentDao.saveOrUpdateUserDept(userDeptDO);
        return userDeptDO;
    }

    private WbDepartmentDO searchAndSaveDepartment(Long teamId, AutoCreateDepartmentVO departmentVO){
        //  查找父级部门
        if (departmentVO.getTop()) {
            //  说明是根部门，根部门不做处理，直接把topDepartment作为根部门
            WbDepartmentDO topDepartment = wbDepartmentDao.getTopDepartment(teamId);
            return updateDepartmentBaseInfo(topDepartment, departmentVO);
        }
        //  判断要保存的部门是否已存在，如果已存在，那么就走更新流程
        WbDepartmentDO departmentDO = getDepartmentByClientAndOuterId(
                departmentVO.getClient(), departmentVO.getOuterCombineId());
        if (departmentDO != null) {
            return updateDepartmentInfo(departmentVO);
        }

        //  说明是普通部门
        WbDepartmentDO parentDept = wbDepartmentDao.getDepartmentByClientAndOuterId(
                departmentVO.getClient(), departmentVO.getOuterParentCombineId()
        );
        //  如果父级部门没找到，那么就直接挂到topDepartment下
        if (parentDept == null) {
            parentDept = getTeamTopDepartment(teamId);
        }
        WbDepartmentDO dept = DepartmentFactory.getDepartmentDO();
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

        return dept;
    }

    private WbDepartmentDO updateDepartmentBaseInfo(WbDepartmentDO departmentDO, AutoCreateDepartmentVO departmentVO) {
        boolean baseInfoChanged = false;
        if (departmentVO.getName() != null) {
            baseInfoChanged = true;
            departmentDO.setName(departmentVO.getName());
        }
        if (departmentVO.getDisplayOrder() != null) {
            baseInfoChanged = true;
            departmentDO.setDisplayOrder(departmentVO.getDisplayOrder());
        }
        if (baseInfoChanged) {
            wbDepartmentDao.saveOrUpdateDepartment(departmentDO);
        }
        return departmentDO;
    }
}
