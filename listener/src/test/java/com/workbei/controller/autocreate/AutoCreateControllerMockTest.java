package com.workbei.controller.autocreate;

import com.alibaba.fastjson.JSON;
import com.workbei.WebBaseTest;
import com.workbei.model.view.autocreate.AutoCreateTeamVO;
import com.workbei.service.autocreate.AutoCreateService;
import com.workbei.util.TestTeamFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;

import java.util.Date;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AutoCreateControllerMockTest extends WebBaseTest {
    private static String URL_CREATE_TEAM = "/v3w/autoCreate/team";
    private static String HEADER_AUTH_CODE = "abc";

    @Mock
    private AutoCreateService autoCreateService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createTeam() throws Exception {
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("dingxxxxaaaa" + new Date().getTime());
        String str = JSON.toJSONString(teamVO);

        doNothing().when(autoCreateService).createTeam(teamVO);

        mockMvc.perform(post(URL_CREATE_TEAM)
                .contentType(MediaType.APPLICATION_JSON)
                .content(str)
                .header("Authorization", HEADER_AUTH_CODE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        verify(autoCreateService, times(1)).createTeam(teamVO);
        verifyNoMoreInteractions(autoCreateService);
    }
}