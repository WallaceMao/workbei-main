package com.workbei.model.kanban;

import java.util.Date;

/**
 * @author Wallace Mao
 * Date: 2018-11-12 1:25
 */
public class WbKanbanPreferenceDO {
    private Long id;
    private Long version;
    private Date dateCreated;
    private Date lastUpdated;
    private Long kanbanId;
    private Long defaultKanbanChildId;
    // 计划同步日程
    private String syncTask;
    // 新建任务添加任务创建者为成员
    private Boolean isAddCreatorAsMember;
    // 新建任务仅成员可见
    private Boolean isOnlyMemberCanSee;
    // 任务完成自动下沉
    private Boolean isTaskSinking;
    // 显示任务优先级标识
    private Boolean isShowPriority;
    // 默认选中子计划模式 : 自由模式，默认模式
    private String kanbanChildMode;

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

    public Long getKanbanId() {
        return kanbanId;
    }

    public void setKanbanId(Long kanbanId) {
        this.kanbanId = kanbanId;
    }

    public Long getDefaultKanbanChildId() {
        return defaultKanbanChildId;
    }

    public void setDefaultKanbanChildId(Long defaultKanbanChildId) {
        this.defaultKanbanChildId = defaultKanbanChildId;
    }

    public String getSyncTask() {
        return syncTask;
    }

    public void setSyncTask(String syncTask) {
        this.syncTask = syncTask;
    }

    public Boolean getAddCreatorAsMember() {
        return isAddCreatorAsMember;
    }

    public void setAddCreatorAsMember(Boolean addCreatorAsMember) {
        isAddCreatorAsMember = addCreatorAsMember;
    }

    public Boolean getOnlyMemberCanSee() {
        return isOnlyMemberCanSee;
    }

    public void setOnlyMemberCanSee(Boolean onlyMemberCanSee) {
        isOnlyMemberCanSee = onlyMemberCanSee;
    }

    public Boolean getTaskSinking() {
        return isTaskSinking;
    }

    public void setTaskSinking(Boolean taskSinking) {
        isTaskSinking = taskSinking;
    }

    public Boolean getShowPriority() {
        return isShowPriority;
    }

    public void setShowPriority(Boolean showPriority) {
        isShowPriority = showPriority;
    }

    public String getKanbanChildMode() {
        return kanbanChildMode;
    }

    public void setKanbanChildMode(String kanbanChildMode) {
        this.kanbanChildMode = kanbanChildMode;
    }

    @Override
    public String toString() {
        return "WbKanbanPreferenceDO{" +
                "id=" + id +
                ", version=" + version +
                ", dateCreated=" + dateCreated +
                ", lastUpdated=" + lastUpdated +
                ", kanbanId=" + kanbanId +
                ", defaultKanbanChildId=" + defaultKanbanChildId +
                ", syncTask='" + syncTask + '\'' +
                ", isAddCreatorAsMember=" + isAddCreatorAsMember +
                ", isOnlyMemberCanSee=" + isOnlyMemberCanSee +
                ", isTaskSinking=" + isTaskSinking +
                ", isShowPriority=" + isShowPriority +
                ", kanbanChildMode='" + kanbanChildMode + '\'' +
                '}';
    }
}
