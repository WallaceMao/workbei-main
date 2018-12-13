package com.workbei.util;

/**
 * @author Wallace Mao
 * Date: 2018-12-13 23:46
 */
public class StringUtil {
    public static Boolean isEmpty(Object str){
        return str == null || "".equals(str);
    }
}
