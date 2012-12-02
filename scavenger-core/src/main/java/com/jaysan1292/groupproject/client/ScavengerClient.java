package com.jaysan1292.groupproject.client;

import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import java.net.URI;

import static com.jaysan1292.groupproject.client.accessors.Accessors.*;

//TODO: Authentication

/** @author Jason Recillo */
@SuppressWarnings("MethodMayBeStatic")
public class ScavengerClient {
    private final Client client;
    private final WebResource root;

    public ScavengerClient() {
        client = Client.create();
        URI host = getDefaultHost();
        root = client.resource(host);
        setHost(host);
    }

    public ScavengerClient(URI host) {
        client = Client.create();
        root = client.resource(host);
    }

    //region /players

    public Player getPlayer(long id) throws GeneralServiceException {
        return getPlayerAccessor().get(id);
    }

    public void updatePlayer(Player player) throws GeneralServiceException {
        getPlayerAccessor().update(player);
    }

    public Player createPlayer(Player player) throws GeneralServiceException {
        return getPlayerAccessor().create(player);
    }

    public void deletePlayer(Player player) throws GeneralServiceException {
        getPlayerAccessor().delete(player);
    }

    //endregion
}
