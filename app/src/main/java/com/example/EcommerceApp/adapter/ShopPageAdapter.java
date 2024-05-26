package com.example.EcommerceApp.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.EcommerceApp.ShopOverviewFragment;
import com.example.EcommerceApp.ShopAllProductsFragment;

public class ShopPageAdapter extends FragmentStateAdapter {
    private String shopId;

    public ShopPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                Fragment fragment0 = new ShopOverviewFragment();
                fragment0.setArguments(createArguments());
                return fragment0;
            case 1:
                Fragment fragment1 = new ShopAllProductsFragment();
                fragment1.setArguments(createArguments());
                return fragment1;
        }
        Fragment fragment0 = new ShopOverviewFragment();
        fragment0.setArguments(createArguments());
        return fragment0; // Trả về Fragment mặc định nếu position không hợp lệ
    }

    @Override
    public int getItemCount() {
        return 2; // Số lượng tab
    }

    private Bundle createArguments() {
        Bundle args = new Bundle();
        args.putString("shopId", shopId);
        return args;
    }
}
