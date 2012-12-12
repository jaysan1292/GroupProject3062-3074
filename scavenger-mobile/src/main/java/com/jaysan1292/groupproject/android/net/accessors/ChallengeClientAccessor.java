package com.jaysan1292.groupproject.android.net.accessors;

import com.jaysan1292.groupproject.data.Challenge;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * Date: 28/11/12
 * Time: 9:01 PM
 *
 * @author Jason Recillo
 */
public class ChallengeClientAccessor extends AbstractClientAccessor<Challenge> {
    protected ChallengeClientAccessor(DefaultHttpClient client) {
        super(Challenge.class,
              client,
              URI.create(Accessors.getDefaultHost(client).toString() + "/challenges"),
              Accessors.getAuthHeader());
    }

    protected ChallengeClientAccessor(URI host, DefaultHttpClient client) {
        super(Challenge.class,
              client,
              URI.create(host.toString() + "/challenges"),
              Accessors.getAuthHeader());
    }
}
