package com.workbei.dao.user;

import com.workbei.model.domain.user.WbOuterDataTeamDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 16:15
 */
@Repository("wbOuterDataTeamDao")
public interface WbOuterDataTeamDao {
    void saveOrUpdateOuterDataTeam(WbOuterDataTeamDO outerDataTeamDO);
    void deleteOuterDataTeamByOuterId(
            @Param("outerId") String outerId
    );
    WbOuterDataTeamDO getOuterDataTeamByClientAndOuterId(
            @Param("client") String client,
            @Param("outerId") String outerId
    );
}
