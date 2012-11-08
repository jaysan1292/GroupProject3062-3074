package com.jaysan1292.groupproject.service.db;

import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.data.PlayerBuilder;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlayerManager extends AbstractManager<Player> {
    public PlayerManager() {
        super(Player.class, "Player", "player_id");
    }

    protected ResultSetHandler<Player> getResultSetHandler() {
        return new ResultSetHandler<Player>() {
            public Player handle(ResultSet rs) throws SQLException {
                if (!rs.next()) return null;

                return new PlayerBuilder()
                        .setPlayerId(rs.getLong("player_id"))
                        .setFirstName(rs.getString("first_name"))
                        .setLastName(rs.getString("last_name"))
                        .setStudentId(rs.getString("student_number"))
                        .build();
            }
        };
    }

    protected ResultSetHandler<Player[]> getArrayResultSetHandler() {
        return new ResultSetHandler<Player[]>() {
            public Player[] handle(ResultSet rs) throws SQLException {
                if (!rs.next()) return null;

                ArrayList<Player> players = new ArrayList<Player>();
                do {
                    players.add(new PlayerBuilder()
                                        .setPlayerId(rs.getLong("player_id"))
                                        .setFirstName(rs.getString("first_name"))
                                        .setLastName(rs.getString("last_name"))
                                        .setStudentId(rs.getString("student_number"))
                                        .build());
                } while (rs.next());

                return players.toArray(new Player[players.size()]);
            }
        };
    }

    protected Player createNewInstance() {
        return new Player();
    }
}
