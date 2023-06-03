package com.example.ham_app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ham_app.R;
import com.example.ham_app.api.ApiService;
import com.example.ham_app.databinding.ActivityMainBinding;
import com.example.ham_app.dialog.LoadingDialog;
import com.example.ham_app.fragments.AccountFragment;
import com.example.ham_app.fragments.AppointmentFragment;
import com.example.ham_app.fragments.HomeFragment;
import com.example.ham_app.fragments.ProfileFragment;
import com.example.ham_app.modules.Patient;
import com.example.ham_app.modules.Service;
import com.example.ham_app.modules.User;
import com.example.ham_app.untils.ApiDataManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            final int itemId = item.getItemId();
            if (itemId == R.id.trangchu) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.appointment) {
                replaceFragment(new AppointmentFragment());
            } else if (itemId == R.id.profile) {
                replaceFragment(new ProfileFragment());
            } else if (itemId == R.id.account) {
                replaceFragment(new AccountFragment());
            }
            return true;
        });
        ApiService.api.getAllServices().enqueue(new Callback<List<Service>>() {
            @Override
            public void onResponse(Call<List<Service>> call, Response<List<Service>> response) {
                if (response.body() != null) {
                    ApiDataManager.getInstance().setServiceList(response.body());
                    Toast.makeText(getApplicationContext(), "Load Services success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Service>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Load services fail" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {

    }
}