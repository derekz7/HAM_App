package com.example.ham_app.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ham_app.R;
import com.example.ham_app.databinding.ActivityMainBinding;
import com.example.ham_app.fragments.AccountFragment;
import com.example.ham_app.fragments.AppointmentFragment;
import com.example.ham_app.fragments.HomeFragment;
import com.example.ham_app.fragments.ProfileFragment;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String fragment = getIntent().getStringExtra("FRAGMENT_TAG");
        if (fragment != null && fragment.equals("appointment")) {
            replaceFragment(new AppointmentFragment());
            binding.bottomNavigationView.setSelectedItemId(R.id.appointment);
        } else {
            replaceFragment(new HomeFragment());
        }
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