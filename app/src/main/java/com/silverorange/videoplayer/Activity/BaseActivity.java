package com.silverorange.videoplayer.Activity;

import android.app.Activity;
import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    // Display progress bar for whole app

    public static ProgressDialog progressDialog;

    public static void ShowProgressDialog(Activity activity, String message) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        try {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
