package com.jaysan1292.groupproject.android.net;

import com.jaysan1292.groupproject.android.MobileAppCommon;
import com.jaysan1292.groupproject.data.*;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.jaysan1292.groupproject.service.security.AuthorizationException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import java.net.URI;

import static com.jaysan1292.groupproject.android.net.accessors.Accessors.*;

/**
 * Created with IntelliJ IDEA.
 * Date: 11/12/12
 * Time: 8:49 PM
 *
 * @author Jason Recillo
 */
@SuppressWarnings("MethodMayBeStatic")
public class ScavengerClient {
    private final Client client;
    private final WebResource root;

    private boolean credentialsSet;

    public ScavengerClient() throws AuthorizationException {
        this(null);
    }

    public ScavengerClient(URI host) throws AuthorizationException {
        this(host, null, null);
    }

    public ScavengerClient(String username, String password) throws AuthorizationException {
        this(null, username, password);
    }

    public ScavengerClient(URI host, String username, String password) throws AuthorizationException {
        client = Client.create();
        if (host == null) host = getDefaultHost(client);
        if ((username != null) && (password != null)) {
            client.addFilter(new HTTPBasicAuthFilter(username, password));
            credentialsSet = true;
        }
        setHost(client, host);
        MobileAppCommon.log.info("Using service located at " + host.toString());
        root = client.resource(host);

        onCreate();
    }

    private void onCreate() throws AuthorizationException {
        // check authentication;
        // service returns 200 OK if credentials are valid
        // service returns 400 Bad Request if credentials are invalid
        if (credentialsSet) {
            ClientResponse response = root.path("auth").get(ClientResponse.class);
            if (response.getStatus() != 200) {
                throw new AuthorizationException("Invalid credentials!");
            }
        }
    }

    public Player getPlayer(long id) throws GeneralServiceException {
        return getPlayerAccessor().get(id);
    }

    public ScavengerHunt getScavengerHunt(long id) throws GeneralServiceException {
        return getScavengerHuntAccessor().get(id);
    }

    public Challenge getChallenge(long id) throws GeneralServiceException {
        return getChallengeAccessor().get(id);
    }

    public Checkpoint getCheckpoint(long id) throws GeneralServiceException {
        return getCheckpointAccessor().get(id);
    }

    public Path getPath(long id) throws GeneralServiceException {
        return getPathAccessor().get(id);
    }

    public Team getTeam(long id) throws GeneralServiceException {
        return getTeamAccessor().get(id);
    }
}
