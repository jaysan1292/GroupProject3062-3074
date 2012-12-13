package com.jaysan1292.groupproject.android;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import com.jaysan1292.groupproject.android.activity.LoginActivity;
import com.jaysan1292.groupproject.android.activity.MainActivity;
import com.jaysan1292.groupproject.android.activity.UserMetaManager;
import com.jaysan1292.groupproject.android.net.ScavengerClient;
import com.jaysan1292.groupproject.android.net.accessors.Accessors;
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

    public static void logout(Activity activity) {
        UserMetaManager.removeMetaValue(UserMetaManager.USER_AUTH);
        UserMetaManager.removeMetaValue(UserMetaManager.SERVICE_URL);
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
        if (!(activity instanceof LoginActivity)) {
            // only finish() if the activity is *not* a LoginActivity
            activity.finish();
        }

        client = null;
        Accessors.logout();
    }

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
