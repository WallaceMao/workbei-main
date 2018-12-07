package com.workbei.controller.util;

import com.workbei.http.HttpResultCode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Wallace Mao
 * Date: 2018-12-07 0:18
 */
public class ResponseResult {
    public static Map fail(){
        return fail(HttpResultCode.failCode());
    }
    public static Map fail(HttpResultCode code){
        Map<String, String> map = new HashMap<>();
        map.put("errcode", code.getCode());
        map.put("errmsg", code.getMsg());
        return map;
    }
}
