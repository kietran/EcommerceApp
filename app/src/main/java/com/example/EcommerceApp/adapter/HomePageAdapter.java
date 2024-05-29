package com.example.EcommerceApp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.EcommerceApp.HomeCategoryFragment;
import com.example.EcommerceApp.HomeHomeFragment;

public class HomePageAdapter extends FragmentStateAdapter {

    public HomePageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeHomeFragment();
            case 1:
                return new HomeCategoryFragment();
        }
        return new HomeHomeFragment(); // Trả về Fragment mặc định nếu position không hợp lệ
    }

    @Override
    public int getItemCount() {
        return 2; // Số lượng tab
    }
}
