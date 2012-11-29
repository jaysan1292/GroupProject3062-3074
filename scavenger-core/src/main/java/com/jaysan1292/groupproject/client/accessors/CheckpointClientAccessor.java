package com.jaysan1292.groupproject.client.accessors;

import com.jaysan1292.groupproject.data.Checkpoint;

import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * Date: 28/11/12
 * Time: 9:12 PM
 *
 * @author Jason Recillo
 */
public class CheckpointClientAccessor extends AbstractClientAccessor<Checkpoint> {
    protected CheckpointClientAccessor() {
        super(Checkpoint.class, client.resource(Accessors.getDefaultHost()).path("checkpoints"));
    }

    protected CheckpointClientAccessor(URI host) {
        super(Checkpoint.class, client.resource(host).path("checkpoints"));
    }
}
