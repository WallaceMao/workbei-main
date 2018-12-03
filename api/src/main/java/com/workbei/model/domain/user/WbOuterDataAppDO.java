package com.workbei.model.domain.user;

import java.io.Serializable;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 18:55
 */
public class WbOuterDataAppDO implements Serializable {
    private Long id;
    private Long version;
    private String name;
    private String key;
    private String token;
    private String whiteIpList;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getWhiteIpList() {
        return whiteIpList;
    }

    public void setWhiteIpList(String whiteIpList) {
        this.whiteIpList = whiteIpList;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "WbOuterDataAppDO{" +
                "id=" + id +
                ", version=" + version +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", token='" + token + '\'' +
                ", whiteIpList='" + whiteIpList + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
