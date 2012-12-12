package com.jaysan1292.groupproject.android.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jaysan1292.groupproject.android.MobileAppCommon;
import com.jaysan1292.groupproject.android.R;
import com.jaysan1292.groupproject.android.net.ScavengerClient;
import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.data.ScavengerHunt;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;

/**
 * Created with IntelliJ IDEA.
 * User: Kazedayz
 * Date: 07/12/12
 * Time: 8:00 PM
 */
public class MainActivity extends Activity implements View.OnClickListener {
    private ScavengerHunt game;
    private Player player;

    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        findViewById(R.id.main_checkin_button).setOnClickListener(this);

        new LoadScavengerHuntTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onLogoutClicked(MenuItem item) {
        MobileAppCommon.logout(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_checkin_button:
                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.initiateScan();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            MobileAppCommon.log.info(result.toString());
        }
    }

    private class LoadScavengerHuntTask extends AsyncTask<Void, Void, ScavengerHunt> {
        private ProgressDialog dialog;
        private Player scPlayer;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Loading your game...");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            dialog.setProgressNumberFormat(null);
            dialog.setProgressPercentFormat(null);
            dialog.show();
        }

        @Override
        protected void onPostExecute(ScavengerHunt scavengerHunt) {
            player = scPlayer;
            game = scavengerHunt;
            dialog.dismiss();

            MainActivity.this.setTitle(player.getFullName() + "'s Scavenger Hunt");
        }

        @Override
        protected ScavengerHunt doInBackground(Void... params) {
            ScavengerClient client = MobileAppCommon.getClient();
            String studentId = new String(Base64.decode(UserMetaManager.getMetaValue(UserMetaManager.USER_AUTH), 0)).split(":")[0];
            try {
                // Load player data first
                scPlayer = client.getPlayer(studentId);

                // Now load the scavenger hunt for the player
                return client.getScavengerHunt(scPlayer);
            } catch (GeneralServiceException e) {
                return null;
            }
        }
    }
}
