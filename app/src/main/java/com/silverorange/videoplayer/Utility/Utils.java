package com.silverorange.videoplayer.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.silverorange.videoplayer.R;

// Utility class

public class Utils {

    // Alert dialog box for whole app
    public static void showAlert(final Activity activity, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(activity.getString(R.string.ok), null);
        builder.show();
    }

    // Check internet connection
    public static boolean isNetworkAvailable(final Context context, boolean canShowErrorDialogOnFail, final boolean isFinish) {
        boolean isNetAvailable = false;

        if (context != null) {
            final ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (mConnectivityManager != null) {
                boolean mobileNetwork = false;
                boolean wifiNetwork = false;
                boolean mobileNetworkConnecetd = false;
                boolean wifiNetworkConnecetd = false;

                final NetworkInfo mobileInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                final NetworkInfo wifiInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (mobileInfo != null) {
                    mobileNetwork = mobileInfo.isAvailable();
                }

                if (wifiInfo != null) {
                    wifiNetwork = wifiInfo.isAvailable();
                }

                if (wifiNetwork || mobileNetwork) {
                    if (mobileInfo != null)
                        mobileNetworkConnecetd = mobileInfo
                                .isConnectedOrConnecting();
                    wifiNetworkConnecetd = wifiInfo.isConnectedOrConnecting();
                }

                isNetAvailable = (mobileNetworkConnecetd || wifiNetworkConnecetd);
            }
            //context.setTheme(R.style.AppTheme);
            if (!isNetAvailable && canShowErrorDialogOnFail) {
                Log.v("TAG", "context : " + context.toString());
                if (context instanceof Activity) {
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            showAlertWithFinish((Activity) context, context.getString(R.string.app_name), context.getString(R.string.network_alert), isFinish);
                        }
                    });
                }
            }
        }
        return isNetAvailable;
    }

    // Alert dialog and close the current activity
    public static void showAlertWithFinish(final Activity activity, String title, String message, final boolean isFinish) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isFinish) {
                    dialog.dismiss();
                    activity.finish();
                } else {
                    dialog.dismiss();
                }
            }
        }).show();
    }

}
