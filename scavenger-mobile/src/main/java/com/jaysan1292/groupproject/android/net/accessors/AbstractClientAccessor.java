package com.jaysan1292.groupproject.android.net.accessors;

import com.jaysan1292.groupproject.data.BaseEntity;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Date: 28/11/12
 * Time: 5:39 PM
 *
 * @author Jason Recillo
 */
public abstract class AbstractClientAccessor<T extends BaseEntity> {
    private final Class<T> _cls;
    protected final WebResource _res;
    protected final Client client;

    public AbstractClientAccessor(Class<T> cls, Client client, WebResource res) {
        this._cls = cls;
        this._res = res;
        this.client = client;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    public T get(long id) throws GeneralServiceException {
        try {
            return _cls.newInstance().readJSON(_res.path(String.valueOf(id)).get(String.class));
        } catch (UniformInterfaceException e) {
            throw new GeneralServiceException(e);
        } catch (IOException e) {
            throw new GeneralServiceException(e);
        } catch (InstantiationException ignored) {
        } catch (IllegalAccessException ignored) {}
        return null;
    }

    public void update(T item) throws GeneralServiceException {
        ClientResponse response = _res.path(String.valueOf(item.getId()))
                                      .entity(item.writeJSON())
                                      .type(MediaType.APPLICATION_JSON_TYPE)
                                      .put(ClientResponse.class);

        int status = response.getStatus();

        // HTTP status 204: NO CONTENT
        if (status != 204) {
            throw new GeneralServiceException("Failed: HTTP code 204 expected, got " +
                                              status + " instead.");
        }
    }
}
