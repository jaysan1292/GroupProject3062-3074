package com.jaysan1292.groupproject.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private long startTime;

    /** The time that this team finished the Scavenger Hunt. */
    private long finishTime;

    public ScavengerHunt() {
        this(INVALID);
    }

    public ScavengerHunt(long scavengerHuntId, Team team, Path path, DateTime startTime, DateTime finishTime) {
        this.scavengerHuntId = scavengerHuntId;
        this.team = team;
        this.path = path;
        this.startTime = startTime.getMillis();
        this.finishTime = finishTime.getMillis();
    }

    public ScavengerHunt(ScavengerHunt other) {
        this(other.scavengerHuntId, other.team, other.path, other.getStartTime(), other.getFinishTime());
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

    @JsonIgnore
    public DateTime getStartTime() {
        return new DateTime(startTime);
    }

    @JsonIgnore
    public DateTime getFinishTime() {
        return new DateTime(finishTime);
    }

    public void setScavengerHuntId(long scavengerHuntId) {
        this.scavengerHuntId = scavengerHuntId;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    @JsonIgnore
    public void setStartTime(DateTime startTime) {
        this.startTime = startTime.getMillis();
    }

    @JsonIgnore
    public void setFinishTime(DateTime finishTime) {
        this.finishTime = finishTime.getMillis();
    }

    //endregion JavaBean

    /** This is only meant for proper JSON serialization. Use getStartTime() instead. */
    public long getStartTimeMillis() {
        return startTime;
    }

    /** This is only meant for proper JSON serialization. Use getFinishTime() instead. */
    public long getFinishTimeMillis() {
        return finishTime;
    }

    /** This is only meant for proper JSON serialization. Use setStartTime(DateTime) instead. */
    public void setStartTimeMillis(long startTime) {
        this.startTime = startTime;
    }

    /** This is only mean for proper JSON serialization. Use setFinishTime(DateTime) instead. */
    public void setFinishTimeMillis(long finishTime) {
        this.finishTime = finishTime;
    }

    public String description() {
        return String.format("SCHT%02d: %d %s, %s completed",
                             scavengerHuntId,
                             team.getTeamMembers().size(),
                             (team.getTeamMembers().size() != 1) ? "members" : "member",
                             completionStatus());
    }

    private String completionStatus() {
        float checkpoints = path.getCheckpoints().size();
        //TODO: Completion status; i.e., percent of checkpoints completed
        return "0%";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ScavengerHunt)) return false;
        ScavengerHunt other = (ScavengerHunt) obj;
        return (scavengerHuntId == other.scavengerHuntId) &&
               team.equals(other.team) &&
               path.equals(other.path) &&
               (startTime == other.startTime) &&
               (finishTime == other.finishTime);
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
