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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ham_app.R;
import com.example.ham_app.activities.ErrorActivity;
import com.example.ham_app.dialog.LoadingDialog;
import com.example.ham_app.adapters.DepartmentAdapter;
import com.example.ham_app.api.ApiService;
import com.example.ham_app.models.Department;
import com.example.ham_app.models.News;
import com.example.ham_app.models.User;
import com.example.ham_app.untils.ApiDataManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    private ImageSlider imageSlider;
    private List<SlideModel> slideModels;
    private List<News> newsList;
    private List<Department> depList;
    private RecyclerView recycleViewDepartment;
    private DepartmentAdapter depAdapter;
    private CircleImageView userImg;
    private TextView tv_Username;
    private User user;
    private SharedPreferences sharedPreferences;
    private Button btn_seeMoreDepartments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setLayout();
        if (ApiDataManager.getInstance().getDepartmentList() == null || ApiDataManager.getInstance().getNewsList() == null) {
            getData();
        } else {
            loadData();
        }
        onClick();

    }

    private void onClick() {
        btn_seeMoreDepartments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialogDepartment();
            }
        });
    }


    private void loadData() {
        LoadingDialog.show(getContext());
        Toast.makeText(getContext(), "Load Data", Toast.LENGTH_SHORT).show();
        depList = ApiDataManager.getInstance().getDepartmentList();
        depAdapter.setData(depList);
        tv_Username.setText(ApiDataManager.getInstance().getUser().getFullName());
        Picasso.get().load(ApiDataManager.getInstance().getUser().getImgUrl()).into(userImg);
        newsList = ApiDataManager.getInstance().getNewsList();
        for (News news : newsList) {
            SlideModel slideModel = new SlideModel(news.getImgUrl(), ScaleTypes.CENTER_CROP);
            slideModel.setTitle(news.getTitle());
            slideModels.add(slideModel);
        }
        imageSlider.setImageList(slideModels);
        LoadingDialog.dismissDialog();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getData() {
        Toast.makeText(getContext(), "Get Data", Toast.LENGTH_SHORT).show();
        LoadingDialog.show(getContext());
        ApiService.api.getAllDepartment().enqueue(new Callback<List<Department>>() {
            @Override
            public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ApiDataManager.getInstance().setDepartmentList(response.body());
                        depList.addAll(response.body());
                        LoadingDialog.dismissDialog();
                        depAdapter.notifyDataSetChanged();
                    }
                } else {
                    LoadingDialog.dismissDialog();
                    startActivity(new Intent(getContext(), ErrorActivity.class));
                }

            }

            @Override
            public void onFailure(Call<List<Department>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                LoadingDialog.dismissDialog();
            }
        });

        ApiService.api.getNewestNews().enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                if (response.body() != null) {
                    ApiDataManager.getInstance().setNewsList(response.body());
                    newsList.addAll(response.body());
                    for (News news : response.body()) {
                        SlideModel slideModel = new SlideModel(news.getImgUrl(), ScaleTypes.CENTER_CROP);
                        slideModel.setTitle(news.getTitle());
                        slideModels.add(slideModel);
                    }
                    Toast.makeText(getContext(), "NEWS "+newsList.size(), Toast.LENGTH_SHORT).show();
                    imageSlider.setImageList(slideModels);

                }else {
                    Toast.makeText(getContext(), "NEWS null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                startActivity(new Intent(getContext(), ErrorActivity.class));
            }
        });
        ApiService.api.getUserByUsername(user.getUsername()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() != null) {
                    tv_Username.setText(response.body().getFullName());
                    Picasso.get().load(response.body().getImgUrl()).into(userImg);
                    ApiDataManager.getInstance().setUser(response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    private void setLayout() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        recycleViewDepartment.setLayoutManager(layoutManager);
        recycleViewDepartment.setNestedScrollingEnabled(false);
        recycleViewDepartment.setAdapter(depAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void init(View view) {
        imageSlider = view.findViewById(R.id.image_slider);
        slideModels = new ArrayList<>();
        depList = new ArrayList<>();
        newsList = new ArrayList<>();
        user = new User();
        recycleViewDepartment = view.findViewById(R.id.recyleViewDepartment);
        depAdapter = new DepartmentAdapter(getContext(), depList);
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