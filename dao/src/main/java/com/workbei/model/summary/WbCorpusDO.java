package com.workbei.model.summary;

import java.util.Date;

/**
 * @author Wallace Mao
 * Date: 2018-11-12 0:32
 */
public class WbCorpusDO {
    private Long id;
    private Long version;
    private Date dateCreated;
    private Date lastUpdated;

    //文集名
    private String name;
    /*
     * 文集的类型,值为：
     *      工作日报-"daily"，
     *      工作周报-"week"，
     *      工作月报-"month"，
     *      普通文档-"essays"
     * */
    private String type;
    //是否显示文集
    private Boolean isDisplay;
    //文集默认封面
    private String cover;
    /* 文集归属
     * person:归属个人，company:归属公司
     */
    private String attribute;
    //文集添加成员时，该成员的默认角色
    private String defaultRole;
    //文集下的笔记是否能被共享
    private Boolean canBeShared;

    //是否允许编辑之前的笔记
    private Boolean allowEditBeforeDoc;
    //是否允许添加之前的笔记
    private Boolean allowAddBeforeDoc;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Boolean getCanBeShared() {
        return canBeShared;
    }

    public void setCanBeShared(Boolean canBeShared) {
        this.canBeShared = canBeShared;
    }

    public Boolean getAllowEditBeforeDoc() {
        return allowEditBeforeDoc;
    }

    public void setAllowEditBeforeDoc(Boolean allowEditBeforeDoc) {
        this.allowEditBeforeDoc = allowEditBeforeDoc;
    }

    public Boolean getAllowAddBeforeDoc() {
        return allowAddBeforeDoc;
    }

    public void setAllowAddBeforeDoc(Boolean allowAddBeforeDoc) {
        this.allowAddBeforeDoc = allowAddBeforeDoc;
    }

    @Override
    public String toString() {
        return "WbCorpusDO{" +
                "id=" + id +
                ", version=" + version +
                ", dateCreated=" + dateCreated +
                ", lastUpdated=" + lastUpdated +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", isDisplay=" + isDisplay +
                ", cover='" + cover + '\'' +
                ", attribute='" + attribute + '\'' +
                ", defaultRole='" + defaultRole + '\'' +
                ", canBeShared=" + canBeShared +
                ", allowEditBeforeDoc=" + allowEditBeforeDoc +
                ", allowAddBeforeDoc=" + allowAddBeforeDoc +
                '}';
    }
}
