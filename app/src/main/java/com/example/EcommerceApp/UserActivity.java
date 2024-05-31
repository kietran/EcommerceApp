package com.example.EcommerceApp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.EcommerceApp.adapter.CartItemAdapter;
import com.example.EcommerceApp.databinding.ActivityUserBinding;
import com.example.EcommerceApp.utils.CartNumberUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


public class UserActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    ActivityUserBinding binding;

    @Override
    protected void onResume() {
        super.onResume();
        CartNumberUtil.getCartNumber();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        boolean isMyorder=false;
        if(intent!=null)
            isMyorder= intent.getBooleanExtra("order", false);

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation_view);
        HomeUserFragment homeUser = new HomeUserFragment();
        MyCartFragment myCartFragment = MyCartFragment.newInstance("","");
        MyOrder myOrder = new MyOrder();
        UserProfileFragment userProfile = new UserProfileFragment();


        CartNumberUtil.setBadge_mycart(binding.bottomNavigationView.getOrCreateBadge(R.id.navigation_mycart));
        CartNumberUtil.getBadge_mycart().setBackgroundColor(Color.RED);
        CartNumberUtil.getBadge_mycart().setBadgeTextColor(Color.WHITE);
        CartNumberUtil.getBadge_mycart().setMaxCharacterCount(4);
        CartNumberUtil cartNumberUtil = new CartNumberUtil();
        CartNumberUtil.getCartNumber();
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                int itemId = item.getItemId();

                if (itemId == R.id.navigation_home)
                    selectedFragment = homeUser;
                else if (itemId == R.id.navigation_mycart)
                    selectedFragment = myCartFragment;
                else if (itemId == R.id.navigation_myorder)
                    selectedFragment = myOrder;
                else if (itemId == R.id.navigation_myprofile)
                    selectedFragment = userProfile;

                if(selectedFragment!=myCartFragment)
                    CartItemAdapter.selectList=new ArrayList<>();

                if (selectedFragment != null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.user_container, selectedFragment).commitAllowingStateLoss();

                return true;
            }
        });



        if(isMyorder) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_myorder);
            getSupportFragmentManager().beginTransaction().replace(R.id.user_container, myOrder).commit();
        }
        else {
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
            getSupportFragmentManager().beginTransaction().replace(R.id.user_container, homeUser).commit();
        }





    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4) {
            if (resultCode == Activity.RESULT_OK) {
                bottomNavigationView.setSelectedItemId(R.id.navigation_mycart);

                CartNumberUtil.getCartNumber();

                getSupportFragmentManager().beginTransaction().replace(R.id.user_container, new MyCartFragment()).commit();

            }
        }
    }
    protected void onDestroy(){
        super.onDestroy();
        binding = null;
    }

}