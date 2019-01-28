package com.workbei.constant;

/**
 * @author Wallace Mao
 * Date: 2018-12-01 17:29
 */
public class TestConstant {
    //  时间相关的最大容忍的差值，用于isCloseTo
    public static Long DEFAULT_DATE_DELTA = 50000L;
    public static String URL_CREATE_TEAM = "/v3w/tokenAuth/team";
    public static String URL_CREATE_DEPARTMENT = "/v3w/tokenAuth/department";
    public static String URL_UPDATE_DEPARTMENT = "/v3w/tokenAuth/department/{outerId}";
    public static String URL_DELETE_DEPARTMENT = "/v3w/tokenAuth/department/{outerId}";
    public static String URL_CREATE_USER = "/v3w/tokenAuth/user";
    public static String URL_UPDATE_USER = "/v3w/tokenAuth/user/{outerId}";
    public static String URL_UPDATE_USER_SET_ADMIN = "/v3w/tokenAuth/user/{outerId}/admin/{admin}";
    public static String URL_UPDATE_USER_REMOVE_TEAM = "/v3w/tokenAuth/user/{outerId}/team/null";
}
