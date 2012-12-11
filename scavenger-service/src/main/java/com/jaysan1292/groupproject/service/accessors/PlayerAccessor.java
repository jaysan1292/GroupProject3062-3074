package com.jaysan1292.groupproject.service.accessors;

import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.service.db.PlayerManager;

import javax.ws.rs.Path;

/** @author Jason Recillo */
@Path("/players")
public class PlayerAccessor extends AbstractAccessor<Player> {
    protected static final PlayerManager manager = new PlayerManager();
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String STUDENT_NUMBER = "studentNumber";

    public PlayerAccessor() {
        super(Player.class);
    }

    protected PlayerManager getManager() {
        return manager;
    }
}
