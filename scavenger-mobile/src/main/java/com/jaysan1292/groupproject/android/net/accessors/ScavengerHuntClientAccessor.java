package com.jaysan1292.groupproject.android.net.accessors;

import com.google.common.base.Preconditions;
import com.jaysan1292.groupproject.android.util.HttpClientUtils;
import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.data.ScavengerHunt;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

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
}
