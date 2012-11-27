package com.jaysan1292.groupproject.service.accessors;

import com.jaysan1292.groupproject.data.Path;
import com.jaysan1292.groupproject.service.db.PathManager;

/** @author Jason Recillo */
@javax.ws.rs.Path("/paths")
public class PathAccessor extends AbstractAccessor<Path> {
    private static final PathManager manager = new PathManager();

    public PathAccessor() {
        super(Path.class);
    }

    protected PathManager getManager() {
        return manager;
    }
}
