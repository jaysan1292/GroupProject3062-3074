package com.jaysan1292.groupproject.client;

import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import java.net.URI;
import java.util.List;

import static com.jaysan1292.groupproject.client.accessors.Accessors.*;

/** @author Jason Recillo */
@SuppressWarnings("MethodMayBeStatic")
public class ScavengerClient {
    private final Client client;
    private final WebResource root;

    private boolean administrator;

    public ScavengerClient() {
        this(null);
    }

    public ScavengerClient(URI host) {
        this(host, null, null);
    }

    public ScavengerClient(String username, String password) {
        this(null, username, password);
    }

    public ScavengerClient(URI host, String username, String password) {
        client = Client.create();
        if (host == null) host = getDefaultHost(client);
        if ((username != null) && (password != null)) {
            client.addFilter(new HTTPBasicAuthFilter(username, password));
        }
        setHost(client, host);
        root = client.resource(host);

        onCreate();
    }

    private void onCreate() {
        //TODO: Check if admin
    }

    //region /players

    public Player getPlayer(long id) throws GeneralServiceException {
        return getPlayerAccessor().get(id);
    }

    public List<Player> getAllPlayers() throws GeneralServiceException {
        return getPlayerAccessor().getAll();
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
