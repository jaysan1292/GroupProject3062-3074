package com.jaysan1292.groupproject.client.accessors;

import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.data.Team;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.sun.jersey.api.client.Client;

import java.net.URI;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Date: 28/11/12
 * Time: 5:53 PM
 *
 * @author Jason Recillo
 */
public class PlayerClientAccessor extends AbstractClientAccessor<Player> {
    protected PlayerClientAccessor(Client client) {
        super(Player.class, client, client.resource(Accessors.getDefaultHost(client)).path("players"));
    }

    protected PlayerClientAccessor(URI host, Client client) {
        super(Player.class, client, client.resource(host).path("players"));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<Player> getPlayersWithoutTeam() throws GeneralServiceException {
        List<Team> allTeams = Accessors.getTeamAccessor().getAll();
        List<Player> players = getAll();

        for (Team team : allTeams) {
            for (Iterator<Player> it = players.iterator(); it.hasNext(); ) {
                Player player = it.next();
                // Remove players from output list if they are already on a team or the "player" is an administrator
                if (player.isAdmin() || team.getTeamMembers().containsValue(player)) it.remove();
            }
        }

        return players;
    }
}
