package com.pradeep.androidintegration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.OptIn;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private final List<VideoModel> videoList;
    private final Context context;
    private final VideoEventListener listener;

    private ExoPlayer exoPlayer;
    private PlayerView currentPlayerView;

    private String currentVideoId;
    private String currentVideoName;
    private String currentVideoUrl;

    public interface VideoEventListener {
        void onVideoSelected(String id, String name, String url, ExoPlayer exoPlayer, long playedPosition);
    }

    public VideoAdapter(Context context, List<VideoModel> videoList, VideoEventListener listener) {
        this.context = context;
        this.videoList = videoList;
        this.listener = listener;
        this.exoPlayer = new ExoPlayer.Builder(context).build();
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    @OptIn(markerClass = UnstableApi.class)
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        VideoModel video = videoList.get(position);
        holder.videoTitle.setText(video.videoTitle);

        holder.playerView.setPlayer(null);
        holder.playerView.setUseController(true);
        holder.playerView.setControllerAutoShow(true);
        holder.playerView.setControllerHideOnTouch(false);

        holder.playerView.setOnClickListener(v -> {
            if (!video.videoId.equals(currentVideoId)) {
                notifyVideoChange();  // save previous progress
                playVideo(holder.playerView, video);
            } else {
                // Toggle controller visibility
                if (holder.playerView.isControllerFullyVisible()) {
                    holder.playerView.hideController();
                } else {
                    holder.playerView.showController();
                }
            }

        });
    }

    private void playVideo(PlayerView newPlayerView, VideoModel video) {
        if (currentPlayerView != null && currentPlayerView != newPlayerView) {
            currentPlayerView.setPlayer(null);
        }

        currentPlayerView = newPlayerView;
        currentVideoId = video.videoId;
        currentVideoName = video.videoTitle;
        currentVideoUrl = video.videoUrl;


        newPlayerView.setPlayer(exoPlayer);
        exoPlayer.setMediaItem(MediaItem.fromUri(video.videoUrl));
        exoPlayer.prepare();
        exoPlayer.play();

        listener.onVideoSelected(currentVideoId, currentVideoName, currentVideoUrl, exoPlayer, 0);
    }

    private void notifyVideoChange() {
        if (currentVideoId != null && exoPlayer != null) {
            long played = exoPlayer.getCurrentPosition();
            listener.onVideoSelected(currentVideoId, currentVideoName, currentVideoUrl, exoPlayer, played);
            exoPlayer.stop();
        }
    }


    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public void releasePlayer() {
        notifyVideoChange(); // save before release
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView videoTitle;
        PlayerView playerView;

        VideoViewHolder(View itemView) {
            super(itemView);
            videoTitle = itemView.findViewById(R.id.videoTitle);
            playerView = itemView.findViewById(R.id.playerView);
        }
    }
}
