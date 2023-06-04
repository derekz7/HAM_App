package com.example.ham_app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ham_app.R;
import com.example.ham_app.modules.Doctor;
import com.example.ham_app.modules.Service;

import java.util.List;


public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private Context context;
    private List<Doctor> doctorList;
    private DoctorAdapter.onItemClickListener listener;


    public interface onItemClickListener {
        void onItemClick(int pos, View view);
    }

    public void setOnClickListener(DoctorAdapter.onItemClickListener listener) {
        this.listener = listener;
    }

    public DoctorAdapter(Context context, List<Doctor> doctorList) {
        this.context = context;
        this.doctorList = doctorList;
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Doctor> list) {
        this.doctorList = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view, listener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctorList.get(position);
        if (doctor == null) {
            return;
        }
        holder.tv_DoctorName.setText(doctor.getName());
    }

    @Override
    public int getItemCount() {
        if (doctorList == null) {
            return 0;
        }
        return doctorList.size();
    }

    public class DoctorViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_DoctorName;
        public DoctorViewHolder(@NonNull View itemView, DoctorAdapter.onItemClickListener listener) {
            super(itemView);
            tv_DoctorName = itemView.findViewById(R.id.tv_doctorName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position, v);
                        }
                    }
                }
            });
        }
    }
}
