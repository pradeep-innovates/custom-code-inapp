package com.example.androidintegration;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clevertap.android.sdk.ActivityLifecycleCallback;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.InboxMessageListener;
import com.clevertap.android.sdk.inbox.CTInboxMessage;
import com.clevertap.android.sdk.inbox.CTInboxMessageContent;
import com.clevertap.android.sdk.pushnotification.CTPushNotificationListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InboxActivity extends AppCompatActivity implements InboxMessageListener {
    public CleverTapAPI cleverTapDefaultInstance;

    private RecyclerView recyclerView;
    private InboxAdapter adapter;
    private List<InboxItem> inboxList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, "onCreate Inbox Called", Toast.LENGTH_SHORT).show();
        ActivityLifecycleCallback.register(getApplication());
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inbox);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(this);

//        cleverTapDefaultInstance.pushEvent("fullgif");

        // Set up Toolbar as ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable back button
            SpannableString s = new SpannableString("Custom App Inbox");
            s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            getSupportActionBar().setTitle(s); // Set colored title
        }

        // Handle back button click
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close activity and return to the previous screen
            }
        });



        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (cleverTapDefaultInstance != null) {
            cleverTapDefaultInstance.initializeInbox();
            Log.d("CleverTap", "Inbox is Initialized");
            loadInboxMessages(); // Fetch messages from CleverTap
        } else {
            Log.d("CleverTap", "I was never here");
        }
    }

    private void loadInboxMessages() {
        Log.d("CleverTap", "loadInboxMessages is working");
        inboxList = new ArrayList<>();
        if (cleverTapDefaultInstance != null) {
//            cleverTapDefaultInstance.initializeInbox();
            Log.d("CleverTap", "Inside of Inbox");
            List<CTInboxMessage> messages = cleverTapDefaultInstance.getAllInboxMessages();

            for (CTInboxMessage message : messages) {
                String messageId = message.getMessageId(); // ✅ Extract message ID
                String title = message.getInboxMessageContents().get(0).getTitle();
                String messageText = message.getInboxMessageContents().get(0).getMessage();
                String imageUrl = message.getInboxMessageContents().get(0).getMedia(); // Fetch image
                String buttonText = "Click Here"; // Default button text

                String bodyLink = "";
                String buttonLink = "";

                try {
                    if (message.getInboxMessageContents() != null && !message.getInboxMessageContents().isEmpty()) {
                        CTInboxMessageContent content = message.getInboxMessageContents().get(0);

                        if (content.getLinks() != null && content.getLinks().length() > 0) {
                            JSONObject firstLink = content.getLinks().getJSONObject(0);

                            // Extract button text if it exists
                            if (firstLink.has("text") && !firstLink.getString("text").isEmpty()) {
                                buttonText = firstLink.getString("text");
                            }
                        }

                        // ✅ Extract body link (Call-To-Action URL)
                        if (content.getActionUrl() != null && !content.getActionUrl().isEmpty()) {
                            bodyLink = content.getActionUrl();
                        }

                        // ✅ Extract button link (First link in 'links' array, if available)
                        if (content.getLinks() != null && content.getLinks().length() > 0) {
                            JSONObject firstLink = content.getLinks().getJSONObject(0);

                            if (firstLink.has("url")) {
                                JSONObject urlObject = firstLink.getJSONObject("url");

                                if (urlObject.has("android")) { // Ensure 'android' object exists
                                    JSONObject androidObject = urlObject.getJSONObject("android");

                                    if (androidObject.has("text")) { // Get the actual link
                                        buttonLink = androidObject.getString("text");
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("InboxActivity", "Error parsing links from message: " + e.getMessage());
                }

//                cleverTapDefaultInstance.markReadInboxMessage(messageId);

                inboxList.add(new InboxItem(messageId, title, messageText, imageUrl, buttonText, bodyLink, buttonLink));
            }
        }

        adapter = new InboxAdapter(this, inboxList);
        recyclerView.setAdapter(adapter);

//        updateMainActivityUnreadCount();

    }

    @Override
    public void onInboxItemClicked(CTInboxMessage message, int contentPageIndex, int buttonIndex) {
        Log.d("CleverTap", "onInboxItemClicked: called");
    }

//    private void updateMainActivityUnreadCount() {
//        if (cleverTapDefaultInstance != null) {
//            Log.d("CleverTap", "updateMainActivityUnreadCount: called");
//            int unreadCount = cleverTapDefaultInstance.getInboxMessageUnreadCount();
//            Log.d("CleverTap", "Unread count: " + unreadCount);
//            Intent intent = new Intent("UPDATE_UNREAD_COUNT");
//            intent.putExtra("unreadCount", unreadCount);
//            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//        } else {
//            Log.e("CleverTap", "cleverTapDefaultInstance is null, cannot fetch unread count");
//        }
//    }


//    @Override
//    public void onNotificationClickedPayloadReceived(HashMap<String, Object> hashMap) {
//        Log.d("CleverTap", "Notification is clicked in Inbox: " + hashMap.toString());
//    }
}