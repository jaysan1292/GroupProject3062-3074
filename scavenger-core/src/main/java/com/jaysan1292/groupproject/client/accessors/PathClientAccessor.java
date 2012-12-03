package com.jaysan1292.groupproject.client.accessors;

import com.jaysan1292.groupproject.data.Path;
import com.sun.jersey.api.client.Client;

import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * Date: 28/11/12
 * Time: 9:12 PM
 *
 * @author Jason Recillo
 */
public class PathClientAccessor extends AbstractClientAccessor<Path> {
    protected PathClientAccessor(Client client) {
        super(Path.class, client, client.resource(Accessors.getDefaultHost(client)).path("paths"));
    }

    protected PathClientAccessor(URI host, Client client) {
        super(Path.class, client, client.resource(host).path("paths"));
    }
}
