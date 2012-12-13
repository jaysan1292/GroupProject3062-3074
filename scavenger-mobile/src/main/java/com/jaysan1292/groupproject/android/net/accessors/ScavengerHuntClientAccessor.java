package com.jaysan1292.groupproject.android.net.accessors;

import com.google.common.base.Preconditions;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import com.jaysan1292.groupproject.android.MobileAppCommon;
import com.jaysan1292.groupproject.android.util.HttpClientUtils;
import com.jaysan1292.groupproject.data.Checkpoint;
import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.data.ScavengerHunt;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * Date: 28/11/12
 * Time: 9:13 PM
 *
 * @author Jason Recillo
 */
public class ScavengerHuntClientAccessor extends AbstractClientAccessor<ScavengerHunt> {
    protected ScavengerHuntClientAccessor(DefaultHttpClient client) {
        super(ScavengerHunt.class,
              client,
              URI.create(Accessors.getDefaultHost(client) + "/scavengerhunts"),
              Accessors.getAuthHeader());
    }

    protected ScavengerHuntClientAccessor(URI host, DefaultHttpClient client) {
        super(ScavengerHunt.class,
              client,
              URI.create(host.toString() + "/scavengerhunts"),
              Accessors.getAuthHeader());
    }

    public ScavengerHunt getScavengerHunt(Player player) {
        try {
            HttpGet request = HttpClientUtils.createGetRequest(URI.create(_res.toString() + "/players/" + player.getId()),
                                                               authHeader);
            HttpResponse response = client.execute(request);
            Preconditions.checkState(response.getStatusLine().getStatusCode() == 200,
                                     "Expected HTTP 200, got %s instead.",
                                     response.getStatusLine().getStatusCode());
            return new ScavengerHunt().readJSON(HttpClientUtils.getStringEntity(response));
        } catch (IOException e) {
            return null;
        }
    }

    public void checkIn(ScavengerHunt scavengerHunt, Checkpoint checkpoint) throws GeneralServiceException {
        try {
            HttpPost request = HttpClientUtils.createPostRequest(URI.create(_res.toString() + "/checkin"), authHeader);
            request.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.FORM_DATA.toString());
            HttpParams params = new BasicHttpParams();
            params.setParameter("scavengerHuntId", scavengerHunt.getId());
            params.setParameter("checkpointId", checkpoint.getId());

            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new GeneralServiceException("There was a problem checking in. Server response: " +
                                                  response.getStatusLine().getStatusCode() + ' ' +
                                                  response.getStatusLine().getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            MobileAppCommon.log.error(e.getMessage(), e);
        } catch (IOException e) {
            MobileAppCommon.log.error(e.getMessage(), e);
        }
    }
}
