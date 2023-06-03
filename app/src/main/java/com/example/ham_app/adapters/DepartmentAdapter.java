package com.example.ham_app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ham_app.R;
import com.example.ham_app.modules.Department;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.DepartmentViewHolder> {

    private Context context;
    private List<Department> listDep;

    private DepartmentAdapter.onItemClickListener mListener;

    public interface onItemClickListener{
        void onItemClick(int pos, View view);
    }

    public void setOnItemClickListener(DepartmentAdapter.onItemClickListener mListener) {
        this.mListener = mListener;
    }

    public DepartmentAdapter(Context context, List<Department> listDep) {
        this.context = context;
        this.listDep = listDep;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Department> list){
        this.listDep = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public DepartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_department,parent,false);
        return new DepartmentViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentViewHolder holder, int position) {
        Department department = listDep.get(position);
        if (department == null) {
            return;
        }
        Picasso.get().load(department.getImgUrl()).into(holder.img_dep);
        holder.tv_depName.setText(department.getName());
    }

    @Override
    public int getItemCount() {
        if (listDep != null){
            return listDep.size();
        }
        return 0;
    }

    public class DepartmentViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_dep;
        private TextView tv_depName;

        public DepartmentViewHolder(@NonNull View itemView, DepartmentAdapter.onItemClickListener listener) {
            super(itemView);
            img_dep = itemView.findViewById(R.id.img_department);
            tv_depName = itemView.findViewById(R.id.tv_depName);
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
