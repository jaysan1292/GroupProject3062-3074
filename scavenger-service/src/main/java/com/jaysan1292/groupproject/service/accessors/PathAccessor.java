package com.jaysan1292.groupproject.service.accessors;

import com.jaysan1292.groupproject.data.Path;
import com.jaysan1292.groupproject.service.db.PathManager;

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
}
