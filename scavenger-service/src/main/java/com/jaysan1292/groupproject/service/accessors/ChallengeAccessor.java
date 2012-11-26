package com.jaysan1292.groupproject.service.accessors;

import com.jaysan1292.groupproject.data.Challenge;
import com.jaysan1292.groupproject.service.db.ChallengeManager;
import com.jaysan1292.groupproject.util.JsonMap;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/** @author Jason Recillo */
@Path("/challenges")
public class ChallengeAccessor extends AbstractAccessor<Challenge> {
    private static final ChallengeManager manager = new ChallengeManager();

    public ChallengeAccessor() {
        super(Challenge.class);
    }

    protected ChallengeManager getManager() {
        return manager;
    }

    protected Response doUpdate(long id, JsonMap map) {
        return null;  //TODO: Auto-generated method stub
    }
}
