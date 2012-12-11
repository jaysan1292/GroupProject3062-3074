package com.jaysan1292.groupproject.service.accessors;

import com.jaysan1292.groupproject.data.Team;
import com.jaysan1292.groupproject.service.db.TeamManager;

import javax.ws.rs.Path;
import java.util.regex.Pattern;

/** @author Jason Recillo */
@Path("/teams")
public class TeamAccessor extends AbstractAccessor<Team> {
    protected static final TeamManager manager = new TeamManager();
    private static final Pattern COMMA = Pattern.compile(",");

    public TeamAccessor() {
        super(Team.class);
    }

    protected TeamManager getManager() {
        return manager;
    }
}
