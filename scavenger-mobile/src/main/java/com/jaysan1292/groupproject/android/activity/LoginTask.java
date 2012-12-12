package com.jaysan1292.groupproject.android.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;
import com.jaysan1292.groupproject.android.MobileAppCommon;
import com.jaysan1292.groupproject.android.net.ScavengerClient;
import com.jaysan1292.groupproject.service.security.AuthorizationException;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.core.util.Base64;

import java.net.URI;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * Date: 11/12/12
 * Time: 10:12 PM
 *
 * @author Jason Recillo
 */
public class LoginTask extends AsyncTask<String, Void, Boolean> {
    private static final Pattern AUTH_SPLIT = Pattern.compile(":");
    private Context context;
    private ScavengerClient client;
    private ProgressDialog dialog;
    private Exception exception;

    public LoginTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Logging in...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setProgressNumberFormat(null);
        dialog.setProgressPercentFormat(null);
        dialog.show();
    }

    @Override
    protected void onPostExecute(Boolean success) {
        dialog.dismiss();
        if (success) {
            MobileAppCommon.setClient(client);
            MobileAppCommon.sendToMain(context);
        } else {
            String errorMessage = "";
            if (this.exception != null) {
                if (exception instanceof AuthorizationException) {
                    errorMessage = "Sorry, your username or password was incorrect.";
                } else if (exception instanceof ClientHandlerException) {
                    errorMessage = "Looks like you mistyped the service URL. ";
                } else {
                    errorMessage = "An unexpected error occurred.";
                }
            }
            Toast t = Toast.makeText(context, errorMessage, Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER, 0, 0);
            t.show();
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        MobileAppCommon.log.info("Connecting to service.");

        // imaginary network delay :p
        try {Thread.sleep(750);} catch (InterruptedException ignored) {}

        try {
            URI hostUri = URI.create(params[0]);
            if (params.length == 3) {
                // host, username, password
                String user = params[1];
                String pass = params[2];
                // the constructor will automatically check if username/password is valid
                client = new ScavengerClient(hostUri, user, pass);

                String authHeader = new String(Base64.encode(String.format("%s:%s", user, pass)));
                UserMetaManager.setMetaValue(UserMetaManager.USER_AUTH, authHeader);
                UserMetaManager.setMetaValue(UserMetaManager.SERVICE_URI, params[0]);
                return true;
            } else if (params.length == 2) {
                // host, auth HTTP header
                String auth = Base64.base64Decode(params[1]);
                String[] credentials = AUTH_SPLIT.split(auth);
                String user = credentials[0];
                String pass = credentials[1];

                client = new ScavengerClient(hostUri, user, pass);
                return true;
            } else {
                //shouldn't happen
                throw new RuntimeException(String.format("Invalid number of parameters! (%s)", params.length));
            }
        } catch (AuthorizationException e) {
            MobileAppCommon.log.error(e.getMessage(), e);
            this.exception = e;
            return false;
        } catch (ClientHandlerException e) {
            this.exception = e;
            return false;
        }
    }
}
