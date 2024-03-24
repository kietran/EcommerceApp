package com.example.EcommerceApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class SellerActivity extends AppCompatActivity {
    Button btSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        btSwitch = (Button)findViewById(R.id.btSwitchTU);
        btSwitch.setOnClickListener(view -> {
            onClickSwitch();
        });
    }

    private void onClickSwitch() {
        startActivity(new Intent(SellerActivity.this, UserActivity.class));
    }
}