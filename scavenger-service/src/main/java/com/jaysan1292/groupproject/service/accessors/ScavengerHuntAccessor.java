package com.jaysan1292.groupproject.service.accessors;

import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.data.ScavengerHunt;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.jaysan1292.groupproject.service.db.PlayerManager;
import com.jaysan1292.groupproject.service.db.ScavengerHuntManager;
import com.jaysan1292.groupproject.service.db.TeamManager;
import com.jaysan1292.groupproject.service.security.AuthorizationException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/** @author Jason Recillo */
@Path("/scavengerhunts")
public class ScavengerHuntAccessor extends AbstractAccessor<ScavengerHunt> {
    protected static final ScavengerHuntManager manager = new ScavengerHuntManager();

    public ScavengerHuntAccessor() {
        super(ScavengerHunt.class);
    }

    protected ScavengerHuntManager getManager() {
        return manager;
    }

    @GET
    @Path("players/{id: [0-9]*}")
    public Response getScavengerHunt(@PathParam("id") long id) {
        try {
            AuthenticationAccessor.authorize(headers, Player.class, id);

            ScavengerHunt game = manager.getScavengerHunt(new TeamManager().getTeam(new PlayerManager().get(id)));

            return Response
                    .ok(game.toString(), MediaType.APPLICATION_JSON_TYPE)
                    .build();
        } catch (AuthorizationException e) {
            return unauthorizedResponse(e);
        } catch (GeneralServiceException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
