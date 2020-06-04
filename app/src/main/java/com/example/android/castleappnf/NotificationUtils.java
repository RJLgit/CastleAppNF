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
    private static final int CASTLE_NOTIFICATION_ID = 1246;

    private static final int CASTLE_PENDING_INTENT_ID = 6542;

    private static final String CASTLE_NOTIFICATION_CHANNEL_ID = "castle_notification_channel";

    private static PendingIntent getPendingIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context, CASTLE_PENDING_INTENT_ID, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Bitmap getNotificationIcon(Context context) {
        Resources res = context.getResources();
        Bitmap myIcon = BitmapFactory.decodeResource(res, R.drawable.castle);
        return myIcon;
    }

    public static void createNotificationForApp(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel myChannel = new NotificationChannel(CASTLE_NOTIFICATION_CHANNEL_ID,
                                                            "My notification channel",
                                                                    NotificationManager.IMPORTANCE_LOW);

        myChannel.setDescription("Channel to receive weekly notifications about castles");
        notificationManager.createNotificationChannel(myChannel);
        Notification notification = new Notification.Builder(context, CASTLE_NOTIFICATION_CHANNEL_ID)
                .setLargeIcon(getNotificationIcon(context))
                .setSmallIcon(R.drawable.castle)
                .setContentTitle("Castle App")
                .setContentText("We now have 24 castles to view")
                .setStyle(new Notification.BigPictureStyle()
                        .bigPicture(getNotificationIcon(context)))
                .setContentIntent(getPendingIntent(context))
                .setCategory(NotificationCompat.CATEGORY_PROMO)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(CASTLE_NOTIFICATION_ID, notification);

    }



    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
