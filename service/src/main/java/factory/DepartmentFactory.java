package factory;

import com.workbei.constant.WbConstant;
import com.workbei.model.domain.user.WbDepartmentDO;
import com.workbei.model.domain.user.WbOuterDataDepartmentDO;
import com.workbei.model.domain.user.WbUserDeptAscriptionDO;
import com.workbei.model.domain.user.WbUserDeptDO;

/**
 * @author Wallace Mao
 * Date: 2018-11-28 10:26
 */
public class DepartmentFactory {
    public static WbDepartmentDO getDepartmentDO(){
        WbDepartmentDO departmentDO = new WbDepartmentDO();
        departmentDO.setType(WbConstant.DEPARTMENT_TYPE_COMMON);
        departmentDO.setDisplayOrder(WbConstant.DEPARTMENT_DEFAULT_DISPLAY_ORDER);
        return departmentDO;
    }

    public static WbUserDeptDO getUserDeptDO(){
        return new WbUserDeptDO();
    }

    public static WbUserDeptAscriptionDO getUserDeptAscriptionDO(){
        return new WbUserDeptAscriptionDO();
    }

    public static WbOuterDataDepartmentDO getOuterDataDepartmentDO(){
        return new WbOuterDataDepartmentDO();
    }
}
