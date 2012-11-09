package com.jaysan1292.groupproject.service.db;

import com.jaysan1292.groupproject.data.Checkpoint;
import com.jaysan1292.groupproject.data.Path;
import com.jaysan1292.groupproject.data.PathBuilder;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/** @author Jason Recillo */
public class PathManager extends AbstractManager<Path> {
    public PathManager() {
        super(Path.class, "Path", "path_id");
    }

    protected ResultSetHandler<Path> getResultSetHandler() {
        return new ResultSetHandler<Path>() {
            public Path handle(ResultSet rs) throws SQLException {
                if (!rs.next()) return null;

                String[] challengeIds = StringUtils.split(rs.getString("checkpoints"), ',');

                ArrayList<Checkpoint> checkpoints = new ArrayList<Checkpoint>();
                CheckpointManager checkpointManager = new CheckpointManager();
                for (String challengeId : challengeIds) {
                    long cid = NumberUtils.toLong(challengeId);
                    checkpoints.add(checkpointManager.get(cid));
                }

                return new PathBuilder()
                        .setPathId(rs.getLong("path_id"))
                        .setCheckpoints(checkpoints)
                        .build();
            }
        };
    }

    protected ResultSetHandler<Path[]> getArrayResultSetHandler() {
        return new ResultSetHandler<Path[]>() {
            public Path[] handle(ResultSet rs) throws SQLException {
                if (!rs.next()) return null;

                ArrayList<Path> paths = new ArrayList<Path>();
                do {
                    String[] challengeIds = StringUtils.split(rs.getString("checkpoints"), ',');

                    ArrayList<Checkpoint> checkpoints = new ArrayList<Checkpoint>();
                    CheckpointManager checkpointManager = new CheckpointManager();
                    for (String challengeId : challengeIds) {
                        long cid = NumberUtils.toLong(challengeId);
                        checkpoints.add(checkpointManager.get(cid));
                    }

                    paths.add(new PathBuilder()
                                      .setPathId(rs.getLong("path_id"))
                                      .setCheckpoints(checkpoints)
                                      .build());
                } while (rs.next());

                return paths.toArray(new Path[paths.size()]);
            }
        };
    }

    protected Path createNewInstance() {
        return new Path();
    }
}
