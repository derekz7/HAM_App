package com.example.ham_app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ham_app.R;
import com.example.ham_app.modules.Appointment;
import com.example.ham_app.modules.Booking;
import com.example.ham_app.modules.Patient;
import com.example.ham_app.untils.ApiDataManager;
import java.util.List;
import java.util.Objects;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private Context context;
    private List<Appointment> appointments;

    private AppointmentAdapter.onItemClickListener mListener;

    public interface onItemClickListener {
        void onItemClick(int pos, View view);
    }

    public void setOnItemClickListener(AppointmentAdapter.onItemClickListener mListener) {
        this.mListener = mListener;
    }

    public AppointmentAdapter(Context context, List<Appointment> appointments) {
        this.context = context;
        this.appointments = appointments;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Appointment> list) {
        this.appointments = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new AppointmentAdapter.AppointmentViewHolder(view, mListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        if (appointment == null) {
            return;
        }
        ColorStateList colorStateList;
        holder.tvNumber.setText("Phiếu khám số " + appointment.getOrderNum());
        if (appointment.getStatus().equals("Chờ khám") || appointment.getStatus().equals("Pending")) {
            colorStateList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.warning));
            holder.tvStatus1.setText(appointment.getStatus());
            holder.tvStatus1.setBackgroundTintList(colorStateList);
        } else if (appointment.getStatus().equals("Đã khám")) {
            colorStateList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green));
            holder.tvStatus1.setText(appointment.getStatus());
            holder.tvStatus1.setBackgroundTintList(colorStateList);
        } else {
            colorStateList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.grey));
            holder.tvStatus1.setText(appointment.getStatus());
            holder.tvStatus1.setBackgroundTintList(colorStateList);
        }
        holder.tvDate1.setText(appointment.getDate());
        holder.tv_bookingTime1.setText(appointment.getTime());
        holder.tvMaPhieu.setText(appointment.getBid());
        holder.tv_ptName1.setText(appointment.getPtName());


    }


    @Override
    public int getItemCount() {
        if (appointments != null) {
            return appointments.size();
        }
        return 0;
    }


    public class AppointmentViewHolder extends RecyclerView.ViewHolder {

        private TextView tvStatus1, tv_ptName1, tv_bookingTime1, tvDate1, tvMaPhieu, tvNumber;

        public AppointmentViewHolder(@NonNull View itemView, AppointmentAdapter.onItemClickListener listener) {
            super(itemView);
            tvStatus1 = itemView.findViewById(R.id.tvStatus1);
            tv_ptName1 = itemView.findViewById(R.id.tv_ptName1);
            tv_bookingTime1 = itemView.findViewById(R.id.tv_bookingTime1);
            tvDate1 = itemView.findViewById(R.id.tvDate1);
            tvMaPhieu = itemView.findViewById(R.id.tvMaPhieu);
            tvNumber = itemView.findViewById(R.id.tvNumber);
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
