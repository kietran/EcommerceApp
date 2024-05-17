package com.example.EcommerceApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.EcommerceApp.adapter.ProductAdapter;
import com.example.EcommerceApp.domain.user.ProductRepository;
import com.example.EcommerceApp.model.Product;

import java.util.ArrayList;
import java.util.List;

public class AllArrivalActivity extends AppCompatActivity {

    ProductAdapter productAdapter;
    androidx.recyclerview.widget.RecyclerView viewArrifals;
    ProductRepository productRepository;

    ImageView btBack;

    public AllArrivalActivity() {
        productRepository = new ProductRepository(this);
        productAdapter = new ProductAdapter(this, new ArrayList<>());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seeall_arrival);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        viewArrifals = findViewById(R.id.viewArrivals);
        productRepository.getAllProductsAsList().addOnCompleteListener(task -> {
            List<Product> products = task.getResult();
            productAdapter.updateData(products);
            GridLayoutManager layoutManagerProduct = new GridLayoutManager(this, 2);
            viewArrifals.setLayoutManager(layoutManagerProduct);
            viewArrifals.setAdapter(productAdapter);
        });
        btBack = findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AllArrivalActivity.this, UserActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}
