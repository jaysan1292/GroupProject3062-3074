package com.jaysan1292.groupproject.service.db;

import com.jaysan1292.groupproject.Global;
import com.jaysan1292.groupproject.data.ScavengerHunt;
import com.jaysan1292.groupproject.data.ScavengerHuntBuilder;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/** @author Jason Recillo */
public class ScavengerHuntManager extends AbstractManager<ScavengerHunt> {
    public static final String TABLE_NAME = "scavengerhunt_t";
    public static final String ID_COLUMN = "scavenger_hunt_id";
    public static final String PATH_COLUMN = "path";
    public static final String TEAM_COLUMN = "team";
    public static final String START_TIME_COLUMN = "start_time";
    public static final String FINISH_TIME_COLUMN = "finish_time";
    private static final PathManager pathManager = new PathManager();
    private static final TeamManager teamManager = new TeamManager();

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
            builder.setPath(pathManager.get(rs.getLong(PATH_COLUMN)));
            builder.setTeam(teamManager.get(rs.getLong(TEAM_COLUMN)));
        } catch (GeneralServiceException e) {
            Global.log.error(e.getMessage(), e);
        }

        return builder.build();
    }

    protected long doInsert(ScavengerHunt item) throws SQLException {
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

    protected void doDelete(ScavengerHunt item) throws SQLException {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + ID_COLUMN + "=?";
        runner.update(query,
                      item.getId());
    }
}
