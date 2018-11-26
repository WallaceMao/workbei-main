package com.workbei.model.kanban;

import java.util.Date;

/**
 * @author Wallace Mao
 * Date: 2018-11-12 0:54
 */
public class WbKanbanCardDO {
    private Long id;
    private Long version;
    private Date dateCreated;
    private Date lastUpdated;
    private Long kanbanChildId;
    private String name;
    private Boolean isDisplay;
    private Double displayOrder;

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

    public Long getKanbanChildId() {
        return kanbanChildId;
    }

    public void setKanbanChildId(Long kanbanChildId) {
        this.kanbanChildId = kanbanChildId;
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

    public Double getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Double displayOrder) {
        this.displayOrder = displayOrder;
    }

    @Override
    public String toString() {
        return "WbKanbanCardDO{" +
                "id=" + id +
                ", version=" + version +
                ", dateCreated=" + dateCreated +
                ", lastUpdated=" + lastUpdated +
                ", kanbanChildId=" + kanbanChildId +
                ", name='" + name + '\'' +
                ", isDisplay=" + isDisplay +
                ", displayOrder=" + displayOrder +
                '}';
    }
}
