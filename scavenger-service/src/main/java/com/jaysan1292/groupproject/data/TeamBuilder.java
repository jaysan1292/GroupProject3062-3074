package com.jaysan1292.groupproject.data;

import java.util.Map;

/** @author Jason Recillo */
public class TeamBuilder extends AbstractBuilder<Team> {
    private Team team;

    public TeamBuilder() {
        init();
    }

    public TeamBuilder(Team team) {
        this.team = team;
    }

    protected void init() {
        team = new Team();
    }

    public Team build() {
        return team;
    }

    public TeamBuilder setTeamId(long teamId) {
        team.setTeamId(teamId);
        return this;
    }

    public TeamBuilder setTeamMembers(Map<Long, Player> teamMembers) {
        team.setTeamMembers(teamMembers);
        return this;
    }
}
