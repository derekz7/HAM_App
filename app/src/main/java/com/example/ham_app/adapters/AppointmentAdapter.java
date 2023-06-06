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
import com.example.ham_app.modules.Booking;
import com.example.ham_app.modules.Patient;
import com.example.ham_app.untils.ApiDataManager;
import java.util.List;
import java.util.Objects;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private Context context;
    private List<Booking> bookingList;

    private AppointmentAdapter.onItemClickListener mListener;

    public interface onItemClickListener {
        void onItemClick(int pos, View view);
    }

    public void setOnItemClickListener(AppointmentAdapter.onItemClickListener mListener) {
        this.mListener = mListener;
    }

    public AppointmentAdapter(Context context, List<Booking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Booking> list) {
        this.bookingList = list;
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
        Booking booking = bookingList.get(position);
        if (booking == null) {
            return;
        }
        ColorStateList colorStateList;
        holder.tvNumber.setText("Phiếu khám số " + booking.getOrderNum());
        if (booking.getStatus().equals("Chờ khám") || booking.getStatus().equals("Pending")) {
            colorStateList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.warning));
            holder.tvStatus1.setText(booking.getStatus());
            holder.tvStatus1.setBackgroundTintList(colorStateList);
        } else if (booking.getStatus().equals("Đã khám")) {
            colorStateList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green));
            holder.tvStatus1.setText(booking.getStatus());
            holder.tvStatus1.setBackgroundTintList(colorStateList);
        } else {
            colorStateList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.grey));
            holder.tvStatus1.setText(booking.getStatus());
            holder.tvStatus1.setBackgroundTintList(colorStateList);
        }
        holder.tvDate1.setText(booking.getDate());
        holder.tv_bookingTime1.setText(booking.getTime());
        holder.tvMaPhieu.setText(booking.getId());
        holder.tv_ptName1.setText(getPatient(booking.getPt_id()) != null ? getPatient(booking.getPt_id()).getPatientName() : "Chưa cập nhật");


    }


    @Override
    public int getItemCount() {
        if (bookingList != null) {
            return bookingList.size();
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

    private Patient getPatient(String id) {
        for (Patient patient : ApiDataManager.instance.getPatientList()) {
            if (patient.getId().equals(id)) {
                return patient;
            }
        }
        return null;
    }
}
