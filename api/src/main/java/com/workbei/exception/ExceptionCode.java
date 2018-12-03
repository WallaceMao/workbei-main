package com.workbei.exception;

/**
 * @author Wallace Mao
 * Date: 2018-12-03 22:59
 */
public enum ExceptionCode {
    //  2xxxx department
    DEPT_NOT_FOUND("20000", "department not found"),
    DEPT_TOP_CANNOT_MOVE("20001", "can not move top department"),
    DEPT_UNASSIGNED_CANNOT_MOVE("20002", "can not move unassigned department"),
    DEPT_PARENT_CANNOT_MOVE_TO_CHILD("20003", "can not move department to its child");

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
