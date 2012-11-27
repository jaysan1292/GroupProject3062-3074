package com.jaysan1292.groupproject.service.db;

import com.jaysan1292.groupproject.data.Checkpoint;
import com.jaysan1292.groupproject.data.Path;
import com.jaysan1292.groupproject.data.PathBuilder;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

/** @author Jason Recillo */
public class PathManager extends AbstractManager<Path> {
    /** Database table name. */
    public static final String TABLE_NAME = "path_t";

    /** Database row ID column name. */
    public static final String ID_COLUMN = "path_id";

    /** Path checkpoint column name. */
    public static final String CHECKPOINT_COLUMN = "checkpoints";

    ///////////////////////////////////////////////////////////////////////////
    private static final Pattern CHECKPOINT_SPLIT = Pattern.compile(",");

    public PathManager() {
        super(Path.class);
    }

    protected String tableName() {
        return TABLE_NAME;
    }

    protected String idColumn() {
        return ID_COLUMN;
    }

    protected Path buildObject(ResultSet rs) throws SQLException {
        PathBuilder builder = new PathBuilder()
                .setPathId(rs.getLong(ID_COLUMN));

        String[] checkpointIdStrings = CHECKPOINT_SPLIT.split(rs.getString(CHECKPOINT_COLUMN));
        ArrayList<Checkpoint> checkpoints = new ArrayList<Checkpoint>(checkpointIdStrings.length);
        CheckpointManager manager = new CheckpointManager();
        try {
            for (String checkpointId : checkpointIdStrings) {
                checkpoints.add(manager.get(Long.parseLong(checkpointId)));
            }
        } catch (GeneralServiceException ignored) {}
        builder.setCheckpoints(checkpoints);

        return builder.build();
    }

    protected void doCreate(Connection conn, Path item) throws SQLException {
        String query = "INSERT INTO " + TABLE_NAME + " (" +
                       CHECKPOINT_COLUMN + ") VALUES (?)";
        runner.update(conn, query, item.getCheckpointString());
    }

    protected void doUpdate(Connection conn, Path item) throws SQLException {
        String query = "UPDATE " + TABLE_NAME + " SET " + CHECKPOINT_COLUMN + "=? WHERE " + ID_COLUMN + "=?";
        runner.update(conn, query, item.getCheckpointString(), item.getId());
    }

    protected void doDelete(Connection conn, Path item) throws SQLException {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + ID_COLUMN + "=?";
        runner.update(conn, query, item.getId());
    }
}
