package com.example.android.castleappnf;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

//Custom Worker class which creates the notification. This class is used to create the periodic worker request in the Main activity class.
//This class handles all the background work to handle the posting of notifications even when the app isn't running
public class NotificationWorker extends Worker {

    Context context;
    private static final String TAG = "NotificationWorker";
    public static final String NOT_TASK_ID = "castle_not";

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        Log.d(TAG, "NotificationWorker: ");
    }

    //This method defines the actions that the Worker object needs to carry out
    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: ");
        NotificationUtils.createNotificationForApp(context);
        return Result.success();
    }


}
