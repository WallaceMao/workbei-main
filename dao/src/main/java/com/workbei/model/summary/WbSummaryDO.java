package com.workbei.model.summary;

import java.util.Date;

/**
 * @author Wallace Mao
 * Date: 2018-11-12 0:38
 */
public class WbSummaryDO {
    private Long id;
    private Long version;
    private Long corpusId;
    private Long noteBookId;
    /** 记录创建时间 */
    private Date dateCreated;
    /** 记录最后更新时间 */
    private Date lastUpdated;


    /** 笔记生成时间，
     * 日报（年.月.日）：2017.12.14
     * 周报（年.周）：2017.50
     * 月报（年.月）：2017.12
     * 其它：none
     * */
    private String date;
    //笔记标题
    private String name;
    //笔记的查看权限，是否仅对成员可见
    private Boolean isOpenToMember;
    //是否可见
    private Boolean isDisplay;
    //是否共享
    private Boolean isShare;
    //笔记还是笔记本
    private Boolean isNoteBook;
    /** corpusId-noteBookId-noteId */
    private String code;
    //是否是系统自动生成
    private Boolean isSystem;

    //顺序值
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

    public Long getCorpusId() {
        return corpusId;
    }

    public void setCorpusId(Long corpusId) {
        this.corpusId = corpusId;
    }

    public Long getNoteBookId() {
        return noteBookId;
    }

    public void setNoteBookId(Long noteBookId) {
        this.noteBookId = noteBookId;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getOpenToMember() {
        return isOpenToMember;
    }

    public void setOpenToMember(Boolean openToMember) {
        isOpenToMember = openToMember;
    }

    public Boolean getDisplay() {
        return isDisplay;
    }

    public void setDisplay(Boolean display) {
        isDisplay = display;
    }

    public Boolean getShare() {
        return isShare;
    }

    public void setShare(Boolean share) {
        isShare = share;
    }

    public Boolean getNoteBook() {
        return isNoteBook;
    }

    public void setNoteBook(Boolean noteBook) {
        isNoteBook = noteBook;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getSystem() {
        return isSystem;
    }

    public void setSystem(Boolean system) {
        isSystem = system;
    }

    public Double getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Double displayOrder) {
        this.displayOrder = displayOrder;
    }

    @Override
    public String toString() {
        return "WbSummaryDO{" +
                "id=" + id +
                ", version=" + version +
                ", corpusId=" + corpusId +
                ", noteBookId=" + noteBookId +
                ", dateCreated=" + dateCreated +
                ", lastUpdated=" + lastUpdated +
                ", date='" + date + '\'' +
                ", name='" + name + '\'' +
                ", isOpenToMember=" + isOpenToMember +
                ", isDisplay=" + isDisplay +
                ", isShare=" + isShare +
                ", isNoteBook=" + isNoteBook +
                ", code='" + code + '\'' +
                ", isSystem=" + isSystem +
                ", displayOrder=" + displayOrder +
                '}';
    }
}
