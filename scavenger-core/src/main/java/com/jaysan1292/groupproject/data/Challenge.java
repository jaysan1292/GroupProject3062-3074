package com.jaysan1292.groupproject.data;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Located at each {@link Checkpoint}, each challenge
 *
 * @author Jason Recillo
 */
public class Challenge extends BaseEntity {
    public static final Challenge INVALID = new Challenge(-1, "(null)");
    /** The challenge ID. Corresponds with the ID for this entry in the database. */
    private long challengeId;

    /** The challenge to display to the user. */
    private String challengeText;

    public Challenge() {
        this(INVALID);
    }

    public Challenge(long challengeId, String challengeText) {
        this.challengeId = challengeId;
        this.challengeText = challengeText;
    }

    public Challenge(Challenge other) {
        this(other.challengeId, other.challengeText);
    }

    //region JavaBean

    public long getId() {
        return challengeId;
    }

    public void setId(long id) {
        challengeId = id;
    }

    public String getChallengeText() {
        return challengeText;
    }

    protected void setChallengeId(long challengeId) {
        this.challengeId = challengeId;
    }

    public void setChallengeText(String challengeText) {
        this.challengeText = challengeText;
    }

    //endregion JavaBean

    public String description() {
        return String.format("CHAL%02d: %s",
                             challengeId,
                             challengeText);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Challenge)) return false;
        Challenge other = (Challenge) obj;
        return (challengeId == other.challengeId) &&
               (challengeText.equals(other.challengeText));
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(57, 35)
                .append(challengeId)
                .append(challengeText)
                .toHashCode();
    }
}
