package com.emmo.rideshare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterView extends RecyclerView.Adapter<AdapterView.MyViewHolder> {

    private ArrayList<Ride> mData;
    private OnItemClickListener listener;

    public AdapterView(ArrayList<Ride> mData, OnItemClickListener listener) {
        this.mData = mData;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Hier können Sie die Views in Ihrem RecyclerView-Element deklarieren

        public ViewHolder(View itemView) {
            super(itemView);
            // Initialisieren Sie die Views hier
        }
    }

    public AdapterView(ArrayList<Ride> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Ride ride = mData.get(position);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(ride);
            }
        });
        // Hier setzen Sie die Daten für jedes TextView entsprechend Ihrer Ride-Klasse
        holder.recTo.setText(ride.getStartCity());
        holder.recFrom.setText(ride.getEndCity());
        holder.recTime.setText(ride.getTime());
        holder.recDate.setText(ride.getDate());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView recTo, recFrom, recTime, recDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recTo = itemView.findViewById(R.id.rec_to);
            recFrom = itemView.findViewById(R.id.rec_from);
            recTime = itemView.findViewById(R.id.rec_time);
            recDate = itemView.findViewById(R.id.rec_date);
        }
    }
}

