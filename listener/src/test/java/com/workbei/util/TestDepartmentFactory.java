package com.workbei.util;

import com.workbei.constant.WbConstant;
import com.workbei.model.domain.user.WbDepartmentDO;
import com.workbei.model.domain.user.WbOuterDataDepartmentDO;
import com.workbei.model.view.autocreate.AutoCreateDepartmentVO;
import com.workbei.factory.DepartmentFactory;

import java.util.Date;

/**
 * @author Wallace Mao
 * Date: 2018-12-02 1:45
 */
public class TestDepartmentFactory {
    private static final Long MILLS_DELAY = 1L;

    public static AutoCreateDepartmentVO getAutoCreateDepartmentVO(String corpOuterId) throws InterruptedException {
        Thread.sleep(MILLS_DELAY);

        Date now = new Date();
        AutoCreateDepartmentVO departmentVO = new AutoCreateDepartmentVO();
        departmentVO.setName("at_dept_name" + now.getTime());
        departmentVO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        departmentVO.setDisplayOrder((double) now.getTime());
        departmentVO.setOuterCorpId(corpOuterId);
        departmentVO.setOuterCombineId("at_dept_outer_comid_" + now.getTime());
        departmentVO.setOuterParentCombineId("at_dept_outer_pcomid" + now.getTime());
        return departmentVO;
    }
    public static WbDepartmentDO getDepartmentDO(Long teamId, Long parentId, Integer level) throws InterruptedException {
        Thread.sleep(MILLS_DELAY);

        Date now = new Date();
        WbDepartmentDO departmentDO = DepartmentFactory.getDepartmentDO();
        departmentDO.setTeamId(teamId);
        departmentDO.setParentId(parentId);
        departmentDO.setCode("");
        departmentDO.setLevel(level);
        departmentDO.setName("at_dept_name" + now.getTime());
        return departmentDO;
    }

    public static WbOuterDataDepartmentDO getOuterDataDepartment(Long departmentId) throws InterruptedException {
        Thread.sleep(MILLS_DELAY);

        Date now = new Date();
        WbOuterDataDepartmentDO outerDataDepartmentDO = DepartmentFactory.getOuterDataDepartmentDO();
        outerDataDepartmentDO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        outerDataDepartmentDO.setDepartmentId(departmentId);
        outerDataDepartmentDO.setOuterId("at_department_outer_id_" + now.getTime());
        return outerDataDepartmentDO;
    }
}
