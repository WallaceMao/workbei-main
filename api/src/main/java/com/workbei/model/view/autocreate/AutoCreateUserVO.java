package com.workbei.model.view.autocreate;

import java.util.List;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 15:20
 */
public class AutoCreateUserVO {
    private String name;
    private String avatar;
    private String client;
    private String outerCorpId;
    private String outerUserId;
    private String outerCombineId;
    private List<String> outerCombineDeptIdList;
    private String outerUnionId;
    private Boolean isAdmin;

    @Deprecated
    private String username;
    @Deprecated
    private String password;
    @Deprecated
    private Long teamId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getOuterCorpId() {
        return outerCorpId;
    }

    public void setOuterCorpId(String outerCorpId) {
        this.outerCorpId = outerCorpId;
    }

    public String getOuterUserId() {
        return outerUserId;
    }

    public void setOuterUserId(String outerUserId) {
        this.outerUserId = outerUserId;
    }

    public String getOuterCombineId() {
        return outerCombineId;
    }

    public void setOuterCombineId(String outerCombineId) {
        this.outerCombineId = outerCombineId;
    }

    public List<String> getOuterCombineDeptIdList() {
        return outerCombineDeptIdList;
    }

    public void setOuterCombineDeptIdList(List<String> outerCombineDeptIdList) {
        this.outerCombineDeptIdList = outerCombineDeptIdList;
    }

    public String getOuterUnionId() {
        return outerUnionId;
    }

    public void setOuterUnionId(String outerUnionId) {
        this.outerUnionId = outerUnionId;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return "AutoCreateUserVO{" +
                "name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", client='" + client   + '\'' +
                ", outerCorpId='" + outerCorpId + '\'' +
                ", outerUserId='" + outerUserId + '\'' +
                ", outerCombineId='" + outerCombineId + '\'' +
                ", outerCombineDeptIdList=" + outerCombineDeptIdList +
                ", outerUnionId='" + outerUnionId + '\'' +
                ", isAdmin=" + isAdmin +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", teamId=" + teamId +
                '}';
    }
}
