package com.jaysan1292.groupproject.android.net;

import com.jaysan1292.groupproject.android.MobileAppCommon;
import com.jaysan1292.groupproject.android.util.HttpClientUtils;
import com.jaysan1292.groupproject.data.*;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.jaysan1292.groupproject.service.security.AuthorizationException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
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
    private final DefaultHttpClient client;
    private final URI root;
    private Header authorizationHeader;
//    private final Client client;
//    private final WebResource root;

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
        client = new DefaultHttpClient();
        if (host == null) host = getDefaultHost(client);
        if ((username != null) && (password != null)) {
            authorizationHeader = HttpClientUtils.createAuthHeader(username, password);
//            CredentialsProvider provider = new BasicCredentialsProvider();
//            provider.setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
//                                    new UsernamePasswordCredentials(username, password));
//            client.setCredentialsProvider(provider);
            credentialsSet = true;
        }
        setHost(client, host, authorizationHeader);

        MobileAppCommon.log.info("Using service located at " + host.toString());
        root = host;

        onCreate();
    }

    private void onCreate() throws AuthorizationException {
        // check authentication;
        // service returns 200 OK if credentials are valid
        // service returns 400 Bad Request if credentials are invalid
        if (credentialsSet) {
//            ClientResponse response = root.path("auth").get(ClientResponse.class);
            URI authUri = URI.create(root.toString() + "/auth");
            HttpGet request = HttpClientUtils.createGetRequest(authUri, authorizationHeader);
            HttpResponse response;
            try {
                response = client.execute(request);

                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new AuthorizationException("Invalid credentials!");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            MobileAppCommon.setClient(this);
        }
    }

    public Player getPlayer(long id) throws GeneralServiceException {
        return getPlayerAccessor().get(id);
    }

    public Player getPlayer(String studentId) throws GeneralServiceException {
        return getPlayerAccessor().getPlayer(studentId);
    }

    public ScavengerHunt getScavengerHunt(long id) throws GeneralServiceException {
        return getScavengerHuntAccessor().get(id);
    }

    public ScavengerHunt getScavengerHunt(Player player) throws GeneralServiceException {
        return getScavengerHuntAccessor().getScavengerHunt(player);
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
