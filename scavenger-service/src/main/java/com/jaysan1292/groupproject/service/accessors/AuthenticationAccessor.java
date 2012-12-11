package com.jaysan1292.groupproject.service.accessors;

import com.google.common.collect.Maps;
import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.data.ScavengerHunt;
import com.jaysan1292.groupproject.data.Team;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.jaysan1292.groupproject.service.db.ScavengerHuntManager;
import com.jaysan1292.groupproject.service.db.TeamManager;
import com.jaysan1292.groupproject.service.security.AuthorizationException;
import com.jaysan1292.groupproject.service.security.AuthorizationLevel;
import com.jaysan1292.groupproject.service.security.EncryptionUtils;
import com.sun.jersey.core.util.Base64;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.sql.SQLException;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * Date: 10/12/12
 * Time: 9:10 PM
 *
 * @author Jason Recillo
 */
@Path("/auth")
public class AuthenticationAccessor {
    private static final Pattern AUTH_SPLIT = Pattern.compile(":");

    @Context
    UriInfo uriInfo;
    @Context
    HttpHeaders headers;

    /**
     * Authorization rules:
     * <ul>
     * <li>Users may access their own information</li>
     * <li>Users may access information about the team they are in</li>
     * <li>Users may access information about the scavenger hunt instance they are participating in</li>
     * <li>Users may NOT access another user's information</li>
     * <li>Users may NOT access another team's information</li>
     * <li>Users may NOT access information about another team's scavenger hunt instance</li>
     * <li>Administrators may access everything</li>
     * </ul>
     *
     * @throws com.jaysan1292.groupproject.service.security.AuthorizationException
     */
    protected static void authorize(HttpHeaders headers, Object... params) throws AuthorizationException {
        if (headers.getRequestHeader(HttpHeaders.AUTHORIZATION) == null) {
            throw new AuthorizationException();
        }

        String authHeader = headers.getRequestHeader(HttpHeaders.AUTHORIZATION).get(0);
        authHeader = authHeader.substring("Basic ".length());
        String credentials[] = AUTH_SPLIT.split(Base64.base64Decode(authHeader));
        try {
            // Check if username is correct
            Player subject = PlayerAccessor.manager.getPlayer(credentials[0]);

            // Check if password is correct
            if ((subject == null) || !EncryptionUtils.checkPassword(credentials[1], subject.getPassword())) {
                throw new AuthorizationException("Student ID or password incorrect");
            }

            // If the subject is an admin, they are automatically authorized to view all
            // information, so in this case skip all the checks below
            if (subject.isAdmin()) {
                return;
            }

            // Check any params passed in
            if ((params != null) && (params.length > 0)) {
                Map<String, Object> parMap = Maps.newHashMap();
                // Collect all params first
                for (Object param : params) {
                    if (param instanceof Class) {
                        parMap.put("Class", param);
                    } else if (param instanceof Long) {
                        parMap.put("ID", param);
                    } else if (param instanceof AuthorizationLevel) {
                        parMap.put("AuthLevel", param);
                    }
                }
                // Now process them
                Class cls = (Class) parMap.get("Class");
                if (cls != null) {
                    if (cls.equals(Player.class)) {
                        if (parMap.get("ID").equals(subject.getId())) {
                            // Players are allowed to look at their own information,
                            // so here authorization is successful
                            return;
                        }
                    } else if (cls.equals(Team.class)) {
                        try {
                            Team subjectTeam = new TeamManager().getTeam(subject);
                            if (parMap.get("ID").equals(subjectTeam.getId())) {
                                // Players can look at their own team's information,
                                // so here authorization is successful
                                return;
                            }
                        } catch (GeneralServiceException e) {
                            // The subject is not on any team and is therefore not authorized to
                            // view any team information; however if the user is an administrator,
                            // they are allowed to continue
                            if (!subject.isAdmin()) {
                                throw new AuthorizationException(e.getMessage(), e);
                            }
                        }
                    } else if (cls.equals(ScavengerHunt.class)) {
                        try {
                            ScavengerHunt subjectGame = new ScavengerHuntManager()
                                    .getScavengerHunt(new TeamManager().getTeam(subject));
                            if (parMap.get("ID").equals(subjectGame.getId())) {
                                // Players can access information about the game they are currently
                                // participating in, so here authorization is successful
                                return;
                            }
                        } catch (GeneralServiceException e) {
                            if (!subject.isAdmin()) {
                                throw new AuthorizationException(e.getMessage(), e);
                            }
                        }
                    }
                }
                // Everything is by default administrator-only, so unless otherwise specified,
                // if execution gets here, the user is not authorized
                if ((parMap.get("AuthLevel") != null) &&
                    parMap.get("AuthLevel").equals(AuthorizationLevel.MOBILE_USER)) {
                    return;
                }
            }
            if (!subject.isAdmin()) {
                throw new AuthorizationException("User not authorized.");
            }
        } catch (SQLException e) {
            throw new AuthorizationException("Student ID or password incorrect");
        }
    }

    @GET
    public Response checkAuthorization() {
        try {
            authorize(headers);
            return Response.ok().build();
        } catch (AuthorizationException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
