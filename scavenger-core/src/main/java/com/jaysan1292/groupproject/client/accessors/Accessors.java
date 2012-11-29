package com.jaysan1292.groupproject.client.accessors;

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

    private Accessors() {}

    public static ChallengeAccessor getChallengeAccessor() {
        if (challenges == null) challenges = new ChallengeAccessor();
        return challenges;
    }

    public static CheckpointAccessor getCheckpointAccessor() {
        if (checkpoints == null) checkpoints = new CheckpointAccessor();
        return checkpoints;
    }

    public static PathAccessor getPathAccessor() {
        if (paths == null) paths = new PathAccessor();
        return paths;
    }

    public static PlayerAccessor getPlayerAccessor() {
        if (players == null) players = new PlayerAccessor();
        return players;
    }

    public static ScavengerHuntAccessor getScavengerHuntAccessor() {
        if (scavengerHunts == null) scavengerHunts = new ScavengerHuntAccessor();
        return scavengerHunts;
    }

    public static TeamAccessor getTeamAccessor() {
        if (teams == null) teams = new TeamAccessor();
        return teams;
    }
}
