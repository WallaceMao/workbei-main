package factory;

import com.workbei.model.domain.user.WbUserRoleGroupDO;

/**
 * @author Wallace Mao
 * Date: 2018-12-02 23:22
 */
public class RoleFactory {
    public static WbUserRoleGroupDO getUserRoleGroupDO(){
        return new WbUserRoleGroupDO();
    }
}
