package com.workbei.service.biz.impl;

import com.workbei.BaseUnitTest;
import com.workbei.dao.user.WbOuterDataTeamDao;
import com.workbei.model.domain.user.WbOuterDataTeamDO;
import com.workbei.service.biz.DemoService;
import com.workbei.factory.TeamFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Wallace Mao
 * Date: 2018-11-30 19:12
 */
@Transactional(transactionManager = "transactionManager")
@Rollback
@Ignore
public class DemoMockitoServiceImplTest extends BaseUnitTest {
    @InjectMocks
    @Autowired
    private DemoService demoService;

    @Mock
    private WbOuterDataTeamDao wbOuterDataTeamDao;

    @Before
    public void before(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMockitoSaveOuterDataTeam(){
        WbOuterDataTeamDO outerDataTeamDO = TeamFactory.getOuterDataTeamDO();
        outerDataTeamDO.setClient("dingtalk");
        Mockito.when(wbOuterDataTeamDao.getOuterDataTeamByClientAndOuterId("dingtalk", "world"))
                .thenReturn(outerDataTeamDO);
        //  这里的result实际返回的是outerDataTeam
        WbOuterDataTeamDO result = demoService.getOuterDataTeamByOuterId("world");
        Assert.assertEquals(outerDataTeamDO.getClient(), result.getClient());
        Assert.assertEquals(outerDataTeamDO, result);
    }
}
