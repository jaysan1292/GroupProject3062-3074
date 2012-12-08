package com.jaysan1292.groupproject.android.activity;

import android.app.Activity;
import android.os.Bundle;
import com.jaysan1292.groupproject.android.R;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class LoginActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        HttpClient client = new DefaultHttpClient();
//        TODO: Create http client bridge class
//        HttpRequest request = new BasicHttpRequest("GET", "http://")
    }
}
