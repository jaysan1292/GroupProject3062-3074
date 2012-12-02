package com.jaysan1292.groupproject.service.accessors;

import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.jaysan1292.groupproject.service.db.PlayerManager;

import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;

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

    protected void doUpdate(Player item, MultivaluedMap<String, String> newValues) throws GeneralServiceException {
        boolean update = false;
        if (newValues.containsKey(FIRST_NAME)) {
            item.setFirstName(newValues.getFirst(FIRST_NAME));
            update = true;
        }
        if (newValues.containsKey(LAST_NAME)) {
            item.setLastName(newValues.getFirst(LAST_NAME));
            update = true;
        }
        if (newValues.containsKey(STUDENT_NUMBER)) {
            item.setStudentNumber(newValues.getFirst(STUDENT_NUMBER));
            update = true;
        }
        if (update) {
            manager.update(item);
        }
    }
}
