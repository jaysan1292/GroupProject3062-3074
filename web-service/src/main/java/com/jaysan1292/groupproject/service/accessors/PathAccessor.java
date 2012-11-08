package com.jaysan1292.groupproject.service.accessors;

import com.jaysan1292.groupproject.data.Path;
import com.jaysan1292.groupproject.service.db.PathManager;
import com.jaysan1292.groupproject.util.JsonMap;

import javax.ws.rs.core.Response;

/** @author Jason Recillo */
@javax.ws.rs.Path("/paths")
public class PathAccessor extends AbstractAccessor<Path> {
    private static final PathManager manager = new PathManager();

    protected PathManager getManager() {
        return manager;
    }

    protected Response doUpdate(long id, JsonMap map) {
        return null;  //TODO: Auto-generated method stub
    }
}
