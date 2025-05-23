package com.example.androidintegration;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
import androidx.lifecycle.LiveData;


@Dao
public interface VideoProgressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProgress(VideoProgressEntity progress);

    @Query("SELECT * FROM video_progress WHERE videoId = :videoId")
    LiveData<VideoProgressEntity> getProgress(String videoId);

    @Query("SELECT * FROM video_progress WHERE videoId = :videoId LIMIT 1")  // Synchronous query
    VideoProgressEntity getProgressSync(String videoId);  // Returns VideoProgressEntity directly


    @Query("SELECT * FROM video_progress")  // Fetch all video progress records
    List<VideoProgressEntity> getAllProgress();


}

