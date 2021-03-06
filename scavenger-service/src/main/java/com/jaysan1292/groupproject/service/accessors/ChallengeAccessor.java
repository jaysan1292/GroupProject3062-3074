package com.jaysan1292.groupproject.service.accessors;

import com.jaysan1292.groupproject.data.Challenge;
import com.jaysan1292.groupproject.service.db.ChallengeManager;

import javax.ws.rs.Path;

/** @author Jason Recillo */
@Path("/challenges")
public class ChallengeAccessor extends AbstractAccessor<Challenge> {
    protected static final ChallengeManager manager = new ChallengeManager();
    private static final String CHALLENGE_TEXT = "challengeText";

    public ChallengeAccessor() {
        super(Challenge.class);
    }

    protected ChallengeManager getManager() {
        return manager;
    }
}
