package com.jaysan1292.groupproject.data;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;

/** @author Jason Recillo */
public final class ScavengerHunt extends BaseEntity {
    public static final ScavengerHunt INVALID = new ScavengerHunt(-1, new Team(), new Path(), new DateTime(0), new DateTime(0));
    /**
     * The ID for this particular instance of the scavenger hunt.
     * Corresponds with the ID for this entry in the database.
     */
    private long scavengerHuntId;

    /** The team participating in this instance of the scavenger hunt. */
    private Team team;

    /**
     * The path from the starting point to the ending point.
     * This will be updated whenever there is a change in the path made
     * by the administration.
     */
    private Path path;

    /** The time that this team started the Scavenger Hunt. */
    private DateTime startTime;

    /** The time that this team finished the Scavenger Hunt. */
    private DateTime finishTime;

    public ScavengerHunt() {
        this(INVALID);
    }

    public ScavengerHunt(long scavengerHuntId, Team team, Path path, DateTime startTime, DateTime finishTime) {
        this.scavengerHuntId = scavengerHuntId;
        this.team = team;
        this.path = path;
        this.startTime = startTime;
        this.finishTime = finishTime;
    }

    public ScavengerHunt(ScavengerHunt other) {
        this(other.scavengerHuntId, other.team, other.path, other.startTime, other.finishTime);
    }

    //region JavaBean

    public long getId() {
        return scavengerHuntId;
    }

    public void setId(long id) {
        scavengerHuntId = id;
    }

    public Team getTeam() {
        return team;
    }

    public Path getPath() {
        return path;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public DateTime getFinishTime() {
        return finishTime;
    }

    protected void setScavengerHuntId(long scavengerHuntId) {
        this.scavengerHuntId = scavengerHuntId;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    protected void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    protected void setFinishTime(DateTime finishTime) {
        this.finishTime = finishTime;
    }
//endregion JavaBean

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ScavengerHunt)) return false;
        ScavengerHunt other = (ScavengerHunt) obj;
        return (scavengerHuntId == other.scavengerHuntId) &&
               team.equals(other.team) &&
               path.equals(other.path) &&
               startTime.equals(other.startTime) &&
               finishTime.equals(other.finishTime);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(47, 95)
                .append(scavengerHuntId)
                .append(team)
                .append(path)
                .append(startTime)
                .append(finishTime)
                .toHashCode();
    }
}
