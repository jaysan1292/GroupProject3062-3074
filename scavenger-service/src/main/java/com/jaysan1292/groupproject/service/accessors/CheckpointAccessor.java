package com.jaysan1292.groupproject.service.accessors;

import com.jaysan1292.groupproject.data.Checkpoint;
import com.jaysan1292.groupproject.service.db.CheckpointManager;

import javax.ws.rs.Path;

/** @author Jason Recillo */
@Path("/checkpoints")
public class CheckpointAccessor extends AbstractAccessor<Checkpoint> {
    protected static final CheckpointManager manager = new CheckpointManager();
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String CHALLENGE = "challenge";

    public CheckpointAccessor() {
        super(Checkpoint.class);
    }

    protected CheckpointManager getManager() {
        return manager;
    }
}
