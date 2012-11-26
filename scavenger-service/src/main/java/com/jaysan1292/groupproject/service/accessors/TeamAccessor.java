package com.jaysan1292.groupproject.service.accessors;

import com.jaysan1292.groupproject.data.Team;
import com.jaysan1292.groupproject.service.db.TeamManager;
import com.jaysan1292.groupproject.util.JsonMap;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/** @author Jason Recillo */
@Path("/teams")
public class TeamAccessor extends AbstractAccessor<Team> {
    public static final TeamManager manager = new TeamManager();

    public TeamAccessor() {
        super(Team.class);
    }

    protected TeamManager getManager() {
        return manager;
    }

    protected Response doUpdate(long id, JsonMap map) {
        return null;  //TODO: Auto-generated method stub
    }
}
