package com.jaysan1292.groupproject.client.accessors;

import com.jaysan1292.groupproject.data.Challenge;

import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * Date: 28/11/12
 * Time: 9:01 PM
 *
 * @author Jason Recillo
 */
public class ChallengeClientAccessor extends AbstractClientAccessor<Challenge> {
    protected ChallengeClientAccessor() {
        super(Challenge.class, client.resource(Accessors.getDefaultHost()).path("challenges"));
    }

    protected ChallengeClientAccessor(URI host) {
        super(Challenge.class, client.resource(host).path("challenges"));
    }
}
