package com.workbei.model.view.autocreate;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 15:20
 */
public class AutoCreateTeamVO {
    private String name;
    private String logo;
    //  对应outerDataTeam中的数据
    private String outerCorpId;
    private String client;

    private AutoCreateUserVO creator;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getOuterCorpId() {
        return outerCorpId;
    }

    public void setOuterCorpId(String outerCorpId) {
        this.outerCorpId = outerCorpId;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public AutoCreateUserVO getCreator() {
        return creator;
    }

    public void setCreator(AutoCreateUserVO creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "AutoCreateTeamVO{" +
                "name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", outerCorpId='" + outerCorpId + '\'' +
                ", client='" + client + '\'' +
                ", creator=" + creator +
                '}';
    }
}
