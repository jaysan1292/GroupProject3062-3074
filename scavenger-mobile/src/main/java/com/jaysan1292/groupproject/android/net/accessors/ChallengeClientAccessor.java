package com.jaysan1292.groupproject.android.net.accessors;

import com.jaysan1292.groupproject.data.Challenge;
import com.sun.jersey.api.client.Client;

import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * Date: 28/11/12
 * Time: 9:01 PM
 *
 * @author Jason Recillo
 */
public class ChallengeClientAccessor extends AbstractClientAccessor<Challenge> {
    protected ChallengeClientAccessor(Client client) {
        super(Challenge.class, client, client.resource(Accessors.getDefaultHost(client)).path("challenges"));
    }

    protected ChallengeClientAccessor(URI host, Client client) {
        super(Challenge.class, client, client.resource(host).path("challenges"));
    }
}
