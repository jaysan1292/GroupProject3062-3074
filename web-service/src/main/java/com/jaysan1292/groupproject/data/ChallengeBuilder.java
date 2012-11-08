package com.jaysan1292.groupproject.data;

/** @author Jason Recillo */
public class ChallengeBuilder extends AbstractBuilder<Challenge> {
    private Challenge challenge;

    public ChallengeBuilder() {
        init();
    }

    public ChallengeBuilder(Challenge challenge) {
        this.challenge = challenge;
    }

    protected void init() {
        challenge = new Challenge();
    }

    public Challenge build() {
        return challenge;
    }

    public ChallengeBuilder setChallengeId(long challengeId) {
        challenge.setChallengeId(challengeId);
        return this;
    }

    public ChallengeBuilder setChallengeText(String challengeText) {
        challenge.setChallengeText(challengeText);
        return this;
    }
}
