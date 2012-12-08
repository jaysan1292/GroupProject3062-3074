package com.jaysan1292.groupproject.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/** @author Jason Recillo */
public class Checkpoint extends BaseEntity {
    public static final Checkpoint INVALID = new Checkpoint(Long.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE, new Challenge());
    /** The checkpoint ID. Corresponds with the ID for this entry in the database. */
    private long checkpointId;

    /** The latitude coordinates of this checkpoint. */
    private float latitude;

    /** The longitude coordinates of this checkpoint. */
    private float longitude;

    /** Whether or not this checkpoint is visible to the team or not. */
    private boolean visible;

    /** Whether or not this checkpoint is the last one in the path. */
    private boolean end;

    /** The challenge that resides at this particular Checkpoint. */
    private Challenge challenge;

    public Checkpoint() {
        this(INVALID);
    }

    protected Checkpoint(long checkpointId, float latitude, float longitude, Challenge challenge, boolean visible, boolean end) {
        this.checkpointId = checkpointId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.challenge = challenge;
        this.visible = visible;
        this.end = end;
    }

    public Checkpoint(long checkpointId, float latitude, float longitude) {
        this(checkpointId, latitude, longitude, new Challenge(), false, false);
    }

    public Checkpoint(long checkpointId, float latitude, float longitude, Challenge challenge) {
        this(checkpointId, latitude, longitude, challenge, false, false);
    }

    public Checkpoint(Checkpoint other) {
        this(other.checkpointId, other.latitude, other.longitude, other.challenge, other.visible, other.end);
    }

    //region JavaBean

    public long getId() {
        return checkpointId;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    @JsonIgnore
    public boolean isVisible() {
        return visible;
    }

    @JsonIgnore
    public boolean isEnd() {
        return end;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setId(long checkpointId) {
        this.checkpointId = checkpointId;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    protected void setEnd(boolean end) {
        this.end = end;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    //endregion JavaBean

    public String description() {
        return String.format("CHPT%02d: %.6f lat, %.6f lon",
                             checkpointId,
                             latitude,
                             longitude);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Checkpoint)) return false;

        Checkpoint other = (Checkpoint) obj;
        return (checkpointId == other.checkpointId) &&
               (Float.compare(latitude, other.latitude) == 0) &&
               (Float.compare(longitude, other.longitude) == 0);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(93, 35)
                .append(checkpointId)
                .append(latitude)
                .append(longitude)
                .append(visible)
                .append(end)
                .toHashCode();
    }
}
