package com.jaysan1292.groupproject.client.accessors;

import com.jaysan1292.groupproject.data.Team;

import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * Date: 28/11/12
 * Time: 9:14 PM
 *
 * @author Jason Recillo
 */
public class TeamClientAccessor extends AbstractClientAccessor<Team> {
    protected TeamClientAccessor() {
        super(Team.class, client.resource(Accessors.getDefaultHost()).path("teams"));
    }

    protected TeamClientAccessor(URI host) {
        super(Team.class, client.resource(host).path("teams"));
    }
}
