package com.workbei.model.summary;

import java.util.Date;

/**
 * @author Wallace Mao
 * Date: 2018-11-12 0:42
 */
public class WbSummaryNoteDO {
    private Long id;
    private Long version;
    private Long summaryId;
    private Date dateCreated;
    private Date lastUpdated;

    /*
    * 笔记内容（从Summary表分出来是为了加快笔记列表的查询）
    * 你可能经常会看到note.note.note这样的操作，
    * 第一个note表示是Summary对象，即笔记对象
    * 第二个note表示笔记内容对象，即本类的对象
    * 第三个note标识当前类的note字段，即真正的笔记的内容
    * */
    private String note;

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

    public Long getSummaryId() {
        return summaryId;
    }

    public void setSummaryId(Long summaryId) {
        this.summaryId = summaryId;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "WbSummaryNoteDO{" +
                "id=" + id +
                ", version=" + version +
                ", summaryId=" + summaryId +
                ", dateCreated=" + dateCreated +
                ", lastUpdated=" + lastUpdated +
                ", note='" + note + '\'' +
                '}';
    }
}
