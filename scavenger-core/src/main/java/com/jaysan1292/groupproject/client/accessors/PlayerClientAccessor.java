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
public class PlayerClientAccessor extends AbstractClientAccessor<Player> {
    private static final Client client = Client.create();

    protected PlayerClientAccessor() {
        super(Player.class, client.resource(Accessors.getDefaultHost()).path("players"));
    }

    protected PlayerClientAccessor(URI host) {
        super(Player.class, client.resource(host).path("players"));
    }
}
