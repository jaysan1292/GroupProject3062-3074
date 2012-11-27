package com.jaysan1292.groupproject.service.accessors;

import com.jaysan1292.groupproject.data.Checkpoint;
import com.jaysan1292.groupproject.service.db.CheckpointManager;

import javax.ws.rs.Path;

/** @author Jason Recillo */
@Path("/checkpoints")
public class CheckpointAccessor extends AbstractAccessor<Checkpoint> {
    private static final CheckpointManager manager = new CheckpointManager();

    public CheckpointAccessor() {
        super(Checkpoint.class);
    }

    protected CheckpointManager getManager() {
        return manager;
    }
}
