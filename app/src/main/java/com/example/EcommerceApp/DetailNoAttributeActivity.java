package com.example.EcommerceApp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.EcommerceApp.model.Product;
import com.squareup.picasso.Picasso;

public class DetailNoAttributeActivity extends AppCompatActivity {
    ImageView btBack;
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_attribute_detail);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            return;
        }
        Product product = (Product) bundle.get("object_product");
        TextView product_name = findViewById(R.id.product_name);
        TextView product_description = findViewById(R.id.product_description);
        TextView price = findViewById(R.id.price);
        ImageView product_image = findViewById(R.id.product_image);
        TextView rate = findViewById(R.id.star);
        TextView review = findViewById(R.id.review);

        if (product.getRating() != 0 && product.getNumberOfRatings() != 0){
            float rating = (product.getRating()/product.getNumberOfRatings());
            String formattedRating = String.format("%.1f", rating);
            rate.setText(formattedRating);
            review.setText("(" + String.format("%d", product.getNumberOfRatings()) + " Reviews)");
        }else{
            rate.setText("0");
            review.setText("(0 Review)");
        }
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
                finish();
            }
        });
    }
}