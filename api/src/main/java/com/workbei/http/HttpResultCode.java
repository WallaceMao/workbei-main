package com.workbei.http;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Wallace Mao
 * Date: 2018-12-06 22:05
 */
public enum HttpResultCode {
    OK("ok", "ok"),
    SYSTEM_ERROR("error", "system error"),
    AUTO_CREATE_TEAM_NAME_NULL("team.name.null", "team name can not be null");

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
