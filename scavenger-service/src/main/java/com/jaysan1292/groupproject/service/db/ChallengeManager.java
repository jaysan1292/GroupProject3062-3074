package com.jaysan1292.groupproject.service.db;

import com.jaysan1292.groupproject.data.Challenge;
import com.jaysan1292.groupproject.data.ChallengeBuilder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/** @author Jason Recillo */
public class ChallengeManager extends AbstractManager<Challenge> {
    private static final String TABLE_NAME = "challenge_t";
    private static final String ID_COLUMN = "challenge_id";
    private static final String TEXT_COLUMN = "challenge_text";

    public ChallengeManager() {
        super(Challenge.class);
    }

    protected String tableName() {
        return TABLE_NAME;
    }

    protected String idColumn() {
        return ID_COLUMN;
    }

    protected Challenge buildObject(ResultSet rs) throws SQLException {
        return new ChallengeBuilder()
                .setChallengeId(rs.getLong(ID_COLUMN))
                .setChallengeText(rs.getString(TEXT_COLUMN))
                .build();
    }

    protected void doCreate(Connection conn, Challenge item) throws SQLException {
        String query = "INSERT INTO " + TABLE_NAME + " (" + TEXT_COLUMN + ") VALUES (?)";
        runner.update(conn, query, item.getChallengeText());
    }

    protected void doUpdate(Connection conn, Challenge item) throws SQLException {
        String query = "UPDATE " + TABLE_NAME + " SET " + TEXT_COLUMN + "=? WHERE " + ID_COLUMN + "=?";
        runner.update(conn, query, item.getChallengeText(), item.getId());
    }

    protected void doDelete(Connection conn, Challenge item) throws SQLException {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + ID_COLUMN + "=?";
        runner.update(conn, query, item.getId());
    }
}
