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
import com.example.EcommerceApp.domain.user.FavoriteRepository;
import com.example.EcommerceApp.domain.user.ProductRepository;
import com.example.EcommerceApp.model.Favorite;
import com.example.EcommerceApp.model.Product;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class AllFavoriteActivity extends AppCompatActivity {
    ProductAdapter productAdapter;
    androidx.recyclerview.widget.RecyclerView viewFavorites;
    ProductRepository productRepository;
    FavoriteRepository favoriteRepository;
    ImageView btBack;
    public AllFavoriteActivity() {
        productRepository = new ProductRepository(this);
        productAdapter = new ProductAdapter(this, new ArrayList<>());
        favoriteRepository = new FavoriteRepository(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_favorite);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        viewFavorites = findViewById(R.id.viewFavorites);
        favoriteRepository.getAllFavoriteAsListByUserID(FirebaseAuth.getInstance().getCurrentUser().getUid()).addOnCompleteListener(task -> {
            List<String> favorites = task.getResult();
            List<Product> products = new ArrayList<>();
            productAdapter.setFavoriteProductID(favorites);
            for (String favorite : favorites) {
                String productId = favorite;

                productRepository.getProductByProductId(productId).addOnCompleteListener(task1 -> {
                    Product product = task1.getResult();
                    product.setFavorite(true);
                    products.add(product);
                    productAdapter.notifyDataSetChanged();

                });
            }
            productAdapter.updateData(products);
            GridLayoutManager layoutManagerProduct = new GridLayoutManager(this, 2);
            viewFavorites.setLayoutManager(layoutManagerProduct);
            viewFavorites.setAdapter(productAdapter);
        });
        btBack = findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AllFavoriteActivity.this, UserActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}