package com.pradeep.androidintegration;

//import android.app.NotificationManager;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//
//public class NotificationUtils {
//
//    //Require to close notification on action button click
//    public static void dismissNotification(Intent intent, Context applicationContext){
//        if (intent == null) return;
//
//        Bundle extras = intent.getExtras();
//        if (extras != null) {
//            String actionId = extras.getString("actionId");
//            if (actionId != null) {
//                Log.d("CleverTap", "dismissNotification: inside actionId : " + actionId);
//                boolean autoCancel = extras.getBoolean("autoCancel", true);
//                int notificationId = extras.getInt("notificationId", -1);
//                if (autoCancel && notificationId > -1) {
//                    Log.d("CleverTap", "dismissNotification: inside NotificationId : " + notificationId);
//                    NotificationManager notifyMgr =
//                            (NotificationManager) applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
//                    notifyMgr.cancel(notificationId);                }
//
//            }
//        }
//    }
//}

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.clevertap.android.pushtemplates.PTConstants;

public class NotificationUtils {

    //Require to close notification on action button click
//    public static void dismissNotification(Intent intent, Context applicationContext){
//        Log.d("CleverTap", "dismissNotification: called");
//        Bundle extras = intent.getExtras();
//        if (extras != null) {
//            String actionId = extras.getString("actionId");
//            if (actionId != null) {
//                boolean autoCancel = extras.getBoolean("autoCancel", true);
//                int notificationId = extras.getInt("notificationId", -1);
//                if (autoCancel && notificationId > -1) {
//                    NotificationManager notifyMgr =
//                            (NotificationManager) applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
//                    notifyMgr.cancel(notificationId);                }
//
//            }
//        }
//    }
    public static void dismissNotification(Intent intent, Context applicationContext) {
        Log.d("CleverTap", "dismissNotification: called");
        if (intent == null || applicationContext == null) return;

        Bundle extras = intent.getExtras();
        if (extras == null) return;

        boolean autoCancel = true;
        int notificationId = -1;

        String actionId = extras.getString("actionId");
        Log.d("CleverTap", "actionId: " + actionId);
        if (actionId != null) {
            Log.d("CleverTap", actionId);
            autoCancel = extras.getBoolean("autoCancel", true);
            notificationId = extras.getInt("notificationId", -1);
        }

        /**
         * If using InputBox template, add ptDismissOnClick flag to not dismiss notification
         * if pt_dismiss_on_click is false in InputBox template payload. Alternatively, if a normal
         * notification is raised then we dismiss notification.
         */
        String ptDismissOnClick = extras.getString(PTConstants.PT_DISMISS_ON_CLICK, "");
        Log.d("CleverTap", "ptDismissOnClick: " + ptDismissOnClick);
        Log.d("CleverTap", "autoCancel: " + autoCancel);
        Log.d("CleverTap", "notificationId: " + notificationId);

        if (autoCancel && notificationId > -1 && "true".equalsIgnoreCase(ptDismissOnClick)) {
            Log.d("CleverTap", "dismissNotification: inside autocancel");
            NotificationManager notifyMgr =
                    (NotificationManager) applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notifyMgr != null) {
                notifyMgr.cancel(notificationId);
                Log.d("CleverTap", "Notification " + notificationId + " dismissed");
            }
        }
    }

}