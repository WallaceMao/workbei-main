package com.workbei.event;

import java.io.Serializable;

/**
 * @author Wallace Mao
 * Date: 2018-11-21 15:41
 */
public class SolutionEvent implements Serializable {
    //  日事清后台的teamId
    private String teamId;
    //  日事清后台的用户id
    private String userId;
    //  类型team或者staff
    private String type;

    public SolutionEvent(String type, String teamId, String userId) {
        this.type = type;
        this.teamId = teamId;
        this.userId = userId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String generateMessageKey(){
        StringBuilder sb = new StringBuilder(this.type);
        sb.append("_").append(this.teamId);
        if(this.userId != null){
            sb.append("_").append(this.userId);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "SolutionEvent{" +
                "teamId='" + teamId + '\'' +
                ", userId='" + userId + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
