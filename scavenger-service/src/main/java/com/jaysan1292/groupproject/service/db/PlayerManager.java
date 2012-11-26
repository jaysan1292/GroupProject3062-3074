package com.jaysan1292.groupproject.service.db;

import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.data.PlayerBuilder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerManager extends AbstractManager<Player> {
    private static final String TABLE_NAME = "Player";
    private static final String ID_COLUMN = "player_id";
    private static final String FIRST_NAME_COLUMN = "first_name";
    private static final String LAST_NAME_COLUMN = "last_name";
    private static final String STUDENT_NUMBER_COLUMN = "student_number";

    public PlayerManager() {
        super(Player.class, TABLE_NAME, ID_COLUMN);
    }

    protected Player createNewInstance() {
        return new Player();
    }

    protected Player buildObject(ResultSet rs) throws SQLException {
        return new PlayerBuilder()
                .setPlayerId(rs.getLong(ID_COLUMN))
                .setFirstName(rs.getString(FIRST_NAME_COLUMN))
                .setLastName(rs.getString(LAST_NAME_COLUMN))
                .setStudentId(rs.getString(STUDENT_NUMBER_COLUMN))
                .build();
    }

    protected void doCreate(Connection conn, Player item) throws SQLException {
        String query = "INSERT INTO " + TABLE_NAME + " (" +
                       FIRST_NAME_COLUMN + ", " +
                       LAST_NAME_COLUMN + ", " +
                       STUDENT_NUMBER_COLUMN + ") VALUES (?, ?, ?)";
        runner.update(conn, query,
                      item.getFirstName(),
                      item.getLastName(),
                      item.getStudentNumber());
    }

    protected void doUpdate(Connection conn, Player item) throws SQLException {
        String query = "UPDATE " + TABLE_NAME + " SET " +
                       FIRST_NAME_COLUMN + "=?, " +
                       LAST_NAME_COLUMN + "=?, " +
                       STUDENT_NUMBER_COLUMN + "=? " +
                       "WHERE " + ID_COLUMN + "=?";
        runner.update(conn, query,
                      item.getFirstName(),
                      item.getLastName(),
                      item.getStudentNumber());
    }

    protected void doDelete(Connection conn, Player item) throws SQLException {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + ID_COLUMN + "=?";
        runner.update(conn, query, item.getId());
    }
}
