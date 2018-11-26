package com.workbei.service;

/**
 * @author Wallace Mao
 * Date: 2018-11-26 11:03
 */
public interface SolutionBizService {
    void generateTeamSolution(String teamId, String userId);

    void generateUserSolution(String teamId, String userId);
}
