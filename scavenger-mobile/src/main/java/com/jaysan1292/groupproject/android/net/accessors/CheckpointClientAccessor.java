package com.jaysan1292.groupproject.android.net.accessors;

import com.jaysan1292.groupproject.data.Checkpoint;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * Date: 28/11/12
 * Time: 9:12 PM
 *
 * @author Jason Recillo
 */
public class CheckpointClientAccessor extends AbstractClientAccessor<Checkpoint> {
    protected CheckpointClientAccessor(DefaultHttpClient client) {
        super(Checkpoint.class,
              client,
              URI.create(Accessors.getDefaultHost(client) + "/checkpoints"),
              Accessors.getAuthHeader());
    }

    protected CheckpointClientAccessor(URI host, DefaultHttpClient client) {
        super(Checkpoint.class,
              client,
              URI.create(host.toString() + "/checkpoints"),
              Accessors.getAuthHeader());
    }
}
