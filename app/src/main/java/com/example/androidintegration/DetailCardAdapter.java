package com.example.androidintegration;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clevertap.android.sdk.CleverTapAPI;

import java.util.List;

public class DetailCardAdapter extends RecyclerView.Adapter<DetailCardAdapter.ViewHolder> {

    private Context context;
    private List<DetailCardItem> detailList;

    public DetailCardAdapter(Context context, List<DetailCardItem> detailList) {
        this.context = context;
        this.detailList = detailList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageButton actionBtn;
        View cardView;

        public ViewHolder(View view) {
            super(view);
            cardView = view;
            title = view.findViewById(R.id.detailTitle);
            actionBtn = view.findViewById(R.id.playButton);
        }
    }

    @NonNull
    @Override
    public DetailCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_detail_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailCardAdapter.ViewHolder holder, int position) {
        DetailCardItem item = detailList.get(position);

        holder.title.setText(item.getTitle());

        // Handle card click (fires CleverTap event)
        holder.cardView.setOnClickListener(v -> {
            // Fire CleverTap event when the card is clicked
            CleverTapAPI.getDefaultInstance(context).pushEvent(item.getEventName());
        });

        holder.actionBtn.setOnClickListener(v -> {
            // Open the URL
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return detailList.size();
    }
}
