package com.workbei.dao.user;

import com.workbei.model.domain.user.WbOuterDataTeamDO;
import com.workbei.model.domain.user.WbOuterDataUserDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 16:15
 */
@Repository("wbOuterDataUserDao")
public interface WbOuterDataUserDao {
    void saveOrUpdateOuterDataUser(WbOuterDataUserDO outerDataTeamDO);
    void saveOrUpdateOuterDataTeam(WbOuterDataTeamDO outerDataTeamDO);
    void deleteOuterDataUserByOuterId(
            @Param("outerId") String outerId
    );
    WbOuterDataUserDO getOuterDataUserByClientAndOuterId(
            @Param("client") String client,
            @Param("outerId") String outerId
    );
}
