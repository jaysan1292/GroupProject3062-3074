package com.jaysan1292.groupproject.client.accessors;

import com.google.common.collect.Lists;
import com.jaysan1292.groupproject.client.Global;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import org.apache.commons.lang3.StringUtils;

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

    private static boolean _hostSet;
    private static URI _hostUri;

    private Accessors() {}

    public static void setHost(URI host) {
        _hostUri = host;
        _hostSet = true;
    }

    public static URI getHost() {
        return _hostUri;
    }

    public static ChallengeClientAccessor getChallengeAccessor() {
        checkState(_hostSet, "Service host was not set!");
        if (challenges == null) challenges = new ChallengeClientAccessor(_hostUri);
        return challenges;
    }

    public static CheckpointClientAccessor getCheckpointAccessor() {
        checkState(_hostSet, "Service host was not set!");
        if (checkpoints == null) checkpoints = new CheckpointClientAccessor(_hostUri);
        return checkpoints;
    }

    public static PathClientAccessor getPathAccessor() {
        checkState(_hostSet, "Service host was not set!");
        if (paths == null) paths = new PathClientAccessor(_hostUri);
        return paths;
    }

    public static PlayerClientAccessor getPlayerAccessor() {
        checkState(_hostSet, "Service host was not set!");
        if (players == null) players = new PlayerClientAccessor(_hostUri);
        return players;
    }

    public static ScavengerHuntClientAccessor getScavengerHuntAccessor() {
        checkState(_hostSet, "Service host was not set!");
        if (scavengerHunts == null) scavengerHunts = new ScavengerHuntClientAccessor(_hostUri);
        return scavengerHunts;
    }

    public static TeamClientAccessor getTeamAccessor() {
        checkState(_hostSet, "Service host was not set!");
        if (teams == null) teams = new TeamClientAccessor(_hostUri);
        return teams;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    private static final List<URI> hosts = Lists.newArrayList(
            URI.create("http://jaysan1292.com:9000/service"),
            URI.create("http://localhost:9000/service"));

    public static URI getDefaultHost(Client cli) {
        String wadl = "";
        for (URI host : hosts) {
            try {
                wadl = cli.resource(host).path("application.wadl").get(String.class);
            } catch (ClientHandlerException ignored) {}
            if (!StringUtils.isBlank(wadl)) {
                Global.log.info("Using service located at " + host.toString());
                return host;
            }
        }
        throw new RuntimeException("The web service appears to be inaccessible.");
    }
}
