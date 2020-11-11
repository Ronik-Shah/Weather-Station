package com.example.weatherstation.models;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherstation.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView description;
    public TextView ans;
    public ImageView image;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        description = itemView.findViewById(R.id.recycler_view_description);
        ans = itemView.findViewById(R.id.recycler_view_ans);
        image = itemView.findViewById(R.id.recycler_view_image);
    }
}
