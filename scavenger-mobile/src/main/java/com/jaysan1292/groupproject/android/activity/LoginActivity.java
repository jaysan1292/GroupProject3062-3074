package com.jaysan1292.groupproject.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.jaysan1292.groupproject.android.MobileAppCommon;

public class LoginActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        MobileAppCommon.log.info("Checking user login status...");
//        if (!UserMetaManager.getLoggedInUser().equals(User.NOT_LOGGED_IN)) {
            MobileAppCommon.log.info("User is logged in; sending to main activity.");
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
//            return;
//        }
//        setContentView(R.layout.login);
    }
}
