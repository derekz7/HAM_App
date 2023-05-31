package com.example.ham_app.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ham_app.R;
import com.example.ham_app.activities.LoadingDialog;
import com.example.ham_app.activities.LoginActivity;
import com.example.ham_app.adapters.DepartmentAdapter;
import com.example.ham_app.api.ApiService;
import com.example.ham_app.models.Department;
import com.example.ham_app.models.News;
import com.example.ham_app.models.User;
import com.example.ham_app.untils.ApiDataManager;
import com.example.ham_app.untils.Common;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
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
    private List<Department> depList;
    private RecyclerView recycleViewDepartment;
    private DepartmentAdapter depAdapter;
    private LoadingDialog loadingDialog;
    private CircleImageView userImg;
    private TextView tv_Username;
    private User user;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setLayout();
        if (ApiDataManager.getInstance().getDepartmentList() == null){
            getData();
        }else {
            loadData();
        }

    }


    private void loadData() {
        loadingDialog.show();
        Toast.makeText(getContext(), "Load Data", Toast.LENGTH_SHORT).show();
        depList = ApiDataManager.getInstance().getDepartmentList();
        depAdapter.setData(depList);
        Toast.makeText(getContext(), depAdapter.getItemCount()+"", Toast.LENGTH_SHORT).show();
        tv_Username.setText(ApiDataManager.getInstance().getUser().getFullName());
        Picasso.get().load(ApiDataManager.getInstance().getUser().getImgUrl()).into(userImg);
        newsList = ApiDataManager.getInstance().getNewsList();
        for (News news : newsList) {
            slideModels.add(new SlideModel(news.getImgUrl(), ScaleTypes.CENTER_CROP));
        }
        imageSlider.setImageList(slideModels);
        loadingDialog.dismissDialog();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getData() {
        Toast.makeText(getContext(), "Get Data", Toast.LENGTH_SHORT).show();
        loadingDialog.show();
        ApiService.api.getAllDepartment().enqueue(new Callback<List<Department>>() {
            @Override
            public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                if (response.body() != null) {
                    ApiDataManager.getInstance().setDepartmentList(response.body());
                    depList.addAll(response.body());
                    loadingDialog.dismissDialog();
                    depAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Department>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismissDialog();
            }
        });

        ApiService.api.getAllNews().enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                if (response.body() != null) {
                    ApiDataManager.getInstance().setNewsList(response.body());
                    newsList.addAll(response.body());
                    for (News news : response.body()) {
                        slideModels.add(new SlideModel(news.getImgUrl(), ScaleTypes.CENTER_CROP));
                    }
                    imageSlider.setImageList(slideModels);

                }
            }
            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {

            }
        });
        ApiService.api.getUserByUsername(user.getUsername()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() != null){
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
        loadingDialog = new LoadingDialog(getContext());
        depAdapter = new DepartmentAdapter(getContext(), depList);
        tv_Username = view.findViewById(R.id.tv_userName);
        userImg = view.findViewById(R.id.profile_image);
        sharedPreferences = this.getActivity().getSharedPreferences("dataLogin", MODE_PRIVATE);
        user.setUsername(sharedPreferences.getString("username", ""));
    }
}