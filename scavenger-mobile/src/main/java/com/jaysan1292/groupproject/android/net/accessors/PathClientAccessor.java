package com.jaysan1292.groupproject.android.net.accessors;

import com.jaysan1292.groupproject.data.Path;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * Date: 28/11/12
 * Time: 9:12 PM
 *
 * @author Jason Recillo
 */
public class PathClientAccessor extends AbstractClientAccessor<Path> {
    protected PathClientAccessor(DefaultHttpClient client) {
        super(Path.class,
              client,
              URI.create(Accessors.getDefaultHost(client) + "/paths"),
              Accessors.getAuthHeader());
    }

    protected PathClientAccessor(URI host, DefaultHttpClient client) {
        super(Path.class,
              client,
              URI.create(host.toString() + "/checkpoints"),
              Accessors.getAuthHeader());
    }
}
