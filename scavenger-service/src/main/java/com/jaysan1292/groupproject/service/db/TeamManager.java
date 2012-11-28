package com.jaysan1292.groupproject.service.db;

import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.data.Team;
import com.jaysan1292.groupproject.data.TeamBuilder;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.regex.Pattern;

public class TeamManager extends AbstractManager<Team> {
    public static final String TABLE_NAME = "team_t";
    public static final String ID_COLUMN = "team_id";
    public static final String PLAYER_COLUMN = "players";
    private static final Pattern PLAYER_SPLIT = Pattern.compile(",");

    public TeamManager() {
        super(Team.class);
    }

    protected String tableName() {
        return TABLE_NAME;
    }

    protected String idColumn() {
        return ID_COLUMN;
    }

    protected Team buildObject(ResultSet rs) throws SQLException {
        TeamBuilder builder = new TeamBuilder()
                .setTeamId(rs.getLong(ID_COLUMN));

        String[] playerIdStrings = PLAYER_SPLIT.split(rs.getString(PLAYER_COLUMN));
        HashMap<Long, Player> players = new HashMap<Long, Player>(playerIdStrings.length);
        PlayerManager manager = new PlayerManager();
        try {
            for (String playerId : playerIdStrings) {
                long id = Long.parseLong(playerId);
                players.put(id, manager.get(id));
            }
        } catch (GeneralServiceException ignored) {}
        builder.setTeamMembers(players);

        return builder.build();
    }

    protected long doInsert(Team item) throws SQLException {
        String query = "INSERT INTO " + TABLE_NAME + " (" +
                       PLAYER_COLUMN + ") VALUES (?)";
        return runner.insert(query,
                             new ScalarHandler<BigDecimal>(1),
                             item.getTeamPlayerString()).longValue();
    }

    protected void doUpdate(Team item) throws SQLException {
        String query = "UPDATE " + TABLE_NAME + " SET " +
                       PLAYER_COLUMN + "=? " +
                       "WHERE " + ID_COLUMN + "=?";
        runner.update(query,
                      item.getTeamPlayerString(),
                      item.getId());
    }

    protected void doDelete(Team item) throws SQLException {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + ID_COLUMN + "=?";
        runner.update(query, item.getId());
    }
}
