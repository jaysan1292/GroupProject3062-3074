package com.jaysan1292.groupproject.service.accessors;

import com.google.common.collect.Lists;
import com.jaysan1292.groupproject.data.Checkpoint;
import com.jaysan1292.groupproject.data.Path;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.jaysan1292.groupproject.service.db.PathManager;
import org.apache.commons.lang3.math.NumberUtils;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;
import java.util.regex.Pattern;

/** @author Jason Recillo */
@javax.ws.rs.Path("/paths")
public class PathAccessor extends AbstractAccessor<Path> {
    protected static final PathManager manager = new PathManager();
    private static final Pattern COMMA = Pattern.compile(",");

    public PathAccessor() {
        super(Path.class);
    }

    protected PathManager getManager() {
        return manager;
    }

    protected void doUpdate(Path item, MultivaluedMap<String, String> newValues) throws GeneralServiceException {
        if (newValues.containsKey("checkpoints")) {
            String[] checkpointString = COMMA.split(newValues.getFirst("checkpoints"));
            List<Checkpoint> checkpoints = Lists.newArrayList();

            for (String checkpointId : checkpointString) {
                checkpoints.add(CheckpointAccessor.manager.get(NumberUtils.toLong(checkpointId)));
            }
            item.setCheckpoints(checkpoints);

            manager.update(item);
        }
        // since "checkpoints" is the only major field for this object other than ID, if that hasn't
        // changed, just return without doing anything
    }
}
