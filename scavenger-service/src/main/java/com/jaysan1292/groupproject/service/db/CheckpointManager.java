package com.jaysan1292.groupproject.service.db;

import com.jaysan1292.groupproject.data.Checkpoint;
import com.jaysan1292.groupproject.data.CheckpointBuilder;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.google.common.base.Preconditions.checkArgument;

public class CheckpointManager extends AbstractManager<Checkpoint> {
    private static final String TABLE_NAME = "checkpoint_t";
    private static final String ID_COLUMN = "checkpoint_id";
    private static final String LATITUDE_COLUMN = "latitude";
    private static final String LONGITUDE_COLUMN = "longitude";
    private static final String CHALLENGE_COLUMN = "challenge";
    private static final ChallengeManager challengeManager = new ChallengeManager();

    public CheckpointManager() {
        super(Checkpoint.class);
    }

    protected String tableName() {
        return TABLE_NAME;
    }

    protected String idColumn() {
        return ID_COLUMN;
    }

    protected Checkpoint buildObject(ResultSet rs) throws SQLException {
        CheckpointBuilder builder = new CheckpointBuilder()
                .setCheckpointId(rs.getLong(ID_COLUMN))
                .setLatitude(rs.getFloat(LATITUDE_COLUMN))
                .setLongitude(rs.getFloat(LONGITUDE_COLUMN));

        try {
            builder.setChallenge(challengeManager.get(rs.getLong(CHALLENGE_COLUMN)));
        } catch (GeneralServiceException e) {
            builder.setChallenge(null);
        }

        return builder.build();
    }

    protected long doInsert(Checkpoint item) throws SQLException {
        validate(item);
        String query = "INSERT INTO " + TABLE_NAME + " (" +
                       LATITUDE_COLUMN + ", " +
                       LONGITUDE_COLUMN + ", " +
                       CHALLENGE_COLUMN + ") VALUES (?, ?, ?)";
        return runner.insert(query,
                             new ScalarHandler<BigDecimal>(1),
                             item.getLatitude(),
                             item.getLongitude(),
                             item.getChallenge().getId()).longValue();
    }

    protected void doUpdate(Checkpoint item) throws SQLException {
        validate(item);
        String query = "UPDATE " + TABLE_NAME + " SET " +
                       LATITUDE_COLUMN + "=?, " +
                       LONGITUDE_COLUMN + "=?, " +
                       CHALLENGE_COLUMN + "=? " +
                       "WHERE " + ID_COLUMN + "=?";
        runner.update(query,
                      item.getLatitude(),
                      item.getLongitude(),
                      item.getChallenge().getId(),
                      item.getId());
    }

    protected void doDelete(Checkpoint item) throws SQLException {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + ID_COLUMN + "=?";
        runner.update(query,
                      item.getId());
    }

    private static void validate(Checkpoint c) {
        // latitude ranges from -90 (south pole) to 90 (north pole)
        // longitude ranges from -180 to 180, with 0 being the Prime Meridian
        checkArgument((c.getLatitude() >= -90f) && (c.getLatitude() <= 90f),
                      "Invalid coordinates received. Latitude must lie within the range " +
                      "-90 to 90. Got %s instead.", c.getLatitude());
        checkArgument((c.getLongitude() >= -180f) && (c.getLongitude() <= 180f),
                      "Invalid coordinates recieved. Longitude must lie within the range " +
                      "-180 to 180. Got %s instead.", c.getLongitude());
    }
}
