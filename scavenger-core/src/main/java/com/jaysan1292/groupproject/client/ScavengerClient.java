package com.jaysan1292.groupproject.client;

import com.jaysan1292.groupproject.client.accessors.AbstractAccessor;
import com.jaysan1292.groupproject.client.accessors.PlayerAccessor;
import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import java.net.URI;

//TODO: Authentication

/** @author Jason Recillo */
public class ScavengerClient {
    private final Client client;
    private final WebResource root;

    public ScavengerClient() {
        client = Client.create();
        root = client.resource(getDefaultHost());
    }

    public ScavengerClient(URI host) {
        client = Client.create();
        root = client.resource(host);
    }

    //region /players

    public Player get(long id) throws GeneralServiceException {
        return new PlayerAccessor().get(id);
    }

    //endregion

    protected static URI getDefaultHost() {
        return AbstractAccessor.getDefaultHost();
    }
}
