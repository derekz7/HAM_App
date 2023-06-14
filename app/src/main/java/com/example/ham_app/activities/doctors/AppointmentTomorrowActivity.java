package com.example.ham_app.activities.doctors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ham_app.R;
import com.example.ham_app.activities.ErrorActivity;
import com.example.ham_app.adapters.AppointmentAdapter;
import com.example.ham_app.api.ApiService;
import com.example.ham_app.dialog.LoadingDialog;
import com.example.ham_app.modules.Appointment;
import com.example.ham_app.untils.ApiDataManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentTomorrowActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private List<Appointment> appointments;
    private AppointmentAdapter adapter;
    private ImageButton igbSearch ,igb_backAppNext;
    private LinearLayout layoutSearchAndFilterNext;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ConstraintLayout layout_notiNext;
    private TextView tvNotiNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_tomorrow);
        initView();
        setLayout();
        loadData();
        onClick();
    }
    private void onClick() {
        igb_backAppNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter.setOnItemClickListener(new AppointmentAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int pos, View view) {
                showDialog(adapter.getAppointments().get(pos));
            }
        });
        igbSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutSearchAndFilterNext.getVisibility() == View.GONE) {
                    layoutSearchAndFilterNext.setVisibility(View.VISIBLE);
                } else {
                    layoutSearchAndFilterNext.setVisibility(View.GONE);
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadingDialog.show(AppointmentTomorrowActivity.this);
                ApiService.api.getAppointmentsTomorrow(ApiDataManager.getInstance().getUser().getId()).enqueue(new Callback<List<Appointment>>() {
                    @Override
                    public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                        if (response.body() != null) {
                            appointments.clear();
                            appointments.addAll(response.body());
                            adapter.setData(appointments);
                            ApiDataManager.getInstance().setAppointmentsTomorrow(response.body());
                        }
                        LoadingDialog.dismissDialog();
                    }

                    @Override
                    public void onFailure(Call<List<Appointment>> call, Throwable t) {
                        LoadingDialog.dismissDialog();
                        startActivity(new Intent(AppointmentTomorrowActivity.this, ErrorActivity.class));
                    }
                });
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
    }

    private void filterList(String searchString) {
        List<Appointment> filteredList = new ArrayList<>();
        for (Appointment app : appointments) {
            if (app.getPtName().toLowerCase().contains(searchString.toLowerCase())) {
                filteredList.add(app);
            }
        }
        if (filteredList.isEmpty()) {
            tvNotiNext.setText("Không tìm thấy lịch khám");
            layout_notiNext.setVisibility(View.VISIBLE);
            adapter.setData(filteredList);
        } else {
            layout_notiNext.setVisibility(View.GONE);
            adapter.setData(filteredList);
        }
    }

    private void loadData() {
        if (ApiDataManager.getInstance().getAppointmentsTomorrow() != null) {
            if (ApiDataManager.getInstance().getAppointmentsTomorrow().size() > 0) {
                layout_notiNext.setVisibility(View.GONE);
                appointments.clear();
                appointments.addAll(ApiDataManager.getInstance().getAppointmentsTomorrow());
                adapter.setData(appointments);
            } else {
                tvNotiNext.setText("Bạn không có lịch khám nào ngày mai");
                layout_notiNext.setVisibility(View.VISIBLE);
            }

        } else {
            LoadingDialog.show(this);
            ApiService.api.getAppointmentsTomorrow(ApiDataManager.getInstance().getUser().getId()).enqueue(new Callback<List<Appointment>>() {
                @Override
                public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                    if (response.body() != null) {
                        if (response.body().size() > 0) {
                            layout_notiNext.setVisibility(View.GONE);
                            appointments.clear();
                            appointments.addAll(response.body());
                            adapter.setData(appointments);
                            ApiDataManager.getInstance().setAppointmentsTomorrow(response.body());
                        } else {
                            tvNotiNext.setText("Bạn không có lịch khám nào ngày mai");
                            layout_notiNext.setVisibility(View.VISIBLE);
                        }

                    } else {
                        tvNotiNext.setText("Bạn không có lịch khám nào ngày mai");
                        layout_notiNext.setVisibility(View.VISIBLE);
                    }

                    LoadingDialog.dismissDialog();
                }

                @Override
                public void onFailure(Call<List<Appointment>> call, Throwable t) {
                    LoadingDialog.dismissDialog();
                    startActivity(new Intent(AppointmentTomorrowActivity.this, ErrorActivity.class));
                }
            });
        }
    }
    private void initView() {
        recyclerView = findViewById(R.id.rec_Next);
        searchView = findViewById(R.id.searchViewNext);
        igbSearch = findViewById(R.id.igbSearchNext);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshNext);
        layoutSearchAndFilterNext = findViewById(R.id.layoutSearchAndFilterNext);
        layout_notiNext = findViewById(R.id.layout_notiNext);
        tvNotiNext = findViewById(R.id.tvNotiNext);
        igb_backAppNext = findViewById(R.id.igb_backAppNext);
    }

    private void setLayout() {
        appointments = new ArrayList<>();
        adapter = new AppointmentAdapter(AppointmentTomorrowActivity.this, appointments);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AppointmentTomorrowActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        layoutSearchAndFilterNext.setVisibility(View.GONE);
        layout_notiNext.setVisibility(View.GONE);
    }


    public void showDialog(Appointment appointment) {
        Dialog dialog = new Dialog(AppointmentTomorrowActivity.this);
        dialog.setContentView(R.layout.custom_dialog_appointment);
        dialog.setCancelable(true);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().getAttributes().windowAnimations = R.style.BottomSheetAnimation;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        final TextView tvStatus2 = dialog.findViewById(R.id.tvStatus2);
        final TextView tvPTname = dialog.findViewById(R.id.tvPTname);
        final TextView tvTime = dialog.findViewById(R.id.tvTime1);
        final TextView tv_BookingID = dialog.findViewById(R.id.tv_BookingID);

        final Button btnCancelBooking = dialog.findViewById(R.id.btnCancelBooking);
        final Button btnSuccess = dialog.findViewById(R.id.btnSuccess);
        final ImageView imgCancelAppointment = dialog.findViewById(R.id.imgCancelAppointment);

        tvStatus2.setText(appointment.getStatus());
        tvPTname.setText(appointment.getPtName());
        tvTime.setText(appointment.getTime());
        tv_BookingID.setText(appointment.getBid());

        imgCancelAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnCancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogReason(appointment);
                dialog.dismiss();
            }
        });
        btnSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiService.api.changeStatus(appointment.getBid(), "Đã khám",null).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        ApiDataManager.getInstance().setAppointmentListToday(null);
                        ApiDataManager.getInstance().setAppointmentsSuccess(null);
                        appointments.clear();
                        adapter.setData(appointments);
                        loadData();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(AppointmentTomorrowActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });


        dialog.show();

    }

    public void showDialogReason(Appointment appointment) {
        Dialog dialog = new Dialog(AppointmentTomorrowActivity.this);
        dialog.setContentView(R.layout.custom_dialog_reason);
        dialog.setCancelable(true);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().getAttributes().windowAnimations = R.style.BottomSheetAnimation;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        final RadioButton rd_reason1 = dialog.findViewById(R.id.rd_reason1);
        final RadioButton rd_reason2 = dialog.findViewById(R.id.rd_reason2);
        final RadioButton rd_other = dialog.findViewById(R.id.rd_other);
        final EditText edt_Reason = dialog.findViewById(R.id.edt_Reason);

        final ImageView imgCancel = dialog.findViewById(R.id.imgCancelReason);
        final Button btnCancelAppointment = dialog.findViewById(R.id.btnCancelAppointment);


        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        rd_reason1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    appointment.setReason(rd_reason1.getText().toString());
                }
            }
        });

        rd_reason2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    appointment.setReason(rd_reason2.getText().toString());
                }
            }
        });
        rd_other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    edt_Reason.setVisibility(View.VISIBLE);
                }else {
                    edt_Reason.setVisibility(View.GONE);
                }
            }
        });
        btnCancelAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appointment.getReason() == null){
                    Toast.makeText(AppointmentTomorrowActivity.this, "Vui lòng chọn lý do hủy lịch khám", Toast.LENGTH_SHORT).show();
                }else{
                    ApiService.api.changeStatus(appointment.getBid(), "Canceled",appointment.getReason()).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Toast.makeText(AppointmentTomorrowActivity.this, "Đã hủy lịch khám", Toast.LENGTH_SHORT).show();
                            ApiDataManager.getInstance().setAppointmentListToday(null);
                            appointments.clear();
                            adapter.setData(appointments);
                            loadData();
                            dialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(AppointmentTomorrowActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

        dialog.show();

    }
}