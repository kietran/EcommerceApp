package com.example.EcommerceApp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.EcommerceApp.adapter.ProductAdapter;
import com.example.EcommerceApp.domain.user.ProductRepository;
import com.example.EcommerceApp.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ShopOverviewFragment extends Fragment {
    ProductAdapter productAdapter;
    androidx.recyclerview.widget.RecyclerView featuredProducts;
    ProductRepository productRepository;

    public ShopOverviewFragment() {
        productRepository = new ProductRepository(getContext());
        productAdapter = new ProductAdapter(getContext(), new ArrayList<>());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_overview, container, false);
        String shopId = getArguments().getString("shopId");

        featuredProducts = view.findViewById(R.id.featured_products);
        productRepository.get4ProductsAsListByShop(shopId).addOnCompleteListener(task -> {
            List<Product> products = task.getResult();
            productAdapter.updateData(products);
            GridLayoutManager layoutManagerProduct = new GridLayoutManager(getContext(), 2);
            featuredProducts.setLayoutManager(layoutManagerProduct);
            featuredProducts.setAdapter(productAdapter);
        });
        return view;
    }
}