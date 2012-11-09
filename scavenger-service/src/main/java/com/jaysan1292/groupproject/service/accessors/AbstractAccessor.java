package com.jaysan1292.groupproject.service.accessors;

import com.jaysan1292.groupproject.Global;
import com.jaysan1292.groupproject.data.BaseEntity;
import com.jaysan1292.groupproject.data.JSONSerializable;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.jaysan1292.groupproject.service.db.AbstractManager;
import com.jaysan1292.groupproject.util.JsonMap;
import com.jaysan1292.groupproject.util.SerializationUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Providers;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
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

    @Context
    SecurityContext context;
    @Context
    Providers providers;

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
            Global.log.error(e.getMessage(), e);
            String errMessage = StringEscapeUtils.escapeEcmaScript(e.getMessage());
            return Response
                    .ok(String.format(ERROR_TEMPLATE, errMessage), MediaType.APPLICATION_JSON_TYPE)
                    .build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id: [0-9]*}")
    public Response get(@PathParam("id") long id, @Context UriInfo uri) {
        try {
            return Response
                    .ok(getManager().get(id).toString(true), MediaType.APPLICATION_JSON_TYPE)
                    .build();
        } catch (Exception e) {
            Global.log.error(e.getMessage(), e);
            String errMessage = StringEscapeUtils.escapeEcmaScript(e.getMessage());
            return Response
                    .ok(encodeErrorMessage(e), MediaType.APPLICATION_JSON_TYPE)
                    .build();
        }
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public Response create(String json) throws GeneralServiceException {
        Map<String, String> createdItem = new HashMap<String, String>();
        createdItem.put("createdItem", getManager().create(json).toString());
        return Response
                .ok(SerializationUtils.serialize(createdItem), MediaType.APPLICATION_JSON_TYPE)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id: [0-9]*}")
    public Response update(@PathParam("id") long id, String json) {
        try {
            JsonMap map = new JsonMap(json);

            return doUpdate(id, map);
        } catch (IOException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(encodeErrorMessage(e))
                    .build();
        }
    }

    protected abstract Response doUpdate(long id, JsonMap map);

    protected static String encodeErrorMessage(Throwable e) {
        return String.format(ERROR_TEMPLATE, StringEscapeUtils.escapeEcmaScript(e.getMessage()));
    }
}
