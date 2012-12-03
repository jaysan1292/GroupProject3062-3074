package com.jaysan1292.groupproject.service.accessors;

import com.google.common.collect.Maps;
import com.jaysan1292.groupproject.Global;
import com.jaysan1292.groupproject.data.BaseEntity;
import com.jaysan1292.groupproject.data.JSONSerializable;
import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.jaysan1292.groupproject.service.db.AbstractManager;
import com.jaysan1292.groupproject.service.security.AuthorizationException;
import com.jaysan1292.groupproject.service.security.AuthorizationLevel;
import com.jaysan1292.groupproject.service.security.EncryptionUtils;
import com.jaysan1292.groupproject.util.SerializationUtils;
import com.sun.jersey.core.util.Base64;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.ws.rs.*;
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
    protected void authorize(AuthorizationLevel level, Object... params) throws AuthorizationException {
        if (headers.getRequestHeader(HttpHeaders.AUTHORIZATION) == null) {
            throw new AuthorizationException();
        }

        String authHeader = headers.getRequestHeader(HttpHeaders.AUTHORIZATION).get(0);
        authHeader = authHeader.substring("Basic ".length());
        String credentials[] = AUTH_SPLIT.split(Base64.base64Decode(authHeader));
        try {
            // Check if username is correct
            Player player = PlayerAccessor.manager.getPlayer(credentials[0]);

            // Check if password is correct
            if (!EncryptionUtils.checkPassword(credentials[1], player.getPassword())) {
                throw new AuthorizationException("Student ID or password incorrect");
            }

            // Check any params passed in
            if ((params != null) && (params.length > 0)) {
                Map<String, Object> parMap = Maps.newHashMap();
                // Collect all params first
                for (Object param : params) {
                    if (param.getClass() == Class.class) {
                        parMap.put("Class", param);
                    } else if (param.getClass() == Long.class) {
                        parMap.put("ID", param);
                    }
                }
                if (parMap.get("Class").equals(Player.class)) {
                    if (parMap.get("ID").equals(player.getId())) {
                        // Players are allowed to look at their own information,
                        // so here authorization is successful
                        return;
                    }
                }
            }

            // User's credentials check out, so now let's check their authorization level
            if (!player.isAdmin() && (level == AuthorizationLevel.ADMINISTRATOR)) {
                throw new AuthorizationException("User not authorized");
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
            if (_cls == Player.class) {
                authorize(AuthorizationLevel.ADMINISTRATOR, Player.class, id);
            }
            T item = getManager().get(id);
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
        } catch (Exception e) {
            return logErrorAndReturnGenericErrorResponse(e);
        }
    }

    @PUT
    @Path("/{id: [0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") long id, String json) {
        try {
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
            authorize(AuthorizationLevel.ADMINISTRATOR);
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
            authorize(AuthorizationLevel.ADMINISTRATOR);
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

    private static MultivaluedMap<String, String> multivaluedMapFromMap(Map<String, String> values) {
        MultivaluedMap<String, String> output = new MultivaluedMapImpl();
        for (String s : values.keySet()) {
            output.putSingle(s, values.get(s));
        }

        return output;
    }
}
