package com.example.android.castleappnf;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class ConnectionReceiver extends BroadcastReceiver {
    private static final String TAG = "ConnectionReceiver";
    public static final int REQUEST_CONN = 172;
    private AlertDialog.Builder builder;
    private AlertDialog alert;

    @Override
    public void onReceive(final Context context, Intent intent) {
        final BaseActivity a = (BaseActivity) context;
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            boolean noConnectivity = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false
            );
            Toast.makeText(context, "No connection", Toast.LENGTH_SHORT).show();
            if (builder == null) {
                builder = new AlertDialog.Builder(context);
                builder.setMessage("This application requires internet connection to work correctly. Please connect to the internet.")
                        .setCancelable(true)
                        .setPositiveButton("Continue in offline mode", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alert.dismiss();
                            }
                        })
                        .setNegativeButton("Close app", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                a.finishAffinity();
                            }
                        });

            }
            if (alert == null) {
                alert = builder.create();
            }


            if (noConnectivity) {
                Log.d(TAG, "onReceive: " + "conn");

                alert.show();
                a.showBottomToolBar();
            } else if (!noConnectivity) {
                Log.d(TAG, "onReceive: " + alert);
                alert.dismiss();
                a.hideBottomToolBar();
                a.logIn();
                Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
