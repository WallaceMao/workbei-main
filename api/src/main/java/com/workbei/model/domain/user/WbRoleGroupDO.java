package com.workbei.model.domain.user;

import java.io.Serializable;

/**
 * @author Wallace Mao
 * Date: 2018-11-28 15:51
 */
public class WbRoleGroupDO implements Serializable {
    private Long id;
    private Long version;
    private String name;

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
}
