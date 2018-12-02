package com.workbei.model.domain.user;

import java.io.Serializable;

/**
 * username为唯一约束
 * @author Wallace Mao
 * Date: 2018-11-27 15:20
 */
public class WbUserDO implements Serializable {
    private Long id;
    private Long version;
    private Boolean isParent;
    private Long teamId;
    private Long accountId;
    //随机的唯一的字符串（类似于微信的unionid）
    private String username;
    //职位
    private String position;
    //公司成员列表设置时改的名字
    private String name;
    // 是否存在
    private Boolean isDisplay;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Boolean getParent() {
        return isParent;
    }

    public void setParent(Boolean parent) {
        isParent = parent;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDisplay() {
        return isDisplay;
    }

    public void setDisplay(Boolean display) {
        isDisplay = display;
    }
}
