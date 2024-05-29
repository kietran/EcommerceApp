package com.example.EcommerceApp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.EcommerceApp.OrderHistoryFragment;
import com.example.EcommerceApp.OrderOrderFragment;

public class OrderPageAdapter extends FragmentStateAdapter {
    public OrderPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new OrderOrderFragment();
            case 1:
                return new OrderHistoryFragment();
        }
        return new OrderOrderFragment(); // Trả về Fragment mặc định nếu position không hợp lệ
    }

    @Override
    public int getItemCount() {
        return 2; // Số lượng tab
    }
}
