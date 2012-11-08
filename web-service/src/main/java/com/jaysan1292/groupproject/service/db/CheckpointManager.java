package com.jaysan1292.groupproject.service.db;

import com.jaysan1292.groupproject.data.Challenge;
import com.jaysan1292.groupproject.data.Checkpoint;
import com.jaysan1292.groupproject.data.CheckpointBuilder;
import com.jaysan1292.groupproject.exceptions.ItemNotFoundException;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CheckpointManager extends AbstractManager<Checkpoint> {
    private final ChallengeManager challengeManager = new ChallengeManager();

    public CheckpointManager() {
        super(Checkpoint.class, "Checkpoint", "checkpoint_id");
    }

    protected ResultSetHandler<Checkpoint> getResultSetHandler() {
        return new ResultSetHandler<Checkpoint>() {
            public Checkpoint handle(ResultSet rs) throws SQLException {
                if (!rs.next()) return null;

                Challenge challenge = null;
                try {
                    challenge = challengeManager.get(rs.getLong("challenge"));
                } catch (ItemNotFoundException ignored) {}
                return new CheckpointBuilder()
                        .setCheckpointId(rs.getLong("checkpoint_id"))
                        .setLatitude(rs.getFloat("latitude"))
                        .setLongitude(rs.getFloat("longitude"))
                        .setChallenge(challenge)
                        .build();
            }
        };
    }

    protected ResultSetHandler<Checkpoint[]> getArrayResultSetHandler() {
        return new ResultSetHandler<Checkpoint[]>() {
            public Checkpoint[] handle(ResultSet rs) throws SQLException {
                if (!rs.next()) return null;

                ArrayList<Checkpoint> checkpoint = new ArrayList<Checkpoint>();

                do {
                    Challenge challenge = null;
                    try {
                        challenge = challengeManager.get(rs.getLong("challenge"));
                    } catch (ItemNotFoundException ignored) {}
                    checkpoint.add(new CheckpointBuilder()
                                           .setCheckpointId(rs.getLong("checkpoint_id"))
                                           .setLatitude(rs.getFloat("latitude"))
                                           .setLongitude(rs.getFloat("longitude"))
                                           .setChallenge(challenge)
                                           .build());
                } while (rs.next());

                return checkpoint.toArray(new Checkpoint[checkpoint.size()]);
            }
        };
    }

    protected Checkpoint createNewInstance() {
        return new Checkpoint();
    }
}
