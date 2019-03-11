package com.workbei.exception;

/**
 * @author Wallace Mao
 * Date: 2018-12-03 22:59
 */
public enum ExceptionCode {
    APP_SYSTEM_ERROR("-1", "system error"),
    APP_CLIENT_NOT_FOUND("10000", "app client not found"),
    APP_REQUEST_PARAM_ERROR("10001", "request params not found"),
    TEAM_NOT_FOUND("11000", "team not found"),
    //  12xxx department
    DEPT_NOT_FOUND("12000", "department not found"),
    DEPT_TOP_CANNOT_MOVE("12001", "can not move top department"),
    DEPT_UNASSIGNED_CANNOT_MOVE("12002", "can not move unassigned department"),
    DEPT_PARENT_CANNOT_MOVE_TO_CHILD("12003", "can not move department to its child"),
    //  13xxx user
    USER_NOT_FOUND("13000", "user not found"),
    USER_OUTER_DATA_NOT_FOUND("13001", "user not found"),
    CLIENT_NOT_FOUND("13002", "client not found"),
    //  14xxx account
    ACCOUNT_NOT_FOUND("14000", "account not found");

    private String code;
    private String msg;

    ExceptionCode(String code, String msg) {
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

    public static String getMessage(ExceptionCode exceptionCode, Object... argv){
        StringBuilder strArgv = new StringBuilder(exceptionCode.getCode());
        strArgv.append(":").append(exceptionCode.getMsg());
        if(argv != null && argv.length > 0){
            strArgv.append(": ");
            for(Object a : argv){
                strArgv.append(a.toString());
            }
        }
        return strArgv.toString();
    }
}
