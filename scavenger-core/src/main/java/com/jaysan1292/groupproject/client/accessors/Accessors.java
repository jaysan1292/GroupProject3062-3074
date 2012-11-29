package com.jaysan1292.groupproject.client.accessors;

import com.google.common.base.Preconditions;

import java.net.URI;

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
    private static PlayerAccessor players;
    private static ScavengerHuntAccessor scavengerHunts;
    private static TeamAccessor teams;

    private static boolean _hostSet;

    private Accessors() {}

    public static void setHost(URI host) {
        _hostSet = true;
    }

    public static ChallengeAccessor getChallengeAccessor() {
        Preconditions.checkState(_hostSet, "Service host was not set!");
        if (challenges == null) challenges = new ChallengeAccessor();
        return challenges;
    }

    public static CheckpointAccessor getCheckpointAccessor() {
        Preconditions.checkState(_hostSet, "Service host was not set!");
        if (checkpoints == null) checkpoints = new CheckpointAccessor();
        return checkpoints;
    }

    public static PathAccessor getPathAccessor() {
        Preconditions.checkState(_hostSet, "Service host was not set!");
        if (paths == null) paths = new PathAccessor();
        return paths;
    }

    public static PlayerAccessor getPlayerAccessor() {
        Preconditions.checkState(_hostSet, "Service host was not set!");
        if (players == null) players = new PlayerAccessor();
        return players;
    }

    public static ScavengerHuntAccessor getScavengerHuntAccessor() {
        Preconditions.checkState(_hostSet, "Service host was not set!");
        if (scavengerHunts == null) scavengerHunts = new ScavengerHuntAccessor();
        return scavengerHunts;
    }

    public static TeamAccessor getTeamAccessor() {
        Preconditions.checkState(_hostSet, "Service host was not set!");
        if (teams == null) teams = new TeamAccessor();
        return teams;
    }
}
