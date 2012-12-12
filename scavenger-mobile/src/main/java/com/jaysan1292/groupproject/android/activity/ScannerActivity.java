package com.jaysan1292.groupproject.android.activity;

import android.graphics.Bitmap;
import android.graphics.Interpolator;
import android.os.Bundle;
import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;
import android.widget.Toast;
import com.jaysan1292.groupproject.android.R;

/**
 * Created with IntelliJ IDEA.
 * User: Kazedayz
 * Date: 11/12/12
 * Time: 1:48 PM
 */
public class ScannerActivity extends CaptureActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    public void handleDecode(Interpolator.Result rawResult, Bitmap barcode)
    {
        Toast.makeText(this.getApplicationContext(), "Scanned code "
                + rawResult.getText(), Toast.LENGTH_LONG);
    }



}
