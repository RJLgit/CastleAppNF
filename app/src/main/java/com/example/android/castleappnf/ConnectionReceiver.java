package com.example.android.castleappnf;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

public class ConnectionReceiver extends BroadcastReceiver {

    private static final String TAG = "ConnectionReceiver";
    //Variables to show alert dialog in activity when device is offline
    private AlertDialog.Builder builder;
    private AlertDialog alert;

    @Override
    public void onReceive(final Context context, Intent intent) {
        //The connection receiver is registered in BaseActivity so the context is of this type
        final BaseActivity a = (BaseActivity) context;
        //Checks if the intent action is a connectivity action
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            //No connectivity is allocated to true if there is no connection
            boolean noConnectivity = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false
            );
            Toast.makeText(context, "No connection", Toast.LENGTH_SHORT).show();
            //If builder does not exist then build a new one
            if (builder == null) {
                builder = new AlertDialog.Builder(context);
                builder.setMessage("This application requires internet connection to work correctly. Please connect to the internet.")
                        .setCancelable(true)
                        //This gives an option to continue in offline mode for the user, which dismisses the dialog
                        .setPositiveButton("Continue in offline mode", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alert.dismiss();
                            }
                        })
                        //User can also close the app
                        .setNegativeButton("Close app", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //This closes the app
                                a.finishAffinity();
                            }
                        });

            }
            //Create the alert if the alert does not exist.
            if (alert == null) {
                alert = builder.create();
            }

            //If there is no connection then set the connected variable in the activity to false, show the alert and show the toolbar to keep the user updated.
            if (noConnectivity) {
                Log.d(TAG, "onReceive: " + "conn");
                a.setConnected(false);
                Log.d(TAG, "onReceive: " + a.isConnected());
                alert.show();
                a.showBottomToolBar();
            } else if (!noConnectivity) {
                //If there is a connection then dismiss the alert and hide the toolbar. Call userConnected() in the activity, which only does something
                //in MainActivity. The boolean passed will be false if the activity was previously offline. This updates the recycler view UI with data
                //from firebase. The connected vairable then needs to be set to true in the activity to show the device is online. Meaning if the connection
                //changes between different forms of online then the recyclerview isnt updated because it doesnt need to be.
                Log.d(TAG, "onReceive: " + alert);
                alert.dismiss();
                a.hideBottomToolBar();
                a.userConnected(a.isConnected());
                a.setConnected(true);
                Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
