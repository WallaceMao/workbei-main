package com.workbei.constant;

import com.workbei.controller.autocreate.AutoCreateV2Controller;
import com.workbei.model.view.autocreate.AutoCreateDepartmentVO;
import com.workbei.model.view.autocreate.AutoCreateTeamVO;
import com.workbei.model.view.autocreate.AutoCreateUserVO;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Wallace Mao
 * Date: 2018-12-01 17:29
 */
public class TestV2Constant {
    //  时间相关的最大容忍的差值，用于isCloseTo
    public static final Long DEFAULT_DATE_DELTA = 50000L;
    public static final String CLIENT_FAKED = "dingtalk";
    public static final String URL_CREATE_TEAM_V2 = getPathCreateTeam(0);
    public static final String URL_CREATE_DEPARTMENT_V2 = getPathCreateDepartment(0);
    public static final String URL_UPDATE_DEPARTMENT_V2 = getPathUpdateDepartment(0);
    public static final String URL_DELETE_DEPARTMENT_V2 = getPathDeleteDepartment(0);
    public static final String URL_CREATE_USER_V2 = getPathCreateUser(0);
    public static final String URL_UPDATE_USER_V2 = getPathUpdateUser(0);
    public static final String URL_UPDATE_USER_SET_ADMIN_V2 = getPathUpdateUserSetAdmin(0);
    public static final String URL_UPDATE_USER_REMOVE_TEAM_V2 = getPathUpdateUserLeaveTeam(0);
    public static final String URL_UPDATE_TEAM_ALL_ADMIN_V2 = getPathUpdateTeamAllAdmin(0);

    private static String getControllerRootPath() {
        RequestMapping requestMapping = AutoCreateV2Controller.class.getAnnotation(RequestMapping.class);
        if (requestMapping != null && requestMapping.value().length > 0) {
            return requestMapping.value()[0];
        } else {
            return "";
        }
    }

    private static String getPathCreateTeam(int mapperIndex) {
        Method method;
        try {
            method = AutoCreateV2Controller.class.getMethod(
                    "createTeam",
                    String.class,
                    AutoCreateTeamVO.class,
                    HttpServletRequest.class,
                    HttpServletResponse.class,
                    BindingResult.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return "";
        }
        PostMapping mapping = method.getAnnotation(PostMapping.class);
        return getControllerRootPath() + mapping.value()[mapperIndex];
    }

    private static String getPathCreateDepartment(int mapperIndex) {
        Method method;
        try {
            method = AutoCreateV2Controller.class.getMethod(
                    "createDepartment",
                    String.class,
                    AutoCreateDepartmentVO.class,
                    HttpServletRequest.class,
                    HttpServletResponse.class,
                    BindingResult.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return "";
        }
        PostMapping mapping = method.getAnnotation(PostMapping.class);
        return getControllerRootPath() + mapping.value()[mapperIndex];
    }

    private static String getPathUpdateDepartment(int mapperIndex) {
        Method method;
        try {
            method = AutoCreateV2Controller.class.getMethod(
                    "updateDepartment",
                    String.class,
                    String.class,
                    AutoCreateDepartmentVO.class,
                    HttpServletRequest.class,
                    HttpServletResponse.class,
                    BindingResult.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return "";
        }
        PutMapping mapping = method.getAnnotation(PutMapping.class);
        return getControllerRootPath() + mapping.value()[mapperIndex];
    }

    private static String getPathDeleteDepartment(int mapperIndex) {
        Method method;
        try {
            method = AutoCreateV2Controller.class.getMethod(
                    "deleteDepartment",
                    String.class,
                    String.class,
                    HttpServletRequest.class,
                    HttpServletResponse.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return "";
        }
        DeleteMapping mapping = method.getAnnotation(DeleteMapping.class);
        return getControllerRootPath() + mapping.value()[mapperIndex];
    }

    private static String getPathCreateUser(int mapperIndex) {
        Method method;
        try {
            method = AutoCreateV2Controller.class.getMethod(
                    "createUser",
                    String.class,
                    AutoCreateUserVO.class,
                    HttpServletRequest.class,
                    HttpServletResponse.class,
                    BindingResult.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return "";
        }
        PostMapping mapping = method.getAnnotation(PostMapping.class);
        return getControllerRootPath() + mapping.value()[mapperIndex];
    }

    private static String getPathUpdateUser(int mapperIndex) {
        Method method;
        try {
            method = AutoCreateV2Controller.class.getMethod(
                    "updateUser",
                    String.class,
                    String.class,
                    AutoCreateUserVO.class,
                    HttpServletRequest.class,
                    HttpServletResponse.class,
                    BindingResult.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return "";
        }
        PutMapping mapping = method.getAnnotation(PutMapping.class);
        return getControllerRootPath() + mapping.value()[mapperIndex];
    }

    private static String getPathUpdateUserSetAdmin(int mapperIndex) {
        Method method;
        try {
            method = AutoCreateV2Controller.class.getMethod(
                    "updateUserSetAdmin",
                    String.class,
                    String.class,
                    Boolean.class,
                    HttpServletRequest.class,
                    HttpServletResponse.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return "";
        }
        PutMapping mapping = method.getAnnotation(PutMapping.class);
        return getControllerRootPath() + mapping.value()[mapperIndex];
    }

    private static String getPathUpdateTeamAllAdmin(int mapperIndex) {
        Method method;
        try {
            method = AutoCreateV2Controller.class.getMethod(
                    "updateTeamAllAdmin",
                    String.class,
                    String.class,
                    List.class,
                    HttpServletRequest.class,
                    HttpServletResponse.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return "";
        }
        PutMapping mapping = method.getAnnotation(PutMapping.class);
        return getControllerRootPath() + mapping.value()[mapperIndex];
    }

    private static String getPathUpdateUserLeaveTeam(int mapperIndex) {
        Method method;
        try {
            method = AutoCreateV2Controller.class.getMethod(
                    "updateUserLeaveTeam",
                    String.class,
                    String.class,
                    HttpServletRequest.class,
                    HttpServletResponse.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return "";
        }
        PutMapping mapping = method.getAnnotation(PutMapping.class);
        return getControllerRootPath() + mapping.value()[mapperIndex];
    }
}
