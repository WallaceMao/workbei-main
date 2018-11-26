package com.workbei.model.todo;

import java.util.Date;

/**
 * @author Wallace Mao
 * Date: 2018-11-12 0:55
 */
public class WbTaskDO {
    private Long id;
    private Date dateCreated;
    private Date lastUpdated;
    private Long version;
    private Long kanbanCardId;
    private Long taskRepeatId;
    private Long creatorId;
    private Long parentTaskId;
    // 任务名称
    private String name;
    // 备注
    private String note;
    // 优先级 1:IE、2：IU、3：UE、4：UU、5:inbox
    private Long  priority;
    // 进度
    private Long progress;
    // 开始时间 是时间不是日期
    private String startTime;
    // 结束时间 是时间不是日期
    private String endTime;
    // 完成时间
    private Date finishTime;
    // 任务类型 1.日程任务，2.计划任务，3.多级子任务
    private Long type;
    // 是否已进行日程和计划任务之间的同步
    private Boolean isSync;
    // 是否是收纳箱
    private Boolean isInbox;
    // 是否已删除
    //Boolean isDeleted = false
    private Boolean isDisplay;
    // 仅成员可见
    private Boolean isOpenToMember;
    // 禁止修改
    private Boolean forbidEdit;
    private Double displayOrder;
    private Long oldTodoId;
    private  Long oldKanbanItemId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getKanbanCardId() {
        return kanbanCardId;
    }

    public void setKanbanCardId(Long kanbanCardId) {
        this.kanbanCardId = kanbanCardId;
    }

    public Long getTaskRepeatId() {
        return taskRepeatId;
    }

    public void setTaskRepeatId(Long taskRepeatId) {
        this.taskRepeatId = taskRepeatId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(Long parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public Long getProgress() {
        return progress;
    }

    public void setProgress(Long progress) {
        this.progress = progress;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Boolean getSync() {
        return isSync;
    }

    public void setSync(Boolean sync) {
        isSync = sync;
    }

    public Boolean getInbox() {
        return isInbox;
    }

    public void setInbox(Boolean inbox) {
        isInbox = inbox;
    }

    public Boolean getDisplay() {
        return isDisplay;
    }

    public void setDisplay(Boolean display) {
        isDisplay = display;
    }

    public Boolean getOpenToMember() {
        return isOpenToMember;
    }

    public void setOpenToMember(Boolean openToMember) {
        isOpenToMember = openToMember;
    }

    public Boolean getForbidEdit() {
        return forbidEdit;
    }

    public void setForbidEdit(Boolean forbidEdit) {
        this.forbidEdit = forbidEdit;
    }

    public Double getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Double displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Long getOldTodoId() {
        return oldTodoId;
    }

    public void setOldTodoId(Long oldTodoId) {
        this.oldTodoId = oldTodoId;
    }

    public Long getOldKanbanItemId() {
        return oldKanbanItemId;
    }

    public void setOldKanbanItemId(Long oldKanbanItemId) {
        this.oldKanbanItemId = oldKanbanItemId;
    }

    @Override
    public String toString() {
        return "WbTaskDO{" +
                "id=" + id +
                ", dateCreated=" + dateCreated +
                ", lastUpdated=" + lastUpdated +
                ", version=" + version +
                ", kanbanCardId=" + kanbanCardId +
                ", taskRepeatId=" + taskRepeatId +
                ", creatorId=" + creatorId +
                ", parentTaskId=" + parentTaskId +
                ", name='" + name + '\'' +
                ", note='" + note + '\'' +
                ", priority=" + priority +
                ", progress=" + progress +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", finishTime=" + finishTime +
                ", type=" + type +
                ", isSync=" + isSync +
                ", isInbox=" + isInbox +
                ", isDisplay=" + isDisplay +
                ", isOpenToMember=" + isOpenToMember +
                ", forbidEdit=" + forbidEdit +
                ", displayOrder=" + displayOrder +
                ", oldTodoId=" + oldTodoId +
                ", oldKanbanItemId=" + oldKanbanItemId +
                '}';
    }
}
