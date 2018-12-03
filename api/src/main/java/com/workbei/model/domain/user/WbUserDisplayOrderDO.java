package com.workbei.model.domain.user;

import java.io.Serializable;
import java.util.Date;

/**
 * userId为唯一约束
 * @author Wallace Mao
 * Date: 2018-11-28 15:08
 */
public class WbUserDisplayOrderDO implements Serializable {
    private Long id;
    private Long userId;
    private Long version;
    private Date dateCreated;
    private Date lastUpdated;
    //文集最大值
    private Double maxCorpusDisplayOrder;
    //文集最小值
    private Double minCorpusDisplayOrder;
    //星标文集最大值
    private Double maxStarCorpusDisplayOrder;
    //星标文集最小值
    private Double minStarCorpusDisplayOrder;
    // 计划列表最大值
    private Double maxKanbanDisplayOrder;
    // 计划列表最小值
    private Double minKanbanDisplayOrder;
    // 计划星标列表最大值
    private Double maxStarKanbanDisplayOrder;
    // 计划星标列表最小值
    private Double minStarKanbanDisplayOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Double getMaxCorpusDisplayOrder() {
        return maxCorpusDisplayOrder;
    }

    public void setMaxCorpusDisplayOrder(Double maxCorpusDisplayOrder) {
        this.maxCorpusDisplayOrder = maxCorpusDisplayOrder;
    }

    public Double getMinCorpusDisplayOrder() {
        return minCorpusDisplayOrder;
    }

    public void setMinCorpusDisplayOrder(Double minCorpusDisplayOrder) {
        this.minCorpusDisplayOrder = minCorpusDisplayOrder;
    }

    public Double getMaxStarCorpusDisplayOrder() {
        return maxStarCorpusDisplayOrder;
    }

    public void setMaxStarCorpusDisplayOrder(Double maxStarCorpusDisplayOrder) {
        this.maxStarCorpusDisplayOrder = maxStarCorpusDisplayOrder;
    }

    public Double getMinStarCorpusDisplayOrder() {
        return minStarCorpusDisplayOrder;
    }

    public void setMinStarCorpusDisplayOrder(Double minStarCorpusDisplayOrder) {
        this.minStarCorpusDisplayOrder = minStarCorpusDisplayOrder;
    }

    public Double getMaxKanbanDisplayOrder() {
        return maxKanbanDisplayOrder;
    }

    public void setMaxKanbanDisplayOrder(Double maxKanbanDisplayOrder) {
        this.maxKanbanDisplayOrder = maxKanbanDisplayOrder;
    }

    public Double getMinKanbanDisplayOrder() {
        return minKanbanDisplayOrder;
    }

    public void setMinKanbanDisplayOrder(Double minKanbanDisplayOrder) {
        this.minKanbanDisplayOrder = minKanbanDisplayOrder;
    }

    public Double getMaxStarKanbanDisplayOrder() {
        return maxStarKanbanDisplayOrder;
    }

    public void setMaxStarKanbanDisplayOrder(Double maxStarKanbanDisplayOrder) {
        this.maxStarKanbanDisplayOrder = maxStarKanbanDisplayOrder;
    }

    public Double getMinStarKanbanDisplayOrder() {
        return minStarKanbanDisplayOrder;
    }

    public void setMinStarKanbanDisplayOrder(Double minStarKanbanDisplayOrder) {
        this.minStarKanbanDisplayOrder = minStarKanbanDisplayOrder;
    }

    @Override
    public String toString() {
        return "WbUserDisplayOrderDO{" +
                "id=" + id +
                ", userId=" + userId +
                ", version=" + version +
                ", dateCreated=" + dateCreated +
                ", lastUpdated=" + lastUpdated +
                ", maxCorpusDisplayOrder=" + maxCorpusDisplayOrder +
                ", minCorpusDisplayOrder=" + minCorpusDisplayOrder +
                ", maxStarCorpusDisplayOrder=" + maxStarCorpusDisplayOrder +
                ", minStarCorpusDisplayOrder=" + minStarCorpusDisplayOrder +
                ", maxKanbanDisplayOrder=" + maxKanbanDisplayOrder +
                ", minKanbanDisplayOrder=" + minKanbanDisplayOrder +
                ", maxStarKanbanDisplayOrder=" + maxStarKanbanDisplayOrder +
                ", minStarKanbanDisplayOrder=" + minStarKanbanDisplayOrder +
                '}';
    }
}
