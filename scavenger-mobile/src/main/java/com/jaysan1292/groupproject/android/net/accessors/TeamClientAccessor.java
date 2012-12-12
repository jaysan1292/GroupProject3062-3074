package com.jaysan1292.groupproject.android.net.accessors;

import com.jaysan1292.groupproject.data.Team;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * Date: 28/11/12
 * Time: 9:14 PM
 *
 * @author Jason Recillo
 */
public class TeamClientAccessor extends AbstractClientAccessor<Team> {
    protected TeamClientAccessor(DefaultHttpClient client) {
        super(Team.class,
              client,
              URI.create(Accessors.getDefaultHost(client) + "/teams"),
              Accessors.getAuthHeader());
    }

    protected TeamClientAccessor(URI host, DefaultHttpClient client) {
        super(Team.class,
              client,
              URI.create(host.toString() + "/teams"),
              Accessors.getAuthHeader());
    }
}
