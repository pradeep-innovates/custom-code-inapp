package com.pradeep.androidintegration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.clevertap.android.sdk.CleverTapAPI;

public class NotificationClickHandler extends Activity {
    public CleverTapAPI cleverTapDefaultInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());


        Log.d("CleverTap", "Notification Handler Called!");

        // Get notification data
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            StringBuilder logMessage = new StringBuilder("Notification Data Received: { ");

            for (String key : extras.keySet()) {
                logMessage.append(key).append(": ").append(extras.get(key)).append(", ");
            }

            // Remove the last comma and space, then close the bracket
            if (logMessage.length() > 2) {
                logMessage.setLength(logMessage.length() - 2);
            }
            logMessage.append(" }");

            Log.d("NotificationClickHandler", logMessage.toString());

            if (cleverTapDefaultInstance != null) {
                Log.d("CleverTap", "Sending pushNotificationClickedEvecleverTapDefaultInstancent with data: " + extras.toString());
                cleverTapDefaultInstance.pushNotificationClickedEvent(extras);
            }
        }

        Intent intent = getIntent();

        // Handle deep linking or open MainActivity if no deep link is provided
        String deepLink = intent.getStringExtra("pt_dl1");
        if (deepLink != null && !deepLink.isEmpty()) {
            Log.d("CleverTap", "Notification Clicked Deeplink Called : " + deepLink);

                Intent mainIntent = new Intent(this, MainActivity.class);
                mainIntent.putExtra("DEEPLINK_URL", deepLink);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);

        } else {
            Intent openIntent = new Intent(this, MainActivity.class); // Fallback to MainActivity
            openIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(openIntent);
        }

        finish(); // Close this activity after handling the click
    }

}
