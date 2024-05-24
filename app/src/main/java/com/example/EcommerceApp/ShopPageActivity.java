package com.example.EcommerceApp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.EcommerceApp.adapter.ShopPageAdapter;
import com.example.EcommerceApp.model.Shop;
import com.example.EcommerceApp.model.User;
import com.example.EcommerceApp.utils.AndroidUtil;
import com.example.EcommerceApp.utils.FirebaseUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ShopPageActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ImageView banner;
    private ImageView logo;
    private Shop shopModel;
    private TextView shopName;
    private TextView shopInfo;
    static Context context;

    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shop_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        context = this;
        // Get the Intent that started this activity
        Intent intent = getIntent();
        // Get the shopID from the intent extras
        String shopId = intent.getStringExtra("shopID");
        getShopData(shopId);

        banner = findViewById(R.id.banner_iv);
        logo = findViewById(R.id.logo_iv);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        shopName = findViewById(R.id.shopName_tv);
        shopInfo = findViewById(R.id.shopInfo_tv);
        btnBack = findViewById(R.id.btBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Tạo adapter và thiết lập cho ViewPager2
        ShopPageAdapter adapter = new ShopPageAdapter(this);
        adapter.setShopId(shopId);
        viewPager.setAdapter(adapter);

        // Kết nối ViewPager2 với TabLayout
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    if (position == 0) {
                        tab.setText("Overview");
                    } else if (position == 1) {
                        tab.setText("All Products");
                    }
                }
        ).attach();
    }

    void getShopData(String shopId){
        FirebaseUtil.getCurrentShopPicStorageRef("banner_shop_pic", shopId).getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Uri uri  = task.getResult();
                        AndroidUtil.setShopProfilePic(this,uri,banner);
                    }
                });

        FirebaseUtil.getCurrentShopPicStorageRef("logo_shop_pic", shopId).getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Uri uri  = task.getResult();
                        AndroidUtil.setShopProfilePic(this,uri,logo);
                    }
                });

        FirebaseUtil.getShopReference(shopId).get().addOnCompleteListener(task -> {
            shopModel = task.getResult().toObject(Shop.class);
            shopName.setText(shopModel.getShopName());
            shopInfo.setText("5 Products");
        });
    }
}