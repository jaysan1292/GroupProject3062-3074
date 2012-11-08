package com.jaysan1292.groupproject.data;

import java.util.ArrayList;

/** @author Jason Recillo */
public class PathBuilder extends AbstractBuilder<Path> {
    private Path path;

    public PathBuilder() {
        init();
    }

    public PathBuilder(Path path) {
        this.path = path;
    }

    protected void init() {
        path = new Path();
    }

    public Path build() {
        return path;
    }

    public PathBuilder setPathId(long pathId) {
        path.setPathId(pathId);
        return this;
    }

    public PathBuilder setCheckpoints(ArrayList<Checkpoint> checkpoints) {
        path.setCheckpoints(checkpoints);
        return this;
    }
}
