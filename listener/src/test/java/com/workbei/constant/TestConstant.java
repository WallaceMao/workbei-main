package com.workbei.constant;

import com.workbei.controller.autocreate.AutoCreateController;
import com.workbei.model.view.autocreate.AutoCreateDepartmentVO;
import com.workbei.model.view.autocreate.AutoCreateTeamVO;
import com.workbei.model.view.autocreate.AutoCreateUserVO;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Wallace Mao
 * Date: 2018-12-01 17:29
 */
public class TestConstant {
    //  时间相关的最大容忍的差值，用于isCloseTo
    public static Long DEFAULT_DATE_DELTA = 50000L;
    public static final String URL_CREATE_TEAM = getPathCreateTeam();
    public static final String URL_CREATE_DEPARTMENT = getPathCreateDepartment();
    public static final String URL_UPDATE_DEPARTMENT = getPathUpdateDepartment();
    public static final String URL_DELETE_DEPARTMENT = getPathDeleteDepartment();
    public static final String URL_CREATE_USER = getPathCreateUser();
    public static final String URL_UPDATE_USER = getPathUpdateUser();
    public static final String URL_UPDATE_USER_SET_ADMIN = getPathUpdateUserSetAdmin();
    public static final String URL_UPDATE_USER_REMOVE_TEAM = getPathUpdateUserLeaveTeam();
    public static final String URL_UPDATE_TEAM_ALL_ADMIN = getPathUpdateTeamAllAdmin();

    private static String getControllerRootPath() {
        RequestMapping requestMapping = AutoCreateController.class.getAnnotation(RequestMapping.class);
        if (requestMapping != null && requestMapping.value().length > 0) {
            return requestMapping.value()[0];
        } else {
            return "";
        }
    }

    private static String getPathCreateTeam() {
        Method method;
        try {
            method = AutoCreateController.class.getMethod("createTeam", AutoCreateTeamVO.class, BindingResult.class);
        } catch (NoSuchMethodException e) {
            return "";
        }
        PostMapping mapping = method.getAnnotation(PostMapping.class);
        return getControllerRootPath() + mapping.value()[0];
    }

    private static String getPathCreateDepartment() {
        Method method;
        try {
            method = AutoCreateController.class.getMethod("createDepartment", AutoCreateDepartmentVO.class, BindingResult.class);
        } catch (NoSuchMethodException e) {
            return "";
        }
        PostMapping mapping = method.getAnnotation(PostMapping.class);
        return getControllerRootPath() + mapping.value()[0];
    }

    private static String getPathUpdateDepartment() {
        Method method;
        try {
            method = AutoCreateController.class.getMethod("updateDepartment", String.class, AutoCreateDepartmentVO.class, BindingResult.class);
        } catch (NoSuchMethodException e) {
            return "";
        }
        PutMapping mapping = method.getAnnotation(PutMapping.class);
        return getControllerRootPath() + mapping.value()[0];
    }

    private static String getPathDeleteDepartment() {
        Method method;
        try {
            method = AutoCreateController.class.getMethod("deleteDepartment", String.class);
        } catch (NoSuchMethodException e) {
            return "";
        }
        DeleteMapping mapping = method.getAnnotation(DeleteMapping.class);
        return getControllerRootPath() + mapping.value()[0];
    }

    private static String getPathCreateUser() {
        Method method;
        try {
            method = AutoCreateController.class.getMethod("createUser", AutoCreateUserVO.class, BindingResult.class);
        } catch (NoSuchMethodException e) {
            return "";
        }
        PostMapping mapping = method.getAnnotation(PostMapping.class);
        return getControllerRootPath() + mapping.value()[0];
    }

    private static String getPathUpdateUser() {
        Method method;
        try {
            method = AutoCreateController.class.getMethod("updateUser", String.class, AutoCreateUserVO.class, BindingResult.class);
        } catch (NoSuchMethodException e) {
            return "";
        }
        PutMapping mapping = method.getAnnotation(PutMapping.class);
        return getControllerRootPath() + mapping.value()[0];
    }

    private static String getPathUpdateUserSetAdmin() {
        Method method;
        try {
            method = AutoCreateController.class.getMethod("updateUserSetAdmin", String.class, Boolean.class);
        } catch (NoSuchMethodException e) {
            return "";
        }
        PutMapping mapping = method.getAnnotation(PutMapping.class);
        return getControllerRootPath() + mapping.value()[0];
    }

    private static String getPathUpdateTeamAllAdmin() {
        Method method;
        try {
            method = AutoCreateController.class.getMethod("updateTeamAllAdmin", String.class, String.class, List.class);
        } catch (NoSuchMethodException e) {
            return "";
        }
        PutMapping mapping = method.getAnnotation(PutMapping.class);
        return getControllerRootPath() + mapping.value()[0];
    }

    private static String getPathUpdateUserLeaveTeam() {
        Method method;
        try {
            method = AutoCreateController.class.getMethod("updateUserLeaveTeam", String.class);
        } catch (NoSuchMethodException e) {
            return "";
        }
        PutMapping mapping = method.getAnnotation(PutMapping.class);
        return getControllerRootPath() + mapping.value()[0];
    }
}
