package com.example.EcommerceApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.EcommerceApp.model.Product;
import com.squareup.picasso.Picasso;

public class DetailBagElectronicActivity extends AppCompatActivity {
    ImageView btBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag_electronic_detail);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            return;
        }
        Product product = (Product) bundle.get("object_product");
        TextView product_name = findViewById(R.id.product_name);
        TextView product_description = findViewById(R.id.product_description);
        TextView price = findViewById(R.id.price);
        ImageView product_image = findViewById(R.id.product_image);

        product_name.setText(product.getName());
        product_description.setText(product.getDescription());
        price.setText(product.getPrice()+"");
        Picasso.get().load(product.getProduct_image())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(product_image);
        btBack = findViewById(R.id.btnBackDetail);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailBagElectronicActivity.this, UserActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}