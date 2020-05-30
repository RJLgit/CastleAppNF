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
    private static final int REQUEST_CONN = 172;

    @Override
    public void onReceive(final Context context, Intent intent) {
        final Activity a = (Activity) context;
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            boolean noConnectivity = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false
            );
            if (noConnectivity) {
                Log.d(TAG, "onReceive: " + "conn");
                Toast.makeText(context, "No connection", Toast.LENGTH_SHORT).show();
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("This application requires internet connection to work correctly. Please connect to the internet.")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                Intent connectionIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                a.startActivityForResult(connectionIntent, REQUEST_CONN);
                            }
                        })
                .setNegativeButton("Close app", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        a.finishAffinity();
                    }
                });
                final AlertDialog alert = builder.create();
                alert.show();
            } else {
                Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
