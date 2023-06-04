package com.example.ham_app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ham_app.R;

import com.example.ham_app.modules.Patient;
import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {
    private Context context;
    private List<Patient> patients;

    private PatientAdapter.onItemClickListener mListener;

    public interface onItemClickListener{
        void onItemClick(int pos, View view);
    }

    public void setOnItemClickListener(PatientAdapter.onItemClickListener mListener) {
        this.mListener = mListener;
    }

    public PatientAdapter(Context context, List<Patient> patients) {
        this.context = context;
        this.patients = patients;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Patient> list){
        this.patients = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient,parent,false);
        return new PatientAdapter.PatientViewHolder(view,mListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PatientAdapter.PatientViewHolder holder, int position) {
        Patient patient = patients.get(position);
        if (patient == null) {
            return;
        }
        holder.tvPatientName.setText(patient.getPatientName());
        holder.tvPatientDOB.setText(patient.getDob());
        holder.tvPatientAddress.setText(patient.getAddress());
        holder.tvChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Choose", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        if (patients != null){
            return patients.size();
        }
        return 0;
    }

    public class PatientViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPatientName, tvPatientDOB, tvPatientAddress, tvChoose;

        public PatientViewHolder(@NonNull View itemView, PatientAdapter.onItemClickListener listener) {
            super(itemView);
            tvPatientName = itemView.findViewById(R.id.tvPatientName);
            tvPatientDOB = itemView.findViewById(R.id.tvPatientDOB);
            tvPatientAddress = itemView.findViewById(R.id.tvPatientAddress);
            tvChoose = itemView.findViewById(R.id.btn_choosePatient);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position,v);
                        }
                    }
                }
            });
        }
    }

}
