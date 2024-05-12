package com.example.EcommerceApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;




public class UserActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation_view);
        HomeUserFragment homeUser = new HomeUserFragment();
        MyCart myCart = new MyCart();
        MyOrder myOrder = new MyOrder();
        UserProfileFragment userProfile = new UserProfileFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.user_container, homeUser).commit();


        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                int itemId = item.getItemId();

                if (itemId == R.id.navigation_home)
                    selectedFragment = homeUser;
                else if (itemId == R.id.navigation_mycart)
                    selectedFragment = myCart;
                else if (itemId == R.id.navigation_myorder)
                    selectedFragment = myOrder;
                else if (itemId == R.id.navigation_myprofile)
                    selectedFragment = userProfile;


                if (selectedFragment != null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.user_container, selectedFragment).commitAllowingStateLoss();

                return true;
            }
        });








    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4) {
            if (resultCode == Activity.RESULT_OK) {
                bottomNavigationView.setSelectedItemId(R.id.navigation_mycart);
            }
        }
    }
}