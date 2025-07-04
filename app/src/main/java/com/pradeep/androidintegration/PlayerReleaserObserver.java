package com.pradeep.androidintegration;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.media3.exoplayer.ExoPlayer;

public class PlayerReleaserObserver implements DefaultLifecycleObserver {
    private final ExoPlayer player;

    public PlayerReleaserObserver(ExoPlayer player) {
        this.player = player;
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        if (player != null) {
            player.release();
            Log.d("CleverTap", "Player released via lifecycle observer from onDestroy");
        }
    }
}
