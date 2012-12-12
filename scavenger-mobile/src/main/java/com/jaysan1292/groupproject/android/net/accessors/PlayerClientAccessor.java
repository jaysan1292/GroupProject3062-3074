package com.jaysan1292.groupproject.android.net.accessors;

import com.jaysan1292.groupproject.android.MobileAppCommon;
import com.jaysan1292.groupproject.data.Player;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

import java.io.IOException;
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

    public Player getPlayer(String studentId) {
        try {
            ClientResponse response = _res.path("studentnumbers/" + studentId).get(ClientResponse.class);
            return new Player().readJSON(response.getEntity(String.class));
        } catch (IOException e) {
            MobileAppCommon.log.error(e.getMessage(), e);
            return null;
        }
    }
}
