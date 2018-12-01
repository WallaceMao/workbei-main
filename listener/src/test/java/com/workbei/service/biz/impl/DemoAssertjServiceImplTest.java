package com.workbei.service.biz.impl;

import com.workbei.BaseUnitTest;
import com.workbei.constant.WbConstant;
import com.workbei.model.domain.user.WbOuterDataTeamDO;
import com.workbei.service.biz.DemoService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

/**
 * @author Wallace Mao
 * Date: 2018-11-30 19:12
 */
@Transactional(transactionManager = "transactionManager")
@Rollback
public class DemoAssertjServiceImplTest extends BaseUnitTest {
    @Autowired
    private DemoService demoService;
    @Autowired
    private DataSource dataSource;

    @Test
    public void testAssertjSaveOuterDataTeam(){
        Long teamId = 99999L;
        String outerId = "aaabbbccc";
        demoService.saveOuterDataTeam(outerId, teamId);
//        Table table = new Table(dataSource, "outer_data_team");
//        assertThat(table).column("outer_id")
//                .value().isEqualTo(outerId);

        WbOuterDataTeamDO outerDataTeamDO = demoService.getOuterDataTeamByOuterId(outerId);
        assertThat(outerDataTeamDO.getTeamId()).as("check teamId: %s", teamId).isEqualTo(teamId);
        List<WbOuterDataTeamDO> teamList = new ArrayList<>();
        teamList.add(outerDataTeamDO);
        assertThat(teamList).extracting("outerId").contains(outerId);
        assertThat(teamList).extracting("outerId", "teamId", "client")
                .contains(tuple(outerId, teamId, WbConstant.APP_DEFAULT_CLIENT));
//        assertEquals(teamId, outerDataTeamDO.getTeamId());
//        assertEquals(outerId, outerDataTeamDO.getOuterId());
//        assertEquals(WbConstant.APP_DEFAULT_CLIENT, outerDataTeamDO.getClient());
    }
}
