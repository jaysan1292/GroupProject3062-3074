package com.jaysan1292.groupproject.service.accessors;

import com.jaysan1292.groupproject.Global;
import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.data.PlayerBuilder;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.jaysan1292.groupproject.service.db.PlayerManager;
import com.jaysan1292.groupproject.util.JsonMap;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Map;

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

    protected Response doUpdate(long id, JsonMap map) {
        PlayerBuilder playerBuilder = null;
        try {
            playerBuilder = new PlayerBuilder(manager.get(id));
        } catch (GeneralServiceException e) {
            throw new RuntimeException(e);
        }

        for (Map.Entry<String, Object> entry : map) {
            String s = entry.getKey();
            if (s.equals("firstName")) {
                playerBuilder.setFirstName((String) entry.getValue());
            } else if (s.equals("lastName")) {
                playerBuilder.setLastName((String) entry.getValue());
            } else if (s.equals("studentId")) {
                playerBuilder.setStudentId((String) entry.getValue());
            }
        }
        Player player = playerBuilder.build();
        try {
            manager.update(player);
        } catch (GeneralServiceException e) {
            Global.log.error(e.getMessage(), e);
            return Response
                    .serverError()
                    .entity(encodeErrorMessage(e))
                    .build();
        }
        return Response
                .ok(player.writeJSON())
                .build();
    }
}
