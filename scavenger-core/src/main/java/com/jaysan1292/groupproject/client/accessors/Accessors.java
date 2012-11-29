package com.jaysan1292.groupproject.client.accessors;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.jaysan1292.groupproject.client.Global;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Date: 28/11/12
 * Time: 6:46 PM
 *
 * @author Jason Recillo
 */
public class Accessors {
    private static ChallengeAccessor challenges;
    private static CheckpointAccessor checkpoints;
    private static PathAccessor paths;
    private static PlayerClientAccessor players;
    private static ScavengerHuntAccessor scavengerHunts;
    private static TeamAccessor teams;

    private static boolean _hostSet;
    private static URI _hostUri;

    private Accessors() {}

    public static void setHost(URI host) {
        _hostUri = host;
        _hostSet = true;
    }

    public static ChallengeAccessor getChallengeAccessor() {
        Preconditions.checkState(_hostSet, "Service host was not set!");
        if (challenges == null) challenges = new ChallengeAccessor(_hostUri);
        return challenges;
    }

    public static CheckpointAccessor getCheckpointAccessor() {
        Preconditions.checkState(_hostSet, "Service host was not set!");
        if (checkpoints == null) checkpoints = new CheckpointAccessor(_hostUri);
        return checkpoints;
    }

    public static PathAccessor getPathAccessor() {
        Preconditions.checkState(_hostSet, "Service host was not set!");
        if (paths == null) paths = new PathAccessor(_hostUri);
        return paths;
    }

    public static PlayerClientAccessor getPlayerAccessor() {
        Preconditions.checkState(_hostSet, "Service host was not set!");
        if (players == null) players = new PlayerClientAccessor(_hostUri);
        return players;
    }

    public static ScavengerHuntAccessor getScavengerHuntAccessor() {
        Preconditions.checkState(_hostSet, "Service host was not set!");
        if (scavengerHunts == null) scavengerHunts = new ScavengerHuntAccessor(_hostUri);
        return scavengerHunts;
    }

    public static TeamAccessor getTeamAccessor() {
        Preconditions.checkState(_hostSet, "Service host was not set!");
        if (teams == null) teams = new TeamAccessor(_hostUri);
        return teams;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    private static final List<URI> hosts = Lists.newArrayList(
            URI.create("http://jaysan1292.com:9000/service"),
            URI.create("http://localhost:9000/service"));

    public static URI getDefaultHost() {
        Client cli = Client.create();
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
