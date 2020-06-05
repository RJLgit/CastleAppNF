package com.example.android.castleappnf;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class NotificationBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Boot completed", Toast.LENGTH_LONG).show();

        Constraints constraints = new Constraints.Builder()
                .build();
        PeriodicWorkRequest workRequest =
                new PeriodicWorkRequest.Builder(NotificationWorker.class, 10, TimeUnit.SECONDS)
                        .build();

        WorkManager.getInstance(context).enqueue(workRequest);
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

    }
}
