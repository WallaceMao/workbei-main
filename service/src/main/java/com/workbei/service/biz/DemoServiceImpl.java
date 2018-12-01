package com.workbei.service.biz;

import com.workbei.constant.WbConstant;
import com.workbei.dao.user.WbOuterDataTeamDao;
import com.workbei.model.domain.user.WbOuterDataTeamDO;
import com.workbei.model.domain.user.WbTeamDO;
import com.workbei.service.base.TeamManageService;
import factory.TeamFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Wallace Mao
 * Date: 2018-11-26 15:50
 */
public class DemoServiceImpl implements DemoService {
    @Autowired
    private WbOuterDataTeamDao wbOuterDataTeamDao;

    @Override
    public String showHello(String name){
        return "hello, " + name;
    }

    @Override
    public void saveOuterDataTeam(String outerId, Long teamId){
        WbOuterDataTeamDO outerDataTeamDO = TeamFactory.getOuterDataTeam();
        outerDataTeamDO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        outerDataTeamDO.setOuterId(outerId);
        outerDataTeamDO.setTeamId(teamId);
        wbOuterDataTeamDao.saveOrUpdateOuterDataTeam(outerDataTeamDO);
    }

    @Override
    public WbOuterDataTeamDO getOuterDataTeamByOuterId(String outerId){
        return wbOuterDataTeamDao.getOuterDataTeamByClientAndOuterId(
                WbConstant.APP_DEFAULT_CLIENT, outerId
        );
    }
}
