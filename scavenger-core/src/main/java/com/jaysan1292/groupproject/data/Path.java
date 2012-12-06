package com.jaysan1292.groupproject.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;

/** @author Jason Recillo */
public class Path extends BaseEntity {
    private static final Logger log = Logger.getLogger(BaseEntity.class);
    public static final Path INVALID = new Path(-1, Lists.<Checkpoint>newArrayList());
    public static final int DIRECTION_UP = -1;
    public static final int DIRECTION_DOWN = 1;

    /** The path ID. Corresponds with the ID for this entry in the database. */
    private long pathId;

    /**
     * The checkpoints in the path.
     * When enumerating through this list (i.e., in a for loop),
     * the checkpoints will be in order from start to finish.
     */
    private List<Checkpoint> checkpoints;

    public Path() {
        this(INVALID);
    }

    public Path(long pathId, List<Checkpoint> checkpoints) {
        this.pathId = pathId;
        this.checkpoints = Lists.newArrayList(checkpoints);
    }

    public Path(List<Checkpoint> checkpoints) {
        this();
        this.checkpoints = Lists.newArrayList(checkpoints);
    }

    public Path(Checkpoint... checkpoints) {
        this();
        this.checkpoints = Lists.newArrayList(checkpoints);
    }

    public Path(Path other) {
        this(other.pathId, other.checkpoints);
    }

    //region JavaBean

    public long getId() {
        return pathId;
    }

    public void setId(long id) {
        pathId = id;
    }

    public String getDescription() {
        return String.format("Path #%d",
                             pathId);
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    protected void setPathId(long pathId) {
        this.pathId = pathId;
    }

    public void setCheckpoints(List<Checkpoint> checkpoints) {
        this.checkpoints = Lists.newArrayList(checkpoints);
    }

    //endregion JavaBean

    public Checkpoint getCheckpoint(int index) {
        return checkpoints.get(index);
    }

    public Path addCheckpoint(Checkpoint checkpoint) {
        checkpoints.add(checkpoint);
        log.info(String.format("Added Checkpoint #%d to Path #%d. New total: %d", checkpoint.getId(), pathId, checkpoints.size()));
        return this;
    }

    public Path removeCheckpoint(Checkpoint checkpoint) {
        if (!checkpoints.contains(checkpoint)) {
            log.info(String.format("Checkpoint #%d does not exist in Path #%d", checkpoint.getId(), pathId));
        }
        checkpoints.remove(checkpoint);
        log.info(String.format("Removed Checkpoint #%d from Path #%d. New total: %d", checkpoint.getId(), pathId, checkpoints.size()));
        return this;
    }

    public Path removeCheckpoint(int index) {
        if ((index < 0) || (index > checkpoints.size())) throw new IndexOutOfBoundsException();
        return removeCheckpoint(getCheckpoint(index));
    }

    public Path moveCheckpoint(Checkpoint checkpoint, int direction) {
        int idx = checkpoints.indexOf(checkpoint);

        if (idx == -1)
            throw new RuntimeException("The specified checkpoint does not exist in this path.");
        if ((idx == 0) || (idx == (checkpoints.size() - 1))) return this;

        Collections.swap(checkpoints, idx, idx + direction);

        return this;
    }

    public Path moveCheckpoint(int index, int direction) {
        if ((index < 0) || (index > checkpoints.size())) return this;
        return moveCheckpoint(checkpoints.get(index), direction);
    }

    @JsonIgnore
    public String getCheckpointString() {
        Long[] ids = new Long[checkpoints.size()];

        for (int i = 0; i < ids.length; i++) {
            ids[i] = checkpoints.get(i).getId();
        }

        return StringUtils.join(ids, ',');
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Path)) return false;
        Path other = (Path) obj;
        return (pathId == other.pathId) &&
               (checkpoints.equals(other.checkpoints));
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(85, 41)
                .append(pathId)
                .append(checkpoints)
                .toHashCode();
    }
}
