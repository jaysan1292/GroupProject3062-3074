package com.jaysan1292.groupproject.service.db;

import com.jaysan1292.groupproject.data.Challenge;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/** @author Jason Recillo */
public class ChallengeManager extends AbstractManager<Challenge> {
    public ChallengeManager() {
        super(Challenge.class, "Challenge", "challenge_id");
    }

    protected Challenge createNewInstance() {
        return new Challenge();
    }

    protected Challenge buildObject(ResultSet rs) {
        return null;  //TODO: Auto-generated method stub
    }

    protected void doCreate(Connection conn, Challenge item) throws SQLException {
        //TODO: Auto-generated method stub
    }

    protected void doUpdate(Connection conn, Challenge item) throws SQLException {
        //TODO: Auto-generated method stub
    }

    protected void doDelete(Connection conn, Challenge item) throws SQLException {
        //TODO: Auto-generated method stub
    }
}
