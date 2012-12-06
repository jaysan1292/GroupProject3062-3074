package com.jaysan1292.groupproject.client.accessors;

import com.jaysan1292.groupproject.data.Checkpoint;
import com.sun.jersey.api.client.Client;

import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * Date: 28/11/12
 * Time: 9:12 PM
 *
 * @author Jason Recillo
 */
public class CheckpointClientAccessor extends AbstractClientAccessor<Checkpoint> {
    protected CheckpointClientAccessor(Client client) {
        super(Checkpoint.class, client, client.resource(Accessors.getDefaultHost(client)).path("checkpoints"));
    }

    protected CheckpointClientAccessor(URI host, Client client) {
        super(Checkpoint.class, client, client.resource(host).path("checkpoints"));
    }
}
