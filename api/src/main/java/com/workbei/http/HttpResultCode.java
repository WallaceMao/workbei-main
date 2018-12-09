package com.workbei.http;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Wallace Mao
 * Date: 2018-12-06 22:05
 */
public enum HttpResultCode {
    OK("0", "ok"),
    SYSTEM_ERROR("-1", "system error"),
    HTTP_FORBIDDEN("403", "no auth"),
    TEAM_NAME_NULL("1001", "team name can not be null"),
    TEAM_OUTER_CORP_ID_NULL("1002", "outerCorpId can not be null"),
    TEAM_CLIENT_NULL("1003", "client can not be null"),
    DEPT_OUTER_CORP_ID_NULL("1101", "department outer corp id can not be null"),
    DEPT_OUTER_ID_NULL("1102", "department outer corp id can not be null"),
    DEPT_OUTER_PARENT_ID_NULL("1103", "department outer corp id can not be null"),
    DEPT_NAME_NULL("1104", "department outer corp id can not be null"),
    DEPT_DISPLAY_ORDER_NULL("1105", "department outer corp id can not be null"),
    DEPT_UPDATE_NO_FIELD("1106", "no department field"),
    USER_OUTER_CORP_ID_NULL("1201", "no user outer corp id"),
    USER_OUTER_ID_NULL("1202", "no user outer id"),
    USER_OUTER_DEPT_ID_NULL("1203", "no user outer dept id"),
    USER_NAME_NULL("1204", "no user name"),
    USER_UPDATE_NO_FIELD("1205", "no user field");

    private String code;
    private String msg;

    private static final Map<String, HttpResultCode> lookup = new HashMap<>();

    static {
        for (HttpResultCode d : HttpResultCode.values()) {
            lookup.put(d.getCode(), d);
        }
    }

    public static HttpResultCode successCode(){
        return HttpResultCode.OK;
    }

    public static HttpResultCode failCode(){
        return HttpResultCode.SYSTEM_ERROR;
    }

    public static HttpResultCode getByCode(String code){
        return lookup.get(code);
    }

    HttpResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
