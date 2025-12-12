package com.pradeep.androidintegration;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationCancelReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra("notification_id", -1);
        int summaryId = intent.getIntExtra("summary_id", 0);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null && notificationId != -1) {
            nm.cancel(notificationId);   // cancel individual
            nm.cancel(summaryId);       // cancel summary (safe to call; no-op if not present)
        }
        Log.d("CleverTap", "NotificationCancelReceiver: canceled id=" + notificationId + " summaryId=" + summaryId);
    }
}

