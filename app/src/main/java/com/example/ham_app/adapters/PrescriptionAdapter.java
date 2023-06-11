package com.example.ham_app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ham_app.R;
import com.example.ham_app.modules.Appointment;
import com.example.ham_app.modules.Prescription;

import java.util.List;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.PrescriptionViewHolder> {

    private Context context;
    private List<Prescription> prescriptionList;

    private PrescriptionAdapter.onItemClickListener mListener;

    public interface onItemClickListener {
        void onItemClick(int pos, View view);
    }

    public void setOnItemClickListener(PrescriptionAdapter.onItemClickListener mListener) {
        this.mListener = mListener;
    }

    public PrescriptionAdapter(Context context, List<Prescription> prescriptionList) {
        this.context = context;
        this.prescriptionList = prescriptionList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Prescription> list) {
        this.prescriptionList = list;
        notifyDataSetChanged();
    }

    public List<Prescription> getPrescriptionList() {
        return prescriptionList;
    }

    @NonNull
    @Override
    public PrescriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prescription, parent, false);
        return new PrescriptionAdapter.PrescriptionViewHolder(view, mListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PrescriptionViewHolder holder, int position) {
        Prescription prescription = prescriptionList.get(position);
        if (prescription == null) {
            return;
        }
        holder.tv_Pre_PtName1.setText(prescription.getPtName());
        holder.tv_disease.setText(prescription.getDisease());
        holder.tvPreID.setText(prescription.getId());
    }


    @Override
    public int getItemCount() {
        if (prescriptionList != null) {
            return prescriptionList.size();
        }
        return 0;
    }


    public class PrescriptionViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_Pre_PtName1, tv_disease, tvPreID;

        public PrescriptionViewHolder(@NonNull View itemView, PrescriptionAdapter.onItemClickListener listener) {
            super(itemView);
            tv_Pre_PtName1 = itemView.findViewById(R.id.tv_Pre_PtName1);
            tv_disease = itemView.findViewById(R.id.tv_disease);
            tvPreID = itemView.findViewById(R.id.tvPreID);
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
