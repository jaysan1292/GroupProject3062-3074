package com.jaysan1292.groupproject.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.jaysan1292.groupproject.android.R;

/**
 * Created with IntelliJ IDEA.
 * User: Kazedayz
 * Date: 07/12/12
 * Time: 8:00 PM
 */
public class MainActivity extends Activity implements View.OnClickListener {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        findViewById(R.id.main_checkin_button).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onLogoutClicked(MenuItem item) {
        UserMetaManager.removeMetaValue(UserMetaManager.USER_AUTH);
        UserMetaManager.removeMetaValue(UserMetaManager.SERVICE_URI);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_checkin_button:
                //TODO: Start QR code reader activity
                Toast.makeText(this, "That's not done yet, but it's coming very soon.", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
