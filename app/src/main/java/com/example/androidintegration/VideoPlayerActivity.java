package com.example.androidintegration;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class VideoPlayerActivity extends AppCompatActivity {

    private VideoAdapter adapter;
    private ExoPlayer player;
    private String selectedVideoId;
    private String selectedVideoName;
    private String selectedVideoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video_player);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.videoRecyclerView);

        List<VideoModel> videoList = new ArrayList<>();
        videoList.add(new VideoModel("video1", "Shark Tank India", "https://biswadeepmondal.github.io/Shark%20Tank%20India%20Season%204%20_%20Streaming%20from%206th%20Jan%20_%20Exclusively%20on%20Sony%20LIV.mp4"));
        videoList.add(new VideoModel("video2", "Standup Comedy", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"));
        videoList.add(new VideoModel("video3", "Tech Talk", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"));
        videoList.add(new VideoModel("video4", "Short Film", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"));

        adapter = new VideoAdapter(this, videoList, new VideoAdapter.VideoEventListener() {
            @Override
            public void onVideoSelected(String id, String name, String url, ExoPlayer exoPlayer, long playedPosition) {
                if (selectedVideoId != null && !selectedVideoId.equals(id)) {
                    saveProgress(); // Save progress of previous video
                }

                selectedVideoId = id;
                selectedVideoName = name;
                selectedVideoUrl = url;
                player = exoPlayer;
            }
        });

        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onStop() {
        super.onStop();
        saveProgress();
        releasePlayer(); // always good to release here
        // Log the database contents
        logDatabaseContents();
    }

    private void saveProgress() {
        if (player == null || selectedVideoId == null) return;

        long currentPosition = player.getCurrentPosition();
        long totalDuration = player.getDuration();

        Log.d("CleverTap", "Saving progress: " + currentPosition + " / " + totalDuration);

        VideoProgressEntity entity = new VideoProgressEntity(
                selectedVideoId,
                selectedVideoName,
                selectedVideoUrl,
                totalDuration,
                currentPosition
        );

        Executors.newSingleThreadExecutor().execute(() -> {
            DatabaseClient.getInstance(getApplicationContext())
                    .getAppDatabase()
                    .videoProgressDao()
                    .insertProgress(entity);
        });


    }

    private void releasePlayer() {
        if (adapter != null) {
            adapter.releasePlayer();
        }
        player = null;
    }


    private void logDatabaseContents() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<VideoProgressEntity> progressList = DatabaseClient.getInstance(getApplicationContext())
                    .getAppDatabase()
                    .videoProgressDao()
                    .getAllProgress();

            for (VideoProgressEntity progress : progressList) {
                Log.d("CleverTap", "Video ID: " + progress.videoId +
                        ", Name: " + progress.videoName +
                        ", URL: " + progress.videoUrl +
                        ", Total Duration: " + progress.totalDuration +
                        ", Played Duration: " + progress.playedDuration);
            }
        });
    }


}