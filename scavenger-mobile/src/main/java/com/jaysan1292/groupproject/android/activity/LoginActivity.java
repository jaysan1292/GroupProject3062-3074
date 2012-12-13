package com.jaysan1292.groupproject.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import com.jaysan1292.groupproject.android.R;

public class LoginActivity extends Activity implements View.OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        findViewById(R.id.login_signin_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        TextView usernameView = (TextView) findViewById(R.id.login_username);
        TextView passwordView = (TextView) findViewById(R.id.login_password);
        TextView hostView = (TextView) findViewById(R.id.login_dev_host);

        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();
        String host = hostView.getText().toString();

        // Hide on-screen keyboard
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(usernameView.getWindowToken(), 0);
        new LoginTask(this).execute(host, username, password);
    }
}
