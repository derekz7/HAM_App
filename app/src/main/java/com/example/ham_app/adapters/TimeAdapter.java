package com.example.ham_app.adapters;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.example.ham_app.R;
import com.example.ham_app.untils.ApiDataManager;

import java.util.ArrayList;
import java.util.HashMap;

public class TimeAdapter extends SectionedRecyclerViewAdapter<TimeAdapter.TimeViewHolder> {
    private Activity activity;
    private ArrayList<String> sectionList;
    private HashMap<String, ArrayList<String>> itemList;
    private int selectedSection;
    private int selectedItem;

    @SuppressLint("NotifyDataSetChanged")
    public TimeAdapter(Activity activity, ArrayList<String> sectionList, HashMap<String, ArrayList<String>> itemList, int selectedSection, int selectedItem) {
        this.activity = activity;
        this.sectionList = sectionList;
        this.itemList = itemList;
        this.selectedSection = selectedSection;
        this.selectedItem = selectedItem;
        notifyDataSetChanged();
    }

    @Override
    public int getSectionCount() {
        return (sectionList != null ? sectionList.size() : 0);
    }

    @Override
    public int getItemCount(int section) {
        return (itemList != null ? itemList.get(sectionList.get(section)).size() : 0);
    }


    @Override
    public int getItemViewType(int section, int relativePosition, int absolutePosition) {
        if (section == 1) {
            return 0;
        }
        return super.getItemViewType(section, relativePosition, absolutePosition);
    }

    @Override
    public void onBindHeaderViewHolder(TimeViewHolder timeViewHolder, int i) {
        timeViewHolder.tvTime.setText(sectionList.get(i));
    }


    @Override
    public void onBindViewHolder(TimeViewHolder timeViewHolder, int i, int i1, int i2) {
        String sItem = itemList.get(sectionList.get(i)).get(i1);
        timeViewHolder.tvTime.setText(sItem);
        timeViewHolder.tvTime.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                Log.d("Booking","Time: " + sItem);
                selectedSection = i;
                selectedItem = i1;
                ApiDataManager.getInstance().setSelectedTime(i,i1);
                ApiDataManager.getInstance().getBooking().setTime(sItem);
                notifyDataSetChanged();
                Intent resultIntent = new Intent();
                activity.setResult(RESULT_OK, resultIntent);
                activity.finish();
            }
        });
        if (selectedSection == i && selectedItem == i1){
            timeViewHolder.tvTime.setBackground(ContextCompat.getDrawable(activity,R.drawable.round_fill));
            timeViewHolder.tvTime.setTextColor(Color.WHITE);

        }else {
            timeViewHolder.tvTime.setBackground(ContextCompat.getDrawable(activity,R.drawable.round_outline_gray_btn));
            timeViewHolder.tvTime.setTextColor(Color.BLACK);
        }
    }

    @NonNull
    @Override
    public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout;
        if (viewType == VIEW_TYPE_HEADER) {
            layout = R.layout.item_header;
        } else {
            layout = R.layout.item_slot;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new TimeViewHolder(view);
    }

    public class TimeViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTime;

        public TimeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
