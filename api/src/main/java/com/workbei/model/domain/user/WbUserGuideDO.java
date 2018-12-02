package com.workbei.model.domain.user;

/**
 * userId为唯一约束
 * @author Wallace Mao
 * Date: 2018-11-28 15:07
 */
public class WbUserGuideDO {
    private Long id;
    private Long version;
    private Long userId;
    private Boolean guideKanbanFlag;
    private Boolean guideWebFlag;
    private Boolean guideTaskFlag;
    private Boolean guideNoteFlag;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getGuideKanbanFlag() {
        return guideKanbanFlag;
    }

    public void setGuideKanbanFlag(Boolean guideKanbanFlag) {
        this.guideKanbanFlag = guideKanbanFlag;
    }

    public Boolean getGuideWebFlag() {
        return guideWebFlag;
    }

    public void setGuideWebFlag(Boolean guideWebFlag) {
        this.guideWebFlag = guideWebFlag;
    }

    public Boolean getGuideTaskFlag() {
        return guideTaskFlag;
    }

    public void setGuideTaskFlag(Boolean guideTaskFlag) {
        this.guideTaskFlag = guideTaskFlag;
    }

    public Boolean getGuideNoteFlag() {
        return guideNoteFlag;
    }

    public void setGuideNoteFlag(Boolean guideNoteFlag) {
        this.guideNoteFlag = guideNoteFlag;
    }
}
