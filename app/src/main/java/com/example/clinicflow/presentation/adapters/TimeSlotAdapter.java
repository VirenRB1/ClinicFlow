package com.example.clinicflow.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicflow.R;
import com.example.clinicflow.models.TimeSlot;
import com.example.clinicflow.presentation.RecyclerViewInterface;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.MyViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    private final Context context;
    private final List<TimeSlot> slots;

    public TimeSlotAdapter(Context context, List<TimeSlot> slots, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.slots = slots;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public TimeSlotAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.time_slot, parent, false);

        return new TimeSlotAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotAdapter.MyViewHolder holder, int position) {
        TimeSlot slot = slots.get(position);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        String formattedStartTime = slot.getStartTime().format(timeFormatter);
        String formattedEndTime = slot.getEndTime().format(timeFormatter);

        holder.timeRange.setText(
                context.getString(
                        R.string.time_slot_range,
                        formattedStartTime,
                        formattedEndTime
                )
        );
    }

    @Override
    public int getItemCount() {
        return slots == null ? 0 : slots.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageview;
        TextView timeRange;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            imageview = itemView.findViewById(R.id.arrow);
            timeRange = itemView.findViewById(R.id.timeRange);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null) {
                        int pos = getBindingAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}