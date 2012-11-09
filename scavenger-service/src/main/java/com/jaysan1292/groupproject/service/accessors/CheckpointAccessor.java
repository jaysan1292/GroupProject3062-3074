package com.jaysan1292.groupproject.service.accessors;

import com.jaysan1292.groupproject.data.Checkpoint;
import com.jaysan1292.groupproject.service.db.CheckpointManager;
import com.jaysan1292.groupproject.util.JsonMap;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/** @author Jason Recillo */
@Path("/checkpoints")
public class CheckpointAccessor extends AbstractAccessor<Checkpoint> {
    private static final CheckpointManager manager = new CheckpointManager();

    protected CheckpointManager getManager() {
        return manager;
    }

    protected Response doUpdate(long id, JsonMap map) {
        return null;  //TODO: Auto-generated method stub
    }
}
