package com.pradeep.androidintegration;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "video_progress")
public class VideoProgressEntity {

    @PrimaryKey
    @NonNull
    public String videoId;

    public String videoName;
    public String videoUrl; // ⬅️ Add this field

    public long totalDuration;
    public long playedDuration;

    public VideoProgressEntity(@NonNull String videoId, String videoName, String videoUrl, long totalDuration, long playedDuration) {
        this.videoId = videoId;
        this.videoName = videoName;
        this.videoUrl = videoUrl;
        this.totalDuration = totalDuration;
        this.playedDuration = playedDuration;
    }
}

