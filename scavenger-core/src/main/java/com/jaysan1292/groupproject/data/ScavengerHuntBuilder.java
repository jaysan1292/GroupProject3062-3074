package com.jaysan1292.groupproject.data;

import org.joda.time.DateTime;

public class ScavengerHuntBuilder extends AbstractBuilder<ScavengerHunt> {
    private ScavengerHunt scavengerHunt;

    public ScavengerHuntBuilder() {
        init();
    }

    public ScavengerHuntBuilder(ScavengerHunt scavengerHunt) {
        this.scavengerHunt = new ScavengerHunt(scavengerHunt);
    }

    protected void init() {
        scavengerHunt = new ScavengerHunt();
    }

    public ScavengerHunt build() {
        return scavengerHunt;
    }

    public ScavengerHuntBuilder setScavengerHuntId(long id) {
        scavengerHunt.setScavengerHuntId(id);
        return this;
    }

    public ScavengerHuntBuilder setTeam(Team team) {
        scavengerHunt.setTeam(team);
        return this;
    }

    public ScavengerHuntBuilder setPath(Path path) {
        scavengerHunt.setPath(path);
        return this;
    }

    public ScavengerHuntBuilder setStartTime(DateTime start) {
        scavengerHunt.setStartTime(start);
        return this;
    }

    public ScavengerHuntBuilder setFinishTime(DateTime finish) {
        scavengerHunt.setFinishTime(finish);
        return this;
    }
}
