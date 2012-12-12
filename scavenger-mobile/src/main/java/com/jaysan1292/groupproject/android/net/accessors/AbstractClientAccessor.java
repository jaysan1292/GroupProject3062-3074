package com.jaysan1292.groupproject.android.net.accessors;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import com.jaysan1292.groupproject.android.MobileAppCommon;
import com.jaysan1292.groupproject.android.util.HttpClientUtils;
import com.jaysan1292.groupproject.data.BaseEntity;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.sun.jersey.api.client.UniformInterfaceException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * Date: 28/11/12
 * Time: 5:39 PM
 *
 * @author Jason Recillo
 */
public abstract class AbstractClientAccessor<T extends BaseEntity> {
    private final Class<T> _cls;
    protected URI _res;
    protected DefaultHttpClient client;
    protected Header authHeader;

    public AbstractClientAccessor(Class<T> cls, DefaultHttpClient client, URI res, Header authHeader) {
        this._cls = cls;
        this._res = res;
        this.client = client;
        this.authHeader = authHeader;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    public T get(long id) throws GeneralServiceException {
        try {
            HttpGet request = HttpClientUtils.createGetRequest(URI.create(_res.toString() + '/' + id), authHeader);
            HttpResponse response = client.execute(request);
            Preconditions.checkState(response.getStatusLine().getStatusCode() == 200,
                                     "Expected HTTP 200, got %s instead.",
                                     response.getStatusLine().getStatusCode());
            return _cls.newInstance().readJSON(HttpClientUtils.getStringEntity(response));
        } catch (UniformInterfaceException e) {
            throw new GeneralServiceException(e);
        } catch (IOException e) {
            throw new GeneralServiceException(e);
        } catch (InstantiationException ignored) {
        } catch (IllegalAccessException ignored) {}
        return null;
    }

    public void update(T item) throws GeneralServiceException {
        try {
            HttpPut request = HttpClientUtils.createPutRequest(URI.create(_res.toString() + item.getId()), authHeader);
            request.setEntity(new StringEntity(item.writeJSON(), Charsets.UTF_8.toString()));
            request.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.JSON_UTF_8.toString());

            HttpResponse response = client.execute(request);

//        ClientResponse response = _res.path(String.valueOf(item.getId()))
//                                      .entity(item.writeJSON())
//                                      .type(MediaType.APPLICATION_JSON_TYPE)
//                                      .put(ClientResponse.class);

            int status = response.getStatusLine().getStatusCode();

            // HTTP status 204: NO CONTENT
            if (status != 204) {
                throw new GeneralServiceException("Failed: HTTP code 204 expected, got " +
                                                  status + " instead.");
            }
        } catch (UnsupportedEncodingException ignored) {
        } catch (ClientProtocolException e) {
            MobileAppCommon.log.error(e.getMessage(), e);
        } catch (IOException e) {
            MobileAppCommon.log.error(e.getMessage(), e);
        }
    }
}
