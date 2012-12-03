package com.jaysan1292.groupproject.service.accessors;

import com.google.common.collect.Maps;
import com.jaysan1292.groupproject.Global;
import com.jaysan1292.groupproject.data.*;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.jaysan1292.groupproject.service.db.AbstractManager;
import com.jaysan1292.groupproject.service.db.ScavengerHuntManager;
import com.jaysan1292.groupproject.service.db.TeamManager;
import com.jaysan1292.groupproject.service.security.AuthorizationException;
import com.jaysan1292.groupproject.service.security.AuthorizationLevel;
import com.jaysan1292.groupproject.service.security.EncryptionUtils;
import com.jaysan1292.groupproject.util.SerializationUtils;
import com.sun.jersey.core.util.Base64;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * The AbstractAccessor (and its subclasses) are the interface
 * for the web service, i.e., what the clients will interact with.
 *
 * @author Jason Recillo
 */
public abstract class AbstractAccessor<T extends BaseEntity> {
    private static final String ERROR_TEMPLATE = "{\"error\":\"%s\"}";
    private static final Pattern AUTH_SPLIT = Pattern.compile(":");
    private Class<T> _cls;

    @Context
    UriInfo uriInfo;
    @Context
    HttpHeaders headers;

    public AbstractAccessor(Class<T> cls) {
        this._cls = cls;
    }

    protected abstract AbstractManager<T> getManager();

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
     * @throws AuthorizationException
     */
    protected void authorize(Object... params) throws AuthorizationException {
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
                throw new AuthorizationException("User not authorized.");
            }
        } catch (SQLException e) {
            throw new AuthorizationException("Student ID or password incorrect");
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try {
            authorize(AuthorizationLevel.ADMINISTRATOR);
            List<T> items = Arrays.asList(getManager().getAll());

            for (T item : items) {
                cleanPassword(item);
            }

            return Response
                    .ok(JSONSerializable.writeJSONArray(items), MediaType.APPLICATION_JSON_TYPE)
                    .build();
        } catch (AuthorizationException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } catch (Exception e) {
            return logErrorAndReturnGenericErrorResponse(e);
        }
    }

    @GET
    @Path("/{id: [0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") long id) {
        try {
            authorize(_cls, id);

            T item = getManager().get(id);

            cleanPassword(item);

            return Response
                    .ok(item.toString(), MediaType.APPLICATION_JSON_TYPE)
                    .build();
        } catch (GeneralServiceException e) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity(errorResponse(e).getEntity())
                           .build();
        } catch (AuthorizationException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } catch (Exception e) {
            return logErrorAndReturnGenericErrorResponse(e);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(String json) {
        try {
            authorize();
            T item = _cls.newInstance().readJSON(json);
            String newid = String.valueOf(getManager().insert(item));

            return Response
                    .created(uriInfo.getAbsolutePathBuilder()
                                    .path(newid)
                                    .build())
                    .build();
        } catch (GeneralServiceException e) {
            return errorResponse(e);
        } catch (IOException e) {
            return errorResponse(e);
        } catch (AuthorizationException e) {
            return unauthorizedResponse(e);
        } catch (Exception e) {
            return logErrorAndReturnGenericErrorResponse(e);
        }
    }

    @PUT
    @Path("/{id: [0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") long id, String json) {
        try {
            authorize();
            T item = getManager().get(id);
            if (item.equals(_cls.newInstance().readJSON(json))) {
                return Response.notModified().build();
            }
            MultivaluedMap<String, String> values = multivaluedMapFromMap(SerializationUtils.deserialize(json));
            doUpdate(item, values);
            return Response.noContent().build();
        } catch (GeneralServiceException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (IOException e) {
            Global.log.error(e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (AuthorizationException e) {
            return unauthorizedResponse(e);
        } catch (Exception e) {
            return logErrorAndReturnGenericErrorResponse(e);
        }
    }

    @PUT
    @Path("/{id: [0-9]*}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response update(@PathParam("id") long id, MultivaluedMap<String, String> values) {
        if (values.isEmpty()) return Response.notModified().build();
        try {
            authorize();
            T item = getManager().get(id);
            doUpdate(item, values);
            return Response.noContent().build();
        } catch (GeneralServiceException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (AuthorizationException e) {
            return unauthorizedResponse(e);
        } catch (Exception e) {
            return logErrorAndReturnGenericErrorResponse(e);
        }
    }

    @DELETE
    @Path("/{id: [0-9]*}")
    public Response delete(@PathParam("id") long id) {
        try {
            authorize();
            getManager().delete(id);
            return Response.status(Response.Status.ACCEPTED).build();
        } catch (GeneralServiceException e) {
            return errorResponse(e);
        } catch (AuthorizationException e) {
            return unauthorizedResponse(e);
        } catch (Exception e) {
            return logErrorAndReturnGenericErrorResponse(e);
        }
    }

    protected static String encodeErrorMessage(Throwable t) {
        return String.format(ERROR_TEMPLATE, StringEscapeUtils.escapeEcmaScript(t.getMessage()));
    }

    protected static Response logErrorAndReturnGenericErrorResponse(Throwable t) {
        Global.log.error(t.getMessage(), t);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .build();
    }

    protected static Response errorResponse(Throwable t) {
        return Response.status(Response.Status.BAD_REQUEST)
                       .entity(encodeErrorMessage(t))
                       .build();
    }

    protected static Response unauthorizedResponse(Throwable t) {
        return Response.status(Response.Status.UNAUTHORIZED)
                       .entity(encodeErrorMessage(t))
                       .build();
    }

    protected abstract void doUpdate(T item, MultivaluedMap<String, String> newValues) throws GeneralServiceException;

    private void cleanPassword(T item) {
        if (item instanceof Player) {
            ((Player) item).setPassword(null);
        } else if (item instanceof Team) {
            for (Player player : ((Team) item).getTeamMembers().values()) {
                player.setPassword(null);
            }
        } else if (item instanceof ScavengerHunt) {
            for (Player player : ((ScavengerHunt) item).getTeam().getTeamMembers().values()) {
                player.setPassword(null);
            }
        }
    }

    private static MultivaluedMap<String, String> multivaluedMapFromMap(Map<String, String> values) {
        MultivaluedMap<String, String> output = new MultivaluedMapImpl();
        for (String s : values.keySet()) {
            output.putSingle(s, values.get(s));
        }

        return output;
    }
}
