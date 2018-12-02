package com.workbei.util;

import java.util.UUID;

/**
 * 生成uuid的工具类，目前的uuid为36位，其中：
 * 前四位：根据具体的实体的不同而做区别
 * 后32位：由java的uuid方法生成的UUID去掉“-”而成
 * @author Wallace Mao
 * Date: 2018-12-02 16:09
 */
public class UuidUtil {
    private static final String UUID_PREFIX_TEAM = "team";
    private static final String UUID_PREFIX_DEPARTMENT = "dept";
    private static final String UUID_PREFIX_ACCOUNT = "acct";
    private static final String UUID_PREFIX_USER = "user";

    private static String generateUuid(String prefix){
        return prefix + UUID.randomUUID().toString().replace("-", "");
    }
    public static String generateTeamUuid(){
        return generateUuid(UUID_PREFIX_TEAM);
    }
    public static String generateDepartmentUuid(){
        return generateUuid(UUID_PREFIX_DEPARTMENT);
    }
    public static String generateAccountUuid(){
        return generateUuid(UUID_PREFIX_ACCOUNT);
    }
    public static String generateUserUuid(){
        return generateUuid(UUID_PREFIX_USER);
    }
    public static void main(String[] args) {
        System.out.println("====" + generateTeamUuid());
    }
}
