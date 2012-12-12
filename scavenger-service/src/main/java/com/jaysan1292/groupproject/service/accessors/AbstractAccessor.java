package com.jaysan1292.groupproject.service.accessors;

import com.jaysan1292.groupproject.Global;
import com.jaysan1292.groupproject.data.*;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.jaysan1292.groupproject.service.db.AbstractManager;
import com.jaysan1292.groupproject.service.security.AuthorizationException;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The AbstractAccessor (and its subclasses) are the interface
 * for the web service, i.e., what the clients will interact with.
 *
 * @author Jason Recillo
 */
public abstract class AbstractAccessor<T extends BaseEntity> {
    private static final String ERROR_TEMPLATE = "{\"error\":\"%s\"}";
    private Class<T> _cls;

    @Context
    UriInfo uriInfo;
    @Context
    HttpHeaders headers;

    public AbstractAccessor(Class<T> cls) {
        this._cls = cls;
    }

    protected abstract AbstractManager<T> getManager();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try {
            AuthenticationAccessor.authorize(headers);

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
            AuthenticationAccessor.authorize(headers, _cls, id);

            T item = getManager().get(id);

            cleanPassword(item);

            return Response
                    .ok(item.toString(), MediaType.APPLICATION_JSON_TYPE)
                    .build();
        } catch (GeneralServiceException e) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity(Response.status(Response.Status.BAD_REQUEST)
                                           .entity(encodeErrorMessage(e))
                                           .build().getEntity())
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
            AuthenticationAccessor.authorize(headers);

            T item = _cls.newInstance().readJSON(json);
            String newid = String.valueOf(getManager().insert(item));

            return Response
                    .created(uriInfo.getAbsolutePathBuilder()
                                    .path(newid)
                                    .build())
                    .build();
        } catch (GeneralServiceException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(encodeErrorMessage(e))
                           .build();
        } catch (IOException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(encodeErrorMessage(e))
                           .build();
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
            AuthenticationAccessor.authorize(headers);

            T item = getManager().get(id);
            T newitem = _cls.newInstance().readJSON(json);
            if (item.equals(newitem)) {
                return Response.notModified().build();
            }
            newitem.setId(item.getId());

            // Special case for players
            if (_cls == Player.class) {
                ((Player) newitem).setPassword(((Player) item).getPassword());
            }

            getManager().update(newitem);
            return Response.noContent().build();
        } catch (GeneralServiceException e) {
            return Response.status(Response.Status.NOT_FOUND)
                           .build();
        } catch (IOException e) {
            Global.log.error(e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST)
                           .build();
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
            AuthenticationAccessor.authorize(headers);
            getManager().delete(id);
            return Response.status(Response.Status.ACCEPTED).build();
        } catch (GeneralServiceException e) {
            Global.log.error(e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(encodeErrorMessage(e))
                           .build();
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

    protected static Response unauthorizedResponse(Throwable t) {
        return Response.status(Response.Status.UNAUTHORIZED)
                       .entity(encodeErrorMessage(t))
                       .build();
    }

    protected void cleanPassword(T item) {
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
