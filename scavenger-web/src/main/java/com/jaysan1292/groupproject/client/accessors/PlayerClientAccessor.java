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
    protected PlayerClientAccessor(Client client) {
        super(Player.class, client, client.resource(Accessors.getDefaultHost(client)).path("players"));
    }

    protected PlayerClientAccessor(URI host, Client client) {
        super(Player.class, client, client.resource(host).path("players"));
    }
}
