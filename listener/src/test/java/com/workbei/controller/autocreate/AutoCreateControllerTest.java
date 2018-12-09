package com.workbei.controller.autocreate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.workbei.WebBaseTest;
import com.workbei.constant.WbConstant;
import com.workbei.http.HttpResultCode;
import com.workbei.model.view.autocreate.AutoCreateDepartmentVO;
import com.workbei.model.view.autocreate.AutoCreateTeamVO;
import com.workbei.model.view.autocreate.AutoCreateUserVO;
import com.workbei.service.autocreate.AutoCreateService;
import com.workbei.util.RegExpUtil;
import com.workbei.util.TestDepartmentFactory;
import com.workbei.util.TestTeamFactory;
import com.workbei.util.TestUserFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional(transactionManager = "transactionManager")
@Rollback
public class AutoCreateControllerTest extends WebBaseTest {
    private static String URL_CREATE_TEAM = "/v3w/tokenAuth/team";
    private static String URL_CREATE_DEPARTMENT = "/v3w/tokenAuth/department";
    private static String URL_UPDATE_DEPARTMENT = "/v3w/tokenAuth/department/{outerId}";
    private static String URL_DELETE_DEPARTMENT = "/v3w/tokenAuth/department/{outerId}";
    private static String URL_CREATE_USER = "/v3w/tokenAuth/user";
    private static String URL_UPDATE_USER = "/v3w/tokenAuth/user/{outerId}";
    private static String URL_UPDATE_USER_SET_ADMIN = "/v3w/tokenAuth/user/{outerId}/admin/{isAdmin}";
    private static String URL_UPDATE_USER_REMOVE_TEAM = "/v3w/tokenAuth/user/{outerId}/team/null";
    private static String HEADER_AUTH_CODE = "abc";

    @Autowired
    private AutoCreateService autoCreateService;

    @Test
    public void createTeamWithError() throws Exception {
        //  默认无权限，返回401
        MvcResult result;
        mockMvc.perform(post(URL_CREATE_TEAM))
                .andExpect(status().isUnauthorized())
                .andReturn();

        JSONObject json = new JSONObject();
        json.put("client", WbConstant.APP_DEFAULT_CLIENT);
        checkSaveValidator(URL_CREATE_TEAM, json.toString(), HttpResultCode.TEAM_OUTER_CORP_ID_NULL);

        json.put("outerCorpId", "dingaaaaaxxxx");
        checkSaveValidator(URL_CREATE_TEAM, json.toString(), HttpResultCode.TEAM_NAME_NULL);
    }

    @Test
    public void createTeam() throws Exception {
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("dingxxxxaaaa" + new Date().getTime());
        String str = JSON.toJSONString(teamVO);

        mockMvc.perform(post(URL_CREATE_TEAM)
                .contentType(MediaType.APPLICATION_JSON)
                .content(str)
                .header("Authorization", HEADER_AUTH_CODE))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();
    }

    @Test
    public void createDepartmentWithError() throws Exception {
        //  默认无权限，返回401
        MvcResult result;
        mockMvc.perform(post(URL_CREATE_DEPARTMENT))
                .andExpect(status().isUnauthorized())
                .andReturn();

        JSONObject json = new JSONObject();
        checkSaveValidator(URL_CREATE_DEPARTMENT, json.toString(), HttpResultCode.DEPT_OUTER_CORP_ID_NULL);
        json.put("outerCorpId", "dingaaaaaxxxx");
        checkSaveValidator(URL_CREATE_DEPARTMENT, json.toString(), HttpResultCode.DEPT_OUTER_ID_NULL);
        json.put("outerCombineId", "ding_outerCombineId");
        checkSaveValidator(URL_CREATE_DEPARTMENT, json.toString(), HttpResultCode.DEPT_OUTER_PARENT_ID_NULL);
        json.put("outerParentCombineId", "ding_outerParentCombineId");
        checkSaveValidator(URL_CREATE_DEPARTMENT, json.toString(), HttpResultCode.DEPT_NAME_NULL);
        json.put("name", "ding_name");
        checkSaveValidator(URL_CREATE_DEPARTMENT, json.toString(), HttpResultCode.DEPT_DISPLAY_ORDER_NULL);
        json.put("displayOrder", "not a number");
        checkSaveValidator(URL_CREATE_DEPARTMENT, json.toString(), HttpResultCode.SYSTEM_ERROR);
    }

    @Test
    public void createDepartment() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        teamVO.setCreator(userVO);
        autoCreateService.createTeam(teamVO);

        AutoCreateDepartmentVO departmentVO = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        String str = JSON.toJSONString(departmentVO);

        mockMvc.perform(post(URL_CREATE_DEPARTMENT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(str)
                .header("Authorization", HEADER_AUTH_CODE))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();
    }

    @Test
    public void updateDepartmentWithError() throws Exception {
        String outerId = "ding_outer_department_id";
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("outerId", outerId);
        String updateDepartmentPath = RegExpUtil.replacePathVariable(URL_UPDATE_DEPARTMENT, pathParams);
        //  默认无权限，返回401
        MvcResult result;
        mockMvc.perform(put(updateDepartmentPath))
                .andExpect(status().isUnauthorized())
                .andReturn();

        JSONObject json = new JSONObject();
        checkUpdateValidator(updateDepartmentPath, json.toString(), HttpResultCode.DEPT_OUTER_CORP_ID_NULL);
        json.put("outerCorpId", "dingaaaaaxxxx");
        checkUpdateValidator(updateDepartmentPath, json.toString(), HttpResultCode.DEPT_UPDATE_NO_FIELD);
    }

    @Test
    public void updateDepartment() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        teamVO.setCreator(userVO);
        autoCreateService.createTeam(teamVO);
        AutoCreateDepartmentVO departmentVO = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        autoCreateService.createDepartment(departmentVO);
        departmentVO.setName("at_new_department_name" + now.getTime());
        departmentVO.setDisplayOrder((double)now.getTime());

        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("outerId", departmentVO.getOuterCombineId());
        String updateDepartmentPath = RegExpUtil.replacePathVariable(URL_UPDATE_DEPARTMENT, pathParams);
        String str = JSON.toJSONString(departmentVO);

        mockMvc.perform(put(updateDepartmentPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(str)
                .header("Authorization", HEADER_AUTH_CODE))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void testDeleteDepartment() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        autoCreateService.createTeam(teamVO);
        AutoCreateDepartmentVO departmentVO = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        autoCreateService.createDepartment(departmentVO);

        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("outerId", departmentVO.getOuterCombineId());
        String deleteDepartmentPath = RegExpUtil.replacePathVariable(URL_DELETE_DEPARTMENT, pathParams);

        mockMvc.perform(delete(deleteDepartmentPath)
                .header("Authorization", HEADER_AUTH_CODE))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void testCreateUserWithError() throws Exception {
        //  默认无权限，返回401
        MvcResult result;
        mockMvc.perform(post(URL_CREATE_USER))
                .andExpect(status().isUnauthorized())
                .andReturn();

        JSONObject json = new JSONObject();
        checkSaveValidator(URL_CREATE_USER, json.toString(), HttpResultCode.USER_OUTER_CORP_ID_NULL);
        json.put("outerCorpId", "dingaaaaaxxxx");
        checkSaveValidator(URL_CREATE_USER, json.toString(), HttpResultCode.USER_OUTER_ID_NULL);
        json.put("outerCombineId", "ding_outerCombineId");
        checkSaveValidator(URL_CREATE_USER, json.toString(), HttpResultCode.USER_OUTER_DEPT_ID_NULL);
        JSONArray deptList = new JSONArray();
        deptList.add("ding_outerCombineDeptIdList");
        json.put("outerCombineDeptIdList", deptList);
        checkSaveValidator(URL_CREATE_USER, json.toString(), HttpResultCode.USER_NAME_NULL);
    }

    @Test
    public void testCreateUser() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        autoCreateService.createTeam(teamVO);

        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterCorpId(teamVO.getOuterCorpId());
        List<String> deptList = new ArrayList<>();
        deptList.add("dept_not_exist");
        userVO.setOuterCombineDeptIdList(deptList);

        String str = JSON.toJSONString(userVO);

        mockMvc.perform(post(URL_CREATE_USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(str)
                .header("Authorization", HEADER_AUTH_CODE))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();
    }

    @Test
    public void testUpdateUserWithError() throws Exception {
        String outerId = "ding_outer_user_id";
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("outerId", outerId);
        String updateUserPath = RegExpUtil.replacePathVariable(URL_UPDATE_USER, pathParams);
        //  默认无权限，返回401
        MvcResult result;
        mockMvc.perform(put(updateUserPath))
                .andExpect(status().isUnauthorized())
                .andReturn();

        JSONObject json = new JSONObject();
        checkUpdateValidator(updateUserPath, json.toString(), HttpResultCode.USER_OUTER_CORP_ID_NULL);
        json.put("outerCorpId", "dingaaaaaxxxx");
        checkUpdateValidator(updateUserPath, json.toString(), HttpResultCode.USER_UPDATE_NO_FIELD);
    }

    @Test
    public void testUpdateUser() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        autoCreateService.createTeam(teamVO);

        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterCorpId(teamVO.getOuterCorpId());
        List<String> deptList = new ArrayList<>();
        deptList.add("dept_not_exist");
        userVO.setOuterCombineDeptIdList(deptList);
        autoCreateService.createUser(userVO);
        userVO.setName("new name" + new Date().getTime());

        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("outerId", userVO.getOuterCombineId());
        String updateUserPath = RegExpUtil.replacePathVariable(URL_UPDATE_USER, pathParams);
        String str = JSON.toJSONString(userVO);

        mockMvc.perform(put(updateUserPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(str)
                .header("Authorization", HEADER_AUTH_CODE))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void testUpdateUserSetAdmin() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        autoCreateService.createTeam(teamVO);

        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterCorpId(teamVO.getOuterCorpId());
        List<String> deptList = new ArrayList<>();
        deptList.add("dept_not_exist");
        userVO.setOuterCombineDeptIdList(deptList);
        autoCreateService.createUser(userVO);

        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("outerId", userVO.getOuterCombineId());
        pathParams.put("isAdmin", true);
        String updateUserSetAdminPath = RegExpUtil.replacePathVariable(URL_UPDATE_USER_SET_ADMIN, pathParams);

        mockMvc.perform(put(updateUserSetAdminPath)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", HEADER_AUTH_CODE))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void testUpdateUserRemoveTeam() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        autoCreateService.createTeam(teamVO);

        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setOuterCorpId(teamVO.getOuterCorpId());
        List<String> deptList = new ArrayList<>();
        deptList.add("dept_not_exist");
        userVO.setOuterCombineDeptIdList(deptList);
        autoCreateService.createUser(userVO);

        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("outerId", userVO.getOuterCombineId());
        String updateUserRemoveTeamPath = RegExpUtil.replacePathVariable(URL_UPDATE_USER_REMOVE_TEAM, pathParams);

        mockMvc.perform(put(updateUserRemoveTeamPath)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", HEADER_AUTH_CODE))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    private void checkSaveValidator(String url, String content, HttpResultCode expectedCode) throws Exception{
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .header("Authorization", HEADER_AUTH_CODE))
//                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errcode").value(expectedCode.getCode()))
                .andExpect(jsonPath("$.errmsg").value(expectedCode.getMsg()))
                .andReturn();
    }

    private void checkUpdateValidator(String url, String content, HttpResultCode expectedCode) throws Exception{
        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .header("Authorization", HEADER_AUTH_CODE))
//                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errcode").value(expectedCode.getCode()))
                .andExpect(jsonPath("$.errmsg").value(expectedCode.getMsg()))
                .andReturn();
    }
}