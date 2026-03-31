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
import com.example.clinicflow.models.Appointment;
import com.example.clinicflow.presentation.RecyclerViewInterface;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private final Context context;
    private final List<Appointment> records;
    private final List<String> names;

    public AppointmentAdapter(Context context, List<Appointment> records, List<String> names,
                              RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.records = records;
        this.names = names;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public AppointmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_medical_record, parent, false);

        return new AppointmentAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentAdapter.MyViewHolder holder, int position) {
        Appointment appointment = records.get(position);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        String formattedDate = appointment.getAppointmentDate().format(dateFormatter);
        String formattedStartTime = appointment.getStartTime().format(timeFormatter);
        String formattedEndTime = appointment.getEndTime().format(timeFormatter);

        holder.name.setText(names.get(position));
        holder.date.setText(
                context.getString(
                        R.string.appointment_date_time,
                        formattedDate,
                        formattedStartTime,
                        formattedEndTime));
    }

    @Override
    public int getItemCount() {
        return records == null ? 0 : records.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview;
        TextView name, date;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            imageview = itemView.findViewById(R.id.arrow);
            name = itemView.findViewById(R.id.doctorName);
            date = itemView.findViewById(R.id.dateTime);

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