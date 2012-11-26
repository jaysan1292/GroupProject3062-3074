package com.jaysan1292.groupproject.service.accessors;

import com.jaysan1292.groupproject.Global;
import com.jaysan1292.groupproject.data.BaseEntity;
import com.jaysan1292.groupproject.data.JSONSerializable;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.jaysan1292.groupproject.service.db.AbstractManager;
import com.jaysan1292.groupproject.util.SerializationUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Providers;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
    SecurityContext context;
    @Context
    Providers providers;

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
            return returnErrorResponse(e);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id: [0-9]*}")
    public Response get(@PathParam("id") long id, @Context UriInfo uri) {
        try {
            return Response
                    .ok(getManager().get(id).toString(), MediaType.APPLICATION_JSON_TYPE)
                    .build();
        } catch (GeneralServiceException e) {
            return returnErrorResponse(e);
        } catch (RuntimeException e) {
            return logErrorAndReturnGenericErrorResponse(e);
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(String json) {
        try {
            T item = _cls.newInstance().readJSON(json);
            return Response
                    .ok(getManager().create(item), MediaType.APPLICATION_JSON_TYPE)
                    .build();
        } catch (ReflectiveOperationException e) {
            return logErrorAndReturnGenericErrorResponse(e);
        } catch (GeneralServiceException e) {
            return returnErrorResponse(e);
        } catch (IOException e) {
            return returnErrorResponse(e);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id: [0-9]*}")
    public Response update(@PathParam("id") long id, String json) {
        try {
            T item = _cls.newInstance().readJSON(json);

            return Response
                    .ok(SerializationUtils.serialize(item), MediaType.APPLICATION_JSON_TYPE)
                    .build();
        } catch (ReflectiveOperationException e) {
            return logErrorAndReturnGenericErrorResponse(e);
        } catch (IOException e) {
            return returnErrorResponse(e);
        }
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id: [0-9]*}")
    public Response delete(@PathParam("id") long id, String json) {
        try {
            T item = _cls.newInstance().readJSON(json);

            getManager().delete(item);
            return Response
                    .ok(SerializationUtils.serialize(item), MediaType.APPLICATION_JSON_TYPE)
                    .build();
        } catch (ReflectiveOperationException e) {
            return logErrorAndReturnGenericErrorResponse(e);
        } catch (IOException e) {
            return returnErrorResponse(e);
        } catch (GeneralServiceException e) {
            return returnErrorResponse(e);
        }
    }

    protected static String encodeErrorMessage(Throwable e) {
        return String.format(ERROR_TEMPLATE, StringEscapeUtils.escapeEcmaScript(e.getMessage()));
    }

    protected static Response logErrorAndReturnGenericErrorResponse(Throwable t) {
        Global.log.error(t.getMessage(), t);
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
    }

    protected static Response returnErrorResponse(Throwable t) {
        Global.log.error(t.getMessage(), t);
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(encodeErrorMessage(t))
                .build();
    }
}
