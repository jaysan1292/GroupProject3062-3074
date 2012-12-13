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
import android.widget.TextView;
import android.widget.Toast;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jaysan1292.groupproject.android.MobileAppCommon;
import com.jaysan1292.groupproject.android.R;
import com.jaysan1292.groupproject.android.net.ScavengerClient;
import com.jaysan1292.groupproject.data.Checkpoint;
import com.jaysan1292.groupproject.data.Player;
import com.jaysan1292.groupproject.data.ScavengerHunt;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kazedayz
 * Date: 07/12/12
 * Time: 8:00 PM
 */
public class MainActivity extends Activity implements View.OnClickListener {
    private ScavengerHunt game;
    private Checkpoint currentCheckpoint;
    private Checkpoint nextCheckpoint;
    private Player player;

    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTitle("");

        findViewById(R.id.main_checkin_button).setOnClickListener(this);

        new LoadScavengerHuntTask().execute();
    }

    private void onScavengerHuntLoaded() {
        setTitle(player.getFullName() + "'s Scavenger Hunt");

        // Check game completion status
        List<Checkpoint> checkpointsToDo = Lists.newArrayList(game.getPath().getCheckpoints());

        for (Iterator<Checkpoint> iterator = game.getCompletedCheckpoints().iterator(); iterator.hasNext(); ) {
            Checkpoint checkpoint = iterator.next();
            if (checkpointsToDo.contains(checkpoint)) iterator.remove();
        }

        if (checkpointsToDo.isEmpty()) {
            // The user has completed the scavenger hunt.
            // TODO: what to do when a player has completed the game?
            MobileAppCommon.log.info("Congratulations! Completed scavenger hunt.");
            return;
        }
        if (UserMetaManager.getMetaValue(UserMetaManager.CURRENT_CHECKPOINT_ID) == null) {
            currentCheckpoint = checkpointsToDo.get(0);

            try {
                nextCheckpoint = checkpointsToDo.get(1);
            } catch (IndexOutOfBoundsException e) { nextCheckpoint = null; }

            setCheckpointActive(currentCheckpoint);
        } else {
            final long cid = NumberUtils.toLong(UserMetaManager.getMetaValue(UserMetaManager.CURRENT_CHECKPOINT_ID));
            currentCheckpoint = Iterables.find(checkpointsToDo, new Predicate<Checkpoint>() {
                public boolean apply(Checkpoint input) {
                    return input.getId() == cid;
                }
            });
            if (currentCheckpoint == null) {
                Toast.makeText(this, "There was a problem getting your currently assigned checkpoint. " +
                                     "Please try again later.", Toast.LENGTH_LONG).show();
            }

            try {
                nextCheckpoint = checkpointsToDo.get(checkpointsToDo.indexOf(currentCheckpoint) + 1);
            } catch (IndexOutOfBoundsException e) { nextCheckpoint = null; }

            setCheckpointActive(currentCheckpoint);
        }
    }

    public void setCheckpointActive(Checkpoint checkpoint) {
        UserMetaManager.setMetaValue(UserMetaManager.CURRENT_CHECKPOINT_ID, checkpoint.getId());
        MobileAppCommon.log.debug(checkpoint.getChallenge().getChallengeText());
        ((TextView) findViewById(R.id.main_challengetext)).setText(checkpoint.getChallenge().getChallengeText());
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
        // Process the result
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            MobileAppCommon.log.info(result.toString());
            // Try to parse the result as a Checkpoint object
            try {
                ObjectMapper mapper = new ObjectMapper();
                final Checkpoint cp = mapper.readValue(result.getContents(), Checkpoint.class);

                Preconditions.checkState(!cp.equals(currentCheckpoint),
                                         "This is not your currently assigned checkpoint.");

                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... params) {
                        try {
                            MobileAppCommon.log.info("Checking in... (Checkpoint " + cp.getId() + ')');
                            MobileAppCommon.getClient().checkIn(game, cp);
                            new LoadScavengerHuntTask().execute((Void) null);
                        } catch (GeneralServiceException e) {
                            MobileAppCommon.log.error(e.getMessage(), e);
                        }
                        return null;
                    }
                }.execute((Void) null);
            } catch (IOException e) {
                Toast.makeText(this, "Sorry, this QR code is invalid.", Toast.LENGTH_LONG).show();
            } catch (IllegalStateException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
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
            Preconditions.checkNotNull(scavengerHunt);
            player = scPlayer;
            game = scavengerHunt;
            dialog.dismiss();

            onScavengerHuntLoaded();
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
