package com.workbei.constant;

/**
 * @author Wallace Mao
 * Date: 2018-11-12 0:34
 */
public class WbConstant {
    //  app层级
    public static final String APP_DEFAULT_CLIENT = "dingtalk";
    public static final String APP_DEFAULT_MODE = "dingtalk";
    public static final String APP_ROLE_GROUP_SUPERUSER = "系统管理员";
    public static final String APP_ROLE_GROUP_ADMIN = "管理员";
    public static final String APP_ROLE_GROUP_SUPER_ADMIN = "超级管理员";
    public static final String APP_ROLE_GROUP_USER = "普通用户";
    public static final String APP_DEFAULT_TEAM_CONTACT_NAME = "空";
    public static final Boolean APP_DEFAULT_TEAM_CAN_TEL = true;
    public static final String APP_DEFAULT_TEAM_INDUSTRY = "1";

    public static final String SUMMARY_DEFAULT_COVER = "cover/default/corpus_v1/card-default-";
    //文集归属个人选项
    public static final String SUMMARY_ATTRIBUTE_PERSON = "person";
    //文集归属企业选项
    public static final String SUMMARY_ATTRIBUTE_COMPANY = "company";
    public static final String SUMMARY_ESSAY_NOTE_DATE = "none";
    public static final String SUMMARY_ESSAY_TYPE = "essays";
    public static final String SUMMARY_DEFAULT_ROLE = "common";

    public static final String KANBAN_DEFAULT_COVER = "https://images.timetask.cn/cover/default/kanban_v1/card-default-3.png";
    public static final String KANBAN_AUTHORITY_USER_ROLE_KANBAN_VISITOR = "visitor";
    public static final String KANBAN_AUTHORITY_USER_ROLE_KANBAN_COMMON = "common";
    public static final String KANBAN_AKANBAN_UTHORITY_USER_ROLE_KANBAN_ADMIN = "admin";
    public static final String KANBAN_AUTHORITY_USER_ROLE_KANBAN_CREATOR = "creator";
    public static final String KANBAN_ATTRIBUTE_COMPANY = "company";
    public static final String KANBAN_ATTRIBUTE_PERSON = "person";
    public static final String KANBAN_SYNC_TASK_TO_TODO = "todo";
    public static final String KANBAN_CHILD_MODE_FREE = "free";
    public static final String KANBAN_DEFAULT_ROLE = "common";

    public static final String USER_DEFAULT_USER_AVATAR = "avatar/default.png";
    public static final String USER_THEME_SKIN_DEFAULT = "skin_0";
    public static final String DEPARTMENT_TYPE_TOP = "top";
    public static final String DEPARTMENT_TYPE_COMMON = "common";
    public static final String DEPARTMENT_TYPE_UNASSIGNED = "unassigned";
    public static final String DEPARTMENT_NAME_UNASSIGNED = "未分配部门";
    public static final String DEPARTMENT_CODE_SEPARATOR = "-";
    public static final Double DEPARTMENT_DEFAULT_DISPLAY_ORDER = 65535.0;
    public static final String TEAM_DEFAULT_LOGO = "default/company.png";
    public static final String TEAM_LOGO_ROOT_PATH = "https://images.timetask.cn/company/";
    public static final String TEAM_COMPANY_VIEW_INDEX = "index";
    public static final Integer TEAM_READ_COLLAGE_COUNT = 30;
    //谁可以邀请成员-所有人
    public static final String TEAM_WHO_CAN_INVITE_ALL = "all";
    //谁可以邀请成员-管理员
    public static final String TEAM_WHO_CAN_INVITE_ADMIN = "admin";
    //谁可以邀请成员-创建者
    public static final String TEAM_WHO_CAN_INVITE_CREATOR = "creator";
    //超级管理员
    public static final String TEAM_USER_ROLE_CREATOR = "creator";
    //管理员
    public static final String TEAM_USER_ROLE_ADMIN = "admin";
    //超级管理员
    public static final String TEAM_USER_ROLE_SUPER_ADMIN = "superAdmin";
    //普通成员
    public static final String TEAM_USER_ROLE_COMMON = "common";
    // 加入公司记录的类型
    public static final String TEAM_RECORD_TYPE_JOIN = "join";
    // 退出公司记录的类型
    public static final String TEAM_RECORD_TYPE_QUIT = "quit";
    // 创建公司记录的类型
    public static final String TEAM_RECORD_TYPE_CREATE = "create";
    // 移出公司记录的类型
    public static final String TEAM_RECORD_TYPE_REMOVE = "remove";
}
