package com.workbei.model.system;

import java.util.Date;

/**
 * @author Wallace Mao
 * Date: 2018-11-12 3:04
 */
public class WbSolutionDO {
    private Long id;
    private Long version;
    private Date dateCreated;
    private Date lastUpdated;
    private String scene;
    private String account;
    private String type;
    private String todo;
    private String kanban;
    private String corpus;
    private Boolean updating;

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

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getKanban() {
        return kanban;
    }

    public void setKanban(String kanban) {
        this.kanban = kanban;
    }

    public String getCorpus() {
        return corpus;
    }

    public void setCorpus(String corpus) {
        this.corpus = corpus;
    }

    public Boolean getUpdating() {
        return updating;
    }

    public void setUpdating(Boolean updating) {
        this.updating = updating;
    }
}
