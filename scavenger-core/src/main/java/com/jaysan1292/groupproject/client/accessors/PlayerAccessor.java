package com.jaysan1292.groupproject.client.accessors;

import com.jaysan1292.groupproject.data.Player;
import com.sun.jersey.api.client.Client;

import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * Date: 28/11/12
 * Time: 5:53 PM
 *
 * @author Jason Recillo
 */
public class PlayerAccessor extends AbstractAccessor<Player> {
    private static final Client client = Client.create();

    protected PlayerAccessor() {
        super(Player.class, client.resource(getDefaultHost()).path("players"));
    }

    protected PlayerAccessor(URI host) {
        super(Player.class, client.resource(host).path("players"));
    }
}
