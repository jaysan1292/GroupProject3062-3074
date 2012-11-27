package com.jaysan1292.groupproject.service.accessors;

import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.service.db.PlayerManager;

import javax.ws.rs.Path;

/** @author Jason Recillo */
@Path("/players")
public class PlayerAccessor extends AbstractAccessor<Player> {
    private static final PlayerManager manager = new PlayerManager();

    public PlayerAccessor() {
        super(Player.class);
    }

    protected PlayerManager getManager() {
        return manager;
    }
}
