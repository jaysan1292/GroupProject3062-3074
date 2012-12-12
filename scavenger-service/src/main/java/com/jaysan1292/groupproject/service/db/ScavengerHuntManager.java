package com.jaysan1292.groupproject.service.db;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jaysan1292.groupproject.Global;
import com.jaysan1292.groupproject.data.Checkpoint;
import com.jaysan1292.groupproject.data.ScavengerHunt;
import com.jaysan1292.groupproject.data.ScavengerHuntBuilder;
import com.jaysan1292.groupproject.data.Team;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/** @author Jason Recillo */
public class ScavengerHuntManager extends AbstractManager<ScavengerHunt> {
    public static final String TABLE_NAME = "scavengerhunt_t";
    public static final String ID_COLUMN = "scavenger_hunt_id";
    public static final String PATH_COLUMN = "path";
    public static final String COMPLETED_CHECKPOINTS_COLUMN = "completed_checkpoints";
    public static final String TEAM_COLUMN = "team";
    public static final String START_TIME_COLUMN = "start_time";
    public static final String FINISH_TIME_COLUMN = "finish_time";
    private static final PathManager pathManager = new PathManager();
    private static final TeamManager teamManager = new TeamManager();
    private static final CheckpointManager checkpointManager = new CheckpointManager();

    public ScavengerHuntManager() {
        super(ScavengerHunt.class);
    }

    protected String tableName() {
        return TABLE_NAME;
    }

    protected String idColumn() {
        return ID_COLUMN;
    }

    protected ScavengerHunt buildObject(ResultSet rs) throws SQLException {
        Calendar cal = Calendar.getInstance();
        ScavengerHuntBuilder builder = new ScavengerHuntBuilder()
                .setScavengerHuntId(rs.getLong(ID_COLUMN))
                .setStartTime(new DateTime(rs.getTimestamp(START_TIME_COLUMN, cal)))
                .setFinishTime(new DateTime(rs.getTimestamp(FINISH_TIME_COLUMN, cal)));

        try {
            List<Checkpoint> completedCheckpoints = Lists.newArrayList();
            String completed = StringUtils.defaultString(rs.getString(COMPLETED_CHECKPOINTS_COLUMN));
            if (!completed.isEmpty()) {
                String[] checkpointIds = completed.split(",");
                for (String checkpointId : checkpointIds) {
                    completedCheckpoints.add(checkpointManager.get(NumberUtils.toLong(checkpointId)));
                }
                builder.setCompletedCheckpoints(completedCheckpoints);
            }
            builder.setPath(pathManager.get(rs.getLong(PATH_COLUMN)));
            builder.setTeam(teamManager.get(rs.getLong(TEAM_COLUMN)));
        } catch (GeneralServiceException e) {
            Global.log.error(e.getMessage(), e);
        }

        return builder.build();
    }

    protected long doInsert(ScavengerHunt item) throws SQLException {
        validate(item);
        String query = "INSERT INTO " + TABLE_NAME + " (" +
                       PATH_COLUMN + ", " +
                       TEAM_COLUMN + ", " +
                       START_TIME_COLUMN + ", " +
                       FINISH_TIME_COLUMN + ") VALUES (?, ?, ?, ?)";

        return runner.insert(query,
                             new ScalarHandler<BigDecimal>(1),
                             item.getPath().getId(),
                             item.getTeam().getId(),
                             item.getStartTime().toDate(),
                             item.getFinishTime().toDate()).longValue();
    }

    protected void doUpdate(ScavengerHunt item) throws SQLException {
        validate(item);
        String query = "UPDATE " + TABLE_NAME + " SET " +
                       PATH_COLUMN + "=?, " +
                       TEAM_COLUMN + "=?, " +
                       START_TIME_COLUMN + "=?, " +
                       FINISH_TIME_COLUMN + "=? " +
                       "WHERE " + ID_COLUMN + "=?";
        runner.update(query,
                      item.getPath().getId(),
                      item.getTeam().getId(),
                      item.getStartTime().toDate(),
                      item.getFinishTime().toDate(),
                      item.getId());
    }

    private static void validate(ScavengerHunt s) {
        // Ensure that team and path are valid; i.e., make sure they exist in the database
        checkArgument(s.getTeam().getId() != -1);
        checkArgument(s.getPath().getId() != -1);
        checkArgument(s.getStartTime().isBefore(s.getFinishTime()),
                      "Start time of scavenger hunt must be before the finish time.");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public ScavengerHunt getScavengerHunt(Team team) throws GeneralServiceException {
        String query = "SELECT " + ID_COLUMN + ", " + TEAM_COLUMN + " FROM " + TABLE_NAME;

        // Get raw results from the database
        try {
            Map<Long, Long> results =
                    runner.query(query, new ResultSetHandler<Map<Long, Long>>() {
                        public Map<Long, Long> handle(ResultSet rs) throws SQLException {
                            if (!rs.next()) return null;
                            Map<Long, Long> m = Maps.newHashMap();
                            do {
                                m.put(rs.getLong(ID_COLUMN), rs.getLong(TEAM_COLUMN));
                            } while (rs.next());
                            return m;
                        }
                    });

            for (Map.Entry<Long, Long> result : results.entrySet()) {
                long teamId = result.getValue();
                if (team.getId() == teamId) {
                    return this.get(teamId);
                }
            }

            throw new GeneralServiceException("The given team is not currently participating in " +
                                              "a scavenger hunt: " + team.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
