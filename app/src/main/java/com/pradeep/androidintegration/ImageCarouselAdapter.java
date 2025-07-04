package com.pradeep.androidintegration;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.clevertap.android.sdk.CleverTapAPI;

import java.util.HashMap;
import java.util.List;

public class ImageCarouselAdapter extends RecyclerView.Adapter<ImageCarouselAdapter.ViewHolder> {
    private List<String> imageUrls;

    private List<String> unitIds;
    private List<String> actionUrls; // List of action URLs
//    private List<String> titles;
//    private List<String> messages;

    private Context context;

    public ImageCarouselAdapter(Context context, List<String> imageUrls, List<String> unitIds, List<String> actionUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.unitIds = unitIds;
        this.actionUrls = actionUrls;
//        this.titles = titles;
//        this.messages = messages;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.carousel_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String imageUrl = imageUrls.get(position);
        String unitId = unitIds.get(position); // Get corresponding unit ID
        String actionUrl = actionUrls.get(position); // Get corresponding action URL
//        String title = titles.get(position);
//        String message = messages.get(position);
        int displayPosition = position + 1; // Convert to 1-based position


//        // Set title and message
//        if (title != null && !title.isEmpty()) {
//            Log.d("CleverTap", "Title at position " + position + ": " + title);
//            holder.titleView.setText(title);
//            holder.titleView.setVisibility(View.VISIBLE);
//        } else {
//            Log.d("CleverTap", "Title at position " + position + " is empty or null");
//            holder.titleView.setVisibility(View.GONE);
//        }
//
//        if (message != null && !message.isEmpty()) {
//            Log.d("CleverTap", "Message at position " + position + ": " + message);
//            holder.messageView.setText(message);
//            holder.messageView.setVisibility(View.VISIBLE);
//        } else {
//            Log.d("CleverTap", "Message at position " + position + " is empty or null");
//            holder.messageView.setVisibility(View.GONE);
//        }


        Glide.with(context)
                .load(imageUrls.get(position))
                .into(holder.imageView);


        // Track "Clicked" event dynamically
        holder.imageView.setOnClickListener(v -> {
            CleverTapAPI.getDefaultInstance(context).pushDisplayUnitClickedEventForID(unitId);

            // Create a map to hold event properties
            HashMap<String, Object> eventProperties = new HashMap<>();
            eventProperties.put("Image URL", imageUrl);
            eventProperties.put("Position", displayPosition);

            CleverTapAPI.getDefaultInstance(context).pushEvent("Native Display Clicked", eventProperties);
            Log.d("CleverTap", "Image clicked: " + imageUrl + " with Unit ID: " + unitId + " at Position: " + displayPosition);

            // Open the action URL
            if (actionUrl != null && !actionUrl.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(actionUrl));
                intent.putExtra("imageUrl", imageUrl);
                intent.putExtra("position", displayPosition);
                context.startActivity(intent);
            }

        });
    }


    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView;
        TextView messageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.carouselImage);
//            titleView = itemView.findViewById(R.id.native_title);
//            messageView = itemView.findViewById(R.id.native_message);
        }
    }
}

