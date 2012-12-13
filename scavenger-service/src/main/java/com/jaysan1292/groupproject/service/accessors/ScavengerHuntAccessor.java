package com.jaysan1292.groupproject.service.accessors;

import com.google.common.base.Preconditions;
import com.jaysan1292.groupproject.data.Checkpoint;
import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.data.ScavengerHunt;
import com.jaysan1292.groupproject.data.Team;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.jaysan1292.groupproject.service.db.CheckpointManager;
import com.jaysan1292.groupproject.service.db.PlayerManager;
import com.jaysan1292.groupproject.service.db.ScavengerHuntManager;
import com.jaysan1292.groupproject.service.db.TeamManager;
import com.jaysan1292.groupproject.service.security.AuthorizationException;
import com.jaysan1292.groupproject.service.security.AuthorizationLevel;
import org.apache.commons.lang3.math.NumberUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
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

            cleanPassword(game);

            return Response
                    .ok(game.toString(), MediaType.APPLICATION_JSON_TYPE)
                    .build();
        } catch (AuthorizationException e) {
            return unauthorizedResponse(e);
        } catch (GeneralServiceException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/checkin")
    public Response doCheckIn(MultivaluedMap<String, String> formParams) {
        try {
            String scavengerHuntId = formParams.getFirst("scavengerHuntId");
            String checkpointId = formParams.getFirst("checkpointId");

            Preconditions.checkNotNull(scavengerHuntId);
            Preconditions.checkNotNull(checkpointId);

            long shid = NumberUtils.toLong(scavengerHuntId, -1);
            long cid = NumberUtils.toLong(checkpointId, -1);

            ScavengerHunt scavengerHunt = manager.get(shid);
            Checkpoint checkpoint = new CheckpointManager().get(cid);

            AuthenticationAccessor.authorize(headers, AuthorizationLevel.MOBILE_USER, Team.class, scavengerHunt.getTeam().getId());

            scavengerHunt.checkIn(checkpoint);
            manager.update(scavengerHunt);

            return Response.ok().build();
        } catch (AuthorizationException e) {
            return unauthorizedResponse(e);
        } catch (GeneralServiceException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (NullPointerException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
