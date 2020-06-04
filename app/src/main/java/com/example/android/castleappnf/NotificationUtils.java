package com.example.android.castleappnf;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
    
}
