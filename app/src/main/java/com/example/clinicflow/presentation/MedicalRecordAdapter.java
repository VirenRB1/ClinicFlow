package com.example.clinicflow.presentation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicflow.R;
import com.example.clinicflow.models.MedicalRecord;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MedicalRecordAdapter extends RecyclerView.Adapter<MedicalRecordAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;

    List<MedicalRecord> records;

    public MedicalRecordAdapter(Context context, List<MedicalRecord> records, RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.records = records;
        this.recyclerViewInterface = recyclerViewInterface;
    }
    @NonNull
    @Override
    public MedicalRecordAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_medical_record, parent, false);

        return new MedicalRecordAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicalRecordAdapter.MyViewHolder holder, int position) {
        holder.doc.setText(records.get(position).getDoctorName());
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        holder.date.setText(formatter.format(records.get(position).getDate()));
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageview;

        TextView doc, date;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            imageview = itemView.findViewById(R.id.arrow);
            doc = itemView.findViewById(R.id.doctorName);
            date = itemView.findViewById(R.id.dateTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface != null){
                        int pos = getBindingAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onRecordClick(pos);
                        }
                    }
                }
            });
        }
    }
}
