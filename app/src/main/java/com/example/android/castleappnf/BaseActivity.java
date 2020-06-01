package com.example.android.castleappnf;

import android.app.Activity;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    ConnectionReceiver connectionReceiver = new ConnectionReceiver();

    public void logIn() {

    }
    public void showBottomToolBar() {

    }

    public void hideBottomToolBar() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(connectionReceiver);
    }
}
