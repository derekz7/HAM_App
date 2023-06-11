package com.example.ham_app.activities.doctors;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ham_app.R;
import com.example.ham_app.activities.ErrorActivity;
import com.example.ham_app.adapters.AppointmentAdapter;
import com.example.ham_app.api.ApiService;
import com.example.ham_app.dialog.LoadingDialog;
import com.example.ham_app.modules.Appointment;
import com.example.ham_app.modules.User;
import com.example.ham_app.untils.ApiDataManager;
import com.google.android.gms.common.api.Api;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuccessScheduleFragment extends Fragment {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private List<Appointment> appointments;
    private List<Appointment> appointmentsToday;
    private AppointmentAdapter adapter;
    private ImageButton igbSearch, igbFilterAppointment;
    private LinearLayout layoutSearchAndFilter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ConstraintLayout layout_notiSuccess;
    private TextView tvNotiSuccess;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_success_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setLayout();
        loadData();
        onClick();

    }

    private void onClick() {

        adapter.setOnItemClickListener(new AppointmentAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int pos, View view) {
                showDialogAppointment(adapter.getAppointments().get(pos));
            }
        });
        igbSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutSearchAndFilter.getVisibility() == View.GONE) {
                    layoutSearchAndFilter.setVisibility(View.VISIBLE);
                } else {
                    layoutSearchAndFilter.setVisibility(View.GONE);
                }
            }
        });

        igbFilterAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadingDialog.show(getContext());
                ApiService.api.getAppointmentsSuccessful(ApiDataManager.getInstance().getUser().getId()).enqueue(new Callback<List<Appointment>>() {
                    @Override
                    public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                        if (response.body() != null) {
                            appointments.clear();
                            appointments.addAll(response.body());
                            adapter.setData(appointments);
                            ApiDataManager.getInstance().setAppointmentsSuccess(response.body());
                        }
                        LoadingDialog.dismissDialog();
                    }

                    @Override
                    public void onFailure(Call<List<Appointment>> call, Throwable t) {
                        LoadingDialog.dismissDialog();
                        startActivity(new Intent(getContext(), ErrorActivity.class));
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
            tvNotiSuccess.setText("Không tìm thấy lịch khám");
            layout_notiSuccess.setVisibility(View.VISIBLE);
            adapter.setData(filteredList);
        } else {
            layout_notiSuccess.setVisibility(View.GONE);
            adapter.setData(filteredList);
        }
    }

    private void loadData() {
        if (ApiDataManager.getInstance().getAppointmentsSuccess() != null) {
            if (ApiDataManager.getInstance().getAppointmentsSuccess().size() > 0) {
                layout_notiSuccess.setVisibility(View.GONE);
                appointments.clear();
                appointments.addAll(ApiDataManager.getInstance().getAppointmentsSuccess());
                adapter.setData(appointments);
            } else {
                tvNotiSuccess.setText("Bạn chưa hoàn thành lịch khám nào");
                layout_notiSuccess.setVisibility(View.VISIBLE);
            }

        } else {
            LoadingDialog.show(getContext());
            ApiService.api.getAppointmentsSuccessful(ApiDataManager.getInstance().getUser().getId()).enqueue(new Callback<List<Appointment>>() {
                @Override
                public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                    if (response.body() != null) {
                        if (response.body().size() > 0) {
                            layout_notiSuccess.setVisibility(View.GONE);
                            appointments.clear();
                            appointments.addAll(response.body());
                            adapter.setData(appointments);
                            ApiDataManager.getInstance().setAppointmentsSuccess(response.body());
                        } else {
                            tvNotiSuccess.setText("Bạn chưa hoàn thành lịch khám nào");
                            layout_notiSuccess.setVisibility(View.VISIBLE);
                        }

                    } else {
                        tvNotiSuccess.setText("Bạn chưa hoàn thành lịch khám nào");
                        layout_notiSuccess.setVisibility(View.VISIBLE);
                    }

                    LoadingDialog.dismissDialog();
                }

                @Override
                public void onFailure(Call<List<Appointment>> call, Throwable t) {
                    LoadingDialog.dismissDialog();
                    startActivity(new Intent(getContext(), ErrorActivity.class));
                }
            });
        }
        ApiService.api.getAppointmentsSuccessfulToday(ApiDataManager.getInstance().getUser().getId()).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.body() != null) {
                    appointmentsToday.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {

            }
        });
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.rec_Success);
        searchView = view.findViewById(R.id.searchView);
        igbSearch = view.findViewById(R.id.igbSearch);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshSuccess);
        layoutSearchAndFilter = view.findViewById(R.id.layoutSearchAndFilter);
        igbFilterAppointment = view.findViewById(R.id.igbFilterAppointment);
        layout_notiSuccess = view.findViewById(R.id.layout_notiSuccess);
        tvNotiSuccess = view.findViewById(R.id.tvNotiSuccess);
    }

    private void setLayout() {
        appointments = new ArrayList<>();
        appointmentsToday = new ArrayList<>();
        adapter = new AppointmentAdapter(getContext(), appointments);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        layoutSearchAndFilter.setVisibility(View.GONE);
        layout_notiSuccess.setVisibility(View.GONE);
    }

    public void showDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog_filter_appointment);
        dialog.setCancelable(true);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().getAttributes().windowAnimations = R.style.BottomSheetAnimation;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        final TextView tvToday = dialog.findViewById(R.id.tvToday);
        final TextView tvAll = dialog.findViewById(R.id.tvAll);

        final ImageView imgCancel = dialog.findViewById(R.id.imgCancelFilter);


        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog.show(getContext());
                ApiService.api.getAppointmentsSuccessfulToday(ApiDataManager.getInstance().getUser().getId()).enqueue(new Callback<List<Appointment>>() {
                    @Override
                    public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                        if (response.body() != null) {
                            if (response.body().size() > 0) {
                                appointmentsToday.clear();
                                appointmentsToday.addAll(response.body());
                                adapter.setData(response.body());
                                layout_notiSuccess.setVisibility(View.GONE);
                            } else {
                                adapter.setData(response.body());
                                tvNotiSuccess.setText("Bạn chưa hoàn thành lịch khám nào ngày hôm nay");
                                layout_notiSuccess.setVisibility(View.VISIBLE);
                            }
                            LoadingDialog.dismissDialog();
                            dialog.dismiss();
                        } else {
                            tvNotiSuccess.setText("Bạn chưa hoàn thành lịch khám nào ngày hôm nay");
                            layout_notiSuccess.setVisibility(View.GONE);
                            dialog.dismiss();
                            LoadingDialog.dismissDialog();
                        }

                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<List<Appointment>> call, Throwable t) {
                        LoadingDialog.dismissDialog();
                        startActivity(new Intent(getContext(), ErrorActivity.class));
                    }
                });

            }
        });

        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
                dialog.dismiss();
            }
        });


        dialog.show();

    }


    public void showDialogAppointment(Appointment appointment) {
        Dialog dialog = new Dialog(getContext());
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

        btnCancelBooking.setVisibility(View.GONE);
        btnSuccess.setText("Tạo đơn thuốc");

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

        btnSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),CreatePrescriptionActivity.class);
                intent.putExtra("appointment",appointment);
                startActivity(intent);
                dialog.dismiss();
            }
        });


        dialog.show();

    }

}