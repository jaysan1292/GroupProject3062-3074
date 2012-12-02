package com.jaysan1292.groupproject.service.accessors;

import com.jaysan1292.groupproject.Global;
import com.jaysan1292.groupproject.data.BaseEntity;
import com.jaysan1292.groupproject.data.JSONSerializable;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.jaysan1292.groupproject.service.db.AbstractManager;
import com.jaysan1292.groupproject.util.SerializationUtils;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.ws.rs.*;
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
    UriInfo context;

    public AbstractAccessor(Class<T> cls) {
        this._cls = cls;
    }

    protected abstract AbstractManager<T> getManager();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try {
            List<T> items = Arrays.asList(getManager().getAll());
            return Response
                    .ok(JSONSerializable.writeJSONArray(items), MediaType.APPLICATION_JSON_TYPE)
                    .build();
        } catch (Exception e) {
            return errorResponse(e);
        }
    }

    @GET
    @Path("/{id: [0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") long id) {
        try {
            T item = getManager().get(id);
            return Response
                    .ok(item.toString(), MediaType.APPLICATION_JSON_TYPE)
                    .build();
        } catch (GeneralServiceException e) {
//            return errorResponse(e);
            return Response.status(Response.Status.NOT_FOUND)
                           .entity(errorResponse(e).getEntity())
                           .build();
        } catch (RuntimeException e) {
            return logErrorAndReturnGenericErrorResponse(e);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(String json) {
        try {
            T item = _cls.newInstance().readJSON(json);
            item.setId(getManager().insert(item));

            return Response
                    .created(context.getAbsolutePathBuilder()
                                    .path(String.valueOf(item.getId()))
                                    .build())
                    .build();
        } catch (ReflectiveOperationException e) {
            return logErrorAndReturnGenericErrorResponse(e);
        } catch (GeneralServiceException e) {
            return errorResponse(e);
        } catch (IOException e) {
            return errorResponse(e);
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
        } catch (ReflectiveOperationException e) {
            Global.log.error(e.getMessage(), e);
            return Response.serverError().build();
        }
    }

    @PUT
    @Path("/{id: [0-9]*}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response update(@PathParam("id") long id, MultivaluedMap<String, String> values) {
        if (values.isEmpty()) return Response.notModified().build();
        try {
            T item = getManager().get(id);
            doUpdate(item, values);
            return Response.noContent().build();
        } catch (GeneralServiceException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id: [0-9]*}")
    public Response delete(@PathParam("id") long id) {
        try {
            getManager().delete(id);
            return Response.status(Response.Status.ACCEPTED).build();
        } catch (GeneralServiceException e) {
            return errorResponse(e);
        }
    }

    protected static String encodeErrorMessage(Throwable t) {
        return String.format(ERROR_TEMPLATE, StringEscapeUtils.escapeEcmaScript(t.getMessage()));
    }

    protected static Response logErrorAndReturnGenericErrorResponse(Throwable t) {
        Global.log.error(t.getMessage(), t);
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
    }

    protected static Response errorResponse(Throwable t) {
        return Response
                .status(Response.Status.BAD_REQUEST)
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
