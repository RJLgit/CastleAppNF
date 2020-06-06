package com.example.android.castleappnf;

import android.app.Activity;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import androidx.appcompat.app.AppCompatActivity;

//Parent class of both DetailsActivity and MainActivity so they both have connectionreceiver functionality.
public class BaseActivity extends AppCompatActivity {
    //Attachs connection receiver to the activities
    ConnectionReceiver connectionReceiver = new ConnectionReceiver();
    //Variable which records if app is online. 
    public static boolean connected = true;

    //Method that is called by the receiver when the users connection state changes between offline and online. The boolean passed allows the activity to know if
    //the method is being called when the activity was previously online or previously offline. If the app comes online then false will be passed and then
    //Mainactivity can know that the app has come online and can populate the adapter
    public void userConnected(boolean b) {

    }

    //Method over-ridden in activties to show toolbar.
    public void showBottomToolBar() {

    }
    //Method over-ridden in activties to hide toolbar.
    public void hideBottomToolBar() {

    }

    //The ConnectionReceiver is registered in onstart so it only occurs when the app is in the foreground.
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionReceiver, filter);
    }

    //The ConnectionReceiver is unregistered in onstop so it only occurs when the app is in the foreground.
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(connectionReceiver);
    }

    //Returns whether the connected variable is true or false so the previous state of the app can be determined (i.e. whether the app is going from offline to online)
    //Or whether the activity is just being loaded for the first time which will trigger the broadcast because it is registered in onstart. So we want different behaviour
    //for when the activity first registers the receiver (in onstart) and when the broadcast is receiver because the app changes connection state
    public boolean isConnected() {
        return connected;
    }

    //Setter for the variable
    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
