package com.jaysan1292.groupproject.android;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import com.jaysan1292.groupproject.android.activity.MainActivity;
import com.jaysan1292.groupproject.android.net.ScavengerClient;
import com.jaysan1292.groupproject.android.util.Log4jConfigurator;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Kazedayz
 * Date: 07/12/12
 * Time: 8:43 PM
 */
public class MobileAppCommon extends Application {
    public static final Logger log = Logger.getLogger("scavenger");
    private static MobileAppCommon instance;
    private static ScavengerClient client;

    @Override
    public void onCreate() {
        Log4jConfigurator.configure();
        instance = this;
        super.onCreate();
    }

    public static ScavengerClient getClient() {
        return client;
    }

    public static void setClient(ScavengerClient client) {
        MobileAppCommon.client = client;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    public static void sendToMain(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
