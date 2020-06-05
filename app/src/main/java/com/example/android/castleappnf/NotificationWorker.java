package com.example.android.castleappnf;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NotificationWorker extends Worker {

    Context context;
    private static final String TAG = "NotificationWorker";
    public static final String NOT_TASK_ID = "castle_not";

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        Log.d(TAG, "NotificationWorker: ");
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: ");
        NotificationUtils.createNotificationForApp(context);
        return Result.success();
    }


}
