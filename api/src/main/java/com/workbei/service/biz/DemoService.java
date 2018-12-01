package com.workbei.service.biz;

import com.workbei.model.domain.user.WbOuterDataTeamDO;
import com.workbei.model.domain.user.WbTeamDO;

/**
 * @author Wallace Mao
 * Date: 2018-11-26 15:50
 */
public interface DemoService {
    String showHello(String name);

    void saveOuterDataTeam(String outerId, Long teamId);

    WbOuterDataTeamDO getOuterDataTeamByOuterId(String outerId);
}
