package com.workbei.model.biz.user;

/**
 * @author Wallace Mao
 * Date: 2018-11-28 11:31
 */
public class TeamBO {
    private Long id;
    private String name;
    private String logo;
    //  对应outerDataTeam中的数据
    private String outerId;
    private String client;

    private UserBO creator;
}
