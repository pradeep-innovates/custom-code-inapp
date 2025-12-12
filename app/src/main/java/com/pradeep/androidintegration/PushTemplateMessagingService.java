package com.pradeep.androidintegration;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.pushnotification.fcm.CTFcmMessageHandler;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import pl.droidsonroids.gif.GifDrawable;

public class PushTemplateMessagingService extends FirebaseMessagingService {

    private static final String TAG = "CleverTap";
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

                if (remoteMessage.getData().containsKey("pt_gif")) {
                    Log.d("CleverTap", "Sending GIF Notification");
                    Bundle extras = new Bundle();
                    for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                        extras.putString(entry.getKey(), entry.getValue());
                    }
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    renderGIFNotification(extras); // simple direct call
                } else if (title != null && message != null) {
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

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private void renderGIFNotification(Bundle extras) {
        Log.d("CleverTap", "renderGIFNotification: called");
        try {
            Log.d(TAG, "inside try block");
            int notificationId = new Random().nextInt(60000);

            String gifUrl = extras.getString("pt_gif");
            String title = extras.getString("pt_title");
            String message = extras.getString("pt_msg");
            String deepLink = extras.getString("pt_dl");
            String channelId = extras.getString("wzrk_cid");

//            if (gifUrl == null || title == null || message == null || deepLink == null || channelId == null) {
//                Log.e("GIF_NOTIFICATION", "Missing required extras");
//                return; // just exit
//            }

            if (channelId == null || channelId.isEmpty()) channelId = "custom_gif";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationChannel channel = nm.getNotificationChannel(channelId);
                if (channel == null) {
                    channel = new NotificationChannel(channelId, "GIF Notifications", NotificationManager.IMPORTANCE_HIGH);
                    nm.createNotificationChannel(channel);
                }
            }

            List<Bitmap> frames = extractOptimizedFrames(gifUrl, 15);
            Log.d(TAG, "Frame count is " + frames);
            if (frames.isEmpty()) {
                Log.d("CleverTap", "No frames extracted.");
                return;
            }

            int nightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            int textColor = (nightMode == Configuration.UI_MODE_NIGHT_YES) ? Color.WHITE : Color.BLACK;

            RemoteViews collapsedView = new RemoteViews(getPackageName(), R.layout.gif_collapsed);
            RemoteViews expandedView = new RemoteViews(getPackageName(), R.layout.gif_notification);


            boolean hideRelLayout = TextUtils.isEmpty(title) && TextUtils.isEmpty(message);
            if (hideRelLayout) {
                Log.d(TAG, "title and message not present");
                expandedView.setViewVisibility(R.id.rel_lyt, View.GONE);
            } else {
                Log.d(TAG, "title and message present");
                expandedView.setViewVisibility(R.id.rel_lyt, View.VISIBLE);
            }



            for (Bitmap bitmap : frames) {
                Log.d(TAG, "inside for loop");
                RemoteViews frameView = new RemoteViews(getPackageName(), R.layout.image_view);
                frameView.setImageViewBitmap(R.id.fimg, bitmap);
                collapsedView.addView(R.id.view_flipper, frameView);
                expandedView.addView(R.id.view_flipper, frameView);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                    .setCustomContentView(collapsedView)
//                    .setCustomBigContentView(expandedView)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setOnlyAlertOnce(true)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setAutoCancel(true);

            NotificationManagerCompat.from(getApplicationContext()).notify(notificationId, builder.build());

        } catch (Exception e) {
            Log.d(TAG, "Notification rendering failed", e);
        }
    }

    public static List<Bitmap> extractOptimizedFrames(String gifUrl, int maxFrames) {
        Log.d(TAG, "extractOptimizedFrames: called");
        List<Bitmap> frames = new ArrayList<>();
        try (InputStream inputStream = new BufferedInputStream(new URL(gifUrl).openStream())) {
            GifDrawable gifDrawable = new GifDrawable(inputStream);
            int totalFrames = gifDrawable.getNumberOfFrames();
            Log.d("CleverTap", "Total frames in gif: " + totalFrames);

            if (totalFrames == 0) {
                return frames; // empty
            }

            // Select frame indices
            Set<Integer> indices = new LinkedHashSet<>();
            if (totalFrames <= maxFrames) {
                Log.d(TAG, "inside if");
                for (int i = 0; i < totalFrames; i += 1) indices.add(i);
                indices.add(totalFrames - 1); // ensure last frame included
            } else {
                Log.d(TAG, "inside else");
                indices.add(0);
                indices.add(totalFrames - 1);
                int step = (totalFrames - 2) / (maxFrames - 2);
                for (int i = 1; i < maxFrames - 1; i++) {
                    indices.add(i * step);
                }
            }

            // Extract and scale frames
            for (int index : indices) {
                Bitmap frame = gifDrawable.seekToFrameAndGet(index);
                Bitmap scaled = Bitmap.createScaledBitmap(frame, 400, 200, true); // can adjust size
                frames.add(scaled);
                Log.d(TAG, "Selected frame index: " + index);
            }

            Log.d(TAG, "Total selected frames: " + frames.size());

        } catch (Exception e) {
            Log.e(TAG, "Frame extraction failed: " + e.getMessage(), e);
        }
        return frames;
    }

    private void sendCustomNotification(String title, String message, String imageUrl, String deepLink, Map<String, String> data) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Ensure notification channel exists for Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = notificationManager.getNotificationChannel("custom");
            if (channel == null) {
                channel = new NotificationChannel(
                        "custom", "Custom Notifications", NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }
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
        } else {
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        }


        int notificationId = new Random().nextInt(60000);
        notificationManager.notify(notificationId, builder.build());

        // Log notification view event
        CleverTapAPI cleverTapAPI = CleverTapAPI.getDefaultInstance(this);
        if (cleverTapAPI != null) {
            Bundle notificationData = new Bundle();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                notificationData.putString(entry.getKey(), entry.getValue());
            }
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