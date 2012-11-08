package com.jaysan1292.groupproject;

import org.apache.log4j.Logger;

/** @author Jason Recillo */
public class Global {
    public static final Logger log = Logger.getLogger(Global.class);
    private static boolean debug;
    private static boolean localHost;

    private Global() {}

    static {
        debug = false;
        localHost = false;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static boolean isLocalHost() {
        return localHost;
    }

    public static void setDebug(boolean b) {
        debug = b;
    }

    public static void setLocalHost(boolean local) {
        localHost = local;
    }
}
