package com.jaysan1292.groupproject.android;

import com.jaysan1292.groupproject.data.Player;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

/** @author Jason Recillo */
public class ScavengerClient {
    private Client client;
    private WebResource res;

    public ScavengerClient() {
        client = Client.create();
//        res = client.resource()
    }

    public Player getPlayer() {
//        String playerJson =
        return null;
    }
}
