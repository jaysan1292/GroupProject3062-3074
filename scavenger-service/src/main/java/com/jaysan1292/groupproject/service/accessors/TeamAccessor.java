package com.jaysan1292.groupproject.service.accessors;

import com.google.common.collect.Maps;
import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.data.Team;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.jaysan1292.groupproject.service.db.TeamManager;
import org.apache.commons.lang3.math.NumberUtils;

import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Map;
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

    protected void doUpdate(Team item, MultivaluedMap<String, String> newValues) throws GeneralServiceException {
        if (newValues.containsKey("teamMembers")) {
            String[] memberString = COMMA.split(newValues.getFirst("teamMembers"));
            Map<Long, Player> players = Maps.newHashMap();

            for (String playerId : memberString) {
                long pid = NumberUtils.toLong(playerId);
                players.put(pid, PlayerAccessor.manager.get(pid));
            }
            item.setTeamMembers(players);

            manager.update(item);
        }
        // since "checkpoints" is the only major field for this object other than ID, if that hasn't
        // changed, just return without doing anything
    }
}
