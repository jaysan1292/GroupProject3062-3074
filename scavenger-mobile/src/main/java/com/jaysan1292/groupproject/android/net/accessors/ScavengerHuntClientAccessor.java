package com.jaysan1292.groupproject.android.net.accessors;

import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.data.ScavengerHunt;
import com.sun.jersey.api.client.Client;

import java.io.IOException;
import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * Date: 28/11/12
 * Time: 9:13 PM
 *
 * @author Jason Recillo
 */
public class ScavengerHuntClientAccessor extends AbstractClientAccessor<ScavengerHunt> {
    protected ScavengerHuntClientAccessor(Client client) {
        super(ScavengerHunt.class, client, client.resource(Accessors.getDefaultHost(client)).path("scavengerhunts"));
    }

    protected ScavengerHuntClientAccessor(URI host, Client client) {
        super(ScavengerHunt.class, client, client.resource(host).path("scavengerhunts"));
    }

    public ScavengerHunt getScavengerHunt(Player player) {
        try {
            return new ScavengerHunt().readJSON(_res.path("players/" + player.getId()).get(String.class));
        } catch (IOException e) {
            return null;
        }
    }
}
