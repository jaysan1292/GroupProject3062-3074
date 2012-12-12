package com.jaysan1292.groupproject.android.net.accessors;

import com.google.common.base.Preconditions;
import com.jaysan1292.groupproject.android.MobileAppCommon;
import com.jaysan1292.groupproject.android.util.HttpClientUtils;
import com.jaysan1292.groupproject.data.Player;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * Date: 28/11/12
 * Time: 5:53 PM
 *
 * @author Jason Recillo
 */
public class PlayerClientAccessor extends AbstractClientAccessor<Player> {
    protected PlayerClientAccessor(DefaultHttpClient client) {
        super(Player.class,
              client,
              URI.create(Accessors.getDefaultHost(client) + "/players"),
              Accessors.getAuthHeader());
    }

    protected PlayerClientAccessor(URI host, DefaultHttpClient client) {
        super(Player.class,
              client,
              URI.create(host.toString() + "/players"),
              Accessors.getAuthHeader());
    }

    public Player getPlayer(String studentId) {
        try {
            HttpGet request = HttpClientUtils.createGetRequest(URI.create(_res.toString() + "/studentnumbers/" + studentId),
                                                               authHeader);
            HttpResponse response = client.execute(request);
            Preconditions.checkState(response.getStatusLine().getStatusCode() == 200,
                                     "Expected HTTP 200, got %s instead.",
                                     response.getStatusLine().getStatusCode());
//            ClientResponse response = _res.path("studentnumbers/" + studentId).get(ClientResponse.class);
            String playerJson = HttpClientUtils.getStringEntity(response);
            MobileAppCommon.log.debug(playerJson);
            return new Player().readJSON(playerJson);
        } catch (IOException e) {
            MobileAppCommon.log.error(e.getMessage(), e);
            return null;
        }
    }
}
