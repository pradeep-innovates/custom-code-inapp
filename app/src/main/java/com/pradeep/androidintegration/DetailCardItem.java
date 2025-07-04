package com.pradeep.androidintegration;

public class DetailCardItem {
    private String title;
    private String url;
    private String eventName;

    public DetailCardItem(String title, String url, String eventName) {
        this.title = title;
        this.url = url;
        this.eventName = eventName;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getEventName() {
        return eventName;
    }
}
