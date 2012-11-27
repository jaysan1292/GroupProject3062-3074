package com.jaysan1292.groupproject.service.accessors;

import com.jaysan1292.groupproject.data.Team;
import com.jaysan1292.groupproject.service.db.TeamManager;

import javax.ws.rs.Path;

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
}
