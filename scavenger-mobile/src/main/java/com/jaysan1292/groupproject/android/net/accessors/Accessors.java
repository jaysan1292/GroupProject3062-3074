package com.jaysan1292.groupproject.android.net.accessors;

import com.google.common.collect.Lists;
import com.jaysan1292.groupproject.android.util.HttpClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;

/**
 * Created with IntelliJ IDEA.
 * Date: 28/11/12
 * Time: 6:46 PM
 *
 * @author Jason Recillo
 */
public class Accessors {
    private static ChallengeClientAccessor challenges;
    private static CheckpointClientAccessor checkpoints;
    private static PathClientAccessor paths;
    private static PlayerClientAccessor players;
    private static ScavengerHuntClientAccessor scavengerHunts;
    private static TeamClientAccessor teams;

    private static boolean _ready;
    private static URI _hostUri;
    private static DefaultHttpClient _client;
    private static Header _authHeader;

    private Accessors() {}

    public static void setHost(DefaultHttpClient client, URI host, Header authHeader) {
        _client = client;
        _hostUri = host;
        _authHeader = authHeader;
        _ready = true;
    }

    public static void logout() {
        _client = null;
        _hostUri = null;
        _authHeader = null;
        challenges = null;
        checkpoints = null;
        paths = null;
        players = null;
        scavengerHunts = null;
        teams = null;
        _ready = false;
    }

    public static URI getHost() {
        return _hostUri;
    }

    public static Header getAuthHeader() {
        return _authHeader;
    }

    public static ChallengeClientAccessor getChallengeAccessor() {
        checkState(_ready, "Service host was not set!");
        if (challenges == null) challenges = new ChallengeClientAccessor(_hostUri, _client);
        return challenges;
    }

    public static CheckpointClientAccessor getCheckpointAccessor() {
        checkState(_ready, "Service host was not set!");
        if (checkpoints == null) checkpoints = new CheckpointClientAccessor(_hostUri, _client);
        return checkpoints;
    }

    public static PathClientAccessor getPathAccessor() {
        checkState(_ready, "Service host was not set!");
        if (paths == null) paths = new PathClientAccessor(_hostUri, _client);
        return paths;
    }

    public static PlayerClientAccessor getPlayerAccessor() {
        checkState(_ready, "Service host was not set!");
        if (players == null) players = new PlayerClientAccessor(_hostUri, _client);
        return players;
    }

    public static ScavengerHuntClientAccessor getScavengerHuntAccessor() {
        checkState(_ready, "Service host was not set!");
        if (scavengerHunts == null)
            scavengerHunts = new ScavengerHuntClientAccessor(_hostUri, _client);
        return scavengerHunts;
    }

    public static TeamClientAccessor getTeamAccessor() {
        checkState(_ready, "Service host was not set!");
        if (teams == null) teams = new TeamClientAccessor(_hostUri, _client);
        return teams;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    private static final List<URI> hosts = Lists.newArrayList(
            URI.create("http://jaysan1292.com:9000/service"),
            URI.create("http://localhost:9000/service"));

    @SuppressWarnings("ObjectAllocationInLoop")
    public static URI getDefaultHost(HttpClient cli) {
        String wadl = "";
        for (URI host : hosts) {
            try {
                HttpGet request = HttpClientUtils.createGetRequest(host, _authHeader);
                HttpResponse response = cli.execute(request);
                wadl = HttpClientUtils.getStringEntity(response);
//                wadl = cli.resource(host).path("application.wadl").get(String.class);
            } catch (ClientProtocolException ignored) {} catch (IOException ignored) {
                if (!StringUtils.isBlank(wadl)) {
                    return host;
                }
            }
        }
        throw new RuntimeException("The web service appears to be inaccessible.");
    }
}
