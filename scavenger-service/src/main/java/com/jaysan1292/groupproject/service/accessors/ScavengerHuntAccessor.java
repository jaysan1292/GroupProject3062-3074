package com.jaysan1292.groupproject.service.accessors;

import com.jaysan1292.groupproject.data.ScavengerHunt;
import com.jaysan1292.groupproject.service.db.ScavengerHuntManager;
import com.jaysan1292.groupproject.util.JsonMap;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/** @author Jason Recillo */
@Path("/scavengerhunts")
public class ScavengerHuntAccessor extends AbstractAccessor<ScavengerHunt> {
    private static final ScavengerHuntManager manager = new ScavengerHuntManager();

    public ScavengerHuntAccessor() {
        super(ScavengerHunt.class);
    }

    protected ScavengerHuntManager getManager() {
        return manager;
    }

    protected Response doUpdate(long id, JsonMap map) {
        return null;  //TODO: Auto-generated method stub
    }
}
