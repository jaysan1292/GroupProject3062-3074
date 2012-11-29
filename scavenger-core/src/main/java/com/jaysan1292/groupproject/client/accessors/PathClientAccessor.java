package com.jaysan1292.groupproject.client.accessors;

import com.jaysan1292.groupproject.data.Path;

import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * Date: 28/11/12
 * Time: 9:12 PM
 *
 * @author Jason Recillo
 */
public class PathClientAccessor extends AbstractClientAccessor<Path> {
    protected PathClientAccessor() {
        super(Path.class, client.resource(Accessors.getDefaultHost()).path("paths"));
    }

    protected PathClientAccessor(URI host) {
        super(Path.class, client.resource(host).path("paths"));
    }
}
