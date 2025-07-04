package com.pradeep.androidintegration;

import android.content.Context;
import android.content.SharedPreferences;

public class InboxItem {
    private String messageId; // ✅ Add messageId
    private String title;
    private String message;
    private String imageUrl;
    private String buttonText;
    private String bodyLink;
    private String buttonLink;

    private static final String PREFS_NAME = "InboxPrefs"; // SharedPreferences name
    private static final String VIEWED_MESSAGES_KEY = "ViewedMessages"; // Key for storing viewed messages



    public InboxItem(String messageId, String title, String message, String imageUrl, String buttonText, String bodyLink, String buttonLink) {
        this.messageId = messageId; // ✅ Initialize messageId
        this.title = (title != null && !title.isEmpty()) ? title : null;
        this.message = (message != null && !message.isEmpty()) ? message : null;
        this.imageUrl = imageUrl;
        this.buttonText = (buttonText == null || buttonText.isEmpty()) ? "Click" : buttonText;
        this.bodyLink = bodyLink;
        this.buttonLink = buttonLink;

    }

    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public String getImageUrl() { return imageUrl; }
    public String getButtonText() { return buttonText; }
    public String getBodyLink() { return bodyLink; }
    public String getButtonLink() { return buttonLink; }

    public String getMessageId() {
        return messageId;
    }

    // ✅ Check if the message was already viewed
    public boolean isViewed(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(messageId, false); // Default is false (not viewed)
    }

    // ✅ Mark the message as viewed and save to SharedPreferences
    public void setViewed(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(messageId, true).apply();
    }

}

