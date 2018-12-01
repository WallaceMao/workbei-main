package com.workbei.dao.user;

import com.workbei.model.domain.user.WbOuterDataDepartmentDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 16:15
 */
@Repository("wbOuterDataDepartmentDao")
public interface WbOuterDataDepartmentDao {
    void saveOrUpdateOuterDataDepartment(WbOuterDataDepartmentDO outerDataTeamDO);
    void deleteOuterDataDepartmentByOuterId(
            @Param("outerId") String outerId
    );
    WbOuterDataDepartmentDO getOuterDataDepartmentByClientAndOuterId(
            @Param("client") String client,
            @Param("outerId") String outerId
    );
    Long getOuterDataDepartmentDepartmentIdByClientAndOuterId(
            @Param("client") String client,
            @Param("outerId") String outerId
    );
}
