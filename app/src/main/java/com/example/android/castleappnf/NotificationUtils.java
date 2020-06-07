package com.example.android.castleappnf;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.core.app.NotificationCompat;



public class NotificationUtils {
    //ID for the notification
    private static final int CASTLE_NOTIFICATION_ID = 1246;
    //ID for the pending intent
    private static final int CASTLE_PENDING_INTENT_ID = 6542;
    //Notification channel ID
    private static final String CASTLE_NOTIFICATION_CHANNEL_ID = "com.example.android.castleappnf.castle_notification_channel";

    //Creates a pending intent which loads the app when it is triggered. This can be associated with the notification to allow the app to load when it is clicked.
    private static PendingIntent getPendingIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context, CASTLE_PENDING_INTENT_ID, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    //Helper method to return a bitmap for the notification image
    private static Bitmap getNotificationIcon(Context context) {
        Resources res = context.getResources();
        Bitmap myIcon = BitmapFactory.decodeResource(res, R.drawable.castle);
        return myIcon;
    }

    //Static method that creates the notification for the app. This is called in the NotificationWorker class
    //so it is scheduled rather than happening at a specific time.
    public static void createNotificationForApp(Context context) {
        //Gets the Notification Manager object from the system.
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //Creates the notification channel. Its importance is low meaning it doesn't pop up on the home screen.
        NotificationChannel myChannel = new NotificationChannel(CASTLE_NOTIFICATION_CHANNEL_ID,
                                                            context.getString(R.string.notification_channel_name),
                                                                    NotificationManager.IMPORTANCE_LOW);
        //Sets the channel description
        myChannel.setDescription(context.getString(R.string.notification_channel_description));
        //Creates notification channel
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(myChannel);
        }
        //Creates the notification to have the bigpicture style
        Notification notification = new Notification.Builder(context, CASTLE_NOTIFICATION_CHANNEL_ID)
                .setLargeIcon(getNotificationIcon(context))
                .setSmallIcon(R.drawable.castle)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_content))
                .setStyle(new Notification.BigPictureStyle()
                        .bigPicture(getNotificationIcon(context)))
                .setContentIntent(getPendingIntent(context))
                .setCategory(NotificationCompat.CATEGORY_PROMO)
                .setAutoCancel(true)
                .build();

        if (notificationManager != null) {
            notificationManager.notify(CASTLE_NOTIFICATION_ID, notification);
        }

    }
}
