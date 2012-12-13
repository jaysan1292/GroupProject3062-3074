package com.jaysan1292.groupproject.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.jaysan1292.groupproject.android.MobileAppCommon;

import static com.jaysan1292.groupproject.android.activity.UserMetaManager.*;

/**
 * Created with IntelliJ IDEA.
 * Date: 11/12/12
 * Time: 11:47 PM
 *
 * @author Jason Recillo
 */
public class SplashActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MobileAppCommon.log.info("Checking login status.");
        if (checkLoginStatus()) {
            MobileAppCommon.log.info("User is logged in; send to main.");
            new LoginTask(this).execute(getMetaValue(SERVICE_URL), getMetaValue(USER_AUTH));
        } else {
            MobileAppCommon.log.info("User is not logged in; send to login.");
            Intent intent;
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
