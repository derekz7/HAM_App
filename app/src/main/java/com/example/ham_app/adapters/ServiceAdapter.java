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
import com.example.ham_app.dialog.BottomDialogMessage;
import com.example.ham_app.modules.Department;
import com.example.ham_app.modules.Service;

import java.text.DecimalFormat;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private Context context;
    private List<Service> serviceList;
    private BottomDialogMessage bottomDialogMessage;
    private onItemClickListener listener;

    public interface onItemClickListener {
        void onItemClick(int pos, View view);
    }

    public void setOnClickListener(ServiceAdapter.onItemClickListener listener) {
        this.listener = listener;
    }

    public ServiceAdapter(Context context, List<Service> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
        this.bottomDialogMessage = new BottomDialogMessage(context);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Service> list) {
        this.serviceList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view, listener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = serviceList.get(position);
        if (service == null) {
            return;
        }
        if (service.price == 0) {
            holder.tvPrice.setText("");
            holder.tvPrice.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_help_24, 0);
            holder.tvPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomDialogMessage.show("Thanh toán tại bệnh viện");
                }
            });
        } else {
            DecimalFormat formatter = new DecimalFormat("###,###,###");
            holder.tvPrice.setText(formatter.format(service.price) + "đ");
            holder.tvPrice.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        holder.tvServiceName.setText(service.getServiceName());
    }

    @Override
    public int getItemCount() {
        if (serviceList == null) {
            return 0;
        }
        return serviceList.size();
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder {

        private TextView tvServiceName, tvPrice;

        public ServiceViewHolder(@NonNull View itemView, ServiceAdapter.onItemClickListener listener) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tv_serviceName);
            tvPrice = itemView.findViewById(R.id.tv_ServicePrice);
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
