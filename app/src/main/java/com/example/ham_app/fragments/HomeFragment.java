package com.example.ham_app.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ham_app.R;
import com.example.ham_app.activities.BookingActivity;
import com.example.ham_app.activities.ErrorActivity;
import com.example.ham_app.activities.LoginActivity;
import com.example.ham_app.activities.UserActivity;
import com.example.ham_app.dialog.LoadingDialog;
import com.example.ham_app.adapters.DepartmentAdapter;
import com.example.ham_app.api.ApiService;
import com.example.ham_app.modules.Booking;
import com.example.ham_app.modules.Department;
import com.example.ham_app.modules.News;
import com.example.ham_app.modules.Patient;
import com.example.ham_app.modules.User;
import com.example.ham_app.untils.ApiDataManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    private ImageSlider imageSlider;
    private List<SlideModel> slideModels;
    private List<News> newsList;
    private RecyclerView recycleViewDepartment;
    private DepartmentAdapter depAdapter;
    private CircleImageView userImg;
    private TextView tv_Username;
    private User user;
    private SharedPreferences sharedPreferences;
    private Button btn_seeMoreDepartments;
    private ImageButton igbDatKham, igbDonThuoc, igbTinTuc;
    private List<Department> departmentList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setLayout();
        getDepartment();
        loadUser();
        getNews();
        onClick();

    }

    private void onClick() {
        igbDatKham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), BookingActivity.class));
                requireActivity().finish();
            }
        });
        btn_seeMoreDepartments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialogDepartment();
            }
        });
        depAdapter.setOnItemClickListener(new DepartmentAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int pos, View view) {
                Toast.makeText(getContext(), "Clicked " +departmentList.get(pos).getName(), Toast.LENGTH_SHORT).show();
            }
        });
        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), UserActivity.class));
            }
        });
    }
    private void getDepartment() {
        if (ApiDataManager.getInstance().getDepartmentList() != null){
            departmentList.addAll(ApiDataManager.getInstance().getDepartmentList());
            depAdapter.setData(departmentList);
        }else {
            ApiService.api.getDepartments().enqueue(new Callback<List<Department>>() {
                @Override
                public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                    if (response.body() != null) {
                        departmentList.addAll(response.body());
                        depAdapter.setData(response.body());
                        ApiDataManager.getInstance().setDepartmentList(departmentList);
                        Toast.makeText(getContext(), "Loading department success", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<List<Department>> call, Throwable t) {
                    Toast.makeText(getContext(), "Loading department fail", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    private void loadUser() {
        if (ApiDataManager.getInstance().getUser() != null) {
            tv_Username.setText(ApiDataManager.getInstance().getUser().getFullName());
            Picasso.get().load(ApiDataManager.getInstance().getUser().getImgUrl()).into(userImg);
        } else {
            ApiService.api.getUserByUsername(user.getUsername()).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.body() != null) {
                        tv_Username.setText(response.body().getFullName());
                        Picasso.get().load(response.body().getImgUrl()).into(userImg);
                        ApiDataManager.getInstance().setUser(response.body());
                        user = response.body();
                        Toast.makeText(getContext(), "userid" + user.getId(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }
    }



    private void getNews() {
        if (ApiDataManager.getInstance().getNewsList() != null) {
            newsList = ApiDataManager.getInstance().getNewsList();
            for (News news : newsList) {
                SlideModel slideModel = new SlideModel(news.getImgUrl(), ScaleTypes.CENTER_CROP);
                slideModel.setTitle(news.getTitle());
                slideModels.add(slideModel);
            }
            imageSlider.setImageList(slideModels);
        } else {
            LoadingDialog.show(getContext());
            ApiService.api.getNewestNews().enqueue(new Callback<List<News>>() {
                @Override
                public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                    if (response.body() != null) {
                        ApiDataManager.getInstance().setNewsList(response.body());
                        for (News news : response.body()) {
                            SlideModel slideModel = new SlideModel(news.getImgUrl(), ScaleTypes.CENTER_CROP);
                            slideModel.setTitle(news.getTitle());
                            slideModels.add(slideModel);
                        }
                        imageSlider.setImageList(slideModels);
                        LoadingDialog.dismissDialog();

                    } else {
                        Toast.makeText(getContext(), "NEWS null", Toast.LENGTH_SHORT).show();
                        LoadingDialog.dismissDialog();
                    }
                }

                @Override
                public void onFailure(Call<List<News>> call, Throwable t) {
                    Toast.makeText(getContext(), "Load news error", Toast.LENGTH_SHORT).show();
                    LoadingDialog.dismissDialog();
                }
            });
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setLayout() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        recycleViewDepartment.setLayoutManager(layoutManager);
        recycleViewDepartment.setNestedScrollingEnabled(false);
        recycleViewDepartment.setAdapter(depAdapter);
    }

    private void init(View view) {
        imageSlider = view.findViewById(R.id.image_slider);
        igbDatKham = view.findViewById(R.id.btn_datKham);
        igbDonThuoc = view.findViewById(R.id.btn_donThuoc);
        igbTinTuc = view.findViewById(R.id.btn_news);
        slideModels = new ArrayList<>();
        newsList = new ArrayList<>();
        departmentList = new ArrayList<>();
        user = new User();
        recycleViewDepartment = view.findViewById(R.id.recyleViewDepartment);
        depAdapter = new DepartmentAdapter(getContext(), ApiDataManager.getInstance().getDepartmentList());
        tv_Username = view.findViewById(R.id.tv_userName);
        userImg = view.findViewById(R.id.profile_image);
        btn_seeMoreDepartments = view.findViewById(R.id.btn_seeMoreDepartment);
        sharedPreferences = this.getActivity().getSharedPreferences("dataLogin", MODE_PRIVATE);
        user.setUsername(sharedPreferences.getString("username", ""));
    }

    private void bottomDialogDepartment() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.bottom_sheet_layout);
        dialog.setCancelable(true);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().getAttributes().windowAnimations = R.style.BottomSheetAnimation;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        final ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        final RecyclerView recyclerViewDepartments = dialog.findViewById(R.id.rec_Department);
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        recyclerViewDepartments.setLayoutManager(layoutManager);
        recyclerViewDepartments.setNestedScrollingEnabled(false);
        recyclerViewDepartments.setAdapter(depAdapter);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}