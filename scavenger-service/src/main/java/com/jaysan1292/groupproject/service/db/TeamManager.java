package com.jaysan1292.groupproject.service.db;

import com.google.common.collect.Maps;
import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.data.Team;
import com.jaysan1292.groupproject.data.TeamBuilder;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;

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
        checkArgument(item.isValid());

        String query = "INSERT INTO " + TABLE_NAME + " (" +
                       PLAYER_COLUMN + ") VALUES (?)";
        return runner.insert(query,
                             new ScalarHandler<BigDecimal>(1),
                             item.getTeamPlayerString()).longValue();
    }

    protected void doUpdate(Team item) throws SQLException {
        checkArgument(item.isValid());

        String query = "UPDATE " + TABLE_NAME + " SET " +
                       PLAYER_COLUMN + "=? " +
                       "WHERE " + ID_COLUMN + "=?";
        runner.update(query,
                      item.getTeamPlayerString(),
                      item.getId());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public Team getTeam(Player player) throws GeneralServiceException {
        try {
            String query = "SELECT * FROM " + TABLE_NAME;
            // Get the raw results from the database
            Map<Long, String> results =
                    runner.query(query, new ResultSetHandler<Map<Long, String>>() {
                        public Map<Long, String> handle(ResultSet rs) throws SQLException {
                            if (!rs.next()) return null;
                            Map<Long, String> m = Maps.newHashMap();
                            do {
                                m.put(rs.getLong(ID_COLUMN), rs.getString(PLAYER_COLUMN));
                            } while (rs.next());
                            return m;
                        }
                    });

            // For each result, get the comma-delimited string that was in the PLAYERS column
            // and check each ID until it finds a match
            for (Map.Entry<Long, String> result : results.entrySet()) {
                String[] playerIds = PLAYER_SPLIT.split(result.getValue());
                for (String playerId : playerIds) {
                    long pid = NumberUtils.toLong(playerId);
                    if (player.getId() == pid) {
                        return this.get(result.getKey());
                    }
                }
            }

            // If we get here, the player isn't on any team
            throw new GeneralServiceException("The given player is not on any team: " +
                                              player.getId());
        } catch (SQLException e) {
            throw new GeneralServiceException(e);
        }
    }
}
