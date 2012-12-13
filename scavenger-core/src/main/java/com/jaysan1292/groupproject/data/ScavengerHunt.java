package com.jaysan1292.groupproject.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/** @author Jason Recillo */
public final class ScavengerHunt extends BaseEntity {
    public static final ScavengerHunt INVALID = new ScavengerHunt(-1, new Team(), new Path(),
                                                                  new ArrayList<Checkpoint>(),
                                                                  new DateTime(0), new DateTime(0));
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

    /** Keeps track of completed checkpoints. */
    private List<Checkpoint> completedCheckpoints;

    /** The time that this team started the Scavenger Hunt. */
    private long startTime;

    /** The time that this team finished the Scavenger Hunt. */
    private long finishTime;

    public ScavengerHunt() {
        this(INVALID);
    }

    public ScavengerHunt(long scavengerHuntId, Team team, Path path, List<Checkpoint> completedCheckpoints,
                         DateTime startTime, DateTime finishTime) {
        this.scavengerHuntId = scavengerHuntId;
        this.team = team;
        this.path = path;
        this.completedCheckpoints = completedCheckpoints;
        this.startTime = startTime.getMillis();
        this.finishTime = finishTime.getMillis();
    }

    public ScavengerHunt(ScavengerHunt other) {
        this(other.scavengerHuntId, other.team, other.path, other.completedCheckpoints, other.getStartTime(), other.getFinishTime());
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

    public List<Checkpoint> getCompletedCheckpoints() {
        return completedCheckpoints;
    }

    @JsonIgnore
    public String getCompletedCheckpointsAsString() {
        List<Long> cids = Lists.newArrayList();
        for (Checkpoint checkpoint : completedCheckpoints) {
            cids.add(checkpoint.getId());
        }

        return StringUtils.join(cids, ',');
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

    public void setCompletedCheckpoints(List<Checkpoint> completedCheckpoints) {
        this.completedCheckpoints = completedCheckpoints;
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

    /** This is only meant for proper JSON serialization. Use setFinishTime(DateTime) instead. */
    public void setFinishTimeMillis(long finishTime) {
        this.finishTime = finishTime;
    }

    public String description() {
        return String.format("SCHT%02d: %dP, CP(%s) | %s",
                             scavengerHuntId,
                             team.getTeamMembers().size(),
                             path.getCheckpointString(),
                             completionStatus());
    }

    private String completionStatus() {
        int totalCheckpoints = path.getCheckpoints().size();
        int completed = completedCheckpoints.size();

        int percent = (int) Math.floor((completed * 100.0) / totalCheckpoints);
        return String.format("%d%%", percent);
    }

    public void checkIn(Checkpoint checkpoint) throws GeneralServiceException {
        Preconditions.checkArgument(path.getCheckpoints().contains(checkpoint));
        if (completedCheckpoints.contains(checkpoint)) {
            throw new GeneralServiceException("You've already visited this checkpoint!");
        }

        List<Checkpoint> remaining = Lists.newArrayList(path.getCheckpoints());
        Iterables.removeIf(remaining, new Predicate<Checkpoint>() {
            public boolean apply(Checkpoint input) {
                return completedCheckpoints.contains(input);
            }
        });

        if (remaining.indexOf(checkpoint) != 0) {
            throw new GeneralServiceException("This isn't your currently assigned checkpoint!");
        }
        completedCheckpoints.add(checkpoint);
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
