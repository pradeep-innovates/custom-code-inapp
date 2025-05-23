package com.example.androidintegration;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class NotificationUtils {

    //Require to close notification on action button click
    public static void dismissNotification(Intent intent, Context applicationContext){
        if (intent == null) return;

        Bundle extras = intent.getExtras();
        if (extras != null) {
            String actionId = extras.getString("actionId");
            if (actionId != null) {
                Log.d("CleverTap", "dismissNotification: inside actionId : " + actionId);
                boolean autoCancel = extras.getBoolean("autoCancel", true);
                int notificationId = extras.getInt("notificationId", -1);
                if (autoCancel && notificationId > -1) {
                    Log.d("CleverTap", "dismissNotification: inside NotificationId : " + notificationId);
                    NotificationManager notifyMgr =
                            (NotificationManager) applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
                    notifyMgr.cancel(notificationId);                }

            }
        }
    }
}
