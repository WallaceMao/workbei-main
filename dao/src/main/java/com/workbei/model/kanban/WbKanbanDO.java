package com.workbei.model.kanban;

import java.util.Date;

/**
 * @author Wallace Mao
 * Date: 2018-11-12 0:48
 */
public class WbKanbanDO {
    private Long id;
    private Long version;
    private Date dateCreated;
    private Date lastUpdated;
    private Long kanbanGroupId;
    private String name;
    private Boolean isDisplay;
    // 封面
    private String cover;
    // 看板分类
    private String attribute;
    // 成员默认权限
    private String defaultRole;

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

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Long getKanbanGroupId() {
        return kanbanGroupId;
    }

    public void setKanbanGroupId(Long kanbanGroupId) {
        this.kanbanGroupId = kanbanGroupId;
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getDefaultRole() {
        return defaultRole;
    }

    public void setDefaultRole(String defaultRole) {
        this.defaultRole = defaultRole;
    }

    @Override
    public String toString() {
        return "WbKanbanDO{" +
                "id=" + id +
                ", version=" + version +
                ", dateCreated=" + dateCreated +
                ", lastUpdated=" + lastUpdated +
                ", kanbanGroupId=" + kanbanGroupId +
                ", name='" + name + '\'' +
                ", isDisplay=" + isDisplay +
                ", cover='" + cover + '\'' +
                ", attribute='" + attribute + '\'' +
                ", defaultRole='" + defaultRole + '\'' +
                '}';
    }
}
