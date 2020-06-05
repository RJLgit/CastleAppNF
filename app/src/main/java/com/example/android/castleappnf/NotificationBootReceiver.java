package com.example.android.castleappnf;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class NotificationBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Boot completed", Toast.LENGTH_LONG).show();

        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .build();
        PeriodicWorkRequest workRequest =
                new PeriodicWorkRequest.Builder(NotificationWorker.class, 1, TimeUnit.SECONDS)
                        //.setInitialDelay(1, TimeUnit.HOURS)
                        .setConstraints(constraints)
                        .build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(NotificationWorker.NOT_TASK_ID,
                ExistingPeriodicWorkPolicy.KEEP, workRequest);
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

    }
}
