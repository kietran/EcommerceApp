package com.example.EcommerceApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;




public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation_view);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentuser_container, new HomeUser()).commit();

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                int itemId = item.getItemId();

                if (itemId == R.id.navigation_home)
                    selectedFragment = new HomeUser();
                else if (itemId == R.id.navigation_myorder)
                    selectedFragment = new MyOrder();
                else if (itemId == R.id.navigation_favorite)
                    selectedFragment = new Favorite();
                else if (itemId == R.id.navigation_myprofile)
                    selectedFragment = new UserProfile();


                if (selectedFragment != null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentuser_container, selectedFragment).commitAllowingStateLoss();

                return true;
            }
        });








    }
}