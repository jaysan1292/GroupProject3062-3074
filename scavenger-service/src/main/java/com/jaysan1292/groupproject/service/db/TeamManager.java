package com.jaysan1292.groupproject.service.db;

import com.jaysan1292.groupproject.data.Team;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TeamManager extends AbstractManager<Team> {
    public TeamManager() {
        super(Team.class, "Team", "team_id");
    }

    protected Team createNewInstance() {
        return new Team();
    }

    protected Team buildObject(ResultSet rs) throws SQLException {
        return null;  //TODO: Auto-generated method stub
    }

    protected void doCreate(Connection conn, Team item) throws SQLException {
        //TODO: Auto-generated method stub
    }

    protected void doUpdate(Connection conn, Team item) throws SQLException {
        //TODO: Auto-generated method stub
    }

    protected void doDelete(Connection conn, Team item) throws SQLException {
        //TODO: Auto-generated method stub
    }
}
