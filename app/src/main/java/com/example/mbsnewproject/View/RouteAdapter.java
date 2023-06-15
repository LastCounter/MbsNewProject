package com.example.mbsnewproject.View;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mbsnewproject.R;
import com.example.mbsnewproject.Tracking.RouteData;

import java.util.ArrayList;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {

    private ArrayList<RouteData> routeDataList;
    private OnItemClickListener listener;

    private OnDeleteClickListener deleteListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public RouteAdapter(ArrayList<RouteData> routeDataList) {
        this.routeDataList = routeDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RouteData routeData = routeDataList.get(position);
        holder.titleTextView.setText(routeData.getRouteTitle());
        holder.dateTimeTextView.setText(routeData.getDateTime());
        holder.timeNeededTextView.setText(routeData.getTimeNeeded());
    }

    @Override
    public int getItemCount() {
        return routeDataList.size();
    }

    public RouteData getItem(int position) {
        return routeDataList.get(position);
    }

    public void removeItem(int position){
        routeDataList.remove(position);
        notifyItemRemoved(position);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener deleteListener) {
        this.deleteListener = deleteListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView titleTextView;
        public TextView dateTimeTextView;
        public TextView timeNeededTextView;
        public ImageButton removeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            dateTimeTextView = itemView.findViewById(R.id.dateTimeTextView);
            timeNeededTextView = itemView.findViewById(R.id.timeNeededTextView);
            removeButton = itemView.findViewById(R.id.removeButton);
            removeButton.setImageResource(android.R.drawable.ic_delete);
            itemView.setOnClickListener(this);
            removeButton.setOnClickListener(v -> deleteOnClick());
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position);
                }
            }
        }

        public void deleteOnClick() {
            if (deleteListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    deleteListener.onDeleteClick(position);
                }
            }

        }
    }
}
