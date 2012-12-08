package com.jaysan1292.groupproject.android;

import android.app.Application;
import android.content.Context;
import org.apache.log4j.Logger;


/**
 * Created with IntelliJ IDEA.
 * User: Kazedayz
 * Date: 07/12/12
 * Time: 8:43 PM
 */
public class MobileAppCommon extends Application {
    private static Context context;
    public static Logger log = Logger.getLogger("scavenger");

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
