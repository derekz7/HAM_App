package com.example.ham_app.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ham_app.R;
import com.example.ham_app.activities.LoadingDialog;
import com.example.ham_app.adapters.DepartmentAdapter;
import com.example.ham_app.api.ApiService;
import com.example.ham_app.models.Department;
import com.example.ham_app.models.News;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    private ImageSlider imageSlider;
    private List<SlideModel> slideModels;
    private List<Department> depList;
    private RecyclerView recycleViewDepartment;
    private DepartmentAdapter depAdapter;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setLayout();
        getData();
    }

    private void getData() {
        imageSlider.setVisibility(View.VISIBLE);
        loadingDialog.show();

        ApiService.api.getAllDepartment().enqueue(new Callback<List<Department>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                if (response.body() != null){
                    depList.clear();
                    depList.addAll(response.body());
                    depAdapter.notifyDataSetChanged();
                    loadingDialog.dismissDialog();
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
                    slideModels.clear();
                    for (News news : response.body()) {
                        SlideModel slide = new SlideModel(news.getImgUrl(), ScaleTypes.CENTER_CROP);
                        slide.setTitle(news.getTitle());
                        slideModels.add(slide);
                    }
                    imageSlider.setImageList(slideModels);
                }
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLayout() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),4);
        recycleViewDepartment.setLayoutManager(layoutManager);
        recycleViewDepartment.setNestedScrollingEnabled(false);
    }

    private void init(View view) {
        imageSlider = view.findViewById(R.id.image_slider);
        slideModels = new ArrayList<>();
        depList = new ArrayList<>();
        recycleViewDepartment = view.findViewById(R.id.recyleViewDepartment);
        loadingDialog = new LoadingDialog(getContext());
        depAdapter = new DepartmentAdapter(getContext(),depList);
        recycleViewDepartment.setAdapter(depAdapter);
    }
}