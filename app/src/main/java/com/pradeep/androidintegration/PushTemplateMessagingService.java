package com.pradeep.androidintegration;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.pushnotification.fcm.CTFcmMessageHandler;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class PushTemplateMessagingService extends FirebaseMessagingService {

    public CleverTapAPI cleverTapDefaultInstance;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("CleverTap", "Push received: " + remoteMessage.getData());

        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(this);


        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();
            // ✅ Add log to check received payload
            Log.d("CleverTap", "Received push payload: " + data.toString());

            boolean isCustom = Boolean.parseBoolean(data.getOrDefault("isCustom", "false"));

            if (isCustom) {
                // Handle custom notification

                String title = data.containsKey("pt_title") ? data.get("pt_title") : data.get("nt");
                String message = data.containsKey("pt_msg") ? data.get("pt_msg") : data.get("nm");
                String imageUrl = data.get("pt_big_img");
                String deepLink = data.get("pt_dl1"); // Get deep link

                if (title != null && message != null) {
                    Log.d("CleverTap", "Sending Custom Notification");
                    sendCustomNotification(title, message, imageUrl, deepLink, data);
                } else {
                    Log.d("FCM", "Invalid notification data, skipping.");
                }
            } else {
                // Use CleverTap for notifications
                Log.d("CleverTap", "Using clevertap");
                new CTFcmMessageHandler().createNotification(getApplicationContext(), remoteMessage);
            }
        }
    }

    private void sendCustomNotification(String title, String message, String imageUrl, String deepLink, Map<String, String> data) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Ensure notification channel exists for Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "custom", "Custom Notifications", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        // Fetch image from URL
        Bitmap imageBitmap = getBitmapFromURL(imageUrl);

        // Create an intent for notification click
        Intent intent = new Intent(this, NotificationClickHandler.class); // Use a separate activity or receiver
        intent.putExtra("pt_dl1", deepLink);

        // Add notification payload for CleverTap tracking
        for (Map.Entry<String, String> entry : data.entrySet()) {
            intent.putExtra(entry.getKey(), entry.getValue());
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "custom")
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        // Add image if available
        if (imageBitmap != null) {
            builder.setStyle(new NotificationCompat.BigPictureStyle()
                    .bigPicture(imageBitmap)
                    .bigLargeIcon((Bitmap) null));
        }

        int notificationId = (int) System.currentTimeMillis();
        notificationManager.notify(notificationId, builder.build());

        // Log notification view event
        CleverTapAPI cleverTapAPI = CleverTapAPI.getDefaultInstance(this);
        if (cleverTapAPI != null) {
            Bundle notificationData = new Bundle();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                notificationData.putString(entry.getKey(), entry.getValue());
            }
//            notificationData.putString("wzrk_pn", "true");
            Log.d("CleverTap", "Sending pushNotificationViewedEvent: " + notificationData);
            cleverTapAPI.pushNotificationViewedEvent(notificationData);
        }
    }




    // Helper method to fetch bitmap from image URL
    private static Bitmap getBitmapFromURL(String imageUrl) {
        try {
            // 🔥 Fix incorrect URL format
            if (imageUrl == null || !imageUrl.startsWith("http")) {
                Log.e("NotificationHelper", "Invalid image URL: " + imageUrl);
                return null;
            }

            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setConnectTimeout(5000); // 5 seconds timeout
            connection.setReadTimeout(5000);
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                Log.e("NotificationHelper", "Error: HTTP " + responseCode);
                return null;
            }

            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            Log.e("NotificationHelper", "Error loading image: " + e.getMessage());
            return null;
        }
    }
}
