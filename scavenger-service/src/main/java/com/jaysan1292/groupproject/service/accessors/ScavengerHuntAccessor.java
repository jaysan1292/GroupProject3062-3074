package com.jaysan1292.groupproject.service.accessors;

import com.jaysan1292.groupproject.data.ScavengerHunt;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.jaysan1292.groupproject.service.db.ScavengerHuntManager;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;

import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;

/** @author Jason Recillo */
@Path("/scavengerhunts")
public class ScavengerHuntAccessor extends AbstractAccessor<ScavengerHunt> {
    protected static final ScavengerHuntManager manager = new ScavengerHuntManager();

    public ScavengerHuntAccessor() {
        super(ScavengerHunt.class);
    }

    protected ScavengerHuntManager getManager() {
        return manager;
    }

    protected void doUpdate(ScavengerHunt item, MultivaluedMap<String, String> newValues) throws GeneralServiceException {
        boolean update = false;
        if (newValues.containsKey("path")) {
            item.setPath(PathAccessor.manager.get(NumberUtils.toLong(newValues.getFirst("path"))));
            update = true;
        }
        if (newValues.containsKey("team")) {
            item.setTeam(TeamAccessor.manager.get(NumberUtils.toLong(newValues.getFirst("team"))));
            update = true;
        }
        if (newValues.containsKey("startTime")) {
            item.setStartTime(DateTime.parse(newValues.getFirst("startTime")));
            update = true;
        }
        if (newValues.containsKey("finishTime")) {
            item.setFinishTime(DateTime.parse(newValues.getFirst("finishTime")));
            update = true;
        }

        if (update) {
            manager.update(item);
        }
    }
}
