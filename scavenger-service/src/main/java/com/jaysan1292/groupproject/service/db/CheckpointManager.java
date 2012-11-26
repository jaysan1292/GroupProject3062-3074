package com.jaysan1292.groupproject.service.db;

import com.jaysan1292.groupproject.data.Checkpoint;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckpointManager extends AbstractManager<Checkpoint> {
    private final ChallengeManager challengeManager = new ChallengeManager();

    public CheckpointManager() {
        super(Checkpoint.class, "Checkpoint", "checkpoint_id");
    }

    protected Checkpoint createNewInstance() {
        return new Checkpoint();
    }

    protected Checkpoint buildObject(ResultSet rs) {
        return null;  //TODO: Auto-generated method stub
    }

    protected void doCreate(Connection conn, Checkpoint item) throws SQLException {
        //TODO: Auto-generated method stub
    }

    protected void doUpdate(Connection conn, Checkpoint item) throws SQLException {
        //TODO: Auto-generated method stub
    }

    protected void doDelete(Connection conn, Checkpoint item) throws SQLException {
        //TODO: Auto-generated method stub
    }
}
