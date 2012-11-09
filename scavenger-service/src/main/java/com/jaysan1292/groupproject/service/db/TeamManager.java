package com.jaysan1292.groupproject.service.db;

import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.data.Team;
import com.jaysan1292.groupproject.data.TeamBuilder;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeamManager extends AbstractManager<Team> {

    public TeamManager() {
        super(Team.class, "Team", "team_id");
    }

    protected ResultSetHandler<Team> getResultSetHandler() {
        return new ResultSetHandler<Team>() {
            public Team handle(ResultSet rs) throws SQLException {
                if (!rs.next()) return null;

                String[] playerIds = StringUtils.split(rs.getString("players"), ',');

                Map<Long, Player> players = new HashMap<Long, Player>();
                PlayerManager playerProvider = new PlayerManager();
                for (String playerId : playerIds) {
                    long pid = NumberUtils.toLong(playerId);
                    players.put(pid, playerProvider.get(pid));
                }

                return new TeamBuilder()
                        .setTeamId(rs.getLong("team_id"))
                        .setTeamMembers(players)
                        .build();
            }
        };
    }

    protected ResultSetHandler<Team[]> getArrayResultSetHandler() {
        return new ResultSetHandler<Team[]>() {
            public Team[] handle(ResultSet rs) throws SQLException {
                if (!rs.next()) return null;

                ArrayList<Team> teams = new ArrayList<Team>();
                do {
                    String[] playerIds = StringUtils.split(rs.getString("players"), ',');

                    Map<Long, Player> players = new HashMap<Long, Player>();
                    if (playerIds != null) {
                        PlayerManager playerManager = new PlayerManager();
                        for (String playerId : playerIds) {
                            long pid = NumberUtils.toLong(playerId);
                            Player player = playerManager.get(pid);
                            if (player == null) continue;
                            players.put(pid, player);
                        }
                    }

                    teams.add(new TeamBuilder()
                                      .setTeamId(rs.getLong("team_id"))
                                      .setTeamMembers(players)
                                      .build());
                } while (rs.next());

                return teams.toArray(new Team[teams.size()]);
            }
        };
    }

    protected Team createNewInstance() {
        return new Team();
    }
}
