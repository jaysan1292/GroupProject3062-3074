package com.jaysan1292.groupproject.client.accessors;

import com.jaysan1292.groupproject.data.Team;
import com.sun.jersey.api.client.Client;

import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * Date: 28/11/12
 * Time: 9:14 PM
 *
 * @author Jason Recillo
 */
public class TeamClientAccessor extends AbstractClientAccessor<Team> {
    protected TeamClientAccessor(Client client) {
        super(Team.class, client, client.resource(Accessors.getDefaultHost(client)).path("teams"));
    }

    protected TeamClientAccessor(URI host, Client client) {
        super(Team.class, client, client.resource(host).path("teams"));
    }
}
