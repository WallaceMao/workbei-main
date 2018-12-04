package com.workbei.model.view.autocreate;

import java.io.Serializable;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 15:20
 */
public class AutoCreateDepartmentVO implements Serializable {
    private Long id;
    private String name;
    private Double displayOrder;

    private String client;
    private String outerCorpId;
    private String outerParentCombineId;
    private String outerCombineId;
    private Boolean isTop = false;

    //  不再使用parentId和teamId来传值
    @Deprecated
    private String parentId;
    @Deprecated
    private String teamId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Double displayOrder) {
        this.displayOrder = displayOrder;
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

    public String getOuterParentCombineId() {
        return outerParentCombineId;
    }

    public void setOuterParentCombineId(String outerParentCombineId) {
        this.outerParentCombineId = outerParentCombineId;
    }

    public String getOuterCombineId() {
        return outerCombineId;
    }

    public void setOuterCombineId(String outerCombineId) {
        this.outerCombineId = outerCombineId;
    }

    public Boolean getTop() {
        return isTop;
    }

    public void setTop(Boolean top) {
        isTop = top;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    @Override
    public String toString() {
        return "AutoCreateDepartmentVO{" +
                "name='" + name + '\'' +
                ", displayOrder=" + displayOrder +
                ", client='" + client + '\'' +
                ", outerCorpId='" + outerCorpId + '\'' +
                ", outerParentCombineId='" + outerParentCombineId + '\'' +
                ", outerCombineId='" + outerCombineId + '\'' +
                ", isTop=" + isTop +
                ", parentId='" + parentId + '\'' +
                ", teamId='" + teamId + '\'' +
                '}';
    }
}
