package com.jaysan1292.groupproject.data;

/** User: Jason Recillo */
public class CheckpointBuilder extends AbstractBuilder<Checkpoint> {
    private Checkpoint checkpoint;

    public CheckpointBuilder() {
        init();
    }

    public CheckpointBuilder(Checkpoint checkpoint) {
        this.checkpoint = checkpoint;
    }

    protected void init() {
        checkpoint = new Checkpoint();
    }

    public Checkpoint build() {
        return checkpoint;
    }

    public CheckpointBuilder setCheckpointId(long checkpointId) {
        checkpoint.setId(checkpointId);
        return this;
    }

    public CheckpointBuilder setLatitude(float latitude) {
        checkpoint.setLatitude(latitude);
        return this;
    }

    public CheckpointBuilder setLongitude(float longitude) {
        checkpoint.setLongitude(longitude);
        return this;
    }

    public CheckpointBuilder setChallenge(Challenge challenge) {
        checkpoint.setChallenge(challenge);
        return this;
    }
}
