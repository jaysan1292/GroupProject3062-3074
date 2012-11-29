package com.jaysan1292.groupproject.client.accessors;

import com.jaysan1292.groupproject.data.ScavengerHunt;

import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * Date: 28/11/12
 * Time: 9:13 PM
 *
 * @author Jason Recillo
 */
public class ScavengerHuntClientAccessor extends AbstractClientAccessor<ScavengerHunt> {
    protected ScavengerHuntClientAccessor() {
        super(ScavengerHunt.class, client.resource(Accessors.getDefaultHost()).path("scavengerhunts"));
    }

    protected ScavengerHuntClientAccessor(URI host) {
        super(ScavengerHunt.class, client.resource(host).path("scavengerhunts"));
    }
}
