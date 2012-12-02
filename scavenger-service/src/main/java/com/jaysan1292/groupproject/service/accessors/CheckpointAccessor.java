package com.jaysan1292.groupproject.service.accessors;

import com.jaysan1292.groupproject.data.Checkpoint;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.jaysan1292.groupproject.service.db.CheckpointManager;
import org.apache.commons.lang3.math.NumberUtils;

import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;

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

    protected void doUpdate(Checkpoint item, MultivaluedMap<String, String> newValues) throws GeneralServiceException {
        boolean update = false;
        if (newValues.containsKey(LATITUDE)) {
            item.setLatitude(NumberUtils.toFloat(newValues.getFirst(LATITUDE)));
            update = true;
        }
        if (newValues.containsKey(LONGITUDE)) {
            item.setLongitude(NumberUtils.toFloat(newValues.getFirst(LONGITUDE)));
            update = true;
        }
        if (newValues.containsKey(CHALLENGE)) {
            item.setChallenge(ChallengeAccessor.manager.get(NumberUtils.toLong(CHALLENGE)));
            update = true;
        }
        if (update) {
            manager.update(item);
        }
    }
}
