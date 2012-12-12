package com.jaysan1292.groupproject.service.accessors;

import com.google.common.collect.Lists;
import com.jaysan1292.groupproject.data.JSONSerializable;
import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.service.db.PlayerManager;
import com.jaysan1292.groupproject.service.security.AuthorizationException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Iterator;
import java.util.List;

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

    @GET
    @Path("/admins")
    public Response getAllAdmins() {
        try {
            AuthenticationAccessor.authorize(headers);

            List<Player> admins = Lists.newArrayList(manager.getAll());

            for (Iterator<Player> it = admins.iterator(); it.hasNext(); ) {
                Player player = it.next();
                if (!player.isAdmin()) it.remove();
            }

            for (Player admin : admins) {
                cleanPassword(admin);
            }

            return Response
                    .ok(JSONSerializable.writeJSONArray(admins), MediaType.APPLICATION_JSON_TYPE)
                    .build();
        } catch (AuthorizationException e) {
            return unauthorizedResponse(e);
        }
    }
}
