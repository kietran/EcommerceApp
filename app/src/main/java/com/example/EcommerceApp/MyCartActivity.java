package com.example.EcommerceApp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MyCartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);


        if (savedInstanceState == null) {
            MyCartFragment myCartFragment = MyCartFragment.newInstance("fromDetail", "");

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.cart_container, myCartFragment)
                    .commit();
        }
    }
}