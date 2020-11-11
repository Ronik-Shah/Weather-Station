package com.example.weatherstation.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weatherstation.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private ArrayList<WeatherData> weatherDataArrayList;
    static private long ANIM_DURATION = 1500;

    public RecyclerViewAdapter(ArrayList<WeatherData> weatherDataArrayList){
        this.weatherDataArrayList = weatherDataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item = inflater.inflate(R.layout.recycler_view_layout,parent,false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherData weatherData = weatherDataArrayList.get(position);
        holder.image.setImageResource(weatherData.imageResource);
        holder.image.animate().alpha(1).setDuration(ANIM_DURATION);
        holder.description.setText(weatherData.data);
        holder.description.animate().alpha(1).setDuration(ANIM_DURATION);
        holder.ans.setText(weatherData.result);
        holder.ans.animate().alpha(1).setDuration(ANIM_DURATION);
    }

    @Override
    public int getItemCount() {
        return weatherDataArrayList.size();
    }
}
