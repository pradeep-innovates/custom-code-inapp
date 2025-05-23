package com.example.androidintegration;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clevertap.android.sdk.CleverTapAPI;

import java.util.List;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxViewHolder> {

    private CleverTapAPI cleverTapDefaultInstance;
    private Context context;
    private List<InboxItem> inboxList;

    public InboxAdapter(Context context, List<InboxItem> inboxList) {
        this.context = context;
        this.inboxList = inboxList;
    }

    @NonNull
    @Override
    public InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_inbox, parent, false);
        return new InboxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InboxViewHolder holder, int position) {
        InboxItem item = inboxList.get(position);

        // ✅ Title handling: Hide if missing
        if (item.getTitle() != null && !item.getTitle().isEmpty()) {
            holder.title.setText(item.getTitle());
            holder.title.setVisibility(View.VISIBLE);
        } else {
            holder.title.setVisibility(View.GONE);
        }

        // ✅ Message handling: Hide if missing
        if (item.getMessage() != null && !item.getMessage().isEmpty()) {
            holder.message.setText(item.getMessage());
            holder.message.setVisibility(View.VISIBLE);
        } else {
            holder.message.setVisibility(View.GONE);
        }

        // ✅ Button handling: Set text & click event, hide if missing
        if (item.getButtonText() != null && !item.getButtonText().isEmpty()
                && item.getButtonLink() != null && !item.getButtonLink().isEmpty()) {
            holder.button.setText(item.getButtonText());
            holder.button.setOnClickListener(v -> openUrl(item.getButtonLink()));
            holder.button.setVisibility(View.VISIBLE);
        } else {
            holder.button.setVisibility(View.GONE);
        }


        // Handle missing image
        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(item.getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder_image)  // Add a placeholder image in drawable
                    .error(R.drawable.placeholder_image)  // If image fails to load
                    .into(holder.image);
        } else {
            holder.image.setVisibility(View.GONE);  // Hide image if URL is missing
        }


        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(context);

        // ✅ Raise "Notification Viewed" event ONLY if the message was never viewed
        if (cleverTapDefaultInstance != null && !item.isViewed(context)) {
            cleverTapDefaultInstance.pushInboxNotificationViewedEvent(item.getMessageId());
            item.setViewed(context); // Persist viewed state
        }

        // ✅ Mark as read if it's unread and visible
        if (cleverTapDefaultInstance != null) {
            cleverTapDefaultInstance.markReadInboxMessage(item.getMessageId());
        }


        // Open body link when clicking on the message container
        holder.itemView.setOnClickListener(v -> {
            if (cleverTapDefaultInstance != null && item.getMessageId() != null && !item.getMessageId().isEmpty()) {
                cleverTapDefaultInstance.pushInboxNotificationClickedEvent(item.getMessageId()); // ✅ Raise event
            }

            if (item.getBodyLink() != null && !item.getBodyLink().isEmpty()) {
                openUrl(item.getBodyLink());
            }
        });

        // Open button link when clicking the button
        holder.button.setOnClickListener(v -> {
            if (cleverTapDefaultInstance != null && item.getMessageId() != null && !item.getMessageId().isEmpty()) {
                cleverTapDefaultInstance.pushInboxNotificationClickedEvent(item.getMessageId()); // ✅ Raise event
            }

            if (item.getButtonLink() != null && !item.getButtonLink().isEmpty()) {
                openUrl(item.getButtonLink());
            }
        });
    }

    @Override
    public int getItemCount() {
        return inboxList.size();
    }

    public static class InboxViewHolder extends RecyclerView.ViewHolder {
        TextView title, message;
        ImageView image;
        Button button;

        public InboxViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            message = itemView.findViewById(R.id.item_message);
            image = itemView.findViewById(R.id.item_image);
            button = itemView.findViewById(R.id.item_button);
        }
    }

    // Open a URL in a browser
    private void openUrl(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Open in a new activity
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Invalid URL", Toast.LENGTH_SHORT).show();
        }
    }

}
