package com.jaysan1292.groupproject.service;

import com.google.common.base.Preconditions;
import com.jaysan1292.groupproject.Global;
import com.jaysan1292.groupproject.service.db.DatabaseHelper;
import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ScanningResourceConfig;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ScavengerService {
    public static URI BASE_URI;
    private static SelectorThread threadSelector;
    private static boolean _started;

    //Disable logging by the Jersey and Grizzly frameworks
    private static final Logger SUN_LOGGER = Logger.getLogger("com.sun");
    private static final java.util.logging.Logger JUL_SUN_LOGGER = java.util.logging.Logger.getLogger("com.sun");

    static {
        SUN_LOGGER.setLevel(Level.OFF);
        JUL_SUN_LOGGER.setLevel(java.util.logging.Level.SEVERE);
    }

    private ScavengerService() {}

    public static void main(String[] args) throws Exception {
        start(args);
        System.in.read();
        stop();
    }

    public static void start(String[] args) throws Exception {
        Preconditions.checkState(!_started);

        try {
            Global.log.info("Starting web service...");
            threadSelector = startServer(args);
            DatabaseHelper.initDatabase();
            Global.log.info(String.format("Service is now running, with WADL available at %sapplication.wadl", BASE_URI));
            Global.log.info("Press <ENTER> to stop...");
        } catch (Exception e) {
            Global.log.info("An exception was thrown when starting the server. Shutting down...", e);
            _started = true; //stop() expects _started == true
            stop();
            System.exit(-1);
        }

        _started = true;
    }

    public static void stop() {
        Preconditions.checkState(_started);

        try {
            Global.log.info("Server shutting down...");
            threadSelector.stopEndpoint();
            DatabaseHelper.cleanDatabase();
            Global.log.info("Done!");
        } catch (Exception e) {
            Global.log.error("There was an error shutting down the service: ", e);
        }

        _started = false;
    }

    protected static SelectorThread startServer(String... args) throws IOException {
        if (args != null) {
            for (String s : args) {
                if (s.equals("--local")) {
                    Global.setLocalHost(true);
                } else if (s.equals("--debug")) {
                    Global.setDebug(true);
                }
            }
            if (!Global.isDebug()) {
                Logger.getLogger("com.jaysan1292").setLevel(Level.INFO);
            }
        }

        Logger.getLogger(PackagesResourceConfig.class.getName()).setLevel(Level.OFF);
        Logger.getLogger(ScanningResourceConfig.class.getName()).setLevel(Level.OFF);

        String host = getNetworkIP(Global.isLocalHost());
        BASE_URI = UriBuilder.fromUri(String.format("http://%s/service/", host)).port(9000).build();
        Map<String, String> initParams = new HashMap<String, String>();

        initParams.put(PackagesResourceConfig.PROPERTY_PACKAGES, "com.jaysan1292.groupproject");

        return GrizzlyWebContainerFactory.create(BASE_URI, initParams);
    }

    /*
    * This is a horrible workaround so that I can get the web service to be seen on the network
    * (and, if run on my server, to be available everywhere on the Internet). If all else fails,
    * we'll launch the service on localhost. Note that this is extremely specific to my laptop
    * and my server setup... Clunky, yes. But it works! At least this is only initialization code.
    */
    private static String getNetworkIP(boolean useLocalHost) {
        InetAddress address = null;
        String hostAddress;
        try {
            if (!useLocalHost) {
                // First try the ethernet port on my server
                try {
                    address = Collections.list(NetworkInterface.getByName("eth0").getInetAddresses()).get(1);
                } catch (IndexOutOfBoundsException ignored) {}

                // Here we check if the address length is 4 because we want to ignore IPv6 addresses
                if ((address == null) || (address.getAddress().length != 4)) {
                    // Next try the ethernet port on my laptop if that doesn't work
                    address = Collections.list(NetworkInterface.getByName("eth3").getInetAddresses()).get(0);
                    if (address.getAddress().length != 4) {
                        // If we get here, that means that the ethernet is not connected, so now try the wi-fi
                        address = Collections.list(NetworkInterface.getByName("net3").getInetAddresses()).get(0);
                    }
                }
            }

            // If we get here, and address is still null, then nothing is connected or useLocalHost
            // is set to true. In either case, we will just set address to 127.0.0.1 (localhost).
            if (address == null) address = InetAddress.getByAddress(new byte[]{127, 0, 0, 1});

            hostAddress = address.getHostAddress();
        } catch (IOException e) {
            hostAddress = "127.0.0.1";
        }
        return hostAddress;
    }
}
