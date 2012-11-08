package com.jaysan1292.groupproject.service.db;

import com.jaysan1292.groupproject.data.ScavengerHunt;
import com.jaysan1292.groupproject.data.ScavengerHuntBuilder;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/** @author Jason Recillo */
public class ScavengerHuntManager extends AbstractManager<ScavengerHunt> {
    private static final PathManager pathManager = new PathManager();
    private static final TeamManager teamManager = new TeamManager();

    public ScavengerHuntManager() {
        super(ScavengerHunt.class, "ScavengerHunt", "scavenger_hunt_id");
    }

    protected ResultSetHandler<ScavengerHunt> getResultSetHandler() {
        return new ResultSetHandler<ScavengerHunt>() {
            public ScavengerHunt handle(ResultSet rs) throws SQLException {
                if (!rs.next()) return null;

                return new ScavengerHuntBuilder()
                        .setScavengerHuntId(rs.getLong("scavenger_hunt_id"))
                        .setPath(pathManager.get(rs.getLong("path")))
                        .setTeam(teamManager.get(rs.getLong("team")))
                        .setStartTime(rs.getDate("start_time"))
                        .setFinishTime(rs.getDate("finish_time"))
                        .build();
            }
        };
    }

    protected ResultSetHandler<ScavengerHunt[]> getArrayResultSetHandler() {
        return new ResultSetHandler<ScavengerHunt[]>() {
            public ScavengerHunt[] handle(ResultSet rs) throws SQLException {
                if (!rs.next()) return null;

                ArrayList<ScavengerHunt> scavengerHunts = new ArrayList<ScavengerHunt>();
                do {
                    scavengerHunts.add(new ScavengerHuntBuilder()
                                               .setScavengerHuntId(rs.getLong("scavenger_hunt_id"))
                                               .setPath(pathManager.get(rs.getLong("path")))
                                               .setTeam(teamManager.get(rs.getLong("team")))
                                               .setStartTime(rs.getDate("start_time"))
                                               .setFinishTime(rs.getDate("finish_time"))
                                               .build());
                } while (rs.next());

                return scavengerHunts.toArray(new ScavengerHunt[scavengerHunts.size()]);
            }
        };
    }

    protected ScavengerHunt createNewInstance() {
        return new ScavengerHunt();
    }
}
