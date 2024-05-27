package com.example.EcommerceApp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.EcommerceApp.MyOrderHistoryFragment;
import com.example.EcommerceApp.MyOrderOrderFragment;

public class OrderPageAdapter extends FragmentStateAdapter {
    public OrderPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MyOrderOrderFragment();
            case 1:
                return new MyOrderHistoryFragment();
        }
        return new MyOrderOrderFragment(); // Trả về Fragment mặc định nếu position không hợp lệ
    }

    @Override
    public int getItemCount() {
        return 2; // Số lượng tab
    }
}
