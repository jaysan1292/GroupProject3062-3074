package com.jaysan1292.groupproject.data;

import java.util.Date;

public class ScavengerHuntBuilder extends AbstractBuilder<ScavengerHunt> {
    private ScavengerHunt scavengerHunt;

    public ScavengerHuntBuilder() {
        init();
    }

    public ScavengerHuntBuilder(ScavengerHunt scavengerHunt) {
        this.scavengerHunt = scavengerHunt;
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

    public ScavengerHuntBuilder setStartTime(Date start) {
        scavengerHunt.setStartTime(start);
        return this;
    }

    public ScavengerHuntBuilder setFinishTime(Date finish) {
        scavengerHunt.setFinishTime(finish);
        return this;
    }
}
