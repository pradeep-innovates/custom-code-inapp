package com.example.androidintegration;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private Context context;
    private List<CardItem> itemList;

    public CardAdapter(Context context, List<CardItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, descriptionText;
        Button actionButton;
        View cardView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView;
            titleText = itemView.findViewById(R.id.titleText);
            descriptionText = itemView.findViewById(R.id.descriptionText);
            actionButton = itemView.findViewById(R.id.actionButton);
        }
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardItem item = itemList.get(position);

        holder.titleText.setText(item.getTitle());
        holder.descriptionText.setText(item.getDescription());
        holder.actionButton.setText(item.getButtonText());


        // Animate card appearance
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_slide_in);
        holder.cardView.startAnimation(animation);

        // Handle card click
        holder.cardView.setOnClickListener(v -> {
            // Get the target activity class and start the activity
            Intent intent = new Intent(context, item.getTargetActivity());

            intent.putExtra("category", item.getTitle());

            context.startActivity(intent);
        });

        // Handle button click
        holder.actionButton.setOnClickListener(v -> {
            Animation bounce = AnimationUtils.loadAnimation(context, R.anim.bounce);
            v.startAnimation(bounce);

            v.postDelayed(() -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getButtonUrl()));
                context.startActivity(intent);
            }, 200); // Delay to match bounce duration


        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

