package com.pradeep.androidintegration;

public class CardItem {
    private String title;
    private String description;
    private String buttonText;
    private String cardUrl;
    private String buttonUrl;
    private Class<?> targetActivity;  // Field to store the target activity class


    public CardItem(String title, String description, String buttonText, String cardUrl, String buttonUrl,  Class<?> targetActivity) {
        this.title = title;
        this.description = description;
        this.buttonText = buttonText;
        this.cardUrl = cardUrl;
        this.buttonUrl = buttonUrl;
        this.targetActivity = targetActivity;  // Set the target activity
    }

    public String getTitle() { return title; }

    public String getDescription() { return description; }

    public String getButtonText() { return buttonText; }

    public String getCardUrl() { return cardUrl; }

    public String getButtonUrl() { return buttonUrl; }

    public Class<?> getTargetActivity() {
        return targetActivity;  // Return the target activity class
    }
}

